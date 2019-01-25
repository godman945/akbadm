package com.pchome.akbadm.bean;

import java.util.HashSet;
import java.util.Set;

public class PfbxUserGroupBean {
    private String id;
    private String pfbxCustomerInfoId;
    private String groupName;
    private int positionAllowType;
    private int sizeAllowType;
    private int urlAllowType;
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
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
