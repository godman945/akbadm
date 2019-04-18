package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPersonal;

public class PfbxPersonalDAO extends BaseDAO<PfbxPersonal, Integer> implements IPfbxPersonalDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfbxPersonal> getListByCustomerId(String customerId)
	{
		StringBuffer hql = new StringBuffer();
		List<Object> Condition = new ArrayList<Object>();
		
		hql.append("from PfbxPersonal where pfbxCustomerInfo.customerInfoId = ? and deleteFlag = ?");
		
		Condition.add(customerId);
		Condition.add("0");
		
		return (List<PfbxPersonal>) super.getHibernateTemplate().find(hql.toString() , Condition.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PfbxPersonal getPfbxMainUsePersonal(String pfbId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxPersonal where 1 = 1 ");
		hql.append(" and pfbxCustomerInfo.customerInfoId = ? ");
		hql.append(" and mainUse = 'Y' ");
		hql.append(" and deleteFlag  = 0 ");
		
		list.add(pfbId);
		
		List<PfbxPersonal> personalList=null;
		PfbxPersonal pfbxPersonal=null;
		personalList=(List<PfbxPersonal>) super.getHibernateTemplate().find(hql.toString(),list.toArray());
		
		if(personalList.size()>0){
			pfbxPersonal=personalList.get(0);
		}
		
		return pfbxPersonal;
	}

}
