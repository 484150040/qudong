package com.isycores.driver;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.isycores.driver.enums.*;
import com.isycores.driver.utils.TimerHandle;
import com.isycores.driver.utils.TimerSchedules;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static com.isycores.driver.utils.HttpClientUtil.*;

public class RotatingRingDriver extends IEdgeDeviceDriverBase {
    private static final Logger log = LoggerFactory.getLogger(RotatingRingDriver.class);
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
    //配置文件参数
    private String path = null;
    private String version = null;
    private String port = null;
    private String token = null;

    //请求第三方接口数据
    private Map<String, Device> deviceIdMap = new HashMap<>();
    private TimerSchedules timerSchedules = new TimerSchedules(1000);

    /**
     * 设备相关配置
     */
    private static class Device {
        private Boolean toOnline = false;
        private Boolean online = false;
        private String devId;
        private String devName;
    }
    public RotatingRingDriver() {
        super("f6d194ec-6451-4e27-aa44-6372c5844fcd", false, false, false);
    }

    @Override
    public DriverFunctions initDriverFunctions() {
        return new DriverFunctions(false, null, null, null);
    }

    @SneakyThrows
    @Override
    public int init(DriverInitialParam driverInitialParam) {
        if (driverInitialParam != null) {
            initialParamHandle(driverInitialParam);
        }
        //本地业务，定时启动
        timerSchedules.register(new TimerHandle((timerHandle)->{

            //修改在线状态
            updateDeviceStatusList(driverInitialParam);
            deviceIdMap.forEach((s, device) -> {
                try {
                    //实时监控
                    getRealTimeMonitoring(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
        }),0, 5 * 60 * 1000);

        timerSchedules.register(new TimerHandle((timerHandle)->{
            getToken();
        }),0, 30 * 60 * 1000);

        timerSchedules.start();
        return 0;
    }


    /**
     * 定时任务获取token
     */
    @SneakyThrows
    private void getToken() {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            Map<String, Object> maps = json2map(sendGet("http://"+path+":"+port+"/Api/Third/GetToken",new HashMap<>()));
            Map<String, Object> data = (Map<String, Object>) maps.get("Data");
            stringBuffer.append(data.get("token_type"));
            stringBuffer.append(" ");
            stringBuffer.append(data.get("access_token"));
            token = stringBuffer.toString();
            System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int exit() {
        timerSchedules.stop();
        return 0;
    }

    @Override
    public DriverResult accessAttribute(TslAttrFrame tslAttrFrame) {

        return DriverResult.SUCCESS;
    }


    @SneakyThrows
    @Override
    public DriverResult accessService(TslServiceAction tslServiceAction) {
        return DriverResult.SUCCESS;
    }

    @Override
    public List<ConnectParam> initDriverConnectParams() {
        return Arrays.asList(
                new ConnectParam("接口地址", "App Http Server Path"),
                new ConnectParam("协议版本", "version"),
                new ConnectParam("端口", "port")
        );
    }

    /**
     * 获取到整体参数
     *
     * @param initialParam
     * @return
     */
    private int initialParamHandle(DriverInitialParam initialParam) {
        for (ConnectParam connectParam : initialParam.getConnectParams()) {
            if (connectParam.getValue() == null) {
                log.error("connectParam.getValue() == null");
                continue;
            }
            if (connectParam.getName().equals("接口地址")) {
                path = connectParam.getValue();
            }
            if (connectParam.getName().equals("协议版本")) {
                version = connectParam.getValue();
            }
            if (connectParam.getName().equals("端口")) {
                port = connectParam.getValue();
            }

        }
        updateDeviceStatusList(initialParam);

        return 0;
    }

    /**
     * 获取参数并修改状态
     *
     * @param initialParam
     */
    private void updateDeviceStatusList(DriverInitialParam initialParam) {
        if (initialParam==null || CollectionUtils.isEmpty(initialParam.getDeviceInfoList())){
            return;
        }
        //标签中json数据的参数（可根据自己配置）
        initialParam.getDeviceInfoList().forEach((deviceInfo)->{
            if (!deviceInfo.getExtra().isEmpty()) {
                JSONObject extra = JSONObject.parseObject(deviceInfo.getExtra());
                if (extra!=null) {
                    Device device = new Device();
                    //是否在线
                    device.toOnline = true;
                    //激活
                    device.online = true;
                    device.devName = deviceInfo.getDevName();
                    device.devId = deviceInfo.getDevId();
                    deviceIdMap.put(device.devId,device);
                } else {
                    log.error("device {} extra not valid",deviceInfo.getDevId());
                }

            } else {
                log.error("device {} extra not valid",deviceInfo.getDevId());
            }
        });
    }


/**
     * 报警信息
     *
     * @param device
     * @param action
     */
    @SneakyThrows
    private void getAlarm(Device device, TslEventAction action) {
        Map<String,String> harder = new HashMap<>();
        harder.put("Authorization",token);
        Map<String, Object> maps = json2map(sendGet("http://"+path+":"+port+"/Api/Third/GetCurAlarm",harder));
       List<LinkedTreeMap> data = (List<LinkedTreeMap>) maps.get("Data");
        if (CollectionUtils.isEmpty(data)){
            return;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("data",data);
        String ip = resourceBundle.getString("jscip");
        String body = httpPost2Json("http://" + ip + "/cockpit/dhalarm/addall", map);

       for (LinkedTreeMap datum : data) {
           if (!device.devId.equals(datum.get("device_id"))){
                continue;
           }
            if (datum.get("category_name").toString().equals("温湿度")){
                action.getOutputs().put(WSDPropertyEnum.WSDSX_GJ.getType(),datum.get("event_level_name"));
            }
        }
    }

    /**
     * 实时监控
     * @param device
     */
    @SneakyThrows
    private void getRealTimeMonitoring(Device device) {
        Map<String,String> harder = new HashMap<>();
        harder.put("Authorization",token);
        String jsons = "{\"keys\": \""+device.devId+"\"}";
        Map<String, Object> map = json2map(sendPost("http://"+path+":"+port+"/Api/Third/GetDeviceList",jsons,harder));
        List<LinkedTreeMap> data = (List<LinkedTreeMap>) map.get("Data");
        if (CollectionUtils.isEmpty(data)){
            return;
        }
        String json = "{\n" +
                "    \"device_ids\": [\""+device.devId+"\"]\n" +
                "}";

        Map<String, Object> maps = json2map(sendPost("http://"+path+":"+port+"/Api/Third/GetRealTimeData",json,harder));
        List<LinkedTreeMap> datas = (List<LinkedTreeMap>) maps.get("Data");

        if (CollectionUtils.isEmpty(datas)){
            return;
        }
        addMysql(datas);
        addOnEvent(datas,device,data);

    }
    @SneakyThrows
    private void addMysql(List<LinkedTreeMap> data){
        Map<String,Object> map = new HashMap<>();
        map.put("data",data);
        String ip = resourceBundle.getString("jscip");
        String body = httpPost2Json("http://" + ip + "/cockpit/property/addall", map);
    }

    private void addOnEvent(List<LinkedTreeMap> datas, Device device, List<LinkedTreeMap> data) {
        TslEventAction action = new TslEventAction();
        action.setDevId(device.devId);
        action.setOutputs(new HashMap<>());
        action.getOutputs().put("devName",device.devName);
        switch (data.get(0).get("category_name").toString()){
            case "电池":
                getDianC(datas,action);
                break;
            case "电量仪":
                getDianLY(datas,action);
                break;
            case "传感器":
                getChuanGQ(datas,action);
                break;
            case "温湿度":
                getWSD(datas,action);
                //实时报警
                getAlarm(device,action);
                break;
            case "空调":
                getKT(datas,action);
                break;
            case "漏水检测":
                getLSJC(datas,action);
                break;
            case "UPS":
                getUPS(datas,action);
                break;
            case "多功能传感器":
                getDGNCGQ(datas,action);
                break;
            case "ECC800":
                getECC(datas,action);
                break;
            case "配电柜":
                getPDG(datas,action);
                break;
            case "智能ETH插座":
                getETH(datas,action);
                break;
            case "电源模块":
                getDYMK(datas,action);
                break;
            case "智能微模块执行器":
                getZXQ(datas,action);
                break;
            case "报警主机":
                getBJZJ(datas,action);
                break;
            case "配电":
                getPD(datas,action);
                break;

            default:
                break;
        }

        updateDeviceStatus(device,device.toOnline);
        listener.onEvent(action);
    }


    /**
     * 电源模块
     *
     * @param datas
     * @param action
     */
    private void getDYMK(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            DYMKPropertyEnum propertyEnum = DYMKPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("ZWZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("YXZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 配电
     *
     * @param datas
     * @param action
     */
    private void getPD(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            PDPropertyEnum propertyEnum = PDPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("poe1kgsatus")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("poe2kgsatus")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("poe3kgsatus")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("poe4kgsatus")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 报警主机
     *
     * @param datas
     * @param action
     */
    private void getBJZJ(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            BJZJPropertyEnum propertyEnum = BJZJPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
        }
    }

    /**
     * 智能微模块执行器
     *
     * @param datas
     * @param action
     */
    private void getZXQ(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            ZNWMKPropertyEnum propertyEnum = ZNWMKPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("FZDL")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("FZDY")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("FZGL")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("KGSM1")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("KGSM2")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
        }
    }

    /**
     * 智能ETH插座
     *
     * @param datas
     * @param action
     */
    private void getETH(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            PDPropertyEnum propertyEnum = PDPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
        }
    }

    /**
     * 配电柜
     *
     * @param datas
     * @param action
     */
    private void getPDG(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            PDGPropertyEnum propertyEnum = PDGPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("SRFLQZT1")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("SRFLQZT2")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("SRKGZT2")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * ECC800
     *
     * @param datas
     * @param action
     */
    private void getECC(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            ECCPropertyEnum propertyEnum = ECCPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
        }
    }

    /**
     * 多功能传感器
     *
     * @param datas
     * @param action
     */
    private void getDGNCGQ(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            DGNCGQPropertyEnum propertyEnum = DGNCGQPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("TXZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("YGZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * UPS
     *
     * @param datas
     * @param action
     */
    private void getUPS(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            UPSPropertyEnum propertyEnum = UPSPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("CDZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 漏水检测
     *
     * @param datas
     * @param action
     */
    private void getLSJC(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            if (LSJCPropertyEnum.ALARM.getName().equals(data.get("property_name"))){
                action.getOutputs().put(LSJCPropertyEnum.ALARM.getType(),data.get("curstatus"));
                break;
            }
        }
    }


    /**
     * 空调
     *
     * @param datas
     * @param action
     */
    private void getKT(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            KTPropertyEnum propertyEnum = KTPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("rf_t")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("rf_h")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("KT_YXSJ")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("YS_SJ1")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("YS_SJ2")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("JR_SJ1")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("JR_SJ2")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }
            if (propertyEnum.getType().equals("JS_SJ")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
                continue;
            }

            action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
        }
    }


    /**
     * 温湿度
     *
     * @param datas
     * @param action
     */
    private void getWSD(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            WSDPropertyEnum propertyEnum = WSDPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 传感器
     *
     * @param datas
     * @param action
     */
    private void getChuanGQ(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            CGQPropertyEnum propertyEnum = CGQPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 电量仪
     *
     * @param datas
     * @param action
     */
    private void getDianLY(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            DLYPropertyEnum propertyEnum = DLYPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("CDZT")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 电池
     *
     * @param datas
     * @param action
     */
    @SneakyThrows
    private void getDianC(List<LinkedTreeMap> datas, TslEventAction action) {
        for (LinkedTreeMap data : datas) {
//            addMysql(data);
            DCPropertyEnum propertyEnum = DCPropertyEnum.getEnumName(data.get("property_name").toString());
            if (propertyEnum==null){
                continue;
            }
            if (propertyEnum.getType().equals("zgj_zt")){
                action.getOutputs().put(propertyEnum.getType(),data.get("curstatus"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_hjwd_sx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_hjwd_lx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_fddl_sx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_cddl_sx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_zdy_xx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            if (propertyEnum.getType().equals("dc_zdy_sx")){
                action.getOutputs().put(propertyEnum.getType(),data.get("value_descript"));
                continue;
            }
            action.getOutputs().put(propertyEnum.getType(),data.get("curvalue"));
        }
    }

    /**
     * 修改激活在线状态
     *
     * @param device
     * @param status
     */
    private void updateDeviceStatus(Device device,Boolean status)
    {
        device.online = status;
        TslEventAction action = new TslEventAction();
        action.setDevId(device.devId);
        action.setIdentifier(status ? CommonEventIdentifier.DEVICE_ONLINE : CommonEventIdentifier.DEVICE_OFFLINE);
        listener.onEvent(action);
    }

    public static void main(String[] args) throws Exception {
       /* RotatingRingDriver object = new RotatingRingDriver();
        object.listener = event -> log.info("{}",event.toString());
        object.path="172.17.105.201";
        object.port="97";
        object.init(null);
        Thread.sleep(1000*60);
        object.exit();*/
       /* Map<String,String> harder = new HashMap<>();
        harder.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE2OTE5OTY0NjEsImV4cCI6MTY5MTk5ODI2MSwiaWF0IjoxNjkxOTk2NDYxLCJpc3MiOiJpc3N1ZXIiLCJhdWQiOiJhdWRpZW5jZSJ9.G0Lb2t8qeiPFiQp8TAZrbbRUduvVIokUO-QyK2iPuM8");

        String jsons = "{\"keys\": \"D_D3S_001\"}";
        Map<String, Object> map = json2map(sendPost("http://172.17.105.201:97/Api/Third/GetDeviceList",jsons,harder));
        List<LinkedHashMap> data = (List<LinkedHashMap>) map.get("Data");
        if (CollectionUtils.isEmpty(data)){
            return;
        }
        String json = "{\n" +
                "    \"device_ids\": [\"D_D3S_001\"]\n" +
                "}";
        Map<String, Object> maps = json2map(sendPost("http://172.17.105.201:97/Api/Third/GetRealTimeData",json,harder));
        List<LinkedHashMap> datas = (List<LinkedHashMap>) maps.get("Data");
        if (CollectionUtils.isEmpty(data)){
            return;
        }*/
        Map<String,String> harder = new HashMap<>();
        harder.put("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYmYiOjE2OTQ2NjU1MzgsImV4cCI6MTY5NDY2NzMzOCwiaWF0IjoxNjk0NjY1NTM4LCJpc3MiOiJpc3N1ZXIiLCJhdWQiOiJhdWRpZW5jZSJ9.Iujrm7gABQ5PsBn_17V6oN5lZVMzwfkv9XpmRlXMMQQ");
        Map<String, Object> maps = json2map(sendGet("http://172.17.105.201:97/Api/Third/GetCurAlarm",harder));
        List<LinkedTreeMap> datas = (List<LinkedTreeMap>) maps.get("Data");
        for (LinkedTreeMap data : datas) {
            Map<String,Object> jsonObject = data;

        }
    }
}
