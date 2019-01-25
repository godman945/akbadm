package com.pchome.akbadm.struts2.action.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmBrandCorrespond;
import com.pchome.akbadm.db.service.admBrandCorrespond.IAdmBrandCorrespondService;
import com.pchome.akbadm.db.vo.ad.AdmBrandCorrespondVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class AdmBrandCorrespondApi extends BaseCookieAction{
	
	private IAdmBrandCorrespondService admBrandCorrespondService;
	// 查詢結果
	private List<AdmBrandCorrespondVO> dataList = new ArrayList<AdmBrandCorrespondVO>();
	
	private String message;
	private Map<String,Object> dataMap;
	
	// api用
	private String brandValue = ""; // 傳進來的參數為 eng/ch/all
	
	// 下載報表相關
	public static final String FILE_TYPE = ".csv";
	private InputStream downloadFileStream; // input stream
	private String downloadFileName; // 下載檔名
	private String hiddenQueryString = ""; // 查詢字串
	private String hiddenCheckboxVal = ""; // 勾選的項目

	// 頁面操作參數
	private String queryString = ""; // 查詢字串
	private String checkboxButton = ""; // 勾選的項目

	// 換頁參數
	private int page = 1; // 第幾頁(初始預設第1頁)
	private int pageSize = 10; //每頁筆數(初始預設每頁N筆)
	private int totalPage = 0; // 總頁數
	private int totalCount = 0; // 資料總筆數
		
	// 新增頁使用參數
	private String brandEng;
	private String brandCh;
	
	private int id;
	
	public String execute() throws Exception {
		AdmBrandCorrespondVO vo = new AdmBrandCorrespondVO();
		vo.setQueryString(queryString);
		vo.setCheckboxButton(checkboxButton);
		vo.setPage(page);
		vo.setPageSize(pageSize);
		
		dataList = admBrandCorrespondService.getBrandCorrespondList(vo);

		totalPage = vo.getTotalPage();
		totalCount = vo.getTotalCount();
		return SUCCESS;
	}

	/**
	 * 點選新增按鈕後，到新增頁時執行部分
	 * @return
	 * @throws Exception
	 */
	public String admBrandCorrespondAddAction() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * 新增頁點選儲存
	 * @return
	 * @throws Exception
	 */
	public String admBrandCorrespondSave() throws Exception {
		//dataMap中的資料將會被Struts2轉換成JSON字串，所以用Map<String,Object>
		dataMap = new HashMap<String, Object>();
		
		AdmBrandCorrespond vo = new AdmBrandCorrespond();
		vo.setBrandEng(brandEng);
		vo.setBrandCh(brandCh);
		int count = admBrandCorrespondService.checkBrandCorrespondTableData(vo);
		
		if (count != 0) {
			dataMap.put("status", "ERROR");
			dataMap.put("msg", "資料已重複。");
			return SUCCESS;
		} else {
			admBrandCorrespondService.insertBrandCorrespond(vo);
			dataMap.put("status", "SUCCESS");
			return SUCCESS;
		}
	}
	
	/**
	 * 點選修改按鈕後，到修改頁時執行部分
	 * @return
	 * @throws Exception
	 */
	public String admBrandCorrespondModifyAction() throws Exception {
		AdmBrandCorrespondVO vo = new AdmBrandCorrespondVO();
		vo.setId(id);
		dataList = admBrandCorrespondService.getBrandCorrespondList(vo);
		return SUCCESS;
	}
	
	/**
	 * 修改頁點選儲存
	 * @return
	 * @throws Exception
	 */
	public String admBrandCorrespondUpdate() throws Exception {
		//dataMap中的資料將會被Struts2轉換成JSON字串，所以用Map<String,Object>
		dataMap = new HashMap<String, Object>();
		
		AdmBrandCorrespond vo = new AdmBrandCorrespond();
		vo.setId(id);
		vo.setBrandEng(brandEng);
		vo.setBrandCh(brandCh);
		int count = admBrandCorrespondService.checkBrandCorrespondTableData(vo);
		
		if (count != 0) {
			dataMap.put("status", "ERROR");
			dataMap.put("msg", "資料已重複。");
			return SUCCESS;
		} else {
			admBrandCorrespondService.updateBrandCorrespond(vo);
			dataMap.put("status", "SUCCESS");
			return SUCCESS;
		}
	}
	
	/**
	 * 刪除資料
	 * @return
	 * @throws Exception
	 */
	public String admBrandCorrespondDelete() throws Exception {
		AdmBrandCorrespond vo = new AdmBrandCorrespond();
		vo.setId(id);
		admBrandCorrespondService.deleteBrandCorrespondData(vo);
		return SUCCESS;
	}
	
	/**
	 * 下載此報表
	 * @return 
	 * @throws Exception
	 */
	public String makeDownloadReportData() throws Exception{
		AdmBrandCorrespondVO vo = new AdmBrandCorrespondVO();
		vo.setQueryString(hiddenQueryString);
		vo.setCheckboxButton(hiddenCheckboxVal);
		vo.setFlag("download");
		
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
		String filename = "品牌對應關鍵字_" + dformat.format(new Date()) + FILE_TYPE;		

		StringBuffer content = admBrandCorrespondService.makeDownloadReportData(vo);
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > -1) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));

		//處理 BOM 開頭要加上 "\uFEFF"
		downloadFileStream = new ByteArrayInputStream(("\uFEFF" + content.toString()).getBytes("UTF-16LE"));

		return SUCCESS;
	}
	
	/**
	 * 提供讓別人打的api，取得品牌對應資料
	 * @return
	 * @throws Exception
	 */
	public String getBrandCorrespond() throws Exception {
		//dataMap中的資料將會被Struts2轉換成JSON字串，所以用Map<String,Object>
		dataMap = new HashMap<String, Object>();
		
		// 如果沒傳或傳的非all/eng/ch值，則傳空json出去
		if (brandValue.isEmpty() || (!"all".equalsIgnoreCase(brandValue) && !"eng".equalsIgnoreCase(brandValue)
				&& !"ch".equalsIgnoreCase(brandValue))) {
			return SUCCESS;
		}
		
		AdmBrandCorrespondVO vo = new AdmBrandCorrespondVO();
		vo.setCheckboxButton(brandValue);
		vo.setFlag("api");
		dataMap = admBrandCorrespondService.getBrandCorrespondListApi(vo);
		
		return SUCCESS;
	}
	
	/**
	 * 設定每頁幾筆
	 * @return
	 */
	public List<String> getPageSizeList() {
		List<String> list = new ArrayList<String>();
		list.add("10");
		list.add("50");
		list.add("100");
		list.add("200");
		return list;
	}

	public IAdmBrandCorrespondService getAdmBrandCorrespondService() {
		return admBrandCorrespondService;
	}

	public void setAdmBrandCorrespondService(IAdmBrandCorrespondService admBrandCorrespondService) {
		this.admBrandCorrespondService = admBrandCorrespondService;
	}

	public List<AdmBrandCorrespondVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdmBrandCorrespondVO> dataList) {
		this.dataList = dataList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
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

	public String getBrandEng() {
		return brandEng;
	}

	public void setBrandEng(String brandEng) {
		this.brandEng = brandEng;
	}

	public String getBrandCh() {
		return brandCh;
	}

	public void setBrandCh(String brandCh) {
		this.brandCh = brandCh;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public void setDownloadFileStream(InputStream downloadFileStream) {
		this.downloadFileStream = downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public String getHiddenQueryString() {
		return hiddenQueryString;
	}

	public void setHiddenQueryString(String hiddenQueryString) {
		this.hiddenQueryString = hiddenQueryString;
	}

	public String getHiddenCheckboxVal() {
		return hiddenCheckboxVal;
	}

	public void setHiddenCheckboxVal(String hiddenCheckboxVal) {
		this.hiddenCheckboxVal = hiddenCheckboxVal;
	}

	public String getBrandValue() {
		return brandValue;
	}

	public void setBrandValue(String brandValue) {
		this.brandValue = brandValue;
	}

}
