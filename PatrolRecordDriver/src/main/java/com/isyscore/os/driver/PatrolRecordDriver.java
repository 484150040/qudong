package com.isyscore.os.driver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dahuatech.hutool.http.Method;
import com.dahuatech.hutool.json.JSONUtil;
import com.dahuatech.icc.exception.ClientException;
import com.dahuatech.icc.oauth.http.DefaultClient;
import com.dahuatech.icc.oauth.http.IClient;
import com.dahuatech.icc.oauth.model.v202010.GeneralRequest;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import com.isyscore.os.driver.utils.TimerHandle;
import com.isyscore.os.driver.utils.TimerSchedules;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.isyscore.os.driver.utils.DateUtils.*;
import static com.isyscore.os.driver.utils.DateUtils.date2String;
import static com.isyscore.os.driver.utils.HttpClientUtil.sendPost;

public class PatrolRecordDriver extends IEdgeDeviceDriverBase {


    private ResourceBundle resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
    private static final Logger log = LoggerFactory.getLogger(PatrolRecordDriver.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static class Device {
        String devId;
        String devOldId;
        String devName;
        String resource;
        Boolean online;
    }
    private Map<String,Device> devIdMap = new HashMap<>();
    private Map<String,Device> resourceMap = new HashMap<>();
    private String host;
    private String version;
    private String clientId;
    private String clientSecret;
    private IClient daHualient;
    private TimerSchedules timerSchedules = new TimerSchedules(1000);

    /**
     * 定义驱动基础参数
     */
    public PatrolRecordDriver() {
        super("3dd915ff-3595-4e01-bf37-a8614aa2c962", false, false, false);
    }

    @Override
    public DriverFunctions initDriverFunctions() {
        return new DriverFunctions(true,null, null, null);
    }

    @Override
    public List<ConnectParam> initDriverConnectParams() {
        return Arrays.asList(
                new ConnectParam("接口地址", "127.0.0.1"),
                new ConnectParam("消息通道", "tcp://127.0.0.1:1883"),
                new ConnectParam("协议版本", "1.0.0"),
                new ConnectParam("客户端ID", "clientId"),
                new ConnectParam("客户端密钥", "clientSecret")
        );
    }
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
            if (connectParam.getName().equals("客户端ID")) {
                clientId = connectParam.getValue();
                continue;
            }
            if (connectParam.getName().equals("客户端密钥")) {
                clientSecret = connectParam.getValue();
                continue;
            }
        }
        initialParam.getDeviceInfoList().forEach((deviceInfo)->{
            try {
                JSONObject configure = JSONObject.parseObject(deviceInfo.getExtra());
                if (configure != null) {
                    Device device = new Device();
                    device.devId = deviceInfo.getDevId();
                    device.devName = deviceInfo.getDevName();
                    device.resource = configure.getString("resource");
                    device.devOldId = configure.getString("devId");
                    device.online = null;
                    resourceMap.put(device.resource, device);
                    devIdMap.put(device.devId, device);
                }
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        });
        return 0;
    }

