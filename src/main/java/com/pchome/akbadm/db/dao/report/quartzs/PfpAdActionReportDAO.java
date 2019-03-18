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
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdActionReport;

public class PfpAdActionReportDAO extends BaseDAO<PfpAdActionReport, Integer> implements IPfpAdActionReportDAO {

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
						hql.append(" (select ac.ad_operating_rule from pfp_ad_action ac where ac.ad_action_seq = r.ad_action_seq) ad_operating_rule, ");
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
                    public List<Object> doInHibernate(Session session) throws HibernateException {
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
                    public List<Object> doInHibernate(Session session) throws HibernateException {
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
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
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

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
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

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
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

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
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

		return (List<Object>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@Override
	public int updateConvertCountData(String convertDate,String convertRangeDate) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE pfp_ad_action_report r, ");
		sql.append(" ( ");
		sql.append(" SELECT ");
		sql.append(" c.customer_info_id,  ");
		sql.append(" convert_belong_date,  ");
		sql.append(" Sum(convert_count)convert_count,  ");
		sql.append(" Sum(convert_price)convert_price,  ");
		sql.append(" c.ad_type,  ");
		sql.append(" c.ad_action_seq,  ");
		sql.append(" c.ad_pvclk_device,  ");
		sql.append(" c.pay_type,  ");
		sql.append(" c.convert_trigger_type  ");
		sql.append(" FROM   pfp_code_convert_trans c  ");
		sql.append(" WHERE  1 = 1  ");
		sql.append(" AND c.convert_date >= :convertRangeDate ");
		sql.append(" AND c.convert_date <= :convertDate ");
		sql.append(" AND c.convert_trigger_type = 'CK'  ");
		sql.append(" GROUP  BY c.customer_info_id,  ");
		sql.append(" c.ad_type,  ");
		sql.append(" c.ad_action_seq,  ");
		sql.append(" c.ad_pvclk_device,  ");
		sql.append(" c.pay_type,  ");
		sql.append(" c.convert_trigger_type,  ");
		sql.append(" c.convert_belong_date,  ");
		sql.append(" c.convert_seq  ");
		sql.append(" )a ");
		sql.append(" SET r.convert_count = a.convert_count, ");
		sql.append(" r.convert_price_count = a.convert_price, ");
		sql.append(" r.update_date = now() ");
		sql.append(" WHERE  1 = 1  ");
		sql.append(" AND a.ad_action_seq = r.ad_action_seq ");
		sql.append(" AND a.customer_info_id= r.customer_info_id ");
		sql.append(" AND a.convert_belong_date = r.ad_pvclk_date ");
		sql.append(" AND a.ad_pvclk_device = r.ad_pvclk_device ");
		sql.append(" AND a.pay_type = r.pay_type ");
		sql.append(" AND a.ad_type= r.ad_type ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("convertDate", convertDate);
		query.setString("convertRangeDate", convertRangeDate);
		return query.executeUpdate();
	}
}
