package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmBonusDetailReport;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;
import com.pchome.enumerate.pfbx.bonus.EnumIncomeExpense;

public interface IAdmBonusDetailReportService extends IBaseService<AdmBonusDetailReport,Integer>{
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList(Date reportDate);
	
	public List<AdmBonusDetailReport> getListByDate(Date reportDate);

	public float findTotalClkPrice(Date reportDate);
	
	public float findIncomeOrExpense(Date reportDate, EnumIncomeExpense incomeExpense);
	
	public Integer deleteAdmBonusDetailReport(Date deleteDate);
	
}
