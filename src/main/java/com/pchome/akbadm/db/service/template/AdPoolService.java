package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.IAdmAdPoolDAO;
import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class AdPoolService extends BaseService<AdmAdPool, String> implements IAdPoolService {

	public List<AdmAdPool> getAdPoolByCondition(String adPoolSeq, String aPoolName, String diffComapny) throws Exception {
		return ((IAdmAdPoolDAO) dao).getAdPoolByCondition(adPoolSeq, aPoolName, diffComapny);
	}

	public AdmAdPool getAdPoolBySeq(String AdPoolSeq) throws Exception {
		return ((IAdmAdPoolDAO) dao).getAdPoolBySeq(AdPoolSeq);
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
