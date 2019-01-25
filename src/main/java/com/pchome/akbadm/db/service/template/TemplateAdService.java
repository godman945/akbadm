package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.AdmTemplateAdDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class TemplateAdService extends BaseService<AdmTemplateAd, String> implements ITemplateAdService {

	private AdmTemplateAdDAO admTemplateAdDAO;

	public List<AdmTemplateAd> getAllTemplateAd() throws Exception {
		return admTemplateAdDAO.loadAll();
	}

	public List<AdmTemplateAd> getTemplateAdByCondition(String templateAdSeq, String templateAdName, String templateAdType, String templateAdWidth, String templateAdHeight) throws Exception {
		return admTemplateAdDAO.getTemplateAdByCondition(templateAdSeq, templateAdName,templateAdType,templateAdWidth,templateAdHeight);
	}
	
	public AdmTemplateAd getTemplateAdById(String templateAdId) throws Exception {
		return admTemplateAdDAO.getTemplateAdById(templateAdId);
	}
	
	public AdmTemplateAd getTemplateAdBySeq(String templateAdSeq) throws Exception {
		return admTemplateAdDAO.getTemplateAdBySeq(templateAdSeq);
	}

	public void insertAdmTemplateAd(AdmTemplateAd admTemplateAd) throws Exception {
		admTemplateAdDAO.insertTemplateAd(admTemplateAd);
	}

	public void updateAdmTemplateAd(AdmTemplateAd admTemplateAd) throws Exception {
		admTemplateAdDAO.updateTemplateAd(admTemplateAd);
	}

	public void deleteAdmTemplateAd(String admTemplateAdSeq) throws Exception {
		admTemplateAdDAO.deleteTemplateAd(admTemplateAdSeq);
	}

	public void saveAdmTemplateAd(AdmTemplateAd admTemplateAd) throws Exception {
		admTemplateAdDAO.saveTemplateAd(admTemplateAd);
	}

	public void setAdmTemplateAdDAO(AdmTemplateAdDAO admTemplateadDAO) throws Exception {
		this.admTemplateAdDAO = admTemplateadDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
