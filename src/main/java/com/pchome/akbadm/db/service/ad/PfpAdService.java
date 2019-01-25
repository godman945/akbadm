package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.ad.IPfpAdDAO;
import com.pchome.akbadm.db.dao.ad.PfpAdGroupDAO;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.AdQueryConditionVO;
import com.pchome.akbadm.db.vo.ad.PfpAdAdViewVO;
import com.pchome.akbadm.db.vo.ad.PfpAdGroupViewVO;
import com.pchome.enumerate.ad.EnumAdType;
import com.pchome.enumerate.ad.EnumAdStatus;

public class PfpAdService extends BaseService<PfpAd, String> implements IPfpAdService {

	public PfpAd getPfpAdBySeq(String adSeq) throws Exception {
		return ((IPfpAdDAO) dao).getPfpAdBySeq(adSeq);
	}

	public int getPfpAdCountByConditions(AdQueryConditionVO vo) throws Exception {
		return ((IPfpAdDAO) dao).getPfpAdCountByConditions(vo);
	}

	public List<PfpAd> getPfpAdByConditions(AdQueryConditionVO vo, int pageNo, int pageSize) throws Exception {
		return ((IPfpAdDAO) dao).getPfpAdByConditions(vo, pageNo, pageSize);
	}

	/**
	 * 批次更新廣告審核後的狀態
	 */
	public void updateAdCheckStatus(String status, String[] seq, String verifyUserId) throws Exception {
		((IPfpAdDAO) dao).updateAdCheckStatus(status, seq, verifyUserId);
	}

	/**
	 * 更新廣告人工審核後的狀態
	 */
	public void updateAdCheckStatus(String adStatus, String adSeq, String adCategorySeq, String verifyUserId,
			String rejectReason) throws Exception {
		((IPfpAdDAO) dao).updateAdCheckStatus(adStatus, adSeq, adCategorySeq, verifyUserId, rejectReason);
	}
	
	/**
	 * 新增人工審核後廣告對應表
	 */
	public void insertAdCategoryMapping(PfpAdCategoryMapping pfpAdCategoryMapping)throws Exception {
		((IPfpAdDAO) dao).insertAdCategoryMapping(pfpAdCategoryMapping);
	}
	
	/**
	 * 取得廣告審核過類別Code
	 * */
	public String getCategoryMappingCodeById(String pfpAdSeq)throws Exception{
	    return ((IPfpAdDAO) dao).getCategoryMappingCodeById(pfpAdSeq);
	}

