package com.pchome.akbadm.struts2.action.pfbx.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.pfbx.report.IPfbPositionPriceReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbPositionPriceReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class PfbPositionPriceReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IPfbPositionPriceReportService pfbPositionPriceReportService;
	
	//輸入的查詢參數
	private String customerInfoId;
	private String pid;
	private String pprice = "3";

	// 查詢結果
	private List<PfbPositionPriceReportVO> dataList = new ArrayList<PfbPositionPriceReportVO>();
	
	private String message = ""; //訊息
	
	public String execute() throws Exception {

		log.info(">>> customerInfoId = " + customerInfoId);
		/*
		 * 命名全小寫原因，Struts2的action變數名稱，首字母小寫次字母大寫。
		 * 當第二個字母為大寫的時候，我們用eclipse自動自成的getter和setter中，首字母也是小寫，
		 * 但前臺在取的時候，取的getter的首字母是大寫的，所以會取不到值。
		 */
		log.info(">>> pid = " + pid);
		log.info(">>> pprice = " + pprice);
		
		//前端已擋，正常操作不會進此部分，輸入空值或非純數字、超過8碼
		if (StringUtils.isBlank(pprice) || !StringUtils.isNumeric(pprice)) {
			this.message = "版位價格請輸入整數數字。";
			return SUCCESS;
		}else if(pprice.length() > 8){
			this.message = "版位價格請輸入8位數內。";
			return SUCCESS;
		}
		
		this.dataList = pfbPositionPriceReportService.getPositionPriceDataList(customerInfoId, pid, Integer.valueOf(pprice));
		if (dataList.size() == 0) {
			this.message = "查無資料！";
		}
		return SUCCESS;
	}
	
	public IPfbPositionPriceReportService getPfbPositionPriceReportService() {
		return pfbPositionPriceReportService;
	}

	public void setPfbPositionPriceReportService(IPfbPositionPriceReportService pfbPositionPriceReportService) {
		this.pfbPositionPriceReportService = pfbPositionPriceReportService;
	}
	
	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPprice() {
		return pprice;
	}

	public void setPprice(String pprice) {
		this.pprice = pprice;
	}

	public List<PfbPositionPriceReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<PfbPositionPriceReportVO> dataList) {
		this.dataList = dataList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
