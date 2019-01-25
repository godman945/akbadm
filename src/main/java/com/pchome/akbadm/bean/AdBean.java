package com.pchome.akbadm.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdBean implements Cloneable {
    private String customerInfoId;
    private String pfdCustomerInfoId;
    private String pfdUserId;
    private String priceType = "";
    private int payType;
    private int adType;
    private int adDevice;
    private String adActionId;
    private String adGroupId;
    private String adId;
    private String adKeywordId = "";
    private String adClass;
    private String[] adClasses = new String[0];
    private String adStyle;
    private String recognize;
    private Date adActionStartDate;
    private Date adActionEndDate;
    private String adAssignTadSeq = "";
    private String adSpecificPlayType = "";
    private String adPvLimitStyle;
    private String adPvLimitPeriod;
    private int adPvLimitAmount;
    private int admArw;
    private String[] categoryCodes = new String[0];
    private String adActionSex = "";
    private int adActionStartAge;
    private int adActionEndAge;
    private String adActionTime = "";
    private float pfpCustomerInfoRemain;
    private float adActionControlPrice;
    private float adActionMaxPrice;
    private float adSearchPrice;
    private float adChannelPrice;
    private float adBidPrice;
    private float adKeywordSearchPrice;
    private float adKeywordChannelPrice;
    private float adKeywordTempPrice;
    private int qualityGrade;
    private int adSearchCount;
    private int adChannelCount;
    private int adBidCount;
    private int adKeywordSearchCount;
    private int adKeywordChannelCount;
    private int adPv;
    private int adClk;
    private int count;
    private boolean offlineFlag;
    private Map<String, AdDetailBean> adDetailMap = new HashMap<String, AdDetailBean>();

    @Override
    @SuppressWarnings("unchecked")
    public AdBean clone() throws CloneNotSupportedException {
        AdBean adBean = (AdBean) super.clone();
        adBean.adDetailMap = (Map<String, AdDetailBean>) ((HashMap<String, AdDetailBean>) this.adDetailMap).clone();
        return adBean;
    }

    public String getCustomerInfoId() {
        return customerInfoId;
    }

    public void setCustomerInfoId(String customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public String getPfdCustomerInfoId() {
        return pfdCustomerInfoId;
    }

    public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
        this.pfdCustomerInfoId = pfdCustomerInfoId;
    }

    public String getPfdUserId() {
        return pfdUserId;
    }

    public void setPfdUserId(String pfdUserId) {
        this.pfdUserId = pfdUserId;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getAdDevice() {
        return adDevice;
    }

    public void setAdDevice(int adDevice) {
        this.adDevice = adDevice;
    }

    public String getAdActionId() {
        return adActionId;
    }

    public void setAdActionId(String adActionId) {
        this.adActionId = adActionId;
    }

    public String getAdGroupId() {
        return adGroupId;
    }

    public void setAdGroupId(String adGroupId) {
        this.adGroupId = adGroupId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdKeywordId() {
        return adKeywordId;
    }

    public void setAdKeywordId(String adKeywordId) {
        this.adKeywordId = adKeywordId;
    }

    public String getAdClass() {
        return adClass;
    }

    public void setAdClass(String adClass) {
        this.adClass = adClass;
    }

    public String[] getAdClasses() {
        return adClasses;
    }

    public void setAdClasses(String[] adClasses) {
        this.adClasses = adClasses;
    }

    public String getAdStyle() {
        return adStyle;
    }

    public void setAdStyle(String adStyle) {
        this.adStyle = adStyle;
    }

    public String getRecognize() {
        return recognize;
    }

    public void setRecognize(String recognize) {
        this.recognize = recognize;
    }

    public Date getAdActionStartDate() {
        return adActionStartDate;
    }

    public void setAdActionStartDate(Date adActionStartDate) {
        this.adActionStartDate = adActionStartDate;
    }

    public Date getAdActionEndDate() {
        return adActionEndDate;
    }

    public void setAdActionEndDate(Date adActionEndDate) {
        this.adActionEndDate = adActionEndDate;
    }

    public String getAdAssignTadSeq() {
        return adAssignTadSeq;
    }

    public void setAdAssignTadSeq(String adAssignTadSeq) {
        this.adAssignTadSeq = adAssignTadSeq;
    }

    public String getAdSpecificPlayType() {
        return adSpecificPlayType;
    }

    public void setAdSpecificPlayType(String adSpecificPlayType) {
        this.adSpecificPlayType = adSpecificPlayType;
    }

    public String getAdPvLimitStyle() {
        return adPvLimitStyle;
    }

    public void setAdPvLimitStyle(String adPvLimitStyle) {
        this.adPvLimitStyle = adPvLimitStyle;
    }

    public String getAdPvLimitPeriod() {
        return adPvLimitPeriod;
    }

    public void setAdPvLimitPeriod(String adPvLimitPeriod) {
        this.adPvLimitPeriod = adPvLimitPeriod;
    }

    public int getAdPvLimitAmount() {
        return adPvLimitAmount;
    }

    public void setAdPvLimitAmount(int adPvLimitAmount) {
        this.adPvLimitAmount = adPvLimitAmount;
    }

    public int getAdmArw() {
        return admArw;
    }

    public void setAdmArw(int admArw) {
        this.admArw = admArw;
    }

    public String[] getCategoryCodes() {
        return categoryCodes;
    }

    public void setCategoryCodes(String[] categoryCodes) {
        this.categoryCodes = categoryCodes;
    }

    public String getAdActionSex() {
        return adActionSex;
    }

    public void setAdActionSex(String adActionSex) {
        this.adActionSex = adActionSex;
    }

    public int getAdActionStartAge() {
        return adActionStartAge;
    }

    public void setAdActionStartAge(int adActionStartAge) {
        this.adActionStartAge = adActionStartAge;
    }

    public int getAdActionEndAge() {
        return adActionEndAge;
    }

    public void setAdActionEndAge(int adActionEndAge) {
        this.adActionEndAge = adActionEndAge;
    }

    public String getAdActionTime() {
        return adActionTime;
    }

    public void setAdActionTime(String adActionTime) {
        this.adActionTime = adActionTime;
    }

    public float getPfpCustomerInfoRemain() {
        return pfpCustomerInfoRemain;
    }

    public void setPfpCustomerInfoRemain(float pfpCustomerInfoRemain) {
        this.pfpCustomerInfoRemain = pfpCustomerInfoRemain;
    }

    public float getAdActionControlPrice() {
        return adActionControlPrice;
    }

    public void setAdActionControlPrice(float adActionControlPrice) {
        this.adActionControlPrice = adActionControlPrice;
    }

    public float getAdActionMaxPrice() {
        return adActionMaxPrice;
    }

    public void setAdActionMaxPrice(float adActionMaxPrice) {
        this.adActionMaxPrice = adActionMaxPrice;
    }

    public float getAdSearchPrice() {
        return adSearchPrice;
    }

    public void setAdSearchPrice(float adSearchPrice) {
        this.adSearchPrice = adSearchPrice;
    }

    public float getAdChannelPrice() {
        return adChannelPrice;
    }

    public void setAdChannelPrice(float adChannelPrice) {
        this.adChannelPrice = adChannelPrice;
    }

    public float getAdBidPrice() {
        return adBidPrice;
    }

    public void setAdBidPrice(float adBidPrice) {
        this.adBidPrice = adBidPrice;
    }

    public float getAdKeywordSearchPrice() {
        return adKeywordSearchPrice;
    }

    public void setAdKeywordSearchPrice(float adKeywordSearchPrice) {
        this.adKeywordSearchPrice = adKeywordSearchPrice;
    }

    public float getAdKeywordChannelPrice() {
        return adKeywordChannelPrice;
    }

    public void setAdKeywordChannelPrice(float adKeywordChannelPrice) {
        this.adKeywordChannelPrice = adKeywordChannelPrice;
    }

    public float getAdKeywordTempPrice() {
        return adKeywordTempPrice;
    }

    public void setAdKeywordTempPrice(float adKeywordTempPrice) {
        this.adKeywordTempPrice = adKeywordTempPrice;
    }

    public int getQualityGrade() {
        return qualityGrade;
    }

    public void setQualityGrade(int qualityGrade) {
        this.qualityGrade = qualityGrade;
    }

    public int getAdSearchCount() {
        return adSearchCount;
    }

    public void setAdSearchCount(int adSearchCount) {
        this.adSearchCount = adSearchCount;
    }

    public int getAdChannelCount() {
        return adChannelCount;
    }

    public void setAdChannelCount(int adChannelCount) {
        this.adChannelCount = adChannelCount;
    }

    public int getAdBidCount() {
        return adBidCount;
    }

    public void setAdBidCount(int adBidCount) {
        this.adBidCount = adBidCount;
    }

    public int getAdKeywordSearchCount() {
        return adKeywordSearchCount;
    }

    public void setAdKeywordSearchCount(int adKeywordSearchCount) {
        this.adKeywordSearchCount = adKeywordSearchCount;
    }

    public int getAdKeywordChannelCount() {
        return adKeywordChannelCount;
    }

    public void setAdKeywordChannelCount(int adKeywordChannelCount) {
        this.adKeywordChannelCount = adKeywordChannelCount;
    }

    public int getAdPv() {
        return adPv;
    }

    public void setAdPv(int adPv) {
        this.adPv = adPv;
    }

    public int getAdClk() {
        return adClk;
    }

    public void setAdClk(int adClk) {
        this.adClk = adClk;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isOfflineFlag() {
        return offlineFlag;
    }

    public void setOfflineFlag(boolean offlineFlag) {
        this.offlineFlag = offlineFlag;
    }

    public Map<String, AdDetailBean> getAdDetailMap() {
        return adDetailMap;
    }

    public void setAdDetailMap(Map<String, AdDetailBean> adDetailMap) {
        this.adDetailMap = adDetailMap;
    }
}
