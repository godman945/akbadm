package com.pchome.akbadm.factory.malice;

public enum EnumMaliceType {
    OK("0"),
    UUID("1"),
    REMOTE_IP("2"),
    REFERER("3"),
    USER_AGENT("4"),
    OVER_HOUR_LIMIT("5"),
    OVER_PADDING("6"),
    INSIDE_RECTANGLE("7"),
    OVER_PRICE("8"),
    OVER_PFB_UUID_LIMIT("9"),
    OVER_PFB_REMOTE_IP_LIMIT("10");

    private String type;

    private EnumMaliceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}