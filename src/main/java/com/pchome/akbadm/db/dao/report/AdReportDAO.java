package com.pchome.akbadm.db.dao.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;
import com.pchome.soft.util.DateValueUtil;

@Transactional
public class AdReportDAO extends BaseDAO<PfpAdPvclk, Integer> implements IAdReportDAO {

//	/**
//	 * (舊)讀取 AdReport (廣告明細資料)
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adKeywordType 廣告形式
//	 * @param sortMode 排序方式
//	 * @param displayCount 顯示比數
//	 * @return List<AdReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdReportVO> getAdReportList(final String startDate, final String endDate,
//			final String adKeywordType, final String sortMode, final int displayCount) throws Exception {
//
//		List<AdReportVO> result = (List<AdReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdReportVO>>() {
//					public List<AdReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" k.adSeq,");
//						sb.append(" k.templateProductSeq,");
//						sb.append(" kr.customerInfoId,");
//						sb.append(" kg.adGroupName,");
//						sb.append(" ka.adActionName,");
//						sb.append(" sum(kr.adPv),");
//						sb.append(" (sum(kr.adClk)-sum(kr.adInvalidClk)),");
//						sb.append(" (sum(kr.adClkPrice)-sum(kr.adInvalidClkPrice))");
//						sb.append(" from PfpAd as k");
//						sb.append(" inner join k.pfpAdPvclks as kr");
//						sb.append(" inner join k.pfpAdGroup as kg");
//						sb.append(" inner join kg.pfpAdAction as ka");
//						sb.append(" where 1=1");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and kr.adPvclkDate >='" + startDate + " 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and kr.adPvclkDate <='" + endDate + " 23:59:59'");
//						}
//						if (StringUtils.isNotEmpty(adKeywordType)) {
//							sb.append(" and kr.adType ='" + adKeywordType + "'");
//						}
//						sb.append(" group by k.adSeq");
//
//						if (StringUtils.isNotEmpty(sortMode)) {
//							if (sortMode.equals("pv_sum")) {
//								sb.append(" order by sum(kr.adPv) desc");
//							} else if (sortMode.equals("clk_sum")) {
//								sb.append(" order by sum(kr.adClk) desc");
//							} else if (sortMode.equals("price_sum")) {
//								sb.append(" order by sum(kr.adClkPrice) desc");
//							}
//						}
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql).setMaxResults(displayCount);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdReportVO> resultData = new ArrayList<AdReportVO>();
//
//						DecimalFormat df = new DecimalFormat("#.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							String adSeq = (String) objArray[0];
//							String adTemplateSeq = (String) objArray[1];
//							String customerId = (String) objArray[2];
//							String adGroupName = (String) objArray[3];
//							String adActionName = (String) objArray[4];
//							Long pvSum = (Long) objArray[5];
//							Long clkSum = (Long) objArray[6];
//							Double price = (Double) objArray[7];
//
//							AdReportVO adReportVO = new AdReportVO();
//							adReportVO.setAdSeq(adSeq);
//							adReportVO.setAdTemplateSeq(adTemplateSeq);
//							adReportVO.setCustomerId(customerId);
//							adReportVO.setAdAction(adActionName);
//							adReportVO.setAdGroup(adGroupName);
//							adReportVO.setKwPvSum(df2.format(pvSum));
//							adReportVO.setKwClkSum(df2.format(clkSum));
//							adReportVO.setKwPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adReportVO.setKwClkRate(df.format(0) + "%");
//							} else {
//								adReportVO.setKwClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							resultData.add(adReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (新)讀取 AdReport (廣告明細資料)，條件包含 pfdCustomerInfoId (經銷商)
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adKeywordType 廣告形式
	 * @param sortMode 排序方式
	 * @param displayCount 顯示比數
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdReportVO> 廣告成效列表
	 */
	@Override
	public List<AdReportVO> getAdReportList(final String startDate, final String endDate,
			final String adKeywordType, final String sortMode, final int displayCount,
			final String pfdCustomerInfoId, final String templateProductSeq) throws Exception {

		List<AdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdReportVO>>() {
					@Override
                    public List<AdReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" kr.adSeq,");
						sb.append(" kr.templateProductSeq,");
						sb.append(" kr.pfpCustomerInfoId,");
						sb.append(" sum(kr.adPv),");
						sb.append(" sum(kr.adClk),");
						sb.append(" sum(kr.adClkPrice)");
						sb.append(" from PfdAdReport as kr");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and kr.adPvclkDate >='" + startDate + " 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and kr.adPvclkDate <='" + endDate + " 23:59:59'");
						}
						if (StringUtils.isNotEmpty(adKeywordType)) {
							sb.append(" and kr.adType ='" + adKeywordType + "'");
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							sb.append(" and kr.pfdCustomerInfoId = '" + pfdCustomerInfoId + "'");
						}
						if (StringUtils.isNotBlank(templateProductSeq)) {
							sb.append(" and kr.templateProductSeq = '" + templateProductSeq + "'");
						}
						sb.append(" group by kr.adSeq");

						if (StringUtils.isNotEmpty(sortMode)) {
							if (sortMode.equals("pv_sum")) {
								sb.append(" order by sum(kr.adPv) desc");
							} else if (sortMode.equals("clk_sum")) {
								sb.append(" order by sum(kr.adClk) desc");
							} else if (sortMode.equals("price_sum")) {
								sb.append(" order by sum(kr.adClkPrice) desc");
							} else if (sortMode.equals("clk_rate")) {
								sb.append(" order by sum(kr.adClk)*100/sum(kr.adPv) desc");
							} else if (sortMode.equals("clk_price_avg")) {
								sb.append(" order by sum(kr.adClkPrice)/sum(kr.adClk) desc");
							}
						}

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql).setMaxResults(displayCount);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdReportVO> resultData = new ArrayList<AdReportVO>();

						DecimalFormat df = new DecimalFormat("0.00");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String adSeq = (String) objArray[0];
							String adTemplateSeq = (String) objArray[1];
							String customerId = (String) objArray[2];
							Long pvSum = (Long) objArray[3];
							Long clkSum = (Long) objArray[4];
							Double price = (Double) objArray[5];

							AdReportVO adReportVO = new AdReportVO();
							adReportVO.setAdSeq(adSeq);
							adReportVO.setAdTemplateSeq(adTemplateSeq);
							adReportVO.setCustomerId(customerId);
							adReportVO.setKwPvSum(df2.format(pvSum));
							adReportVO.setKwClkSum(df2.format(clkSum));
							adReportVO.setKwPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adReportVO.setKwClkRate(df.format(0) + "%");
							} else {
								adReportVO.setKwClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adReportVO.setClkPriceAvg(df.format(0));
							} else {
								adReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							resultData.add(adReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

//	/**
//	 * (舊)讀取 AdActionReport(總廣告成效) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告刑式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportList(final String startDate, final String endDate,
//			final String adStyle, final String customerInfoId, final String adType) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" r.adPvclkDate,");
//						sb.append(" sum(r.adPv),");
//						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
//						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
//						sb.append(" from PfpAdPvclk as r");
//						sb.append(" where 1=1");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						if (StringUtils.isNotEmpty(customerInfoId)) {
//							sb.append(" and r.customerInfoId ='"+customerInfoId+"'");
//						}
//						if (StringUtils.isNotEmpty(adType)) {
//							sb.append(" and r.adType = '" + adType + "'");
//						}
//						if (StringUtils.isNotEmpty(adStyle)) {
//							sb.append(" and r.pfpAd.adStyle = '" + adStyle + "'");
//						}
//						sb.append(" group by r.adPvclkDate");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//						DecimalFormat df = new DecimalFormat("#.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						int days = dataList.size();
//
//						for (int i=0; i<days; i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							java.sql.Date reportDate = (java.sql.Date) objArray[0];
//							Long pvSum = (Long) objArray[1];
//							Long clkSum = (Long) objArray[2];
//							Double price = (Double) objArray[3];
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setReportDate(sdf.format(reportDate));
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (新)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告型式 (找東西廣告 or PChome頻道廣告)
	 * @param pfdCustomerInfoId 經銷商編號
	 * @param payType 付款方式
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdActionReportVO> getAdActionReportList(final String startDate, final String endDate,
			final String pfpCustomerInfoId, final String adType,
			final String pfdCustomerInfoId, final String pfpPayType) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
						try{
//							log.info(">>> startDate = " + startDate);
//							log.info(">>> endDate = " + endDate);
//							log.info(">>> pfpCustomerInfoId = " + pfpCustomerInfoId);
//							log.info(">>> adType = " + adType);
//							log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
//							log.info(">>> pfpPayType = " + pfpPayType);

							StringBuffer sql = new StringBuffer();
//							sb.append(" select");
//							sb.append(" r.adPvclkDate, ");
//							sb.append(" sum(r.adPv),");
//							sb.append(" sum(case when r.adClkPriceType = 'CPC' then r.adClk else r.adView end),");
//							sb.append(" sum(r.adClkPrice)");
//							sb.append(" from PfdAdActionReport as r ");
//							sb.append(" select   ");
//							sb.append(" a.ad_pvclk_date, ");
//							sb.append(" a.ad_type, ");
//							sb.append(" a.pay_type,  ");
//							sb.append(" sum(a.customer_info_id),  ");
//							sb.append(" sum(a.pfd_customer_info_id),  ");
//							sb.append(" sum(a.ad_pv),  ");
//							sb.append(" sum(a.ad_clk),  ");
//							sb.append(" sum(a.ad_clk_price)  ");
//							sb.append(" from (  ");
//							sb.append(" select  ");
//							sb.append(" a.ad_pvclk_date, ");
//							sb.append(" a.ad_type, ");
//							sb.append(" a.pay_type, ");
//							sb.append(" a.customer_info_id, ");
//							sb.append(" a.pfd_customer_info_id, ");
//							sb.append(" a.ad_pv, ");
//							sb.append(" a.ad_clk, ");
//							sb.append(" a.ad_clk_price ");
//							sb.append(" from ");
//							sb.append(" ( ");
//							sb.append(" SELECT ");
//							sb.append(" r.ad_pvclk_date, ");
//							sb.append(" r.ad_type, ");
//							sb.append(" r.pay_type, ");
//							sb.append(" r.customer_info_id, ");
//							sb.append(" (SELECT ");
//							sb.append(" d.pfd_customer_info_id ");
//							sb.append(" FROM pfd_user_ad_account_ref d ");
//							sb.append(" WHERE d.pfp_customer_info_id = r.customer_info_id) ");
//							sb.append(" pfd_customer_info_id, ");
//							sb.append(" SUM(r.ad_pv) ad_pv, ");
//							sb.append(" SUM(CASE ");
//							sb.append(" WHEN r.ad_clk_price_type = 'CPC' THEN r.ad_clk ");
//							sb.append(" ELSE r.ad_view ");
//							sb.append(" END) ad_clk, ");
//							sb.append(" FLOOR(SUM(r.ad_clk_price)) ad_clk_price ");
//							sb.append(" FROM pfp_ad_action_report r ");
//							sb.append(" WHERE 1 = 1");
//							if (StringUtils.isNotBlank(startDate)) {
//								sb.append(" AND r.ad_pvclk_date >= :startDate ");
//							}
//							if (StringUtils.isNotBlank(endDate)) {
//								sb.append(" AND r.ad_pvclk_date <= :endDate ");
//							}
//							sb.append(" GROUP BY customer_info_id, ");
//							sb.append(" r.ad_pvclk_date ");
//							sb.append(" )a ");
//							sb.append(" WHERE 1 = 1");
//							if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
//								sb.append(" and a.customer_info_id = :pfpCustomerInfoId");
//							}
//							if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
//								sb.append(" and a.pfd_customer_info_id = :pfdCustomerInfoId");
//							}
//							if (StringUtils.isNotBlank(adType)) {
//								sb.append(" and a.ad_type = :adType");
//							}
//							if (StringUtils.isNotBlank(pfpPayType)) {
//								sb.append(" and a.pay_type = :pfpPayType");
//							}
//							sb.append(" group by a.customer_info_id ,a.ad_pvclk_date ");
//							sb.append(" )a  group by a.ad_pvclk_date ");
							
							
							
							sql.append(" select  ");
							sql.append(" a.ad_pvclk_date, ");
							sql.append(" sum(a.ad_pv), ");
							sql.append(" sum(a.ad_clk), ");
							sql.append(" sum(a.ad_clk_price) ");
							sql.append(" from( ");
							sql.append(" SELECT ");
							sql.append(" a.ad_pvclk_date, ");
							sql.append(" a.ad_type, ");
							sql.append(" a.pay_type, ");
							sql.append(" a.customer_info_id, ");
							sql.append(" a.pfd_customer_info_id, ");
							sql.append(" a.ad_pv, ");
							sql.append(" a.ad_clk, ");
							sql.append(" a.ad_clk_price ");
							sql.append(" FROM (SELECT ");
							sql.append(" r.ad_pvclk_date, ");
							sql.append(" r.ad_type, ");
							sql.append(" r.pay_type, ");
							sql.append(" r.customer_info_id, ");
							sql.append(" (SELECT ");
							sql.append(" d.pfd_customer_info_id ");
							sql.append(" FROM pfd_user_ad_account_ref d ");
							sql.append(" WHERE d.pfp_customer_info_id = r.customer_info_id) ");
							sql.append(" pfd_customer_info_id, ");
							sql.append(" SUM(r.ad_pv) ad_pv, ");
							sql.append(" SUM(CASE ");
							sql.append(" WHEN r.ad_clk_price_type = 'CPC' THEN r.ad_clk ");
							sql.append(" ELSE r.ad_view ");
							sql.append(" END) ad_clk, ");
							sql.append(" FLOOR(SUM(r.ad_clk_price)) ad_clk_price ");
							sql.append(" FROM pfp_ad_action_report r ");
							sql.append(" WHERE 1 = 1 ");
							if (StringUtils.isNotBlank(startDate)) {
								sql.append(" AND r.ad_pvclk_date >= :startDate ");
							}
							if (StringUtils.isNotBlank(endDate)) {
								sql.append(" AND r.ad_pvclk_date <= :endDate ");
							}
							if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
								sql.append(" and a.customer_info_id = :pfpCustomerInfoId");
							}
							if (StringUtils.isNotBlank(adType)) {
								sql.append(" and a.ad_type = :adType");
							}
							if (StringUtils.isNotBlank(pfpPayType)) {
								sql.append(" and a.pay_type = :pfpPayType");
							}
							sql.append(" GROUP BY customer_info_id, ");
							sql.append(" r.ad_pvclk_date) a ");
							sql.append(" WHERE 1 = 1 ");
							if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
								sql.append(" and a.pfd_customer_info_id = :pfdCustomerInfoId");
							}
							sql.append(" GROUP BY a.customer_info_id, ");
							sql.append(" a.ad_pvclk_date ");
							sql.append(" )a ");
							sql.append(" group by ad_pvclk_date ");
							log.info(">>> sql = " + sql);
							
							Query query = session.createSQLQuery(sql.toString());
							if (StringUtils.isNotBlank(startDate)) {
								query.setParameter("startDate", startDate);
							}
							if (StringUtils.isNotBlank(endDate)) {
								query.setParameter("endDate", endDate);
							}
							if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
								query.setParameter("pfpCustomerInfoId", pfpCustomerInfoId);
							}
							if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
								query.setParameter("pfdCustomerInfoId", pfdCustomerInfoId);
							}
							if (StringUtils.isNotBlank(adType)) {
								query.setParameter("adType", adType);
							}
							if (StringUtils.isNotBlank(pfpPayType)) {
								query.setParameter("pfpPayType", pfpPayType);
							}
							
							//設定日期格式
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
							DecimalFormat df = new DecimalFormat("#.##");
							DecimalFormat df2 = new DecimalFormat("###,###,###,###");
							
							List<Object> dataList = query.list();
							List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
							int days = dataList.size();
							for (int i=0; i<days; i++) {
								Object[] objArray = (Object[]) dataList.get(i);
								double pvSum = 0;
								double clkSum = 0;
								double price = 0;
								
								java.sql.Date reportDate = (java.sql.Date) objArray[0];
								pvSum = ((BigDecimal) objArray[1]).doubleValue();
								clkSum = ((BigDecimal) objArray[2]).doubleValue();
								price = ((Double) objArray[3]).doubleValue();
								AdActionReportVO adActionReportVO = new AdActionReportVO();
								adActionReportVO.setSearchDate(objArray[0].toString());
								adActionReportVO.setReportDate(sdf.format(reportDate));
								adActionReportVO.setPvSum(df2.format(pvSum));
								adActionReportVO.setClkSum(df2.format(clkSum));
								adActionReportVO.setPriceSum(df2.format(price));
								if (clkSum==0 || pvSum==0) {
									adActionReportVO.setClkRate(df.format(0) + "%");
								} else {
									adActionReportVO.setClkRate(df.format((clkSum / pvSum)*100) + "%");
								}
								if (price==0 || clkSum==0) {
									adActionReportVO.setClkPriceAvg(df.format(0));
								} else {
									adActionReportVO.setClkPriceAvg(df.format(price / clkSum));
								}
	                            if (price==0 || pvSum==0) {
	                                adActionReportVO.setPvPriceAvg(df.format(0));
	                            } else {
	                                adActionReportVO.setPvPriceAvg(df.format(price * 1000 / pvSum));
	                            }
								resultData.add(adActionReportVO);
							}
							return resultData;
						}catch(Exception e){
							e.printStackTrace();
						}
						return null;
					}
				}
		);

		return result;
	}

	/**
	 * 強哥改的，完全看不懂在改什麼，動機為何
	 * (2014-04-24)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告刑式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	/*public List<AdActionReportVO> getAdActionReportList(final String startDate, final String endDate, final List<String> adSeqs, final String pfpCustomerInfoId, final String adType, final String pfdCustomerInfoId) throws Exception {
		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adPvclkDate,");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adClkPrice)");
						sb.append(" from PfdAdReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
							sb.append(" and r.adPvclkDate between :startDate and :endDate");
						} else if(StringUtils.isNotBlank(startDate) && StringUtils.isBlank(endDate)) {
							sb.append(" and r.adPvclkDate >= :startDate");
						} else if(StringUtils.isBlank(startDate) && StringUtils.isNotBlank(endDate)) {
							sb.append(" and r.adPvclkDate <= :endDate");
						}
						if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
							sb.append(" and r.pfpCustomerInfoId = :pfpCustomerInfoId");
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							sb.append(" and r.pfdCustomerInfoId = :pfdCustomerInfoId");
						}
						if (StringUtils.isNotBlank(adType)) {
							sb.append(" and r.adType = :adType");
						}
						if(adSeqs != null && adSeqs.size() > 0) {
							sb.append(" and r.adSeq in ( :adSeq )");
						}
						sb.append(" group by r.adPvclkDate");

 						String hql = sb.toString();
						log.info(">>> hql = " + hql);

						//設定日期格式
						SimpleDateFormat sdfq = new SimpleDateFormat("yyyy-MM-dd");
						Query query = session.createQuery(hql);
						if (StringUtils.isNotBlank(startDate)) {
							try {
								query.setDate("startDate", sdfq.parse(startDate));
							} catch(Exception ex) {
								log.info("startDate Error:" + ex);
							}
						}
						if(StringUtils.isNotBlank(endDate)) {
							try {
								query.setDate("endDate", sdfq.parse(endDate));
							} catch(Exception ex) {
								log.info("endDate Error:" + ex);
							}
						}
						if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
							query.setString("pfpCustomerInfoId", pfpCustomerInfoId);
						}
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							query.setString("pfdCustomerInfoId", pfdCustomerInfoId);
						}
						if (StringUtils.isNotBlank(adType)) {
							query.setString("adType", adType);
						}
						if(adSeqs != null && adSeqs.size() > 0) {
							query.setParameterList("adSeq", adSeqs);
						}


						List<Object> dataList = query.list();
						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						int days = dataList.size();

						for (int i=0; i<days; i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							java.sql.Date reportDate = (java.sql.Date) objArray[0];
							Long pvSum = (Long) objArray[1];
							Long clkSum = (Long) objArray[2];
							Double price = (Double) objArray[3];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setReportDate(sdf.format(reportDate));
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							resultData.add(adActionReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}*/

