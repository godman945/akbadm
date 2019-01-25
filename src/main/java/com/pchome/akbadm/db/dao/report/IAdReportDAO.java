package com.pchome.akbadm.db.dao.report;

import java.util.HashMap;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;

public interface IAdReportDAO extends IBaseDAO<PfpAdPvclk, Integer> {
	
//	/**
//	 * (舊)讀取 AdReport (廣告明細資料)
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adKeywordType 廣告形式
//	 * @param sortMode 排序方式
//	 * @param displayCount 顯示比數
//	 * @return List<AdReportVO> 廣告成效列表
//	 */
//	public List<AdReportVO> getAdReportList(String startDate, String endDate, String adKeywordType, String sortMode, int displayCount) throws Exception;

	/**
	 * (新)讀取 AdReport (廣告明細資料)，條件包含 pfdCustomerInfoId (經銷商)
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adKeywordType 廣告形式
	 * @param sortMode 排序方式
	 * @param displayCount 顯示比數
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdReportVO> 廣告成效列表
	 */
	public List<AdReportVO> getAdReportList(String startDate, String endDate,
			String adKeywordType, String sortMode, int displayCount,
			String pfdCustomerInfoId, String templateProductSeq) throws Exception;
	
//	/**
//	 * (舊)讀取 AdActionReport(總廣告成效) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告刑式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdActionReportVO> getAdActionReportList(String startDate, String endDate, String adStyle, String customerInfoId, String adType) throws Exception;

	/**
	 * (新)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告刑式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdActionReportVO> getAdActionReportList(String startDate, String endDate,
			String pfpCustomerInfoId, String adType, String pfdCustomerInfoId,
			String payType) throws Exception;

	/**
	 * 強哥改的，完全看不懂在改什麼，動機為何
	 * (2014-04-24)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告刑式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
//	public List<AdActionReportVO> getAdActionReportList(String startDate, String endDate, List<String> adSeqs, String pfpCustomerInfoId, String adType, String pfdCustomerInfoId) throws Exception;

//	/**
//	 * (舊)讀取 AdActionReportDetail(總廣告成效明細表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, String adStyle, String customerInfoId, String adType) throws Exception;

//	/**
//	 * (新)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, String adStyle, String customerInfoId, String adType, String pfdCustomerInfoId) throws Exception;

	/**
	 * 強哥改的，完全看不懂在改什麼，動機為何
	 * (2014)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告型式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
//	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, List<String> adStyle, String customerInfoId, String adType, String pfdCustomerInfoId) throws Exception;

	/**
	 * (新)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告型式 (找東西廣告 or PChome頻道廣告)
	 * @param pfdCustomerInfoId 經銷商編號
	 * @param payType 付款方式
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate,
			String pfpCustomerInfoId, String adType,
			String pfdCustomerInfoId, String payType) throws Exception;

//	/**
//	 * (舊)讀取 AdTemplateReport(廣告樣版成效表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdTemplateReportVO> getAdTemplateReport(String startDate, String endDate) throws Exception;

	/**
	 * (新)讀取 AdTemplateReport(廣告樣版成效表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdTemplateReportVO> getAdTemplateReport(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception;

//	/**
//	 * (舊)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdActionReportVO> getOfflineAdActionReportList(String startDate, String endDate) throws Exception;

//	/**
//	 * (新)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	public List<AdActionReportVO> getOfflineAdActionReportList(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception;

	/**
	 * (2014-04-24)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdActionReportVO> getOfflineAdActionReportList(HashMap<String, PfpAdAction> adActionSeqMap, String pfdCustomerInfoId) throws Exception;

//	/**
//	 * (舊)花費成效排名查詢DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 花費成效排名列表
//	 */
//	public List<AdActionReportVO> getAdSpendReport(String startDate, String endDate, int displayCount) throws Exception;

	/**
	 * (新)花費成效排名查詢DAO，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 花費成效排名列表
	 */
	public List<AdActionReportVO> getAdSpendReport(String startDate, String endDate, String pfdCustomerInfoId) throws Exception;
	
	public List<AdActionReportVO> getAdNotArrivalReport(String startDate, String endDate, int displayCount) throws Exception;

//	/**
//	 * (舊)未達每日花費上限報表DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 未達每日花費上限列表
//	 */
//	public List<AdActionReportVO> getUnReachBudgetReport(String startDate, String endDate, int displayCount) throws Exception;

	/**
	 * (2014-04-25)未達每日花費上限報表DAO，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 未達每日花費上限列表
	 */
	public List<AdActionReportVO> getUnReachBudgetReport(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception;

	/**
	 * (2014-04-28)行動裝置成效DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	public List<AdActionReportVO> getAdMobileOSReport(String startDate, String endDate, String adMobileOS, String customerInfoId) throws Exception;

	/**
	 * (2014-04-28)行動裝置成效明細DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	public List<AdActionReportVO> getAdMobileOSReportDetail(String startDate, String endDate, String adMobileOS, String customerInfoId) throws Exception;
}
