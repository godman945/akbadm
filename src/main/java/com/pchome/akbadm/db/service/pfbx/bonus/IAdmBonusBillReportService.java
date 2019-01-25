package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmBonusBillReport;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;

public interface IAdmBonusBillReportService extends IBaseService<AdmBonusBillReport, Integer>
{
	/**
	 * 查詢功能與每日排程共用
	 * @param sdate
	 * @param edate
	 * @return List<PfbxInComeReportVo>
	 * @throws Exception
	 */
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList5(String sdate, String edate) throws Exception;
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList4(String sdate, String edate) throws Exception;
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList3(String sdate, String edate) throws Exception;
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList2(String sdate, String edate) throws Exception;
	
	public List<PfbxInComeReportVo> getPfbxInComeReportVoList1(String sdate, String edate) throws Exception;
	
//	public List<PfbxInComeReportVo> getPfbxInComeReportVoList(String sdate , String edate)  throws Exception;

	public List<AdmBonusBillReport> getListBySEDate(Date sdate, Date edate) throws Exception;
	
	public AdmBonusBillReport getOneBySEDate(Date sdate, Date edate) throws Exception;
	
	public Integer deleteAdmBonusBillReport(Date deleteDate);
}
