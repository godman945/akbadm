package com.pchome.akbadm.db.dao.customerInfo;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

public class PfbCustomerInfoDAO extends BaseDAO<PfbxCustomerInfo, String> implements IPfbCustomerInfoDAO{

	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> findPfbValidCustomerInfo() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfbxCustomerInfo ");
		hql.append(" where activateDate != null ");
		hql.append(" and (status = ? or status = ? or status = ?) ");
		
		List<Object> list = new ArrayList<Object>();
		
		list.add(EnumPfdAccountStatus.START.getStatus());
		list.add(EnumPfdAccountStatus.CLOSE.getStatus());
		list.add(EnumPfdAccountStatus.STOP.getStatus());
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> findPfbCustomerInfo(String pfbCustomerInfoId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxCustomerInfo ");
		hql.append(" where customerInfoId = ? ");
		
		list.add(pfbCustomerInfoId);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<Object> findManagerPfbAccount(final String memberId, final Date startDate, final Date endDate) {
		
		List<Object> result = getHibernateTemplate().execute(

		        new HibernateCallback<List<Object> >() {
		        	
		        	public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {
		        		
		        		Query q = null;

		        		StringBuffer sql = new StringBuffer();
		        		sql.append(" select pfbc.customer_info_id, pfbc.company_name, pfbc.status ");	 	        			        		
		        		sql.append(" from pfbx_customer_info as pfbc ");
		        		sql.append("		left join adm_channel_account as admc ");
		        		sql.append("			on pfbc.customer_info_id = admc.account_id ");
		        		sql.append("		left join adm_manager_detail as admd ");
		        		sql.append("			on admc.member_id = admd.member_id ");        		
		        		
		        		if(StringUtils.isNotBlank(memberId)){ 
		        			sql.append(" where admc.channel_category = :channelCategory ");
		        			sql.append(" and admc.member_id = :memberId ");
		        			sql.append(" group by pfbc.customer_info_id ");
		        			
		        			log.info(sql.toString());
		        			q = session.createSQLQuery(sql.toString())
			        				.setString("channelCategory", EnumChannelCategory.PFB.getCategory())
			        				.setString("memberId", memberId);
		        		}
		        		else{
		        			
		        			sql.append(" group by pfbc.customer_info_id ");
		        			
		        			q = session.createSQLQuery(sql.toString());
		        					
		        		}
		        	            	
		            	return q.list();
		        	}
		        		
		        }
	        );
			
			return result;
	}
}
