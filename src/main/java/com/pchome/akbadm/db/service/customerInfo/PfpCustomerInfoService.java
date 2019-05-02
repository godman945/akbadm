package com.pchome.akbadm.db.service.customerInfo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.bean.ValidKeywordBean;
import com.pchome.akbadm.db.dao.ad.IPfpAdKeywordPvclkDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdPvclkDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfpCustomerInfoDAO;
//import com.pchome.akbadm.db.dao.recognize.AdmRecognizeRecordDAO;
import com.pchome.akbadm.db.dao.recognize.IAdmRecognizeRecordDAO;
import com.pchome.akbadm.db.dao.report.quartzs.IPfpAdActionReportDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
//import com.pchome.akbadm.db.pojo.PfpOrder;
//import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.CustomerInfoReportVO;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
//import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.recognize.EnumOrderType;
//import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.manager.PfpAccountVO;
import com.pchome.soft.util.DateValueUtil;

public class PfpCustomerInfoService extends BaseService<PfpCustomerInfo,String> implements IPfpCustomerInfoService{

    private IPfpAdPvclkDAO pfpAdPvclkDAO;
    private IPfpAdKeywordPvclkDAO pfpAdKeywordPvclkDAO;
    private IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO;
    private IAdmRecognizeRecordDAO admRecognizeRecordDAO;
    private IPfpAdActionReportDAO pfpAdActionReportDAO;
    private float remain;
    private float keywordSysprice;

	@Override
    public List<Object> findPfpCustomerInfo(String keyword, String customerInfoName, int page, int pageSize) throws Exception{
		return ((IPfpCustomerInfoDAO) dao).findPfpCustomerInfo(keyword, customerInfoName, page, pageSize);
	}

	@Override
    public PfpCustomerInfo getCustomerInfo(String customerInfoId){
		return ((IPfpCustomerInfoDAO) dao).getCustomerInfo(customerInfoId);
	}

	public List<CustomerInfoReportVO> findPfpCustomerInfosByDate(String startDate, String endDate,
			String pfdCustomerInfoId, String orderType, String payType) throws Exception{

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> orderType = " + orderType);
		log.info(">>> payType = " + payType);

		String pfpCustInfoId = "";
		if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId);

			for (int i=0; i<refList.size(); i++) {
				//過濾付款方式
				if (StringUtils.isNotBlank(payType)) {
					//非使用者要查詢的付款方式 -> 排除
					if (!refList.get(i).getPfpPayType().equals(payType)) {
						continue;
					}
				}
				pfpCustInfoId += "'" + refList.get(i).getPfpCustomerInfo().getCustomerInfoId() + "',";
			}
			if (!pfpCustInfoId.equals("")) {
				pfpCustInfoId = pfpCustInfoId.substring(0, pfpCustInfoId.length()-1);
			}
		}
		log.info(">>> pfpCustInfoId = " + pfpCustInfoId);

		String status = "'" + EnumAccountStatus.START.getStatus() + "','" +
				EnumAccountStatus.CLOSE.getStatus() + "','" +
				EnumAccountStatus.STOP.getStatus() + "'";

		//有選 PFD 帳戶卻找不到對應的 PFP 帳戶 -> 不查詢
		if (StringUtils.isNotEmpty(pfdCustomerInfoId) && StringUtils.isEmpty(pfpCustInfoId)) {
			return null;
		}

		List<CustomerInfoReportVO> customerInfoReports = null;
		List<PfpCustomerInfo> pfpCustomerInfoList = ((IPfpCustomerInfoDAO) dao).findPfpCustomerInfosByDate(startDate, endDate,
				pfpCustInfoId, status);

		if(pfpCustomerInfoList != null && pfpCustomerInfoList.size() > 0){

			customerInfoReports = new ArrayList<CustomerInfoReportVO>();

			Map<String, String> payTypeMap = new HashMap<String, String>();
			for (EnumPfdAccountPayType _payType : EnumPfdAccountPayType.values()) {
				payTypeMap.put(_payType.getPayType(), _payType.getPayName());
			}

			for(PfpCustomerInfo pfpCustInfo:pfpCustomerInfoList){

				//撈儲值類型資料
				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("pfpCustomerInfoId", pfpCustInfo.getCustomerInfoId());
				conditionMap.put("startDate", startDate);
				conditionMap.put("endDate", endDate);
				if(StringUtils.isNotBlank(orderType)) {
					conditionMap.put("orderType", orderType);
				}

				CustomerInfoReportVO vo = new CustomerInfoReportVO();

				vo.setActivateDate(DateValueUtil.getInstance().dateToString(pfpCustInfo.getActivateDate()));
				vo.setMemberId(pfpCustInfo.getMemberId());
				vo.setCustomerInfoId(pfpCustInfo.getCustomerInfoId());
				vo.setCustomerInfoName(pfpCustInfo.getCustomerInfoTitle());

				if(pfpCustInfo.getCategory().equals("1")){
					vo.setCustomerInfoType("個人戶");
				}else{
					vo.setCustomerInfoType("公司戶");
				}

				List<AdmRecognizeRecord> admRecognizeRecordList = admRecognizeRecordDAO.findRecognizeRecords(conditionMap);

				if (admRecognizeRecordList.size() > 0) {
					AdmRecognizeRecord admRecognizeRecord = admRecognizeRecordList.get(0);
					vo.setTransType(admRecognizeRecord.getOrderType());
					for(EnumOrderType ot:EnumOrderType.values()) {
						if(ot.getTypeTag().equals(admRecognizeRecord.getOrderType())) {
							vo.setTransContent(ot.getTypeName());
						}
					}
					vo.setAddMoney((int)admRecognizeRecord.getOrderPrice());
					vo.setTax((int)admRecognizeRecord.getTax());
				}

				//有被 PFD 管的 PFP 帳戶才有經銷商相關資料
				if (pfpCustInfo.getPfdUserAdAccountRefs().size() > 0) {
					PfdUserAdAccountRef pfdUserAdAccountRef = new ArrayList<PfdUserAdAccountRef>(pfpCustInfo.getPfdUserAdAccountRefs()).get(0);
					vo.setPfdCustInfoName(pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
					vo.setPfdUserName(pfdUserAdAccountRef.getPfdUser().getUserName());
					vo.setPayType(payTypeMap.get(pfdUserAdAccountRef.getPfpPayType()));
				}

				customerInfoReports.add(vo);
			}
		}

		return customerInfoReports;
	}

	/**
	 * 2014-05-02
	 * 強哥亂寫的
	 */
