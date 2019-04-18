package com.pchome.enumerate.catalog;

public enum EnumCatalogProdEcCheckStatus {
    VERIFY("0"),
    APPROVE("1"),
    REJECT("2"),
    VERIFYING("9");

    private String status;

    private EnumCatalogProdEcCheckStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
