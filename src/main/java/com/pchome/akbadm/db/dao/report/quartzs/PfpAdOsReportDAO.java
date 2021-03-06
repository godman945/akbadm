package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdOsReport;

public class PfpAdOsReportDAO extends BaseDAO<PfpAdOsReport, Integer> implements IPfpAdOsReportDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" pc.ad_pvclk_date, ");
						hql.append(" sum(pc.ad_pv), ");
						hql.append(" (case when (SUM(pc.ad_clk) - SUM(pc.ad_invalid_clk)) < 0 then 0 else (SUM(pc.ad_clk) - SUM(pc.ad_invalid_clk)) end ), ");
						hql.append(" (case when (sum(pc.ad_clk_price - pc.ad_invalid_clk_price)) < 0 then 0 else (sum(pc.ad_clk_price - pc.ad_invalid_clk_price)) end ), ");
						hql.append(" sum(pc.ad_invalid_clk), ");
						hql.append(" sum(pc.ad_invalid_clk_price), ");
						hql.append(" pc.customer_info_id, ");
						hql.append(" pc.ad_pvclk_device,  ");
						hql.append(" pc.ad_pvclk_os,  ");
						hql.append(" pc.ad_seq,  ");
						hql.append(" pc.ad_group_seq,  ");
						hql.append(" pc.ad_action_seq,  ");
						hql.append(" pc.template_product_seq,  ");
						hql.append(" pc.ad_price_type, ");
						hql.append(" sum(pc.ad_view), ");
						hql.append(" (select ac.ad_operating_rule from pfp_ad_action ac where ac.ad_action_seq = pc.ad_action_seq ) ad_operating_rule, ");
						hql.append(" sum(pc.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as pc");
						hql.append(" where 1 = 1");
						hql.append("   and pc.ad_pvclk_date ='" + reportDate + "'");
						hql.append("   and pc.ad_pvclk_device ='mobile'");
						hql.append(" group by pc.customer_info_id, pc.ad_pvclk_date, ad_pvclk_device, ad_pvclk_os,ad_price_type");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());

						return resultData;
					}
				}
		);

		return result;
	}

	@Override
    public void deleteReportDataByReportDate(String reportDate) throws Exception {
		String sql = "delete from PfpAdOsReport where adPvclkDate = '" + reportDate + "'";
		log.info(">>> sql = " + sql);
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdOsReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	@Override
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE pfp_ad_os_report r, ");
		sql.append(" ( ");
		sql.append(" SELECT ");
		sql.append(" c.customer_info_id, ");
		sql.append(" c.ad_action_seq,  ");
		sql.append(" c.ad_group_seq, ");
		sql.append(" c.ad_seq, ");
		sql.append(" convert_belong_date,  ");
		sql.append(" Sum(convert_count)convert_count,  ");
		sql.append(" Sum(convert_price)convert_price,  ");
		sql.append(" c.ad_pvclk_device, ");
		sql.append(" c.ad_pvclk_os, ");
		sql.append(" c.convert_trigger_type  ");
		sql.append(" FROM   pfp_code_convert_trans c  ");
		sql.append(" WHERE  1 = 1  ");
		sql.append(" AND c.convert_date >= :convertRangeDate ");
		sql.append(" AND c.convert_date <= :convertDate ");
		sql.append(" AND c.convert_trigger_type = 'CK'  ");
		sql.append(" GROUP  BY c.customer_info_id, ");
		sql.append(" c.convert_belong_date, ");
		sql.append(" c.ad_pvclk_device, ");
		sql.append(" c.ad_pvclk_os,		 ");	  
		sql.append(" c.convert_trigger_type,  ");
		sql.append(" c.convert_belong_date,  ");
		sql.append(" c.convert_seq  ");
		sql.append(" )a ");
		sql.append(" SET r.convert_count = a.convert_count, ");
		sql.append(" r.convert_price_count = a.convert_price, ");
		sql.append(" r.update_date = now() ");
		sql.append(" WHERE  1 = 1 ");
		sql.append(" AND a.convert_belong_date = r.ad_pvclk_date ");
		sql.append(" AND a.ad_action_seq = r.ad_action_seq ");
		sql.append(" AND a.ad_group_seq = r.ad_group_seq ");
		sql.append(" AND a.customer_info_id = r.customer_info_id ");
		sql.append(" AND a.ad_seq = r.ad_seq ");
		sql.append(" AND a.ad_pvclk_device = r.ad_pvclk_device ");
		sql.append(" AND a.ad_pvclk_os = r.ad_pvclk_os ");
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("convertDate", convertDate);
		query.setString("convertRangeDate", convertRangeDate);
		return query.executeUpdate();
	}
}

