package com.pchome.enumerate.catalog;

public enum EnumCatalogUploadStatus {
    NONE("0"),
    UPLOADING("1"),
    COMPLETE("2");

    private String status;

    private EnumCatalogUploadStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
