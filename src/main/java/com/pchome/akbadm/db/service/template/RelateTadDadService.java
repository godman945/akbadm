package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.AdmRelateTadDadDAO;
import com.pchome.akbadm.db.dao.template.AdmRelateTadDadVO;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class RelateTadDadService extends BaseService<AdmRelateTadDad, String> implements IRelateTadDadService {

	private AdmRelateTadDadDAO admRelateTadDadDAO;

	public List<AdmRelateTadDad> getAllRelateTadDad() throws Exception {
		return admRelateTadDadDAO.loadAll();
	}

	public List<AdmRelateTadDad> getRelateTadDadByCondition(String templateAdSeq, String defineAdSeq) throws Exception {
		return admRelateTadDadDAO.getRelateTadDadByCondition(templateAdSeq, defineAdSeq);
	}
	
	public AdmRelateTadDad getRelateTadDadById(String relateTadDadId) throws Exception {
		return admRelateTadDadDAO.getRelateTadDadById(relateTadDadId);
	}
	
	public AdmRelateTadDad getRelateTadDadBySeq(String relateTadDadSeq) throws Exception {
		return admRelateTadDadDAO.getRelateTadDadBySeq(relateTadDadSeq);
	}

	public void insertAdmRelateTadDad(AdmRelateTadDad admRelateTadDad) throws Exception {
		admRelateTadDadDAO.insertRelateTadDad(admRelateTadDad);
	}

	public void saveAdmRelateTadDad(AdmRelateTadDadVO admRelateTadDadVO) throws Exception {
		admRelateTadDadDAO.saveRelateTadDad(admRelateTadDadVO);
	}

	public void updateAdmRelateTadDad(AdmRelateTadDad admRelateTadDad) throws Exception {
		admRelateTadDadDAO.updateRelateTadDad(admRelateTadDad);
	}

	public void deleteAdmRelateTadDad(String admRelateTadDadSeq) throws Exception {
		admRelateTadDadDAO.deleteRelateTadDad(admRelateTadDadSeq);
	}

	public void setAdmRelateTadDadDAO(AdmRelateTadDadDAO admRelateTadDadDAO) throws Exception {
		this.admRelateTadDadDAO = admRelateTadDadDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
