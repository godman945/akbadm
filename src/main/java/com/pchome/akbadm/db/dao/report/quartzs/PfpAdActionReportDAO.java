package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdActionReport;

public class PfpAdActionReportDAO extends BaseDAO<PfpAdActionReport, Integer> implements IPfpAdActionReportDAO {

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
						hql.append(" sum(r.ad_clk - r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_clk_price - r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_action_max_price), ");
						hql.append(" count(r.ad_action_seq), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_type, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.pay_type, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" (case when r.ad_price_type = 'CPC' then 'MEDIA'  else 'VIDEO' end ) ad_operating_rule, ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.ad_type, r.ad_action_seq, r.ad_pvclk_device, r.pay_type,ad_price_type");

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
	/**
	 * 統計 pfp_ad_pvclk 的曝光數、點擊數、點擊數總費用、無效點擊數、無效點擊數總費用
	 * @param reportDate 報表日期
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAdPvclkReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
						String qryDate = reportDate;
						if(StringUtils.isBlank(reportDate))	{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							qryDate = sdf.format(new Date());
						}

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk - r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_clk_price - r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + qryDate + "'");

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

	/**
	 * 統計 pfp_ad_action_report 的曝光數、點擊數、點擊數總費用、無效點擊數、無效點擊數總費用
	 * @param reportDate 報表日期
	 * @return List<Object>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getAdReportData(final String reportDate) throws Exception {

		List<Object> result = getHibernateTemplate().execute(
				new HibernateCallback<List<Object>>() {
					@Override
                    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
						String qryDate = reportDate;
						if(StringUtils.isBlank(reportDate))	{
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							qryDate = sdf.format(new Date());
						}

						StringBuffer hql = new StringBuffer();

						hql.append("select ");
						hql.append(" sum(r.ad_pv), ");
						hql.append(" sum(r.ad_clk), ");
						hql.append(" sum(r.ad_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" from pfp_ad_action_report as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + qryDate + "'");

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
		String sql = "delete from PfpAdActionReport where adPvclkDate = '" + reportDate + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdActionReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	@Override
    public float findAdClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adClkPrice) ");
		hql.append(" from PfpAdActionReport ");
    	hql.append(" where customerInfoId = :customerInfoId ");
        hql.append(" and payType = :payType");
    	hql.append(" and adPvclkDate >= :startDate");
    	hql.append(" and adPvclkDate <= :endDate");

    	Query query = this.getSession().createQuery(hql.toString());
    	query.setString("customerInfoId", customerInfoId)
        .setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;

//    	Object result = query.uniqueResult();
//    	if (result == null) {
//    		return 0f;
//    	}
//
//    	return ((Double) result).floatValue();
	}

	@Override
    public float findAdInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adInvalidClkPrice) ");
		hql.append(" from PfpAdActionReport ");
    	hql.append(" where customerInfoId = :customerInfoId ");
        hql.append(" and payType = :payType");
    	hql.append(" and adPvclkDate >= :startDate");
    	hql.append(" and adPvclkDate <= :endDate");

    	Query query = this.getSession().createQuery(hql.toString());
    	query.setString("customerInfoId", customerInfoId)
        .setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;
	}

	@Override
    public float findAdClkAndInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(adInvalidClkPrice+adClkPrice) ");
		hql.append(" from PfpAdActionReport ");
    	hql.append(" where customerInfoId = :customerInfoId ");
        hql.append(" and payType = :payType");
    	hql.append(" and adPvclkDate >= :startDate");
    	hql.append(" and adPvclkDate <= :endDate");

    	Query query = this.getSession().createQuery(hql.toString());
    	query.setString("customerInfoId", customerInfoId)
        .setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object> findPfpAdActionReport(Date startDate, Date endDate) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" select customerInfoId, sum(adClkPrice) ");
		hql.append(" from PfpAdActionReport ");
		hql.append(" where adPvclkDate between ? and ? ");
    	hql.append(" group by customerInfoId");

    	list.add(startDate);
    	list.add(endDate);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
