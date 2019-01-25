package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbPositionPriceReportDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbPositionPriceReportVO;

public class PfbPositionPriceReportService extends BaseService<PfbPositionPriceReportVO, String> implements IPfbPositionPriceReportService {
	
	/**
	 * 取得版位定價查詢資料
	 * @param customerInfoId PFB帳號
	 * @param pid            PFB 版位編號
	 * @param pprice         版位價格
	 * @return List<PfbPositionPriceReportVO> 廣告版位表資料
	 * @throws Exception
	 */
	@Override
	public List<PfbPositionPriceReportVO> getPositionPriceDataList(String customerInfoId, String pid, int pprice) throws Exception{
		return ((IPfbPositionPriceReportDAO) dao).getPositionPriceDataList(customerInfoId, pid, pprice);
	}

	/**
	 * 更新版位定價資料
	 * @param pid        版位編號
	 * @param pprice     出價
	 * @throws Exception
	 */
	@Override
	public void updatePositionPrice(String pid, int pprice) throws Exception {
		((IPfbPositionPriceReportDAO) dao).updatePositionPrice(pid, pprice);
	}
	
}
