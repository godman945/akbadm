package com.pchome.akbadm.struts2.action.order;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.service.order.PfpOrderService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class OrderAction extends BaseCookieAction{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5359614258084333347L;
	private String billingServer;				// 金流server
	private String ordStrDate;					// 訂單開始日期
	private String ordEndDate;					// 訂單結速日期
	private String billingUrl;					// 訂單查詢URL
	

	public String execute() throws Exception{
		
		ordEndDate = DateValueUtil.getInstance().dateToString(new Date());
		ordStrDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);
 
		billingUrl = billingServer+"orderDetail.html?orderSno=";
		
		return SUCCESS;
	}


	public void setBillingServer(String billingServer) {
		this.billingServer = billingServer;
	}

	public String getOrdStrDate() {
		return ordStrDate;
	}

	public String getOrdEndDate() {
		return ordEndDate;
	}

	public String getBillingUrl() {
		return billingUrl;
	}


}
