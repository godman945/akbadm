package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.ad.IPfpAdActionDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdPvclkDAO;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdActionViewVO;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.ad.EnumAdType;
import com.pchome.soft.util.DateValueUtil;

public class PfpAdActionService extends BaseService<PfpAdAction, String> implements IPfpAdActionService {
    private IPfpAdPvclkDAO pfpAdPvclkDAO;

    public List<PfpAdAction> getValidAdAction(String customerInfoId) {
    	return ((IPfpAdActionDAO)dao).getValidAdAction(customerInfoId);
    }

    public void updatePfpAdAction(PfpAdAction pfpAdAction){
    	((IPfpAdActionDAO)dao).saveOrUpdate(pfpAdAction);
    }
    
    //改善廣告報表效能
    public List<PfpAdActionViewVO> getAdActionReport(Map<String,String> adActionConditionMap) throws Exception {
	List<Object> adActionReportObjList = ((IPfpAdActionDAO)dao).getAdActionReport(adActionConditionMap);
    	Double adInvalidClkPrice;
    	Double adPvPrice; 
    	float clkRate = 0;
    	float adClkPriceAvg =0;
    	List<PfpAdActionViewVO> adActionViewList = new ArrayList<PfpAdActionViewVO>();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	DateFormat chtDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date adCreateDate=null;
    	for (int i=0; i<adActionReportObjList.size(); i++) {
    		Object[] obj = (Object[]) adActionReportObjList.get(i);
    		PfpAdActionViewVO pfpAdActionViewVO = new PfpAdActionViewVO();
    		if(!StringUtils.isEmpty(adActionConditionMap.get("userAccount")) && !adActionConditionMap.get("userAccount").equals(obj[1].toString())){
    		continue;
	     }
    	    //廣告活動建立時間
	    obj[0] = obj[0].toString().replace(".0", "");
	    adCreateDate = dateFormat.parse(obj[0].toString());
	    pfpAdActionViewVO.setAdActionCreatTime(chtDateFormat.format(adCreateDate).toString());
	    //負責人帳號
	    pfpAdActionViewVO.setMemberId(obj[1].toString());
	    //帳戶名稱
	    pfpAdActionViewVO.setCustomerInfoTitle(obj[2].toString());
	    //帳戶使用狀態
	    for(EnumAccountStatus accountStatus:EnumAccountStatus.values()){
		if(accountStatus.getStatus().equals(obj[3].toString())){
		    pfpAdActionViewVO.setCustomerInfoStatusDesc(accountStatus.getDescription());
		}
	    }
	    //裝置
	    if(adActionConditionMap.get("adPvclkDevice")!= null && adActionConditionMap.get("adPvclkDevice").trim().length()>0){
		pfpAdActionViewVO.setAdPvclkDevice(obj[4].toString());
	    }else{
		pfpAdActionViewVO.setAdPvclkDevice("ALL Device");
	    }
	    //帳戶餘額
	    pfpAdActionViewVO.setRemain(Float.parseFloat(obj[5].toString()));
	    //廣告名稱
	    pfpAdActionViewVO.setAdActionName(obj[6].toString());
	    //廣告狀態
	    for(EnumAdStatus adActionStatus:EnumAdStatus.values()){
		if(adActionStatus.getStatusId()== Integer.valueOf(obj[7].toString())){
		    pfpAdActionViewVO.setAdActionStatusDesc(adActionStatus.getStatusDesc());
		}
	    }
	    //廣告開始日期
	    pfpAdActionViewVO.setAdStartDate(obj[8].toString());
	    //廣告結束日期
	    pfpAdActionViewVO.setAdEndDate(obj[9].toString());
	    //每日預算上限
	    pfpAdActionViewVO.setAdActionMax(Float.parseFloat(obj[10].toString()));
	    //調控金額
	    pfpAdActionViewVO.setAdActionControlPrice(Float.parseFloat(obj[11].toString()));
	    //廣告曝光數
	    pfpAdActionViewVO.setAdPv(Integer.valueOf(obj[12].toString()));
	    //廣告點擊數
	    pfpAdActionViewVO.setAdClk(Integer.valueOf(obj[13].toString()));
	    //無效點擊數
	    pfpAdActionViewVO.setAdInvalidClk(Integer.valueOf(obj[14].toString()));
	    //無效點擊數總費用
	    adInvalidClkPrice = Double.parseDouble(obj[15].toString());
	    pfpAdActionViewVO.setAdInvalidClkPrice(adInvalidClkPrice.intValue());
	    //廣告曝光數總費用
	    pfpAdActionViewVO.setAdPvPrice(Float.parseFloat(obj[17].toString()));
	    //計算廣告點擊率
	    if(!StringUtils.isEmpty(obj[12].toString()) && Integer.valueOf(obj[12].toString()) > 0){
	    	clkRate = (float)Integer.valueOf(obj[13].toString()) / (float) Integer.valueOf(obj[12].toString()) * 100;
	    	pfpAdActionViewVO.setAdClkRate(clkRate);
	      }
	    adPvPrice = Double.parseDouble(obj[17].toString());
	    //計算平均點選費用
	    if(adPvPrice > 0){
	    	adClkPriceAvg =(float) (Double.parseDouble(obj[17].toString()) / Integer.valueOf(obj[13].toString()));
	    	pfpAdActionViewVO.setAdClkPriceAvg(adClkPriceAvg);
	      }
	    //廣告活動序號
	    pfpAdActionViewVO.setAdActionSeq(obj[19].toString());
	      adActionViewList.add(pfpAdActionViewVO);
	 }
	return adActionViewList;
    }

