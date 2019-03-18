package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserGroup;

public class PfbxUserGroupDAO extends BaseDAO<PfbxUserGroup, String> implements IPfbxUserGroupDAO {
    @SuppressWarnings("unchecked")
    public List<PfbxUserGroup> selectPfbxUserGroupByStatus(String status) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxUserGroup ");
        hql.append("where pfbxCustomerInfo.status = ? ");

        return (List<PfbxUserGroup>) this.getHibernateTemplate().find(hql.toString(), status);
    }
    
    @SuppressWarnings("unchecked")
	public PfbxUserGroup getPfbxUserGroupById(String id) throws Exception {
    	
    	 StringBuffer hql = new StringBuffer();
         hql.append("from PfbxUserGroup ");
         hql.append("where GId = ? ");
         
         PfbxUserGroup pfbxUserGroup=null;
         
         if(this.getHibernateTemplate().find(hql.toString(), id).size()>0){
         
         pfbxUserGroup=(PfbxUserGroup) this.getHibernateTemplate().find(hql.toString(), id).get(0);
         
         }

         return pfbxUserGroup;
	}
}
