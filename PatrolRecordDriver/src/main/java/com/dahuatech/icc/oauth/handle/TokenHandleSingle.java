//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dahuatech.icc.oauth.handle;

import com.dahuatech.hutool.core.thread.NamedThreadFactory;
import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.hutool.log.Log;
import com.dahuatech.hutool.log.LogFactory;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.exception.ServerException;
import com.dahuatech.icc.oauth.constant.OauthConstant;
import com.dahuatech.icc.oauth.constant.OauthConstant.ClientType;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.http.IccHttpHttpRequest;
import com.dahuatech.icc.oauth.http.IccTokenResponse;
import com.dahuatech.icc.oauth.http.IccTokenResponse.IccToken;
import com.dahuatech.icc.oauth.model.v202010.BrmKeepAliveRequest;
import com.dahuatech.icc.oauth.model.v202010.BrmKeepAliveResponse;
import com.dahuatech.icc.oauth.model.v202010.OauthPublicKeyResponse;
import com.dahuatech.icc.oauth.model.v202010.OauthRefreshTokenRequest;
import com.dahuatech.icc.oauth.model.v202010.OauthRefreshTokenResponse;
import com.dahuatech.icc.oauth.model.v202010.OauthRefreshTokenResponse.IccReFreshToken;
import com.dahuatech.icc.oauth.profile.GrantType;
import com.dahuatech.icc.oauth.profile.IccProfile;
import com.dahuatech.icc.util.BeanUtil;
import com.dahuatech.icc.util.SignUtil;
import io.netty.handler.codec.http.HttpHeaderNames;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenHandleSingle {
    private static final Log logger = LogFactory.get();
    private static final AtomicBoolean TOKEN_INITED;
    private static final long FRESH_TOKEN_INTERVAL = 30000L;
    private final ScheduledExecutorService REFRESH_TOKEN_SCHEDULED;
    private final Map<String, IccToken> tokenMap;

    public Map<String, IccToken> getTokenMap() {
        return this.tokenMap;
    }

    private TokenHandleSingle() {
        this.REFRESH_TOKEN_SCHEDULED = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Icc-Refresh-Token", true));
        this.tokenMap = new ConcurrentHashMap();
        this.REFRESH_TOKEN_SCHEDULED.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    TokenHandleSingle.this.refreshTokenAndKeepAlive();
                } catch (Throwable var2) {
                    TokenHandleSingle.logger.error("Unexpected error occur at token refresh, cause: " + var2.getMessage(), new Object[]{var2});
                }

            }
        }, 30000L, 30000L, TimeUnit.MILLISECONDS);
    }

    public static synchronized TokenHandleSingle getInstance() {
        return TokenHandleSingle.SingletonHolder.INSTANCE;
    }

    public synchronized IccToken refreshToken(GrantType grantType) {
        IccToken token = null;

        try {
            switch(grantType) {
                case password:
                    token = this.password();
                    break;
                case client_credentials:
                    token = this.clientCredentials();
            }
        } catch (ClientException var4) {
            logger.error("get token failure", new Object[0]);
        }

        if (!TOKEN_INITED.get() && token != null) {
            TOKEN_INITED.set(Boolean.TRUE);
        }

        return token;
    }

    private IccToken password() throws ClientException, ServerException {
        IccHttpHttpRequest pubRequest = new IccHttpHttpRequest(OauthConstant.url("/evo-apigw/evo-oauth/%s/oauth/public-key"), Method.GET);
        String pubBody = pubRequest.execute();
        OauthPublicKeyResponse keyResp = (OauthPublicKeyResponse)BeanUtil.toBean(pubBody, OauthPublicKeyResponse.class);
        if (keyResp.getData() == null) {
            logger.error("get public key faiure [{}]", new Object[]{"/evo-apigw/evo-oauth/%s/oauth/public-key"});
            throw new ServerException("get public key faiure");
        } else {
            Map<String, Object> map = new HashMap();
            map.put("grant_type", "password");
            map.put("username", IccProfile.username);
            map.put("password", SignUtil.encryptRSANew(IccProfile.password, keyResp.getData().getPublicKey()));
            map.put("client_id", IccProfile.pwdClientId);
            map.put("client_secret", IccProfile.pwdClientSecret);
            map.put("public_key", keyResp.getData().getPublicKey());
            IccHttpHttpRequest pr = new IccHttpHttpRequest(OauthConstant.url("/evo-apigw/evo-oauth/%s/oauth/extend/token"), Method.POST, JSONUtil.toJsonStr(map));
            String prBody = pr.execute();


            IccTokenResponse token = (IccTokenResponse)BeanUtil.toBean(prBody, IccTokenResponse.class);
            if (token != null && token.isSuccess()) {
                IccToken iccToken = token.getData();
                iccToken.setTtl(System.currentTimeMillis() + iccToken.getExpires_in() * 1000L);
                this.tokenMap.put(this.enGrantKeyName(GrantType.password.name()), iccToken);
                return iccToken;
            } else {
                logger.error(" auth failure [{}] reason [{}]", new Object[]{OauthConstant.url("/evo-apigw/evo-oauth/%s/oauth/extend/token"), token == null ? "" : token.getErrMsg()});
                throw new ClientException("GrantType [password] username=[" + IccProfile.username + "],password=[" + IccProfile.password + "] get access_token failure");
            }
        }
    }

    private IccToken clientCredentials() throws ClientException, ServerException {
        Map<String, Object> map = new HashMap();
        map.put("grant_type", "client_credentials");
        map.put("client_id", IccProfile.clientId);
        map.put("client_secret", IccProfile.clientSecret);
        IccHttpHttpRequest cr = new IccHttpHttpRequest(OauthConstant.url("/evo-apigw/evo-oauth/1.0.0/oauth/extend/token"), Method.POST,JSONUtil.toJsonStr(map));
        cr.getHeader().put(HttpHeaderNames.CONTENT_TYPE.toString(),"application/json");
        String crBody = cr.execute();
        logger.info("{}",crBody);
        IccTokenResponse token = (IccTokenResponse)BeanUtil.toBean(crBody, IccTokenResponse.class);
        if (token != null && token.isSuccess()) {
            IccToken iccToken = token.getData();
            iccToken.setTtl(System.currentTimeMillis() + iccToken.getExpires_in() * 1000L);
            this.tokenMap.put(this.enGrantKeyName(GrantType.client_credentials.name()), iccToken);
            return iccToken;
        } else {
            logger.error("client auth failure [{}] reason [{}]", new Object[]{OauthConstant.url("/evo-apigw/evo-oauth/1.0.0/oauth/extend/token"), token == null ? "" : token.getErrMsg()});
            throw new ClientException("client auth failure" + (token == null ? "" : token.getErrMsg()));
        }
    }

    public IccToken getTokenCache(String grantType) {
        return (IccToken)this.tokenMap.get(this.enGrantKeyName(grantType));
    }

    public IccToken refreshToken(GrantType grantType, String refreshToken) {
        try {
            IClient iClient = new DefaultClient();
            OauthRefreshTokenRequest refreshTokenRequest = new OauthRefreshTokenRequest();
            if (grantType.equals(GrantType.password)) {
                refreshTokenRequest.setClient_id(IccProfile.pwdClientId);
                refreshTokenRequest.setClient_secret(IccProfile.pwdClientSecret);
                refreshTokenRequest.setGrant_type(GrantType.refresh_token.name());
                refreshTokenRequest.setRefresh_token(refreshToken);
                OauthRefreshTokenResponse refreshTokenResponse = (OauthRefreshTokenResponse)iClient.doAction(refreshTokenRequest, refreshTokenRequest.getResponseClass());
                if (refreshTokenResponse.isSuccess()) {
                    IccReFreshToken freshToken = refreshTokenResponse.getData();
                    IccToken iccToken = new IccToken();
                    iccToken.setTtl(System.currentTimeMillis() + freshToken.getExpires_in() * 1000L);
                    iccToken.setAccess_token(freshToken.getAccess_token());
                    iccToken.setExpires_in(freshToken.getExpires_in());
                    iccToken.setMagicId(freshToken.getMagicId());
                    iccToken.setUserId(freshToken.getUserId());
                    iccToken.setToken_type(freshToken.getToken_type());
                    iccToken.setRefresh_token(freshToken.getRefresh_token());
                    iccToken.setScope(freshToken.getScope());
                    return iccToken;
                }
            }

            if (grantType.equals(GrantType.client_credentials)) {
                return this.clientCredentials();
            }
        } catch (ClientException var8) {
            logger.error("fresh token error , grantType=[{}],freshToken=[{}]", new Object[]{grantType.name(), refreshToken});
        }

        return null;
    }

    private void refreshTokenAndKeepAlive() {
        if (TOKEN_INITED.get() && this.tokenMap.size() > 0) {
            Iterator var1 = this.tokenMap.entrySet().iterator();

            while(var1.hasNext()) {
                Entry<String, IccToken> entry = (Entry)var1.next();
                IccToken token = (IccToken)entry.getValue();
                Long currentTime = System.currentTimeMillis();
                if (token.getTtl() - currentTime <= 120000L) {
                    GrantType grantType = this.deGrantType((String)entry.getKey());
                    token = this.refreshToken(grantType, token.getRefresh_token());
                    if (token != null) {
                        this.tokenMap.put(entry.getKey(), token);
                        logger.debug("refresh token success, [{}],token=[{}]", new Object[]{entry.getKey(), token});
                    }
                }
            }

            this.keepalive();
        }

    }

    private void keepalive() {
        try {
            IClient iClient = new DefaultClient();
            if (IccProfile.inited) {
                Iterator var2 = this.tokenMap.entrySet().iterator();

                while(var2.hasNext()) {
                    Entry<String, IccToken> entry = (Entry)var2.next();
                    if (GrantType.client_credentials != this.deGrantType((String)entry.getKey())) {
                        IccToken token = (IccToken)entry.getValue();
                        BrmKeepAliveRequest request = new BrmKeepAliveRequest();
                        request.setMagicId(token.getMagicId());
                        request.setClientType(ClientType.WEB.getClientType());
                        BrmKeepAliveResponse brmKeepAliveResponse = (BrmKeepAliveResponse)iClient.doAction(request, request.getResponseClass());
                        if (brmKeepAliveResponse.isSuccess()) {
                            logger.debug("evo-brm [{}] keeplive success", new Object[]{entry.getKey()});
                        } else if ("27001007".equalsIgnoreCase(brmKeepAliveResponse.getCode())) {
                            logger.info("[{}] token invalid , get new token", new Object[]{entry.getKey()});
                            this.tokenMap.remove(entry.getKey());
                            this.refreshToken(this.deGrantType((String)entry.getKey()));
                        }
                    }
                }
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            logger.error("keepalive error = {}", new Object[]{var7});
        }

    }

    private String enGrantKeyName(String grantType) {
        return grantType + ":" + IccProfile.host;
    }

    private GrantType deGrantType(String grantKeyName) {
        return GrantType.valueOf(grantKeyName.substring(0, grantKeyName.indexOf(":")));
    }

    static {
        TOKEN_INITED = new AtomicBoolean(Boolean.FALSE);
    }

    private static class SingletonHolder {
        private static final TokenHandleSingle INSTANCE = new TokenHandleSingle();

        private SingletonHolder() {
        }
    }
}
