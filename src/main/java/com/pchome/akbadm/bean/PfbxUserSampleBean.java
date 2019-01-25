package com.pchome.akbadm.bean;

import java.util.HashSet;
import java.util.Set;

public class PfbxUserSampleBean {
    private String id;
    private String pfbxCustomerInfoId;
    private String sampleName;
    private String adType;
    private String adwordType;
    private String borderColor;
    private String titleColor;
    private String bgColor;
    private String fontColor;
    private String urlColor;
    private String status;
    private String adbackupType;
    private String adbackupContent;
    private int positionAllowType;
    private int sizeAllowType;
    private int urlAllowType;
    private int sort;
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
    public String getSampleName() {
        return sampleName;
    }
    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
    public String getAdType() {
        return adType;
    }
    public void setAdType(String adType) {
        this.adType = adType;
    }
    public String getAdwordType() {
        return adwordType;
    }
    public void setAdwordType(String adwordType) {
        this.adwordType = adwordType;
    }
    public String getBorderColor() {
        return borderColor;
    }
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }
    public String getTitleColor() {
        return titleColor;
    }
    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }
    public String getBgColor() {
        return bgColor;
    }
    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
    public String getFontColor() {
        return fontColor;
    }
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }
    public String getUrlColor() {
        return urlColor;
    }
    public void setUrlColor(String urlColor) {
        this.urlColor = urlColor;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAdbackupType() {
        return adbackupType;
    }
    public void setAdbackupType(String adbackupType) {
        this.adbackupType = adbackupType;
    }
    public String getAdbackupContent() {
        return adbackupContent;
    }
    public void setAdbackupContent(String adbackupContent) {
        this.adbackupContent = adbackupContent;
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
    public int getSort() {
        return sort;
    }
    public void setSort(int sort) {
        this.sort = sort;
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
