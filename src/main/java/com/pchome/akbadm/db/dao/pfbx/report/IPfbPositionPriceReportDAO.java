package com.pchome.akbadm.db.dao.pfbx.report;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbPositionPriceReportVO;

public interface IPfbPositionPriceReportDAO extends IBaseDAO<PfbPositionPriceReportVO, String> {

	/**
	 * 取得版位定價查詢資料
	 * @param customerInfoId PFB帳號
	 * @param pid            PFB 版位編號
	 * @param pprice         版位價格
	 * @return 
	 * @throws Exception
	 */
	public List<PfbPositionPriceReportVO> getPositionPriceDataList(String customerInfoId, String pid, int pprice) throws Exception;

	/**
	 * 更新版位定價資料
	 * @param pid        版位編號
	 * @param pprice     出價
	 * @throws Exception
	 */
	public void updatePositionPrice(String pid, int pprice) throws Exception;
}
