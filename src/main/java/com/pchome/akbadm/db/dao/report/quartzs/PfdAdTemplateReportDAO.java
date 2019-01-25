package com.pchome.akbadm.db.dao.report.quartzs;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdAdTemplateReport;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;

public class PfdAdTemplateReportDAO extends BaseDAO<PfdAdTemplateReport, Integer> implements IPfdAdTemplateReportDAO {

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
						hql.append(" r.pfd_customer_info_id, ");
						hql.append(" r.template_product_seq, ");
						hql.append(" r.ad_price_type, ");
						hql.append(" (case when r.ad_price_type ='CPC' then 'MEDIA' else 'VIDEO' end)ad_operating_rule, ");
						hql.append(" SUM(r.ad_vpv), ");
						hql.append(" SUM(r.ad_view) ");
						hql.append(" from pfp_ad_pvclk as r");
						hql.append(" where 1 = 1 ");
						hql.append(" and r.ad_pvclk_date ='" + reportDate + "'");
						hql.append(" and r.pfd_customer_info_id is not null");
						hql.append(" and r.pfd_user_id is not null");
						hql.append(" group by r.pfd_customer_info_id, r.template_product_seq");

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
						hql2.append(" group by r.template_product_seq");

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
		String sql = "delete from PfdAdTemplateReport where adPvclkDate = '" + reportDate + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@Override
    public void insertReportData(List<PfdAdTemplateReport> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdTemplateReportVO> findPfdAdTemplateReport(final String startDate, final String endDate,
			final String pfdCustomerInfoId) throws Exception {

		List<AdTemplateReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdTemplateReportVO>>() {
					@Override
                    public List<AdTemplateReportVO> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" templateProductSeq,");
						sb.append(" sum(adPv),");
						sb.append(" sum(adClk),");
						sb.append(" sum(adClkPrice)");
						sb.append(" from PfdAdTemplateReport");
						sb.append(" where templateProductSeq is not null");
						sb.append(" and templateProductSeq != ''");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and adPvclkDate >='"+startDate+" 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and adPvclkDate <='"+endDate+" 23:59:59'");
						}
						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
							sb.append(" and pfdCustomerInfoId = '" + pfdCustomerInfoId + "'");
						}

						sb.append(" group by templateProductSeq");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdTemplateReportVO> resultData = new ArrayList<AdTemplateReportVO>();

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String templateProductSeq = (String) objArray[0];

							Long pvSum = (Long) objArray[1];
							Long clkSum = (Long) objArray[2];
							Double price = (Double) objArray[3];

							AdTemplateReportVO adTemplateReportVO = new AdTemplateReportVO();
							adTemplateReportVO.setTemplateProdSeq(templateProductSeq);
							adTemplateReportVO.setPvSum(df2.format(pvSum));
							adTemplateReportVO.setClkSum(df2.format(clkSum));
							adTemplateReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adTemplateReportVO.setClkRate(df.format(0) + "%");
							} else {
								adTemplateReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adTemplateReportVO.setClkPriceAvg(df.format(0));
							} else {
								adTemplateReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							resultData.add(adTemplateReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}
}
