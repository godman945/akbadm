package com.pchome.akbadm.db.service.trans;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.dao.trans.IPfpTransDetailDAO;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.PfpTransDetailReportVO;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class PfpTransDetailService extends BaseService <PfpTransDetail, String> implements IPfpTransDetailService{


	private IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO;

	public void setPfdUserAdAccountRefDAO(IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO) {
		this.pfdUserAdAccountRefDAO = pfdUserAdAccountRefDAO;
	}

	@Override
    public List<PfpTransDetail> findTransDetail(String customerInfo, String startDate, String endDate,
			String pfdCustomerInfoId) throws Exception {

		String pfpCustInfoId = "";
		if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId);

			for (int i=0; i<refList.size(); i++) {
				pfpCustInfoId += "'" + refList.get(i).getPfpCustomerInfo().getCustomerInfoId() + "',";
			}
			if (!pfpCustInfoId.equals("")) {
				pfpCustInfoId = pfpCustInfoId.substring(0, pfpCustInfoId.length()-1);
			}
		}
		log.info(">>> pfpCustInfoId = " + pfpCustInfoId);

		//有選 PFD 帳戶卻找不到對應的 PFP 帳戶 -> 不查詢
		if (StringUtils.isNotEmpty(pfdCustomerInfoId) && StringUtils.isEmpty(pfpCustInfoId)) {
			return null;
		}

		return ((IPfpTransDetailDAO) dao).findTransDetail(customerInfo, startDate, endDate, pfpCustInfoId);
	}

	@Override
    public List<PfpTransDetailReportVO> findPfpTransDetailReport(String startDate, String endDate,
			String pfdCustomerInfoId) throws Exception {

		String pfpCustInfoId = "";
		if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId);

			for (int i=0; i<refList.size(); i++) {
				pfpCustInfoId += "'" + refList.get(i).getPfpCustomerInfo().getCustomerInfoId() + "',";
			}
			if (!pfpCustInfoId.equals("")) {
				pfpCustInfoId = pfpCustInfoId.substring(0, pfpCustInfoId.length()-1);
			}
		}
		log.info(">>> pfpCustInfoId = " + pfpCustInfoId);

		//有選 PFD 帳戶卻找不到對應的 PFP 帳戶 -> 不查詢
		if (StringUtils.isNotEmpty(pfdCustomerInfoId) && StringUtils.isEmpty(pfpCustInfoId)) {
			return null;
		}

		List<PfpTransDetail> dataList = ((IPfpTransDetailDAO) dao).findPfpTransDetail(startDate, endDate,
				pfpCustInfoId);

		Map<String, PfpTransDetailReportVO> map = new LinkedHashMap<String, PfpTransDetailReportVO>();

		for (int i=0; i<dataList.size(); i++) {
			PfpTransDetail transDetail = dataList.get(i);

			String reportDate = DateValueUtil.getInstance().dateToString(transDetail.getTransDate());
			String transType = transDetail.getTransType();

			PfpTransDetailReportVO vo = null;
			if (map.containsKey(reportDate)) {
				vo = map.get(reportDate);
			} else {
				vo = new PfpTransDetailReportVO();
				vo.setReportDate(reportDate);
				map.put(reportDate, vo);
			}

			if (transType.equals(EnumTransType.ADD_MONEY.getTypeId())) {
				vo.setAdd(vo.getAdd() + transDetail.getTransPrice());
				vo.setTax(vo.getTax() + transDetail.getTax());
			} else if (transType.equals(EnumTransType.SPEND_COST.getTypeId())) {
				vo.setSpend(vo.getSpend() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.REFUND.getTypeId())) {
				vo.setRefund(vo.getRefund() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.INVALID_COST.getTypeId())) {
				vo.setInvalid(vo.getInvalid() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.GIFT.getTypeId())) {
				vo.setFree(vo.getFree() + transDetail.getTransPrice());
			} else if(transType.equals(EnumTransType.LATER_SAVE.getTypeId())){
				vo.setAdd(vo.getAdd() + transDetail.getTransPrice());
			} else if(transType.equals(EnumTransType.LATER_REFUND.getTypeId())){
				vo.setRefund(vo.getRefund() + transDetail.getTransPrice());
			}
		}

		List<PfpTransDetailReportVO> voList = new ArrayList<PfpTransDetailReportVO>(map.values());

		return voList;
	}

	@Override
    public List<PfpTransDetailReportVO> findPfpTransDetailReportDetail(String reportDate,
			String pfdCustomerInfoId) throws Exception {

		String pfpCustInfoId = "";
		if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId);

			for (int i=0; i<refList.size(); i++) {
				pfpCustInfoId += "'" + refList.get(i).getPfpCustomerInfo().getCustomerInfoId() + "',";
			}
			if (!pfpCustInfoId.equals("")) {
				pfpCustInfoId = pfpCustInfoId.substring(0, pfpCustInfoId.length()-1);
			}
		}
		log.info(">>> pfpCustInfoId = " + pfpCustInfoId);

		//有選 PFD 帳戶卻找不到對應的 PFP 帳戶 -> 不查詢
		if (StringUtils.isNotEmpty(pfdCustomerInfoId) && StringUtils.isEmpty(pfpCustInfoId)) {
			return null;
		}

		List<PfpTransDetail> dataList = ((IPfpTransDetailDAO) dao).findPfpTransDetail(reportDate, reportDate,
				pfpCustInfoId);

		Map<String, PfpTransDetailReportVO> map = new LinkedHashMap<String, PfpTransDetailReportVO>();

		for (int i=0; i<dataList.size(); i++) {
			PfpTransDetail transDetail = dataList.get(i);

			String customerInfoId = transDetail.getPfpCustomerInfo().getCustomerInfoId();
			String customerInfoName = transDetail.getPfpCustomerInfo().getCustomerInfoTitle();

			String transType = transDetail.getTransType();

			PfpTransDetailReportVO vo = null;
			if (map.containsKey(customerInfoId)) {
				vo = map.get(customerInfoId);
			} else {
				vo = new PfpTransDetailReportVO();
				vo.setReportDate(reportDate);
				vo.setCustomerInfoId(customerInfoId);
				vo.setCustomerInfoName(customerInfoName);
				map.put(customerInfoId, vo);
			}

			if (transType.equals(EnumTransType.ADD_MONEY.getTypeId())) {
				vo.setAdd(vo.getAdd() + transDetail.getTransPrice());
				vo.setTax(vo.getTax() + transDetail.getTax());
			} else if (transType.equals(EnumTransType.SPEND_COST.getTypeId())) {
				vo.setSpend(vo.getSpend() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.REFUND.getTypeId())) {
				vo.setRefund(vo.getRefund() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.INVALID_COST.getTypeId())) {
				vo.setInvalid(vo.getInvalid() + transDetail.getTransPrice());
			} else if (transType.equals(EnumTransType.GIFT.getTypeId())) {
				vo.setFree(vo.getFree() + transDetail.getTransPrice());
			} else if(transType.equals(EnumTransType.LATER_SAVE.getTypeId())){
				vo.setAdd(vo.getAdd() + transDetail.getTransPrice());
			} else if(transType.equals(EnumTransType.LATER_REFUND.getTypeId())){
				vo.setRefund(vo.getRefund() + transDetail.getTransPrice());
			}
		}

		List<PfpTransDetailReportVO> voList = new ArrayList<PfpTransDetailReportVO>(map.values());

		return voList;
	}

	@Override
    public List<PfpTransDetail> checkExistentTransDetail(String customerInfoId, Date date, String transType) throws Exception{
		return ((IPfpTransDetailDAO) dao).checkExistentTransDetail(customerInfoId, date, transType);
	}

