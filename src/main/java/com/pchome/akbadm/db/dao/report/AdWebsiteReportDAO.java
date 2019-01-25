package com.pchome.akbadm.db.dao.report;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdWebsiteReportVO;
import com.pchome.enumerate.report.EnumAdTimeCode;

public class AdWebsiteReportDAO extends BaseDAO<PfpAdWebsiteReportVO, String> implements IAdWebsiteReportDAO {

	List<PfpAdWebsiteReportVO> resultData = new ArrayList<PfpAdWebsiteReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.000");

	@Override
    public List<PfpAdWebsiteReportVO> getAdWebsiteReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdWebsiteReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdWebsiteReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdWebsiteReportVO> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
	                    hql.append(" sum(case when a.ad_clk_price_type = 'CPC' then a.ad_clk else a.ad_view end),");
						hql.append(" sum(a.ad_clk_price), ");
						hql.append(" ifnull(count(distinct a.customer_info_id),0), ");
						hql.append(" a.ad_pvclk_date, ");
						hql.append(" a.ad_pvclk_device, ");
						hql.append(" a.website_category_code ");
						hql.append(" from pfp_ad_website_report a ");
						hql.append(" where 1=1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchCategory"))) {
							hql.append(" and a.website_category_code = '"+conditionMap.get("searchCategory")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}

						hql.append(" group by a.ad_pvclk_date, a.website_category_code ");
						hql.append(" order by a.ad_pvclk_date, a.website_category_code ");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						log.info(">>> dataList.size() = " + dataList.size());

						resultData = new ArrayList<PfpAdWebsiteReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();

							Object[] objArray = (Object[]) dataList.get(i);

							Long pvSum = new Long(objArray[0].toString());
							Long clkSum = new Long(objArray[1].toString());
							Double price = new Double(0);
							if(objArray[2] != null){
								price = (Double) objArray[2];
							}

							//點擊率
							if (clkSum==0 || pvSum==0) {
								vo.setAdClkRate(df.format(0) + "%");
							} else {
								vo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}

							//平均點擊費用
							if (price==0 || clkSum==0) {
								vo.setAdClkAvgPrice(df.format(0));
							} else {
								vo.setAdClkAvgPrice(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							//千次曝光費用
							if (price==0 || pvSum==0) {
								vo.setAdPvAvgPrice(df.format(0));
							} else {
								vo.setAdPvAvgPrice(df.format(price.doubleValue()*1000 / pvSum) );
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setAdClkPriceSum(df3.format(new Double((objArray[2]).toString())));
							vo.setPfpCustomerInfoIdCount(df2.format(new Long((objArray[3]).toString())));

							if(objArray[4] != null){
								vo.setAdPvclkDate((objArray[4]).toString());
							}

							if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
								if(objArray[5] != null){
									String adDevice = (objArray[5]).toString();
									String adDeviceName = "行動裝置";

									if(StringUtils.equals(adDevice, "PC")){
										adDeviceName = "電腦";
									}
									vo.setAdDevice(adDeviceName);
								}
							} else {
								vo.setAdDevice("全部");
							}

							if(objArray[6] != null){
								vo.setWebsiteCategoryCode((objArray[6]).toString());
							}


							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
    public List<PfpAdWebsiteReportVO> getAdWebsiteReportSumByCondition(final Map<String, String> conditionMap) {
		List<PfpAdWebsiteReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdWebsiteReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdWebsiteReportVO> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
                        hql.append(" sum(case when a.ad_clk_price_type = 'CPC' then a.ad_clk else a.ad_view end),");
						hql.append(" sum(a.ad_clk_price), ");
						hql.append(" a.ad_pvclk_device, ");
						hql.append(" a.website_category_code ");
						hql.append(" from pfp_ad_website_report a ");
						hql.append(" where 1=1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchCategory"))) {
							if(StringUtils.equals(conditionMap.get("searchCategory"), "A")){
								hql.append(" and a.website_category_code = ''");
							} else {
								hql.append(" and a.website_category_code = '"+conditionMap.get("searchCategory")+"'");
							}
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}

						hql.append(" group by a.website_category_code ");
						hql.append(" order by a.website_category_code ");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						log.info(">>> dataList.size() = " + dataList.size());

						resultData = new ArrayList<PfpAdWebsiteReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();

							Object[] objArray = (Object[]) dataList.get(i);

							Long pvSum = new Long(objArray[0].toString());
							Long clkSum = new Long(objArray[1].toString());
							Double price = new Double(0);
							if(objArray[2] != null){
								price = (Double) objArray[2];
							}

							//點擊率
							if (clkSum==0 || pvSum==0) {
								vo.setAdClkRate(df.format(0) + "%");
							} else {
								vo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}

							//平均點擊費用
							if (price==0 || clkSum==0) {
								vo.setAdClkAvgPrice(df.format(0));
							} else {
								vo.setAdClkAvgPrice(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							//千次曝光費用
							if (price==0 || pvSum==0) {
								vo.setAdPvAvgPrice(df.format(0));
							} else {
								vo.setAdPvAvgPrice(df.format(price.doubleValue()*1000 / pvSum) );
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setAdClkPriceSum(df3.format(new Double((objArray[2]).toString())));

							if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
								if(objArray[3] != null){
									String adDevice = (objArray[3]).toString();
									String adDeviceName = "行動裝置";

									if(StringUtils.equals(adDevice, "PC")){
										adDeviceName = "電腦";
									}
									vo.setAdDevice(adDeviceName);
								}
							} else {
								vo.setAdDevice("全部");
							}

							if(objArray[4] != null){
								vo.setWebsiteCategoryCode((objArray[4]).toString());
							}


							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
    public List<PfpAdWebsiteReportVO> getAdWebsiteDetalReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdWebsiteReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdWebsiteReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdWebsiteReportVO> doInHibernate(Session session) throws HibernateException, SQLException {

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
                        hql.append(" sum(case when a.ad_clk_price_type = 'CPC' then a.ad_clk else a.ad_view end),");
						hql.append(" sum(a.ad_clk_price), ");
						hql.append(" a.customer_info_id, ");
						hql.append(" a.ad_pvclk_date, ");
						hql.append(" a.ad_pvclk_device, ");
						hql.append(" a.website_category_code, ");
						hql.append(" a.time_code ");
						hql.append(" from pfp_ad_website_report a ");
						hql.append(" where 1=1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchCategory"))) {
							if(StringUtils.equals(conditionMap.get("searchCategory"), "A")){
								hql.append(" and a.website_category_code = ''");
							} else {
								hql.append(" and a.website_category_code = '"+conditionMap.get("searchCategory")+"'");
							}
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}

						hql.append(" group by a.ad_pvclk_date, a.website_category_code, a.customer_info_id, a.time_code ");
						hql.append(" order by a.ad_pvclk_date, a.website_category_code, a.customer_info_id, a.time_code ");

						String sql = hql.toString();
//						log.info(">>> sql = " + sql);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						log.info(">>> dataList.size() = " + dataList.size());

						resultData = new ArrayList<PfpAdWebsiteReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();

							Object[] objArray = (Object[]) dataList.get(i);

							Long pvSum = new Long(objArray[0].toString());
							Long clkSum = new Long(objArray[1].toString());
							Double price = new Double(0);
							if(objArray[2] != null){
								price = (Double) objArray[2];
							}

							//點擊率
							if (clkSum==0 || pvSum==0) {
								vo.setAdClkRate(df.format(0) + "%");
							} else {
								vo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
							}

							//平均點擊費用
							if (price==0 || clkSum==0) {
								vo.setAdClkAvgPrice(df.format(0));
							} else {
								vo.setAdClkAvgPrice(df.format(price.doubleValue() / clkSum.doubleValue()));
							}

							//千次曝光費用
							if (price==0 || pvSum==0) {
								vo.setAdPvAvgPrice(df.format(0));
							} else {
								vo.setAdPvAvgPrice(df.format(price.doubleValue()*1000 / pvSum) );
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setAdClkPriceSum(df3.format(new Double((objArray[2]).toString())));

							if(objArray[3] != null){
								vo.setPfpCustomerInfoId((objArray[3]).toString());
							}

							if(objArray[4] != null){
								vo.setAdPvclkDate((objArray[4]).toString());
							}

							if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
								if(objArray[5] != null){
									String adDevice = (objArray[5]).toString();
									String adDeviceName = "行動裝置";

									if(StringUtils.equals(adDevice, "PC")){
										adDeviceName = "電腦";
									}
									vo.setAdDevice(adDeviceName);
								}
							} else {
								vo.setAdDevice("全部");
							}

							if(objArray[6] != null){
								vo.setWebsiteCategoryCode((objArray[6]).toString());
							}

							if(objArray[7] != null){
								for(EnumAdTimeCode enumAdTimeCode:EnumAdTimeCode.values()){
									if(StringUtils.equals(objArray[7].toString(), enumAdTimeCode.getCode())){
										vo.setTimeCode(enumAdTimeCode.getName());
									}
								}
							} else {
								vo.setTimeCode("全時段");
							}

							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}
}
