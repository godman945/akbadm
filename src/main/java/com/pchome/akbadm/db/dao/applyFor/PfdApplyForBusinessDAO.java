package com.pchome.akbadm.db.dao.applyFor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;

public class PfdApplyForBusinessDAO extends BaseDAO<PfdApplyForBusiness, String> implements IPfdApplyForBusinessDAO {

	public int findPfdApplyForBusinessCount(Map<String, String> conditionsMap) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<Object> paramlist = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();
		hql.append("select count(*) from PfdApplyForBusiness where 1=1 ");
		if (conditionsMap.containsKey("taxId")) {
			hql.append(" and targetTaxId = ?");
			paramlist.add(conditionsMap.get("taxId"));
		}
		if (conditionsMap.containsKey("status")) {
			hql.append(" and status = ?");
			paramlist.add(conditionsMap.get("status"));
		}
		if (conditionsMap.containsKey("startDate")) {
			hql.append(" and applyForTime >= ?");
			paramlist.add(sdf.parse(conditionsMap.get("startDate") + " 00:00:00"));
		}
		if (conditionsMap.containsKey("endDate")) {
			hql.append(" and applyForTime <= ?");
			paramlist.add(sdf.parse(conditionsMap.get("endDate") + " 23:59:59"));
		}

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Query q = super.getSession().createQuery(strHQL);

		for (int i = 0; i < paramlist.size(); i++) {
            q.setParameter(i, paramlist.get(i));
        }

		return ((Long) q.uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<PfdApplyForBusiness> findPfdApplyForBusiness(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfdApplyForBusiness where 1=1 ");
		if (conditionsMap.containsKey("seq")) {
			hql.append(" and seq = :seq");
		}
		if (conditionsMap.containsKey("taxId")) {
			hql.append(" and targetTaxId = :taxId");
		}
		if (conditionsMap.containsKey("status")) {
			hql.append(" and status = :status");
		}
		if (conditionsMap.containsKey("startDate")) {
			hql.append(" and applyForTime >= :startDate");
		}
		if (conditionsMap.containsKey("endDate")) {
			hql.append(" and applyForTime <= :endDate");
		}
		if (conditionsMap.containsKey("orderBy")) {
			hql.append(" order by " + conditionsMap.get("orderBy"));
		}
		if (conditionsMap.containsKey("desc")) {
			hql.append(" desc");
		}

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Session session = getSession();
		Query q;
		if (pageNo==-1) {
			q = session.createQuery(strHQL);
		} else {
			q = session.createQuery(strHQL)
			.setFirstResult((pageNo-1)*pageSize)
			.setMaxResults(pageSize);
		}

		if (conditionsMap.containsKey("seq")) {
			q.setParameter("seq", Integer.valueOf(conditionsMap.get("seq")));
		}
		if (conditionsMap.containsKey("taxId")) {
			q.setParameter("taxId", conditionsMap.get("taxId"));
		}
		if (conditionsMap.containsKey("status")) {
			q.setParameter("status", conditionsMap.get("status"));
		}
		if (conditionsMap.containsKey("startDate")) {
			q.setParameter("startDate", sdf.parse(conditionsMap.get("startDate") + " 00:00:00"));
		}
		if (conditionsMap.containsKey("endDate")) {
			q.setParameter("endDate", sdf.parse(conditionsMap.get("endDate") + " 23:59:59"));
		}

		return q.list();
	}

	/**
	 * 更新審核後的狀態
	 */
	public void updatePfdApplyForBusinessStatus(String status, String seq, String verifyUserId,
			String rejectReason) throws Exception {
		String hql = "update PfdApplyForBusiness set status=:status, checkUser=:verifyUserId, rejectReason=:rejectReason, checkTime=CURRENT_TIMESTAMP() where seq = :seq)";
		Session session = getSession();
        Query query = session.createQuery(hql);
        query.setString("status", status);
        query.setString("verifyUserId", verifyUserId);
        query.setString("rejectReason", rejectReason);
        query.setString("seq", seq);
        query.executeUpdate();
        session.flush();
	}

	public List<PfdApplyForBusiness> findPfdApplyForBusinessStatus(String status) throws Exception {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfdApplyForBusiness where 1=1 ");
		if (StringUtils.isNotEmpty(status)) {
			hql.append(" and status = :status");
		}
		
		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Session session = getSession();
		Query q = session.createQuery(strHQL);
		

		if (StringUtils.isNotEmpty(status)) {
			q.setParameter("status", status);
		}
		
		return q.list();
	}
	
}
