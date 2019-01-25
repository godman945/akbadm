package com.pchome.akbadm.db.dao.pfd.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdUser;

public interface IPfdUserDAO extends IBaseDAO<PfdUser, String> {

	public List<PfdUser> getPfdUserByCondition(Map<String, String> conditionMap) throws Exception;

	public HashMap<String, PfdUser> getPfdUserBySeqList(List<String> userIdList) throws Exception;

	public List<PfdUser> findRootPfdUser(String pfdCustomerInfoId) throws Exception;
}
