package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdAdVideoReport;

public interface IPfdAdVideoReportService {
	/**
	 * 取得當日資料
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<PfdAdVideoReport> findVideoInfoByDate(String date) throws Exception;

	/**
	 * 刪除當日pfd_ad_video_report資料
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public int deleteVideoReportDataBytDate(String date) throws Exception;

	/**
	 * 新增當日pfd_ad_video_report資料
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int addVideoReportDataBytDate(List<PfdAdVideoReport> list) throws Exception;
}
