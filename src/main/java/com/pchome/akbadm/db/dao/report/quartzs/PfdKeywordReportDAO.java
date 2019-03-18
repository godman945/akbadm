package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdKeywordReport;

public class PfdKeywordReportDAO extends BaseDAO<PfdKeywordReport, Integer> implements IPfdKeywordReportDAO {

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
						/*hql.append(" sum(r.ad_keyword_pv), ");
						hql.append(" (sum(r.ad_keyword_clk)-sum(r.ad_keyword_invalid_clk)), ");
						hql.append(" (sum(r.ad_keyword_clk_price)-sum(r.ad_keyword_invalid_clk_price)), ");
						hql.append(" sum(r.ad_keyword_invalid_clk), ");
						hql.append(" sum(r.ad_keyword_invalid_clk_price), ");*/
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.pfd_user_id, ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_keyword_type, ");
						hql.append(" r.ad_keyword_seq, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_group_seq, ");
						hql.append(" r.ad_keyword_pvclk_device ");
						hql.append(" from pfp_ad_keyword_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_keyword_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfd_customer_info_id is not null");
						hql.append(" and r.pfd_user_id is not null");
						hql.append(" group by r.pfd_customer_info_id, r.pfd_user_id, r.customer_info_id, r.ad_keyword_type, r.ad_keyword_seq, r.ad_keyword_pvclk_device");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();
//						log.info(">>> resultData.size() = " + resultData.size());

						StringBuffer hql2 = new StringBuffer();

						hql2.append("select ");
						hql2.append(" sum(r.ad_keyword_pv), ");
						hql2.append(" (sum(r.ad_keyword_clk)-sum(r.ad_keyword_invalid_clk)), ");
						hql2.append(" (sum(r.ad_keyword_clk_price)-sum(r.ad_keyword_invalid_clk_price)), ");
						hql2.append(" sum(r.ad_keyword_invalid_clk), ");
						hql2.append(" sum(r.ad_keyword_invalid_clk_price), ");
						hql2.append(" r.pfd_customer_info_id, ");
						hql2.append(" r.pfd_user_id, ");
						hql2.append(" r.customer_info_id, ");
						hql2.append(" r.ad_keyword_type, ");
						hql2.append(" r.ad_keyword_seq, ");
						hql2.append(" r.ad_action_seq, ");
						hql2.append(" r.ad_group_seq, ");
						hql2.append(" r.ad_keyword_pvclk_device ");
						hql2.append(" from pfp_ad_keyword_pvclk as r");
						hql2.append(" where 1 = 1 ");
						hql2.append(" and r.ad_keyword_pvclk_date ='" + reportDate + "'");
						hql2.append(" and r.pfd_customer_info_id is null");
						hql2.append(" and r.pfd_user_id is null");
						hql2.append(" group by r.customer_info_id, r.ad_keyword_type, r.ad_keyword_seq, r.ad_keyword_pvclk_device");

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
		String sql = "delete from PfdKeywordReport where adPvclkDate = '" + reportDate + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfdKeywordReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}
}