//	/**
//	 * (舊)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getOfflineAdActionReportList(final String startDate, final String endDate) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionStartDate,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionEndDate,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionName,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.registration,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle,");
//						sb.append(" sum(r.adActionMaxPrice),");
//						sb.append(" count(r.adPvclkSeq),");
//						sb.append(" sum(r.adPv),");
//						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
//						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
//						sb.append(" from PfpAdPvclk as r");
//						sb.append(" where 1=1");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.pfpAd.pfpAdGroup.pfpAdAction.adActionEndDate >= '" + startDate + "'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.pfpAd.pfpAdGroup.pfpAdAction.adActionEndDate <= '" + endDate + "'");
//						}
//						sb.append(" group by r.pfpAd.pfpAdGroup.pfpAdAction.adActionSeq");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						java.util.Date today = new java.util.Date();
//						int oneDay = 1000*3600*24;
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							java.sql.Date startDate = (java.sql.Date) objArray[0];
//							java.sql.Date endDate = (java.sql.Date) objArray[1];
//							String adActionName = (String) objArray[2];
//							String taxId = (String) objArray[3];
//							String customerInfoName = (String) objArray[4];
//							Double adActionMaxPrice = (Double) objArray[5];
//							int count = ((Long) objArray[6]).intValue();
//							Long pvSum = (Long) objArray[7];
//							Long clkSum = (Long) objArray[8];
//							Double price = (Double) objArray[9];
//
//							//計算平均每日花費上限
//							double adActionMaxPriceAvg = 0;
//							if (adActionMaxPrice>0 && count>0) {
//								adActionMaxPriceAvg = adActionMaxPrice/count;
//							}
//
//							//計算達成率
//							double arrivalRate = 0;
//							if (price>0 && adActionMaxPriceAvg>0) {
//								double dateRange = (today.getTime() - startDate.getTime()) / oneDay;
//								arrivalRate = price / (dateRange * adActionMaxPriceAvg);
//							}
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setStartDate(sdf.format(startDate));
//							adActionReportVO.setEndDate(sdf.format(endDate));
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setTaxId(taxId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
//							adActionReportVO.setArrivalRate(df.format(arrivalRate) + "%");
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

//	/**
//	 * (新)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getOfflineAdActionReportList(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" aa.adActionStartDate,");
//						sb.append(" aa.adActionEndDate,");
//						sb.append(" aa.adActionName,");
//						sb.append(" ci.registration,");
//						sb.append(" ci.customerInfoTitle,");
//						sb.append(" sum(r.adActionMaxPrice * r.adPvlckCount), ");
//						sb.append(" sum(r.adPvlckCount), ");
//						sb.append(" sum(r.adPv),");
//						sb.append(" sum(r.adClk),");
//						sb.append(" sum(r.adClkPrice)");
//						sb.append(" from PfdAdActionReport as r, PfpCustomerInfo as ci, PfpAdAction aa, PfdCustomerInfo dci");
//						sb.append(" where 1=1");
//						sb.append(" and r.pfpCustomerInfoId = ci.customerInfoId");
//						sb.append(" and r.adActionSeq = aa.adActionSeq ");
//						sb.append(" and r.pfdCustomerInfoId = dci.customerInfoId");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and aa.adActionEndDate >= '" + startDate + "'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and aa.adActionEndDate <= '" + endDate + "'");
//						}
//						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
//							sb.append(" and r.pfdCustomerInfoId = '" + pfdCustomerInfoId + "'");
//						}
//						sb.append(" group by r.adActionSeq");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						java.util.Date today = new java.util.Date();
//						int oneDay = 1000*3600*24;
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							java.sql.Date startDate = (java.sql.Date) objArray[0];
//							java.sql.Date endDate = (java.sql.Date) objArray[1];
//							String adActionName = (String) objArray[2];
//							String taxId = (String) objArray[3];
//							String customerInfoName = (String) objArray[4];
//							Double adActionMaxPrice = (Double) objArray[5];
//							int count = ((Long) objArray[6]).intValue();
//							Long pvSum = (Long) objArray[7];
//							Long clkSum = (Long) objArray[8];
//							Double price = (Double) objArray[9];
//
//							//計算平均每日花費上限
//							double adActionMaxPriceAvg = 0;
//							if (adActionMaxPrice>0 && count>0) {
//								adActionMaxPriceAvg = adActionMaxPrice/count;
//							}
//
//							//計算達成率
//							double arrivalRate = 0;
//							if (price>0 && adActionMaxPriceAvg>0) {
//								double dateRange = (today.getTime() - startDate.getTime()) / oneDay;
//								arrivalRate = price / (dateRange * adActionMaxPriceAvg);
//							}
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setStartDate(sdf.format(startDate));
//							adActionReportVO.setEndDate(sdf.format(endDate));
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setTaxId(taxId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
//							adActionReportVO.setArrivalRate(df.format(arrivalRate) + "%");
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (2014-04-24)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdActionReportVO> getOfflineAdActionReportList(final HashMap<String, PfpAdAction> adActionMap, final String pfdCustomerInfoId) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adActionSeq,");
						sb.append(" r.pfpCustomerInfoId,");
						sb.append(" r.pfdCustomerInfoId,");
						sb.append(" sum(r.adActionMaxPrice * r.adPvlckCount), ");
						sb.append(" sum(r.adPvlckCount), ");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adClkPrice)");
						sb.append(" from PfdAdActionReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
							sb.append(" and r.pfdCustomerInfoId = :pfdCustomerInfoId");
						}
						if(adActionMap != null && adActionMap.size() > 0) {
							sb.append(" and r.adActionSeq in (:adActionSeq)");
						}
						sb.append(" group by r.adActionSeq");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
						if (StringUtils.isNotBlank(pfdCustomerInfoId)) {
							query.setString("pfdCustomerInfoId", pfdCustomerInfoId);
						}
//						System.out.println("pfdCustomerInfoId = " + pfdCustomerInfoId);
						if(adActionMap != null && adActionMap.size() > 0) {
							List<String> adActionSeqs = new ArrayList<String>(adActionMap.keySet());
//							System.out.println(adActionSeqs);
							query.setParameterList("adActionSeq", adActionSeqs);
						}

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);
							String adActionSeq = (String)objArray[0];
							String pfpCustomerInfoId = (String) objArray[1];
							String pfdCustomerInfoId = (String) objArray[2];

							Double adActionMaxPrice = (Double) objArray[3];
							int count = ((Long) objArray[4]).intValue();
							String pvSum = Long.toString((Long) objArray[5]);
							String clkSum = Long.toString((Long) objArray[6]);
							Double price = (Double) objArray[7];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setAdActionSeq(adActionSeq);
							adActionReportVO.setCustomerInfoId(pfpCustomerInfoId);
							adActionReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);

							adActionReportVO.setMaxPrice(Double.toString(adActionMaxPrice));
							adActionReportVO.setCount(count);
							adActionReportVO.setPvSum(pvSum);
							adActionReportVO.setClkSum(clkSum);
							adActionReportVO.setPriceSum(Double.toString(price));

							resultData.add(adActionReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

//	/**
//	 * (舊)讀取 AdActionReportDetail(總廣告成效明細表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportDetail(final String startDate, final String endDate, final String adStyle, final String customerInfoId, final String adType) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//
//						sb.append("select");
//						sb.append(" r.adPvclkDate,");
//						sb.append(" r.pfpCustomerInfoId,");
//						sb.append(" ci.customerInfoTitle,");
//						sb.append(" aa.adActionName,");
//						sb.append(" sum(r.adPv),");
//						sb.append(" sum(r.adClk),");
//						sb.append(" sum(r.adClkPrice),");
//						sb.append(" dci.companyName,");
//						sb.append(" pu.userName");
//						sb.append(" from PfdAdActionReport as r, PfpCustomerInfo as ci, PfpAdAction aa, PfdUser pu, PfdCustomerInfo dci");
//						sb.append(" where 1=1");
//						sb.append(" and r.pfpCustomerInfoId = ci.customerInfoId");
//						sb.append(" and r.adActionSeq = aa.adActionSeq ");
//						sb.append(" and r.pfdUserId = pu.userId ");
//						sb.append(" and r.pfdCustomerInfoId = dci.customerInfoId");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						if (StringUtils.isNotEmpty(customerInfoId)) {
//							sb.append(" and r.customerInfoId ='"+customerInfoId+"'");
//						}
//						if (StringUtils.isNotEmpty(adType)) {
//							sb.append(" and r.adType = '" + adType + "'");
//						}
//						if (StringUtils.isNotEmpty(adStyle)) {
//							sb.append(" and r.pfpAd.adStyle = '" + adStyle + "'");
//						}
//						sb.append(" group by r.pfpCustomerInfoId");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//						DecimalFormat df = new DecimalFormat("#.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							java.sql.Date reportDate = (java.sql.Date) objArray[0];
//							String customerInfoId = (String) objArray[1];
//							String customerInfoName = (String) objArray[2];
//							String adActionName = (String) objArray[3];
//							Long pvSum = (Long) objArray[4];
//							Long clkSum = (Long) objArray[5];
//							Double price = (Double) objArray[6];
//							String dealer = (String) objArray[7];
//							String sales = (String) objArray[8];
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setReportDate(sdf.format(reportDate));
//							adActionReportVO.setCustomerInfoId(customerInfoId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							adActionReportVO.setDealer(dealer);
//							adActionReportVO.setSales(sales);
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

//	/**
//	 * (新)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportDetail(final String startDate, final String endDate, final String adStyle, final String customerInfoId, final String adType, final String pfdCustomerInfoId) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//
//						sb.append("select");
//						sb.append(" r.adPvclkDate,");
//						sb.append(" r.pfpCustomerInfoId,");
//						sb.append(" ci.customerInfoTitle,");
//						sb.append(" aa.adActionName,");
//						sb.append(" sum(r.adPv),");
//						sb.append(" sum(r.adClk),");
//						sb.append(" sum(r.adClkPrice),");
//						sb.append(" dci.companyName,");
//						sb.append(" pu.userName");
//						sb.append(" from PfdAdActionReport as r, PfpCustomerInfo as ci, PfpAdAction aa, PfdUser pu, PfdCustomerInfo dci");
//						sb.append(" where 1=1");
//						sb.append(" and r.pfpCustomerInfoId = ci.customerInfoId");
//						sb.append(" and r.adActionSeq = aa.adActionSeq ");
//						sb.append(" and r.pfdUserId = pu.userId ");
//						sb.append(" and r.pfdCustomerInfoId = dci.customerInfoId");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						if (StringUtils.isNotEmpty(customerInfoId)) {
//							sb.append(" and r.customerInfoId ='"+customerInfoId+"'");
//						}
//						if (StringUtils.isNotEmpty(adType)) {
//							sb.append(" and r.adType = '" + adType + "'");
//						}
//						if (StringUtils.isNotEmpty(adStyle)) {
//							sb.append(" and r.pfpAd.adStyle = '" + adStyle + "'");
//						}
//						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
//							sb.append(" and r.pfdCustomerInfoId = '" + pfdCustomerInfoId + "'");
//						}
//						sb.append(" group by r.pfpCustomerInfoId");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//
//						DecimalFormat df = new DecimalFormat("#.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							java.sql.Date reportDate = (java.sql.Date) objArray[0];
//							String customerInfoId = (String) objArray[1];
//							String customerInfoName = (String) objArray[2];
//							String adActionName = (String) objArray[3];
//							Long pvSum = (Long) objArray[4];
//							Long clkSum = (Long) objArray[5];
//							Double price = (Double) objArray[6];
//							String dealer = (String) objArray[7];
//							String sales = (String) objArray[8];
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setReportDate(sdf.format(reportDate));
//							adActionReportVO.setCustomerInfoId(customerInfoId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							adActionReportVO.setDealer(dealer);
//							adActionReportVO.setSales(sales);
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * 強哥改的，完全看不懂在改什麼，動機為何
	 * (2014)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告型式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	/*public List<AdActionReportVO> getAdActionReportDetail(final String startDate, final String endDate, final List<String> adStyle, final String customerInfoId, final String adType, final String pfdCustomerInfoId) throws Exception {

		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();

						sb.append("select");
						sb.append(" r.adPvclkDate,");
						sb.append(" r.pfpCustomerInfoId,");
						sb.append(" r.adActionSeq,");
						sb.append(" r.pfdCustomerInfoId,");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adClkPrice),");
						sb.append(" r.pfdUserId");
						sb.append(" from PfdAdReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >= :startDate");
							try {
								sqlParams.put("startDate", sdf.parse(startDate));
							} catch(Exception ex) {
								log.info(ex);
							}
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <= :endDate");
							try {
								sqlParams.put("endDate", sdf.parse(endDate));
							} catch(Exception ex) {
								log.info(ex);
							}
						}
						if (StringUtils.isNotEmpty(customerInfoId)) {
							sb.append(" and r.pfpCustomerInfoId = :customerInfoId");
							sqlParams.put("customerInfoId", customerInfoId);
						}
						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
							sb.append(" and r.pfdCustomerInfoId = :pfdCustomerInfoId");
							sqlParams.put("pfdCustomerInfoId", pfdCustomerInfoId);
						}
						if (StringUtils.isNotEmpty(adType)) {
							sb.append(" and r.adType = :adType");
							sqlParams.put("adType", adType);
						}
						sb.append(" group by r.pfpCustomerInfoId");

 						String hql = sb.toString();
						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
				        for (String paramName:sqlParams.keySet()) {
			        		query.setParameter(paramName, sqlParams.get(paramName));
				        }

						List<Object> dataList = query.list();
						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							java.sql.Date reportDate = (java.sql.Date) objArray[0];
							String customerInfoId = (String) objArray[1];
							String adActionSeq = (String) objArray[2];
							String pfdCustomerInfoId = (String) objArray[3];
							Long pvSum = (Long) objArray[4];
							Long clkSum = (Long) objArray[5];
							Double price = (Double) objArray[6];
							String pfdUserId = (String) objArray[7];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setReportDate(sdf.format(reportDate));
							adActionReportVO.setCustomerInfoId(customerInfoId);
							adActionReportVO.setAdActionSeq(adActionSeq);
							adActionReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							adActionReportVO.setPfdUserId(pfdUserId);
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							resultData.add(adActionReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}*/

	/**
	 * 原本的被強哥改爛了，重寫
	 * 讀取 AdActionReport(廣告成效表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告型式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @param payType 付款方式
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdActionReportVO> getAdActionReportDetail(final String startDate, final String endDate,
			final String pfpCustomerInfoId, final String adType,
			final String pfdCustomerInfoId, final String payType) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

						HashMap<String, Object> sqlParams = new HashMap<String, Object>();

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adPvclkDate,");
						sb.append(" r.pfpCustomerInfoId,");
						sb.append(" r.adActionSeq,");
						sb.append(" r.pfdCustomerInfoId,");
                        sb.append(" sum(r.adPv),");
                        sb.append(" sum(case when r.adClkPriceType = 'CPC' then r.adClk else r.adView end),");
						sb.append(" FLOOR(sum(r.adClkPrice)),");
						sb.append(" r.pfdUserId");
						sb.append(" from PfdAdActionReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate) && StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate between '" + startDate + "'");
							sb.append(" and '" + endDate  + "'");
						}
						if (StringUtils.isNotEmpty(pfpCustomerInfoId)) {
							sb.append(" and r.pfpCustomerInfoId = '" + pfpCustomerInfoId + "'");
						}
						if (StringUtils.isNotEmpty(adType)) {
							sb.append(" and r.adType = '" + adType + "'");
						}
						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
							sb.append(" and r.pfdCustomerInfoId = '" + pfdCustomerInfoId + "'");
						}
						if (StringUtils.isNotEmpty(payType)) {
							sb.append(" and r.pfpPayType = '" + payType + "'");
						}
						sb.append(" group by r.pfpCustomerInfoId");

 						String hql = sb.toString();
						//log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);

						List<Object> dataList = query.list();
						//log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("#.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							java.sql.Date reportDate = (java.sql.Date) objArray[0];
							String customerInfoId = (String) objArray[1];
							String adActionSeq = (String) objArray[2];
							String pfdCustomerInfoId = (String) objArray[3];
							Long pvSum = (Long) objArray[4];
							Long clkSum = (Long) objArray[5];
							Double price = (double)((Integer) objArray[6]);
							String pfdUserId = (String) objArray[7];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setReportDate(sdf2.format(reportDate));
							adActionReportVO.setCustomerInfoId(customerInfoId);
							adActionReportVO.setAdActionSeq(adActionSeq);
							adActionReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							adActionReportVO.setPfdUserId(pfdUserId);
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
                            if (price==0 || pvSum==0) {
                                adActionReportVO.setPvPriceAvg(df.format(0));
                            } else {
                                adActionReportVO.setPvPriceAvg(df.format(price.doubleValue() * 1000 / pvSum.doubleValue()));
                            }

							resultData.add(adActionReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

//	/**
//	 * (舊)讀取 AdTemplateReport(廣告樣版成效表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdTemplateReportVO> getAdTemplateReport(final String startDate, final String endDate) throws Exception {
//
//		List<AdTemplateReportVO> result = (List<AdTemplateReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdTemplateReportVO>>() {
//					public List<AdTemplateReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" r.templateProductSeq,");
//						sb.append(" t.templateProductName,");
//						sb.append(" sum(r.adPv),");
//						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
//						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
//						sb.append(" from PfpAdPvclk as r, AdmTemplateProduct as t");
//						sb.append(" where r.templateProductSeq = t.templateProductSeq");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						sb.append(" group by r.templateProductSeq");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdTemplateReportVO> resultData = new ArrayList<AdTemplateReportVO>();
//
//						DecimalFormat df = new DecimalFormat("#.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							String templateProductName = (String) objArray[1];
//							Long pvSum = (Long) objArray[2];
//							Long clkSum = (Long) objArray[3];
//							Double price = (Double) objArray[4];
//
//							AdTemplateReportVO adTemplateReportVO = new AdTemplateReportVO();
//							adTemplateReportVO.setTemplateProdName(templateProductName);
//							adTemplateReportVO.setPvSum(df2.format(pvSum));
//							adTemplateReportVO.setClkSum(df2.format(clkSum));
//							adTemplateReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adTemplateReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adTemplateReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adTemplateReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adTemplateReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							resultData.add(adTemplateReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (新)讀取 AdTemplateReport(廣告樣版成效表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdTemplateReportVO> getAdTemplateReport(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception {

		List<AdTemplateReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdTemplateReportVO>>() {
					@Override
                    public List<AdTemplateReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.templateProductSeq,");
						sb.append(" t.templateProductName,");
						sb.append(" sum(r.adPv),");
						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
						sb.append(" from PfpAdPvclk as r, AdmTemplateProduct as t");
						sb.append(" where r.templateProductSeq = t.templateProductSeq");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
						}
						sb.append(" group by r.templateProductSeq");

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

							String templateProductName = (String) objArray[1];
							Long pvSum = (Long) objArray[2];
							Long clkSum = (Long) objArray[3];
							Double price = (Double) objArray[4];

							AdTemplateReportVO adTemplateReportVO = new AdTemplateReportVO();
							adTemplateReportVO.setTemplateProdName(templateProductName);
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

	@Override
	public List<AdActionReportVO> getAdNotArrivalReport(final String startDate, final String endDate,
			final int displayCount) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adPvclkDate,");
						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId,");
						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle,");
						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionName,");
						sb.append(" sum(r.adActionMaxPrice),");
						sb.append(" count(r.adPvclkSeq),");
						sb.append(" sum(r.adPv),");
						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
						sb.append(" from PfpAdPvclk as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
						}
						sb.append(" group by r.adPvclkDate, r.pfpAd.pfpAdGroup.pfpAdAction.adActionSeq");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql).setMaxResults(displayCount);

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String customerInfoId = (String) objArray[0];
							String customerInfoName = (String) objArray[1];
							String adActionName = (String) objArray[2];
							Double adActionMaxPrice = (Double) objArray[3];
							int count = ((Long) objArray[4]).intValue();
							Long pvSum = (Long) objArray[5];
							Long clkSum = (Long) objArray[6];
							Double price = (Double) objArray[7];

							//計算平均每日花費上限
							double adActionMaxPriceAvg = 0;
							if (adActionMaxPrice>0 && count>0) {
								adActionMaxPriceAvg = adActionMaxPrice/count;
							}

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setCustomerInfoId(customerInfoId);
							adActionReportVO.setCustomerInfoName(customerInfoName);
							adActionReportVO.setAdActionName(adActionName);
							adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							resultData.add(adActionReportVO);
						}

						return resultData;

					}
				}
		);

		return result;
	}

//	/**
//	 * (舊)花費成效排名查詢DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 花費成效排名列表
//	 */
//	@Override
//	public List<AdActionReportVO> (final String startDate, final String endDate, final int displayCount) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionName,");
//						sb.append(" sum(r.adActionMaxPrice),");
//						sb.append(" count(r.adPvclkSeq),");
//						sb.append(" sum(r.adPv),");
//						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
//						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice))");
//						sb.append(" from PfpAdPvclk as r");
//						sb.append(" where 1=1");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						sb.append(" group by r.pfpAd.pfpAdGroup.pfpAdAction.adActionSeq");
//						sb.append(" order by sum(r.adClkPrice) desc");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql).setMaxResults(displayCount);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							String customerInfoId = (String) objArray[0];
//							String customerInfoName = (String) objArray[1];
//							String adActionName = (String) objArray[2];
//							Double adActionMaxPrice = (Double) objArray[3];
//							int count = ((Long) objArray[4]).intValue();
//							Long pvSum = (Long) objArray[5];
//							Long clkSum = (Long) objArray[6];
//							Double price = (Double) objArray[7];
//
//							//計算平均每日花費上限
//							double adActionMaxPriceAvg = 0;
//							if (adActionMaxPrice>0 && count>0) {
//								adActionMaxPriceAvg = adActionMaxPrice/count;
//							}
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setCustomerInfoId(customerInfoId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							resultData.add(adActionReportVO);
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (2014-04-23)花費成效排名查詢DAO，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 花費成效排名列表
	 */
	@Override
	public List<AdActionReportVO> getAdSpendReport(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
			new HibernateCallback<List<AdActionReportVO>>() {
				@Override
                public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					HashMap<String, Object> sqlParams = new HashMap<String, Object>();
					StringBuffer sb = new StringBuffer();
					sb.append("select");
					sb.append(" r.ad_action_seq,");
					sb.append(" r.pfp_customer_info_id,");
					sb.append(" r.pfd_customer_info_id,");
					sb.append(" sum(r.ad_action_max_price * r.ad_pvlck_count), ");
					sb.append(" sum(r.ad_pvlck_count), ");
					sb.append(" sum(r.ad_pv),");
                    sb.append(" sum(case when r.ad_clk_price_type = 'CPC' then r.ad_clk else r.ad_view end),");
					sb.append(" sum(r.ad_clk_price),");
					sb.append(" sum(r.ad_action_control_price * r.ad_pvlck_count), ");
					sb.append(" (select ifnull(ad_action_max,0.00) from pfp_ad_action where ad_action_seq = r.ad_action_seq  ) ");
					sb.append(" from pfd_ad_action_report as r");
					sb.append(" where 1=1");
					if (StringUtils.isNotEmpty(startDate)) {
						sb.append(" and r.ad_pvclk_date >= :startDate");
						try {
							sqlParams.put("startDate", sdf.parse(startDate));
						} catch(Exception ex) {
							log.error(ex);
						}
					}
					if (StringUtils.isNotEmpty(endDate)) {
						sb.append(" and r.ad_pvclk_date <= :endDate");
						try {
							sqlParams.put("endDate", sdf.parse(endDate));
						} catch(Exception ex) {
							log.error(ex);
						}
					}
					if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
						sb.append(" and r.pfd_customer_info_id = :pfdCustomerInfoId");
						sqlParams.put("pfdCustomerInfoId", pfdCustomerInfoId);
					}
					sb.append(" group by r.ad_action_seq");
					sb.append(" order by sum(r.ad_clk_price) desc");

					String sql = sb.toString();
//					log.info(" hql: " + hql);

					Query query = session.createSQLQuery(sql);
			        for (String paramName:sqlParams.keySet()) {
		        		query.setParameter(paramName, sqlParams.get(paramName));
			        }

					List<Object> dataList = query.list();
					//log.info(">>> dataList.size() = " + dataList.size());

					List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

					DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
					DecimalFormat df2 = new DecimalFormat("###,###,###,###");

					long countDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);
//					log.info(" countDay: "+countDay);

					for (int i=0; i<dataList.size(); i++) {
						Object[] objArray = (Object[]) dataList.get(i);

						String adActionSeq = (String) objArray[0];
						String pfpcustomerInfoId = (String) objArray[1];
						String pfdcustomerInfoId = (String) objArray[2];
						Double adActionMaxPrice = (Double) objArray[3];
						int count = ((BigDecimal) objArray[4]).intValue();
						Long pvSum = ((BigDecimal) objArray[5]).longValue();
						Long clkSum = ((BigDecimal) objArray[6]).longValue();
						Double price = (Double) objArray[7];
						double adActionControlPrice = (Double) objArray[8];
						Double nowMaxPrice = (Double) objArray[9];

						//計算平均每日花費上限
						double adActionMaxPriceAvg = 0;
						if (adActionMaxPrice>0 && count>0) {
							adActionMaxPriceAvg = adActionMaxPrice/count;
						}

						//計算平均調控金額
						double adActionControlPriceAvg = 0;
						if (adActionControlPrice>0 && count>0) {
							adActionControlPriceAvg = adActionControlPrice/count;
						}

						AdActionReportVO adActionReportVO = new AdActionReportVO();
						// 消耗比率
						//float spendRate = (float) (price / (adActionControlPriceAvg * countDay)) * 100;
						float spendRate = (float) (price / (adActionMaxPriceAvg * countDay)) * 100;

//						log.info(" spendRate: "+spendRate);

						adActionReportVO.setAdActionSeq(adActionSeq);
						adActionReportVO.setCustomerInfoId(pfpcustomerInfoId);
						adActionReportVO.setPfdCustomerInfoId(pfdcustomerInfoId);
						adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
						adActionReportVO.setAdActionControlPrice(df2.format(adActionControlPriceAvg));
						adActionReportVO.setPvSum(df2.format(pvSum));
						adActionReportVO.setClkSum(df2.format(clkSum));
						adActionReportVO.setPriceSum(df2.format(price));
						adActionReportVO.setSpendRate(df.format(spendRate));
						if (clkSum==0 || pvSum==0) {
							adActionReportVO.setClkRate(df.format(0));
						} else {
							adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100));
						}
						if (price==0 || clkSum==0) {
							adActionReportVO.setClkPriceAvg(df.format(0));
						} else {
							adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
						}
						adActionReportVO.setNowMaxPrice(df2.format(nowMaxPrice));
	                    //千次曝光收益
	                    if (price==0 || pvSum==0) {
	                        adActionReportVO.setPvPriceAvg(df.format(0));
	                    } else {
	                        adActionReportVO.setPvPriceAvg(df.format(price.doubleValue()*1000 / pvSum));
	                    }
	                    adActionReportVO.setPriceSum(df.format(price));

						resultData.add(adActionReportVO);
					}

					return resultData;

				}
			}
		);

		return result;
	}

