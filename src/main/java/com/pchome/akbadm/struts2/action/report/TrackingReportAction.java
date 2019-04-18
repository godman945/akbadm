package com.pchome.akbadm.struts2.action.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.report.ITrackingReportService;
import com.pchome.akbadm.db.vo.report.PfpCodeTrackingVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class TrackingReportAction extends BaseCookieAction {
	
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private ITrackingReportService trackingReportService;
	private List<PfpCodeTrackingVO> dataList = new ArrayList<>(); // 查詢結果
	
	private Map<String, String> pfdCustomerInfoIdMap; // 經銷商下拉選單

	private String pfdCustomerInfoId; // 經銷商名稱
	private String pfpCustomerInfoId; // 帳戶編號
	private String pfpCustomerInfoName; // 帳戶名稱
	private String status = "1"; // 狀態
	private String message;
	
	private int pageNo = 1;     // 初始化目前頁數
    private int pageSize = 50;  // 初始化每頁幾筆
    private int pageCount = 0;  // 初始化共幾頁
    private int totalCount = 0; // 初始化共幾筆
	
	@Override
    public String execute() throws Exception {
    	return SUCCESS;
    }

	/**
	 * 查詢明細
	 * @return
	 * @throws Exception
	 */
	public String trackingReportDetail() throws Exception {
		PfpCodeTrackingVO pfpCodeTrackingVO = new PfpCodeTrackingVO();
		pfpCodeTrackingVO.setPfdCustomerInfoId(pfdCustomerInfoId);
		pfpCodeTrackingVO.setPfpCustomerInfoId(pfpCustomerInfoId);
		pfpCodeTrackingVO.setPfpCustomerInfoName(pfpCustomerInfoName);
		pfpCodeTrackingVO.setStatus(status);
		
		pfpCodeTrackingVO.setPageNo(pageNo);
		pfpCodeTrackingVO.setPageSize(pageSize);
		
		dataList = trackingReportService.getTrackingReportDetail(pfpCodeTrackingVO);
		
		if (dataList.size() == 0) {
			message = "查無資料。";
		}
		
		pageNo = pfpCodeTrackingVO.getPageNo();
		pageCount = pfpCodeTrackingVO.getPageCount();
		totalCount = pfpCodeTrackingVO.getTotalCount();
		return SUCCESS;
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
		list.add("50");
		list.add("100");
		list.add("150");
		return list;
	}
	
	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setTrackingReportService(ITrackingReportService trackingReportService) {
		this.trackingReportService = trackingReportService;
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

	public List<PfpCodeTrackingVO> getDataList() {
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