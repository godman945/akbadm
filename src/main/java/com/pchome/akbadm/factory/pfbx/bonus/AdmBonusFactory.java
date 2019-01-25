package com.pchome.akbadm.factory.pfbx.bonus;

import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;

public class AdmBonusFactory {

	private AAdmBonus incomePfb;
	private AAdmBonus incomePfd;
	private AAdmBonus incomePfp;
	private AAdmBonus expensePfd;
	private AAdmBonus expensePfb;
	
	public AAdmBonus getAdmBonus(EnumAdmBonusDetailItem item){
		
		AAdmBonus aAdmBonus = null;
		
		switch(item){
		
			//case INCOME_PFD:
				//aAdmBonus = incomePfd;
				//break;
			case INCOME_PFP:
				aAdmBonus = incomePfp;
				break;
			case INCOME_PFB:
				aAdmBonus = incomePfb;
				break;
			case EXPENSE_PFD:
				aAdmBonus = expensePfd;
				break;
			case EXPENSE_PFB:
				aAdmBonus = expensePfb;
				break;
			default:
				
				break;
		
		}
		
		return aAdmBonus;
	}
	
	
	
	public void setIncomePfb(AAdmBonus incomePfb) {
		this.incomePfb = incomePfb;
	}
	
	public void setIncomePfd(AAdmBonus incomePfd) {
		this.incomePfd = incomePfd;
	}
	
	public void setIncomePfp(AAdmBonus incomePfp) {
		this.incomePfp = incomePfp;
	}
	
	public void setExpensePfd(AAdmBonus expensePfd) {
		this.expensePfd = expensePfd;
	}
	
	public void setExpensePfb(AAdmBonus expensePfb) {
		this.expensePfb = expensePfb;
	}
	
}
