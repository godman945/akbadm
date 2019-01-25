package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdAdVideoReport;

public interface IPfdAdVideoReportDAO {
	
	public List<Object> findVideoInfoByDate(final String date) throws Exception;
	
	public int deleteVideoReportDataBytDate(String date) throws Exception;
	
	public int addVideoReportDataBytDate(List<PfdAdVideoReport> list) throws Exception;
}
