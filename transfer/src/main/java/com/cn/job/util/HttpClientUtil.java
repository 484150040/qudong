package com.cn.job.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vconinfo
 * @createdOn 2019/8/19
 * @description: http工具类
 */
@Slf4j
public class HttpClientUtil {
    private static PoolingHttpClientConnectionManager pcm;
    private static String EMPTY_STR = "";
    private static String UTF_8 = "UTF-8";
    /**
     * 连接池最大连接数
     */
    private static int MAX_TOTAL = 300;
    /**
     * 每路由最大连接数，默认是2
     */
    private static int MAX_PER_ROUTE = 5;
    /**
     * 连接超时时间 单位毫秒
     */
    private static int CONN_TIME_OUT = 60000;
    /**
     * 从connect Manager获取connection超时时间 单位毫秒
     */
    private static int reqTimeout = 5000;

    private static void init() {
        if (pcm == null) {
            pcm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            pcm.setMaxTotal(MAX_TOTAL);
            // 每路由最大连接数，默认值是2
            pcm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        }
    }
    /**
     * 通过连接池获取HttpClient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        init();
        return HttpClients.custom().setConnectionManager(pcm).build();
    }


    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 处理Http请求
     */
    private static String getResult(HttpRequestBase request) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        try {

            CloseableHttpResponse response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity);
                response.close();
                // httpClient.close();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EMPTY_STR;
    }

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

    public static String doHttpGet(String url, Map<String,String> headParams,Map<String,String> params) {
        String result = null;
        //1.获取httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //接口返回结果
        CloseableHttpResponse response = null;
        String paramStr = null;
        try {
            List<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();

            for (String key : params.keySet()) {
                paramsList.add(new BasicNameValuePair(key,params.get(key)));
            }
            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(paramsList));
            //拼接参数
            StringBuffer sb = new StringBuffer();
            sb.append(url);
            sb.append("?");
            sb.append(paramStr);

            //2.创建get请求
            HttpGet httpGet = new HttpGet(sb.toString());
            //3.设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            /*此处可以添加一些请求头信息，例如：
            httpGet.addHeader("content-type","text/xml");*/
            for (String head : headParams.keySet()) {
                httpGet.addHeader(head,headParams.get(head));
            }
            //4.提交参数
            response = httpClient.execute(httpGet);
            //5.得到响应信息
            int statusCode = response.getStatusLine().getStatusCode();
            //6.判断响应信息是否正确
            if (HttpStatus.SC_OK != statusCode) {
                //终止并抛出异常
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            //7.转换成实体类
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                result = EntityUtils.toString(entity);
            }
            EntityUtils.consume(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //8.关闭所有资源连接
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }



}
