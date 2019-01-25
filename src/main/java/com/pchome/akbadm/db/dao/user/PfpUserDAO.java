package com.pchome.akbadm.db.dao.user;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.enumerate.pfbx.user.EnumUserPrivilege;
import com.pchome.enumerate.user.EnumUserStatus;



public class PfpUserDAO extends BaseDAO<PfpUser,String> implements IPfpUserDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfpUser> getCustomerInfoUsers(String customerInfoId) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfpUser   ");
		hql.append(" where pfpCustomerInfo.customerInfoId = '"+customerInfoId+"' ");
		hql.append(" and status != '"+EnumUserStatus.DELETE.getStatus()+"' ");
		hql.append(" order by createDate ");
		
		return super.getHibernateTemplate().find(hql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public PfpUser getCustomerInfoUser(String userId) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfpUser  ");
		hql.append(" where userId = '"+userId+"' ");
		
		List<PfpUser> list = super.getHibernateTemplate().find(hql.toString());
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpUser> findPfpUser(String pfpCustomerInfoId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfpUser  ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		
		list.add(pfpCustomerInfoId);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	public Integer deletePfpUser(String userId) {
		
		StringBuffer hql = new StringBuffer();
		
	    hql.append("delete from PfpUser ");
	    hql.append("where userId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), userId);
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpUser> findOpenAccountUser(String pfpCustomerInfoId)  throws Exception{
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfpUser  ");
		hql.append(" where pfpCustomerInfo.customerInfoId  = ? ");
		hql.append(" and privilegeId  = ? ");
		hql.append(" order by userId asc ");
		
		list.add(pfpCustomerInfoId);
		list.add(EnumUserPrivilege.ROOT_USER.getPrivilegeId());
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
