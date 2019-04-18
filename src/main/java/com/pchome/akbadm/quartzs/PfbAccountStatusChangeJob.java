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
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

/**
 * 1.檢查所有狀態為[待驗證]的帳戶，已經有成效的，將狀態改為[開通]  ==> 目前改為手動驗證，所以停用
 * 2.檢查帳戶有設定關閉時間的，若超過關閉時間則將狀態變更為關閉
 */
@Transactional
public class PfbAccountStatusChangeJob {

	private Logger log = LogManager.getRootLogger();

	private IPfbxCustomerInfoService pfbxCustomerInfoService;
//	private IPfbxCustomerReportService pfbxCustomerReportService;

	public static final String MAIL_API_NO = "P159";
	private SpringEmailUtil springEmailUtil;

	public void process() {
        log.info("====PfbAccountStatusChangeJob.process() start====");

		Date now = new Date();

		try {

			Map<String, String> conditionMap = new HashMap<String, String>();

			//因改手動認證，所以此功能停用
			//Step 1 檢查所有狀態為[待驗證]的帳戶，已經有成效的，將狀態改為[開通]
			//檢查成效數字
//			conditionMap.put("status", "'" + EnumPfbAccountStatus.APPLY.getStatus() + "'");
//			List<PfbxCustomerInfo> accountList = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
//			log.info(">>> accountList.size() = " + accountList.size());
//
//			String customerInfoId = null;
//			for (int i=0; i<accountList.size(); i++) {
//				PfbxCustomerInfo account = accountList.get(i);
//				customerInfoId = account.getCustomerInfoId();
//				log.info(">>> customerInfoId = " + customerInfoId);
//
//				Map<String, String> conditionMap2 = new HashMap<String, String>();
//				conditionMap2.put("PfbxCustomerInfoId", customerInfoId);
//
//				List<PfbxCustomerReportVo> voList = pfbxCustomerReportService.getPfbxCustomerReportByCondition(new HashMap<String, String>(), conditionMap2);
//				log.info(">>> voList.size() = " + voList.size());
//
//				for (int k=0; k<voList.size(); k++) {
//					PfbxCustomerReportVo vo = voList.get(k);
//					//只要有pv就改變狀態
//					if (Integer.parseInt(vo.getAdPvSum())>0) {
//						account.setStatus(EnumPfbAccountStatus.START.getStatus());
//						account.setUpdateDate(now);
//						pfbxCustomerInfoService.saveOrUpdate(account);
//						break;
//					}
//				}
//			}

			//Step 2
			//超過關閉日期，將狀態改為關閉
			conditionMap = new HashMap<String, String>();
			conditionMap.put("status_Not", "'" + EnumPfbAccountStatus.CLOSE.getStatus() + "'");
			conditionMap.put("closeDate", DateTimeUtils.getDateStr(DateTimeUtils.getDate(),DateTimeUtils.yyyy_MM_dd));

			//取得需要關閉帳號的清單
			List<PfbxCustomerInfo> accountList = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
			log.info(">>> accountList.size() = " + accountList.size());

			String customerInfoId = null;
			for (int i=0; i<accountList.size(); i++) {
				PfbxCustomerInfo account = accountList.get(i);
				customerInfoId = account.getCustomerInfoId();

				account.setStatus(EnumPfbAccountStatus.CLOSE.getStatus());
				pfbxCustomerInfoService.saveOrUpdate(account);
				log.info(">>> customerInfoId = " + customerInfoId + " Status Change to Close !");
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

        log.info("====PfbAccountStatusChangeJob.process() end====");
	}

	//聯播網帳戶申請中e-mail通知
	public void pfbxCustomerInfoApplyNotifyProcess(){
        log.info("====PfbAccountStatusChangeJob.pfbxCustomerInfoApplyNotifyProcess() start====");

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("status", "'" + EnumPfbAccountStatus.APPLY.getStatus() + "'");
		//取得申請中帳號的清單
		try {
			List<PfbxCustomerInfo> accountList = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
			log.info(">>> accountList.size() = " + accountList.size());
			if (accountList.size()>0) {
				Mail mail = null;
				mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

	        	if (mail == null) {
	        		throw new Exception("Mail Object is null.");
	        	}

	        	mail.setMsg("<html><body>您有 " + accountList.size() + " 個申請中帳戶待審</body></html>");

	            springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

        log.info("====PfbAccountStatusChangeJob.pfbxCustomerInfoApplyNotifyProcess() end====");
	}

	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

//	public void setPfbxCustomerReportService(IPfbxCustomerReportService pfbxCustomerReportService) {
//		this.pfbxCustomerReportService = pfbxCustomerReportService;
//	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        PfbAccountStatusChangeJob job = context.getBean(PfbAccountStatusChangeJob.class);
        job.process();
        job.pfbxCustomerInfoApplyNotifyProcess();
    }

}
