package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class IncomePfp extends AAdmBonus{

	@Override
	protected EnumAdmBonusDetailItem getBonusItem() {
		// TODO Auto-generated method stub
		return EnumAdmBonusDetailItem.INCOME_PFP;
	}
	
	@Override
	protected float findSaveBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		float pfpSaveBonus = 0;
		float pfpSave = admRecognizeDetailService.findPfbAdPvClkPrice(null, EnumOrderType.SAVE.getTypeTag(), transDate, transDate);
		float totalAdvanceRefundPrice = pfpRefundOrderService.findTotalRefundPrice(transDate, transDate, EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
		pfpSave = pfpSave - totalAdvanceRefundPrice;
		if(pfpSave > 0){
			pfpSaveBonus = pfpSave * (admBonusSet.getPfpPercent()/100);
		}		
		
		return pfpSaveBonus;
	}

	@Override
	protected float findFreeBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		float pfpFreeBonus = 0;
		float pfpFree = admRecognizeDetailService.findPfbAdPvClkPriceOrderTypeForFree(null, transDate, transDate);
		if(pfpFree > 0){
			pfpFreeBonus = pfpFree * (admBonusSet.getPfpPercent()/100);
		}
		return pfpFreeBonus;
	}

	@Override
	protected float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
				float pfpPaidBonus = 0;
				float pfpPaid = admRecognizeDetailService.findPfbAdPvClkPrice(null, EnumOrderType.VIRTUAL.getTypeTag(), transDate, transDate);
				float totalLaterRefundPrice = pfpRefundOrderService.findTotalRefundPrice(transDate, transDate, EnumPfdAccountPayType.LATER.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
				pfpPaid = pfpPaid - totalLaterRefundPrice;
				if(pfpPaid > 0){
					pfpPaidBonus = pfpPaid * (admBonusSet.getPfpPercent()/100);
				}
				return pfpPaidBonus;
	}
	
	

}