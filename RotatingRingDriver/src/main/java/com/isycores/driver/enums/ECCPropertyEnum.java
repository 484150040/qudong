package com.isycores.driver.enums;

/**
 * ECC800冷通道传感器
 */
public enum ECCPropertyEnum {

   DI("DI","水浸传感器DI状态"),
   GD("GD","水浸传感器供电状态"),
    ;

    private String type;
    private String name;

    ECCPropertyEnum(String type, String name) {
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
    public static ECCPropertyEnum getEnumName(String name) {
        ECCPropertyEnum[] upsPropertyEnums = ECCPropertyEnum.values();
        for (ECCPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
