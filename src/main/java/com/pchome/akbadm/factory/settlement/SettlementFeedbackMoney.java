package com.pchome.akbadm.factory.settlement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.feedback.IAdmFeedbackRecordService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;

/**
 * 回饋金計算
 * 1. 取回饋金資料
 * 2. 更新總加值金額
 * 3. 更新帳戶餘額
 * 4. 刪除餘額不足公告
 */
public class SettlementFeedbackMoney extends ASettlement{

	private IAdmFeedbackRecordService feedbackRecordService;
	private IBoardProvider boardProvider;
	private IPfdBoardService pfdBoardService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();

		List<AdmFeedbackRecord> records = feedbackRecordService.findFeedbackRecord(customerInfoId, transDate);

//		log.info(">>> feedbackRecord: "+records);

		if(!records.isEmpty()){

			float feedbackMoney = 0;
			float totalSavePrice = 0;
			float remain = 0;

			for(AdmFeedbackRecord record:records){

				feedbackMoney += record.getFeedbackMoney();

			}

			//log.info(" feedbackMoney: "+feedbackMoney);

			vo.setTransPrice(feedbackMoney);

			// 更新總加值金額
			totalSavePrice = vo.getTotalAddMoney() + feedbackMoney;
			// 更新帳戶餘額
			remain = vo.getRemain() + feedbackMoney;

			vo.setTotalAddMoney(totalSavePrice);
			vo.setRemain(remain);
		}
	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {

		if(vo.getTransPrice() > 0){

			Date transDate = transDetail.getTransDate();

		    transDetail.setTransDate(transDate);

			transDetail.setTransContent(EnumTransType.FEEDBACK_MONEY.getChName());
			transDetail.setTransType(EnumTransType.FEEDBACK_MONEY.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.FEEDBACK_MONEY.getOperator());
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

	public void setFeedbackRecordService(IAdmFeedbackRecordService feedbackRecordService) {
		this.feedbackRecordService = feedbackRecordService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

}
