package com.pchome.akbadm.db.service.user;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.IBaseService;

public interface IUserService extends IBaseService<AdmUser, String>{

	public List<AdmUser> getAllUser() throws Exception;

	public List<AdmUser> getUserByCondition(String userEmail, String userName) throws Exception;

	public AdmUser getUserById(String userEmail) throws Exception;

	public void insertUser(AdmUser user) throws Exception;

	public void updateUser(AdmUser user) throws Exception;

	public void deleteUser(String userEmail) throws Exception;

}
