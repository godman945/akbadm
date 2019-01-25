package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusDayReport;

public interface IPfbxBonusDayReportDAO extends IBaseDAO<PfbxBonusDayReport, Integer>{
	
	public List<Object> findPfbxBonusDayReport(Date startDate, Date endDate);
	
	public List<Object> findPfbxIdBonusDayReport(String pfbId,Date startDate, Date endDate);
	
	public Integer deletePfbxBonusDayReport(Date deleteDate);
}
