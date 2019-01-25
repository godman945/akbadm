package com.pchome.akbadm.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PfbxUserOptionBean {
    private String id;
    private String pfbxCustomerInfoId;
    private String optionName;
    private int positionAllowType;
    private int sizeAllowType;
    private int urlAllowType;
    private Map<Integer, AdCategoryNewBean> allowIndustries = new HashMap<Integer, AdCategoryNewBean>(0);
    private Map<String, String> allowCusurls = new HashMap<String, String>();
    private Map<Integer, AdCategoryNewBean> blockIndustries = new HashMap<Integer, AdCategoryNewBean>(0);
    private Map<String, String> blockCusurls = new HashMap<String, String>();
    private Set<Integer> areas = new HashSet<Integer>();
    private Set<String> positions = new HashSet<String>();
    private Set<Integer> urls = new HashSet<Integer>();
    private Set<Integer> sizes = new HashSet<Integer>();

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
    public String getOptionName() {
        return optionName;
    }
    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }
    public int getPositionAllowType() {
        return positionAllowType;
    }
    public void setPositionAllowType(int positionAllowType) {
        this.positionAllowType = positionAllowType;
    }
    public int getSizeAllowType() {
        return sizeAllowType;
    }
    public void setSizeAllowType(int sizeAllowType) {
        this.sizeAllowType = sizeAllowType;
    }
    public int getUrlAllowType() {
        return urlAllowType;
    }
    public void setUrlAllowType(int urlAllowType) {
        this.urlAllowType = urlAllowType;
    }
    public Map<Integer, AdCategoryNewBean> getAllowIndustries() {
        return allowIndustries;
    }
    public void setAllowIndustries(Map<Integer, AdCategoryNewBean> allowIndustries) {
        this.allowIndustries = allowIndustries;
    }
    public Map<String, String> getAllowCusurls() {
        return allowCusurls;
    }
    public void setAllowCusurls(Map<String, String> allowCusurls) {
        this.allowCusurls = allowCusurls;
    }
    public Map<Integer, AdCategoryNewBean> getBlockIndustries() {
        return blockIndustries;
    }
    public void setBlockIndustries(Map<Integer, AdCategoryNewBean> blockIndustries) {
        this.blockIndustries = blockIndustries;
    }
    public Map<String, String> getBlockCusurls() {
        return blockCusurls;
    }
    public void setBlockCusurls(Map<String, String> blockCusurls) {
        this.blockCusurls = blockCusurls;
    }
    public Set<Integer> getAreas() {
        return areas;
    }
    public void setAreas(Set<Integer> areas) {
        this.areas = areas;
    }
    public Set<String> getPositions() {
        return positions;
    }
    public void setPositions(Set<String> positions) {
        this.positions = positions;
    }
    public Set<Integer> getUrls() {
        return urls;
    }
    public void setUrls(Set<Integer> urls) {
        this.urls = urls;
    }
    public Set<Integer> getSizes() {
        return sizes;
    }
    public void setSizes(Set<Integer> sizes) {
        this.sizes = sizes;
    }
}
