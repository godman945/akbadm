package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusDetailReport;

public interface IAdmBonusDetailReportDAO extends IBaseDAO<AdmBonusDetailReport, Integer>{

	public List<AdmBonusDetailReport> findAdmBonusDetailReport(Date reportDate);
	
	public Integer deleteAdmBonusDetailReport(Date deleteDate);
}
