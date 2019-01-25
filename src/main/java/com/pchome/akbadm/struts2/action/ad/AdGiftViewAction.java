package com.pchome.akbadm.struts2.action.ad;


import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.freeAction.EnumGiftStyle;
import com.pchome.enumerate.freeAction.EnumPayment;

public class AdGiftViewAction extends BaseCookieAction{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EnumGiftStyle[] searchGiftStyle;
	private EnumPayment[] searchPayment;
	
	public String execute() throws Exception{
		
		searchGiftStyle = EnumGiftStyle.values();
		searchPayment = EnumPayment.values();
		
		return SUCCESS;
	}

	public EnumGiftStyle[] getSearchGiftStyle() {
		return searchGiftStyle;
	}

	public void setSearchGiftStyle(EnumGiftStyle[] searchGiftStyle) {
		this.searchGiftStyle = searchGiftStyle;
	}

	public EnumPayment[] getSearchPayment() {
		return searchPayment;
	}

	public void setSearchPayment(EnumPayment[] searchPayment) {
		this.searchPayment = searchPayment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
