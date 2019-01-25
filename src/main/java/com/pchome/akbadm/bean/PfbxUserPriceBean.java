package com.pchome.akbadm.bean;

import java.util.HashMap;
import java.util.Map;

public class PfbxUserPriceBean {
    private String id;
    private String pfbxCustomerInfoId;
    private String priceName;
    private String priceType;
    private PfbxAssignPriceBean assignPriceBean;
    private PfbxPriceBean priceBean;
    private PfbxPriceBean otherBean;
    private Map<Integer, PfbxAreaBean> areas = new HashMap<Integer, PfbxAreaBean>();
    private Map<String, PfbxPositionBean> positions = new HashMap<String, PfbxPositionBean>();
    private Map<Integer, PfbxUrlBean> urls = new HashMap<Integer, PfbxUrlBean>();
    private Map<Integer, PfbxSizeBean> sizes = new HashMap<Integer, PfbxSizeBean>();

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
    public String getPriceName() {
        return priceName;
    }
    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }
    public String getPriceType() {
        return priceType;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
    public PfbxAssignPriceBean getAssignPriceBean() {
        return assignPriceBean;
    }
    public void setAssignPriceBean(PfbxAssignPriceBean assignPriceBean) {
        this.assignPriceBean = assignPriceBean;
    }
    public PfbxPriceBean getPriceBean() {
        return priceBean;
    }
    public void setPriceBean(PfbxPriceBean priceBean) {
        this.priceBean = priceBean;
    }
    public PfbxPriceBean getOtherBean() {
        return otherBean;
    }
    public void setOtherBean(PfbxPriceBean otherBean) {
        this.otherBean = otherBean;
    }
    public Map<Integer, PfbxAreaBean> getAreas() {
        return areas;
    }
    public void setAreas(Map<Integer, PfbxAreaBean> areas) {
        this.areas = areas;
    }
    public Map<String, PfbxPositionBean> getPositions() {
        return positions;
    }
    public void setPositions(Map<String, PfbxPositionBean> positions) {
        this.positions = positions;
    }
    public Map<Integer, PfbxUrlBean> getUrls() {
        return urls;
    }
    public void setUrls(Map<Integer, PfbxUrlBean> urls) {
        this.urls = urls;
    }
    public Map<Integer, PfbxSizeBean> getSizes() {
        return sizes;
    }
    public void setSizes(Map<Integer, PfbxSizeBean> sizes) {
        this.sizes = sizes;
    }
}
