package com.pchome.akbadm.db.dao.pfbx.report.quartz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public class PfbAdUrlReportDAO extends BaseDAO<PfbxAdUrlReport, Integer> implements IPfbAdUrlReportDAO
{
	
	@SuppressWarnings("unchecked")
	public List<String> getErrorUrlByPfbId2(String pfbId , String startDate , String endDate , String searchUrl , String domain) throws Exception
	{
		List<String> listRS = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct ad_pvclk_url from pfbx_ad_url_report where 1=1 ");
		sql.append("and customer_info_id = '"+pfbId+"' ");
		sql.append("and ad_pvclk_url not like '%"+domain+"%' " );
		if(StringUtils.isNotBlank(startDate))
		{
			sql.append("and ad_pvclk_date >= '"+startDate+"' ");
		}
		if(StringUtils.isNotBlank(endDate))
		{
			sql.append("and ad_pvclk_date <= '"+endDate+"' ");
		}
		if(StringUtils.isNotBlank(searchUrl))
		{
			sql.append("and ad_pvclk_url like '%"+searchUrl+"%' ");
		}
		sql.append("order by ad_pvclk_date desc ");
		
		Query q = this.getSession().createSQLQuery(sql.toString());
		log.info("" + q.getQueryString());
		
		listRS = q.list();
		
		return listRS;
	}

	@SuppressWarnings("unchecked")
	public List<PfbAdUrlListVO> getErrorUrlByPfbId(String pfbId , String startDate , String endDate , String searchUrl) throws Exception
	{
		StringBuffer hql = new StringBuffer();

		hql.append("select ad_pvclk_date as detaildate , ");
		hql.append("ad_pvclk_url as detailurl , ");
		hql.append("COUNT(ad_pvclk_url) as detailcount ");
		hql.append("from pfbx_ad_url_report where 1=1 ");
		hql.append("and customer_info_id = :pfbid ");
		if(StringUtils.isNotBlank(startDate))
		{
			hql.append("and ad_pvclk_date >= :startDate ");
		}
		if(StringUtils.isNotBlank(endDate))
		{
			hql.append("and ad_pvclk_date <= :endDate ");
		}
		if(StringUtils.isNotBlank(searchUrl))
		{
			hql.append("and ad_pvclk_url = :searchUrl ");
		}
		hql.append("group by ad_pvclk_date , ad_pvclk_url ");
		hql.append("order by ad_pvclk_date desc ");
		
		Query q = this.getSession().createSQLQuery(hql.toString())
				.addScalar("detaildate", Hibernate.DATE)
				.addScalar("detailurl", Hibernate.STRING)
				.addScalar("detailcount", Hibernate.INTEGER)
				.setString("pfbid", pfbId)
				.setResultTransformer(Transformers.aliasToBean(PfbAdUrlListVO.class));
		if(StringUtils.isNotBlank(startDate))
		{
			q.setString("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate))
		{
			q.setString("endDate" , endDate);
		}
		if(StringUtils.isNotBlank(searchUrl))
		{
			q.setString("searchUrl", searchUrl);
		}

		List<PfbAdUrlListVO> list = q.list();

		return list;
	}
	
	/**
	 * 
	 * @param pfbId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> getListByPfbId2(String pfbcId , String domain) throws Exception
	{
		String sql = "";
		List<String> list = new ArrayList<String>();
		
		sql += "select distinct ad_pvclk_url from pfbx_ad_url_report where customer_info_id = '"+pfbcId+"' and ad_pvclk_url not like '%"+domain+"%' ";
		
		Query q = this.getSession().createSQLQuery(sql);
		log.info("" + q.getQueryString());
		
		list = q.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<PfbxAdUrlReport> getListByPfbId(String pfbId) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> con = new ArrayList<Object>();
		List<PfbxAdUrlReport> list = new ArrayList<PfbxAdUrlReport>();

		hql.append("from PfbxAdUrlReport where customerInfoId = ?");
		con.add(pfbId);

		list = this.getHibernateTemplate().find(hql.toString(), con.toArray());

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception
	{

		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>()
				{
					public List<Object> doInHibernate(Session session) throws HibernateException, SQLException
					{

						StringBuffer hql = new StringBuffer();
						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.pfbx_customer_info_id, ");
						hql.append(" r.ad_url ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfbx_customer_info_id is not null");
						hql.append(" and r.pfbx_customer_info_id !=''");
						hql.append(" group by r.pfbx_customer_info_id, r.ad_url");

						String sql = hql.toString();
						log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
						log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				}
				);

		return result;
	}

	public void deleteReportDataByReportDate(String reportDate) throws Exception
	{
		String sql = "delete from PfbxAdUrlReport where adPvclkDate = '" + reportDate + "'";
		Session session = getSession();
		session.createQuery(sql).executeUpdate();
		session.flush();
	}

	public void insertReportData(List<PfbxAdUrlReport> dataList) throws Exception
	{
		for (int i = 0; i < dataList.size(); i++)
		{
			this.save(dataList.get(i));
		}
	}
}