    public int getAdActionReportSize(Map<String,String> adActionConditionMap) throws Exception{
    	return  ((IPfpAdActionDAO)dao).getAdActionReportSize(adActionConditionMap);
    }
    
    
    
    public PfpAdAction getAdAction(String adActionSeq){
    	return ((IPfpAdActionDAO)dao).getAdAction(adActionSeq);
    }

	public List<PfpAdActionViewVO> getAdActionView(String userAccount, String adStatus, int adType, String adActionName, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception{

		List<PfpAdActionViewVO> adActionViewVOs = null;
		List<Object> objects = ((IPfpAdActionDAO)dao).getAdActionPvclk(userAccount, adStatus, adType, adActionName, adPvclkDevice, dateType, startDate, endDate, page, pageSize);

		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[0] != null){

				if(adActionViewVOs == null){
					adActionViewVOs = new ArrayList<PfpAdActionViewVO>();
				}

				PfpAdActionViewVO adActionViewVO = new PfpAdActionViewVO();
				adActionViewVO.setAdActionSeq(ob[0].toString());
				adActionViewVO.setAdActionName(ob[1].toString());
				// 廣告類型
				for(EnumAdType type:EnumAdType.values()){
					int ad_type = Integer.parseInt(ob[2].toString());
					if(type.getType() == ad_type){
						adActionViewVO.setAdType(type.getChName());
					}
				}
				// 廣告狀態
				for(EnumAdStatus status:EnumAdStatus.values()){
					int ad_action_status = Integer.parseInt(ob[3].toString());

					if(status.getStatusId() == ad_action_status){
						//System.out.println("ob[1].toString() = " + ob[1].toString());
						//System.out.println("status.getStatusId() = " + status.getStatusId());
						System.out.println("ad_action_status = " + ad_action_status);
						System.out.println("status.getStatusDesc() = " + status.getStatusDesc());
						adActionViewVO.setAdActionStatus(ad_action_status);
						adActionViewVO.setAdActionStatusDesc(status.getStatusDesc());
					}
				}

				adActionViewVO.setAdActionMax(Float.parseFloat(ob[4].toString()));
				//System.out.println("AdActionControlPrice = " + Float.parseFloat(ob[5].toString()));
				adActionViewVO.setAdActionControlPrice(Float.parseFloat(ob[5].toString()));

				// 判斷廣告走期
				adActionViewVO.setAdStartDate(ob[6].toString());
				adActionViewVO.setAdEndDate(ob[7].toString());

				Date adStartDate = DateValueUtil.getInstance().stringToDate(adActionViewVO.getAdStartDate());
				Date adEndDate = DateValueUtil.getInstance().stringToDate(adActionViewVO.getAdEndDate());

				if(adActionViewVO.getAdActionStatus() == EnumAdStatus.Open.getStatusId()){

					Date today = DateValueUtil.getInstance().getNowDateTime();
					Date yesterday = DateValueUtil.getInstance().getDateForStartDateAddDay(DateValueUtil.getInstance().dateToString(today), -1);

					if(today.after(adStartDate) && yesterday.before(adEndDate)){ // 因為結束時間為  00:00:00 所以取昨天比較
						adActionViewVO.setAdActionStatusDesc(EnumAdStatus.Broadcast.getStatusDesc());
					}
					else if(today.before(adStartDate)){
						adActionViewVO.setAdActionStatusDesc(EnumAdStatus.Waitbroadcast.getStatusDesc());
					}
					else if(today.after(adEndDate)){
						adActionViewVO.setAdActionStatusDesc(EnumAdStatus.End.getStatusDesc());
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				adActionViewVO.setAdActionCreatTime(sdf.format(ob[8]));
				adActionViewVO.setMemberId(ob[9].toString());
				adActionViewVO.setCustomerInfoTitle(ob[10].toString());
				//帳戶餘額
				adActionViewVO.setRemain(Float.parseFloat(ob[11].toString()));
				// 帳號狀態
				//log.info("CustomerInfoStatus = " + ob[12].toString());
				for(EnumAccountStatus accountStatus:EnumAccountStatus.values()){
					if(accountStatus.getStatus().equals(ob[12].toString())){
						adActionViewVO.setCustomerInfoStatus(ob[12].toString());
						adActionViewVO.setCustomerInfoStatusDesc(accountStatus.getDescription());
					}
				}
//				if(ob[13] != null) {
//					adActionViewVO.setAdPvclkDevice(ob[13].toString());
//				}
//				adActionViewVO.setAdGroupSeq(ob[14].toString());

				// 求點閱率
//				int pv = Integer.parseInt(ob[15].toString());
//				int clk = Integer.parseInt(ob[16].toString());
//				float clkPrice = Float.parseFloat(ob[17].toString());
//				adActionViewVO.setAdPv(pv);
//				adActionViewVO.setAdClk(clk);
//				adActionViewVO.setAdClkPrice(clkPrice);

				float clkRate = 0;
				float clkPriceAvg = 0;

//				if(clk > 0 || pv > 0){
//					clkRate = (float)clk / (float)pv*100;
//
//				}
//
//				if(clkPrice > 0 || clk > 0){
//					clkPriceAvg = clkPrice / clk;
//				}

				adActionViewVO.setAdClkRate(clkRate);
				adActionViewVO.setAdClkPriceAvg(clkPriceAvg);

				// 無效點擊
//				int invalidClk = Integer.parseInt(ob[18].toString());
//				float invalidClkPrice = Float.parseFloat(ob[19].toString());
//				adActionViewVO.setAdInvalidClk(invalidClk);
//				adActionViewVO.setAdInvalidClkPrice(invalidClkPrice);

				adActionViewVOs.add(adActionViewVO);
			}
		}

		return adActionViewVOs;
	}

	public List<Object> getAllAdActionView(String userAccount, String adStatus, int adType, String adActionName, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception{
		return ((IPfpAdActionDAO)dao).getAdActionPvclk(userAccount, adStatus, adType, adActionName, adPvclkDevice, dateType, startDate, endDate, -1, -1);
	}

    public List<PfpAd> selectValidAd() {
        List<PfpAd> list = new ArrayList<PfpAd>();

        int price = 0;
        List<PfpAdAction> actionList = ((IPfpAdActionDAO) dao).selectValidAdAction(Calendar.getInstance().getTime());

//        log.info("action " + actionList.size());

        for (PfpAdAction action: actionList) {
            price = 0;

//            log.info("group " + action.getPfpAdGroups().size());

            // count price
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

//                log.info("ad " + group.getPfpAds().size());

                for (PfpAd ad: group.getPfpAds()) {
                    if (ad.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

//                    log.info("pvclk " + ad.getPfpAdPvclks().size());

                    for (PfpAdPvclk pvclk: ad.getPfpAdPvclks()) {
                        price += pvclk.getAdPvPrice() + pvclk.getAdClkPrice();
                    }
                }
            }

            // determine price
            if (price >= action.getAdActionControlPrice()) {
                continue;
            }

            // add ad
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

                for (PfpAd ad: group.getPfpAds()) {
                    if (ad.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

                    list.add(ad);
                }
            }
        }

        return list;
    }

    public List<PfpAdDetail> selectValidAdDetail() {
        List<PfpAdDetail> list = new ArrayList<PfpAdDetail>();

        int price = 0;
        List<PfpAdAction> actionList = ((IPfpAdActionDAO) dao).selectValidAdAction(Calendar.getInstance().getTime());

//        log.info("action " + actionList.size());

        for (PfpAdAction action: actionList) {
            price = 0;

//            log.info("group " + action.getPfpAdGroups().size());

            // count price
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

//                log.info("ad " + group.getPfpAds().size());

                for (PfpAd ad: group.getPfpAds()) {
                    if (ad.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

//                    log.info("pvclk " + ad.getPfpAdPvclks().size());

                    for (PfpAdPvclk pvclk: ad.getPfpAdPvclks()) {
                        price += pvclk.getAdPvPrice() + pvclk.getAdClkPrice();
                    }
                }
            }

            // determine price
            if (price >= action.getAdActionControlPrice()) {
                continue;
            }

            // add adDetail
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

                for (PfpAd ad: group.getPfpAds()) {
                    if (ad.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

                    list.addAll(ad.getPfpAdDetails());
                }
            }
        }

        return list;
    }

    public List<PfpAdKeyword> selectValidAdKeyword() {
        List<PfpAdKeyword> list = new ArrayList<PfpAdKeyword>();

        int price = 0;
        List<PfpAdAction> actionList = ((IPfpAdActionDAO) dao).selectValidAdAction(Calendar.getInstance().getTime());
        for (PfpAdAction action: actionList) {
            price = 0;

            // count price
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

                for (PfpAdKeyword keyword: group.getPfpAdKeywords()) {
                    if (keyword.getAdKeywordStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

                    for (PfpAdKeywordPvclk kpvclk: keyword.getPfpAdKeywordPvclks()) {
                        price += kpvclk.getAdKeywordPvPrice() + kpvclk.getAdKeywordClkPrice();
                    }
                }
            }

            // determine price
            if (price >= action.getAdActionControlPrice()) {
                continue;
            }

            // add keyword
            for (PfpAdGroup group: action.getPfpAdGroups()) {
                if (group.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                    continue;
                }

                for (PfpAdKeyword keyword: group.getPfpAdKeywords()) {
                    if (keyword.getAdKeywordStatus() != EnumAdStatus.Open.getStatusId()) {
                        continue;
                    }

                    list.add(keyword);
                }
            }
        }

        return list;
    }

    public int selectAdUnderMaxByPvclkDate(String pvclkDate) {
        int adNum = 0;
        Float[] pvclks = null;
        List<PfpAdAction> pfpAdActionList = ((IPfpAdActionDAO) dao).selectAdActionByStatus(EnumAdStatus.Open.getStatusId());

        for (PfpAdAction pfpAdAction: pfpAdActionList) {
            pvclks = pfpAdPvclkDAO.selectAdPvclkSumByPvclkDate(pvclkDate, null, pfpAdAction.getAdActionSeq(), null, null);

            // (adClkPrice - adInvalidClkPrice) >= actionMax
            if ((pvclks[2] - pvclks[4]) >= pfpAdAction.getAdActionMax()) {
                continue;
            }

//            for (PfpAdGroup pfpAdGroup: pfpAdAction.getPfpAdGroups()) {
//                adNum += pfpAdGroup.getPfpAds().size();
//            }

            adNum++;
        }

        return adNum;
    }

    public List<PfpAdAction> selectAdActionByStatus(int status) {
        return ((IPfpAdActionDAO)dao).selectAdActionByStatus(status);
    }

    public float selectActionMaxSum() {
        return ((IPfpAdActionDAO)dao).selectActionMaxSum();
    }

    public float selectReachRate(String pvclkDate) {
        Float[] pvclks = pfpAdPvclkDAO.selectAdPvclkSumByPvclkDate(pvclkDate, null, null, null, null);
        float actionMax = ((IPfpAdActionDAO) dao).selectActionMaxSum();

        if (actionMax <= 0) {
            return 0f;
        }

        return (pvclks[2] - pvclks[4]) / actionMax;


    }

    public void setPfpAdPvclkDAO(IPfpAdPvclkDAO pfpAdPvclkDAO) {
        this.pfpAdPvclkDAO = pfpAdPvclkDAO;
    }

    public List<PfpAdAction> getAdActionByConditions(Map<String, String> conditionMap) throws Exception {
    	return ((IPfpAdActionDAO) dao).getAdActionByConditions(conditionMap);
    }
    
    public List<PfpAdAction> findBroadcastAdAction(String customerInfoId) {
    	return ((IPfpAdActionDAO) dao).findBroadcastAdAction(customerInfoId);
    }
}
