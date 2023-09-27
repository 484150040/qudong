package com.isycores.driver.enums;

/**
 * 电量仪
 */
public enum DLYPropertyEnum {

   UA_IN("Ua","A相电压"),
   UB_IN("Ub","B相电压"),
   UC_IN("Uc","C相电压"),
   UAB_IN("Uab","AB线电压"),
   UBC_IN("Ubc","BC线电压"),
   UCA_IN("Uca","CA线电压"),
   IA_IN("Ia","A相电流"),
   IB_IN("Ib","B相电流"),
   IC_IN("Ic","C相电流"),
   INPUT_hz("hz","频率"),
   YFA_IN("YFa_in","A相有功功率"),
   YFB_IN("YFb_in","B相有功功率"),
   YFC_IN("YFc_in","C相有功功率"),
   WFA_IN("WFa_in","A相无功功率"),
   WFB_IN("WFb_in","B相无功功率"),
   WFC_IN("WFc_in","C相无功功率"),
   SFA_IN("SFa_in","A相视在功率"),
   SFB_IN("SFb_in","B相视在功率"),
   SFC_IN("SFc_in","C相视在功率"),
   PFA_IN("PFa_in","A相功率因数"),
   PFB_IN("PFb_in","B相功率因数"),
   PFC_IN("PFc_in","C相功率因数"),
   Y_IN("P","总有功功率"),
   P_IN("PF","总功率因数"),
   W_IN("Q","总无功功率"),
   S_IN("S","总视在功率"),
   BPM_UA_IN("bpm_Ua_in","A相旁路电压"),
   BPM_UB_IN("bpm_Ub_in","B相旁路电压"),
   BPM_UC_IN("bpm_Uc_in","C相旁路电压"),
   BPM_UAB_IN("bpm_Uab_in","AB旁路线电压"),
   BPM_UBC_IN("bpm_Ubc_in","BC旁路线电压"),
   BPM_UCA_IN("bpm_Uca_in","CA旁路线电压"),
   TD_DXDD("td_dxdd","停电或单相掉电"),
    ;

    private String type;
    private String name;

    DLYPropertyEnum(String type, String name) {
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
    public static DLYPropertyEnum getEnumName(String name) {
        DLYPropertyEnum[] upsPropertyEnums = DLYPropertyEnum.values();
        for (DLYPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
