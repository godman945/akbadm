package com.pchome.akbadm.db.dao.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmChannelAccount;

public class AdmChannelAccountDAO extends BaseDAO<AdmChannelAccount, Integer> implements IAdmChannelAccountDAO{
	
	@SuppressWarnings("unchecked")
	public List<AdmChannelAccount> findAdmChannelAccount(String memberId, String channelCategory){
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from AdmChannelAccount where memberId = ? ");
		hql.append(" and channelCategory = ? ");
		
		list.add(memberId);	
		list.add(channelCategory);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	public Integer deleteAdmChannelAccount(String memberId, String channelCategory) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
	    hql.append(" delete from AdmChannelAccount ");
	    hql.append(" where memberId = ? ");
	    hql.append(" and channelCategory = ? ");
	    
	    list.add(memberId);	
		list.add(channelCategory);

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), list.toArray());
	}
}
