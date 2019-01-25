package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdBonusDayReport;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfdBonusDayReportService extends IBaseService<PfdBonusDayReport, Integer>{
	
	public List<Object[]> getListObjPFDDetailByReportDate(Date reportDate);

	public float findPfdSaveMonthBonus(Date reportDate);
	
	public float findPfdPaidMonthBonus(Date reportDate);
	
	public Integer deletePfdBonusDayReport(Date deleteDate);
	
    public float getPfdSavePrice(String pfdId, String startDate, String endDate);
	
	public float getPfdPaidPrice(String pfdId, String startDate, String endDate);
}
