package com.pchome.akbadm.db.vo.ad;

public class AdmBrandCorrespondVO {
	
	private int id; // 流水號
	private String brand_eng; // 英文關鍵字
	private String brand_ch; // 中文關鍵字
	private String update_date; // 更新時間
	private String create_date; // 新增時間

	private String queryString; // 查詢字串
	private String checkboxButton; // 勾選的項目
	private int page = 1; // 第幾頁(初始預設第1頁)
	private int pageSize = 10; // 每頁筆數(初始預設每頁N筆)
	private int totalPage = 0; // 總頁數
	private int totalCount = 0; // 資料總筆數
	private String flag; // 判斷使用
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getBrand_eng() {
		return brand_eng;
	}
	
	public void setBrand_eng(String brand_eng) {
		this.brand_eng = brand_eng;
	}
	
	public String getBrand_ch() {
		return brand_ch;
	}
	
	public void setBrand_ch(String brand_ch) {
		this.brand_ch = brand_ch;
	}
	
	public String getUpdate_date() {
		return update_date;
	}
	
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	
	public String getCreate_date() {
		return create_date;
	}
	
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public String getCheckboxButton() {
		return checkboxButton;
	}
	
	public void setCheckboxButton(String checkboxButton) {
		this.checkboxButton = checkboxButton;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
