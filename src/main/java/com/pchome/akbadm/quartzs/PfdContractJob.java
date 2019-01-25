package com.pchome.akbadm.quartzs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

@Transactional
public class PfdContractJob {

	private Log log = LogFactory.getLog(this.getClass());

	public static final String MAIL_API_NO = "P150";

	private IPfdContractService pfdContractService;
	private SpringEmailUtil springEmailUtil;

	public void process() throws Exception {
        log.info("====PfdContractJob.process() start====");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		//多久前提醒
		long remindTime = 1000*60*60*24*60; //預設60天

		Date now = new Date();
		log.info(">>> now time: " + sdf.format(now));

		Map<String, String> conditionsMap = new HashMap<String, String>();

		List<PfdContract> pojoList = pfdContractService.findPfdContract(conditionsMap, -1, 0);

		PfdContract pojo = null;
		String orgStatus;
		String newStatus;
		long _startDate = 0;
		long _endDate = 0;
		long _now = now.getTime();
		String continueFlag;
		String mailContent = ""; //提醒續約名單

		Calendar cal = Calendar.getInstance();
		
		for (int i=0; i<pojoList.size(); i++) {

			pojo = pojoList.get(i);

			orgStatus = pojo.getStatus();
			continueFlag = pojo.getContinueFlag();

			log.info(">>> PfdContractId = " + pojo.getPfdContractId() + ", orgStatus = " + orgStatus +
					", continueFlag = " + continueFlag);

			//改變合約狀態
			if (!pojo.getStatus().equals(EnumContractStatus.CLOSE.getStatusId())) { //作廢不處理

				_startDate = pojo.getStartDate().getTime();
				_endDate = pojo.getEndDate().getTime();
				log.info(">>> during time: " + sdf.format(pojo.getStartDate()) + " ~ " + sdf.format(pojo.getEndDate()));

				if (_now > _endDate) {

					//有選擇到期自動續約的自動延長 365 天，狀態不改變
					if (continueFlag!=null && continueFlag.equals("y")) {
						newStatus = pojo.getStatus();
						
						cal.setTime(pojo.getEndDate());
						cal.add(Calendar.YEAR, +1);
						cal.set(Calendar.HOUR_OF_DAY, 23);
						cal.set(Calendar.MINUTE, 59);
						cal.set(Calendar.SECOND, 59);
						
						pojo.setEndDate(cal.getTime());
					} else {
						newStatus = EnumContractStatus.OVERTIME.getStatusId();
					}
				} else if (_now >= _startDate && _now<=_endDate) {
					newStatus = EnumContractStatus.USE.getStatusId();
				} else {
					newStatus = EnumContractStatus.WAIT.getStatusId();
				}
				log.info(">>> newStatus = " + newStatus);

				pojo.setStatus(newStatus);
			}

			//提醒續約
			if (orgStatus.equals(EnumContractStatus.USE.getStatusId()) && pojo.getRemindFlag().equals("n")) {
				if ((_endDate-_now) < remindTime) {
					mailContent += "合約編號：" + pojo.getPfdContractId() + "(" + pojo.getPfdCustomerInfo().getCompanyName() +  ") 即將於 " + sdf.format(pojo.getEndDate()) + " 到期<br>";
					pojo.setRemindFlag("y");
					pojo.setRemindDate(now);
				}
			}

			pfdContractService.saveOrUpdate(pojo);
		}

		//發信通知管理者續約
		if (StringUtils.isNotEmpty(mailContent)) {

			mailContent = "<html><body>" + mailContent;
			mailContent += "</body></html>";

			try {

				Mail mail = null;

		        try {

		        	mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		        	if (mail == null) {
		        		throw new Exception("Mail Object is null.");
		        	}

		        } catch (Exception e) {
		        	log.error(e.getMessage(), e);
		        	throw e;
		        }

	            mail.setMsg(mailContent);

	            springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

        log.info("====PfdContractJob.process() end====");
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

	public static void main(String[] args) throws Exception {
		
//		System.out.println("測試資料start");
//		String[] test = { "local" };

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfdContractJob job = context.getBean(PfdContractJob.class);

		job.process();
		
//		System.out.println("測試資料end");
//		System.exit(1);
	}
}
