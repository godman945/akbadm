package com.pchome.akbadm.bean;

import java.math.BigDecimal;

public class PfbxPriceBean {
    private String id;
    private String positionType;
    private String priceType;
    private BigDecimal assignPositionPrice;
    private BigDecimal assignPrice;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPositionType() {
        return positionType;
    }
    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }
    public String getPriceType() {
        return priceType;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
    public BigDecimal getAssignPositionPrice() {
        return assignPositionPrice;
    }
    public void setAssignPositionPrice(BigDecimal assignPositionPrice) {
        this.assignPositionPrice = assignPositionPrice;
    }
    public BigDecimal getAssignPrice() {
        return assignPrice;
    }
    public void setAssignPrice(BigDecimal assignPrice) {
        this.assignPrice = assignPrice;
    }
}
