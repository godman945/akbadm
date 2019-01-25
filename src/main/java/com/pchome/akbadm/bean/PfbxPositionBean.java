package com.pchome.akbadm.bean;

public class PfbxPositionBean {
    private String id;
    private String pfbxCustomerInfoId;
    private String pfdCustomerInfoId;
    private String pfpCustomerInfoId;
    private String PName;
    private Integer SId;
    private String xType;
    private String pType;
    private int pPrice;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPfbxCustomerInfoId() {
        return pfbxCustomerInfoId;
    }
    public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
        this.pfbxCustomerInfoId = pfbxCustomerInfoId;
    }
    public String getPfdCustomerInfoId() {
        return pfdCustomerInfoId;
    }
    public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
        this.pfdCustomerInfoId = pfdCustomerInfoId;
    }
    public String getPfpCustomerInfoId() {
        return pfpCustomerInfoId;
    }
    public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
        this.pfpCustomerInfoId = pfpCustomerInfoId;
    }
    public String getPName() {
        return PName;
    }
    public void setPName(String pName) {
        PName = pName;
    }
    public Integer getSId() {
        return SId;
    }
    public void setSId(Integer sId) {
        SId = sId;
    }
    public String getxType() {
        return xType;
    }
    public void setxType(String xType) {
        this.xType = xType;
    }
    public String getpType() {
        return pType;
    }
    public void setpType(String pType) {
        this.pType = pType;
    }
    public int getpPrice() {
        return pPrice;
    }
    public void setpPrice(int pPrice) {
        this.pPrice = pPrice;
    }
}
