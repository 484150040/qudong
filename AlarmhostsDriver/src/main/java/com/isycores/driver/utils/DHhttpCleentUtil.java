package com.isycores.driver.utils;

import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DHhttpCleentUtil {


    private static String host;
    private static String clientId;
    private static String clientSecret;
    /**
     * 大华post请求
     *
     * @param url
     * @param param
     * @return
     */
    @SneakyThrows
    public String post(String url, Map<String, Object> param, IClient iClient) throws ClientException {
        GeneralRequest generalRequest =
                new GeneralRequest(url, Method.POST);
        generalRequest.body(JSONUtil.toJsonStr(JSONUtil.parseObj(param)));
        GeneralResponse subscribeResponse =
                iClient.doAction(generalRequest, generalRequest.getResponseClass());
        return subscribeResponse.getResult();
    }

    /**
     * 大华get请求地址
     *
     * @param url
     * @param param
     * @return
     */
    @SneakyThrows
    public String get(String url, Map<String, Object> param, IClient iClient) throws ClientException {
        GeneralRequest generalRequest =
                new GeneralRequest(url, Method.GET);
        generalRequest.form(param);
        GeneralResponse subscribeResponse =
                iClient.doAction(generalRequest, generalRequest.getResponseClass());
        return subscribeResponse.getResult();
    }

    /**
     * 大华get请求地址
     *
     * @param url
     * @param param
     * @return
     */
    @SneakyThrows
    public String get(String url, String param, IClient iClient) throws ClientException {
        GeneralRequest generalRequest =
                new GeneralRequest(url, Method.GET);

        generalRequest.get(generalRequest.getUrl() + "/" + param);
        GeneralResponse subscribeResponse =
                iClient.doAction(generalRequest, generalRequest.getResponseClass());
        return subscribeResponse.getResult();
    }

    /**
     * String 转换数据
     * @param str_json
     * @return
     */
    public static Map<String, Object> json2map(String str_json) {
        Map<String, Object> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }
}
