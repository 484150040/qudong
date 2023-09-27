package com.isycores.driver.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author vconinfo
 * @createdOn 2019/8/19
 * @description: http工具类
 */
public class HttpClientUtil {
    private static String UTF_8 = "UTF-8";


  public static String replaceBlank(String str) {
    String dest = "";
    if (str != null) {
      Pattern p = Pattern.compile("\\s*|\t|\r|\n");
      Matcher m = p.matcher(str);
      dest = m.replaceAll("");
    }
    return dest;
  }

  public static Map<String, Object> objectToMap(Object object){
    Map<String,Object> dataMap = new HashMap<>();
    Class<?> clazz = object.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      try {
        field.setAccessible(true);
        dataMap.put(field.getName(),field.get(object));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return dataMap;
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
     * 封装请求头
     */
    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获得响应结果
     */
    public static String getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 执行请求
        httpResponse = httpClient.execute(httpMethod);

        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), UTF_8);
            }
            return content;
        }

        return "";
    }
    /**
     * 释放资源
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }


    /**
     * url: 请求地址
     * param： 请求参数 [{'key1':'value1','key2':'value2'},{'key1':'value3','key2':'value4'}]
     * header 设置header
     *
     */
    public static String sendPost(String url, String param, Map<String, String> header) throws UnsupportedEncodingException, IOException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection conn = realUrl.openConnection();
        //设置超时时间
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);
        // 设置通用的请求属性
        if (header!=null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 获取URLConnection对象对应的输出流
        out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        out.print(param);
        // flush输出流的缓冲
        out.flush();
        // 定义BufferedReader输入流来读取URL的响应
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "utf8"));
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if(out!=null){
            out.close();
        }
        if(in!=null){
            in.close();
        }
        return result;
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

    @SneakyThrows
    public static void main(String[] args) {
        String parm = "{\n" +
                "    \"devId\": \"1001110\",\n" +
                "    \"A10-MJ-3F-104.32--机房305\": 20,\n" +
                "    \"direction\": \"进门\",\n" +
                "    \"openType\": \"人脸合法开门\",\n" +
                "    \"time\": \"1693032657\",\n" +
                "    \"personName\": \"胡起\",\n" +
                "    \"paperNumber\": \"50022219945254625\",\n" +
                "    \"personImage\": \"http://172.17.1.2:8927/354a0a09-da19-11ed-815c-e8611f43cf1e/20230822/1/dsf_6bff8ece-40cd-11ee-95d1-e8611f43cf1e_13340098_13350674.jpg\",\n" +
                "    \"recordImage\": \"http://172.17.1.2:8927/354a0a09-da19-11ed-815c-e8611f43cf1e/20230822/1/dsf_6bff8ece-40cd-11ee-95d1-e8611f43cf1e_13340098_13350674.jpg\"\n" +
                "}";
        sendPost("http://localhost:8181/cockpit/access",parm,new HashMap<>());
    }
}
