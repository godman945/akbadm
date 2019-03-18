package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAgeReport;

public class PfpAdAgeReportDAO extends BaseDAO<PfpAdAgeReport, Integer> implements IPfpAdAgeReportDAO {

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
						hql.append(" sum(r.ad_clk_price-r.ad_invalid_clk_price), ");
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.customer_info_id, ");
						hql.append(" r.ad_type, ");
						hql.append(" r.ad_action_seq, ");
						hql.append(" r.ad_group_seq, ");
						hql.append(" r.ad_pvclk_device, ");
						hql.append(" r.sex, ");
						hql.append(" r.age_code, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" sum(r.ad_view), ");
						hql.append(" (select ac.ad_operating_rule from pfp_ad_action ac where ac.ad_action_seq = r.ad_action_seq  ) ad_operating_rule, ");
						hql.append(" sum(r.ad_vpv) ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" group by r.customer_info_id, r.ad_type, r.ad_action_seq, r.ad_group_seq, r.ad_pvclk_device, r.sex, r.age_code,r.ad_price_type");

						String sql = hql.toString();

						Query q = session.createSQLQuery(sql);

						List<Object> resultData = q.list();

						return resultData;
					}
				}
		);

		return result;
	}

	@Override
    public void deleteReportDataByReportDate(String reportDate) throws Exception {
		String sql = "delete from PfpAdAgeReport where adPvclkDate = '" + reportDate + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfpAdAgeReport> dataList) throws Exception {
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

						hql.append("select max(r.adPvclkDate) from PfpAdPvclk as r");

						String sql = hql.toString();

						Query q = session.createQuery(sql);

						return q.list();
					}
				}
		);

		return result;
	}

	@Override
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  pfp_ad_age_report r, ");
		sql.append(" ( ");
		sql.append(" SELECT ");
		sql.append(" c.customer_info_id, ");
		sql.append(" c.ad_action_seq,  ");
		sql.append(" c.ad_group_seq, ");
		sql.append(" c.time_code, ");
		sql.append(" c.ad_type, ");
		sql.append(" c.sex, ");
		sql.append(" c.age_code, ");
		sql.append(" convert_belong_date,  ");
		sql.append(" Sum(convert_count)convert_count,  ");
		sql.append(" Sum(convert_price)convert_price,  ");
		sql.append(" c.ad_pvclk_device, ");
		sql.append(" c.convert_trigger_type  ");
		sql.append(" FROM   pfp_code_convert_trans c  ");
		sql.append(" WHERE  1 = 1  ");
		sql.append(" AND c.convert_date >= :convertRangeDate ");
		sql.append(" AND c.convert_date <= :convertDate ");
		sql.append(" AND c.convert_trigger_type = 'CK'  ");
		sql.append(" GROUP  BY c.customer_info_id, ");
		sql.append(" c.ad_type, ");
		sql.append(" c.ad_action_seq, ");
		sql.append(" c.ad_group_seq, ");
		sql.append(" c.ad_pvclk_device, ");
		sql.append(" c.sex, ");
		sql.append(" c.age_code,  ");
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
		sql.append(" AND a.ad_pvclk_device = r.ad_pvclk_device ");
		sql.append(" AND IFNULL(a.sex,'') = IFNULL(r.sex,'') ");
		sql.append(" AND a.age_code = r.age_code ");
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("convertDate", convertDate);
		query.setString("convertRangeDate", convertRangeDate);
		return query.executeUpdate();
	}
}
