package com.pchome.akbadm.factory.settlement;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.factory.EnumSettlementItem;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.soft.util.DateValueUtil;

/**
 * 結算流程
 * 1. 刪掉記錄
 * 2. 取最後一筆交易明細
 * 3. 帳戶判斷 - 測試用
 * 4. 逐天結算到昨天
 */
public class SettlementProcess {

	protected Log log = LogFactory.getLog(this.getClass());

    private IBoardProvider boardProvider;
    private IPfdBoardService pfdBoardService;
    private IPfpCustomerInfoService customerInfoService;
	private IPfpTransDetailService transDetailService;
	private IAdmTransLossService transLossService;
	private SettlementFactory settlementFactory;

    @Deprecated
    public void startProcess(List<PfpCustomerInfo> customerInfos, String startDate) {
        String customerInfoId = null;
        SettlementVO vo = null;
        String afterStartDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);

        if(StringUtils.isBlank(startDate)){
            startDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
        }

        // 刪掉記錄
        this.deleteRecord(startDate);

        for (PfpCustomerInfo customerInfo : customerInfos) {
            customerInfoId = customerInfo.getCustomerInfoId();

            // 取最後一筆交易明細
            vo = transDetailService.findLatestTransDetail(customerInfoId);

            // 結算到昨天
            String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
            //endDate = "2014-11-10";

            long diffyDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);

            Date transDate = null;
            int countDay = 0;

            // 逐天處理
            while(diffyDay > 0){
                transDate = DateValueUtil.getInstance().getDateForStartDateAddDay(startDate, countDay);

                // 進行結算
                vo = this.settlementItem(customerInfo, vo, transDate);

                diffyDay--;
                countDay++;
            }

            //最後要寫入當天生效的P幣退款、禮金及回饋金
            Date newTransDate = DateValueUtil.getInstance().getDateForStartDateAddDay(afterStartDate, 0);
            vo = this.settlementItemNow(customerInfo, vo, newTransDate);
        }
    }

    public void startProcess2(List<PfpCustomerInfo> customerInfos, String startDate) {
        String customerInfoId = null;
        SettlementVO vo = null;

        String afterStartDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
        if(StringUtils.isBlank(startDate)){
            startDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
        }

        // 刪掉記錄
        this.deleteRecord(startDate);

        // 結算到昨天
        String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
        long diffyDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);

        for (PfpCustomerInfo customerInfo: customerInfos) {
            customerInfoId = customerInfo.getCustomerInfoId();

            // 取最後一筆交易明細
            vo = transDetailService.findLatestTransDetail2(customerInfoId);

            Date transDate = null;

            // 逐天處理
            for (int i = 0; i < diffyDay; i++) {
                transDate = DateValueUtil.getInstance().getDateForStartDateAddDay(startDate, i);

                // 進行結算
                for (EnumSettlementItem enumSettlementItem: EnumSettlementItem.values()) {
                    vo = settlementFactory.get(enumSettlementItem).startProcess2(customerInfo, vo, transDate);
                }
            }

            //最後要寫入當天生效的禮金及回饋金
            Date newTransDate = DateValueUtil.getInstance().getDateForStartDateAddDay(afterStartDate, 0);
            vo = settlementFactory.get(EnumSettlementItem.GIFT_SNO_MONEY).startProcess2(customerInfo, vo, newTransDate);
            vo = settlementFactory.get(EnumSettlementItem.FEEDBACK_MONEY).startProcess2(customerInfo, vo, newTransDate);

            // 刪除餘額不足公告
            if(customerInfo.getRemain() > 0){
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
            customerInfoService.saveOrUpdate(customerInfo);
        }
    }

	/**
	 * 1. 刪掉交易明細
	 * 2. 刪掉超播記錄
	 */
	private void deleteRecord(String startDate){
		transDetailService.deleteRecordAfterDate(startDate);
		transLossService.deleteRecordAfterDate(startDate);
	}

	/**
	 * 結算每個項目
	 */
    @Deprecated
	private SettlementVO settlementItem(PfpCustomerInfo customerInfo, SettlementVO vo, Date transDate){
		ASettlement settlementItem = null;

		for (EnumSettlementItem enumSettlementItem : EnumSettlementItem.values()) {

			settlementItem = settlementFactory.get(enumSettlementItem);

			// 結算每個項目
			//if(this.isTest(customerInfo.getCustomerInfoId())){

//				log.info("========== "+enumSettlementItem.toString()+" start ==========");

				vo = settlementItem.startProcess(customerInfo, vo, transDate);

//				log.info("========== "+enumSettlementItem.toString()+" end ==========");
			//}

		}

		return vo;
	}

    /**
     * 	補當天退款及加值記綠
     */
    private SettlementVO settlementItemNow(PfpCustomerInfo customerInfo, SettlementVO vo, Date transDate){
    	ASettlement settlementItem = null;
    	List<EnumSettlementItem> enumSettlementItemList = new ArrayList<EnumSettlementItem>();
    	enumSettlementItemList.add(EnumSettlementItem.LATER_REFUND);		//後付P幣退款
    	enumSettlementItemList.add(EnumSettlementItem.FEEDBACK_MONEY);		//回饋金
    	enumSettlementItemList.add(EnumSettlementItem.GIFT_SNO_MONEY);		//廣告金(禮金)
    	enumSettlementItemList.add(EnumSettlementItem.SAVE_MONEY);			//預付儲值
    	enumSettlementItemList.add(EnumSettlementItem.LATER_SAVE);			//後付P幣儲值
    	
    	for (EnumSettlementItem enumSettlementItem : enumSettlementItemList) {
    		settlementItem = settlementFactory.get(enumSettlementItem);
    		vo = settlementItem.startProcess(customerInfo, vo, transDate);
    	}
    	
    	return vo;
    }
    
//	/**
//	 * 測試用
//	 * 1. 自行加帳戶判斷
//	 * 2. 測試完註解掉 false 打開 true
//	 */
//	private boolean isTest(String customerInfoId){
//
//		boolean isTest = false;
//		//boolean isTest = true;
//
//		// 測試帳戶用
//		if(!isTest){
//
//			if(customerInfoId.equals("AC2014032000001")){
//
//				log.info(" customerInfoId: "+customerInfoId);
//
//				isTest = true;
//			}
//		}
//
//		return isTest;
//	}

    public void setBoardProvider(IBoardProvider boardProvider) {
        this.boardProvider = boardProvider;
    }

    public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
        this.customerInfoService = customerInfoService;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		this.transDetailService = transDetailService;
	}

	public void setTransLossService(IAdmTransLossService transLossService) {
		this.transLossService = transLossService;
	}

	public void setSettlementFactory(SettlementFactory settlementFactory) {
		this.settlementFactory = settlementFactory;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}
	
}
