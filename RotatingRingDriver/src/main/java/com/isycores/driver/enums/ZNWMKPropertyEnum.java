package com.isycores.driver.enums;

/**
 * 智能微模块执行器
 */
public enum ZNWMKPropertyEnum {

   TXZT("TXZT","通信状态"),
   FZDL("FZDL","负载电流"),
   FZDY("FZDY","负载电压"),
   FZGL("FZGL","负载功率"),
   SCZT1("SCZT1","第1路输出状态"),
   SCZT2("SCZT2","第2路输出状态"),
   KGSM1("KGSM1","第1路开关继电器寿命"),
   KGSM2("KGSM2","第2路开关继电器寿命"),
   TCGDZT("TCGDZT","天窗磁力锁供电状态"),
   MJGD("MJGD","门禁磁力锁供电状态"),
   GDZT("GDZT","DO供电状态"),
   MZT("MZT","门状态")
    ;

    private String type;
    private String name;

    ZNWMKPropertyEnum(String type, String name) {
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
    public static ZNWMKPropertyEnum getEnumName(String name) {
        ZNWMKPropertyEnum[] upsPropertyEnums = ZNWMKPropertyEnum.values();
        for (ZNWMKPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
