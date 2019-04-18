package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusDetailReport;

public class AdmBonusDetailReportDAO extends BaseDAO<AdmBonusDetailReport, Integer> implements IAdmBonusDetailReportDAO{

	@SuppressWarnings("unchecked")
	public List<AdmBonusDetailReport> findAdmBonusDetailReport(Date reportDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from AdmBonusDetailReport ");
		hql.append(" where reportDate = ? ");
		hql.append(" order by id desc ");
		
		return (List<AdmBonusDetailReport>) super.getHibernateTemplate().find(hql.toString(), reportDate);
	}
	
	public Integer deleteAdmBonusDetailReport(Date deleteDate) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" delete from AdmBonusDetailReport ");
		hql.append(" where reportDate >= ? ");
		
		return super.getHibernateTemplate().bulkUpdate(hql.toString(), deleteDate);
	}
	
}
