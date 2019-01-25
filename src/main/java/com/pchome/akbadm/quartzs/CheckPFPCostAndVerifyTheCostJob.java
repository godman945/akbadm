package com.pchome.akbadm.quartzs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusBillReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;
import com.pchome.config.TestConfig;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

public class CheckPFPCostAndVerifyTheCostJob {

	protected Log log = LogFactory.getLog(this.getClass());
	
	private List<PfbxInComeReportVo> PfbxInComeReportVo;
	//Service
	private IAdmBonusBillReportService admBonusBillReportService;
	
    private SpringEmailUtil springEmailUtil;
    public static final String MAIL_API_NO = "P098";
    
	/**
	 * 本機測試排程用
	 * @param args
	 */
    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        CheckPFPCostAndVerifyTheCostJob job = context.getBean(CheckPFPCostAndVerifyTheCostJob.class);
        job.process();
    }

    /**
     * 每日排程
     * 處理比對前一天PFP花費與驗證花費是否相等
     * 不相等時發email
     */
	public void process() {
		log.info("====CheckPFPCostAndVerifyTheCostJob.process() start====");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");          //撈DB資料使用格式
		SimpleDateFormat subjectSdf = new SimpleDateFormat("yyyy年MM月dd日"); //信件主旨使用格式
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		String yesterday = sdf.format(c.getTime());

		try {
			PfbxInComeReportVo = admBonusBillReportService.getPfbxInComeReportVoList5(yesterday, yesterday);
			if(PfbxInComeReportVo.size() != 0){
				Float adclkprice = new Float(PfbxInComeReportVo.get(0).getAdclkprice());   //PFP花費
				Float sysclkprice = new Float(PfbxInComeReportVo.get(0).getSysclkprice()); //驗證花費
				log.info("PFP花費:" + adclkprice);
				log.info("驗證花費:" + sysclkprice);
	
				//PFP花費與驗證花費不相等時寄通知信
				if(!adclkprice.equals(sysclkprice)){
					Mail mail = null;
					mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
					if (mail == null) {
						throw new Exception("Mail Object is null.");
					}
					String subject = "付費刊登 盈虧查詢報表資料異常 " + subjectSdf.format(c.getTime());
					mail.setMsg("<html><body>盈虧查詢報表資料異常 " + subjectSdf.format(c.getTime()) + "</body></html>");
					springEmailUtil.sendHtmlEmail(subject, mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
				}
			}else{
				log.info(yesterday + " PFP花費、驗證花費，查無資料。");
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		
	}

	public List<PfbxInComeReportVo> getPfbxInComeReportVo() {
		return PfbxInComeReportVo;
	}

	public void setPfbxInComeReportVo(List<PfbxInComeReportVo> pfbxInComeReportVo) {
		PfbxInComeReportVo = pfbxInComeReportVo;
	}

	public IAdmBonusBillReportService getAdmBonusBillReportService() {
		return admBonusBillReportService;
	}

	public void setAdmBonusBillReportService(IAdmBonusBillReportService admBonusBillReportService) {
		this.admBonusBillReportService = admBonusBillReportService;
	}

	public SpringEmailUtil getSpringEmailUtil() {
		return springEmailUtil;
	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

}
