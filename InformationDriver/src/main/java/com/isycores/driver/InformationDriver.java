package com.isycores.driver;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.isycores.driver.enums.XXFBPropertyEnum;
import com.isycores.driver.utils.TimerHandle;
import com.isycores.driver.utils.TimerSchedules;
import com.isyscore.os.driver.core.driver.*;
import com.isyscore.os.driver.core.iedge.IEdgeDeviceDriverBase;
import lombok.SneakyThrows;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.isycores.driver.utils.HttpClientUtil.*;

public class InformationDriver extends IEdgeDeviceDriverBase {
    private static final Logger log = LoggerFactory.getLogger(InformationDriver.class);
    private ResourceBundle resourceBundle;
    //配置文件参数
    private String path = null;
    private String version = null;
    private String appkey = null;
    private String username = null;

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
        private String responce;
        private String devName;
    }

    public InformationDriver() {
        super("668e47fc-d373-48b1-aec7-1e79831ade22", false, false, false);
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
        timerSchedules.register(new TimerHandle((timerHandle) -> {

            //修改在线状态
            updateDeviceStatusList(driverInitialParam);
            deviceIdMap.forEach((s, device) -> {
                try {
                    //终端信息
                    getRealTimeMonitoring(device);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        }), 1000, 5*60*1000);
        timerSchedules.start();
        return 0;
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
                new ConnectParam("APPKEY", "appkey"),
                new ConnectParam("username", "username")
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
            if (connectParam.getName().equals("APPKEY")) {
                appkey = connectParam.getValue();
            }
            if (connectParam.getName().equals("username")) {
                username = connectParam.getValue();
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
                    device.responce = extra.getString("responce");
                    device.devId = deviceInfo.getDevId();
                    deviceIdMap.put(device.devId, device);
                } else {
                    log.error("device {} extra not valid", deviceInfo.getDevId());
                }

            } else {
                log.error("device {} extra not valid", deviceInfo.getDevId());
            }
        });
    }

    /**
     * 实时监控
     *
     * @param device
     */
    @SneakyThrows
    private void getRealTimeMonitoring(Device device) {
        Map<String, Object> map = json2map(sendGet("http://" + path + "/Webedit/api/player/players/list?key="+appkey+"&username="+username+"&ids="+device.responce, new HashMap<>()));
        if (map == null || map.size() <= 0) {
            return;
        }
        List<LinkedTreeMap<String, Object>> data = (List<LinkedTreeMap<String, Object>>) map.get("data");
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        addOnEvent(data, device);

    }

    private void addOnEvent(List<LinkedTreeMap<String, Object>> datas, Device device) {
        TslEventAction action = new TslEventAction();
        action.setDevId(device.devId);
        action.setOutputs(new HashMap<>());
        action.getOutputs().put("devName", device.devName);
        getXXFB(datas, action, device);
        updateDeviceStatus(device, device.toOnline);
        listener.onEvent(action);
    }


    /**
     * 信息发布
     *
     * @param datas
     * @param action
     * @param device
     */
    @SneakyThrows
    private void getXXFB(List<LinkedTreeMap<String, Object>> datas, TslEventAction action, Device device) {
        for (LinkedTreeMap<String, Object> data : datas) {
            resourceBundle = ResourceBundle.getBundle("application", Locale.CHINA);
            String ip = resourceBundle.getString("jscip");
            httpPost2Json("http://" + ip + "/cockpit/webeditplayer", data);
            action.getOutputs().put(XXFBPropertyEnum.CURRENT_PROGRAM.getType(), data.get(XXFBPropertyEnum.CURRENT_PROGRAM.getType()));
            action.getOutputs().put(XXFBPropertyEnum.CURRENT_SENCE.getType(), data.get(XXFBPropertyEnum.CURRENT_SENCE.getType()));
            action.getOutputs().put(XXFBPropertyEnum.DEVICETYPE.getType(), data.get(XXFBPropertyEnum.DEVICETYPE.getType()));
            action.getOutputs().put(XXFBPropertyEnum.WIDTH.getType(), data.get(XXFBPropertyEnum.WIDTH.getType()));
            action.getOutputs().put(XXFBPropertyEnum.HEIGHT.getType(), data.get(XXFBPropertyEnum.HEIGHT.getType()));
            Boolean onlone = data.get(XXFBPropertyEnum.STATUS.getType()).equals("1");
            device.toOnline = onlone;
            break;
        }

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

}