package com.pchome.akbadm.db.dao.user;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpUserMemberRef;

public class PfpUserMemberRefDAO extends BaseDAO<PfpUserMemberRef,String> implements IPfpUserMemberRefDAO{

	@SuppressWarnings("unchecked")
	public PfpUserMemberRef getUserMemberRef(String userId) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfpUserMemberRef  ");
		hql.append(" where id.userId = '"+userId+"' ");
		
		List<PfpUserMemberRef> list = (List<PfpUserMemberRef>) super.getHibernateTemplate().find(hql.toString());
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	public Integer deletePfpUserMemberRef(String pcId) {
		
		StringBuffer hql = new StringBuffer();
		
	    hql.append("delete from PfpUserMemberRef ");
	    hql.append("where id.memberId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), pcId);
	}
	
}
