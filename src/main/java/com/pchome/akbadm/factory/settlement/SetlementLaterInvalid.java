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
 * 1. 取後付無效點擊費用
 * 2. 更新後付總無效點擊費用
 * 3. 更新後付帳戶餘額
 */
public class SetlementLaterInvalid extends ASettlement{

private IPfpAdActionReportService adActionReportService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();

		float adInvalidClkPrice = adActionReportService.findAdInvalidClkPrice(customerInfoId, transDate, transDate, EnumPfdAccountPayType.LATER.getPayType());

//		log.info(">>> adInvalidClkPrice: "+adInvalidClkPrice);

		if(adInvalidClkPrice > 0){

			vo.setTransPrice(adInvalidClkPrice);

			// 更新後付總無效點擊費用
			float totalLaterRetrieve = vo.getTotalLaterRetrieve() + adInvalidClkPrice;
			// 更新帳戶餘額
			float remain = vo.getRemain() + adInvalidClkPrice;

			vo.setTotalLaterRetrieve(totalLaterRetrieve);
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

			transDetail.setTotalLaterRetrieve(vo.getTotalLaterRetrieve());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalLaterRetrieve(transDetail.getTotalLaterRetrieve());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalLaterRetrieve(transDetail.getTotalLaterRetrieve());
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
}
