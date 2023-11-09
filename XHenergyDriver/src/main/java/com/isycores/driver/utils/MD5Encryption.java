package com.isycores.driver.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.isycores.driver.utils.DateUtils.*;
import static com.isycores.driver.utils.DateUtils.getEndOfMonth;
import static com.isycores.driver.utils.HttpClientUtil.*;


public class MD5Encryption {
    private static final char[] HEX_CHAR = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5Encryption() {
    }
    public static String getSign(Map<String, Object> params) {
        Map<String, String> sortedMap = new TreeMap(params);
        Set<Map.Entry<String, String>> entrySet = sortedMap.entrySet();
        StringBuilder stringA = new StringBuilder();
        Iterator var5 = entrySet.iterator();

        while(var5.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var5.next();
            String key = (String)entry.getKey();
            if (key != null && !key.equals("sign")) {
                stringA.append(key).append("=").append((String)entry.getValue()).append("&");
            }
        }

        StringBuilder stringSignTemp = stringA.append("wlyb@00001!");
        String signValue = null;

        try {
            signValue = encrypt(stringSignTemp.toString()).toUpperCase();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return signValue;
    }
    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

   /* public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap();
        JSONObject json = JSONObject.fromObject(jsonStr);
        Iterator var3 = json.keySet().iterator();

        while(true) {
            while(var3.hasNext()) {
                Object k = var3.next();
                Object v = json.get(k);
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList();
                    Iterator it = ((JSONArray)v).iterator();

                    while(it.hasNext()) {
                        JSONObject json2 = (JSONObject)it.next();
                        list.add(parseJSON2Map(json2.toString()));
                    }

                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }

            return map;
        }
    }*/
   public static String convertMapToString(Map<String, Object> map) {
       JSONObject jsonObject = new JSONObject(map);
       return jsonObject.toString();
   }
    public static void main(String[] args) {
       StringBuffer stringBuffer = new StringBuffer();
       String b = "0021000";
        for (int i = 1; i <= 2; i++) {
            stringBuffer.append("\'");
            stringBuffer.append(b+i);
            stringBuffer.append("\'");
            stringBuffer.append(",");
        }
        System.out.println(stringBuffer);
       /* String enterTimeStrLeft = getStartTime(date2String(getStartOfMonth(new Date())));
        String enterTimeStrRight = getEndTime(date2String(getEndOfMonth(new Date())));
        LinkedHashMap body = new LinkedHashMap<>();
        body.put("startTime",enterTimeStrLeft);
        body.put("endTime",enterTimeStrRight);
        body.put("zhnum","00020002");
        body.put("timestamp","123456");
        String str = getSign(body).toUpperCase();
        body.put("sign",str);
        try {
            System.out.println(body);
            String ss = httpPost2Json("http://172.18.127.81:81/api/GetJfData",body);
//            Map<String, Object> maps = json2map();
            System.out.println(ss);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


/*
        String enterTimeStrLeft = getStartTime(date2String(getStartOfMonth(new Date())));
        String enterTimeStrRight = getEndTime(date2String(getEndOfMonth(new Date())));
        LinkedHashMap body = new LinkedHashMap<>();
        body.put("startTime",enterTimeStrLeft);
        body.put("endTime",enterTimeStrRight);
        body.put("zhnum","00110002");
        body.put("timestamp","123456");
        String str = getSign(body).toUpperCase();
        body.put("sign",str);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");

        String ss=convertMapToString(body);
        System.out.println(ss);

        try {
            String resp = sendPost("http://172.18.127.81:81/api/GetChildNode",ss,header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }
}