package com.pchome.enumerate.catalog;

public enum EnumCatalogLogoStatus {
    VERIFY("0"),
    APPROVE("1"),
    REJECT("2"),
    VERIFYING("9");

    private String status;

    private EnumCatalogLogoStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
