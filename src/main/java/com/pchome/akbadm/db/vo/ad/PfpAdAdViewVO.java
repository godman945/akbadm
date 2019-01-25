package com.pchome.akbadm.db.vo.ad;

public class PfpAdAdViewVO {

	private String adActionSeq = "";
	private String adActionName = "";
	private String adType = "";
	private String adGroupSeq = "";
	private String adGroupName = "";
	private int adGroupStatus = 0;
	private String adGroupStatusDesc = "";
	private String adSeq = "";
	private int adStatus = 0;
	private String adStatusDesc = "";
	private String adPvclkDevice = "";
	private int adPv = 0;
	private int adClk = 0;
	private float adClkRate = 0;
	private float adClkPrice = 0;
	private float adClkPriceAvg = 0;
	private int adInvalidClk = 0;
	private float adInvalidClkPrice = 0;
	private String adHtml = "";
	private String adTemplateNo = "";
	private String adRejectReason = "";
	private String adCreateTime = "";
	private String memberId = "";
	private String customerInfoTitle = "";
	private String realUrl = "";
	private String img = "";
	private String originalImg = "";
	private String adStyle = "";
	private String imgWidth = "";
	private String imgHeight = "";
	private String showUrl = "";
	private String title = "";
	private String adCategoryName;
	private String adCategoryCode;
	private String adCategoryName2;
	private String adCategoryName3;
	private String content;
	
	/** 圖像(html5)廣告預覽專用 **/
	private String html5Tag;
	private String zipTitle;
	
	public String getAdActionSeq() {
		return adActionSeq;
	}
	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}
	public String getAdActionName() {
		return adActionName;
	}
	public void setAdActionName(String adActionName) {
		this.adActionName = adActionName;
	}
	public String getAdType() {
		return adType;
	}
	public void setAdType(String adType) {
		this.adType = adType;
	}
	public String getAdGroupSeq() {
		return adGroupSeq;
	}
	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}
	public String getAdGroupName() {
		return adGroupName;
	}
	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}
	public int getAdGroupStatus() {
		return adGroupStatus;
	}
	public void setAdGroupStatus(int adGroupStatus) {
		this.adGroupStatus = adGroupStatus;
	}
	public String getAdGroupStatusDesc() {
		return adGroupStatusDesc;
	}
	public void setAdGroupStatusDesc(String adGroupStatusDesc) {
		this.adGroupStatusDesc = adGroupStatusDesc;
	}
	public String getAdSeq() {
		return adSeq;
	}
	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}
	public int getAdStatus() {
		return adStatus;
	}
	public void setAdStatus(int adStatus) {
		this.adStatus = adStatus;
	}
	public String getAdStatusDesc() {
		return adStatusDesc;
	}
	public void setAdStatusDesc(String adStatusDesc) {
		this.adStatusDesc = adStatusDesc;
	}
	public String getAdPvclkDevice() {
		return adPvclkDevice;
	}
	public void setAdPvclkDevice(String adPvclkDevice) {
		this.adPvclkDevice = adPvclkDevice;
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
	public float getAdClkRate() {
		return adClkRate;
	}
	public void setAdClkRate(float adClkRate) {
		this.adClkRate = adClkRate;
	}
	public float getAdClkPrice() {
		return adClkPrice;
	}
	public void setAdClkPrice(float adClkPrice) {
		this.adClkPrice = adClkPrice;
	}
	public float getAdClkPriceAvg() {
		return adClkPriceAvg;
	}
	public void setAdClkPriceAvg(float adClkPriceAvg) {
		this.adClkPriceAvg = adClkPriceAvg;
	}
	public int getAdInvalidClk() {
		return adInvalidClk;
	}
	public void setAdInvalidClk(int adInvalidClk) {
		this.adInvalidClk = adInvalidClk;
	}
	public float getAdInvalidClkPrice() {
		return adInvalidClkPrice;
	}
	public void setAdInvalidClkPrice(float adInvalidClkPrice) {
		this.adInvalidClkPrice = adInvalidClkPrice;
	}
	public String getAdHtml() {
		return adHtml;
	}
	public void setAdHtml(String adHtml) {
		this.adHtml = adHtml;
	}
	public String getAdTemplateNo() {
		return adTemplateNo;
	}
	public void setAdTemplateNo(String adTemplateNo) {
		this.adTemplateNo = adTemplateNo;
	}
	public String getAdRejectReason() {
		return adRejectReason;
	}
	public void setAdRejectReason(String adRejectReason) {
		this.adRejectReason = adRejectReason;
	}
	public String getAdCreateTime() {
		return adCreateTime;
	}
	public void setAdCreateTime(String adCreateTime) {
		this.adCreateTime = adCreateTime;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}
	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}
	public String getImgWidth() {
		return imgWidth;
	}
	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}
	public String getImgHeight() {
		return imgHeight;
	}
	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getOriginalImg() {
		return originalImg;
	}
	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
	}
	public String getAdStyle() {
		return adStyle;
	}
	public void setAdStyle(String adStyle) {
		this.adStyle = adStyle;
	}
	public String getShowUrl() {
		return showUrl;
	}
	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}
	public String getRealUrl() {
		return realUrl;
	}
	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAdCategoryName() {
		return adCategoryName;
	}
	public void setAdCategoryName(String adCategoryName) {
		this.adCategoryName = adCategoryName;
	}
	public String getAdCategoryCode() {
		return adCategoryCode;
	}
	public void setAdCategoryCode(String adCategoryCode) {
		this.adCategoryCode = adCategoryCode;
	}
	public String getAdCategoryName2() {
		return adCategoryName2;
	}
	public void setAdCategoryName2(String adCategoryName2) {
		this.adCategoryName2 = adCategoryName2;
	}
	public String getAdCategoryName3() {
		return adCategoryName3;
	}
	public void setAdCategoryName3(String adCategoryName3) {
		this.adCategoryName3 = adCategoryName3;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHtml5Tag() {
		return html5Tag;
	}
	public void setHtml5Tag(String html5Tag) {
		this.html5Tag = html5Tag;
	}
	public String getZipTitle() {
		return zipTitle;
	}
	public void setZipTitle(String zipTitle) {
		this.zipTitle = zipTitle;
	}
}
