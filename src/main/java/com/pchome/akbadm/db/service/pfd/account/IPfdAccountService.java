package com.pchome.akbadm.db.service.pfd.account;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfdAccountService extends IBaseService<PfdCustomerInfo, String>{

	public List<PfdCustomerInfo> getPfdCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception;

}
