package com.pchome.akbadm.factory.settlement;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

/**
 * 無效點擊費用計算
 * 1. 取預付無效點擊費用
 * 2. 更新預付總無效點擊費用
 * 3. 更新預付帳戶餘額
 */
public class SettlementInvalidCost extends ASettlement{

	private IPfpAdActionReportService adActionReportService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();

		float adInvalidClkPrice = adActionReportService.findAdInvalidClkPrice(customerInfoId, transDate, transDate, EnumPfdAccountPayType.ADVANCE.getPayType());

//		log.info(">>> adInvalidClkPrice: "+adInvalidClkPrice);

		if(adInvalidClkPrice > 0){

			vo.setTransPrice(adInvalidClkPrice);

			// 更新預付總無效點擊費用
			float totalRetrievePrice = vo.getTotalRetrieve() + adInvalidClkPrice;
			// 更新預付帳戶餘額
			float remain = vo.getRemain() + adInvalidClkPrice;

			vo.setTotalRetrieve(totalRetrievePrice);
			vo.setRemain(remain);
		}
	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {

		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.INVALID_COST.getChName());
			transDetail.setTransType(EnumTransType.INVALID_COST.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.INVALID_COST.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());

			transDetail.setTotalRetrievePrice(vo.getTotalRetrieve());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalRetrieve(transDetail.getTotalRetrievePrice());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalRetrieve(transDetail.getTotalRetrievePrice());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}

	public void setAdActionReportService(
			IPfpAdActionReportService adActionReportService) {
		this.adActionReportService = adActionReportService;
	}


//	private IPfpAdInvalidService adInvalidService;
////	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
//
//	@Override
//	public PfpTransDetail getTransDetail(PfpCustomerInfo customerInfo, Date transDate, SettlementVO vo) throws Exception {
//
//		String customerInfoId = customerInfo.getCustomerInfoId();
//
//		List<Object> objects = adInvalidService.accountInvalidSum(customerInfoId, transDate);
//
////		List<PfdUserAdAccountRef> pfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(customerInfoId);
////
////		String pfdCustomerInfoId = null;
////		if (!pfdUserAdAccountRefList.isEmpty()) {
////			//正常而言，一個 pfp 帳戶只能對應到一個 pfd 帳戶
////			if (pfdUserAdAccountRefList.size()>1) {
////				throw new Exception(">>> PfdUserAdAccountRef too much error");
////			}
////			pfdCustomerInfoId = pfdUserAdAccountRefList.get(0).getPfdCustomerInfo().getCustomerInfoId();
////		}
//
//		PfpTransDetail transDetail = null;
//
//		if(objects.get(0) != null){
//
//			// 惡意點擊費用
//			float invalidCost = Float.parseFloat(objects.get(0).toString());
//
//			if(invalidCost > 0){
//
//				transDetail = new PfpTransDetail();
//
//				transDetail.setPfpCustomerInfo(customerInfo);
////				if (pfdCustomerInfoId!=null) {
////					transDetail.setPfdCustomerInfoId(pfdCustomerInfoId);
////				}
//				transDetail.setTransDate(transDate);
//				transDetail.setTransContent(EnumTransType.INVALID_COST.getChName());
//				transDetail.setTransType(EnumTransType.INVALID_COST.getTypeId());
//				transDetail.setIncomeExpense("+");
//				transDetail.setTransPrice(invalidCost);
//				transDetail.setTax(0);
//				transDetail.setRemain(0);
//
//				Date today = new Date();
//				transDetail.setUpdateDate(today);
//				transDetail.setCreateDate(today);
//			}
//
//		}
//
//		return transDetail;
//	}
//
//	@Override
//	public void countRemainWithTotalCost(SettlementVO vo, PfpTransDetail transDetail) {
//		// TODO Auto-generated method stub
//		float totalInvalidCost = vo.getLastTotalInvalidCost() + transDetail.getTransPrice();
//		float remain = vo.getLastRemain() + transDetail.getTransPrice();
//
//		transDetail.setTotalSavePrice(vo.getLastTotalSaveMoney());
//		transDetail.setTotalSpendPrice(vo.getLastTotalSpendCost());
//		transDetail.setTotalRetrievePrice(totalInvalidCost);
//		transDetail.setRemain(remain);
//
//		vo.setLastTotalInvalidCost(totalInvalidCost);
//		vo.setLastRemain(remain);
//	}
//
//	@Override
//	public void countAccountRemain(PfpCustomerInfo customerInfo, Date transDate) {
//		// TODO Auto-generated method stub
//
//	}
//
//
////	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
////		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
////	}
}
