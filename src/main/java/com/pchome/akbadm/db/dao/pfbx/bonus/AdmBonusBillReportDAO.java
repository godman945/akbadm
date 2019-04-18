package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusBillReport;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxInComeReportVo;

public class AdmBonusBillReportDAO extends BaseDAO<AdmBonusBillReport, Integer> implements IAdmBonusBillReportDAO
{
	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxInComeReportVo> getListBySEDate2(Date sdate, Date edate)
	{
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ");
		hql.append(" b.id, ");
		hql.append(" b.report_date reportdate, ");
		hql.append(" (a.ad_clk_price - a.ad_invalid_clk_price) - (IFNULL((SELECT ");
		hql.append(" SUM(loss_cost) ");
		hql.append(" FROM adm_trans_loss ");
		hql.append(" WHERE trans_date = b.report_date), 0)) adclkprice, ");
		hql.append(" b.sys_clk_price sysclkprice, ");
		hql.append(" b.income, ");
		hql.append(" b.expense, ");
		hql.append(" total ");
		hql.append(" FROM (SELECT ");
		hql.append(" SUM(ad_clk_price) ad_clk_price, ");
		hql.append(" SUM(ad_invalid_clk_price) ad_invalid_clk_price, ");
		hql.append(" ad_pvclk_date ");
		hql.append(" FROM (SELECT ");
		hql.append(" FLOOR(SUM(ad_clk_price)) ad_clk_price, ");
		hql.append(" SUM(ad_invalid_clk_price) ad_invalid_clk_price, ");
		hql.append(" ad_pvclk_date ");
		hql.append(" FROM pfp_ad_pvclk ");
		hql.append(" WHERE ad_pvclk_date BETWEEN :startdate AND :enddate ");
		hql.append(" GROUP BY customer_info_id, ");
		hql.append(" ad_pvclk_date ");
		hql.append(" ORDER BY pfp_ad_pvclk.ad_pvclk_date) a ");
		hql.append(" GROUP BY ad_pvclk_date) a, ");
		hql.append(" adm_bonus_bill_report b ");
		hql.append(" WHERE report_date BETWEEN :startdate AND :enddate ");
		hql.append(" AND a.ad_pvclk_date = b.report_date ");
		
		
		
		
		
		
//		hql.append("SELECT id, ");
//		hql.append("report_date as reportdate, ");
//		hql.append("ROUND((select sum(ad_clk_price - ad_invalid_clk_price) ");
////		hql.append("(select  FLOOR(sum(ad_clk_price)) - sum(ad_invalid_clk_price) ) ");
//		hql.append("- ");
//		hql.append("(IFNULL((select SUM(loss_cost) from adm_trans_loss where trans_date = adm_bonus_bill_report.report_date),0)) from pfp_ad_pvclk where ad_pvclk_date = adm_bonus_bill_report.report_date),0) as adclkprice, ");
//		hql.append("ROUND(sys_clk_price,0) as sysclkprice, ");
//		hql.append("income, ");
//		hql.append("expense, ");
//		hql.append("total ");
//		hql.append("FROM adm_bonus_bill_report WHERE report_date between :startdate and :enddate");

		/*
		 * private int id; 
		 * private Date reportdate; //報表日期 
		 * private Double adclkprice=0.0d; //PFP花費 
		 * private float sysclkprice; //驗證花費 
		 * private float income; //收入 
		 * private float expense; //支出
		 * private float total; //盈虧
		 */

		String strHQL = hql.toString();

		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();

		Query q = session.createSQLQuery(strHQL)
				.addScalar("id", StandardBasicTypes.INTEGER)
				.addScalar("reportdate", StandardBasicTypes.DATE)
				.addScalar("adclkprice", StandardBasicTypes.FLOAT)
				.addScalar("sysclkprice", StandardBasicTypes.FLOAT)
				.addScalar("income", StandardBasicTypes.FLOAT)
				.addScalar("expense", StandardBasicTypes.FLOAT)
				.addScalar("total", StandardBasicTypes.FLOAT)
				.setResultTransformer(Transformers.aliasToBean(PfbxInComeReportVo.class));
		q.setDate("startdate", sdate);
		q.setDate("enddate", edate);

		log.info(">>> strHQL_query = " + q.getQueryString());

		List<PfbxInComeReportVo> pfbxInComeReportVoList = q.list();

		return pfbxInComeReportVoList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AdmBonusBillReport> getListBySEDate(Date sdate, Date edate)
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();

		hql.append("from AdmBonusBillReport where 1=1 ");
		if (StringUtils.isNotBlank(sdate.toString()))
		{
			hql.append("and reportDate >= ? ");
			pos.add(sdate);
		}
		if (StringUtils.isNotBlank(edate.toString()))
		{
			hql.append("and reportDate <= ? ");
			pos.add(edate);
		}
		hql.append("order by reportDate desc ");

		return (List<AdmBonusBillReport>) super.getHibernateTemplate().find(hql.toString(), pos.toArray());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map> getMapsBySEDate(String sdate, String edate)
	{
		StringBuffer hql = new StringBuffer();

		hql.append("select * from adm_bonus_bill_report where 1=1 ");
		if (StringUtils.isNotBlank(sdate))
		{
			hql.append("and report_Date >= :sdate ");
		}
		if (StringUtils.isNotBlank(edate))
		{
			hql.append("and report_Date <= :edate ");
		}
		hql.append("order by report_date desc ");

		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		if (StringUtils.isNotBlank(sdate))
		{
			q.setString("sdate", sdate);
		}
		if (StringUtils.isNotBlank(edate))
		{
			q.setString("edate", edate);
		}
		List<Map> maps = q.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();

		return maps;
	}

	public Integer deleteAdmBonusBillReport(Date deleteDate)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from AdmBonusBillReport ");
		hql.append(" where reportDate >= ? ");

		return super.getHibernateTemplate().bulkUpdate(hql.toString(), deleteDate);
	}
}
