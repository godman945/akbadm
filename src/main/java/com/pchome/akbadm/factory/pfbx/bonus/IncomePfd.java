package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;
import com.pchome.enumerate.recognize.EnumOrderType;

public class IncomePfd extends AAdmBonus{

	@Override
	protected EnumAdmBonusDetailItem getBonusItem() {
		// TODO Auto-generated method stub
		//return EnumAdmBonusDetailItem.INCOME_PFD;
		//20150908 移除這個費用　nico
		return null;
	}

	@Override
	protected float findSaveBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		float pfdSaveBonus = 0;
		float pfdSave = admRecognizeDetailService.findPfdAdPvClkPrice(null, EnumOrderType.SAVE.getTypeTag(), transDate, transDate);
		
		if(pfdSave > 0){
			pfdSaveBonus = pfdSave * (admBonusSet.getPfpPercent()/100);
		}
		
		return pfdSaveBonus;
	}

	@Override
	protected float findFreeBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		float pfdFreeBonus = 0;
		float pfdFree = admRecognizeDetailService.findPfdAdPvClkPriceOrderTypeForFree(null, transDate, transDate);
		
		if(pfdFree > 0){
			pfdFreeBonus = pfdFree * (admBonusSet.getPfpPercent()/100);
		}
		
		return pfdFreeBonus;
	}

	@Override
	protected float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		float pfdPostpaidBonus = 0;
		float pfdPostpaid = admRecognizeDetailService.findPfdAdPvClkPrice(null, EnumOrderType.VIRTUAL.getTypeTag(), transDate, transDate);
		
		if(pfdPostpaid > 0){
			pfdPostpaidBonus = pfdPostpaid * (admBonusSet.getPfpPercent()/100);
		}
		
		return pfdPostpaidBonus;
	}

}
