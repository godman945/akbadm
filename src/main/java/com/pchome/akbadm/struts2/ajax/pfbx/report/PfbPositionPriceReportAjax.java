package com.pchome.akbadm.struts2.ajax.pfbx.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.pfbx.report.IPfbPositionPriceReportService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;


public class PfbPositionPriceReportAjax extends BaseCookieAction {
	
	private static final long serialVersionUID = 1L;
	private IPfbPositionPriceReportService pfbPositionPriceReportService;
	private IAdmAccesslogService admAccesslogService;
	
	private String customerInfoId;
	private String pid;
	private String pprice;
	private Map<String,Object> dataMap; 
	
	public String updatePriceReportAjax() throws Exception {
		/*
		 * 命名全小寫原因，Struts2的action變數名稱，首字母小寫次字母大寫。
		 * 當第二個字母為大寫的時候，我們用eclipse自動自成的getter和setter中，首字母也是小寫，
		 * 但前臺在取的時候，取的getter的首字母是大寫的，所以會取不到值。
		 */
		log.info(">>> pid = " + pid);
		log.info(">>> pprice = " + pprice);

		//dataMap中的資料將會被Struts2轉換成JSON字串，所以用Map<String,Object>
		dataMap = new HashMap<String, Object>();
		
		//前端已擋，正常操作不會進此部分，輸入空值或非純數字、超過8碼
		if (StringUtils.isBlank(pprice) || !StringUtils.isNumeric(pprice)) {
			dataMap.put("status", "ERROR");
			dataMap.put("msg", "出價請輸入整數數字。");
			return SUCCESS;
		}else if(pprice.length() > 8){
			dataMap.put("status", "ERROR");
			dataMap.put("msg", "出價請輸入8位數內。");
			return SUCCESS;
		}
		
		pfbPositionPriceReportService.updatePositionPrice(pid, Integer.valueOf(pprice));
		
		//access log
		String logMsg = "版位出價異動-->版位編號：" + pid + "，出價：" + pprice;
		
		admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB, EnumAccesslogAction.AD_MONEY_MODIFY, logMsg, 
				super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, customerInfoId, 
			     null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		
		//少做一次連線DB撈取更新時間，直接取系統時間回傳前端
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss"); 
		
		dataMap.put("status", "SUCCESS");
		dataMap.put("msg", "更新成功。");
		dataMap.put("updateTime", formatter.format(now.getTime()));

		return SUCCESS;
	}

	public IPfbPositionPriceReportService getPfbPositionPriceReportService() {
		return pfbPositionPriceReportService;
	}

	public void setPfbPositionPriceReportService(IPfbPositionPriceReportService pfbPositionPriceReportService) {
		this.pfbPositionPriceReportService = pfbPositionPriceReportService;
	}

	public IAdmAccesslogService getAdmAccesslogService() {
		return admAccesslogService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
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

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

}
