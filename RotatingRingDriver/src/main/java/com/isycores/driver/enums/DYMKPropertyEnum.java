package com.isycores.driver.enums;

/**
 * ECC800冷通道传感器
 */
public enum DYMKPropertyEnum {

   ZWZT("ZWZT","在位状态"),
   YXZT("YXZT","运行状态"),
   ZYXS("ZYXSJ","总运行时间"),
   SCGL("SCGL","输出功率"),
   SCDY("SCDY","输出电压"),
   SCDL("SCDL","输出电流"),
   NBWD("NBWD","内部温度"),
    ;

    private String type;
    private String name;

    DYMKPropertyEnum(String type, String name) {
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
    public static DYMKPropertyEnum getEnumName(String name) {
        DYMKPropertyEnum[] upsPropertyEnums = DYMKPropertyEnum.values();
        for (DYMKPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
