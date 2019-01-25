package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public interface IAdmPfpAdVideoReportDAO {
	
	public List<Object> findVideoInfoByDate(final String date) throws Exception;
	
	public int deleteVideoReportDataBytDate(String date) throws Exception;
	
	public int addVideoReportDataBytDate(List<PfpAdVideoReportVO> list) throws Exception;
}