    @Override
    public int init(DriverInitialParam initialParam) {
        if (initialParam != null) {
            initialParamHandle(initialParam);
        }
        try {
            daHualient = new DefaultClient(host,clientId,clientSecret);
        } catch (ClientException e) {
            log.error(e.getMessage(),e);
    }

        timerSchedules.register(new TimerHandle(timerHandle -> {
            devIdMap.forEach((s, device) -> {
                try {
                    parkinglot(device);
                    APIDoorChannelInfo(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
        }),0, 4*60 * 1000);
        timerSchedules.start();
        return 0;
    }
    @Override
    public int exit() {
        timerSchedules.stop();
        return 0;
    }
    @Override
    public DriverResult accessService(TslServiceAction serviceAction) {
        serviceAction.getIdentifier();
        serviceAction.getInputs();
        serviceAction.setOutputs(new HashMap<>());
        return DriverResult.SUCCESS;
    }

    @Override
    public DriverResult accessAttribute(TslAttrFrame attrFrame) {
        return DriverResult.FAILED;
    }


    /**
     * 属性定义
     *
     * @param device
     * @throws ClientException
     */
    private void APIDoorChannelInfo(Device device) throws ClientException {
        GeneralRequest request = new GeneralRequest("/evo-apigw/evo-brm/"+ version +"/device/channel/subsystem/page", Method.POST,"{\"deviceCodeList\":[\""+device.devOldId+"\"],\"channelCodeList\":[\""+device.resource+"\"],\"includeSubOwnerCodeFlag\":true}");
        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
        if (response.isSuccess()) {
            JSONObject root = JSONObject.parseObject(response.getResult());
            if (root != null) {
                if (root.getBoolean("success") != null && root.getBoolean("success").equals(true)) {
                    JSONObject datas = root.getJSONObject("data");
                    JSONArray data = datas.getJSONArray("pageData");
                    data.forEach( _item -> {
                        JSONObject item = (JSONObject)_item;
                        String ip = resourceBundle.getString("jscip");
                        try {
                            sendPost("http://"+ip+"/cockpit/deviceChannel",item.toString(),new HashMap<>());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        TslEventAction action = new TslEventAction();
                        action.setDevId(device.devId);
                        action.setOutputs(new HashMap<>());
                        action.getOutputs().put("stat",((JSONObject) _item).get("stat"));
                        Boolean online = item.getInteger("isOnline")==1;
                        listener.onEvent(action);

                        if (!online.equals(device.online)) {
                            device.online = online;
                            TslEventAction onlineEvent = new TslEventAction();
                            onlineEvent.setDevId(device.devId);
                            onlineEvent.setIdentifier(online ? CommonEventIdentifier.DEVICE_ONLINE : CommonEventIdentifier.DEVICE_OFFLINE);
                            listener.onEvent(onlineEvent);
                        }
                    });
                }
            }
        } else {
            log.warn(response.getResult());
        }
    }


    /**
     * 列表查询巡更记录
     *
     * @param device
     * @throws ClientException
     *
     */
    private void parkinglot(Device device) throws ClientException {
        String enterTimeStrLeft = getStartTime(date2String(new Date()));
        String enterTimeStrRight = getEndTime(date2String(new Date()));;

        String body = "{\n" +
                "    \"pageNum\": 1,\n" +
                "    \"pageSize\": 1000,\n" +
                "\t\"startTime\":\""+enterTimeStrLeft+"\",\n" +
                "\t\"endTime\":\""+enterTimeStrRight+"\"\n" +
                "}";
        GeneralRequest request = new GeneralRequest("/evo-apigw/evo-jpatrol/"+version+"/record/page", Method.POST,body);
            GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
            if (response.isSuccess()) {
                JSONObject root = JSONObject.parseObject(response.getResult());
                if (root != null) {
                    if (root.getBoolean("success") != null && root.getBoolean("success").equals(true)) {
                        JSONObject datas = root.getJSONObject("data");
                        JSONArray data = datas.getJSONArray("pageData");
                        data.forEach( _item -> {
                            try {
                                resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);
                                String ip = resourceBundle.getString("jscip");
                                String end  = sendPost("http://"+ip+"/cockpit/patrolrecord",_item.toString(),new HashMap<>());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
        } else {
            log.warn(response.getResult());
        }
    }

    public static void main(String[] args) throws Exception {
       /* DHICCBarrierGate object = new DHICCBarrierGate();
        object.exportDriverDescFile("");

        object.listener = tslEventAction -> {
            log.info("{}[{}] -> {}",tslEventAction.getIdentifier(),tslEventAction.getDevId(),tslEventAction.getOutputs());
        };

        DriverInitialParam initialParam = new DriverInitialParam();
        initialParam.setConnectParams(Arrays.asList(
                new ConnectParam("接口地址", "172.17.1.2","172.17.1.2"),
                new ConnectParam("消息通道", "tcp://127.0.0.1:1883","tcp://127.0.0.1:1883"),
                new ConnectParam("协议版本", "version","1.0.0"),
                new ConnectParam("客户端ID", "clientId","kechuang"),
                new ConnectParam("客户端密钥", "clientSecret","bf8093df-8c98-4a35-a349-cb527874d73a")
        ));
        initialParam.setDeviceInfoList(Arrays.asList(

        ));

        object.init(initialParam);
        Thread.sleep(1000 * 60);
        object.exit();*/

        IClient daHualient = new DefaultClient("172.17.1.2","kechuang","bf8093df-8c98-4a35-a349-cb527874d73a");
        GeneralRequest request = new GeneralRequest("/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/doorAuthority/42623E8E", Method.POST,"{\"showInherit\":true}");
        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());


        JSONObject root = JSONObject.parseObject(response.getResult());
        JSONObject datas = root.getJSONObject("data");
        JSONArray data = datas.getJSONArray("cardPrivilegeDetails");
        List<Map<String,Object>> list = new ArrayList<>();
        data.forEach( _item -> {
                    JSONObject item = (JSONObject) _item;
                   Map<String,Object> map = new HashMap<>();
                   map.put("privilegeType",item.getString("privilegeType"));
                   map.put("resouceCode",item.getString("resouceCode"));
                   map.put("resourceName",item.getString("resourceName"));
                    list.add(map);
                });
        Map<String,Object> map = new HashMap<>();

        map.put("cardNumber","42623E8E");
        map.put("cardPrivilegeDetails",list);

        System.out.println(map.toString());
        GeneralRequest request1 = new GeneralRequest("/evo-apigw/evo-accesscontrol/1.0.0/card/accessControl/doorAuthority/deleteSingleCardPrivilege", Method.POST, JSONUtil.toJsonStr(map));
        GeneralResponse response1 = daHualient.doAction(request1,request1.getResponseClass());
        System.out.println(response1);
    }
}
