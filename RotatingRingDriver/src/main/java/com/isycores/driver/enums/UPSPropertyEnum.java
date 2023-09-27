package com.isycores.driver.enums;

/**
 * UPS
 */
public enum UPSPropertyEnum {

   CDZT("CDZT","充电状态"),
   UA_IN("Ua_in","A相电压"),
   UB_IN("Ub_in","B相电压"),
   UC_IN("Uc_in","C相输入电压"),
   UAB_IN("Uab_in","AB线电压"),
   UBC_IN("Ubc_in","BC线电压"),
   UCA_IN("Uca_in","CA线电压"),
   IA_IN("Ia_in","A相输入电流"),
   IB_IN("Ib_in","B相输入电流"),
   IC_IN("Ic_in","C相输入电流"),
   INPUT_hz("input_hz","输入频率"),
   PFA_IN("PFa_in","A相输入功率因数"),
   PFB_IN("PFb_in","B相输入功率因数"),
   PFC_IN("PFc_in","C相输入功率因数"),
   BPM_UA_IN("bpm_Ua_in","A相旁路电压"),
   BPM_UB_IN("bpm_Ub_in","B相旁路电压"),
   BPM_UC_IN("bpm_Uc_in","C相旁路电压"),
   BPM_UAB_IN("bpm_Uab_in","AB旁路线电压"),
   BPM_UBC_IN("bpm_Ubc_in","BC旁路线电压"),
   BPM_UCA_IN("bpm_Uca_in","CA旁路线电压"),
    ;

    private String type;
    private String name;

    /**
     * 根据Key得到枚举的Value
     * 增强for循环遍历，比较判断
     *
     * @param name
     * @return
     */
    public static UPSPropertyEnum getEnumName(String name) {
        UPSPropertyEnum[] upsPropertyEnums = UPSPropertyEnum.values();
        for (UPSPropertyEnum upsPropertyEnum : upsPropertyEnums) {
            System.out.println(upsPropertyEnum.getType());
            if (upsPropertyEnum.getName().equals(name)) {
                return upsPropertyEnum;
            }
        }
        return null;
    }

    UPSPropertyEnum(String type, String name) {
        this.type = type;
        this.name = name;
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
