package com.isyscore.os.driver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dahuatech.hutool.http.Method;
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
import java.util.concurrent.atomic.AtomicReference;

import static com.isyscore.os.driver.utils.DateUtils.*;
import static com.isyscore.os.driver.utils.HttpClientUtil.*;

public class DHICCBarrierGate extends IEdgeDeviceDriverBase {
    private ResourceBundle resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);;
    private static final Logger log = LoggerFactory.getLogger(DHICCBarrierGate.class);
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
    public DHICCBarrierGate() {
        super("e4c50e31-6055-43ca-9c40-7f6a72b08c7d", false, false, false);
    }

    @Override
    public DriverFunctions initDriverFunctions() {
        return new DriverFunctions(true,Arrays.asList(
                new DriverAttribute("state","状态",""),
                new DriverAttribute("mode","模式",""),
                new DriverAttribute("control","控制","")
        ), Arrays.asList(
                new DriverEvent("access","通行事件","")
        ), null);
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
                    decodeMessageRealTimeRecord(device);
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                }
            });
        }),0, 2 * 60 * 1000);
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
    public DriverResult accessAttributeWrite(TslAttrFrame attrFrame) {
        AtomicReference<DriverResult> result = new AtomicReference<>(DriverResult.SUCCESS);
        attrFrame.getAttributeActions().forEach(tslAttributeAction -> {
            Device device = devIdMap.get(tslAttributeAction.getDevId());
            switch (tslAttributeAction.getDriverFunctionName()) {
                case "控制": {
                    String value = tslAttributeAction.getValue().toString();
                    switch (value) {
                        case "1": //起
                            controlSluice(device,value);
                            break;
                        case "4": //常开
                            controlSluice(device,value);
                            break;
                        case "5": //正常
                            controlSluice(device,value);
                            break;
                        default:
                            break;
                    }
                    break;
                }
                default:
                    break;
            }
        });
        return result.get();
    }


    /**
     * 道闸控制
     *
     * @param device
     * @param value
     */
    private void controlSluice(Device device, String value) {
        try {
            String channelId = device.resource.replace(device.devOldId,(Integer.valueOf(device.devOldId)+1)+"");
            GeneralRequest request = new GeneralRequest("/evo-apigw/ipms/subSystem/control/sluice", Method.POST,"{\"channelId\":\""+channelId+"\",\"operateType\":"+value+"}");
            GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
            if (response.isSuccess()) {
                log.info("请求成功！");
            }
        }catch (Exception e){
            log.error("请求失败！返回提示：{}",e.getMessage());
        }
    }

    @Override
    public DriverResult accessAttribute(TslAttrFrame attrFrame) {
        switch (attrFrame.getOperation()) {
            case WRITE:
                return accessAttributeWrite(attrFrame);
            default:
                break;
        }
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
                    String ip = resourceBundle.getString("jscip");
                    try {
                        sendPost("http://"+ip+"/cockpit/parkinglot",datas.toString(),new HashMap<>());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONArray data = datas.getJSONArray("pageData");
                    data.forEach( _item -> {
                        JSONObject item = (JSONObject)_item;
                        TslEventAction action = new TslEventAction();
                        action.setDevId(device.devId);
                        action.setOutputs(new HashMap<>());
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
     * 列表查询车场
     *
     * @param device
     * @throws ClientException
     *
     */
    private void parkinglot(Device device) throws ClientException {
        GeneralRequest request = new GeneralRequest("/evo-apigw/ipms/parkinglot/query", Method.GET);
        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
        if (response.isSuccess()) {
            JSONObject root = JSONObject.parseObject(response.getResult());
            if (root != null) {
                if (root.getBoolean("success") != null && root.getBoolean("success").equals(true)) {
                    JSONArray data = root.getJSONArray("data");
                    data.forEach( _item -> {
                        try {
                            String ip = resourceBundle.getString("jscip");
                            sendPost("http://"+ip+"/cockpit/parkinglot",_item.toString(),new HashMap<>());
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

    /**
     * 事件定义
     *
     * @param device
     * @throws Exception
     */
    private void decodeMessageRealTimeRecord(Device device) throws Exception {
        String enterTimeStrLeft = getStartTime(date2String(new Date()));
        String enterTimeStrRight = getEndTime(date2String(new Date()));;
        GeneralRequest request = new GeneralRequest("/evo-apigw/ipms/caraccess/find/his?pageSize=10&enterTimeStrLeft="+enterTimeStrLeft+"&enterTimeStrRight="+enterTimeStrRight+"&exitTimeStrLeft="+enterTimeStrLeft+"&exitTimeStrRight="+enterTimeStrRight, Method.GET);
        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
        if (response.isSuccess()) {
            JSONObject root = JSONObject.parseObject(response.getResult());
            if (root != null) {
                if (root.getBoolean("success") != null && root.getBoolean("success").equals(true)) {
                    JSONObject datas = root.getJSONObject("data");
                    JSONArray data = datas.getJSONArray("pageData");
                    data.forEach( _item -> {
                        JSONObject extend = (JSONObject)_item;
                        if (device != null) {
                            Long exitTime = extend.getLong("exitTime");
                            Long enterTime = extend.getLong("enterTime");
                            String carNum = extend.getString("carNum");
                            String enterItcDevChnname = extend.getString("enterItcDevChnname");
                            String enterSluiceDevChnname = extend.getString("enterSluiceDevChnname");
                            String enterNumImg = extend.getString("enterNumImg");
                            String enterImg = extend.getString("enterImg");
                            String enterSluiceDevChnid = extend.getString("enterSluiceDevChnid");
                            String exitSluiceDevChnid = extend.getString("exitSluiceDevChnid");
                            TslEventAction action = new TslEventAction();
                            String enterSluiceDevChnidresesource = device.resource.replace(device.devOldId,(Integer.valueOf(device.devOldId)+1)+"");
                            String exitSluiceDevChnidresesource = device.resource.replace(device.devOldId,(Integer.valueOf(device.devOldId)+1)+"");
                            if (enterSluiceDevChnid!=null&&enterSluiceDevChnid.equals(enterSluiceDevChnidresesource) || exitSluiceDevChnid!=null&&exitSluiceDevChnid.equals(exitSluiceDevChnidresesource)){
                                try {
                                    resourceBundle=ResourceBundle.getBundle("application", Locale.CHINA);
                                    String ip = resourceBundle.getString("jscip");
                                    String car = sendGet("http://"+ip+"/cockpit/caraccess/"+extend.getString("id"),new HashMap<>());
                                    if (json2map(car).get("data")!=null){
                                        String parm = extend.toString();
                                        sendPost("http://"+ip+"/cockpit/caraccess",parm,new HashMap<>());
                                        action.setDevId(device.devId);
                                        action.setIdentifier("access");
                                        action.setOutputs(new HashMap<>());
                                        action.getOutputs().put("devName",device.devName);
                                        action.getOutputs().put("carNum",carNum);
                                        action.getOutputs().put("enterItcDevChnname",enterItcDevChnname);
                                        action.getOutputs().put("enterSluiceDevChnname",enterSluiceDevChnname);
                                        action.getOutputs().put("enterTime",enterTime);
                                        action.getOutputs().put("exitTime",exitTime);
                                        action.getOutputs().put("enterImg",(StringUtils.isBlank(enterImg)) ? "" : "http://" + host + ":8927/" + enterImg);
                                        action.getOutputs().put("enterNumImg",(StringUtils.isBlank(enterNumImg)) ?  "" : "http://" + host + ":8927/" + enterNumImg);
                                        listener.onEvent(action);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }
            }
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
       String channelId = "1003927$14$0$0".replace("1003927",(Integer.valueOf("1003927")+1)+"");
        System.out.println(channelId);
        IClient daHualient = new DefaultClient("172.17.1.2","kechuang","bf8093df-8c98-4a35-a349-cb527874d73a");
        GeneralRequest request = new GeneralRequest("/evo-apigw/ipms/subSystem/control/sluice", Method.POST,"{\"channelId\":\""+"1003927$14$0$0".replace("1003927",(Integer.valueOf("1003927")+1)+"")+"\",\"operateType\":"+1+"}");
        System.out.println(request.getBody());
        GeneralResponse response = daHualient.doAction(request,request.getResponseClass());
    }
}
