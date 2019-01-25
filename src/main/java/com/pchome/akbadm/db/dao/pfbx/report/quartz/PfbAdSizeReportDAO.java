package com.pchome.akbadm.db.dao.pfbx.report.quartz;


import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxAdSizeReport;

public class PfbAdSizeReportDAO extends BaseDAO<PfbxAdSizeReport, Integer> implements IPfbAdSizeReportDAO {

	@SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = (List<Object>) getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.pfbx_customer_info_id, ");
						hql.append(" r.pfbx_position_id ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfbx_customer_info_id is not null");
						hql.append(" and r.pfbx_customer_info_id !=''");
						hql.append(" group by  r.pfbx_customer_info_id, r.pfbx_position_id");

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

	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		String sql = "delete from PfbxAdSizeReport where adPvclkDate = '" + reportDate + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public void insertReportData(List<PfbxAdSizeReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}
}
