package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdOsReport;

public class PfpAdOsReportDAO extends BaseDAO<PfpAdOsReport, Integer> implements IPfpAdOsReportDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {

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
						hql.append(" (case when pc.ad_price_type = 'CPC' then 'MEDIA'  else 'VIDEO' end ) ad_operating_rule, ");
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
		Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdOsReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}
}
