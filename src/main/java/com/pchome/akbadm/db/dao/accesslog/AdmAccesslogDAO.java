package com.pchome.akbadm.db.dao.accesslog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.enumerate.accesslog.EnumSearchType;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class AdmAccesslogDAO extends BaseDAO <AdmAccesslog, Integer> implements IAdmAccesslogDAO {

	@SuppressWarnings("unchecked")
	public List<AdmAccesslog> findAdmAccesslog(String id, EnumSearchType type) throws Exception{
		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmAccesslog ");

		if(type.equals(EnumSearchType.LOG_ACCESSLOG_ID)){
			hql.append(" where accesslogId = '"+id+"' ");
		}

		if(type.equals(EnumSearchType.LOG_CUSTOMERINFO_ID)){
			hql.append(" where customerInfoId = '"+id+"' ");
		}

		if(type.equals(EnumSearchType.LOG_IP)){
			hql.append(" where clientIp = '"+id+"' ");
		}

		if(type.equals(EnumSearchType.LOG_MEMBER_ID)){
			hql.append(" where memberId = '"+id+"' ");
		}

		if(type.equals(EnumSearchType.LOG_ORDER_ID)){
			hql.append(" where orderId = '"+id+"' ");
		}

		if(type.equals(EnumSearchType.LOG_USER_ID)){
			hql.append(" where userId = '"+id+"' ");
		}

		hql.append(" order by createDate desc ");
		//log.info(">> hql = "+hql.toString());
		return (List<AdmAccesslog>) super.getHibernateTemplate().find(hql.toString());
	}

    public int selectAdmAccesslogCount(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate) {
        StringBuffer hql = new StringBuffer();
        List<Object> list = new ArrayList<Object>();

        hql.append("select count(accesslogId) ");
        hql.append("from AdmAccesslog ");
        hql.append("where 1 = 1 ");
        if (StringUtils.isNotBlank(channel)) {
            hql.append("and channel = ? ");
            list.add(channel);
        }
        if (StringUtils.isNotBlank(action)) {
            hql.append("and action = ? ");
            list.add(action);
        }
        if (StringUtils.isNotBlank(memberId)) {
            hql.append("and memberId = ? ");
            list.add(memberId);
        }
        if (StringUtils.isNotBlank(orderId)) {
            hql.append("and orderId = ? ");
            list.add(orderId);
        }
        if (StringUtils.isNotBlank(customerInfoId)) {
            hql.append("and customerInfoId = ? ");
            list.add(customerInfoId);
        }
        if (StringUtils.isNotBlank(userId)) {
            hql.append("and userId = ? ");
            list.add(userId);
        }
        if (StringUtils.isNotBlank(clientIp)) {
            hql.append("and clientIp = ? ");
            list.add(clientIp);
        }
        if (StringUtils.isNotBlank(message)) {
            hql.append("and message like ? ");
            list.add("%" + message + "%");
        }
        if (startDate != null) {
            hql.append("and createDate >= ? ");
            list.add(startDate);
        }
        if (endDate != null) {
            hql.append("and createDate <= ? ");
            list.add(endDate);
        }
        
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
        for (int i = 0; i < list.size(); i++) {
            query.setParameter(i, list.get(i));
        }
        return ((Long) query.uniqueResult()).intValue();
    }

	@SuppressWarnings("unchecked")
	public List<AdmAccesslog> selectAdmAccesslog(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate, int firstResult, int maxResults) {
	    StringBuffer hql = new StringBuffer();
        List<Object> list = new ArrayList<Object>();

	    hql.append("from AdmAccesslog ");
	    hql.append("where 1 = 1 ");
	    if (StringUtils.isNotBlank(channel)) {
	        hql.append("and channel = ? ");
            list.add(channel);
	    }
        if (StringUtils.isNotBlank(action)) {
            hql.append("and action = ? ");
            list.add(action);
        }
        if (StringUtils.isNotBlank(memberId)) {
            hql.append("and memberId = ? ");
            list.add(memberId);
        }
        if (StringUtils.isNotBlank(orderId)) {
            hql.append("and orderId = ? ");
            list.add(orderId);
        }
        if (StringUtils.isNotBlank(customerInfoId)) {
            hql.append("and customerInfoId = ? ");
            list.add(customerInfoId);
        }
        if (StringUtils.isNotBlank(userId)) {
            hql.append("and userId = ? ");
            list.add(userId);
        }
        if (StringUtils.isNotBlank(clientIp)) {
            hql.append("and clientIp = ? ");
            list.add(clientIp);
        }
        if (StringUtils.isNotBlank(message)) {
            hql.append("and message like ? ");
            list.add("%" + message + "%");
        }
        if (startDate != null) {
            hql.append("and createDate >= ? ");
            list.add(startDate);
        }
        if (endDate != null) {
            hql.append("and createDate <= ? ");
            list.add(endDate);
        }
        hql.append("order by accesslogId desc ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        for (int i = 0; i < list.size(); i++) {
            query.setParameter(i, list.get(i));
        }
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmAccesslog> findSendMail() {
		
		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmAccesslog ");
		hql.append(" where mailSend = ? ");
		hql.append(" order by createDate desc ");

		return (List<AdmAccesslog>) super.getHibernateTemplate().find(hql.toString(),EnumAccesslogEmailStatus.YES.getStatus());
	}
}
