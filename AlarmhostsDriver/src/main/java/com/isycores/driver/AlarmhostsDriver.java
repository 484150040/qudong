package com.isycores.driver;

import com.alibaba.fastjson.JSONObject;
import com.dahuatech.hutool.http.Method;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.isycores.driver.utils.BootStrapManager;
import com.isycores.driver.utils.DHhttpCleentUtil;
import com.isycores.driver.utils.TimerHandle;
import com.isycores.driver.utils.TimerSchedules;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.enums.AttributeOperationEnum;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.isycores.driver.utils.DHhttpCleentUtil.json2map;


public class AlarmhostsDriver extends IEdgeDeviceDriverBase {
    private static final Logger log = LoggerFactory.getLogger(AlarmhostsDriver.class);

    //配置文件参数
    private IClient daHualient;
    private String host;
    private String clientId;
    private String clientSecret;
    private String version;
    private String password = null;
    private String username = null;
    private DHhttpCleentUtil dHhttpCleentUtil = new DHhttpCleentUtil();

    //请求第三方接口数据
    private Map<String, Device> deviceIdMap = new HashMap<>();
    private Map<String, Device> channalIdMap = new HashMap<>();
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
        private String channalId;
    }

    public AlarmhostsDriver() {
        super("4b67a2b9-c615-42dc-9fc1-3f67073fb080", false, false, false);
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
            channalIdMap.forEach((s, device) -> {
                try {
                    getAlarm(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
        }),1000,4 * 60 * 1000);
        timerSchedules.register(new TimerHandle((timerHandle) -> {
            getToken();
        }), 0, 30 * 60 * 1000);
        timerSchedules.start();
        return 0;
    }


    /**
     * 获取报警主机信息
     *
     * @param device
     */
    @SneakyThrows
    private void getAlarm(Device device) {
        String data = dHhttpCleentUtil.get("/evo-apigw/evo-alarm/" + version + "/alarmhosts/one/" + device.devOldId, new HashMap<>(), daHualient);
        if (StringUtils.isEmpty(data)) {
            return;
        }
        Map<String, Object> map = (Map<String, Object>) json2map(data).get("data");
        if (map.get("value")==null){
            log.error("数据为空参数：通道号：{}，设备编号：{}",device.channalId,device.devOldId);
            return;
        }
        List<Map<String, Object>> islist = (List<Map<String, Object>>) map.get("value");


        String status = null;
        for (Map<String, Object> stringObjectMap : islist) {
            //判断多个子系统
            boolean isTrue = false;

            if (device.channalId.equals(stringObjectMap.get("subsystemId"))) {
                isTrue = true;
                if (stringObjectMap.get("status").equals("7.0")){
                    device.toOnline=false;
                }else {
                    device.toOnline=true;
                }
                status = String.valueOf(stringObjectMap.get("status"));
            }else {
                //判断子系统内部防区
                List<Map<String, Object>> mapList = (List<Map<String, Object>>) stringObjectMap.get("defenceAreaList");
                for (Map<String, Object> objectMap : mapList) {
                    if (device.channalId.equals(objectMap.get("defenceAreaId"))) {
                        isTrue = true;
                        switch (String.valueOf(objectMap.get("isOnline"))){
                            case "0.0":
                                device.toOnline=false;
                                break;
                            case "1.0":
                                device.toOnline=true;
                                break;
                            default:
                                break;
                        }
                        status = String.valueOf(stringObjectMap.get("status"));
                        break;
                    }
                }
            }
            TslEventAction action = new TslEventAction();
            action.setDevId(device.devId);
            action.setOutputs(new HashMap<>());
            action.getOutputs().put("devName",device.devName);
            action.getOutputs().put("status",status.replace(".0",""));
            updateDeviceStatus(device,device.toOnline);
            listener.onEvent(action);
            if (isTrue) {
                break;
            }
        }

    }


    /**
     * 定时任务获取token
     */
    @SneakyThrows
    private void getToken() {
        try {
            daHualient = new DefaultClient(host, clientId, clientSecret);
        } catch (ClientException e) {
            return;
        }
    }


    @Override
    public int exit() {
        timerSchedules.stop();
        return 0;
    }

    @Override
    public DriverResult accessAttribute(TslAttrFrame attrFrame) {
        AtomicReference<DriverResult> result = new AtomicReference<>(DriverResult.SUCCESS);
        if (attrFrame.getOperation().equals(AttributeOperationEnum.WRITE)) {
            attrFrame.getAttributeActions().forEach(tslAttributeAction -> {
                Device device = deviceIdMap.get(tslAttributeAction.getDevId());
                if (tslAttributeAction.getIdentifier().equals("operate")) {
                    String value = tslAttributeAction.getValue().toString();
                    log.error("写入数据：{}",value);
                    switch (value) {
                        case "1": //布防
                            APIoperate(device.channalId, value);
                            break;
                        case "2": //撤防
                            APIoperate(device.channalId, value);
                            break;
                        case "3": //旁路
                            APIoperate(device.channalId, value);
                            break;
                        case "5": //延迟布防
                            APIoperate(device.channalId, value);
                            break;
                        case "6": //消警
                            APIoperate(device.channalId, value);
                            break;
                        case "14": //隔离
                            APIoperate(device.channalId, value);
                            break;
                        case "15": //取消旁路
                            APIoperate(device.channalId, value);
                            break;
                        default:
                            break;
                    }
                }

            });
        }

        return result.get();
    }

    /**
     * @param channalId
     */
    @SneakyThrows
    private void APIoperate(String channalId, String value) {
        if (!StringUtils.isEmpty(channalId)) {
            Map<String, Object> operate = new HashMap<>();
            List<Map<String, Object>> nodeInfoList = new ArrayList<>();
            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("nodeName", "");
            objectMap.put("nodeCode", channalId);
            nodeInfoList.add(objectMap);
            operate.put("nodeInfoList", nodeInfoList);
            operate.put("operate", value);
            operate.put("delayTime", 0);
            dHhttpCleentUtil.post("/evo-apigw/evo-alarm/" + version + "/alarmhosts/operate", operate, daHualient);
        }
    }


    @SneakyThrows
    @Override
    public DriverResult accessService(TslServiceAction tslServiceAction) {
        return DriverResult.SUCCESS;
    }

    @Override
    public List<ConnectParam> initDriverConnectParams() {
        return Arrays.asList(
                new ConnectParam("接口地址", "127.0.0.1"),
                new ConnectParam("消息通道", "tcp://127.0.0.1:1883"),
                new ConnectParam("协议版本", "1.0.0"),
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
                host = connectParam.getValue();
                log.info("接口地址: {}", host);
                continue;
            }
            if (connectParam.getName().equals("协议版本")) {
                version = connectParam.getValue();
                continue;
            }
            if (connectParam.getName().equals("账号")) {
                username = connectParam.getValue();
                continue;
            }
            if (connectParam.getName().equals("密码")) {
                password = connectParam.getValue();
                continue;
            }
            if (connectParam.getName().equals("客户端ID")) {
                clientId = connectParam.getValue();
                continue;
            }
            if (connectParam.getName().equals("客户端密钥")) {
                clientSecret = connectParam.getValue();
                continue;
            }

        }
        try {
            daHualient = new DefaultClient(host, clientId, clientSecret);
        } catch (ClientException e) {
            return -1;
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
        if (initialParam == null || CollectionUtils.isEmpty(initialParam.getDeviceInfoList())) {
            return;
        }
        //标签中json数据的参数（可根据自己配置）
        initialParam.getDeviceInfoList().forEach((deviceInfo) -> {
            if (!deviceInfo.getExtra().isEmpty()) {
                JSONObject extra = JSONObject.parseObject(deviceInfo.getExtra());
                if (extra != null) {
                    Device device = new Device();
                    //是否在线
                    device.toOnline = false;
                    //激活
                    device.online = false;
                    device.devName = deviceInfo.getDevName();
                    device.devId = deviceInfo.getDevId();
                    device.channalId = String.valueOf(extra.get("channalId"));
                    device.devOldId = String.valueOf(extra.get("devIds"));
                    deviceIdMap.put(device.devId, device);
                    channalIdMap.put(device.channalId, device);
                } else {
                    log.error("device {} extra not valid", deviceInfo.getDevId());
                }

            } else {
                log.error("device {} extra not valid", deviceInfo.getDevId());
            }
        });
    }


    /**
     * 修改激活在线状态
     *
     * @param device
     * @param status
     */
    private void updateDeviceStatus(Device device, Boolean status) {
        device.online = status;
        TslEventAction action = new TslEventAction();
        action.setDevId(device.devId);
        action.setIdentifier(status ? CommonEventIdentifier.DEVICE_ONLINE : CommonEventIdentifier.DEVICE_OFFLINE);
        listener.onEvent(action);
    }

    public static void main(String[] args) throws Exception {
        /*AlarmhostsDriver object = new AlarmhostsDriver();
        BootStrapManager.init(1, 10);
        object.host = "172.17.1.2";
        object.version = "1.0.0";
        object.username = "openAPI";
        object.password = "kechuang123";
        object.clientId = "kechuang";
        object.clientSecret = "bf8093df-8c98-4a35-a349-cb527874d73a";
        object.listener = event -> log.info("{}", event.toString());
        object.init(null);
        Thread.sleep(1000 * 60);
        object.exit();

        System.out.println(UUID.randomUUID());*/
        DHhttpCleentUtil dHhttpCleentUtil = new DHhttpCleentUtil();
        IClient daHualient = new DefaultClient("172.17.1.2", "kechuang", "bf8093df-8c98-4a35-a349-cb527874d73a");


        GeneralRequest request = new GeneralRequest("/evo-apigw/evo-alarm/1.0.0/alarmhosts/one/1004199", Method.GET);

        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
        String data = dHhttpCleentUtil.get("/evo-apigw/evo-alarm/1.0.0/alarmhosts/one/1004199" , new HashMap<>(), daHualient);
        System.out.println(data);
     /*   String json = "{\"success\":true,\"data\":{\"value\":[{\"subsystemId\":\"1001901$25$0$0\",\"subSystemName\":\"子系统1\",\"status\":2,\"defenceAreaList\":[{\"defenceAreaId\":\"1001901$3$0$16\",\"defenceAreaName\":\"BJ-R1-1F-01红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$17\",\"defenceAreaName\":\"无障碍厕所手报\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$18\",\"defenceAreaName\":\"BJ_R1-1F-04红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$19\",\"defenceAreaName\":\"BJ-R1-1F-05红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$20\",\"defenceAreaName\":\"BJ-R1-1F-06红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$21\",\"defenceAreaName\":\"BJ-R1-1F-07红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$22\",\"defenceAreaName\":\"BJ-R1-1F-08红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2},{\"defenceAreaId\":\"1001901$3$0$23\",\"defenceAreaName\":\"BJ-R1-1F-09红外\",\"status\":2,\"isOnline\":1,\"byPass\":0,\"defenceAreaType\":2}]}]},\"code\":\"0\",\"errMsg\":\"\"}\n";
        Map<String, Object> map = (Map<String, Object>) json2map(json).get("data");
        List<Map<String,Object>> islist = (List<Map<String, Object>>) map.get("value");
        Map<String,Object> stringObjectMap = islist.get(0);
        List<Map<String,Object>> mapList = (List<Map<String, Object>>) stringObjectMap.get("defenceAreaList");
        for (Map<String, Object> objectMap : mapList) {
            System.out.println(objectMap.get("defenceAreaId"));
        }*/
    }
}
