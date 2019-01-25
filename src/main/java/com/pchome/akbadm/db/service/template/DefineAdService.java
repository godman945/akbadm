package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.IAdmDefineAdDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class DefineAdService extends BaseService<AdmDefineAd, String> implements IDefineAdService {

    public List<AdmDefineAd> getDefineAdByCondition(String defineAdSeq, String defineAdName, String adPoolSeq) throws Exception {
		return ((IAdmDefineAdDAO) dao).getDefineAdByCondition(defineAdSeq, defineAdName, adPoolSeq);
	}

	public AdmDefineAd getDefineAdById(String defineAdId) throws Exception {
		return ((IAdmDefineAdDAO) dao).getDefineAdById(defineAdId);
	}

	public AdmDefineAd getDefineAdBySeq(String defineAdSeq) throws Exception {
		return ((IAdmDefineAdDAO) dao).getDefineAdBySeq(defineAdSeq);
	}

	public AdmDefineAd getDefineAdByPoolSeq(String adPoolSeq) throws Exception {
		return ((IAdmDefineAdDAO) dao).getDefineAdByPoolSeq(adPoolSeq);
	}

    public int deleteDefineAdByPoolSeq(String adPoolSeq) throws Exception {
        return ((IAdmDefineAdDAO) dao).deleteDefineAdByPoolSeq(adPoolSeq);
    }

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