	public List<Object> getValidAd(String customerInfoId, Date today) throws Exception{
		return ((IPfpAdDAO) dao).getValidAd(customerInfoId, today);
	}

	
	public List<PfpAdAdViewVO> getAdViewReport(Map<String,String> adViewConditionMap) throws Exception{
	    long startTime = System.currentTimeMillis();
	    List<Object> adViewReportObjList = ((IPfpAdDAO) dao).getAdViewReport(adViewConditionMap);
	    long endTime = System.currentTimeMillis();
	    log.info("花費>>>>"+(endTime - startTime) / 1000+"秒");
	    Double adInvalidClkPrice;
	    Double adPvPrice;
	    float clkRate = 0;
	    float adClkPriceAvg = 0;
	    List<PfpAdAdViewVO> adGroupViewList = new ArrayList<PfpAdAdViewVO>();
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    DateFormat chtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date adCreateDate = null;
	    for (int i = 0; i < adViewReportObjList.size(); i++) {
		Object[] obj = (Object[]) adViewReportObjList.get(i);
		PfpAdAdViewVO pfpAdAdViewVO = new PfpAdAdViewVO();
		//廣告建立日期
		obj[0] = obj[0].toString().replace(".0", "");
		adCreateDate = dateFormat.parse(obj[0].toString());
		pfpAdAdViewVO.setAdCreateTime(chtDateFormat.format(adCreateDate).toString());
		pfpAdAdViewVO.setMemberId(obj[1].toString());
		pfpAdAdViewVO.setCustomerInfoTitle(obj[2].toString());
		// 廣告狀態
		for(EnumAdStatus adActionStatus:EnumAdStatus.values()){
		    if(adActionStatus.getStatusId()== Integer.valueOf(obj[4].toString())){
			pfpAdAdViewVO.setAdStatusDesc(adActionStatus.getStatusDesc());
		    }
		}
		if(!StringUtils.isBlank(adViewConditionMap.get("adPvclkDevice")) && adViewConditionMap.get("adPvclkDevice").equals("ALL Device")){
		    pfpAdAdViewVO.setAdPvclkDevice("All Device");
		}else{
		    pfpAdAdViewVO.setAdPvclkDevice(obj[5].toString());
		}
		
		pfpAdAdViewVO.setAdPv(Integer.valueOf(obj[6].toString()));
		pfpAdAdViewVO.setAdClk(Integer.valueOf(obj[7].toString()));
		//計算廣告點擊率
		if(!StringUtils.isEmpty(obj[7].toString()) && Integer.valueOf(obj[7].toString()) > 0){
		    clkRate = (float)Integer.valueOf(obj[7].toString()) / (float) Integer.valueOf(obj[6].toString()) * 100;
		    pfpAdAdViewVO.setAdClkRate(clkRate);
		}
		//費用
		pfpAdAdViewVO.setAdClkPrice(Float.parseFloat(obj[8].toString()));
		//計算平均點選費用
		adPvPrice = Double.parseDouble(obj[8].toString());
		if(adPvPrice > 0){
		    adClkPriceAvg =(float) (Double.parseDouble(obj[8].toString()) / Integer.valueOf(obj[7].toString()));
		    pfpAdAdViewVO.setAdClkPriceAvg(adClkPriceAvg);
		}
		//無效點擊數
		pfpAdAdViewVO.setAdInvalidClk(Integer.valueOf(obj[9].toString()));
		//無效點擊費用
		adInvalidClkPrice = Double.parseDouble(obj[10].toString());
		pfpAdAdViewVO.setAdInvalidClkPrice(adInvalidClkPrice.intValue());
		//分類名稱
		pfpAdAdViewVO.setAdGroupName(obj[11].toString());
		for(EnumAdStatus status:EnumAdStatus.values()){
		    int adgGroupStatus = Integer.parseInt(obj[12].toString());
		    if(status.getStatusId() == adgGroupStatus){
			pfpAdAdViewVO.setAdGroupStatus(adgGroupStatus);
			pfpAdAdViewVO.setAdGroupStatusDesc(status.getStatusDesc());
		    }
		}
		pfpAdAdViewVO.setAdActionName(obj[3].toString());
		pfpAdAdViewVO.setAdSeq(obj[13].toString());
		pfpAdAdViewVO.setAdGroupSeq(obj[15].toString());
		pfpAdAdViewVO.setAdTemplateNo(obj[14].toString());
		pfpAdAdViewVO.setAdRejectReason(obj[18].toString());
		pfpAdAdViewVO.setAdStyle(obj[19].toString());
		
		String html5Flag = "N";
		if(obj[20]!= null && StringUtils.equals("c_x05_po_tad_0059", obj[20].toString())){
			html5Flag = "Y";
		}
		pfpAdAdViewVO.setHtml5Tag(html5Flag);
		
		adGroupViewList.add(pfpAdAdViewVO);
	    }
	    return adGroupViewList;
	}

	public int getAdViewReportSize(Map<String,String> adViewConditionMap) throws Exception{
	    return  ((IPfpAdDAO) dao).getAdViewReportSize(adViewConditionMap);
	}
	
