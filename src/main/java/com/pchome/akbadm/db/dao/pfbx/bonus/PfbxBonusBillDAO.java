package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusBill;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;

public class PfbxBonusBillDAO extends BaseDAO<PfbxBonusBill, Integer> implements IPfbxBonusBillDAO
{
	@SuppressWarnings("unchecked")
	public List<PfbxBonusBill> listByKeyWord(String keyword, String category, String status)
	{

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT b.*  ");
		sql.append(" 	FROM	pfbx_bonus_bill b,  ");
		sql.append(" 			pfbx_customer_info c  ");
		sql.append(" 	WHERE  1 = 1  ");
		sql.append(" 		AND b.pfb_id = c.customer_info_id  ");
		sql.append(" 		AND c.status IN( "+EnumPfbAccountStatus.START.getStatus()+", "+EnumPfbAccountStatus.CLOSE.getStatus()+", "+EnumPfbAccountStatus.STOP.getStatus()+" ) ");
		if (StringUtils.isNotBlank(category)){
			sql.append("and c.category = :category ");
		}
		if (StringUtils.isNotBlank(status)){
			sql.append("and b.pfb_bonus_set_status  = :pfbBonusSetStatus ");
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		if (StringUtils.isNotBlank(category)){
			query.setParameter("category", category);
		}
		if (StringUtils.isNotBlank(status)){
			query.setParameter("pfbBonusSetStatus", status);
		}
		
		List<PfbxBonusBill> pfbxBonusBillList = new ArrayList<>();
		List<Object> list = query.list();
		for (Object object : list) {
			Object[] objArray = (Object[]) object;
			PfbxBonusBill pfbxBonusBill = new PfbxBonusBill();
			pfbxBonusBill.setId((int)objArray[0]);
			pfbxBonusBill.setPfbId(objArray[1].toString());
			pfbxBonusBill.setPfbBonusSetStatus(objArray[2].toString());
			pfbxBonusBill.setLastApplyMoney((float)objArray[3]);
			pfbxBonusBill.setApplyMoney((float)objArray[4]);
			pfbxBonusBill.setTotalApplyMoney((float)objArray[5]);
			pfbxBonusBill.setMinLimit((float)objArray[6]);
			pfbxBonusBill.setBillRemain((float)objArray[7]);
			pfbxBonusBill.setUpdateDate((Date)objArray[8]);
			pfbxBonusBill.setCreateDate((Date)objArray[9]);
			pfbxBonusBillList.add(pfbxBonusBill);
		}
//		List<Object> pos = new ArrayList<Object>();
//		StringBuffer hql = new StringBuffer();
//		hql.append("from PfbxBonusBill,PfbxCustomerInfo where 1=1 ");
//		hql.append("and PfbxBonusBill.pfbId = PfbxCustomerInfo.customerInfoId ");
//		hql.append("and PfbxCustomerInfo.status in (?,?,?) ");
//		pos.add(EnumPfbAccountStatus.START.getStatus());
//		pos.add(EnumPfbAccountStatus.CLOSE.getStatus());
//		pos.add(EnumPfbAccountStatus.STOP.getStatus());
		
//		if (StringUtils.isNotBlank(category))
//		{
//			hql.append("and pfbxCustomerInfo.category = ? ");
//			pos.add(category);
//		}
//		if (StringUtils.isNotBlank(status))
//		{
//			hql.append("and pfbBonusSetStatus = ? ");
//			pos.add(status);
//		}
		/*if (StringUtils.isNotBlank(keyword))
		{
			hql.append("and ( ");
			hql.append("pfbxCustomerInfo.customerInfoId like ? or ");
			hql.append("pfbxCustomerInfo.taxId like ? or ");
			hql.append("pfbxCustomerInfo.memberId like ? ");
			hql.append(") ");
			pos.add("%" + keyword + "%");
			pos.add("%" + keyword + "%");
			pos.add("%" + keyword + "%");
		}*/
		return pfbxBonusBillList;
//		return super.getHibernateTemplate().find(hql.toString(), pos.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfbxBonusBill> listByPfbId(String pfbId)
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("from PfbxBonusBill where pfbId = ? ");
		pos.add(pfbId);

		return (List<PfbxBonusBill>) super.getHibernateTemplate().find(hql.toString(), pos.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfbxBonusBill> findPfbxBonusBills(String pfbId)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusBill ");
		hql.append(" where 1 = 1 ");

		if (StringUtils.isNotBlank(pfbId))
		{
			hql.append(" and pfbId = ? ");
		}

		return (List<PfbxBonusBill>) super.getHibernateTemplate().find(hql.toString(), pfbId);
	}

}
