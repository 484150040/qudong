package com.isycores.driver.enums;

/**
 * 空调
 *
 */
public enum KTPropertyEnum {

    RF_T("rf_t","回风温度"),
    RF_H("rf_h","回风湿度"),
    KT_YXSJ("KT_YXSJ","空调运行时间"),
    YS_SJ1("YS_SJ1","1#压缩机运行时间"),
    YS_SJ2("YS_SJ2","2#压缩机运行时间"),
    JR_SJ1("JR_SJ1","1#加热器运行时间"),
    JR_SJ2("JR_SJ2","2#加热器运行时间"),
    JS_SJ("JS_SJ","加湿器运行时间"),
    STATUS("Status","空调开机状态"),
    COMPRESSOR_S1("compressor_s1","1#压缩机状态"),
    COMPRESSOR_S2("compressor_s2","2#压缩机状态"),
    JR_ZT1("JR_ZT1","1#加热器状态"),
    JR_ZT2("JR_ZT2","2#加热器状态"),
    CS_ZT("CS_ZT","除湿器状态"),
    JS_ZT("JS_ZT","加湿器状态"),
    JZ_SFDW_BJ("jz_sfdw_bj","室内机组-送风低温报警"),
    JZ_SFGW_BJ("jz_sfgw_bj","室内机组-送风高温报警"),
    JZ_DS_BJ("jz_ds_bj","室内机组-低湿报警"),
    JZ_GS_BJ("jz_gs_bj","室内机组-高湿报警"),
    JZ_DW_BJ("jz_dw_bj","室内机组-低温报警"),
    JZ_GW_BJ("jz_gw_bj","室内机组-高温报警"),
    ;

    private String type;
    private String name;

    KTPropertyEnum(String type, String name) {
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
    public static KTPropertyEnum getEnumName(String name) {
        KTPropertyEnum[] upsPropertyEnums = KTPropertyEnum.values();
        for (KTPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
