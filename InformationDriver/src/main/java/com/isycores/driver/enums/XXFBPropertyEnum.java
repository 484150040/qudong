package com.isycores.driver.enums;

/**
 * 信息发布
 */
public enum XXFBPropertyEnum {

    CURRENT_SENCE("current_sence","场景名称"),
    CURRENT_PROGRAM("current_program","节目名称"),
    DEVICETYPE("deviceType","设备类型"),
    HEIGHT("height","屏幕高度"),
    WIDTH("width","屏幕宽度"),
    STATUS("status","终端状态"),
    ;

    private String type;
    private String name;

    XXFBPropertyEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * 根据Key得到枚举的Value
     * 增强for循环遍历，比较判断
     *
     * @param type
     * @return
     */
    public static XXFBPropertyEnum getEnumName(String type) {
        XXFBPropertyEnum[] upsPropertyEnums = XXFBPropertyEnum.values();
        for (XXFBPropertyEnum upsPropertyEnum : upsPropertyEnums) {
            if (upsPropertyEnum.getType().equals(type)) {
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
