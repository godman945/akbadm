package com.pchome.akbadm.factory.settlement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.retrieve.IAdmRetrieveRecordService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;

/**
 * 廣告金&回饋金到期回收計算
 * 1. 取回饋金資料
 * 2. 更新總加值金額
 * 3. 更新帳戶餘額
 */
public class SettlementRetrieve extends ASettlement{

	private IAdmRetrieveRecordService admRetrieveRecordService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String customerInfoId = vo.getCustomerInfoId();

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("pfpCustomerInfoId", customerInfoId);
		conditionMap.put("recordDate", sdf.format(transDate));

		List<AdmRetrieveRecord> records = null;

		try {

			records = admRetrieveRecordService.findRetrieveRecord(conditionMap);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
//		log.info(">>> records.size() = " + records.size());

		if(!records.isEmpty()){

			float retrieveMoney = 0;

			for(AdmRetrieveRecord record:records){
				retrieveMoney += record.getRetrieveMoney();
			}

			vo.setTransPrice(retrieveMoney);

			// 更新帳戶餘額
			vo.setRemain(vo.getRemain() - retrieveMoney);
		}
	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {

		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.GIFT_FEEDBACK_RETRIEVE.getChName());
			transDetail.setTransType(EnumTransType.GIFT_FEEDBACK_RETRIEVE.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.GIFT_FEEDBACK_RETRIEVE.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}

	public void setAdmRetrieveRecordService(IAdmRetrieveRecordService admRetrieveRecordService) {
		this.admRetrieveRecordService = admRetrieveRecordService;
	}
}
