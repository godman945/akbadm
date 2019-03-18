package com.pchome.akbadm.db.dao.pfbx;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPosition;

public class PfbxPositionDAO extends BaseDAO<PfbxPosition,String> implements IPfbxPositionDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<PfbxPosition> selectPfbxPositionByDeleteFlag(int deleteFlag) {
        String hql = "from PfbxPosition where deleteFlag = ?";
        return (List<PfbxPosition>) this.getHibernateTemplate().find(hql, deleteFlag);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<PfbxPosition> findPositionByPfbCustomerId(String position, String pfbCustomerId){
    	StringBuffer hql =  new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfbxPosition ");
		hql.append(" where PId = ? ");
        hql.append(" and pfbxCustomerInfo.customerInfoId = ? ");

		list.add(position);
        list.add(pfbCustomerId);

		return (List<PfbxPosition>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
    }
}
