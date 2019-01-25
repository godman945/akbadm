package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public interface IAdmPfpAdVideoReportService {
	//取得當日資料
	public List<PfpAdVideoReportVO> findVideoInfoByDate(String date) throws Exception;
	//刪除當日pfp_ad_video_report資料
	public int deleteVideoReportDataBytDate(String date) throws Exception;
	//刪除當日pfp_ad_video_report資料
	public int addVideoReportDataBytDate(List<PfpAdVideoReportVO> list) throws Exception;
}
