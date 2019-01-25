package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.AdQueryConditionVO;
import com.pchome.akbadm.db.vo.ad.PfpAdAdViewVO;

public interface IPfpAdService extends IBaseService<PfpAd, String> {
	public PfpAd getPfpAdBySeq(String adSeq) throws Exception;

	public int getPfpAdCountByConditions(AdQueryConditionVO vo) throws Exception;

	public List<PfpAd> getPfpAdByConditions(AdQueryConditionVO vo, int pageNo, int pageSize) throws Exception;

	/**
	 * 批次更新廣告審核後的狀態
	 */
	public void updateAdCheckStatus(String status, String[] seq, String verifyUserId) throws Exception;

	/**
	 * 更新廣告人工審核後的狀態
	 */
	public void updateAdCheckStatus(String adStatus, String adSeq, String adCategorySeq, String verifyUserId,
			String rejectReason) throws Exception;

	/**
	 * 新增人工審核後廣告對應表
	 */
	public void insertAdCategoryMapping(PfpAdCategoryMapping pfpAdCategoryMapping)throws Exception;

	/**
	 * 取得廣告審核過類別Code
	 * */
	public String getCategoryMappingCodeById(String pfpAdSeq)throws Exception;

	public List<PfpAdAdViewVO> getAdAdView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception;

	public List<Object> getAllAdAdView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception;

	public List<PfpAd> validAdAd(String adGroupSeq) throws Exception;

	public List<Object> getValidAd(String customerInfoId, Date today) throws Exception;

	public void saveOrUpdateWithCommit(PfpAd adAd) throws Exception;

	public int selectAdNewByUserVerifyDate(String userVerifyDate);

	public int selectAdNumByActionDate(String actionDate);

	public int selectAdReadyByActionDate(String actionDate);

	public int selectAdDueByActionDate(String startDate, String endDate);

	public List<PfpAdAdViewVO> getAdViewReport(Map<String,String> adViewConditionMap) throws Exception;

	public int getAdViewReportSize(Map<String,String> adViewConditionMap) throws Exception;
}
