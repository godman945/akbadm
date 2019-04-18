package com.pchome.akbadm.db.vo.report;

public class PfpCodeTrackingVO {

	private String pfdCustomerInfoId; // 經銷商編號
	private String pfdCustomerInfoName; // 經銷商名稱
	private String pfpCustomerInfoId; // 帳戶編號
	private String pfpCustomerInfoName; // 帳戶名稱
	private String trackingSeq; // 再行銷ID
	private String trackingName; // 再行銷名稱
	private String status; // 狀態
	private String certificationStatus; // 認證狀態
	private String codeType; // 代碼類型
	private int trackingRangeDate; // 追蹤天數
	
	private int pageNo = 1;     // 初始化目前頁數
    private int pageSize = 50;  // 初始化每頁幾筆
    private int pageCount = 0;  // 初始化共幾頁
    private int totalCount = 0; // 初始化共幾筆
    
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	
	public String getPfdCustomerInfoName() {
		return pfdCustomerInfoName;
	}
	
	public void setPfdCustomerInfoName(String pfdCustomerInfoName) {
		this.pfdCustomerInfoName = pfdCustomerInfoName;
	}
	
	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}
	
	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}
	
	public String getPfpCustomerInfoName() {
		return pfpCustomerInfoName;
	}
	
	public void setPfpCustomerInfoName(String pfpCustomerInfoName) {
		this.pfpCustomerInfoName = pfpCustomerInfoName;
	}
	
	public String getTrackingSeq() {
		return trackingSeq;
	}
	
	public void setTrackingSeq(String trackingSeq) {
		this.trackingSeq = trackingSeq;
	}
	
	public String getTrackingName() {
		return trackingName;
	}
	
	public void setTrackingName(String trackingName) {
		this.trackingName = trackingName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCertificationStatus() {
		return certificationStatus;
	}
	
	public void setCertificationStatus(String certificationStatus) {
		this.certificationStatus = certificationStatus;
	}
	
	public String getCodeType() {
		return codeType;
	}
	
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
	public int getTrackingRangeDate() {
		return trackingRangeDate;
	}

	public void setTrackingRangeDate(int trackingRangeDate) {
		this.trackingRangeDate = trackingRangeDate;
	}

	public int getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageCount() {
		return pageCount;
	}
	
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}