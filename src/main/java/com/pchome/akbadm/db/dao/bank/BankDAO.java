package com.pchome.akbadm.db.dao.bank;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.Bank;

public class BankDAO extends BaseDAO<Bank, Integer>implements IBankDAO{

	@SuppressWarnings("unchecked")
	public List<Bank> findBank() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from Bank ");
		hql.append(" order by id asc ");
		
		return super.getHibernateTemplate().find(hql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Bank> findMainBank(String code) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from Bank ");
		hql.append(" where bankCode = ? ");
		hql.append(" order by id asc ");
		
		list.add(code);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
