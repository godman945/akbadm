package com.pchome.akbadm.db.dao.pfd.account;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;

public interface IPfdAccountDAO extends IBaseDAO<PfdCustomerInfo, String> {

	public List<PfdCustomerInfo> getPfdCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception;

}
