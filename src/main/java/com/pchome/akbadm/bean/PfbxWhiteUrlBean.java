package com.pchome.akbadm.bean;

public class PfbxWhiteUrlBean {
    private String pfbxCustomerInfoId;
    private String playType = "";
    private String defaultType = "";
    private String categoryCode = "";
    private String url;

    public String getPfbxCustomerInfoId() {
        return pfbxCustomerInfoId;
    }
    public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
        this.pfbxCustomerInfoId = pfbxCustomerInfoId;
    }
    public String getPlayType() {
        return playType;
    }
    public void setPlayType(String playType) {
        this.playType = playType;
    }
    public String getDefaultType() {
        return defaultType;
    }
    public void setDefaultType(String defaultType) {
        this.defaultType = defaultType;
    }
    public String getCategoryCode() {
        return categoryCode;
    }
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
