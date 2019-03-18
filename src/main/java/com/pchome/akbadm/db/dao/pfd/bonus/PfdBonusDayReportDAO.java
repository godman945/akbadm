package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusDayReport;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfdBonusDayReportDAO extends BaseDAO<PfdBonusDayReport, Integer> implements IPfdBonusDayReportDAO{
	
	public List<Object[]> getListObjPFDDetailByReportDate(Date reportDate)
	{
		StringBuffer hql = new StringBuffer();

		hql.append("select ");
		hql.append("pfd_id, ");
		hql.append("save_clk_price, ");
		hql.append("free_clk_price, ");
		hql.append("postpaid_clk_price ");
		hql.append("from pfd_bonus_day_report where report_date = :reportDate order by pfd_id");

		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		q.setDate("reportDate", reportDate);
		
		List<Object[]> list = q.list();

		return list;
	}
	 
	public float findPfdSaveMonthBonus(Date reportDate) {
		
		StringBuffer hql = new StringBuffer();
        hql.append(" select sum(saveBouns) ");
        hql.append(" from PfdBonusDayReport ");
        hql.append(" where reportDate = :reportDate ");
        
        Double result = (Double) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setDate("reportDate", reportDate)
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;		
	}
	
   public float findPfdPaidMonthBonus(Date reportDate) {
		
		StringBuffer hql = new StringBuffer();
        hql.append(" select sum(postpaidBonus) ");
        hql.append(" from PfdBonusDayReport ");
        hql.append(" where reportDate = :reportDate ");
        
        Double result = (Double) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setDate("reportDate", reportDate)
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;		
	}
	
	public Integer deletePfdBonusDayReport(Date deleteDate) {
		StringBuffer hql = new StringBuffer();
		
		hql.append(" delete from PfdBonusDayReport ");
		hql.append(" where reportDate >= ? ");
		
		return super.getHibernateTemplate().bulkUpdate(hql.toString(), deleteDate);
	}

	@Override
	public float getPfdSavePrice(String pfdId, String startDate, String endDate) {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT sum(saveClkPrice) FROM PfdBonusDayReport  ");
		hql.append(" WHERE pfdCustomerInfo.customerInfoId = :pfdId and  ");
		hql.append(" reportDate between :startDate and :endDate ");
		
		 Double result = (Double) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
	                .createQuery(hql.toString())
	                .setString("pfdId", pfdId)
	                .setString("startDate", startDate)
	                .setString("endDate", endDate)
	                .uniqueResult();
	        return result != null ? result.floatValue() : 0f;	
	}

	@Override
	public float getPfdPaidPrice(String pfdId, String startDate, String endDate) {
		StringBuffer hql = new StringBuffer();
		
		hql.append(" SELECT sum(postpaidClkPrice) FROM PfdBonusDayReport  ");
		hql.append(" WHERE pfdCustomerInfo.customerInfoId = :pfdId and  ");
		hql.append(" reportDate between :startDate and :endDate ");
		
		 Double result = (Double) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
	                .createQuery(hql.toString())
	                .setString("pfdId", pfdId)
	                .setString("startDate", startDate)
	                .setString("endDate", endDate)
	                .uniqueResult();
	        return result != null ? result.floatValue() : 0f;	
	}

	
}
