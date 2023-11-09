package com.cn.job.enums;

import lombok.Data;

@Data
public class Urlenum {

    //调用url
    public static final String url = "http://hrapi.hic.zju.edu.cn";
    //登录
    public static final String token = "/oauth/token";
    //获取所有指标集
    public static final String GetBuiltCollect = "/api/Sys/GetBuiltCollect";
    //查询单个字典
    public static final String GetCodeInfo = "/api/Sys/GetCodeInfo";
    //获取员工集合信息数据
    public static final String GetEmployeeList = "/api/Employee/GetEmployeeList";
    //获取机构集合数据
    public static final String GetOrgCollect = "/api/Org/GetOrgCollect";
    //获取岗位集合数据
    public static final String GetPositionCollect = "/api/Position/GetPositionCollect";
    //获取新岗位集合
    public static final String GetNewPositionCollect = "/api/Position/GetNewPositionCollect";
    //获取待入职员工数量
    public static final String GetWaitingEntry = "/api/Employee/GetWaitingEntry";

    //查询下级字典
    public static final String GetChildCodes = "/api/Sys/GetChildCodes";

    //获取视图数据
    public static final String DataView = "/api/DataView/Get";

    //获取员工照片
    public static final String GetEmployeePhoto = "/api/employee/GetEmployeePhotoNew";
    //获取请假可申请天数
    public static final String getdaysremaining = "/api/kq/getdaysremaining";

}
