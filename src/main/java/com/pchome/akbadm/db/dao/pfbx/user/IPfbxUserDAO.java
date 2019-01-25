package com.pchome.akbadm.db.dao.pfbx.user;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUser;

public interface IPfbxUserDAO extends IBaseDAO<PfbxUser, String>{

	public List<PfbxUser> findAllPfbxRootUser(String keyword);
}
