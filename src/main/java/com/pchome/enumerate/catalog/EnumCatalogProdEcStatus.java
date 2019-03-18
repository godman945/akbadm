package com.pchome.enumerate.catalog;

public enum EnumCatalogProdEcStatus {
    CLOSE("0"),
    OPEN("1");

    private String status;

    private EnumCatalogProdEcStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
