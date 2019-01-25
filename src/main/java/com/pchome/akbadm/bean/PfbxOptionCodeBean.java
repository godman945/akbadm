package com.pchome.akbadm.bean;

public class PfbxOptionCodeBean {
    private String id;
    private String optionType;
    private String pfbxCustomerInfoId;
    private String PName;
    private int PCode;
    private Integer SId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOptionType() {
        return optionType;
    }
    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }
    public String getPfbxCustomerInfoId() {
        return pfbxCustomerInfoId;
    }
    public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
        this.pfbxCustomerInfoId = pfbxCustomerInfoId;
    }
    public String getPName() {
        return PName;
    }
    public void setPName(String pName) {
        PName = pName;
    }
    public int getPCode() {
        return PCode;
    }
    public void setPCode(int pCode) {
        PCode = pCode;
    }
    public Integer getSId() {
        return SId;
    }
    public void setSId(Integer sId) {
        SId = sId;
    }
}
