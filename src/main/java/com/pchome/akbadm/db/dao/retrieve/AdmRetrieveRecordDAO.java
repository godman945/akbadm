package com.pchome.akbadm.db.dao.retrieve;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;

public class AdmRetrieveRecordDAO extends BaseDAO<AdmRetrieveRecord, Integer> implements IAdmRetrieveRecordDAO{

	@SuppressWarnings("unchecked")
	public List<AdmRetrieveRecord> findRetrieveRecord(Map<String, String> conditionMap) throws Exception {

		List<Object> paramList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();
		hql.append(" from AdmRetrieveRecord where 1=1");

		if (conditionMap.containsKey("pfpCustomerInfoId")) {
			hql.append(" and pfpCustomerInfoId = ? ");
			paramList.add(conditionMap.get("pfpCustomerInfoId"));
		}
		if (conditionMap.containsKey("recordDate")) {
			hql.append(" and recordDate = ? ");
			paramList.add(conditionMap.get("recordDate"));
		}
		if (conditionMap.containsKey("orderBy")) {
			hql.append(" order by " + conditionMap.get("orderBy"));
		}
		if (conditionMap.containsKey("desc")) {
			hql.append(" desc");
		}

		return (List<AdmRetrieveRecord>) super.getHibernateTemplate().find(hql.toString(), paramList.toArray());
	}
}
