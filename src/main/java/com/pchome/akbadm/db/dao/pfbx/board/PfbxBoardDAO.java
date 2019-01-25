package com.pchome.akbadm.db.dao.pfbx.board;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBoard;

public class PfbxBoardDAO extends BaseDAO<PfbxBoard, Integer> implements IPfbxBoardDAO {

	@SuppressWarnings("unchecked")
	public List<PfbxBoard> findPfbxBoard(Map<String, String> conditionsMap) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBoard where 1=1");
		if (conditionsMap.containsKey("boardId")) {
			hql.append(" and boardId = :boardId");
		}
		if (conditionsMap.containsKey("isSysBoard")) {
			hql.append(" and isSysBoard = :isSysBoard");
		}
		if (conditionsMap.containsKey("pfdCustomerInfoId")) {
			hql.append(" and pfbxCustomerInfoId = :pfbxCustomerInfoId");
		}
		if (conditionsMap.containsKey("pfbxCustomerInfoId")) {
			hql.append(" and pfbxCustomerInfoId = :pfbxCustomerInfoId");
		}
		if (conditionsMap.containsKey("deleteId")) {
			hql.append(" and deleteId = :deleteId");
		}
		if (conditionsMap.containsKey("orderBy")) {
			hql.append(" order by :orderBy");
		}
		if (conditionsMap.containsKey("desc")) {
			hql.append(" desc");
			conditionsMap.remove("desc");
		}

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Session session = getSession();

		Query q = session.createQuery(strHQL);

		if (conditionsMap.containsKey("boardId")) {
			q.setInteger("boardId", Integer.parseInt(conditionsMap.get("boardId")));
		}
		if (conditionsMap.containsKey("isSysBoard")) {
			q.setString("isSysBoard", conditionsMap.get("isSysBoard"));
		}
		if (conditionsMap.containsKey("pfbxCustomerInfoId")) {
			q.setString("pfbxCustomerInfoId", conditionsMap.get("pfbxCustomerInfoId"));
		}
		if (conditionsMap.containsKey("deleteId")) {
			q.setString("deleteId", conditionsMap.get("deleteId"));
		}
		if (conditionsMap.containsKey("orderBy")) {
			q.setString("orderBy", conditionsMap.get("orderBy"));
		}

		return q.list();
	}

    public Integer deletePfbxBoardOvertime(Date overtime) throws Exception {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfbxBoard ");
        hql.append("where endDate < ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), overtime);
    }

	public void deletePfbxBoardById(String boardId) throws Exception {

		String hql = "delete from PfbxBoard where boardId = '" + boardId + "'";
		Session session = getSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}

	public void deleteBoard(String pfbCustomerInfoId, String deleteId) throws Exception {

		if (StringUtils.isNotBlank(pfbCustomerInfoId) && StringUtils.isNotBlank(deleteId)) {

			StringBuffer hql = new StringBuffer();
			hql.append(" delete PfbxBoard where 1=1 ");
			hql.append(" and pfbxCustomerInfoId = '" + pfbCustomerInfoId + "' ");
			hql.append(" and deleteId = '" + deleteId + "'");

			this.getSession().createQuery(hql.toString()).executeUpdate();
		}
	}

	/**
	 * 依下面輸入參數值，刪除相對應PFB系統公告
	 * @param pfbxCustomerInfo 聯播網帳戶
	 * @param deleteId 刪除的依據
	 * @param createDate 建立日期
	 */
	@Override
	public void delectBoardFindLatestContent(String pfbxCustomerInfo, String deleteId, String createDate) {
		String hql = "DELETE FROM PfbxBoard ";
		hql += " WHERE pfbx_customer_info_id = '" + pfbxCustomerInfo + "'";
		hql += " AND delete_id = '" + deleteId + "'";
		hql += " AND create_date >= '" + createDate + "'";
		Session session = getSession();
		session.createQuery(hql).executeUpdate();
		session.flush();
	}
}
