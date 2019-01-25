package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusBillReport;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;

public interface IAdmBonusBillReportDAO extends IBaseDAO<AdmBonusBillReport, Integer>
{
	public List<PfbxInComeReportVo> getListBySEDate2(Date sdate, Date edate);
	
	public List<AdmBonusBillReport> getListBySEDate(Date sdate, Date edate);

	public List<Map> getMapsBySEDate(String sdate, String edate);

	public Integer deleteAdmBonusBillReport(Date deleteDate);
}
