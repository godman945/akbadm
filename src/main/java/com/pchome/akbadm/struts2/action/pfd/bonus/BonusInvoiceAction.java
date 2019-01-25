package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.DateValueUtil;

public class BonusInvoiceAction extends BaseCookieAction{

	
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private List<PfdCustomerInfo> pfdCustomerInfo;
	
	
	private String invoiceStrDate;
	private String invoiceEndDate;

	
	public String execute(){
		
		pfdCustomerInfo = pfdCustomerInfoService.findPfdValidCustomerInfo();
		
		invoiceStrDate = DateValueUtil.getInstance().getDateYear()+"-"+DateValueUtil.getInstance().getDateMonthO();
		invoiceEndDate = DateValueUtil.getInstance().getDateYear()+"-"+DateValueUtil.getInstance().getDateMonthO();
		
		return SUCCESS;
	}
	
	
	
	

	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public List<PfdCustomerInfo> getPfdCustomerInfo() {
		return pfdCustomerInfo;
	}

	public String getInvoiceStrDate() {
		return invoiceStrDate;
	}

	public String getInvoiceEndDate() {
		return invoiceEndDate;
	}

	
	
	
}
