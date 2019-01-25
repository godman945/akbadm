package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdAdActionReport;

public class PfdAdActionReportDAO extends BaseDAO<PfdAdActionReport, Integer> implements IPfdAdActionReportDAO {

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
						hql.append(" (sum(r.ad_clk)-sum(r.ad_invalid_clk)), ");
						hql.append(" (sum(r.ad_clk_price)-sum(r.ad_invalid_clk_price)), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_action_max_price), ");
						hql.append(" (select a.ad_action_control_price from pfp_ad_action a where a.ad_action_seq = r.ad_action_seq), ");
						hql.append(" count(r.ad_action_seq), ");
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.pfd_user_id, ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_type, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.pay_type, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" SUM(r.ad_view), ");
						hql.append(" SUM(r.ad_vpv), ");
						hql.append(" (case when r.ad_price_type ='CPC' then 'MEDIA' else 'VIDEO' end)ad_operating_rule ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfd_customer_info_id is not null");
						hql.append(" and r.pfd_user_id is not null");
//						hql.append(" group by r.customer_info_id,r.pfd_customer_info_id, r.pfd_user_id,  r.ad_type, r.ad_action_seq, r.ad_pvclk_device, r.pay_type");
						hql.append(" group by r.pfd_customer_info_id, r.pfd_user_id, r.customer_info_id, r.ad_type, r.ad_action_seq, r.ad_pvclk_device, r.pay_type");
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
						hql2.append(" sum(r.ad_action_max_price), ");
						hql2.append(" (select a.ad_action_control_price from pfp_ad_action a where a.ad_action_seq = r.ad_action_seq), ");
						hql2.append(" count(r.ad_action_seq), ");
						hql2.append(" r.pfd_customer_info_id, ");
						hql2.append(" r.pfd_user_id, ");
						hql2.append(" r.customer_info_id, ");
						hql2.append(" r.ad_type, ");
						hql2.append(" r.ad_action_seq, ");
						hql2.append(" r.ad_pvclk_device, ");
						hql2.append(" r.pay_type, ");
						hql2.append(" r.ad_price_type, ");
						hql2.append(" SUM(r.ad_view), ");
						hql2.append(" SUM(r.ad_vpv), ");
						hql2.append(" (case when r.ad_price_type ='CPC' then 'MEDIA' else 'VIDEO' end)ad_operating_rule ");
						hql2.append(" from pfp_ad_pvclk as r");
						hql2.append(" where 1 = 1 ");
						hql2.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql2.append(" and r.pfd_customer_info_id is null");
						hql2.append(" and r.pfd_user_id is null");
						hql2.append(" group by r.customer_info_id, r.ad_type, r.ad_action_seq, r.ad_pvclk_device, r.pay_type");

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
		String sql = "delete from PfdAdActionReport where adPvclkDate = '" + reportDate + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfdAdActionReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findPfdAdActionClickCost(String pfdCustomerInfoId, Date startDate, Date endDate, String payType) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" select sum(adClkPrice) ");
		hql.append(" from PfdAdActionReport ");
		hql.append(" where pfdCustomerInfoId = ? ");
		hql.append(" and adPvclkDate >= ? ");
		hql.append(" and adPvclkDate <= ? ");
		hql.append(" and pfpPayType = ? ");

		list.add(pfdCustomerInfoId);
		list.add(startDate);
		list.add(endDate);
		list.add(payType);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, Date startDate, Date endDate, String payType) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" select sum(adClkPrice) ");
		hql.append(" from PfdAdActionReport ");
		hql.append(" where pfdCustomerInfoId = ? ");
		hql.append(" and pfpCustomerInfoId = ? ");
		hql.append(" and adPvclkDate >= ? ");
		hql.append(" and adPvclkDate <= ? ");
		hql.append(" and pfpPayType = ? ");

		list.add(pfdCustomerInfoId);
		list.add(pfpCustomerInfoId);
		list.add(startDate);
		list.add(endDate);
		list.add(payType);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findPfdAdActionReportToVo(final String pfdId, final Date startDate, final Date endDate, final String payType) {

		List<Object> result = getHibernateTemplate().execute(

		        new HibernateCallback<List<Object> >() {

		        	@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {

		        		Query q = null;
		        		StringBuffer sql = new StringBuffer();

		        		sql.append(" select c.customer_info_id, c.customer_info_title, r.pfp_pay_type,sum(r.ad_clk_price) ");
		        		sql.append(" from pfd_ad_action_report as r ");
		        		sql.append(" left join pfp_customer_info as c ");
		        		sql.append(" 	on r.pfp_customer_info_id = c.customer_info_id ");
		        		sql.append(" where r.pfd_customer_info_id = :pfdId ");
		        		sql.append(" and r.ad_pvclk_date between :startDate and :endDate ");
		        		//sql.append(" and r.pfp_pay_type = :payType ");
		        		sql.append(" group by r.pfp_customer_info_id,r.pfp_pay_type ");

		        		q = session.createSQLQuery(sql.toString())
		        				.setString("pfdId", pfdId)
	        					.setDate("startDate", startDate)
	        					.setDate("endDate", endDate);
	        					//.setString("payType", payType);

		        		return q.list();
		        	}
		        });

		return result;
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public List<PfdAdActionReport> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate) {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
	
		hql.append(" select pfpCustomerInfoId, sum(adClkPrice) ");
		hql.append(" from PfdAdActionReport ");
		hql.append(" where pfdCustomerInfoId = ? ");
		hql.append(" and adPvclkDate between ? and ? ");
		hql.append(" group by pfpCustomerInfoId");
	
		list.add(pfdCustomerInfoId);
		list.add(startDate);
		list.add(endDate);
	
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
