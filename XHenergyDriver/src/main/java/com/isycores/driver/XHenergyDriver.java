package com.isycores.driver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.isycores.driver.enums.EnergyEnum;
import com.isycores.driver.utils.DBConnection;
import com.isycores.driver.utils.TimerHandle;
import com.isycores.driver.utils.TimerSchedules;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.isycores.driver.utils.DateUtils.*;
import static com.isycores.driver.utils.HttpClientUtil.*;
import static com.isycores.driver.utils.MD5Encryption.convertMapToString;
import static com.isycores.driver.utils.MD5Encryption.getSign;

public class XHenergyDriver extends IEdgeDeviceDriverBase {
    private static final Logger log = LoggerFactory.getLogger(XHenergyDriver.class);
    private ResourceBundle resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);
    //配置文件参数
    private String path = null;
    private String version = null;
    private String password = null;
    private String username = null;
    private String token = null;
    private double sum = 9999;

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
        private String termId;
        private List<String> zhnum;
        private String jdId;
        private String devName;
    }
    public XHenergyDriver() {
        super("bf786218-add3-4era-92e7-91cdqa26594a2", false, false, false);
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
        timerSchedules.register(new TimerHandle((timerHandle)->{
            getToken();
        }),0, 60 * 1000);
        //本地业务，定时启动
        timerSchedules.register(new TimerHandle((timerHandle)->{

            //修改在线状态
            updateDeviceStatusList(driverInitialParam);
            deviceIdMap.forEach((s, device) -> {
                try {
                    getOnline(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
            //在线状态
        }),1000,5 * 60 * 1000);

        timerSchedules.register(new TimerHandle((timerHandle)->{
            deviceIdMap.forEach((s, device) -> {
                try {
                    GetCbdata(device);
                    GetJfData(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
        }),1000,10 * 60 * 1000);

        timerSchedules.start();
        return 0;
    }

    /**
     * 定时任务获取token
     */
    private void getToken() {
        Map<String,Object> body = new HashMap<>();
        body.put("username",username);
        body.put("password",password);
        try {
            Map<String, Object> maps = json2map(httpPost2Json("http://"+path+"/dev-api/login",body));
            token = maps.get("token").toString();
        } catch (UnsupportedEncodingException e) {
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
                new ConnectParam("消息通道", "messageChannel"),
                new ConnectParam("协议版本", "version"),
                new ConnectParam("账号", "username"),
                new ConnectParam("密码", "password")
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
            if (connectParam.getName().equals("账号")) {
                username = connectParam.getValue();
            }
            if (connectParam.getName().equals("密码")) {
                password = connectParam.getValue();
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
                    device.toOnline = false;
                    //激活
                    device.online = false;
                    if (!StringUtils.isEmpty(extra.getString("termId"))){
                        device.termId = extra.getString("termId");
                    }
                    if (!StringUtils.isEmpty(extra.getString("zhnum"))){
                        device.zhnum = extra.getJSONArray("zhnum");
                    }
                    if (!StringUtils.isEmpty(extra.getString("jdId"))){
                        device.jdId = extra.getString("jdId");
                    }
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
     * 在线状态
     *
     * @param device
     */
    @SneakyThrows
    private void getOnline(Device device) {
        if (device != null) {
            TslEventAction action = new TslEventAction();
            action.setOutputs(new HashMap<>());
            action.setDevId(device.devId);
            action.getOutputs().put("devName",device.devName);
            try{
                if (!StringUtils.isEmpty(device.jdId)){
                    YFDflowPath(device);
                } else if (StringUtils.isEmpty(device.termId)){
                    XhflowPath(device,action);
                }else {
                    YKTflowPath(device,action);
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
            updateDeviceStatus(device,device.toOnline);
            listener.onEvent(action);
        }
    }

    private void YFDflowPath(Device device) {
        LinkedHashMap body = new LinkedHashMap<>();
        body.put("nodeId",device.jdId);
        body.put("timestamp","123456");
        String str = getSign(body).toUpperCase();
        body.put("sign",str);
        try {
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type","application/json");
            String respese = sendPost("http://"+path+"/api/GetChildNode",convertMapToString(body),header);
                List<String> maps = Collections.singletonList(respese);
                if (!CollectionUtils.isEmpty(maps)){
                    device.toOnline=true;
                }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 一卡通水表流程走向
     *
     * @param device
     * @param action
     */
    private void YKTflowPath(Device device, TslEventAction action) {
        Connection mConnection = new DBConnection().getConnection();
        if (mConnection != null) {
            try {
                String sql = "select ecode, termid, termname, termaddr, dptcode, acccode, dscrp, isuse, extendedtermaddr, poscode, typeid, samcardno, commode, alldptcode, updateflag, updatedt, downdt, ver, sourcetable from m_base_term where termid="+device.termId;
                PreparedStatement pstm = mConnection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery();
                device.toOnline=true;
                while (rs.next()) {
                    action.getOutputs().put("status",rs.getString("isuse"));
                    action.getOutputs().put("type",rs.getString("typeid"));
                    action.getOutputs().put("downdt",rs.getString("updatedt"));
                    action.getOutputs().put("dscrp",rs.getString("dscrp"));
                }
                rs.close();
                pstm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (mConnection != null) {
                        mConnection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 象和水电表流程走向
     *
     * @param device
     * @param action
     * @throws Exception
     */
    private void XhflowPath(Device device, TslEventAction action) throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("Authorization",token);
        map.put("Content-Type","application/json");
        Map<String,Object> info = json2map(sendGet("http://"+path+"/dev-api/device/"+device.devId,map));
        Map<String,Object> data = (Map<String, Object>) info.get("data");
        //报警次数
        String  countAlarm = data.get("countAlarm").toString().replaceAll(".0","");
        //在离线状态0为在线1为离线
        String  deviceStatus = data.get("deviceStatus").toString().replaceAll(".0","");
        switch (deviceStatus){
            case "0":
                device.toOnline=true;
                break;
            case "1":
                device.toOnline=false;
                break;
            default:
                break;
        }

        //是否启动
        String  isStart = data.get("isStart").toString().replace(".0","");

        Map<String,Object> vulue = json2map(sendGet("http://"+path+"/dev-api/pointValue/"+device.devId,map));
        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) vulue.get("dataOne");
        List<LinkedTreeMap> linkedHashMaps = (List<LinkedTreeMap>) linkedTreeMap.get("recordes");
        List<LinkedTreeMap> maps = (List<LinkedTreeMap>) linkedHashMaps.get(0).get("pointData");
        //水电表的数值
        String values = String.valueOf(maps.get(0).get("value"));
        String unit = String.valueOf(maps.get(0).get("unit"));
        //水表
        if (data.get("type").equals(EnergyEnum.WATER_POINT_TYPE.getType())){
            Double flow = Double.valueOf(values.replace(unit,""));
            Double forwardFlow =flow;
            Double reverseFlow =sum- Double.valueOf(flow);
            action.getOutputs().put("forwardFlow",forwardFlow);
            action.getOutputs().put("reverseFlow",reverseFlow);
        }

        action.getOutputs().put("countAlarm",countAlarm);
        action.getOutputs().put("freezeNumber",values);
        action.getOutputs().put("valveState",isStart);
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

       /**
     * 事件定义
     *
     * @param device
     * @throws Exception
     */
    private void GetJfData(Device device) throws Exception {
        String enterTimeStrLeft = getStartTime(date2String(getStartOfMonth(new Date())));
        String enterTimeStrRight = getEndTime(date2String(getEndOfMonth(new Date())));
        if (CollectionUtils.isEmpty(device.zhnum)) {
            return;
        }
        JSONArray datas =new JSONArray();
        for (String zhnums : device.zhnum) {
            LinkedHashMap body = new LinkedHashMap<>();
            body.put("startTime",enterTimeStrLeft);
            body.put("endTime",enterTimeStrRight);
            body.put("zhnum",zhnums);
            body.put("timestamp","123456");
            String str = getSign(body).toUpperCase();
            body.put("sign",str);
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type","application/json");
            String resp = sendPost("http://"+path+"/api/GetJfData",convertMapToString(body),header);
            if (resp.length()<=2){
                continue;
            }
            JSONArray data = JSONArray.parseArray(resp);
            if (!CollectionUtils.isEmpty(data)){
                datas.addAll(data);
            }
        }
        datas.forEach( _item -> {
            JSONObject extend = (JSONObject) _item;
            if (device != null) {
                String pjcode = extend.getString("pjcode");
                String jfje = extend.getString("jfje");
                String dj = extend.getString("dj");
                String bcye = extend.getString("bcye");
                String zhnum = extend.getString("zhnum");
                String gl = extend.getString("gl");
                String usernm = extend.getString("usernm");
                TslEventAction action = new TslEventAction();
                try {
                    resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
                    String ip = resourceBundle.getString("jscip");
                    String car = sendGet("http://" + ip + "/cockpit/childnode/" + extend.getString("id"), new HashMap<>());
                    if (json2map(car).get("data") == null) {
                        String parm = extend.toString();
                        sendPost("http://" + ip + "/cockpit/childnode", parm, new HashMap<>());
                        action.setDevId(device.devId);
                        action.setIdentifier("Jf");
                        action.setOutputs(new HashMap<>());
                        action.getOutputs().put("devName", device.devName);
                        action.getOutputs().put("pjcode", pjcode);
                        action.getOutputs().put("jfje", jfje);
                        action.getOutputs().put("dj", dj);
                        action.getOutputs().put("bcye", bcye);
                        action.getOutputs().put("zhnum", zhnum);
                        action.getOutputs().put("gl", gl);
                        action.getOutputs().put("usernm", usernm);
                        listener.onEvent(action);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 事件定义
     *
     * @param device
     * @throws Exception
     */
    private void GetCbdata(Device device) throws Exception {
        String enterTimeStrLeft = getStartTime(date2String(getStartOfMonth(new Date())));
        String enterTimeStrRight = getEndTime(date2String(getEndOfMonth(new Date())));
        if (CollectionUtils.isEmpty(device.zhnum)) {
            return;
        }
        List<Map<String,Object>> datas = new ArrayList<>();
        for (String zhnums : device.zhnum) {
            LinkedHashMap body = new LinkedHashMap<>();
            body.put("zhnum",zhnums);
            body.put("timestamp","123456");
            body.put("StartDate",enterTimeStrLeft);
            body.put("EndDate",enterTimeStrRight);
            body.put("blxTag","0");
            String str = getSign(body).toUpperCase();
            body.put("sign",str);
            Map<String, String> header = new HashMap<>();
            header.put("Content-Type","application/json");
            String resp = sendPost("http://"+path+"/api/GetCbdata",convertMapToString(body),header);
            if (resp.length()<=2){
                continue;
            }
            List<Map<String,Object>> data = JSONArray.parseArray(resp);

            if (!CollectionUtils.isEmpty(data)){
                datas.addAll(data);
            }

        }

        datas.forEach( _item -> {
            JSONObject extend = (JSONObject) _item;
            if (device != null) {
                String xm = extend.getString("姓名");
                String mph = extend.getString("门牌号");
                String zz = extend.getString("住址");
                String cbz = extend.getString("抄表值");
                Long cbrq = extend.getLong("抄表日期");
                String cbfs = extend.getString("抄表方式");
                TslEventAction action = new TslEventAction();
                try {
                    resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
                    String ip = resourceBundle.getString("jscip");
                    JSONObject ext = new JSONObject();
                    ext.put("xm",xm);
                    ext.put("mph",mph);
                    ext.put("zz",zz);
                    ext.put("cbz",cbz);
                    ext.put("cbrq",cbrq);
                    ext.put("cbfs",cbfs);
                    ext.put("id",zz+cbrq);

                    String parm = ext.toString();
                    sendPost("http://" + ip + "/cockpit/cbdata", parm, new HashMap<>());
                    action.setDevId(device.devId);
                    action.setIdentifier("Cb");
                    action.setOutputs(new HashMap<>());
                    action.getOutputs().put("devName", device.devName);
                    action.getOutputs().put("xm", xm);
                    action.getOutputs().put("mph", mph);
                    action.getOutputs().put("zz", zz);
                    action.getOutputs().put("cbz", cbz);
                    action.getOutputs().put("cbrq", cbrq);
                    action.getOutputs().put("cbfs", cbfs);
                    listener.onEvent(action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void main(String[] args) throws Exception {
       /* XHenergyDriver object = new XHenergyDriver();
        BootStrapManager.init(1,10);
        object.listener = event -> log.info("{}",event.toString());
        object.path="172.17.102.244";
        object.username="admin";
        object.password="admin123";
        object.init(null);
        Thread.sleep(1000*60);
        object.exit();*/
        String enterTimeStrLeft = getStartTime(date2String(getStartOfMonth(new Date())));
        String enterTimeStrRight = getEndTime(date2String(getEndOfMonth(new Date())));
        LinkedHashMap body = new LinkedHashMap<>();
        body.put("zhnum","00200002");
        body.put("timestamp","123456");
        body.put("StartDate",enterTimeStrLeft);
        body.put("EndDate",enterTimeStrRight);
        body.put("blxTag","0");
        String str = getSign(body).toUpperCase();
        body.put("sign",str);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type","application/json");
        String resp = sendPost("http://172.18.127.81:81/api/GetCbdata",convertMapToString(body),header);
        JSONArray data = JSONArray.parseArray(resp);
        System.out.println(data);
    }
}