//	public void createTransDetail(SettlementVO vo, PfpCustomerInfo customerInfo, float remain, float tax, EnumTransType enumTransType, float transPrice, Date date)throws Exception{
//
//		PfpTransDetail transDetail = new PfpTransDetail();
//
//		transDetail.setIncomeExpense(enumTransType.getOperator());
//		transDetail.setPfpCustomerInfo(customerInfo);
//		transDetail.setRemain(remain);
//		transDetail.setTax(tax);
//		transDetail.setTransContent(EnumTransType.GIFT.getChName());
//		transDetail.setTransDate(date);
//		transDetail.setTransPrice(transPrice);
//		transDetail.setTransType(EnumTransType.GIFT.getTypeId());
//		transDetail.setTotalSavePrice(vo.getLastTotalSaveMoney());
//		transDetail.setTotalSpendPrice(vo.getLastTotalSpendCost());
//		transDetail.setTotalRetrievePrice(vo.getLastTotalInvalidCost());
//		transDetail.setRemain(vo.getLastRemain());
//
//		Date today = new Date();
//		transDetail.setUpdateDate(today);
//		transDetail.setCreateDate(today);
//
//		((IPfpTransDetailDAO) dao).saveOrUpdate(transDetail);
//	}

	@Override
    public SettlementVO findLatestTransDetail(String customerInfoId) {
		SettlementVO vo = null;
		List<PfpTransDetail> transDetails = ((IPfpTransDetailDAO) dao).sortTransDetailDesc(customerInfoId, 0, 1);

		if(!transDetails.isEmpty()){

			PfpTransDetail transDetail = transDetails.get(0);

			vo = new SettlementVO();

			vo.setCustomerInfoId(transDetail.getPfpCustomerInfo().getCustomerInfoId());
			vo.setTotalAddMoney(transDetail.getTotalSavePrice());
			vo.setTotalSpend(transDetail.getTotalSpendPrice());
			vo.setTax(transDetail.getTax());
			vo.setTotalRetrieve(transDetail.getTotalRetrievePrice());
			vo.setRemain(transDetail.getRemain());
			vo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
			vo.setTotalLaterSpend(transDetail.getTotalLaterSpend());
			//vo.setLaterRemain(transDetail.getLaterRemain());

		}

		return vo;
	}

    @Override
    public SettlementVO findLatestTransDetail2(String customerInfoId) {
        SettlementVO vo = null;
        List<PfpTransDetail> transDetails = ((IPfpTransDetailDAO) dao).sortTransDetailDesc(customerInfoId, 0, 1);

        if(!transDetails.isEmpty()){
            PfpTransDetail transDetail = transDetails.get(0);

            vo = new SettlementVO();

            vo.setCustomerInfoId(transDetail.getPfpCustomerInfo().getCustomerInfoId());
            vo.setTotalAddMoney(transDetail.getTotalSavePrice());
            vo.setTotalSpend(transDetail.getTotalSpendPrice());
            vo.setTax(transDetail.getTax());
            vo.setTotalRetrieve(transDetail.getTotalRetrievePrice());
            vo.setRemain(transDetail.getRemain());
            vo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
            vo.setTotalLaterSpend(transDetail.getTotalLaterSpend());
            //vo.setLaterRemain(transDetail.getLaterRemain());
        }

        return vo;
    }

	@Override
    public Integer deleteRecordAfterDate(String startDate) {
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IPfpTransDetailDAO) dao).deleteRecordAfterDate(date);
	}

	@Override
    public Integer deleteRecordAfterDateNotFeedback(String startDate) {
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IPfpTransDetailDAO) dao).deleteRecordAfterDateNotFeedback(date);
	}

	@Override
    public Integer deleteRecordAfterDateForFeedback(String startDate) {
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IPfpTransDetailDAO) dao).deleteRecordAfterDateForFeedback(date);
	}

	@Override
    public float findTransDetailSpendCost(String customerInfoId, Date date){

		List<PfpTransDetail> transDetails = ((IPfpTransDetailDAO) dao).findTransDetailSpendCost(customerInfoId, date);

		float cost = 0;

		if(transDetails.size() > 0){
			cost = transDetails.get(0).getTransPrice();
		}

		return cost;
	}

	@Override
    public float findTransDetailInvalidCost(String customerInfoId, Date date){

		List<PfpTransDetail> transDetails = ((IPfpTransDetailDAO) dao).findTransDetailInvalidCost(customerInfoId, date);

		float cost = 0;

		if(transDetails.size() > 0){
			cost = transDetails.get(0).getTransPrice();
		}

		return cost;
	}

    @Override
    public float selectActivatePriceSumByActivateDate(EnumTransType enumTransType, String activateDate) {
        return ((IPfpTransDetailDAO) dao).selectActivatePriceSumByActivateDate(enumTransType, activateDate);
    }


    @Override
    public float selectActivatePriceSumByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String activateDate) {
        return ((IPfpTransDetailDAO) dao).selectActivatePriceSumByActivateDate(enumPfdAccountPayType, enumTransType, activateDate);
    }


    @Override
    public int selectCustomerInfoCountByTransDate(EnumTransType enumTransType, String transDate) {
        return ((IPfpTransDetailDAO) dao).selectCustomerInfoCountByTransDate(enumTransType, transDate);
    }

    @Override
    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
        return ((IPfpTransDetailDAO) dao).selectCustomerInfoCountByTransDate(enumPfdAccountPayType, enumTransType, transDate);
    }

