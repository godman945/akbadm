package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;

public class ExpensePfb extends AAdmBonus{

	@Override
	protected EnumAdmBonusDetailItem getBonusItem() {
		// TODO Auto-generated method stub
		return EnumAdmBonusDetailItem.EXPENSE_PFB;
	}

	@Override
	protected float findSaveBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxSaveExpense(transDate);
	}

	@Override
	protected float findFreeBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxFreeExpense(transDate);
	}

	@Override
	protected float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet) {
		// TODO Auto-generated method stub
		return pfbxBonusDayReportService.findPfbxPostpaidExpense(transDate);
	}
}