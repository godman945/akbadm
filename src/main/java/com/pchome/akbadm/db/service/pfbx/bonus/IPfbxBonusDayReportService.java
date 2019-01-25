package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfbxBonusDayReport;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxBonusDayReportService extends IBaseService<PfbxBonusDayReport,Integer>{

	public float findPfbxSaveIncome(Date reportDate);
	
	public float findPfbxFreeIncome(Date reportDate);
	
	public float findPfbxPostpaidIncome(Date reportDate);
	
	public float findPfbxSaveExpense(Date reportDate);
	
	public float findPfbxFreeExpense(Date reportDate);
	
	public float findPfbxPostpaidExpense(Date reportDate);
	
	public float findTotalPfbxExpense(Date startDate, Date endDate);
	
	public float findPfbxTotalMonthBonus(String pfbId ,Date startDate, Date endDate);
	
	public float findPfbxTotalClkPrice(String pfbId, Date startDate, Date endDate);
	
	public Integer deletePfbxBonusDayReport(Date deleteDate);
}
