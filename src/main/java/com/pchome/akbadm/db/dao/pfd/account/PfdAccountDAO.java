package com.pchome.akbadm.db.dao.pfd.account;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;

public class PfdAccountDAO extends BaseDAO<PfdCustomerInfo, String> implements IPfdAccountDAO {

	@SuppressWarnings("unchecked")
	public List<PfdCustomerInfo> getPfdCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception {

		StringBuffer sql = new StringBuffer("from PfdCustomerInfo where 1=1");

		if (conditionMap.containsKey("customerInfoId")) {
			sql.append(" and customerInfoId = '" + conditionMap.get("customerInfoId") + "'");
		}
		if (conditionMap.containsKey("status")) {
			sql.append(" and status in (" + conditionMap.get("status") + ")");
		}
		if (conditionMap.containsKey("companyTaxId")) {
			sql.append(" and companyTaxId = '" + conditionMap.get("companyTaxId") + "'");
		}

		return super.getHibernateTemplate().find(sql.toString());
	}

}
