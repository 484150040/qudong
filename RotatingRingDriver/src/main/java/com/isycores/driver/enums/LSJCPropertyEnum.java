package com.isycores.driver.enums;

/**
 * 漏水报警
 */
public enum LSJCPropertyEnum {

    ALARM("Alarm","漏水报警"),
    ;

    private String type;
    private String name;

    LSJCPropertyEnum(String type, String name) {
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
    public static LSJCPropertyEnum getEnumName(String name) {
        LSJCPropertyEnum[] upsPropertyEnums = LSJCPropertyEnum.values();
        for (LSJCPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
