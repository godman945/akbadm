package com.pchome.akbadm.db.dao.user;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmUser;

public interface IAdmUserDAO extends IBaseDAO<AdmUser, String> {

	public List<AdmUser> getUserByPrivilegeModelId(String privilegeModelId) throws Exception;

	public List<AdmUser> getUserByCondition(String userEmail, String userName) throws Exception;

	public void insertUser(AdmUser user) throws Exception;

	public void updateUser(AdmUser user) throws Exception;

	public AdmUser getUserById(String userEmail) throws Exception;

	public List<AdmUser> getUserByDeptId(String deptId) throws Exception;

	public List<AdmUser> getUserByDeptId2(String deptId) throws Exception;

}
