package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;

public class PfbxBonusSetDAO extends BaseDAO<PfbxBonusSet, Integer> implements IPfbxBonusSetDAO
{

	@SuppressWarnings("unchecked")
	public List<PfbxBonusSet> findPfbxBonusSets(String pfbId, Date startDate)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusSet ");
		hql.append(" where pfbId = ? ");
		//hql.append(" and startDate <= ? ");
		hql.append(" order by id desc ");

		
		return (List<PfbxBonusSet>) super.getHibernateTemplate().find(hql.toString(), pfbId);
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxBonusSet> getListOrderByStartDate()
	{
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfbxBonusSet order by startDate desc , createDate desc");

		return (List<PfbxBonusSet>) super.getHibernateTemplate().find(hql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxBonusSet> getListByPfbIdOrderSDate(String pfbId)
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();
		
		hql.append(" from PfbxBonusSet where pfbId = ? order by startDate desc , createDate desc");
		pos.add(pfbId);
		
		return (List<PfbxBonusSet>) super.getHibernateTemplate().find(hql.toString() , pos.toArray());

	}




}
