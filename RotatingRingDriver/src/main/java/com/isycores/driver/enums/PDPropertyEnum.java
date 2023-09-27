package com.isycores.driver.enums;

/**
 * 配电、智能ETH插座
 */
public enum PDPropertyEnum {
    POE1KGSATUS("poe1kgsatus","POE1电源开关状态"),
    POE2KGSATUS("poe2kgsatus","POE2电源开关状态"),
    POE3KGSATUS("poe3kgsatus(","POE3电源开关状态"),
    POE4KGSATUS("poe4kgsatus","POE4电源开关状态"),
    POE1LJSATUS("poe1ljsatus","POE1连接状态"),
    POE2LJSATUS("poe2ljsatus","POE2连接状态"),
    POE3LJSATUS("poe3ljsatus","POE3连接状态"),
    POE4LJSATUS("poe4ljsatus","POE4连接状态"),
    FE1LJSATUS("fe1ljsatus","FE1连接状态"),
    FE2LJSATUS("fe2ljsatus","FE2连接状态"),
    TXSATUS("txsatus","通信状态")
    ;

    private String type;
    private String name;

    PDPropertyEnum(String type, String name) {
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
    public static PDPropertyEnum getEnumName(String name) {
        PDPropertyEnum[] upsPropertyEnums = PDPropertyEnum.values();
        for (PDPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
