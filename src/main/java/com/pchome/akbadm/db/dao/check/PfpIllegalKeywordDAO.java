package com.pchome.akbadm.db.dao.check;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpIllegalKeyword;

public class PfpIllegalKeywordDAO extends BaseDAO<PfpIllegalKeyword,String> implements IPfpIllegalKeywordDAO {

	public int getIllegalKeywordCountByCondition(String queryString) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from PfpIllegalKeyword where 1=1");
		if (StringUtils.isNotEmpty(queryString)) {
			sb.append(" and content like '%" + queryString.trim() + "%'");
		}

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		Long count = (Long) (super.getHibernateTemplate().find(hql).listIterator().next());

		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<PfpIllegalKeyword> getIllegalKeywordByCondition(String queryString, int pageNo, int pageSize) throws Exception {
		StringBuffer sb = new StringBuffer("from PfpIllegalKeyword where 1=1");
		if (StringUtils.isNotEmpty(queryString)) {
			sb.append(" and content like '%" + queryString.trim() + "%'");
		}

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query;
		if (pageNo==-1) {
			query = session.createQuery(hql.toString());
		} else {
			query = session.createQuery(hql.toString())
			.setFirstResult((pageNo-1)*pageSize)  
			.setMaxResults(pageSize);
		}

		return query.list();
	}

	public void updateIllegalKeywordBySeq(String seq, String content) throws Exception {
		String sql = "update PfpIllegalKeyword set content = '" + content + "', update_date = CURRENT_DATE() where seq = '" + seq + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public void deleteIllegalKeywordBySeq(String seq) throws Exception {
		String sql = "delete from PfpIllegalKeyword where seq = '" + seq + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public int checkIllegalKeywordExists(String queryString) throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) from PfpIllegalKeyword where 1=1");
		if (StringUtils.isNotEmpty(queryString)) {
			sb.append(" and content = '" + queryString.trim() + "'");
		}

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		Long count = (Long) (super.getHibernateTemplate().find(hql).listIterator().next());

		return count.intValue();
	}
}
