package com.pchome.akbadm.struts2.ajax.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.service.order.PfpOrderService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class OrderAjax extends BaseCookieAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8200638296399157701L;
	private PfpOrderService pfpOrderService;
	
	private String ordNo;						// 訂單編號
	private String account;						// 帳戶名稱或編號
	private String ordStrDate;					// 訂單開始日期
	private String ordEndDate;					// 訂單結速日期
	private String ordStatus;					// 訂單狀態
	private List<PfpOrder> pfpOrderDetail;		// 交易明細列表
	private HashMap<String,List<Integer>> totalTrans;			// 交易明細總計

	private int total = 0;
	private int B1Count = 0;
	private int B1Total = 0;
	private int B2Count = 0;
	private int B2Total = 0;
	private int B3Count = 0;
	private int B3Total = 0;
	private int B4Count = 0;
	private int B4Total = 0;
	private int R3Count = 0;
	private int R3Total = 0;
	
	public String orderQueryAjax() throws Exception{
		
		pfpOrderDetail = pfpOrderService.findPfpOrder(ordNo, account, ordStrDate, ordEndDate, ordStatus);
		
		
		for(PfpOrder detail:pfpOrderDetail){
			

			total +=(int)detail.getOrderPrice();
			
			if(detail.getStatus().substring(0,2).equals("B1")){
				B1Count++;
				B1Total += (int)detail.getOrderPrice();
			}
			
			if(detail.getStatus().substring(0,2).equals("B2")){
				B2Count++;
				B2Total += (int)detail.getOrderPrice();
			}
			
			if(detail.getStatus().substring(0,2).equals("B3")){
				B3Count++;
				B3Total += (int)detail.getOrderPrice();
			}
			
			if(detail.getStatus().substring(0,2).equals("B4") || detail.getStatus().substring(0,2).equals("B5")){
				B4Count++;
				B4Total += (int)detail.getOrderPrice();
			}
			
			if(detail.getStatus().substring(0,2).equals("R3")){
				R3Count++;
				R3Total += (int)detail.getOrderPrice();
			}
		}
		
		return SUCCESS;
	}
	
	
	
	
	public List<PfpOrder> getPfpOrderDetail() {
		return pfpOrderDetail;
	}
	public void setPfpOrderService(PfpOrderService pfpOrderService) {
		this.pfpOrderService = pfpOrderService;
	}
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setOrdStrDate(String ordStrDate) {
		this.ordStrDate = ordStrDate;
	}
	public void setOrdEndDate(String ordEndDate) {
		this.ordEndDate = ordEndDate;
	}
	public void setOrdStatus(String ordStatus) {
		this.ordStatus = ordStatus;
	}


	public int getTotal() {
		return total;
	}

	public HashMap<String, List<Integer>> getTotalTrans() {
		return totalTrans;
	}

	public int getB1Count() {
		return B1Count;
	}

	public int getB1Total() {
		return B1Total;
	}

	public int getB2Count() {
		return B2Count;
	}

	public int getB2Total() {
		return B2Total;
	}

	public int getB3Count() {
		return B3Count;
	}

	public int getB3Total() {
		return B3Total;
	}

	public int getB4Count() {
		return B4Count;
	}

	public int getB4Total() {
		return B4Total;
	}

	public int getR3Count() {
		return R3Count;
	}

	public int getR3Total() {
		return R3Total;
	}
	
}
