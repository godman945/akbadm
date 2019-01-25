package com.pchome.akbadm.db.dao.pfbx.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUser;
import com.pchome.enumerate.pfbx.user.EnumUserPrivilege;

public class PfbxUserDAO extends BaseDAO<PfbxUser, String> implements IPfbxUserDAO{

	@SuppressWarnings("unchecked")
	public List<PfbxUser> findAllPfbxRootUser(String keyword) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxUser ");
		hql.append(" where privilegeId = ? ");
		
		list.add(EnumUserPrivilege.ROOT_USER.getPrivilegeId());
		
		if(StringUtils.isNotBlank(keyword)){
			
			hql.append(" and memberId like ?");
			list.add("%"+keyword+"%");
		}
		
		hql.append(" order by userId ");
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
