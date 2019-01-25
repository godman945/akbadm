package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdGroupReport;

public class PfpAdGroupReportDAO extends BaseDAO<PfpAdGroupReport, Integer> implements IPfpAdGroupReportDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_clk_price-r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_type, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_group_seq, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.ad_pvclk_os, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" (case when r.ad_price_type = 'CPC' then 'MEDIA'  else 'VIDEO' end ) ad_operating_rule, ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.ad_type, r.ad_group_seq, r.ad_pvclk_device, r.ad_pvclk_os,r.pay_type,ad_price_type");

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
		String sql = "delete from PfpAdGroupReport where adPvclkDate = '" + reportDate + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdGroupReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> getLastDate() throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();

						hql.append("select max(r.adPvclkDate) from PfpAdPvclk as r");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);

						Query q = session.createQuery(sql);

//						log.info(">>> q.list().size() = " + q.list().size());

						return q.list();
					}
				}
		);

		return result;
	}
}
