package com.pchome.akbadm.db.dao.free;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeGift;
import com.pchome.enumerate.freeAction.EnumGiftSnoUsed;

public class AdmFreeGiftDAO extends BaseDAO<AdmFreeGift, Integer> implements IAdmFreeGiftDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFreeGift> findAdmFreeGiftSno(String freeActionId, String customerInfoId, Date openDate){
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

        hql.append("from AdmFreeGift ");
        hql.append("where admFreeAction.actionId = ? ");
        hql.append("and customerInfoId = ? ");
        hql.append("and openDate = ? ");
        hql.append("and giftSnoStatus = ? ");

        list.add(freeActionId);
        list.add(customerInfoId);
        list.add(openDate);
        list.add(EnumGiftSnoUsed.YES.getStatus());

        return this.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFreeGift> findAdmFreeGift(String actionId){

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

        hql.append(" from AdmFreeGift ");
        hql.append(" where admFreeAction.actionId = ?  ");

        list.add(actionId);

        return this.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFreeGift> findAdmFreeGiftToBeUsed(String actionId){

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

        hql.append(" from AdmFreeGift ");
        hql.append(" where customerInfoId is not null ");
        hql.append(" and admFreeAction.actionId = ? ");

        list.add(actionId);

        return this.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
