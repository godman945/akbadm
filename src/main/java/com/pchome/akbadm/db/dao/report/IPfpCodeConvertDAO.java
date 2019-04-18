package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCodeConvert;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;

public interface IPfpCodeConvertDAO extends IBaseDAO<PfpCodeConvert, String> {

	public List<Map<String,Object>> findPfpCodeConvertList(PfpConvertTrackingVO convertTrackingVO) throws Exception;
	
	public List<Map<String,Object>> getConvertTrackingList(PfpConvertTrackingVO convertTrackingVO) throws Exception;
	
	public String getConvertTrackingCount(PfpConvertTrackingVO convertTrackingVO) throws Exception;
	
}