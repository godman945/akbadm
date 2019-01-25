package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusSet;

public class AdmBonusSetDAO extends BaseDAO<AdmBonusSet, Integer> implements IAdmBonusSetDAO{

	@SuppressWarnings("unchecked")
	public List<AdmBonusSet> findLastAdmBonusSet(Date startDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from AdmBonusSet ");
		hql.append(" where startDate <= ? ");
		hql.append(" order by id ");
		
		return super.getHibernateTemplate().find(hql.toString(), startDate);
	}
}
