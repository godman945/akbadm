package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdAdReport;

public class PfdAdReportDAO extends BaseDAO<PfdAdReport, Integer> implements IPfdAdReportDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" (sum(r.ad_clk)-sum(r.ad_invalid_clk)), ");
						hql.append(" (sum(r.ad_clk_price)-sum(r.ad_invalid_clk_price)), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.pfd_user_id, ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_type, ");
						hql.append(" r.ad_seq, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_group_seq, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.template_product_seq, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" (select ac.ad_operating_rule from pfp_ad_action ac where ac.ad_action_seq = r.ad_action_seq) ad_operating_rule, ");
						hql.append(" SUM(r.ad_vpv), ");
						hql.append(" SUM(r.ad_view) ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfd_customer_info_id is not null");
						hql.append(" and r.pfd_user_id is not null");
						hql.append(" group by r.pfd_customer_info_id, r.pfd_user_id, r.customer_info_id, r.ad_type, r.ad_seq, r.ad_pvclk_device, r.template_product_seq");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());

						StringBuffer hql2 = new StringBuffer();

						hql2.append("select ");
						hql2.append(" sum(r.ad_pv), ");
						hql2.append(" (sum(r.ad_clk)-sum(r.ad_invalid_clk)), ");
						hql2.append(" (sum(r.ad_clk_price)-sum(r.ad_invalid_clk_price)), ");
						hql2.append(" sum(r.ad_invalid_clk), ");
						hql2.append(" sum(r.ad_invalid_clk_price), ");
						hql2.append(" r.pfd_customer_info_id, ");
						hql2.append(" r.pfd_user_id, ");
						hql2.append(" r.customer_info_id, ");
						hql2.append(" r.ad_type, ");
						hql2.append(" r.ad_seq, ");
						hql2.append(" r.ad_action_seq, ");
						hql2.append(" r.ad_group_seq, ");
						hql2.append(" r.ad_pvclk_device, ");
						hql2.append(" r.template_product_seq, ");
						hql2.append(" r.ad_price_type, ");
						hql2.append(" (case when r.ad_price_type ='CPC' then 'MEDIA' else 'VIDEO' end)ad_operating_rule, ");
						hql2.append(" SUM(r.ad_vpv), ");
						hql2.append(" SUM(r.ad_view) ");
						hql2.append(" from pfp_ad_pvclk as r");
						hql2.append(" where 1 = 1 ");
						hql2.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql2.append(" and r.pfd_customer_info_id is null");
						hql2.append(" and r.pfd_user_id is null");
						hql2.append(" group by r.customer_info_id, r.ad_type, r.ad_seq, r.ad_pvclk_device, r.template_product_seq");

						String sql2 = hql2.toString();
//						log.info(">>> sql2 = " + sql2);

						Query q2 = session.createSQLQuery(sql2);

						List<Object> resultData2 = q2.list();
//						log.info(">>> resultData2.size() = " + resultData2.size());

						resultData.addAll(resultData2);

						return resultData;
					}
				}
		);

		return result;
	}

	@Override
    public void deleteReportDataByReportDate(String reportDate) throws Exception {
		String sql = "delete from PfdAdReport where adPvclkDate = '" + reportDate + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfdAdReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}
	
	@Override
	 @SuppressWarnings("unchecked")
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE pfd_ad_report r, ");
		sql.append("    (SELECT ");
		sql.append(" 			pfd_customer_info_id,	");
		sql.append(" 			customer_info_id, ");
		sql.append(" 			ad_type, ");
		sql.append(" 			ad_seq, ");
		sql.append(" 			ad_pvclk_device, ");
		sql.append(" 			template_ad_seq, ");
		sql.append(" 			convert_belong_date, ");	   
		sql.append("        	Sum(convert_count)convert_count, ");
		sql.append("        	Sum(convert_price)convert_price, ");
		sql.append("        	convert_trigger_type ");
		sql.append("     	FROM   pfp_code_convert_trans ");
		sql.append("     	WHERE  1 = 1 ");
		sql.append("            AND convert_date >= :convertRangeDate ");
		sql.append("            AND convert_date <= :convertDate ");
		sql.append("            AND convert_trigger_type = 'CK' ");
		sql.append("		GROUP  BY ");
		sql.append("			pfd_customer_info_id, ");
		sql.append("			customer_info_id, ");
		sql.append("			ad_type, ");
		sql.append("			ad_seq, ");
		sql.append("			ad_pvclk_device, ");
		sql.append("			template_ad_seq, ");
		sql.append("			convert_belong_date, ");
		sql.append("			convert_trigger_type, ");
		sql.append("			convert_seq ");
		sql.append("	)a ");
		sql.append(" 	SET     				");
		sql.append("		r.convert_count = a.convert_count, ");
		sql.append("    	r.convert_price_count = a.convert_price, ");
		sql.append("    	r.update_date = Now() ");
		sql.append(" 	WHERE  1 = 1 ");
		sql.append("		AND a.pfd_customer_info_id = r.pfd_customer_info_id ");
		sql.append("    	AND a.customer_info_id = r.pfp_customer_info_id ");
		sql.append("    	AND a.ad_type = r.ad_type ");
		sql.append("    	AND a.ad_seq = r.ad_seq ");
		sql.append("    	AND a.ad_pvclk_device = r.ad_pvclk_device ");
		sql.append("    	AND a.template_ad_seq = r.template_product_seq "); 
		sql.append("    	AND a.convert_belong_date = r.ad_pvclk_date ");
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("convertDate", convertDate);
		query.setString("convertRangeDate", convertRangeDate);
		return query.executeUpdate();
	}
	
}
