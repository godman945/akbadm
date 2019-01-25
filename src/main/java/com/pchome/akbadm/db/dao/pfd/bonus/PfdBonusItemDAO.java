package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItem;

public class PfdBonusItemDAO extends BaseDAO<PfdBonusItem, Integer> implements IPfdBonusItemDAO{

	@SuppressWarnings("unchecked")
	public List<PfdBonusItem> findPfdBonusItem(int bonusItemId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusItem ");
		hql.append(" where bonusItemId = ? ");
		
		list.add(bonusItemId);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
