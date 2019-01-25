package com.pchome.akbadm.db.service.pfd.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfd.account.IPfdAccountDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAccountService extends BaseService<PfdCustomerInfo, String> implements IPfdAccountService {

	public List<PfdCustomerInfo> getPfdCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception {
		return ((IPfdAccountDAO) dao).getPfdCustomerInfoByCondition(conditionMap);
	}
}
