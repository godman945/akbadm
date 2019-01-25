package com.pchome.akbadm.db.service.user;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.user.AdmUserDAO;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class UserService extends BaseService<AdmUser, String> implements IUserService {

	private AdmUserDAO admuserDAO;

	public List<AdmUser> getAllUser() throws Exception {
		return admuserDAO.loadAll();
	}

	public List<AdmUser> getUserByCondition(String userEmail, String userName) throws Exception {
		return admuserDAO.getUserByCondition(userEmail, userName);
	}
	
	public AdmUser getUserById(String userEmail) throws Exception {
		return admuserDAO.getUserById(userEmail);
	}

	public void insertUser(AdmUser user) throws Exception {
		admuserDAO.insertUser(user);
	}

	public void updateUser(AdmUser user) throws Exception {
		admuserDAO.updateUser(user);
	}

	public void deleteUser(String userEmail) throws Exception {
		admuserDAO.deleteUser(userEmail);
	}

	public void setAdmuserDAO(AdmUserDAO admuserDAO) {
		this.admuserDAO = admuserDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
