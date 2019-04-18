package com.pchome.akbadm.db.service.report;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpCodeConvert;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;

public interface IPfpCodeConvertService extends IBaseService<PfpCodeConvert, String> {

	public List<PfpConvertTrackingVO> findPfpCodeConvertList(PfpConvertTrackingVO convertTrackingVO) throws Exception;

	public PfpConvertTrackingVO getConvertTrackingList(PfpConvertTrackingVO convertTrackingVO) throws Exception;
	
	public String getConvertTrackingCount(PfpConvertTrackingVO convertTrackingVO) throws Exception;
}