package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.bonus.IAdmBonusDetailReportDAO;
import com.pchome.akbadm.db.pojo.AdmBonusDetailReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;
import com.pchome.enumerate.pfbx.bonus.EnumIncomeExpense;

public class AdmBonusDetailReportService extends BaseService<AdmBonusDetailReport, Integer> implements IAdmBonusDetailReportService{
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList(Date reportDate)
	{
		List<PfbxInComeReportVo> voList = new ArrayList<PfbxInComeReportVo>();
		
		List<AdmBonusDetailReport> detailList = this.getListByDate(reportDate);
		PfbxInComeReportVo vo = null;
		for(AdmBonusDetailReport detail : detailList)
		{
			vo = new PfbxInComeReportVo();
			vo.setReportdate(reportDate);
			vo.setDetailItem(detail.getDetailItem());
			vo.setDetailItemName(detail.getItemName());
			vo.setDetailIncomeExpense(detail.getIncomeExpense());
			vo.setDetailSave(detail.getSave());
			vo.setDetailFree(detail.getFree());
			vo.setDetailPostPaid(detail.getPostpaid());
			vo.setDetailTotal(detail.getTotal());
			voList.add(vo);
		}
		
		return voList;
	}
	
	public List<AdmBonusDetailReport> getListByDate(Date reportDate)
	{
		return ((IAdmBonusDetailReportDAO) dao).findAdmBonusDetailReport(reportDate);
	}

	public float findTotalClkPrice(Date reportDate) {
		
		float totalClkPrice = 0;
		List<AdmBonusDetailReport> reports  = ((IAdmBonusDetailReportDAO) dao).findAdmBonusDetailReport(reportDate);
		
		if(!reports.isEmpty()){
			
			for(AdmBonusDetailReport report:reports){
				log.info("report.getItemName="+report.getItemName());
				log.info("report.Enum="+EnumAdmBonusDetailItem.EXPENSE_PFD.getItemName());
				//if(!report.getItemName().equals(EnumAdmBonusDetailItem.EXPENSE_PFD.getItemName())){
					totalClkPrice += report.getTotal();
				//}
			}
		}
		
		return totalClkPrice;
	}
	
	public float findIncomeOrExpense(Date reportDate, EnumIncomeExpense incomeExpense) {
		
		float total = 0;
		List<AdmBonusDetailReport> reports  = ((IAdmBonusDetailReportDAO) dao).findAdmBonusDetailReport(reportDate);
		
		if(!reports.isEmpty()){
			
			for(AdmBonusDetailReport report:reports){

				if(EnumIncomeExpense.INCOME.equals(incomeExpense) && report.getIncomeExpense().equals(incomeExpense.getTag())){
					total += report.getSave();
					total += report.getPostpaid();
				}
				
				if(EnumIncomeExpense.EXPENSE.equals(incomeExpense) && report.getIncomeExpense().equals(incomeExpense.getTag())){
					total += report.getSave();
					total += report.getPostpaid();
					total += report.getFree();
				}
			}
		}
		
		return total;
	}
	
	public Integer deleteAdmBonusDetailReport(Date deleteDate) {
		return ((IAdmBonusDetailReportDAO) dao).deleteAdmBonusDetailReport(deleteDate);
	}
	
}
