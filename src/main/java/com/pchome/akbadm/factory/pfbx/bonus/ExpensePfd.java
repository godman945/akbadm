package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;

public class ExpensePfd extends AAdmBonus{

	@Override
	protected EnumAdmBonusDetailItem getBonusItem() {
		// TODO Auto-generated method stub
		return EnumAdmBonusDetailItem.EXPENSE_PFD;
	}

	@Override
	protected float findSaveBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub		
		return pfdBonusDayReportService.findPfdSaveMonthBonus(transDate);
		//return 0;
	}

	@Override
	protected float findFreeBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfdBonusDayReportService.findPfdPaidMonthBonus(transDate);
		//return 0;
	}

}