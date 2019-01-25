package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;

public class IncomePfb extends AAdmBonus{

	@Override
	protected EnumAdmBonusDetailItem getBonusItem() {
		// TODO Auto-generated method stub
		return EnumAdmBonusDetailItem.INCOME_PFB;
	}

	@Override
	protected float findSaveBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxSaveIncome(transDate);
	}

	@Override
	protected float findFreeBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxFreeIncome(transDate);
	}
	
	@Override
	protected float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxPostpaidIncome(transDate);
	}

}
