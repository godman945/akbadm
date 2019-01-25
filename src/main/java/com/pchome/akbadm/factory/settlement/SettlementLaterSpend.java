package com.pchome.akbadm.factory.settlement;

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
 * 1. 計算後付廣告費用
 * 2. 更新後付總廣告費用
 * 3. 更新後付帳戶餘額
 * 4. 更新交易損失
 */
public class SettlementLaterSpend extends ASettlement {

	private IPfpAdActionReportService adActionReportService;
	private IAdmTransLossService transLossService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();
		// 取出廣告費用要包含惡意點擊費用
		float adClkPrice = adActionReportService.findAdClkAndInvalidClkPrice(customerInfoId, transDate, transDate, EnumPfdAccountPayType.LATER.getPayType());

//		log.info(">>> adClkPrice: "+adClkPrice);

		if(adClkPrice > 0){
			
			System.out.println(customerInfoId +" "+adClkPrice);
			adClkPrice = (float)Math.floor(adClkPrice);
			vo.setTransPrice(adClkPrice);

			// 更新後付總廣告費用
			float totalLaterSpend = vo.getTotalLaterSpend() + adClkPrice;
			// 更新後付帳戶餘額
			float remain = vo.getRemain() - adClkPrice;

			vo.setTotalLaterSpend(totalLaterSpend);
			vo.setRemain(remain);

			// 餘額不可以小於 0
			if(remain < 0 ){
				// 交易損失
				this.createTransLoss(customerInfoId, remain, transDate);
				vo.setRemain(0);
				vo.setTransPrice((adClkPrice+remain));
				
				// 平衡帳戶
				totalLaterSpend += remain;
				vo.setTotalLaterSpend(totalLaterSpend);
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

			transDetail.setTotalLaterSpend(vo.getTotalLaterSpend());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalLaterSpend(transDetail.getTotalLaterSpend());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalLaterSpend(transDetail.getTotalLaterSpend());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	/**
	 * 交易損失
	 */
	private void createTransLoss(String customerInfoId, float lossCost, Date transDate){

		log.info(">>> lossCost: "+lossCost);

		// 播放廣告損失
		AdmTransLoss transLoss = new AdmTransLoss();
		transLoss.setCustomerInfoId(customerInfoId);
		transLoss.setLossCost(Math.abs(lossCost));
		transLoss.setTransDate(transDate);
		transLoss.setPayType(EnumPfdAccountPayType.LATER.getPayType());

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
}
