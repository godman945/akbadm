package com.pchome.akbadm.db.service.template;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.template.IAdmDefineAdTypeDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAdType;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class DefineAdTypeService extends BaseService<AdmDefineAdType, String> implements IDefineAdTypeService {

    public List<AdmDefineAdType> getAllDefineAdType() throws Exception {
		return ((IAdmDefineAdTypeDAO) dao).getDefineAdType();
	}

	public List<AdmDefineAdType> getDefineAdTypeByCondition(String defineAdTypeId, String defineAdTypeName) throws Exception {
		return ((IAdmDefineAdTypeDAO) dao).getDefineAdTypeByCondition(defineAdTypeId, defineAdTypeName);
	}

	public AdmDefineAdType getDefineAdTypeById(String defineAdTypeId) throws Exception {
		return ((IAdmDefineAdTypeDAO) dao).getDefineAdTypeById(defineAdTypeId);
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
