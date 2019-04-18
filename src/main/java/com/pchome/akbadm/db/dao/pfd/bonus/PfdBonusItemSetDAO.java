package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;

public class PfdBonusItemSetDAO extends BaseDAO <PfdBonusItemSet, Integer> implements IPfdBonusItemSetDAO{

	@SuppressWarnings("unchecked")
	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusItemSet ");
		hql.append(" where pfdContract.pfdContractId = ? ");
		hql.append(" order by id ");
		
		list.add(contractId);
		
		return (List<PfdBonusItemSet>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId, String pfdBonusType) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusItemSet ");
		hql.append(" where pfdContract.pfdContractId = ? ");
		hql.append(" and bonusType = ? ");
		hql.append(" order by id ");
		
		list.add(contractId);
		list.add(pfdBonusType);
		
		
		return (List<PfdBonusItemSet>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	public Integer deletePfdBonusItemSet(String contractId, String pfdBonusType) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" delete from PfdBonusItemSet ");
		hql.append(" where pfdContract.pfdContractId = ? ");
		hql.append(" and bonusType = ? ");
		
		list.add(contractId);
		list.add(pfdBonusType);
		
		
		return super.getHibernateTemplate().bulkUpdate(hql.toString(), list.toArray());
	}
}