/*
	public List<CustomerInfoReportVO> findPfpCustomerInfosByDate(String startDate, String endDate,
			String pfdCustomerInfoId, String transType, String orderType) throws Exception{

		startDate += " 00:00:00";
		endDate += " 23:59:59";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<CustomerInfoReportVO> customerInfoReports = null;

		List<String> status = new ArrayList<String>();
		status.add(EnumAccountStatus.START.getStatus());
		status.add(EnumAccountStatus.CLOSE.getStatus());
		status.add(EnumAccountStatus.STOP.getStatus());

		List<String> pfpCustomerInfoIds = new ArrayList<String>();
		// 先取出所有有經銷商的 pfp_customer_info_id
		Map<String, PfdUserAdAccountRef> pfdUserAdAccountRefMap = new HashMap<String, PfdUserAdAccountRef>();

			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId, status);

			for(PfdUserAdAccountRef pfdUserAdAccountRef:refList) {
				pfpCustomerInfoIds.add(pfdUserAdAccountRef.getPfpCustomerInfo().getCustomerInfoId());
				pfdUserAdAccountRefMap.put(pfdUserAdAccountRef.getPfpCustomerInfo().getCustomerInfoId(), pfdUserAdAccountRef);
			}


		// 取出帳戶資料，如果有選擇經銷商，則僅會抓出有經銷商的帳戶資料，沒有經銷商時，會查出所有資料
		HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = ((IPfpCustomerInfoDAO) dao).getPfpCustomerInfoBySeqList(startDate, endDate, StringUtils.isBlank(pfdCustomerInfoId)?null:pfpCustomerInfoIds, status);

		// 如果有選擇經銷商，但是沒有查到任何經銷商所屬帳戶資料時，會將所有PFP的帳戶資料查出，這時候要清空資料，以免錯亂
		if(StringUtils.isNotBlank(pfdCustomerInfoId) && (pfpCustomerInfoIds == null || pfpCustomerInfoIds.size() == 0)) {
			pfpAdCustomerInfoIdMap = null;
		}

		// 抓取預付、後付的Tag
		Map<String, String> payTypeMap = new HashMap<String, String>();
		for (EnumPfdAccountPayType payType : EnumPfdAccountPayType.values()) {
			payTypeMap.put(payType.getPayType(), payType.getPayName());
		}

		//有選 PFD 帳戶又找得到對應的 PFP 帳戶 -> 查詢
		if (pfpAdCustomerInfoIdMap != null && pfpAdCustomerInfoIdMap.size() > 0) {

				List<String> orderTypes = new ArrayList<String>();
				if(StringUtils.isBlank(orderType)) {
					orderTypes.add(EnumOrderType.SAVE.getTypeTag());
					orderTypes.add(EnumOrderType.GIFT.getTypeTag());
					orderTypes.add(EnumOrderType.FEEDBACK.getTypeTag());
				} else {
					orderTypes.add(orderType);
				}

				TreeMap<String, List<AdmRecognizeRecord>> admRecognizeRecordMap = admRecognizeRecordDAO.getRecognizeRecordByCustomerInfoIdList(startDate, endDate, new ArrayList<String>(pfpAdCustomerInfoIdMap.keySet()), orderTypes);
				if(admRecognizeRecordMap != null && admRecognizeRecordMap.size() > 0){

					customerInfoReports = new ArrayList<CustomerInfoReportVO>();
					for(String customerInfoId :admRecognizeRecordMap.keySet()){
						List<AdmRecognizeRecord> admRecognizeRecords = admRecognizeRecordMap.get(customerInfoId);

						PfpCustomerInfo pfpCustomerInfo = pfpAdCustomerInfoIdMap.get(customerInfoId);
						if(pfpCustomerInfo != null) {
							Date activateDate = pfpCustomerInfo.getActivateDate();

							for(AdmRecognizeRecord admRecognizeRecord:admRecognizeRecords) {
								Date saveDate = admRecognizeRecord.getSaveDate();

								if(sdf.format(activateDate).equals(sdf.format(saveDate))) {
									CustomerInfoReportVO vo = new CustomerInfoReportVO();

									if(pfpCustomerInfo != null) {
										vo.setActivateDate(DateValueUtil.getInstance().dateToString(pfpCustomerInfo.getActivateDate()));
										vo.setMemberId(pfpCustomerInfo.getMemberId());
										vo.setCustomerInfoId(pfpCustomerInfo.getCustomerInfoId());
										vo.setCustomerInfoName(pfpCustomerInfo.getCustomerInfoTitle());
										if(pfpCustomerInfo.getCategory().equals("1")){
											vo.setCustomerInfoType("個人戶");
										}else{
											vo.setCustomerInfoType("公司戶");
										}

										vo.setTransType(admRecognizeRecord.getOrderType());
										for(EnumOrderType ot:EnumOrderType.values()) {
											if(ot.getTypeTag().equals(admRecognizeRecord.getOrderType())) {
												vo.setTransContent(ot.getTypeName());
											}
										}
										vo.setAddMoney((int)admRecognizeRecord.getOrderPrice());

										PfdUserAdAccountRef pfdUserAdAccountRef = pfdUserAdAccountRefMap.get(pfpCustomerInfo.getCustomerInfoId());
										if(pfdUserAdAccountRef != null) {
											vo.setPfdCustInfoName(pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
											vo.setPfdUserName(pfdUserAdAccountRef.getPfdUser().getUserName());
											vo.setPayType(payTypeMap.get(pfdUserAdAccountRef.getPfpPayType()));
										}

										customerInfoReports.add(vo);
									}
								}
							}
						}
					}
				}
		}

		return customerInfoReports;
	}
*/
    @Override
    public List<PfpCustomerInfo> getAllCustomerInfo() throws Exception {
    	return ((IPfpCustomerInfoDAO) dao).getAllCustomerInfo();
    }

    @Override
    public List<Object> getCustomerInfos(String keyword, String userAccount, String userEmail, String customerInfoStatus, int page, int pageSize,String pfdCustomerId) throws Exception{
    	return ((IPfpCustomerInfoDAO) dao).getCustomerInfos(keyword, userAccount, userEmail, customerInfoStatus, page, pageSize,pfdCustomerId);
    }

    @Override
    public List<PfpCustomerInfo> getExistentCustomerInfo() throws Exception {
        return ((IPfpCustomerInfoDAO) dao).getExistentCustomerInfo();
    }

    @Override
    public List<PfpCustomerInfo> getActivityCustomerInfo() throws Exception{
        return ((IPfpCustomerInfoDAO) dao).findValidCustomerInfo();
    }

    @Override
    public List<PfpCustomerInfo> findValidCustomerInfo(){
    	return ((IPfpCustomerInfoDAO) dao).findValidCustomerInfo();
    }

    @Override
    public List<PfpCustomerInfo> findValidCustomerInfo(String customerInfoId) {
    	return ((IPfpCustomerInfoDAO) dao).findValidCustomerInfo(customerInfoId);
    }

    @Override
    public List<PfpCustomerInfo> selectValidCustomerInfo(float remain) {
        return ((IPfpCustomerInfoDAO) dao).selectValidCustomerInfo(remain);
    }

    @Override
    public List<String> selectValidAdGroup(String type) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String pvclkDate = df.format(calendar.getTime());

        // cache
        Map<String, Float> actionCostCache = null;
        if ("ad".equals(type)) {
            ((IPfpCustomerInfoDAO) dao).updateLaterRemainLessThanAdCost(pvclkDate);
        }
        else if ("keyword".equals(type)) {
            ((IPfpCustomerInfoDAO) dao).updateLaterRemainLessThanAdKeywordCost(pvclkDate);
        }
        else {
            log.info("error type = " + type);
            return new ArrayList<String>();
        }

        List<String> groupList = new ArrayList<String>();

        List<Object[]> objList = ((IPfpCustomerInfoDAO) dao).selectValidAdGroup(pvclkDate, remain);
        String groupId = null;
        float laterRemain = 0;
        float controlPrice = 0;
        double customerCost = 0;
        double actionCost = 0;
        for (Object[] objs: objList) {
            if (objs == null) {
                continue;
            }
            if (objs.length < 7) {
                continue;
            }

            groupId = (String) objs[2];
            laterRemain = (Float) objs[3];
            controlPrice = (Float) objs[4];
            customerCost = (Double) objs[5];
            actionCost = (Double) objs[6];

            if (customerCost >= laterRemain) {
                continue;
            }
            if (actionCost >= controlPrice) {
                continue;
            }

            groupList.add(groupId);
        }

        return groupList;
    }

    @Override
    public List<PfpAd> selectValidAd() {
        List<PfpAd> list = new ArrayList<PfpAd>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(df.format(calendar.getTime())));
        } catch (ParseException pe) {
            //log.error(calendar.getTime(), pe);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        Date date = calendar.getTime();
        Date now = Calendar.getInstance().getTime();

        List<PfpCustomerInfo> pfpCustomerInfoList = ((IPfpCustomerInfoDAO) dao).selectValidCustomerInfo(remain);
        String customerInfoId = null;
        Float customerCost = 0f;
        Float actionCost = 0f;

        // cache
        Map<String, Float> customerCostCache = pfpAdPvclkDAO.selectAdClkPriceMapByPfpCustomerInfoId(date);
        Map<String, Float> actionCostCache = pfpAdPvclkDAO.selectAdClkPriceMapByPfpAdActionId(date);

        int customerSum = 0;
        int actionSum = 0;
        int groupSum = 0;
        int adSum = 0;
        int[] customerCounts = new int[1];
        int[] actionCounts = new int[4];
        int[] groupCounts = new int[1];
        int[] adCounts = new int[1];

        // check customerInfo remain
        for (PfpCustomerInfo pfpCustomerInfo: pfpCustomerInfoList) {
            customerSum++;

            customerInfoId = pfpCustomerInfo.getCustomerInfoId();

            //log.debug("customerInfoId = " + customerInfoId);

            try {
                customerCost = customerCostCache.get(customerInfoId);
                if (customerCost == null) {
                    customerCost = 0f;
                }
                if (customerCost >= pfpCustomerInfo.getLaterRemain()) {
                    //log.debug("cost(" + customerCost + ") >= remain(" + pfpCustomerInfo.getRemain() + ")");
                    if (pfpCustomerInfo.getLaterRemain() > 0) {
                        pfpCustomerInfo.setLaterRemain(0f);
                        pfpCustomerInfo.setUpdateDate(now);
                        update(pfpCustomerInfo);
                    }
                    continue;
                }
                customerCounts[0]++;

                // check adActionMax
                for (PfpAdAction pfpAdAction: pfpCustomerInfo.getPfpAdActions()) {
                    actionSum++;

                    // check status
                    if (pfpAdAction.getAdActionStatus() != EnumAdStatus.Open.getStatusId()) {
                        //log.debug("adActionStatus=" + pfpAdAction.getAdActionStatus());
                        continue;
                    }
                    actionCounts[0]++;

                    // check date
                    if (date.getTime() < pfpAdAction.getAdActionStartDate().getTime()) {
                        //log.debug("date(" + df.format(date) + ") < startDate(" + df.format(pfpAdAction.getAdActionStartDate().getTime()) + ")");
                        continue;
                    }
                    actionCounts[1]++;

                    if (date.getTime() > pfpAdAction.getAdActionEndDate().getTime()) {
                        //log.debug("date(" + df.format(date) + ") > endDate(" + df.format(pfpAdAction.getAdActionEndDate().getTime()) + ")");
                        continue;
                    }
                    actionCounts[2]++;

                    // check price
                    actionCost = actionCostCache.get(pfpAdAction.getAdActionSeq());
                    if (actionCost == null) {
                        actionCost = 0f;
                    }
                    if (actionCost >= pfpAdAction.getAdActionControlPrice()) {
                        //log.debug("cost(" + actionCost + ") >= max(" + pfpAdAction.getAdActionControlPrice() + ")");
                        continue;
                    }
                    actionCounts[3]++;

                    // add adDetail
                    for (PfpAdGroup pfpAdGroup: pfpAdAction.getPfpAdGroups()) {
                        groupSum++;

                        if (pfpAdGroup.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                            //log.debug("adGroupStatus=" + pfpAdGroup.getAdGroupStatus());
                            continue;
                        }
                        groupCounts[0]++;

                        for (PfpAd pfpAd: pfpAdGroup.getPfpAds()) {
                            adSum++;

                            if (pfpAd.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                                //log.debug("adStatus=" + pfpAd.getAdStatus());
                                continue;
                            }
                            adCounts[0]++;

                            list.add(pfpAd);
                            log.info("add " + pfpAd.getAdSeq());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("customerInfoId=" + customerInfoId, e);
            }
        }

        for (int i= 0; i < customerCounts.length; i++) {
            log.info("customerCount[" + i + "]=" + customerCounts[i] + "/" + customerSum);
        }
        for (int i= 0; i < actionCounts.length; i++) {
            log.info("actionCount[" + i + "]=" + actionCounts[i] + "/" + actionSum);
        }
        for (int i= 0; i < groupCounts.length; i++) {
            log.info("groupCount[" + i + "]=" + groupCounts[i] + "/" + groupSum);
        }
        for (int i= 0; i < adCounts.length; i++) {
            log.info("adCount[" + i + "]=" + adCounts[i] + "/" + adSum);
        }

        return list;
    }

    @Override
    public List<PfpAdDetail> selectValidAdDetail() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(df.format(calendar.getTime())));
        } catch (ParseException pe) {
            //log.error(calendar.getTime(), pe);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        return selectValidAdDetail(calendar.getTime());
    }

    @Override
    public List<PfpAdDetail> selectValidAdDetail(Date date) {
        List<PfpAdDetail> list = new ArrayList<PfpAdDetail>();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        List<PfpCustomerInfo> pfpCustomerInfoList = ((IPfpCustomerInfoDAO) dao).selectValidCustomerInfo(0);
        String customerInfoId = null;
        Float customerCost = 0f;
        Float actionCost = 0f;

        // cache
        Map<String, Float> customerCostCache = pfpAdPvclkDAO.selectAdClkPriceMapByPfpCustomerInfoId(date);
        Map<String, Float> actionCostCache = pfpAdPvclkDAO.selectAdClkPriceMapByPfpAdActionId(date);

//        log.info("customer " + pfpCustomerInfoList.size());

        // check customerInfo remain
        for (PfpCustomerInfo pfpCustomerInfo: pfpCustomerInfoList) {
            customerInfoId = pfpCustomerInfo.getCustomerInfoId();

            //log.info("customerInfoId = " + customerInfoId);

            try {
                customerCost = customerCostCache.get(customerInfoId);
                if (customerCost == null) {
                    customerCost = 0f;
                }
                if (customerCost >= pfpCustomerInfo.getLaterRemain()) {
                    //log.debug("cost(" + customerCost + ") >= remain(" + pfpCustomerInfo.getRemain() + ")");
                    if (pfpCustomerInfo.getLaterRemain() > 0) {
                        pfpCustomerInfo.setLaterRemain(0f);
                        pfpCustomerInfo.setUpdateDate(calendar.getTime());
                        update(pfpCustomerInfo);
                    }
                    continue;
                }

                //log.info("action " + pfpCustomerInfo.getPfpAdActions().size());


                // check adActionMax
                for (PfpAdAction pfpAdAction: pfpCustomerInfo.getPfpAdActions()) {

                    //log.info(i+"---> action seq="+ pfpAdAction.getAdActionSeq());

                    // check status
                    if (pfpAdAction.getAdActionStatus() != EnumAdStatus.Open.getStatusId()) {
                        //log.info("action status error seq="+ pfpAdAction.getAdActionSeq()+",status="+pfpAdAction.getAdActionStatus());
                        //log.debug("adActionStatus=" + pfpAdAction.getAdActionStatus());
                        continue;
                    }

                    // check date
                    if (date.getTime() < pfpAdAction.getAdActionStartDate().getTime()) {
                        //log.info("action date error seq="+ pfpAdAction.getAdActionSeq());
                        //log.info("date(" + df.format(date) + ") < startDate(" + df.format(pfpAdAction.getAdActionStartDate().getTime()) + ")");
                        continue;
                    }
                    if (date.getTime() > pfpAdAction.getAdActionEndDate().getTime()) {
                        //log.info("action date error seq="+ pfpAdAction.getAdActionSeq());
                        //log.info("date(" + df.format(date) + ") > endDate(" + df.format(pfpAdAction.getAdActionEndDate().getTime()) + ")");
                        continue;
                    }

                    // offline earlier
                    if (date.getTime() == pfpAdAction.getAdActionEndDate().getTime()) {
                        if ((hour >= 23) && (minute >= 30)) {
                            continue;
                        }
                    }

                    //log.info("action date ok seq="+ pfpAdAction.getAdActionSeq());

                    // check price
                    actionCost = actionCostCache.get(pfpAdAction.getAdActionSeq());
                    if (actionCost == null) {
                        actionCost = 0f;
                    }
                    if (actionCost >= pfpAdAction.getAdActionControlPrice()) {
                        //log.debug("cost(" + actionCost + ") >= max(" + pfpAdAction.getAdActionControlPrice() + ")");
                        continue;
                    }

                    // add adDetail
                    //log.info("action price ok seq="+ pfpAdAction.getAdActionSeq());
                    //log.info("group size="+ pfpAdAction.getPfpAdGroups().size());
                    for (PfpAdGroup pfpAdGroup: pfpAdAction.getPfpAdGroups()) {
                        //log.info("action group status error seq="+ pfpAdAction.getAdActionSeq());
                        if (pfpAdGroup.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                            //log.info("adGroupStatus=" + pfpAdGroup.getAdGroupSeq()+","+pfpAdGroup.getAdGroupStatus());
                            continue;
                        }

                        //log.info("ad size="+ pfpAdGroup.getPfpAds());
                        for (PfpAd pfpAd: pfpAdGroup.getPfpAds()) {
                            //log.info("action ad status error seq="+ pfpAdGroup.getAdGroupSeq());
                            if (pfpAd.getAdStatus() != EnumAdStatus.Open.getStatusId()) {
                                //log.info("adStatus=" +pfpAd.getAdSeq()+","+ pfpAd.getAdStatus());
                                continue;
                            }


                            list.addAll(pfpAd.getPfpAdDetails());
                            //log.info("add " + pfpAd.getAdSeq());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("customerInfoId=" + customerInfoId, e);
            }
        }

//        log.info("size=" + list.size());

        return list;
    }

    @Override
    public List<PfpAdKeyword> selectValidAdKeyword() {
        List<PfpAdKeyword> list = new ArrayList<PfpAdKeyword>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(df.format(calendar.getTime())));
        } catch (ParseException pe) {
            //log.error(calendar.getTime(), pe);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        Date date = calendar.getTime();
        Date now = Calendar.getInstance().getTime();

        List<PfpCustomerInfo> pfpCustomerInfoList = ((IPfpCustomerInfoDAO) dao).selectValidCustomerInfo(remain);
        String customerInfoId = null;
        Float customerCost = 0f;
        Float actionCost = 0f;

        // cache
        Map<String, Float> customerCostCache = pfpAdKeywordPvclkDAO.selectAdKeywordClkPriceMapByPfpCustomerInfoId(date);
        Map<String, Float> actionCostCache = pfpAdKeywordPvclkDAO.selectAdKeywordClkPriceMapByPfpAdActionId(date);

        int customerSum = 0;
        int actionSum = 0;
        int groupSum = 0;
        int adSum = 0;
        int[] customerCounts = new int[1];
        int[] actionCounts = new int[4];
        int[] groupCounts = new int[1];
        int[] adCounts = new int[1];

        // check customerInfo remain
        for (PfpCustomerInfo pfpCustomerInfo: pfpCustomerInfoList) {
            customerSum++;

            customerInfoId = pfpCustomerInfo.getCustomerInfoId();

            //log.debug("customerInfoId = " + customerInfoId);

            try {
                customerCost = customerCostCache.get(customerInfoId);
                if (customerCost == null) {
                    customerCost = 0f;
                }
                if (customerCost >= pfpCustomerInfo.getLaterRemain()) {
                    //log.debug("cost(" + customerCost + ") >= remain(" + pfpCustomerInfo.getRemain() + ")");
                    if (pfpCustomerInfo.getLaterRemain() > 0) {
                        pfpCustomerInfo.setLaterRemain(0f);
                        pfpCustomerInfo.setUpdateDate(now);
                        update(pfpCustomerInfo);
                    }
                    continue;
                }
                customerCounts[0]++;

                // check adActionMax
                for (PfpAdAction pfpAdAction: pfpCustomerInfo.getPfpAdActions()) {
                    actionSum++;

                    // check status
                    if (pfpAdAction.getAdActionStatus() != EnumAdStatus.Open.getStatusId()) {
                        //log.debug("adActionStatus=" + pfpAdAction.getAdActionStatus());
                        continue;
                    }
                    actionCounts[0]++;

                    // check date
                    if (date.getTime() < pfpAdAction.getAdActionStartDate().getTime()) {
                        //log.debug("date(" + df.format(date) + ") < startDate(" + df.format(pfpAdAction.getAdActionStartDate().getTime()) + ")");
                        continue;
                    }
                    actionCounts[1]++;

                    if (date.getTime() > pfpAdAction.getAdActionEndDate().getTime()) {
                        //log.debug("date(" + df.format(date) + ") > endDate(" + df.format(pfpAdAction.getAdActionEndDate().getTime()) + ")");
                        continue;
                    }
                    actionCounts[2]++;

                    // check price
                    actionCost = actionCostCache.get(pfpAdAction.getAdActionSeq());
                    if (actionCost == null) {
                        actionCost = 0f;
                    }
                    if (actionCost >= pfpAdAction.getAdActionControlPrice()) {
                        //log.debug("cost(" + actionCost + ") >= max(" + pfpAdAction.getAdActionControlPrice() + ")");
                        continue;
                    }
                    actionCounts[3]++;

                    // add adDetail
                    for (PfpAdGroup pfpAdGroup: pfpAdAction.getPfpAdGroups()) {
                        groupSum++;

                        if (pfpAdGroup.getAdGroupStatus() != EnumAdStatus.Open.getStatusId()) {
                            //log.debug("adGroupStatus=" + pfpAdGroup.getAdGroupStatus());
                            continue;
                        }
                        groupCounts[0]++;

                        for (PfpAdKeyword pfpAdKeyword: pfpAdGroup.getPfpAdKeywords()) {
                            adSum++;

                            if (pfpAdKeyword.getAdKeywordStatus() != EnumAdStatus.Open.getStatusId()) {
                                //log.debug("adKeywordStatus=" + pfpAdKeyword.getAdKeywordStatus());
                                continue;
                            }
                            adCounts[0]++;

                            list.add(pfpAdKeyword);
//                            log.info("add " + pfpAdKeyword.getAdKeywordSeq() + " " + pfpAdKeyword.getAdKeyword());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("customerInfoId=" + customerInfoId, e);
            }
        }

        for (int i= 0; i < customerCounts.length; i++) {
            log.info("customerCount[" + i + "]=" + customerCounts[i] + "/" + customerSum);
        }
        for (int i= 0; i < actionCounts.length; i++) {
            log.info("actionCount[" + i + "]=" + actionCounts[i] + "/" + actionSum);
        }
        for (int i= 0; i < groupCounts.length; i++) {
            log.info("groupCount[" + i + "]=" + groupCounts[i] + "/" + groupSum);
        }
        for (int i= 0; i < adCounts.length; i++) {
            log.info("adCount[" + i + "]=" + adCounts[i] + "/" + adSum);
        }

        return list;
    }

    @Override
    public List<ValidKeywordBean> selectValidAdKeyword(List<PfpAdExcludeKeyword> excludeKeywordList, Map<String, Float> syspriceMap, String groupId, String pvclkDate) {
        List<Object[]> objList = ((IPfpCustomerInfoDAO) dao).selectValidAdKeyword(groupId, pvclkDate, remain);

        // exclude keyword
        StringBuffer excludeKeyword = new StringBuffer();
        for (PfpAdExcludeKeyword adExcludeKeyword: excludeKeywordList) {
            excludeKeyword.append(adExcludeKeyword.getAdExcludeKeyword()).append(",");
        }

        // sysprice
        Float sysprice = null;

        // search & channel price
        float keywordSearchPrice = 0f;
        float keywordSearchPhrasePrice = 0f;
        float keywordSearchPrecisionPrice = 0f;
        float keywordChannelPrice = 0f;

        List<ValidKeywordBean> keywordList = new ArrayList<ValidKeywordBean>();
        ValidKeywordBean bean = null;

        for (Object[] objs: objList) {
            bean = new ValidKeywordBean();
            bean.setAdActionId((String) objs[0]);
            bean.setAdGroupId((String) objs[1]);
            bean.setAdId((String) objs[2]);
            bean.setAdKeywordId((String) objs[3]);
            bean.setAdKeyword((String) objs[4]);
            bean.setAdExcludeKeyword(excludeKeyword.toString());
            bean.setAdActionControlPrice((Float) objs[5]);
            bean.setAdKeywordSearchPrice((Float) objs[6]);
            bean.setAdKeywordSearchPhrasePrice((Float) objs[7]);
            bean.setAdKeywordSearchPrecisionPrice((Float) objs[8]);
            bean.setAdKeywordChannelPrice((Float) objs[9]);
            bean.setAdKeywordOpen((Integer) objs[10]);
            bean.setAdKeywordPhraseOpen((Integer) objs[11]);
            bean.setAdKeywordPrecisionOpen((Integer) objs[12]);
            bean.setAdGroupSearchPriceType((Integer) objs[13]);
            bean.setAdKeywordPv(((BigDecimal) objs[14]).intValue());
            bean.setAdKeywordClk(((BigDecimal) objs[15]).intValue());
            bean.setRecognize((String) objs[16]);
            bean.setUpdateDate((Date) objs[17]);
            bean.setCreateDate((Date) objs[18]);
            bean.setPk(bean.getAdId() + "_" + bean.getAdKeywordId());

            // sysprice
            sysprice = syspriceMap.get(bean.getAdKeyword());
            if (sysprice != null) {
                bean.setAdKeywordSystemPrice(sysprice);
            }
            else {
                bean.setAdKeywordSystemPrice(keywordSysprice);
            }

            // special rule
            if ("Y".equals(bean.getRecognize())) {
                keywordSearchPrice = Math.min(bean.getAdKeywordSearchPrice(), bean.getAdKeywordSystemPrice());
                keywordSearchPhrasePrice = Math.min(bean.getAdKeywordSearchPhrasePrice(), bean.getAdKeywordSystemPrice() + RandomUtils.nextInt(3) + 1);
                keywordSearchPrecisionPrice = Math.min(bean.getAdKeywordSearchPrecisionPrice(), bean.getAdKeywordSystemPrice() + RandomUtils.nextInt(3) + 4);

//                switch (bean.getAdGroupSearchPriceType()) {
//                case 1:
//                    bean.setAdKeywordSearchPrice(bean.getAdKeywordSystemPrice());
//                    bean.setAdKeywordSearchPhrasePrice(keywordSearchPhrasePrice);
//                    bean.setAdKeywordSearchPrecisionPrice(keywordSearchPrecisionPrice);
//
//                    break;
//                case 2:
//                default:
                    bean.setAdKeywordSearchPrice(keywordSearchPrice);
                    bean.setAdKeywordSearchPhrasePrice(keywordSearchPhrasePrice);
                    bean.setAdKeywordSearchPrecisionPrice(keywordSearchPrecisionPrice);
//
//                    break;
//                }

                keywordChannelPrice = bean.getAdKeywordChannelPrice() > bean.getAdKeywordSystemPrice() ? bean.getAdKeywordSystemPrice() : bean.getAdKeywordChannelPrice();
                bean.setAdKeywordChannelPrice(keywordChannelPrice);
            }
            else {
                bean.setAdKeywordSearchPrice(0f);
                bean.setAdKeywordSearchPhrasePrice(0f);
                bean.setAdKeywordSearchPrecisionPrice(0f);
                bean.setAdKeywordChannelPrice(0f);
            }

            // temp price
            bean.setAdKeywordTempPrice((bean.getAdKeywordPv() > 0 ? (bean.getAdKeywordClk() / bean.getAdKeywordPv() * 10) : 0) + RandomUtils.nextFloat());

            keywordList.add(bean);
        }

        return keywordList;
    }

    @Override
    public List<PfpCustomerInfo> findValidCustomerInfoByMemberId(String memberId) {
    	return ((IPfpCustomerInfoDAO) dao).findValidCustomerInfoByMemberId(memberId);
    }

    @Override
    public List<PfpCustomerInfo> selectCustomerInfo(EnumAccountStatus enumAccountStatus) {
        return ((IPfpCustomerInfoDAO) dao).selectCustomerInfo(enumAccountStatus);
    }

    @Override
    public float selectCustomerInfoRemain() {
        return ((IPfpCustomerInfoDAO) dao).selectCustomerInfoRemain();
    }

    @Override
    public int selectCustomerInfoIdCountByActivateDate(String activateDate) {
        return ((IPfpCustomerInfoDAO) dao).selectCustomerInfoIdCountByActivateDate(activateDate);
    }


    @Override
    public int selectCustomerInfoIdCountByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, String activateDate) {
        return ((IPfpCustomerInfoDAO) dao).selectCustomerInfoIdCountByActivateDate(enumPfdAccountPayType, activateDate);
    }


    @Override
    public List<PfpCustomerInfo> findPfpCustomerInfo(String pcId) {
    	 return ((IPfpCustomerInfoDAO) dao).findPfpCustomerInfo(pcId);
    }


    @Override
    public Integer deletePfpCustomerInfo(String pfpCustomerInfoId){
    	return ((IPfpCustomerInfoDAO) dao).deletePfpCustomerInfo(pfpCustomerInfoId);
    }


    @Override
    @Transactional
    public List<PfpAccountVO> findManagerPfpAccount(String memberId, Date startDate, Date endDate) {

    	List<PfpAccountVO> vos = new ArrayList<PfpAccountVO>();

    	// 先取出 PFP 帳戶
    	List<Object> objects = ((IPfpCustomerInfoDAO) dao).findManagerPfpCustomerInfo(memberId);

		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[0] != null){

				PfpAccountVO vo = new PfpAccountVO();
				String status = ob[2].toString();				// pfp status

				vo.setCustomerInfoId(ob[0].toString());			// pfp customerInfoId
				vo.setCustomerInfoName(ob[1].toString());		// pfp customerInfoTitle

				// pfp status
				for(EnumAccountStatus enumStatus:EnumAccountStatus.values()){
					if(enumStatus.getStatus().equals(status)){
						vo.setCustomerInfoStatus(enumStatus.getDescription());
						break;
					}
				}

				vo.setCustomerInfoRemain(Float.parseFloat(ob[3].toString()));		// pfp remain
				vo.setManagerMemberId(ob[4].toString());							// manager memeberId
				vo.setManagerName(ob[5].toString());								// manager name
				vo.setMemberId(ob[6].toString());

				vos.add(vo);
			}
		}

		// 取經銷商關聯
		List<PfdUserAdAccountRef> pfdUserAdAccountRefs = pfdUserAdAccountRefDAO.findPfdUserAdAccountRefs();

		for(PfpAccountVO vo:vos){

			for(PfdUserAdAccountRef ref:pfdUserAdAccountRefs){

				if(vo.getCustomerInfoId().equals(ref.getPfpCustomerInfo().getCustomerInfoId())){

					vo.setPfdCustomerInfoId(ref.getPfdCustomerInfo().getCustomerInfoId());
					vo.setPfdCustomerInfoName(ref.getPfdCustomerInfo().getCompanyName());

					// pfp payType
					if(ref.getPfpPayType().equals(EnumPfdAccountPayType.ADVANCE.getPayType())){
						vo.setCustomerInfoPayType(EnumPfdAccountPayType.ADVANCE.getPayName());
					}else if(ref.getPfpPayType().equals(EnumPfdAccountPayType.LATER.getPayType())) {
						vo.setCustomerInfoPayType(EnumPfdAccountPayType.LATER.getPayName());
					}

					break;
				}
			}

		}

		// 取1星期的廣告花費
		List<Object> reports = pfpAdActionReportDAO.findPfpAdActionReport(startDate, endDate);

		for(PfpAccountVO vo:vos){

			for(Object object:reports){

				Object[] ob = (Object[])object;

				if(ob[0] != null){

					String pfpCustomerInfoId = ob[0].toString();

					if(vo.getCustomerInfoId().equals(pfpCustomerInfoId)){
						vo.setOneWeekAdCost(Float.parseFloat(ob[1].toString()));
						break;
					}
				}

			}
		}

//    	if(StringUtils.isNotBlank(memberId)){
//    		// 一般管理者權限
//    		vos = this.findManagerAccount(memberId);
//    	}
//    	else{
//    		// 最大管理者權限
//    		vos = this.findAllPfpCustomerInfo();
//    	}
//    	List<Object> list = ((IPfpCustomerInfoDAO) dao).findManagerPfpAccount(memberId, startDate, endDate);
//
//    	List<PfpAccountVO> vos = new ArrayList<PfpAccountVO>();
//
//    	for(Object object:list){
//
//			Object[] ob = (Object[])object;
//
//			if(ob[0] != null){
//
//				PfpAccountVO vo = new PfpAccountVO();
//				String payType = ob[4].toString();
//				String status = ob[2].toString();
//
//				vo.setCustomerInfoId(ob[0].toString());								// pfp customerInfoId
//				vo.setCustomerInfoName(ob[1].toString());							// pfp customerInfoTitle
//
//				//log.info(">>> CustomerInfoId: "+ob[0].toString());
//
//				// pfp status
//				for(EnumAccountStatus enumStatus:EnumAccountStatus.values()){
//					if(enumStatus.getStatus().equals(status)){
//						vo.setCustomerInfoStatus(enumStatus.getDescription());
//						break;
//					}
//				}
//
//				vo.setCustomerInfoRemain(Float.parseFloat(ob[3].toString()));		// pfp remain
//
//				// pfp payType
//				if(EnumPfdAccountPayType.ADVANCE.getPayType().equals(payType)){
//					vo.setCustomerInfoPayType(EnumPfdAccountPayType.ADVANCE.getPayName());
//				}else if(EnumPfdAccountPayType.LATER.getPayType().equals(payType)) {
//					vo.setCustomerInfoPayType(EnumPfdAccountPayType.LATER.getPayName());
//				}
//
//				vo.setPfdCustomerInfoId(ob[5].toString());							// pfd customerInfoId
//				vo.setPfdCustomerInfoName(ob[6].toString());						// pfd companyName
//				vo.setOneWeekAdCost(Float.parseFloat(ob[7].toString()));			// one week pvClkPrice
//
//				//log.info(">>> WeekAdCost: "+ob[7].toString());
//
//				vo.setManagerMemberId(ob[8].toString());							// manager memeberId
//				vo.setManagerName(ob[9].toString());								// manager name
//
//				vos.add(vo);
//			}
//    	}

    	return vos;
    }

    @Override
    public Map<String,String> findPfpCustomerInfoNameMap() throws Exception{
    	Map<String,String> map = new LinkedHashMap<String,String>();

    	List<PfpCustomerInfo> list = ((IPfpCustomerInfoDAO) dao).getAllCustomerInfo();

    	for(PfpCustomerInfo pfpCustomerInfo:list){
    		map.put(pfpCustomerInfo.getCustomerInfoId(), pfpCustomerInfo.getCustomerInfoTitle());
    	}

    	return map;
    }

    public void setPfpAdPvclkDAO(IPfpAdPvclkDAO pfpAdPvclkDAO) {
        this.pfpAdPvclkDAO = pfpAdPvclkDAO;
    }

    public void setPfpAdKeywordPvclkDAO(IPfpAdKeywordPvclkDAO pfpAdKeywordPvclkDAO) {
        this.pfpAdKeywordPvclkDAO = pfpAdKeywordPvclkDAO;
    }

	public void setPfpAdActionReportDAO(IPfpAdActionReportDAO pfpAdActionReportDAO) {
		this.pfpAdActionReportDAO = pfpAdActionReportDAO;
	}

	public void setPfdUserAdAccountRefDAO(IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO) {
		this.pfdUserAdAccountRefDAO = pfdUserAdAccountRefDAO;
	}

	public void setAdmRecognizeRecordDAO(IAdmRecognizeRecordDAO admRecognizeRecordDAO) {
		this.admRecognizeRecordDAO = admRecognizeRecordDAO;
	}

    public void setRemain(float remain) {
        this.remain = remain;
    }

    public void setKeywordSysprice(float keywordSysprice) {
        this.keywordSysprice = keywordSysprice;
    }

    @Override
    public void saveOrUpdateWithCommit(PfpCustomerInfo pfpCustomerInfo) throws Exception{
		((IPfpCustomerInfoDAO)dao).saveOrUpdateWithCommit(pfpCustomerInfo);
	}

//	public static void main(String arg[]) throws Exception{
//
//		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.path);
//
//		Logger log = Logger.getLogger(PfpCustomerInfoService.class);
//
//		PfpCustomerInfoService service = (PfpCustomerInfoService) context.getBean("PfpCustomerInfoService");
//
//		AccountVO vo = service .findRegisterDataById("reantoilpc");
//
//	}

    @Override
    public List<PfpCustomerInfo> findCustomerInfoIds(List<String> customerInfoList) throws Exception{
    	return ((IPfpCustomerInfoDAO) dao).findCustomerInfoIds(customerInfoList);
    }

    @Override
    @Transactional
    public List<String> findTransDetailPfp(String yesterday , String today , String tomorrow) throws Exception {

    	List<String> vos = new ArrayList<String>();

    	// 先取出昨日開始至今有交易的pfp
    	List<Object> objects = ((IPfpCustomerInfoDAO) dao).findTransDetailPfp(yesterday , today , tomorrow);

		for(Object object:objects){
				String pfp = (String) object;
				vos.add(pfp);
		}
    	return vos;
    }
}
