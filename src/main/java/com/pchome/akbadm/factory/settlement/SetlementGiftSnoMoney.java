package com.pchome.akbadm.factory.settlement;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.free.IAdmFreeRecordService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;

/**
 * 填序號廣告金(禮金)
 * 1. 取贈送廣告金總額
 * 2. 更新帳戶餘額
 */
public class SetlementGiftSnoMoney extends ASettlement{
	private IAdmFreeRecordService admFreeRecordService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {
		String customerInfoId = vo.getCustomerInfoId();
		// 查看廣告金贈送記錄
		List<AdmFreeRecord> admFreeRecords = admFreeRecordService.findFreeActionRecord(customerInfoId, transDate);

//		log.info(">>> admFreeRecords: "+admFreeRecords);

		if(!admFreeRecords.isEmpty()){

			float giftMoney = 0;
			float totalSavePrice = 0;
			float remain = 0;

			for(AdmFreeRecord record:admFreeRecords){
				giftMoney += record.getAdmFreeAction().getGiftMoney();
			}

			vo.setTransPrice(giftMoney);

			// 更新總加值金額
			totalSavePrice = vo.getTotalAddMoney() + giftMoney;
			// 更新帳戶餘額
			remain = vo.getRemain() + giftMoney;

			vo.setTotalAddMoney(totalSavePrice);
			vo.setRemain(remain);
		}
	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {
		if(vo.getTransPrice() > 0){
			transDetail.setTransContent(EnumTransType.GIFT.getChName());
			transDetail.setTransType(EnumTransType.GIFT.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.GIFT.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTotalSavePrice(vo.getTotalAddMoney());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}

	public void setAdmFreeRecordService(IAdmFreeRecordService admFreeRecordService) {
		this.admFreeRecordService = admFreeRecordService;
	}

}