//	/**
//	 * (舊)未達每日花費上限報表DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 未達每日花費上限列表
//	 */
//	@Override
//	public List<AdActionReportVO> getUnReachBudgetReport(final String startDate, final String endDate, final int displayCount) throws Exception {
//
//		List<AdActionReportVO> result = (List<AdActionReportVO>) getHibernateTemplate().execute(
//				new HibernateCallback<List<AdActionReportVO>>() {
//					public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {
//
//						StringBuffer sb = new StringBuffer();
//						sb.append("select");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle,");
//						sb.append(" r.pfpAd.pfpAdGroup.pfpAdAction.adActionName,");
//						sb.append(" sum(r.adPv),");
//						sb.append(" (sum(r.adClk)-sum(r.adInvalidClk)),");
//						sb.append(" (sum(r.adClkPrice)-sum(r.adInvalidClkPrice)),");
//						sb.append(" sum(r.adActionMaxPrice),");
//						sb.append(" count(r.adPvclkSeq)");
//						sb.append(" from PfpAdPvclk as r");
//						sb.append(" where 1=1");
//						if (StringUtils.isNotEmpty(startDate)) {
//							sb.append(" and r.adPvclkDate >='"+startDate+" 00:00:00'");
//						}
//						if (StringUtils.isNotEmpty(endDate)) {
//							sb.append(" and r.adPvclkDate <='"+endDate+" 23:59:59'");
//						}
//						sb.append(" group by r.pfpAd.pfpAdGroup.pfpAdAction.adActionSeq");
//
// 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);
//
//						Query query = session.createQuery(hql).setMaxResults(displayCount);
//
//						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());
//
//						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();
//
//						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
//						DecimalFormat df2 = new DecimalFormat("###,###,###,###");
//
//						//計算查詢日期區間
//						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//						long daterange = 0;
//						try {
//							daterange = dateFormat.parse(endDate).getTime() - dateFormat.parse(startDate).getTime();
//						} catch (Exception e) {
//							log.error(e.getMessage(), e);
//						}
//
//						int days = (int)(daterange/(1000*3600*24)) + 1;
//
//						for (int i=0; i<dataList.size(); i++) {
//
//							Object[] objArray = (Object[]) dataList.get(i);
//
//							String customerInfoId = (String) objArray[0];
//							String customerInfoName = (String) objArray[1];
//							String adActionName = (String) objArray[2];
//							Long pvSum = (Long) objArray[3];
//							Long clkSum = (Long) objArray[4];
//							Double price = (Double) objArray[5];
//							Double adActionMaxPrice = (Double) objArray[6];
//							int count = ((Long) objArray[7]).intValue();
//
//							//計算平均每日花費上限
//							int adActionMaxPriceAvg = 0;
//							if (adActionMaxPrice>0 && count>0) {
//								adActionMaxPriceAvg = (int) Math.rint(adActionMaxPrice/count);
//							}
//
//							AdActionReportVO adActionReportVO = new AdActionReportVO();
//							adActionReportVO.setCustomerInfoId(customerInfoId);
//							adActionReportVO.setCustomerInfoName(customerInfoName);
//							adActionReportVO.setAdActionName(adActionName);
//							adActionReportVO.setPvSum(df2.format(pvSum));
//							adActionReportVO.setClkSum(df2.format(clkSum));
//							adActionReportVO.setPriceSum(df2.format(price));
//							if (clkSum==0 || pvSum==0) {
//								adActionReportVO.setClkRate(df.format(0) + "%");
//							} else {
//								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
//							}
//							if (price==0 || clkSum==0) {
//								adActionReportVO.setClkPriceAvg(df.format(0));
//							} else {
//								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
//							}
//							adActionReportVO.setMaxPrice(df2.format(adActionMaxPriceAvg));
//
//							int unReachPrice = (int) (adActionMaxPriceAvg*days - price);
//							adActionReportVO.setUnReachPrice(df2.format(unReachPrice));
//
//							//未達每日花費上限大於 0 的才要顯示，超播的不顯示
//							if (unReachPrice > 0) {
//								resultData.add(adActionReportVO);
//							}
//
//						}
//
//						return resultData;
//
//					}
//				}
//		);
//
//		return result;
//	}

	/**
	 * (2014-04-25)未達每日花費上限報表DAO，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 未達每日花費上限列表
	 */
	@Override
	public List<AdActionReportVO> getUnReachBudgetReport(final String startDate, final String endDate, final String pfdCustomerInfoId) throws Exception {

		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adActionSeq,");
						sb.append(" r.pfpCustomerInfoId,");
						sb.append(" r.pfdCustomerInfoId,");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adClkPrice),");
						sb.append(" sum(r.adActionMaxPrice * r.adPvlckCount), ");
						sb.append(" sum(r.adPvlckCount), ");
						sb.append(" sum(r.adActionControlPrice * r.adPvlckCount) ");
						sb.append(" from PfdAdActionReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >= :startDate");
							try {
								sqlParams.put("startDate", sdf.parse(startDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <= :endDate");
							try {
								sqlParams.put("endDate", sdf.parse(endDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
							sb.append(" and r.pfdCustomerInfoId = :pfdCustomerInfoId");
							sqlParams.put("pfdCustomerInfoId", pfdCustomerInfoId);
						}
						sb.append(" group by r.adActionSeq");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
				        for (String paramName:sqlParams.keySet()) {
			        		query.setParameter(paramName, sqlParams.get(paramName));
				        }

						List<Object> dataList = query.list();
						//log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						//計算查詢日期區間
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						long daterange = 0;
						try {
							daterange = dateFormat.parse(endDate).getTime() - dateFormat.parse(startDate).getTime();
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

						int days = (int)(daterange/(1000*3600*24)) + 1;

						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String adActionSeq = (String)objArray[0];
							String pfpCustomerInfoId = (String) objArray[1];
							String pfdCustomerInfoId = (String) objArray[2];
							Long pvSum = (Long) objArray[3];
							Long clkSum = (Long) objArray[4];
							Double price = (Double) objArray[5];
							Double adActionMaxPrice = (Double) objArray[6];
							int count = ((Long) objArray[7]).intValue();
							Double adActionControlPrice = (Double) objArray[8];

							//計算平均每日花費上限
							int adActionMaxPriceAvg = 0;
							if (adActionMaxPrice>0 && count>0) {
								adActionMaxPriceAvg = (int) Math.rint(adActionMaxPrice/count);
							}

							//計算平均調控金額
							double adActionControlPriceAvg = 0;
							if (adActionControlPrice>0 && count>0) {
								adActionControlPriceAvg = adActionControlPrice/count;
							}

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setAdActionSeq(adActionSeq);
							adActionReportVO.setCustomerInfoId(pfpCustomerInfoId);
							adActionReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0));
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100));
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							adActionReportVO.setMaxPrice(df2.format(adActionMaxPriceAvg));
							adActionReportVO.setAdActionControlPrice(df2.format(adActionControlPriceAvg));

							int unReachPrice = (int) (adActionMaxPriceAvg*days - price);
							adActionReportVO.setUnReachPrice(df2.format(unReachPrice));

							//未達每日花費上限大於 0 的才要顯示，超播的不顯示
							if (unReachPrice > 0) {
								resultData.add(adActionReportVO);
							}
						}

						return resultData;
					}
				}
		);

		return result;
	}

	/**
	 * (2014-04-28)行動裝置成效DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	@Override
    public List<AdActionReportVO> getAdMobileOSReport(final String startDate, final String endDate, final String adMobileOS, final String customerInfoId) throws Exception {
		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.customerInfoId,");
						sb.append(" r.adPvclkDevice,");
						sb.append(" r.adPvclkOs,");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adInvalidClk),");
						sb.append(" sum(r.adClkPrice)");
						sb.append(" from PfpAdOsReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >= :startDate");
							try {
								sqlParams.put("startDate", sdf.parse(startDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <= :endDate");
							try {
								sqlParams.put("endDate", sdf.parse(endDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (adMobileOS != null && !adMobileOS.equals("all")) {
							sb.append(" and r.adPvclkOs = :adMobileOS");
							sqlParams.put("adMobileOS", adMobileOS);
						}
						if (StringUtils.isNotEmpty(customerInfoId)) {
							sb.append(" and r.customerInfoId = :customerInfoId");
							sqlParams.put("customerInfoId", customerInfoId);
						}
						sb.append(" group by r.customerInfoId, r.adPvclkDevice, r.adPvclkOs");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
				        for (String paramName:sqlParams.keySet()) {
			        		query.setParameter(paramName, sqlParams.get(paramName));
				        }

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						String tmp_customerInfoId = "";
						// 計算每個客戶編號需要的 rowspan 數目，並存成 Map
						HashMap<String, Integer> rowspanMap = new HashMap<String, Integer>();
						for (int i=0; i<dataList.size(); i++) {
							Object[] objArray = (Object[]) dataList.get(i);
							String customerInfoId = (String)objArray[0];

							if(rowspanMap.get(customerInfoId) == null) {
								rowspanMap.put(customerInfoId, 1);
							} else {
								int tmp = rowspanMap.get(customerInfoId);
								rowspanMap.remove(customerInfoId);
								rowspanMap.put(customerInfoId, tmp + 1);
							}
							//System.out.println(customerInfoId + " = " + rowspanMap.get(customerInfoId));
						}

						for (int i=0; i<dataList.size(); i++) {
							Object[] objArray = (Object[]) dataList.get(i);
							String customerInfoId = (String)objArray[0];
							String adDevice = (String) objArray[1];
							String adOs = (String) objArray[2];
							Long pvSum = (Long) objArray[3];
							Long clkSum = (Long) objArray[4];
							Long invalidClkSum = (Long) objArray[5];
							Double price = (Double) objArray[6];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							// 在第一筆客戶編號的時候，讀取客戶編號的 rowspan，並設定
							if(!tmp_customerInfoId.equals(customerInfoId)) {
								tmp_customerInfoId = customerInfoId;
								adActionReportVO.setRowspan(rowspanMap.get(customerInfoId));
							}
							adActionReportVO.setCustomerInfoId(customerInfoId);
							adActionReportVO.setAdDevice(adDevice);
							adActionReportVO.setAdOs(adOs);
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setInvalidClkSum(df2.format(invalidClkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							resultData.add(adActionReportVO);
						}

						return resultData;
					}
				}
		);

		return result;

	}
	/**
	 * (2014-04-28)行動裝置成效明細DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	@Override
    public List<AdActionReportVO> getAdMobileOSReportDetail(final String startDate, final String endDate, final String adMobileOS, final String customerInfoId) throws Exception {
		List<AdActionReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdActionReportVO>>() {
					@Override
                    public List<AdActionReportVO> doInHibernate(Session session) throws HibernateException {

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();
						sb.append("select");
						sb.append(" r.adSeq,");
						sb.append(" r.templateProductSeq,");
						sb.append(" r.adGroupSeq,");
						sb.append(" r.adActionSeq,");
						sb.append(" sum(r.adPv),");
						sb.append(" sum(r.adClk),");
						sb.append(" sum(r.adClkPrice)");
						sb.append(" from PfpAdOsReport as r");
						sb.append(" where 1=1");
						if (StringUtils.isNotEmpty(startDate)) {
							sb.append(" and r.adPvclkDate >= :startDate");
							try {
								sqlParams.put("startDate", sdf.parse(startDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (StringUtils.isNotEmpty(endDate)) {
							sb.append(" and r.adPvclkDate <= :endDate");
							try {
								sqlParams.put("endDate", sdf.parse(endDate));
							} catch(Exception ex) {
								log.error(ex);
							}
						}
						if (adMobileOS != null && !adMobileOS.equals("all")) {
							sb.append(" and r.adPvclkOs = :adMobileOS");
							sqlParams.put("adMobileOS", adMobileOS);
						}
						if (StringUtils.isNotEmpty(customerInfoId)) {
							sb.append(" and r.customerInfoId = :customerInfoId");
							sqlParams.put("customerInfoId", customerInfoId);
						}
						sb.append(" group by r.customerInfoId, r.adSeq");

 						String hql = sb.toString();
//						log.info(">>> hql = " + hql);

						Query query = session.createQuery(hql);
				        for (String paramName:sqlParams.keySet()) {
			        		query.setParameter(paramName, sqlParams.get(paramName));
				        }

						List<Object> dataList = query.list();
//						log.info(">>> dataList.size() = " + dataList.size());

						List<AdActionReportVO> resultData = new ArrayList<AdActionReportVO>();

						DecimalFormat df = new DecimalFormat("###,###,###,###.##");
						DecimalFormat df2 = new DecimalFormat("###,###,###,###");

						for (int i=0; i<dataList.size(); i++) {
							Object[] objArray = (Object[]) dataList.get(i);
							String adSeq = (String)objArray[0];
							String templateProductSeq = (String) objArray[1];
							String adGroupSeq = (String) objArray[2];
							String adActionSeq = (String) objArray[3];
							Long pvSum = (Long) objArray[4];
							Long clkSum = (Long) objArray[5];
							Double price = (Double) objArray[6];

							AdActionReportVO adActionReportVO = new AdActionReportVO();
							adActionReportVO.setAdSeq(adSeq);
							adActionReportVO.setTemplateProductSeq(templateProductSeq);
							adActionReportVO.setAdGroupSeq(adGroupSeq);
							adActionReportVO.setAdActionSeq(adActionSeq);
							adActionReportVO.setCustomerInfoId(customerInfoId);
							adActionReportVO.setPvSum(df2.format(pvSum));
							adActionReportVO.setClkSum(df2.format(clkSum));
							adActionReportVO.setPriceSum(df2.format(price));
							if (clkSum==0 || pvSum==0) {
								adActionReportVO.setClkRate(df.format(0) + "%");
							} else {
								adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}
							if (price==0 || clkSum==0) {
								adActionReportVO.setClkPriceAvg(df.format(0));
							} else {
								adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
							}
							resultData.add(adActionReportVO);
						}

						return resultData;
					}
				}
		);

		return result;

	}
}
