package com.pchome.akbadm.db.dao.pfd.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBoard;

public class PfdBoardDAO extends BaseDAO<PfdBoard, Integer> implements IPfdBoardDAO {

	@SuppressWarnings("unchecked")
	public List<PfdBoard> findPfdBoard(Map<String, String> conditionsMap) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfdBoard where 1=1");
		if (conditionsMap.containsKey("boardId")) {
			hql.append(" and boardId = :boardId");
		}
		if (conditionsMap.containsKey("isSysBoard")) {
			hql.append(" and isSysBoard = :isSysBoard");
		}
		if (conditionsMap.containsKey("pfdCustomerInfoId")) {
			hql.append(" and pfdCustomerInfoId = :pfdCustomerInfoId");
		}
		if (conditionsMap.containsKey("pfdUserId")) {
			hql.append(" and pfdUserId = :pfdUserId");
		}
		if (conditionsMap.containsKey("deleteId")) {
			hql.append(" and deleteId = :deleteId");
		}
		if (conditionsMap.containsKey("content")) {
			hql.append(" and boardContent like :content");
		}
		if (conditionsMap.containsKey("orderBy")) {
			hql.append(" order by " + conditionsMap.get("orderBy"));
		}
		if (conditionsMap.containsKey("desc")) {
			hql.append(" desc");
			conditionsMap.remove("desc");
		}

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query q = session.createQuery(strHQL);

		if (conditionsMap.containsKey("boardId")) {
			q.setInteger("boardId", Integer.parseInt(conditionsMap.get("boardId")));
		}
		if (conditionsMap.containsKey("isSysBoard")) {
			q.setString("isSysBoard", conditionsMap.get("isSysBoard"));
		}
		if (conditionsMap.containsKey("pfdCustomerInfoId")) {
			q.setString("pfdCustomerInfoId", conditionsMap.get("pfdCustomerInfoId"));
		}
		if (conditionsMap.containsKey("pfdUserId")) {
			q.setString("pfdUserId", conditionsMap.get("pfdUserId"));
		}
		if (conditionsMap.containsKey("deleteId")) {
			q.setString("deleteId", conditionsMap.get("deleteId"));
		}
		if (conditionsMap.containsKey("content")) {
			q.setString("content", conditionsMap.get("content"));
		}
		/*if (conditionsMap.containsKey("orderBy")) {
			q.setString("orderBy", conditionsMap.get("orderBy"));
		}*/

//		q.setProperties(conditionsMap);
		
		return q.list();
	}

    public Integer deletePfdBoardOvertime(Date overtime) throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfdBoard ");
        hql.append("where endDate < ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), overtime);
    }

	public void deletePfdBoardById(String boardId) throws Exception {

		String hql = "delete from PfdBoard where boardId = '" + boardId + "'";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}
	
	public void deletePfdBoardByDeleteId(String deleteId) throws Exception {

		String hql = "delete from PfdBoard where deleteId = '" + deleteId + "'";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}
}
