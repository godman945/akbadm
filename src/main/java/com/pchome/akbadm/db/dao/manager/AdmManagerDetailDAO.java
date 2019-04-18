package com.pchome.akbadm.db.dao.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.enumerate.manager.EnumManagerStatus;

public class AdmManagerDetailDAO extends BaseDAO<AdmManagerDetail, Integer> implements IAdmManagerDetailDAO{

	@SuppressWarnings("unchecked")
	public List<AdmManagerDetail> findAdmManagerDetail(String memberId, String channelCategory) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmManagerDetail where memberId = ? ");
		hql.append(" and managerChannel = ? ");
		hql.append(" and managerStatus != ? ");
		
		list.add(memberId);
		list.add(channelCategory);
		list.add(EnumManagerStatus.DELETE.getStatus());
		
		return (List<AdmManagerDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmManagerDetail> findExistAdmManagerDetail(String memberId, String channelCategory) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmManagerDetail where memberId = ? ");
		hql.append(" and managerChannel = ? ");
		hql.append(" and managerStatus != ? ");
		
		list.add(memberId);
		list.add(channelCategory);
		list.add(EnumManagerStatus.DELETE.getStatus());
		
		return (List<AdmManagerDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmManagerDetail> findLoginAdmManagerDetail(String memberId, String channelCategory) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmManagerDetail where memberId = ? ");
		hql.append(" and managerChannel = ? ");
		hql.append(" and managerStatus = ? ");
		
		list.add(memberId);
		list.add(channelCategory);
		list.add(EnumManagerStatus.START.getStatus());
		
		return (List<AdmManagerDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmManagerDetail> findAdmManagerDetails(String system) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmManagerDetail ");
		hql.append(" where managerStatus != ? ");
		
		list.add(EnumManagerStatus.DELETE.getStatus());
		
		if(StringUtils.isNotBlank(system)){
			hql.append(" and managerChannel = ? ");
			list.add(system);
		}
		
		hql.append(" order by createDate desc ");
		
		return (List<AdmManagerDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmManagerDetail> findAdmManagerDetail(int managerId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmManagerDetail ");		
		hql.append(" where id = ? ");
		hql.append(" order by createDate desc ");
		
		list.add(managerId);
		
		
		return (List<AdmManagerDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	public Integer deleteAdmManagerDetail(String memberId, String channelCategory) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
	    hql.append(" delete from AdmManagerDetail ");
	    hql.append(" where memberId = ? ");
	    hql.append(" and managerChannel  = ? ");
	    
	    list.add(memberId);	
		list.add(channelCategory);

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), list.toArray());
	}
}
