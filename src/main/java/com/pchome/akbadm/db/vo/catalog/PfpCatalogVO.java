package com.pchome.akbadm.db.vo.catalog;

public class PfpCatalogVO {

	private String pfpCustomerInfoId;
	
	private String queryString = ""; // 預設為空
	private int pageNo = 1;          // 初始化目前頁數
	private int pageSize = 10;       // 初始化每頁幾筆
	private int pageCount = 0;       // 初始化共幾頁
	private int totalCount = 0;      // 初始化共幾筆
	private boolean paginationFlag = true; // 判斷是否做分頁，預設是
	
	private String catalogSeq;        // 商品目錄ID
	private String catalogName;       // 商品目錄名稱
	private String catalogType;       // 商品目錄類型(1:一般購物類, 2:訂房住宿類, 3:交通航班類, 4:房產租售類)
	private String catalogTypeName;   // 商品目錄類型中文
	private String catalogUploadType; // 上傳方式(1:檔案上傳, 2:自動排程上傳, 3:賣場網址上傳, 4:手動上傳)
	private String catalogUploadTypeName; //上傳方式中文
	private String uploadContent;     // 更新內容(檔名或網址)
	private int catalogProdNum;       // 所有商品數量
	private String updateDatetime;    // 執行更新時間
	private String successNum;        // 成功筆數
	private String errorNum;          // 失敗筆數
	private String catalogUploadLogSeq; // 更新紀錄編號
	private String nextUpdateDatetime;// 下次執行更新時間
	private String uploadStatus; // 資料上傳狀態(0:未上傳 (預設值), 1:上傳中, 2:上傳完成)
	
	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
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

	public String getCatalogSeq() {
		return catalogSeq;
	}

	public void setCatalogSeq(String catalogSeq) {
		this.catalogSeq = catalogSeq;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCatalogType() {
		return catalogType;
	}

	public void setCatalogType(String catalogType) {
		this.catalogType = catalogType;
	}

	public String getCatalogUploadType() {
		return catalogUploadType;
	}

	public void setCatalogUploadType(String catalogUploadType) {
		this.catalogUploadType = catalogUploadType;
	}

	public String getUploadContent() {
		return uploadContent;
	}

	public void setUploadContent(String uploadContent) {
		this.uploadContent = uploadContent;
	}

	public String getUpdateDatetime() {
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public String getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(String successNum) {
		this.successNum = successNum;
	}

	public String getErrorNum() {
		return errorNum;
	}

	public void setErrorNum(String errorNum) {
		this.errorNum = errorNum;
	}

	public String getCatalogTypeName() {
		return catalogTypeName;
	}

	public void setCatalogTypeName(String catalogTypeName) {
		this.catalogTypeName = catalogTypeName;
	}

	public String getCatalogUploadTypeName() {
		return catalogUploadTypeName;
	}

	public void setCatalogUploadTypeName(String catalogUploadTypeName) {
		this.catalogUploadTypeName = catalogUploadTypeName;
	}

	public String getNextUpdateDatetime() {
		return nextUpdateDatetime;
	}

	public void setNextUpdateDatetime(String nextUpdateDatetime) {
		this.nextUpdateDatetime = nextUpdateDatetime;
	}

	public boolean isPaginationFlag() {
		return paginationFlag;
	}

	public void setPaginationFlag(boolean paginationFlag) {
		this.paginationFlag = paginationFlag;
	}

	public String getCatalogUploadLogSeq() {
		return catalogUploadLogSeq;
	}

	public void setCatalogUploadLogSeq(String catalogUploadLogSeq) {
		this.catalogUploadLogSeq = catalogUploadLogSeq;
	}

	public int getCatalogProdNum() {
		return catalogProdNum;
	}

	public void setCatalogProdNum(int catalogProdNum) {
		this.catalogProdNum = catalogProdNum;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	
}