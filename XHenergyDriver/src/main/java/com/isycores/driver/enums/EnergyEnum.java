package com.isycores.driver.enums;

public enum EnergyEnum {
    WATER_POINT_TYPE("water_point_type","水表"),
    ELETRIC_POINT_TYPE("eletric_point_type","电表");
    private String type;
    private String name;

    EnergyEnum(String type, String name) {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }
}
