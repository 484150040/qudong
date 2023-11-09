package com.cn.job.service;


import com.cn.job.enums.Urlenum;
import com.cn.job.util.HttpClientUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.cn.job.util.HttpClientUtil.json2map;


@Service
public class TransferServiceimpl {

    @SneakyThrows
    public Map<String, Object> addGetBuiltCollect(String token,Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetBuiltCollect;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
        return map;

    }

    @SneakyThrows
    public String login(String username, String password) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        Map<String, Object> params = new HashMap<>();
        params.put("username", "Hic1001001");
        params.put("password", "H123!@#$zh");
        params.put("grant_type", "password");
        String urls = Urlenum.url + Urlenum.token;
        String token = HttpClientUtil.httpPostRequest(urls, headers, params);
        Map<String, Object> map = json2map(token);
        if (CollectionUtils.isEmpty(map)){
            return "";
        }
        return (String) map.get("access_token");
    }

    @SneakyThrows
    public Map<String, Object> addGetEmployeeList(String token,Map<String,String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        String urls = Urlenum.url + Urlenum.GetEmployeeList;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
        return map;
    }

    @SneakyThrows
    public Map<String, Object> addGetOrgCollect(String token,Map<String,String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        String urls = Urlenum.url + Urlenum.GetOrgCollect;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
       return map;
    }

    @SneakyThrows
    public Map<String, Object> addGetPositionCollect(String token,Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetPositionCollect;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
        return map;

    }

    @SneakyThrows
    public  Map<String, Object> addGetNewPositionCollect(String token,Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetNewPositionCollect;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
        return map;
    }

    @SneakyThrows
    public Map<String, Object> addDataView(String token,Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.DataView;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        map.put("Result",json2map(map.get("Result").toString()));
        return map;

    }


    public String addGetEmployeePhoto(String token, Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetEmployeePhoto;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        return count;
    }

    /**
     * 查询下级字典信息
     * @param token
     * @param params
     * @return
     */
    public Map<String, Object> addGetChildCodes(String token, Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetChildCodes;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        return map;
    }

    /**
     * 查询单个字典信息
     * @param token
     * @param params
     * @return
     */
    public Map<String, Object> addGetCodeInfo(String token, Map<String, String> params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);

        String urls = Urlenum.url + Urlenum.GetCodeInfo;
        String count = HttpClientUtil.doHttpGet(urls, headers, params);
        Map<String, Object> map = json2map(count);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        return map;
    }
}
