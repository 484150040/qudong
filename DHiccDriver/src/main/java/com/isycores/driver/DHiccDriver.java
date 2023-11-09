package com.isycores.driver;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.isycores.driver.utils.DHhttpCleentUtil;
import com.isycores.driver.utils.HttpServer;
import com.isycores.driver.utils.TimerHandle;
import com.isycores.driver.utils.TimerSchedules;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.isycores.driver.utils.DHhttpCleentUtil.json2map;

public class DHiccDriver extends IEdgeDeviceDriverBase {
    private static final Logger log = LoggerFactory.getLogger(DHiccDriver.class);

    private DHhttpCleentUtil dHhttpCleentUtil  = new DHhttpCleentUtil();;

    //配置文件参数
    private String path = null;
    private String messageChannel = null;
    private int port=8090;
    private String version = null;
    private String password = null;
    private String username = null;
    private String clientId = null;
    private String clientSecret = null;
    //请求第三方接口数据
    private IClient iClient = null;
    private Map<String, Device> deviceIdMap = new HashMap<>();
    private Map<String, Device> channelMap = new HashMap<>();
    private Map<String, Device> parkZoneMap = new HashMap<>();
    private TimerSchedules timerSchedules = new TimerSchedules(1000);

    /**
     * 设备相关配置
     */
    private static class Device {
        private Boolean toOnline = false;
        private Boolean online = false;
        private String devId;
        private String devOldId;
        private String devName;
        private String channel;
    }
    public DHiccDriver() {
        super("bf786278-add8-4eea-92e7-91cdd26194a2", false, false, false);
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
            updateDeviceStatusList(driverInitialParam);
        }),0,5*60*1000);

        timerSchedules.start();
        return 0;
    }


    /**
     * 获取在线信息
     */
    @SneakyThrows
    private void getAlarm(Device device) {
        iClient =  new DefaultClient(path,clientId,clientSecret);
       try{
           Map<String, Object> data = new HashMap<>();
           List<String> deviceCodeList = new ArrayList<>();
           deviceCodeList.add(device.devOldId);
           List<String> channelCodeList = new ArrayList<>();
           channelCodeList.add(device.channel);
           data.put("deviceCodeList",deviceCodeList);
           data.put("includeSubOwnerCodeFlag",true);
           data.put("pageNum",1);
           data.put("channelCodeList",channelCodeList);
           data.put("pageSize",10);
           String datas = dHhttpCleentUtil.post("/evo-apigw/evo-brm/"+version+"/device/channel/subsystem/page",data,iClient);
           //服务出参
           JSONObject root = JSONObject.parseObject(datas);
           JSONObject jsonObject = JSONObject.parseObject(root.getString("data"));
           JSONArray jsonArray = (JSONArray) jsonObject.get("pageData");
           JSONObject jsonObjects = JSONObject.parseObject(jsonArray.get(0).toString());
           switch (jsonObjects.getInteger("isOnline")){
               case 0:
                   device.toOnline=false;
                   break;
               case 1:
                   device.toOnline=true;
                   break;
               default:
                   break;
           }
           channelMap.put(device.channel,device);
           channelMap.forEach((i,devices)-> {
               updateDeviceStatus(devices,devices.toOnline);
           });
       }catch (Exception e){
           log.error("error :{}",e.getMessage());
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
        iClient =  new DefaultClient(path,clientId,clientSecret);
        //请求标识符
        if (tslServiceAction.getIdentifier().equals("APIPreviewURLs")){
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("channelId",tslServiceAction.getInputs().get("channelId"));
            data.put("dataType",tslServiceAction.getInputs().get("dataType"));
            data.put("streamType",tslServiceAction.getInputs().get("streamType"));
            param.put("data",data);
            log.error("请求接口数据：{}",param);
            String datas = dHhttpCleentUtil.post("/evo-apigw/admin/API/MTS/Video/StartVideo",param,iClient);
            log.info(datas);
            //服务出参
            JSONObject root = JSONObject.parseObject(datas);
            JSONObject jsonObject = JSONObject.parseObject(root.getString("data"));
            Map<String,Object> map = new HashMap<>();
            map.put("url",jsonObject.getString("url"));
            tslServiceAction.setOutputs(map);
        }
        return DriverResult.SUCCESS;
    }

    @Override
    public List<ConnectParam> initDriverConnectParams() {
        return Arrays.asList(
                new ConnectParam("接口地址", "App Http Server Path"),
                new ConnectParam("消息通道", "messageChannel"),
                new ConnectParam("协议版本", "version"),
                new ConnectParam("账号", "username"),
                new ConnectParam("密码", "password"),
                new ConnectParam("客户端ID", "clientId"),
                new ConnectParam("客户端密钥", "clientSecret")
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
            if (connectParam.getName().equals("消息通道")) {
                messageChannel = connectParam.getValue();
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
            if (connectParam.getName().equals("客户端ID")) {
                clientId = connectParam.getValue();
            }
            if (connectParam.getName().equals("客户端密钥")) {
                clientSecret = connectParam.getValue();
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
        //标签中json数据的参数（可根据自己配置）
        initialParam.getDeviceInfoList().forEach((deviceInfo)->{
            if (!deviceInfo.getExtra().isEmpty()) {
               Map<String,Object> extra = json2map(deviceInfo.getExtra());
               if (extra.size()==2){
                   Device device = new Device();
                   //是否在线
                   device.toOnline = false;
                   //激活
                   device.online = false;
                   device.devName = deviceInfo.getDevName();
                   device.devId = deviceInfo.getDevId();
                   device.devOldId = String.valueOf(extra.get("devId"));
                   device.channel = String.valueOf(extra.get("channelId"));
                   getAlarm(device);
                   deviceIdMap.put(device.devId,device);
               }

            } else {
                log.error("device {} extra not valid",deviceInfo.getDevId());
            }
        });
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
        DHiccDriver object = new DHiccDriver();
        object.listener = event -> log.info("{}",event.toString());
        object.path="172.17.1.2";
        object.clientId="kechuang";
        object.username="openAPI";
        object.password="kechuang123";
        object.clientSecret="bf8093df-8c98-4a35-a349-cb527874d73a";
//        object.init(null);
//        Thread.sleep(1000*60);
//        object.exit();
        IClient iClient = new DefaultClient(object.path,object.username,object.password,object.clientId,object.clientSecret);

    }
}
