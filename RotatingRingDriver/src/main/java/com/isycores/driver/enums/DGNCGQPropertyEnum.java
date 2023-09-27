package com.isycores.driver.enums;

/**
 * 多功能传感器
 */
public enum DGNCGQPropertyEnum {

   TXZT("TXZT","通信状态"),
   YGZT("YGZT","烟雾传感器状态"),
   WD("WD","温度"),
   SD("SD","湿度"),
    ;

    private String type;
    private String name;

    DGNCGQPropertyEnum(String type, String name) {
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
    public static DGNCGQPropertyEnum getEnumName(String name) {
        DGNCGQPropertyEnum[] upsPropertyEnums = DGNCGQPropertyEnum.values();
        for (DGNCGQPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
