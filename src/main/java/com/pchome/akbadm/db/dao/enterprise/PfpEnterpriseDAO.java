package com.pchome.akbadm.db.dao.enterprise;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpEnterprise;

public class PfpEnterpriseDAO extends BaseDAO<PfpEnterprise, String> implements IPfpEnterpriseDAO {

	@SuppressWarnings("unchecked")
	public List<PfpEnterprise> findPfpEnterprise(Map<String, String> conditionsMap) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpEnterprise where 1=1 ");
		if (conditionsMap.containsKey("taxId")) {
			hql.append(" and taxId = :taxId");
		}

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Query q = super.getSession().createQuery(strHQL);
		if (conditionsMap.containsKey("taxId")) {
			q.setParameter("taxId", conditionsMap.get("taxId"));
		}

		return q.list();
	}

	public void deletePfpEnterprise(String taxId) throws Exception {
		Session session = getSession();
		String sql = "delete from PfpEnterprise where taxId = '" + taxId + "'";
		session.createQuery(sql).executeUpdate();
	}
}
