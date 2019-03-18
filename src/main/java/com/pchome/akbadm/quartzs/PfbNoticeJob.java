package com.pchome.akbadm.quartzs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;

/**
 * 檢查所有狀態為[待驗證]的帳戶，已經有成效的，將狀態改為[開通]
 */
@Transactional
public class PfbNoticeJob {

	private Logger log = LogManager.getRootLogger();

	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	
	private IPfbxBoardService pfbxBoardService;

	public void process() {

		Date now = new Date();

		try {

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("status", "'" + EnumPfbAccountStatus.START.getStatus() + "'");

			List<PfbxCustomerInfo> accountList = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
			log.info(">>> accountList.size() = " + accountList.size());

			String customerInfoId = null;
			for (int i=0; i<accountList.size(); i++) {
				PfbxCustomerInfo account = accountList.get(i);
				customerInfoId = account.getCustomerInfoId();
				log.info(">>> customerInfoId = " + customerInfoId);

//				//檢查分潤數字，若大於 1000 寫公告
//				PfbxBonus pfbxBonus = pfbxBonusService.findPfbxBonus(customerInfoId);
//
//				if (pfbxBonus!=null && pfbxBonus.getApplyBonus()>1000) {
//
//					//查詢是否寫過公告了，寫過就不再寫了
//					Map<String, String> conditionMap2 = new HashMap<String, String>();
//					conditionMap2.put("pfbxCustomerInfoId", customerInfoId);
//					conditionMap2.put("deleteId", EnumBoardContent.BOARD_CONTENT_4.getId());
//
//					if (pfbxBoardService.findPfbxBoard(conditionMap2).size() == 0) {
//
//						PfbxBoard board = new PfbxBoard();
//						board.setBoardType(EnumPfbBoardType.REMIND.getType());
//						board.setBoardContent(EnumBoardContent.BOARD_CONTENT_4.getContent());
//						board.setPfbxCustomerInfoId(customerInfoId);
//						board.setStartDate(now);
//						board.setEndDate(new Date(now.getTime() + (1000 * 60 * 60 * 24 * 365 * 10)));
//						board.setHasUrl("n");
//						board.setUrlAddress(null);
//						board.setDeleteId(EnumBoardContent.BOARD_CONTENT_4.getId());
//						board.setCreateDate(now);
//
//						pfbxBoardService.save(board);
//					}
//				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

    public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
		this.pfbxBoardService = pfbxBoardService;
	}

	public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        PfbNoticeJob job = context.getBean(PfbNoticeJob.class);
        job.process();
    }
}
