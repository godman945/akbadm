package com.pchome.akbadm.db.dao.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.db.vo.KeywordReportVO;

@Transactional
public class AdKeywordReportDAO extends BaseDAO<PfpAdKeywordPvclk, Integer> implements IAdKeywordReportDAO {

	@Override
	public List<KeywordReportVO> getAdKeywordReportList(final String startDate, final String endDate,
			final String adKeywordType, final String sortMode, final int displayCount,
			final String pfdCustomerInfoId) throws Exception {

		List<KeywordReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<KeywordReportVO>>() {
					@Override
                    public List<KeywordReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" k.ad_keyword,");
						sb.append(" sum(kr.ad_pv),");
						sb.append(" sum(kr.ad_clk),");
						sb.append(" sum(kr.ad_clk_price)");
						sb.append(" from pfp_ad_keyword as k, pfd_keyword_report kr");
						sb.append(" where k.ad_keyword_seq = kr.keyword_seq");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and kr.ad_pvclk_date >='" + startDate + " 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and kr.ad_pvclk_date <='" + endDate + " 23:59:59'");
						}
						if (StringUtils.isNotEmpty(adKeywordType)) {
							sb.append(" and kr.ad_type ='" + adKeywordType + "'");
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							sb.append(" and kr.pfd_customer_info_id = '" + pfdCustomerInfoId + "'");
						}
						sb.append(" group by k.ad_keyword");

						if (StringUtils.isNotEmpty(sortMode)) {
							if (sortMode.equals("pv_sum")) {
								sb.append(" order by sum(kr.ad_pv) desc");
							} else if (sortMode.equals("clk_sum")) {
								sb.append(" order by sum(kr.ad_clk) desc");
							} else if (sortMode.equals("price_sum")) {
								sb.append(" order by sum(kr.ad_clk_price) desc");
							}
						}

 						String sql = sb.toString();
//						log.info(">>> sql = " + sql);

						Query query = session.createSQLQuery(sql).setMaxResults(displayCount);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<KeywordReportVO> resultData = new ArrayList<KeywordReportVO>();

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String keyword = (String) objArray[0];
							long pvSum = ((BigDecimal) objArray[1]).longValue();
							long clkSum = ((BigDecimal) objArray[2]).longValue();
							double price = (Double) objArray[3];

							KeywordReportVO kwReportVO = new KeywordReportVO();
							kwReportVO.setKeyword(keyword);
							kwReportVO.setKwPvSum(df2.format(pvSum));
							kwReportVO.setKwClkSum(df2.format(clkSum));
							kwReportVO.setKwPriceSum(df.format(price));
							if (clkSum==0 || pvSum==0) {
								kwReportVO.setKwClkRate(df.format(0) + "%");
							} else {
								kwReportVO.setKwClkRate(df.format((clkSum / pvSum)*100) + "%");
							}
							if (price==0 || clkSum==0) {
								kwReportVO.setClkPriceAvg(df.format(0));
							} else {
								kwReportVO.setClkPriceAvg(df.format(price / clkSum));
							}

