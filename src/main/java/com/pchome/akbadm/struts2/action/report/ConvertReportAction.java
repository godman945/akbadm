package com.pchome.akbadm.struts2.action.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.report.IPfpCodeConvertService;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class ConvertReportAction extends BaseCookieAction {
	
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private IPfpCodeConvertService pfpCodeConvertService;
	private List<Object> dataList;// 查詢結果
	
	private Map<String, String> pfdCustomerInfoIdMap; // 經銷商下拉選單

	private String pfdCustomerInfoId; // 經銷商名稱
	private String pfpCustomerInfoId; // 帳戶編號
	private String pfpCustomerInfoName; // 帳戶名稱
	private String status = "1"; // 狀態
	private String message;
	
	private int pageNo = 1;     // 初始化目前頁數
    private int pageSize = 10;  // 初始化每頁幾筆
    private int pageCount = 0;  // 初始化共幾頁
    private int totalCount = 0; // 初始化共幾筆
    
	
	@Override
    public String execute() throws Exception {
    	return SUCCESS;
    }

	/**
	 * 轉換查詢明細
	 * @return
	 * @throws Exception
	 */
	public String convertReportDetail() throws Exception {
		
		log.info(">>> pageNo: " + pageNo);
		log.info(">>> pageSize: " + pageSize);
		log.info(">>> status: " + status);
		log.info(">>> pfdCustomerInfoId: " + pfdCustomerInfoId);
		log.info(">>> pfpCustomerInfoId: " + pfpCustomerInfoId);
		log.info(">>> pfpCustomerInfoName: " + pfpCustomerInfoName);
		
		
		PfpConvertTrackingVO convertTrackingVO = new PfpConvertTrackingVO();
		convertTrackingVO.setPfdCustomerInfoId(pfdCustomerInfoId);
		convertTrackingVO.setPfpCustomerInfoId(pfpCustomerInfoId);
		convertTrackingVO.setPfpCustomerInfoName(pfpCustomerInfoName);
		convertTrackingVO.setStatus(status);
		
		convertTrackingVO.setPageNo(pageNo);
		convertTrackingVO.setPageSize(pageSize);
		

		//撈單頁轉換資料
		List<PfpConvertTrackingVO> pfpCodeConvertList =pfpCodeConvertService.findPfpCodeConvertList(convertTrackingVO);
		dataList = new ArrayList<>();
		
		if (pfpCodeConvertList.size() <= 0) {
			message = "查無資料。";
		}
		
		for (PfpConvertTrackingVO pfpCodeConvert : pfpCodeConvertList) {
			convertTrackingVO.setConvertSeq(pfpCodeConvert.getConvertSeq());
			//ck
			int clickRangeDate = Integer.parseInt(pfpCodeConvert.getClickRangeDate());
			convertTrackingVO.setCkStartDate(calculateConvertDay(clickRangeDate));
			convertTrackingVO.setCkEndDate(calculateConvertDay(1));
			//pv
			int impRangeDate = Integer.parseInt(pfpCodeConvert.getImpRangeDate());
			convertTrackingVO.setPvStartDate(calculateConvertDay(impRangeDate));
			convertTrackingVO.setPvEndDate(calculateConvertDay(1));
			
			PfpConvertTrackingVO convertTransVO = pfpCodeConvertService.getConvertTrackingList(convertTrackingVO);
			dataList.add(convertTransVO);
		}
		
		//資料總筆數
		totalCount =  Integer.valueOf(pfpCodeConvertService.getConvertTrackingCount(convertTrackingVO));
		//總頁數
		pageCount = (int)Math.ceil((float)totalCount / pageSize);
		
		return SUCCESS;
	}
	
	
	public String calculateConvertDay(int day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -day);
		Date date = cal.getTime();
		String dateStr = sdf.format(date);
		
		return dateStr;
	}
	
	
	/**
	 * 經銷商下拉選單
	 * @return
	 */
	public Map<String, String> getPfdCustomerInfoIdMap() {
        List<PfdCustomerInfo> list = pfdCustomerInfoService.loadAll();

        pfdCustomerInfoIdMap = new TreeMap<String, String>();
        for (PfdCustomerInfo data: list) {
            pfdCustomerInfoIdMap.put(data.getCustomerInfoId(), data.getCompanyName());
        }

        return pfdCustomerInfoIdMap;
    }
	
	

	/**
	 * 設定每頁幾筆
	 * @return 
	 */
	public  List<String> getPageSizeList() {
		List<String> list = new ArrayList<String>();
		list.add("10");
		list.add("50");
		list.add("100");
		list.add("150");
		return list;
	}
	
	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setPfpCodeConvertService(IPfpCodeConvertService pfpCodeConvertService) {
		this.pfpCodeConvertService = pfpCodeConvertService;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getDataList() {
		return dataList;
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

	public String getMessage() {
		return message;
	}
	
}