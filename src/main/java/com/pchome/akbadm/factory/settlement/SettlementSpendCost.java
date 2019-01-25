package com.pchome.akbadm.factory.settlement;

import java.math.BigDecimal;
import java.util.Date;

import com.pchome.akbadm.db.pojo.AdmTransLoss;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

/**
 * 廣告花費計算
 * 1. 計算預付廣告費用
 * 2. 更新預付總廣告費用
 * 3. 更新預付帳戶餘額
 * 4. 更新交易損失
 */
public class SettlementSpendCost extends ASettlement{

	private IPfpAdActionReportService adActionReportService;
	private IAdmTransLossService transLossService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();
		// 取出廣告費用要包含惡意點擊費用
		float adClkPrice = adActionReportService.findAdClkAndInvalidClkPrice(customerInfoId, transDate, transDate, EnumPfdAccountPayType.ADVANCE.getPayType());

//		log.info(">>> adClkPrice: "+adClkPrice);
		
		if(adClkPrice > 0){
						
			adClkPrice = (float)Math.floor(adClkPrice);
			
			vo.setTransPrice(adClkPrice);
			// 更新總預付廣告費用
			float totalSpendPrice = vo.getTotalSpend() + adClkPrice;
			// 更新帳戶餘額
			float remain = vo.getRemain() - adClkPrice;

			vo.setTotalSpend(totalSpendPrice);
			vo.setRemain(remain);

			// 餘額不可以小於 0
			if(remain < 0 ){
				// 交易損失
				this.createTransLoss(customerInfoId, remain, transDate);
				vo.setRemain(0);
				vo.setTransPrice((adClkPrice+remain));
				// 平衡帳戶
				totalSpendPrice += remain;
				vo.setTotalSpend(totalSpendPrice);
			}

		}

	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {
		if(vo.getTransPrice() > 0){
			transDetail.setTransContent(EnumTransType.SPEND_COST.getChName());
			transDetail.setTransType(EnumTransType.SPEND_COST.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.SPEND_COST.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTotalSpendPrice(vo.getTotalSpend());
			transDetail.setRemain(vo.getRemain());
			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalSpend(transDetail.getTotalSpendPrice());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalSpend(transDetail.getTotalSpendPrice());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	/**
	 * 交易損失
	 */
	private void createTransLoss(String customerInfoId, float lossCost, Date transDate){

//		log.info(">>> lossCost: "+lossCost);

		// 播放廣告損失
		AdmTransLoss transLoss = new AdmTransLoss();
		transLoss.setCustomerInfoId(customerInfoId);
		transLoss.setLossCost(Math.abs(lossCost));
		transLoss.setTransDate(transDate);
		transLoss.setPayType(EnumPfdAccountPayType.ADVANCE.getPayType());

		Date today = new Date();
		transLoss.setUpdateDate(today);
		transLoss.setCreateDate(today);
		transLossService.saveOrUpdate(transLoss);
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

	public void setTransLossService(IAdmTransLossService transLossService) {
		this.transLossService = transLossService;
	}


//	private IPfpAdPvclkService adPvclkService;
//	private IAdmTransLossService transLossService;
////	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
//
//	@Override
//	public PfpTransDetail getTransDetail(PfpCustomerInfo customerInfo, Date transDate, SettlementVO vo) throws Exception {
//
//		String customerInfoId = customerInfo.getCustomerInfoId();
//
//		List<Object> objects = adPvclkService.accountPvclkSum(customerInfoId, transDate);
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
//		for(Object object:objects){
//
//			Object[] ob = (Object[])object;
//
//			if(ob[2] != null){
//
//				// 當日花費
//				float spendCost = Float.parseFloat(ob[2].toString());
//
//				if(spendCost > 0){
//
//					transDetail = new PfpTransDetail();
//
//					transDetail.setPfpCustomerInfo(customerInfo);
////					if (pfdCustomerInfoId!=null) {
////						transDetail.setPfdCustomerInfoId(pfdCustomerInfoId);
////					}
//					transDetail.setTransDate(transDate);
//					transDetail.setTransContent(EnumTransType.SPEND_COST.getChName());
//					transDetail.setTransType(EnumTransType.SPEND_COST.getTypeId());
//					transDetail.setIncomeExpense("-");
//					transDetail.setTransPrice(spendCost);
//					transDetail.setTax(0);
//					transDetail.setRemain(0);
//
//					Date today = new Date();
//					transDetail.setUpdateDate(today);
//					transDetail.setCreateDate(today);
//				}
//			}
//		}
//
//		return transDetail;
//	}
//
//	@Override
//	public void countRemainWithTotalCost(SettlementVO vo, PfpTransDetail transDetail) {
//		// TODO Auto-generated method stub
//
//		float remain = 0;
//		float totalSpendPrice = 0;
//
//		if(vo.getLastRemain() >= transDetail.getTransPrice()){
//			remain = vo.getLastRemain() - transDetail.getTransPrice();
//			totalSpendPrice = vo.getLastTotalSpendCost() + transDetail.getTransPrice();
//		}
//		else{
//
//			float lossCost = transDetail.getTransPrice() - vo.getLastRemain();
//
//			// 播放廣告損失
//			AdmTransLoss transLoss = new AdmTransLoss();
//			transLoss.setCustomerInfoId(transDetail.getPfpCustomerInfo().getCustomerInfoId());
//			transLoss.setLossCost(lossCost);
//			transLoss.setTransDate(transDetail.getTransDate());
//
//			Date today = new Date();
//			transLoss.setUpdateDate(today);
//			transLoss.setCreateDate(today);
//			transLossService.saveOrUpdate(transLoss);
//
//			totalSpendPrice = vo.getLastTotalSpendCost() + vo.getLastRemain();
//
//			transDetail.setTransPrice(vo.getLastRemain());
//		}
//
//		transDetail.setTotalSavePrice(vo.getLastTotalSaveMoney());
//		transDetail.setTotalSpendPrice(totalSpendPrice);
//		transDetail.setTotalRetrievePrice(vo.getLastTotalInvalidCost());
//		transDetail.setRemain(remain);
//
//		vo.setLastTotalSpendCost(totalSpendPrice);
//		vo.setLastRemain(remain);
//
//	}
//
//	@Override
//	public void countAccountRemain(PfpCustomerInfo customerInfo, Date transDate) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void setTransDetailService(IPfpTransDetailService transDetailService) {
//		this.transDetailService = transDetailService;
//	}
//
//	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
//		this.customerInfoService = customerInfoService;
//	}
//
//	public void setAdPvclkService(IPfpAdPvclkService adPvclkService) {
//		this.adPvclkService = adPvclkService;
//	}
//
//	public void setTransLossService(IAdmTransLossService transLossService) {
//		this.transLossService = transLossService;
//	}
//
////	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
////		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
////	}
}