							resultData.add(kwReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

	@Override
	public List<KeywordReportVO> getAdKeywordReportDetail(final String startDate, final String endDate,
			final String adKeywordType, final String sortMode, final int displayCount,
			final String keyword, final String pfdCustomerInfoId) throws Exception {

		List<KeywordReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<KeywordReportVO>>() {
					@Override
                    public List<KeywordReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" kr.keyword_seq, ");
						sb.append(" kr.pfp_customer_info_id, ");
						sb.append(" sum(kr.ad_pv),");
						sb.append(" (sum(kr.ad_clk)-sum(kr.ad_invalid_clk)),");
						sb.append(" (sum(kr.ad_clk_price)-sum(kr.ad_invalid_clk_price))");
						sb.append(" from pfp_ad_keyword as k, pfd_keyword_report kr");
						sb.append(" where k.ad_keyword_seq = kr.keyword_seq");
						sb.append(" and k.ad_keyword ='" + keyword + "'");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and kr.ad_pvclk_date >='" + startDate + " 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and kr.ad_pvclk_date <='" + endDate + " 23:59:59'");
						}
						if (StringUtils.isNotEmpty(adKeywordType)) {
							sb.append(" and kr.ad_type ='" + adKeywordType + "'");
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							sb.append(" and kr.pfd_customer_info_id = '" + pfdCustomerInfoId + "'");
						}
						sb.append(" group by kr.ad_action_seq, kr.ad_group_seq, kr.pfp_customer_info_id");

						if (StringUtils.isNotEmpty(sortMode)) {
							if (sortMode.equals("pv_sum")) {
								sb.append(" order by sum(kr.ad_pv) desc");
							} else if (sortMode.equals("clk_sum")) {
								sb.append(" order by sum(kr.ad_clk) desc");
							} else if (sortMode.equals("price_sum")) {
								sb.append(" order by sum(kr.ad_clk_price) desc");
							}
						}

 						String sql = sb.toString();
//						log.info(">>> sql = " + sql);

						Query query = session.createSQLQuery(sql).setMaxResults(displayCount);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<KeywordReportVO> resultData = new ArrayList<KeywordReportVO>();

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String keywordSeq = (String) objArray[0];
							String customerId = (String) objArray[1];
							Long pvSum = ((BigDecimal) objArray[2]).longValue();
							Long clkSum = ((BigDecimal) objArray[3]).longValue();
							Double price = (Double) objArray[4];

							KeywordReportVO kwReportVO = new KeywordReportVO();
							kwReportVO.setKeywordSeq(keywordSeq);
							kwReportVO.setCustomerId(customerId);
							kwReportVO.setKeyword(keyword);
							kwReportVO.setKwPvSum(df2.format(pvSum));
							kwReportVO.setKwClkSum(df2.format(clkSum));
							kwReportVO.setKwPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								kwReportVO.setKwClkRate(df.format(0) + "%");
							} else {
								kwReportVO.setKwClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								kwReportVO.setClkPriceAvg(df.format(0));
							} else {
								kwReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							resultData.add(kwReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

	@Override
	public List<KeywordReportVO> getAdKeywordOfferPriceReportList(final String startDate,
			final String endDate, final String adKeywordType, final String searchText,
			final int displayCount, final String pfdCustomerInfoId) throws Exception {

		List<KeywordReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<KeywordReportVO>>() {
					@Override
                    public List<KeywordReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" k.ad_keyword_seq,");
						sb.append(" sum(kr.ad_pv),");
						sb.append(" sum(kr.ad_clk),");
						sb.append(" sum(kr.ad_clk_price)");
						sb.append(" from pfp_ad_keyword as k, pfd_keyword_report kr");
						sb.append(" where k.ad_keyword_seq = kr.keyword_seq");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and kr.ad_pvclk_date >='" + startDate + " 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and kr.ad_pvclk_date <='" + endDate + " 23:59:59'");
						}
						if (StringUtils.isNotEmpty(adKeywordType)) {
							sb.append(" and kr.ad_type ='" + adKeywordType + "'");
						}
						if (StringUtils.isNotEmpty(searchText)) {
							sb.append(" and k.ad_keyword like ('%" + searchText + "%')");
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							sb.append(" and kr.pfd_customer_info_id = '" + pfdCustomerInfoId + "'");
						}
						sb.append(" group by k.ad_keyword_seq");
						sb.append(" order by k.ad_keyword_search_price desc");

 						String sql = sb.toString();
//						log.info(">>> sql = " + sql);

						Query query = session.createSQLQuery(sql).setMaxResults(displayCount);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<KeywordReportVO> resultData = new ArrayList<KeywordReportVO>();

						DecimalFormat numberFormat = new DecimalFormat("###,###,###,###");
						DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###.##");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String keywordSeq = (String) objArray[0];
							Long pvSum = ((BigDecimal) objArray[1]).longValue();
							Long clkSum = ((BigDecimal) objArray[2]).longValue();
							Double price = (Double) objArray[3];

							KeywordReportVO kwReportVO = new KeywordReportVO();

							kwReportVO.setKeywordSeq(keywordSeq);
							kwReportVO.setKwPvSum(numberFormat.format(pvSum));
							kwReportVO.setKwClkSum(numberFormat.format(clkSum));
							kwReportVO.setKwPriceSum(numberFormat.format(price));
							if (clkSum==0 || pvSum==0) {
								kwReportVO.setKwClkRate(decimalFormat.format(0) + "%");
							} else {
								kwReportVO.setKwClkRate(decimalFormat.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								kwReportVO.setClkPriceAvg(decimalFormat.format(0));
							} else {
								kwReportVO.setClkPriceAvg(decimalFormat.format(price.doubleValue() / clkSum.doubleValue()));
							}

							resultData.add(kwReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}
}
