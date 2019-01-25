package com.pchome.akbadm.quartzs;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;
import com.pchome.akbadm.db.service.applyFor.IPfdApplyForBusinessService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.applyFor.EnumPfdApplyForBusiness;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

public class PfdApplyForBusinessJob {

	private Log log = LogFactory.getLog(getClass().getName());
	private IPfdApplyForBusinessService pfdApplyForBusinessService;
	
	public static final String MAIL_API_NO = "P159";
	private SpringEmailUtil springEmailUtil;
	
	//經銷商申請開發審核e-mail通知
	public void process() {
		List<PfdApplyForBusiness> data;
		try {
			data = pfdApplyForBusinessService.findPfdApplyForBusinessStatus(EnumPfdApplyForBusiness.YET.getType());
			log.info(">>> data.size() = " + data.size());
			if(data.size() > 0){
				Mail mail = null;
				mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

	        	if (mail == null) {
	        		throw new Exception("Mail Object is null.");
	        	}

	        	mail.setMsg("<html><body>您有 " + data.size() + " 個經銷商申請開發待審</body></html>");
	        	
	            springEmailUtil.sendHtmlEmail("廣告經銷商申請開發審核通知", mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}		
	}
	
	public void setPfdApplyForBusinessService(
			IPfdApplyForBusinessService pfdApplyForBusinessService) {
		this.pfdApplyForBusinessService = pfdApplyForBusinessService;
	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

	public static void main(String[] args) {
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        PfdApplyForBusinessJob job = context.getBean(PfdApplyForBusinessJob.class);
        job.process();
	}

}
