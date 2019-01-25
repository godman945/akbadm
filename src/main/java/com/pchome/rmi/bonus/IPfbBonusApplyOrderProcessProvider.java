package com.pchome.rmi.bonus;

public interface IPfbBonusApplyOrderProcessProvider {

	public String pfbApplyOrderCreate(String pfbId,String applyMoney,String memberId,String address);
	
	public Integer pfbApprovePayment(String memberId,String pfbCustomerId,String orderId,String ip);

}