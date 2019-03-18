package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPermission;

public class PfbxPermissionDAO extends BaseDAO<PfbxPermission,String> implements IPfbxPermissionDAO {

    public void savePfbxPermissionList(List<PfbxPermission> PfbxPermissionList) throws Exception {
	for (PfbxPermission pfbxPermission : PfbxPermissionList) {
	    this.saveOrUpdate(pfbxPermission);
	}
    }
    
    @SuppressWarnings("unchecked")
    public List<Object> findPfbxPermissionListByUrl(String pfbxApplyUrl) throws Exception{
	StringBuffer sb = new StringBuffer();
	sb.append(" select * ");
	sb.append(" from pfbx_permission a");
	sb.append(" where a.content like '%"+pfbxApplyUrl+"%' and type =1");
	String sql = sb.toString();
	log.info(">>> sql = " + sql);
	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
	return query.list();
    }
}
