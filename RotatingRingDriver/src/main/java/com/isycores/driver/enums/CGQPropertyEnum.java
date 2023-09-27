package com.isycores.driver.enums;

/**
 * 传感器
 */
public enum CGQPropertyEnum {

    QTND("qtnd","气体浓度"),
    ;

    private String type;
    private String name;

    CGQPropertyEnum(String type, String name) {
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
    public static CGQPropertyEnum getEnumName(String name) {
        CGQPropertyEnum[] upsPropertyEnums = CGQPropertyEnum.values();
        for (CGQPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
