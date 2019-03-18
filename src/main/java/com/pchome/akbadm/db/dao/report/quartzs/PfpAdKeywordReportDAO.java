package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordReport;

public class PfpAdKeywordReportDAO extends BaseDAO<PfpAdKeywordReport, Integer> implements IPfpAdKeywordReportDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> prepareReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						//廣泛比對
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '1' then r.ad_keyword_pv else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '1' then r.ad_keyword_clk-r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '1' then r.ad_keyword_clk_price-r.ad_keyword_invalid_clk_price else 0.00 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '1' then r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '1' then r.ad_keyword_invalid_clk_price else 0.00 end)), ");
						/*hql.append(" sum(r.ad_keyword_pv), ");
						hql.append(" sum(r.ad_keyword_clk-r.ad_keyword_invalid_clk), ");
						hql.append(" sum(r.ad_keyword_clk_price-r.ad_keyword_invalid_clk_price), ");
						hql.append(" sum(r.ad_keyword_invalid_clk), ");
						hql.append(" sum(r.ad_keyword_invalid_clk_price), ");*/

						//詞組比對
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '2' then r.ad_keyword_pv else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '2' then r.ad_keyword_clk-r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '2' then r.ad_keyword_clk_price-r.ad_keyword_invalid_clk_price else 0.00 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '2' then r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '2' then r.ad_keyword_invalid_clk_price else 0.00 end)), ");

						//精準比對
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '3' then r.ad_keyword_pv else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '3' then r.ad_keyword_clk-r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '3' then r.ad_keyword_clk_price-r.ad_keyword_invalid_clk_price else 0.00 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '3' then r.ad_keyword_invalid_clk else 0 end)), ");
						hql.append(" sum((case when IFNULL(r.ad_keyword_search_style,'1') = '3' then r.ad_keyword_invalid_clk_price else 0.00 end)), ");

						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_keyword_type, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_group_seq, ");
						hql.append(" r.ad_keyword_seq, ");
						hql.append(" k.ad_keyword, ");
						hql.append(" r.ad_keyword_pvclk_device, ");
						hql.append(" r.ad_keyword_pvclk_os, ");
						hql.append(" r.ad_keyword_pvclk_date, ");
						hql.append(" r.ad_keyword_pvclk_time ");
						hql.append(" from pfp_ad_keyword_pvclk as r, pfp_ad_keyword as k");
						hql.append(" where r.ad_keyword_seq = k.ad_keyword_seq ");
						hql.append(" and r.ad_keyword_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.ad_keyword_type, r.ad_keyword_seq, r.ad_keyword_pvclk_device, r.ad_keyword_pvclk_os");

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
		String sql = "delete from PfpAdKeywordReport where adKeywordPvclkDate = '" + reportDate + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdKeywordReport> dataList) throws Exception {
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
                    public List<Object> doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append("select max(r.adKeywordPvclkDate) from PfpAdKeywordPvclk as r");

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
