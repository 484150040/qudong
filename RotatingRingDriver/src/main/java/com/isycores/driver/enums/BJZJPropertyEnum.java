package com.isycores.driver.enums;

/**
 * 报警主机
 */
public enum BJZJPropertyEnum {

   FQ17("FQ17","防区17"),
   FQ18("FQ18","防区18"),
   FQ19("FQ19","防区19"),
   FQ20("FQ20","防区20"),
   FQ21("FQ21","防区21"),
   FQ22("FQ22","防区22"),
   FQ23("FQ23","防区23"),
   FQ24("FQ24","防区24"),
   FQ25("FQ25","防区25"),
   FQ26("FQ26","防区26"),
   FQ27("FQ27","防区27"),
   FQ28("FQ28","防区28"),
   FQ29("FQ29","防区29"),
   FQ30("FQ30","防区30"),
   FQ31("FQ31","防区31"),
   FQ32("FQ32","防区32"),
   FQ33("FQ33","防区33"),
   FQ34("FQ34","防区34"),
   FQ35("FQ35","防区35"),
   FQ36("FQ36","防区36"),
   FQ41("FQ41","防区41"),
   FQ42("FQ42","防区42"),
   FQ43("FQ43","防区43"),
   FQ44("FQ44","防区44"),
   FQ45("FQ45","防区45"),
   FQ46("FQ46","防区46"),
   FQ47("FQ47","防区47"),
   FQ48("FQ48","防区48")
    ;

    private String type;
    private String name;

    BJZJPropertyEnum(String type, String name) {
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
    public static BJZJPropertyEnum getEnumName(String name) {
        BJZJPropertyEnum[] upsPropertyEnums = BJZJPropertyEnum.values();
        for (BJZJPropertyEnum upsPropertyEnum : upsPropertyEnums) {
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
