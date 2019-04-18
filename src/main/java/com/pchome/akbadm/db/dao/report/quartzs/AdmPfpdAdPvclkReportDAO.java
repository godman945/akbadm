package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmPfpdAdPvclkReport;

public class AdmPfpdAdPvclkReportDAO extends BaseDAO<AdmPfpdAdPvclkReport, Integer> implements IAdmPfpdAdPvclkReportDAO {

	@SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.pfbx_customer_info_id, ");
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.ad_pvclk_date, ");
						hql.append(" '全播', ");
						hql.append(" '', ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.time_code, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r ");
						hql.append(" join pfbx_customer_info pfb ");
						hql.append(" on r.pfbx_customer_info_id = pfb.customer_info_id ");
						hql.append(" where 1 = 1 ");
						hql.append(" and pfb.play_type = '0' ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.pfbx_customer_info_id, r.pfd_customer_info_id, r.ad_pvclk_date, r.time_code, r.ad_pvclk_device,ad_price_type ");

						String sql = hql.toString();
						//log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
						//log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				}
		);

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> prepareReportData2(final String reportDate) throws Exception {

		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.pfbx_customer_info_id, ");
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.ad_pvclk_date, ");
						hql.append(" aurl.url_name, ");
						hql.append(" aurl.url, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.time_code, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r ");
						hql.append(" join pfbx_customer_info pfb ");
						hql.append(" on r.pfbx_customer_info_id = pfb.customer_info_id ");
						hql.append(" join pfbx_allow_url aurl ");
						hql.append(" on r.pfbx_customer_info_id = aurl.customer_info_id ");
						hql.append(" where 1 = 1 ");
						hql.append(" and (r.ad_url like CONCAT('%.',aurl.root_domain,'%') or  r.ad_url like CONCAT(aurl.root_domain,'%') ) ");
						//hql.append(" and (r.ad_url like '%.' || aurl.root_domain || '%' or  r.ad_url like aurl.root_domain || '%') ");
						hql.append(" and pfb.play_type = '1' ");
						hql.append(" and aurl.url_status = '2' ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.pfbx_customer_info_id, r.pfd_customer_info_id, r.ad_pvclk_date, r.time_code, aurl.url, aurl.url_name, r.ad_pvclk_device,ad_price_type ");

						String sql = hql.toString();
						//log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
						//log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				}
		);

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> prepareReportData3(final String reportDate) throws Exception {

		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.pfbx_customer_info_id, ");
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.ad_pvclk_date, ");
						hql.append(" '網頁快取及其他', ");
						hql.append(" '', ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.time_code, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r ");
						hql.append(" join pfbx_customer_info pfb ");
						hql.append(" on r.pfbx_customer_info_id = pfb.customer_info_id ");
						
						hql.append(" where 1 = 1 ");
						hql.append(" and pfb.play_type = '1' ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						
						hql.append(" and CONCAT(r.pfbx_customer_info_id,r.ad_url) not in( ");
				        hql.append(" (select   CONCAT(aurl.customer_info_id,r.ad_url) from  pfbx_allow_url aurl  ");
				        hql.append(" where 1=1 and aurl.delete_flag = '0' and aurl.url_status = '2' ");
				        hql.append(" and (r.ad_url like CONCAT('%.',aurl.root_domain,'%') or  r.ad_url like CONCAT(aurl.root_domain,'%') ) ");
				        hql.append(" )) ");
						
						hql.append(" group by r.customer_info_id, r.pfbx_customer_info_id, r.pfd_customer_info_id, r.ad_pvclk_date, r.time_code, r.ad_pvclk_device,ad_price_type ");

						String sql = hql.toString();
						//log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
						//log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				}
		);

		return result;
	}
	
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		String sql = "delete from AdmPfpdAdPvclkReport where adPvclkDate = '" + reportDate + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}
	
	public void insertReportData(List<AdmPfpdAdPvclkReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}
	
	
	@Override
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE adm_pfpd_ad_pvclk_report r,  ");
		sql.append(" (SELECT  ");
		sql.append(" c.customer_info_id,  ");
		sql.append(" c.pfd_customer_info_id,  ");
		sql.append(" c.pfbx_customer_info_id,  ");
		sql.append(" c.convert_belong_date,  ");
		sql.append(" c.time_code,  ");
		sql.append(" Sum(convert_count)convert_count, "); 
		sql.append(" Sum(convert_price)convert_price, "); 
		sql.append(" c.ad_pvclk_device, "); 
		sql.append(" c.ad_pvclk_os, "); 
		sql.append(" c.convert_trigger_type "); 
		sql.append(" FROM   pfp_code_convert_trans c "); 
		sql.append(" WHERE  1 = 1 "); 
		sql.append(" AND c.convert_date >= :convertRangeDate "); 
		sql.append(" AND c.convert_date <= :convertDate "); 
		sql.append(" AND c.convert_trigger_type = 'CK' "); 
		sql.append(" GROUP  BY c.customer_info_id, "); 
		sql.append(" c.convert_belong_date, "); 
		sql.append(" c.ad_pvclk_device, "); 
		sql.append(" c.ad_pvclk_os, "); 
		sql.append(" c.convert_trigger_type, "); 
		sql.append(" c.convert_belong_date, c.convert_seq)a "); 
		sql.append(" SET    r.convert_count = a.convert_count, "); 
		sql.append(" r.convert_price_count = a.convert_price, "); 
		sql.append(" r.update_time = Now() "); 
		sql.append(" WHERE  1 = 1 "); 
		sql.append(" AND a.customer_info_id = r.customer_info_id "); 
		sql.append(" AND a.pfbx_customer_info_id = r.pfbx_customer_info_id "); 
		sql.append(" AND a.pfd_customer_info_id = r.pfd_customer_info_id "); 
		sql.append(" AND a.convert_belong_date = r.ad_pvclk_date "); 
		sql.append(" AND a.ad_pvclk_device = r.ad_pvclk_device "); 
		sql.append(" AND a.time_code = r.time_code "); 
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("convertDate", convertDate);
		query.setString("convertRangeDate", convertRangeDate);
		return query.executeUpdate();
	}
	
}
