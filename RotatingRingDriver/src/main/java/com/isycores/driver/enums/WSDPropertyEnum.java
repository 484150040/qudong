package com.isycores.driver.enums;

/**
 * 温湿度
 */
public enum WSDPropertyEnum {

    TEMPERATURE("Temperature","温度"),
    RH("rh","湿度"),
    WSDSX_GJ("wsdsx_gj","温湿度上限告警"),
    ;

    private String type;
    private String name;

    WSDPropertyEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据Key得到枚举的Value
     * 增强for循环遍历，比较判断
     *
     * @param name
     * @return
     */
    public static WSDPropertyEnum getEnumName(String name) {
        WSDPropertyEnum[] upsPropertyEnums = WSDPropertyEnum.values();
        for (WSDPropertyEnum upsPropertyEnum : upsPropertyEnums) {
            if (upsPropertyEnum.getName().equals(name)) {
                return upsPropertyEnum;
            }
        }
        return null;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
