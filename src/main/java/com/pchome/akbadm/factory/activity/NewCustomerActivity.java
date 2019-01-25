package com.pchome.akbadm.factory.activity;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.free.IAdmFreeActionService;
import com.pchome.akbadm.db.service.free.IAdmFreeRecordService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.enumerate.factory.EnumActivityType;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.soft.util.DateValueUtil;

public class NewCustomerActivity implements IActivity{
	
	protected Log log = LogFactory.getLog(this.getClass());
	

	private final static String saveEndDate = "2014-02-28";
	private final static String spendEndDate = "2014-03-31";
	
	private IPfpCustomerInfoService customerInfoService;
	private IAdmFreeRecordService freeRecordService; 
	private IAdmFreeActionService freeActionService;
	private IAdmRecognizeRecordService recognizeRecordService;
	private ISequenceService sequenceService;
	private IBoardProvider boardProvider;
	
	
	@Override
	public AdmFreeAction activityCondition(List<Object> conditions) {
		// TODO Auto-generated method stub
		AdmFreeAction freeAction = null; 
		
		float totalSpendCost = 0;
		String customerInfoId = null;
		Date transDate = null;
		PfpCustomerInfo customerInfo = null;
		
		if(!conditions.isEmpty()){
			totalSpendCost = Float.parseFloat(conditions.get(0).toString());
			customerInfoId = conditions.get(1).toString();
			transDate = (Date) conditions.get(2);
			List<PfpCustomerInfo> customerInfos = customerInfoService.findValidCustomerInfo(customerInfoId);
			
			if(!customerInfos.isEmpty()){
				customerInfo = customerInfos.get(0);
			}
		}
		
		if(customerInfo != null){

			String activateDate = DateValueUtil.getInstance().dateToString(customerInfo.getActivateDate());
			String today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);;
			long saveDiffDay = DateValueUtil.getInstance().getDateDiffDay(activateDate, saveEndDate);
			long spendDiffDay = DateValueUtil.getInstance().getDateDiffDay(today, spendEndDate);
			
			log.info(" customerInfoId = "+customerInfoId);
			log.info(" totalSpendCost = "+totalSpendCost);			
			log.info(" customerInfo activateDate = "+activateDate);
			log.info(" saveEndDate = "+saveEndDate);
			log.info(" spendEndDate = "+spendEndDate);
			log.info(" saveDiffDay = "+saveDiffDay);
			log.info(" today = "+today);
			log.info(" spendDiffDay = "+spendDiffDay);
			
			// 2013-12-31 開通帳號，並於2014-03-31  前花費超過500才送
			if(saveDiffDay >= 0 && spendDiffDay >= 0 && totalSpendCost >= EnumActivityType.NEW_CUSTOMER.getCondition()){
				
				if(!this.isRecord(customerInfoId)){
					
					freeAction = freeActionService.findFreeAction(EnumActivityType.NEW_CUSTOMER);
					
					this.addActionRecord(customerInfoId, freeAction, transDate);
					
					this.addRecognizeRecord(customerInfoId, freeAction, transDate);
					
					this.addBoard(customerInfoId);
				}
				
			}
			
		}
		
		return freeAction;
	}
	
	public boolean isRecord(String customerInfoId) {
		// 贈送記錄
		List<AdmFreeRecord> records = freeRecordService.findRecords(customerInfoId, EnumActivityType.NEW_CUSTOMER);

		if(records.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public void addActionRecord(String customerInfoId, AdmFreeAction freeAction, Date recordDate) {
		// TODO Auto-generated method stub
		freeRecordService.createFreeRecord(freeAction, customerInfoId, recordDate);
	}

	@Override
	public void addRecognizeRecord(String customerInfoId, AdmFreeAction freeAction, Date recordDate) {
		// TODO Auto-generated method stub
		// 贈送廣告金需記錄在攤提表 
		List<AdmRecognizeRecord> records = recognizeRecordService.findRecognizeRecords(customerInfoId, freeAction.getActionId(), EnumOrderType.GIFT);
		
		// 贈送過不再記錄
		if(records.size() <= 0){
			
			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_RECOGNIZE_RECORD);

			recognizeRecordService.createAdmRecognizeRecord(recordId,
																customerInfoId,
																freeAction.getGiftMoney(), 
																0,
																EnumOrderType.GIFT,
																freeAction.getActionId(), 
																recordDate,
																null);
		}
	}

	@Override
	public void addBoard(String customerInfoId) {
		// TODO Auto-generated method stub
		// 公告通知
		boardProvider.delete(customerInfoId, EnumCategory.MONEY_ENTERED);
		boardProvider.delete(customerInfoId, EnumCategory.REMAIN_NOT_ENOUGH);
		
		boardProvider.add(customerInfoId, 
							EnumBoardType.ACTION, 
							EnumCategory.MONEY_ENTERED);		
	}

	
	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}

	public void setFreeRecordService(IAdmFreeRecordService freeRecordService) {
		this.freeRecordService = freeRecordService;
	}

	public void setFreeActionService(IAdmFreeActionService freeActionService) {
		this.freeActionService = freeActionService;
	}

	public void setRecognizeRecordService(IAdmRecognizeRecordService recognizeRecordService) {
		this.recognizeRecordService = recognizeRecordService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

}
