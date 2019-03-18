package com.pchome.akbadm.bean;

import java.util.HashMap;
import java.util.Map;

public class TproBean {
    private String tproId;
    private String backupTproId;
    private String prodId;
    private Map<String, TadMapBean> tadMap = new HashMap<String, TadMapBean>();
    private String backupTadId;
    private boolean auto;
    private int pageSize;
    private int startNum;
    private String adType;
    private String templateAdType;
    private String xType;
    private boolean iframe;
    private int iframeHeight;
    private int iframeWidth;
    private int positionHeight;
    private int positionWidth;
    private int logoHeight;
    private int logoWidth;
    private String html = "";

    public String getTproId() {
        return tproId;
    }
    public void setTproId(String tproId) {
        this.tproId = tproId;
    }
    public String getBackupTproId() {
        return backupTproId;
    }
    public void setBackupTproId(String backupTproId) {
        this.backupTproId = backupTproId;
    }
    public String getProdId() {
        return prodId;
    }
    public void setProdId(String prodId) {
        this.prodId = prodId;
    }
    public Map<String, TadMapBean> getTadMap() {
        return tadMap;
    }
    public void setTadMap(Map<String, TadMapBean> tadMap) {
        this.tadMap = tadMap;
    }
    public String getBackupTadId() {
        return backupTadId;
    }
    public void setBackupTadId(String backupTadId) {
        this.backupTadId = backupTadId;
    }
    public boolean isAuto() {
        return auto;
    }
    public void setAuto(boolean auto) {
        this.auto = auto;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getStartNum() {
        return startNum;
    }
    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }
    public String getAdType() {
        return adType;
    }
    public void setAdType(String adType) {
        this.adType = adType;
    }
    public String getTemplateAdType() {
        return templateAdType;
    }
    public void setTemplateAdType(String templateAdType) {
        this.templateAdType = templateAdType;
    }
    public String getxType() {
        return xType;
    }
    public void setxType(String xType) {
        this.xType = xType;
    }
    public boolean isIframe() {
        return iframe;
    }
    public void setIframe(boolean iframe) {
        this.iframe = iframe;
    }
    public int getIframeHeight() {
        return iframeHeight;
    }
    public void setIframeHeight(int iframeHeight) {
        this.iframeHeight = iframeHeight;
    }
    public int getIframeWidth() {
        return iframeWidth;
    }
    public void setIframeWidth(int iframeWidth) {
        this.iframeWidth = iframeWidth;
    }
    public int getPositionHeight() {
        return positionHeight;
    }
    public void setPositionHeight(int positionHeight) {
        this.positionHeight = positionHeight;
    }
    public int getPositionWidth() {
        return positionWidth;
    }
    public void setPositionWidth(int positionWidth) {
        this.positionWidth = positionWidth;
    }
    public int getLogoHeight() {
        return logoHeight;
    }
    public void setLogoHeight(int logoHeight) {
        this.logoHeight = logoHeight;
    }
    public int getLogoWidth() {
        return logoWidth;
    }
    public void setLogoWidth(int logoWidth) {
        this.logoWidth = logoWidth;
    }
    public String getHtml() {
        return html;
    }
    public void setHtml(String html) {
        this.html = html;
    }
}