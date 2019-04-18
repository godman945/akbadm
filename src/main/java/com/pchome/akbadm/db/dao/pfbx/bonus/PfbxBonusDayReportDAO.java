package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusDayReport;

public class PfbxBonusDayReportDAO extends BaseDAO<PfbxBonusDayReport,Integer> implements IPfbxBonusDayReportDAO{
	
	@SuppressWarnings("unchecked")
	public List<Object> findPfbxBonusDayReport(Date startDate, Date endDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(saveCharge), sum(freeCharge), sum(postpaidCharge), ");
		hql.append("  sum(saveBonus), sum(freeBonus), sum(postpaidBonus), ");
		hql.append("  sum(totalBonus) ");
		hql.append(" from PfbxBonusDayReport ");
		hql.append(" where reportDate between ? and ? ");
		
		return (List<Object>) super.getHibernateTemplate().find(hql.toString(), startDate, endDate);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findPfbxIdBonusDayReport(String pfbId,Date startDate, Date endDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(saveCharge), sum(freeCharge), sum(postpaidCharge), ");
		hql.append("  sum(saveBonus), sum(freeBonus), sum(postpaidBonus), ");
		hql.append("  sum(totalBonus), sum(pfbClkPrice) ");
		hql.append(" from PfbxBonusDayReport ");
		hql.append(" where pfbxCustomerInfo.customerInfoId =? and reportDate between ? and ? ");
		
		return (List<Object>) super.getHibernateTemplate().find(hql.toString(), pfbId , startDate, endDate);
	}
	
	public Integer deletePfbxBonusDayReport(Date deleteDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" delete from PfbxBonusDayReport ");
		hql.append(" where reportDate >= ? ");
		
		return super.getHibernateTemplate().bulkUpdate(hql.toString(), deleteDate);
	}

	
}