//    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
//        return ((IPfpTransDetailDAO) dao).selectCustomerInfoCountByTransDate(enumPfdAccountPayType, enumTransType, transDate);
//    }

    @Override
    public float selectTransPriceSumByTransDate(EnumTransType enumTransType, String transDate) {
        return ((IPfpTransDetailDAO) dao).selectTransPriceSumByTransDate(enumTransType, transDate);
    }


    @Override
    public float selectTransPriceSumByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate) {
        return ((IPfpTransDetailDAO) dao).selectTransPriceSumByTransDate(enumPfdAccountPayType, enumTransType, transDate);
    }


    @Override
    public float selectRemainPriceSumByTransDate(String transDate) {
        return ((IPfpTransDetailDAO) dao).selectRemainPriceSumByTransDate(transDate);
    }

    @Override
    public PfpTransDetail findTransDetail(String customerInfoId, Date date, EnumTransType enumTransType){

    	List<PfpTransDetail> details = ((IPfpTransDetailDAO) dao).findTransDetail(customerInfoId, date, enumTransType.getTypeId());

    	if(details.size() > 0){
    		return details.get(0);
    	}else{
    		return null;
    	}

    }

//    public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String startDate, String endDate) {
//
//    	float adClkPrice = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			EnumTransType.SPEND_COST.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float adInvalidClkPrice = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			EnumTransType.INVALID_COST.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float gift = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			EnumTransType.GIFT.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float refund = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			EnumTransType.REFUND.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float feedbackMoney = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			EnumTransType.FEEDBACK_MONEY.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	return adClkPrice - adInvalidClkPrice - gift - refund - feedbackMoney;
//    }
//
//    public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate) {
//
//    	float adClkPrice = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			pfpCustomerInfoId,
//    			EnumTransType.SPEND_COST.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float adInvalidClkPrice = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			pfpCustomerInfoId,
//    			EnumTransType.INVALID_COST.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float gift = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			pfpCustomerInfoId,
//    			EnumTransType.GIFT.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float refund = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			pfpCustomerInfoId,
//    			EnumTransType.REFUND.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	float feedbackMoney = ((IPfpTransDetailDAO) dao).findPfdTransDetail(pfdCustomerInfoId,
//    			pfpCustomerInfoId,
//    			EnumTransType.FEEDBACK_MONEY.getTypeId(), DateValueUtil.getInstance().stringToDate(startDate), DateValueUtil.getInstance().stringToDate(endDate));
//
//    	return adClkPrice - adInvalidClkPrice - gift - refund - feedbackMoney;
//    }
}
