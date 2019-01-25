package com.pchome.akbadm.factory.settlement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfdVirtualRecord;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfdVirtualRecordService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;

/**
 * 後付加值計算

 */
public class SettlementLaterSave extends ASettlement{

	private IPfdVirtualRecordService pfdVirtualRecordService;
	private IBoardProvider boardProvider;
	private IPfdBoardService pfdBoardService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();

		List<PfdVirtualRecord> virtualRecord = pfdVirtualRecordService.findPfdVirtualRecord(customerInfoId, transDate);

//		log.info(">>> virtualRecord: "+virtualRecord);

		if(!virtualRecord.isEmpty()){

			float saveMoney = 0;
			float totalLaterSave = 0;
			float remain = 0;

			for(PfdVirtualRecord record:virtualRecord){
				// 當日後付加值金額
				saveMoney += record.getAddMoney();
			}

			vo.setTransPrice(saveMoney);

			// 更新總加值金額
			totalLaterSave = vo.getTotalLaterAddMoney() + saveMoney;
			// 更新帳戶餘額
			remain = vo.getRemain() + saveMoney;

			vo.setTotalLaterAddMoney(totalLaterSave);
			vo.setRemain(remain);
		}

	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {

		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.LATER_SAVE.getChName());
			transDetail.setTransType(EnumTransType.LATER_SAVE.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.LATER_SAVE.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTotalLaterSave(vo.getTotalLaterAddMoney());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}

	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);

		if(customerInfo.getRemain() > 0){
			// 刪除餘額不足公告
			boardProvider.delete(customerInfo.getCustomerInfoId(), EnumCategory.REMAIN_NOT_ENOUGH);
			
			Set<PfdUserAdAccountRef> set = customerInfo.getPfdUserAdAccountRefs();
			List<PfdUserAdAccountRef> list = new ArrayList<PfdUserAdAccountRef>(set);
			
			//查看是否有經銷商，有的話也要刪除經銷商公告
			try {
				if(list.size()>0){
					pfdBoardService.deletePfdBoardByDeleteId(customerInfo.getCustomerInfoId());
				}
			} catch (Exception e) {
				log.error(customerInfo.getCustomerInfoId(), e);
			}
		}
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}

	public void setPfdVirtualRecordService(IPfdVirtualRecordService pfdVirtualRecordService) {
		this.pfdVirtualRecordService = pfdVirtualRecordService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	
}
