package com.cn.job.controller;

import com.cn.job.service.TransferServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TransferController {

    @Autowired
    private TransferServiceimpl transferService;

    private static String username = "Hic1001001";
    private static String password = "H123!@#$zh";



    /**
     * 查询下级字典
     *
     * @return
     */
    @GetMapping("/GetChildCodes")
    private Map<String, Object> addGetChildCodes(String pid,String withchild){
        String token = transferService.login(username,password);
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(pid)){
            params.put("pid",pid);
        }
        if(!StringUtils.isEmpty(withchild)){
            params.put("withchild",withchild);
        }
        return transferService.addGetChildCodes(token,params);
    }

    /**
     * 查询单个字典
     *
     * @return
     */
    @GetMapping("/GetCodeInfo")
    private Map<String, Object> addGetCodeInfo(String pid){
        String token = transferService.login(username,password);
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(pid)){
            params.put("pid",pid);
        }
        return transferService.addGetCodeInfo(token,params);
    }
    /**
     * 查询所有指标集
     *
     * @return
     */
    @GetMapping("/GetBuiltCollect")
    private Map<String, Object> addGetBuiltCollect(){
        String token = transferService.login(username,password);
        return transferService.addGetBuiltCollect(token,new HashMap<String, String>());
    }

    /**
     * 查询视图查询
     *
     * @param viewname
     * @param pageindex
     * @param pagesize
     * @return
     */
    @GetMapping("/DataView")
    private Map<String, Object> addDataView(String viewname,String pageindex,String pagesize){
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(viewname)){
            params.put("viewname",viewname);
        }
        if(!StringUtils.isEmpty(pageindex)){
            params.put("pageindex",pageindex);
        }
        if(!StringUtils.isEmpty(pagesize)){
            params.put("pagesize",pagesize);
        }

        String token = transferService.login(username,password);
        return transferService.addDataView(token,params);
    }

    /**
     * 获取人员信息
     *
     * @param bm
     * @param yglx
     * @param ygzt
     * @param pageindex
     * @param pagesize
     * @return
     */
    @GetMapping("/GetEmployeeList")
    private Map<String, Object> addGetEmployeeList(String bm,String yglx,String ygzt,String pageindex,String pagesize){
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(bm)){
            params.put("bm",bm);
        }
        if(!StringUtils.isEmpty(yglx)){
            params.put("yglx",yglx);
        }
        if(!StringUtils.isEmpty(ygzt)){
            params.put("ygzt",ygzt);
        }
        if(!StringUtils.isEmpty(pageindex)){
            params.put("pageindex",pageindex);
        }
        if(!StringUtils.isEmpty(pagesize)){
            params.put("pagesize",pagesize);
        }
        String token = transferService.login(username,password);
        return transferService.addGetEmployeeList(token,params);
    }


    /**
     * 获取新岗位集合
     *
     * @return
     */
    @GetMapping("/GetNewPositionCollect")
    private Map<String, Object> addGetNewPositionCollect(){
        String token = transferService.login(username,password);
        return transferService.addGetNewPositionCollect(token,new HashMap<String, String>());
    }
    @GetMapping("/GetOrgCollect")
    private Map<String, Object> addGetOrgCollect(String pid){
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(pid)){
            params.put("pid",pid);
        }
        String token = transferService.login(username,password);
        return transferService.addGetOrgCollect(token,params);
    }


    /**
     * 获取机构集合
     *
     * @param pid
     */
    @GetMapping("/GetPositionCollect")
    private Map<String, Object> addGetPositionCollect(String pid){
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(pid)){
            params.put("pid",pid);
        }
        String token = transferService.login(username,password);
       return transferService.addGetPositionCollect(token,params);
    }

    /**
     * 获取照片
     *
     * @param userid
     * @return
     */
    @GetMapping("/GetEmployeePhoto")
    private String addGetEmployeePhoto(String userid){
        Map<String, String> params = new HashMap<>();
        if(!StringUtils.isEmpty(userid)){
            params.put("userid",userid);
        }
        String token = transferService.login(username,password);
        return transferService.addGetEmployeePhoto(token,params);
    }


}
