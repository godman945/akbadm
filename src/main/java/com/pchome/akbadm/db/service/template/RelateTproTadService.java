package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.AdmRelateTproTadDAO;
import com.pchome.akbadm.db.dao.template.AdmRelateTproTadVO;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class RelateTproTadService extends BaseService<AdmRelateTproTad, String> implements IRelateTproTadService {

	private AdmRelateTproTadDAO admRelateTproTadDAO;

	public List<AdmRelateTproTad> getAllRelateTproTad() throws Exception {
		return admRelateTproTadDAO.loadAll();
	}

	public List<AdmRelateTproTad> getRelateTproTadByCondition(String templateProductSeq, String templateAdSeq) throws Exception {
		return admRelateTproTadDAO.getRelateTproTadByCondition(templateProductSeq, templateAdSeq);
	}
	
	public AdmRelateTproTad getRelateTproTadById(String relateTproTadId) throws Exception {
		return admRelateTproTadDAO.getRelateTproTadById(relateTproTadId);
	}
	
	public AdmRelateTproTad getRelateTproTadBySeq(String relateTproTadSeq) throws Exception {
		return admRelateTproTadDAO.getRelateTproTadBySeq(relateTproTadSeq);
	}

	public void insertAdmRelateTproTad(AdmRelateTproTad admRelateTproTad) throws Exception {
		admRelateTproTadDAO.insertRelateTproTad(admRelateTproTad);
	}

	public void saveAdmRelateTproTad(AdmRelateTproTadVO admRelateTproTadVO) throws Exception {
		admRelateTproTadDAO.saveRelateTproTad(admRelateTproTadVO);
	}

	public void updateAdmRelateTproTad(AdmRelateTproTad admRelateTproTad) throws Exception {
		admRelateTproTadDAO.updateRelateTproTad(admRelateTproTad);
	}

	public void deleteAdmRelateTproTad(String admRelateTproTadSeq) throws Exception {
		admRelateTproTadDAO.deleteRelateTproTad(admRelateTproTadSeq);
	}

	public void setAdmRelateTproTadDAO(AdmRelateTproTadDAO admRelateTproTadDAO) throws Exception {
		this.admRelateTproTadDAO = admRelateTproTadDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
