package com.isycores.driver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.isycores.driver.utils.HttpClientUtil.json2map;

/**
 * @author vconinfo
 * @createdOn 2019/8/19
 * @description: http工具类
 */
@Slf4j
public class HttpClientUtil {


  public static String replaceBlank(String str) {
    String dest = "";
    if (str != null) {
      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
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
    public static String httpPost2Json(String url, Map<String, Object> parameters) throws UnsupportedEncodingException {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).build();
        ObjectMapper objectMapper = new ObjectMapper();
        MediaType mediaType = MediaType.parse("application/json");
        String param;
        try {
            param = objectMapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e1) {
            return "";
        }
        RequestBody body = RequestBody.create(mediaType, param);
        Request request =
                new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json")
                        .addHeader("cache-control", "no-cache").build();

        Response response;
        try {
            response = client.newCall(request).execute();
            int code = response.code();
            String str = response.body().string();
            str = replaceBlank(str);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 发送GET请求并获取响应结果
     * @param url 请求的URL地址
     * @return 响应结果字符串
     */
    public static String sendGet(String url, Map<String, String> header) {
        StringBuilder result = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            if (header!=null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