	public List<PfpAdAdViewVO> getAdAdView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception{

		List<PfpAdAdViewVO> adAdViewVOs = null;
		List<Object> objects = ((IPfpAdDAO)dao).getAdAdPvclk(userAccount, searchAdStatus, adType, keyword, adGroupSeq, adSeq, adPvclkDevice, dateType, startDate, endDate, page, pageSize);

		for(Object object:objects){
			Object[] ob = (Object[])object;
			if(ob[0] != null){

				if(adAdViewVOs == null){
					adAdViewVOs = new ArrayList<PfpAdAdViewVO>();
				}

				PfpAdAdViewVO adAdViewVO = new PfpAdAdViewVO();
				adAdViewVO.setAdActionSeq(ob[0].toString());
				adAdViewVO.setAdActionName(ob[1].toString());
				// 廣告類型
				for(EnumAdType type:EnumAdType.values()){
					int ad_type = Integer.parseInt(ob[2].toString());
					if(type.getType() == ad_type){
						adAdViewVO.setAdType(type.getChName());
					}
				}

				adAdViewVO.setAdGroupSeq(ob[3].toString());
				adAdViewVO.setAdGroupName(ob[4].toString());
				// 關鍵字狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int adGroupStatus = Integer.parseInt(ob[5].toString());

					if(status.getStatusId() == adGroupStatus){
						adAdViewVO.setAdGroupStatus(adGroupStatus);
						adAdViewVO.setAdGroupStatusDesc(status.getStatusRemark());
					}
				}

				adAdViewVO.setAdSeq(ob[6].toString());
				adAdViewVO.setAdTemplateNo(ob[7].toString());
				for(EnumAdStatus status:EnumAdStatus.values()){
					int adStatus = Integer.parseInt(ob[8].toString());

					if(status.getStatusId() == adStatus){
						adAdViewVO.setAdStatus(adStatus);
						adAdViewVO.setAdStatusDesc(status.getStatusRemark());
					}
				}
				
				if(ob[9] != null) {
					adAdViewVO.setAdPvclkDevice(ob[9].toString());
				}

				adAdViewVO.setAdRejectReason(ob[10].toString());

				int pv = Integer.parseInt(ob[11].toString());
				int clk = Integer.parseInt(ob[12].toString());
				float clkPrice = Float.parseFloat(ob[13].toString());
				adAdViewVO.setAdPv(pv);
				adAdViewVO.setAdClk(clk);
				adAdViewVO.setAdClkPrice(clkPrice);

				// 求點閱率
				float clkRate = 0;
				float clkPriceAvg = 0;

				if(clk > 0 || pv > 0){
					clkRate = (float)clk / (float)pv*100;

				}

				if(clkPrice > 0 || clk > 0){
					clkPriceAvg = clkPrice / clk;
				}

				adAdViewVO.setAdClkRate(clkRate);
				adAdViewVO.setAdClkPriceAvg(clkPriceAvg);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				adAdViewVO.setAdCreateTime(sdf.format(ob[14]));
				adAdViewVO.setMemberId(ob[15].toString());
				adAdViewVO.setCustomerInfoTitle(ob[16].toString());

				// 無效點擊
				int invalidClk = Integer.parseInt(ob[17].toString());
				float invalidClkPrice = Float.parseFloat(ob[18].toString());
				adAdViewVO.setAdInvalidClk(invalidClk);
				adAdViewVO.setAdInvalidClkPrice(invalidClkPrice);

				adAdViewVOs.add(adAdViewVO);
			}
		}

		return adAdViewVOs;
	}

	public List<Object> getAllAdAdView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception{
		return ((IPfpAdDAO)dao).getAdAdPvclk(userAccount, searchAdStatus, adType, keyword, adGroupSeq, adSeq, adPvclkDevice, dateType, startDate, endDate, -1, -1);
	}

	public List<PfpAd> validAdAd(String adGroupSeq) throws Exception{
		return ((IPfpAdDAO)dao).validAdAd(adGroupSeq);
	}

	public void saveOrUpdateWithCommit(PfpAd adAd) throws Exception{
		((IPfpAdDAO)dao).saveOrUpdateWithCommit(adAd);
	}

	public int selectAdNewByUserVerifyDate(String userVerifyDate) {
	    return ((IPfpAdDAO)dao).selectAdNewByUserVerifyDate(userVerifyDate);
	}

	public int selectAdNumByActionDate(String actionDate) {
	    return ((IPfpAdDAO)dao).selectAdNumByActionDate(actionDate);
	}

	public int selectAdReadyByActionDate(String actionDate) {
	    return ((IPfpAdDAO)dao).selectAdReadyByActionDate(actionDate);
	}

	public int selectAdDueByActionDate(String startDate, String endDate) {
	    return ((IPfpAdDAO)dao).selectAdDueByActionDate(startDate, endDate);
	}
}
