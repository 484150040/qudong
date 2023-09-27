package com.isycores.driver.enums;

/**
 * 配电柜
 */
public enum PDGPropertyEnum {
    ZSRDN("ZSRDN","总输入电能"),
    ZSRGL("ZSRGL","总输入功率"),
    L1SRDY1("L1SRDY1","1路L1输入电压"),
    L2SRDY1("L2SRDY1","1路L2输入电压"),
    L3SRDY1("L3SRDY1","1路L3输入电压"),
    L1L2SRXDY1("L1L2SRXDY1","1路L1-L2输入线电压"),
    L2L3SRXDY1("L2L3SRXDY1","1路L2-L3输入线电压"),
    L1L3SRDY1("L1L3SRDY1","1路L1-L3输入线电压"),
    L1SRDL1("L1SRDL1","1路L1输入电流"),
    L2SRDL1("L2SRDL1","1路L2输入电流"),
    L3SRDL1("L3SRDL1","1路L3输入电流"),
    SRPL1("SRPL1","1路输入频率"),
    L1SRGLYS1("L1SRGLYS1","1路L1输入功率因数"),
    L2SRGLYS1("L2SRGLYS1","1路L2输入功率因数"),
    L3SRGLYS1("L3SRGLYS1","1路L3输入功率因数"),
    SRYGDL1("SRYGDL1","1路输入有功电能"),
    L1SRDYFZL1("L1SRDYFZL1","1路L1输入电源负载率"),
    L2SRDYFZL1("L2SRDYFZL1","1路L2输入电源负载率"),
    L3SRDYFZL1("L3SRDYFZL1","1路L3输入电源负载率"),
    SRNXDL1("SRNXDL1","1路输入N线电流"),
    L1DYXBZJBG1("L1DYXBZJBG1","1路L1电压谐波总畸变率"),
    L2DYXBZJBG1("L2DYXBZJBG1","1路L2电压谐波总畸变率"),
    L3DYXBZJBG1("L3DYXBZJBG1","1路L3电压谐波总畸变率"),
    L1DLXBZJBG1("L1DLXBZJBG1","1路L1电流谐波总畸变率"),
    L2DLXBZJBG1("L2DLXBZJBG1","1路L2电流谐波总畸变率"),
    L3DLXBZJBG1("L3DLXBZJBG1","1路L3电流谐波总畸变率"),
    ZSRYGGL1("ZSRYGGL1","1路总输入有功功率"),
    ZSRSZGL1("ZSRSZGL1","1路总输入视在功率"),
    ZSRWGGL1("ZSRWGGL1","1路总输入无功功率"),
    SRKGZT1("SRKGZT1","1路输入开关状态"),
    SRFLQZT1("SRFLQZT1","1路输入防雷器状态"),
    L1SRDY2("L1SRDY2","2路L1输入电压"),
    L2SRDY2("L2SRDY2","2路L2输入电压"),
    L3SRDY2("L3SRDY2","2路L3输入电压"),
    L1L2SRXDY2("L1L2SRXDY2","2路L1-L2输入线电压"),
    L2L3SRXDY2("L2L3SRXDY2","2路L2-L3输入线电压"),
    L1L3SRXDY2("L1L3SRXDY2","2路L1-L3输入线电压"),
    L1SRDL2("L1SRDL2","2路L1输入电流"),
    L2SRDL2("L2SRDL2","2路L2输入电流"),
    L3SRDL2("L3SRDL2","2路L3输入电流"),
    SRPL2("SRPL2","2路输入频率"),
    L1SRGLYS2("L1SRGLYS2","2路L1输入功率因数"),
    L2SRGLYS2("L2SRGLYS2","2路L2输入功率因数"),
    L3SRGLYS2("L3SRGLYS2","2路L3输入功率因数"),
    SRYGDN2("SRYGDN2","2路输入有功电能"),
    L1SRDYFZL2("L1SRDYFZL2","2路L1输入电源负载率"),
    L2SRDYFZL2("L2SRDYFZL2","2路L2输入电源负载率"),
    L3SRDYFZL2("L3SRDYFZL2","2路L3输入电源负载率"),
    SRNXDL2("SRNXDL2","2路输入N线电流"),
    L1DYXBZJBG2("L1DYXBZJBG2","2路L1电压谐波总畸变率"),
    L2DYXBZJBG2("L2DYXBZJBG2","2路L2电压谐波总畸变率"),
    L3DYXBZJBG2("L3DYXBZJBG2","2路L3电压谐波总畸变率"),
    L1DLXBZJBG2("L1DLXBZJBG2","2路L1电流谐波总畸变率"),
    L2DLXBZJBG2("L2DLXBZJBG2","2路L2电流谐波总畸变率"),
    L3DLXBZJBG2("L3DLXBZJBG2","2路L3电流谐波总畸变率"),
    ZSRYGGL2("ZSRYGGL2","2路总输入有功功率"),
    ZSRSZGL2("ZSRSZGL2","2路总输入视在功率"),
    ZSRWGGL2("ZSRWGGL2","2路总输入无功功率"),
    SRKGZT2("SRKGZT2","2路输入开关状态"),
    SRFLQZT2("SRFLQZT2","2路输入防雷器状态"),

    ;

    private String type;
    private String name;

    PDGPropertyEnum(String type, String name) {
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
    public static PDGPropertyEnum getEnumName(String name) {
        PDGPropertyEnum[] upsPropertyEnums = PDGPropertyEnum.values();
        for (PDGPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
