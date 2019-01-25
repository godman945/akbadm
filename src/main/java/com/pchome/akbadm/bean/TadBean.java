package com.pchome.akbadm.bean;

public class TadBean {
    private String poolId;
    private String tadId;
    private boolean diffCompany;
    private String content = "";

    public String getPoolId() {
        return poolId;
    }
    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    public String getTadId() {
        return tadId;
    }
    public void setTadId(String tadId) {
        this.tadId = tadId;
    }
    public boolean isDiffCompany() {
        return diffCompany;
    }
    public void setDiffCompany(boolean diffCompany) {
        this.diffCompany = diffCompany;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}