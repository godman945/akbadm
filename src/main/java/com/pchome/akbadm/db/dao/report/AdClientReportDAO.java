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
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;
import com.pchome.enumerate.report.EnumAdTimeCode;

public class AdClientReportDAO extends BaseDAO<PfpAdReportVO, String> implements IAdClientReportDAO {

	List<PfpAdReportVO> resultData = new ArrayList<PfpAdReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");

	@Override
    public List<PfpAdReportVO> getAdClientReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
						hql.append(" sum(case when a.ad_clk_price_type = 'CPC' then (a.ad_clk - a.ad_invalid_clk) else a.ad_view end), ");
						hql.append(" sum(a.ad_clk_price - a.ad_invalid_clk_price), ");
						hql.append(" a.ad_pvclk_date,");
						hql.append(" a.customer_info_id, ");
						hql.append(" b.customer_info_title, ");
						hql.append(" a.pfd_customer_info_id, ");
						hql.append(" c.company_name, ");
						hql.append(" count(distinct a.pfbx_customer_info_id) ");
						hql.append(" from adm_pfpd_ad_pvclk_report a ");
						hql.append(" join pfp_customer_info b ");
						hql.append(" on a.customer_info_id = b.customer_info_id ");
						hql.append(" join pfd_customer_info c ");
						hql.append(" on a.pfd_customer_info_id = c.customer_info_id ");
						hql.append(" where 1 = 1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfdCustomerInfoId"))){
							hql.append(" and a.pfd_customer_info_id= '"+conditionMap.get("pfdCustomerInfoId")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
							hql.append(" and (a.customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
							hql.append(" or b.customer_info_title like '%"+conditionMap.get("searchAll")+"%')");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
							hql.append(" and a.customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfpCustomerInfoTitle"))) {
							hql.append(" and b.customer_info_title like '%"+conditionMap.get("pfpCustomerInfoTitle")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}

						hql.append(" group by a.ad_pvclk_date, a.customer_info_id ");
						hql.append(" order by a.ad_pvclk_date, a.customer_info_id ");

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

						resultData = new ArrayList<PfpAdReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdReportVO vo = new PfpAdReportVO();

							Object[] objArray = (Object[]) dataList.get(i);

//							log.info("length: "+objArray.length);
//
//							log.info(">>>pvSum: "+objArray[0]);
//							log.info(">>>clkSum: "+objArray[1]);
//							log.info(">>>>price: "+objArray[2]);

							Long pvSum = new Long(objArray[0].toString());;
							Long clkSum = new Long(objArray[1].toString());;
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
								vo.setAdClkAvgPrice(df3.format(price.doubleValue() / clkSum.doubleValue()));
							}

                            //千次曝光收益
                            if (price==0 || pvSum==0) {
                                vo.setAdPvRate(df.format(0));
                            } else {
                                vo.setAdPvRate(df3.format(price.doubleValue()*1000 / pvSum));
                            }

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setAdClkPriceSum(df2.format(Double.parseDouble((objArray[2]).toString())));

							if(objArray[3] != null){
								vo.setAdPvclkDate((objArray[3]).toString());
							}

							if(objArray[4] != null){
								vo.setPfpCustomerInfoId((objArray[4]).toString());
							}

							if(objArray[5] != null){
								vo.setPfpCustomerInfoName((objArray[5]).toString());
							}

							if(objArray[6] != null){
								vo.setPfdCustomerInfoId((objArray[6]).toString());
							}

							if(objArray[7] != null){
								vo.setPfdCustomerInfoName((objArray[7]).toString());
							}

							if(objArray[8] != null){
								vo.setPfbCustomerInfoId(df2.format(new Long((objArray[8]).toString())));
							}

							resultData.add(vo);
//							log.info("resultData: "+resultData.size());
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
    public List<PfpAdReportVO> getAdClientDetalReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
                        hql.append(" sum(case when a.ad_clk_price_type = 'CPC' then (a.ad_clk - a.ad_invalid_clk) else a.ad_view end), ");
						hql.append("  IFNULL((sum(a.ad_clk_price) - sum(a.ad_invalid_clk_price)) ");
		    			hql.append(" /(select sum(e.ad_clk_price) - sum(e.ad_invalid_clk_price) from pfbx_ad_time_report e ");
		    			hql.append(" where a.pfbx_customer_info_id = e.customer_info_id and a.ad_pvclk_date = e.ad_pvclk_date ) ");
		    			hql.append(" *(f.total_bonus),0.00),");
						hql.append(" a.ad_pvclk_date,");
						hql.append(" a.customer_info_id, ");
						hql.append(" b.customer_info_title, ");
						hql.append(" a.pfd_customer_info_id, ");
						hql.append(" c.company_name, ");
						hql.append(" a.pfbx_customer_info_id, ");
						hql.append(" a.ad_url_name, ");
						hql.append(" a.ad_url, ");
						hql.append(" a.time_code, ");
						hql.append(" case when d.category = '1' then d.contact_name else d.company_name end, ");
						hql.append(" d.website_chinese_name ");
						hql.append(" from adm_pfpd_ad_pvclk_report a ");
						hql.append(" join pfp_customer_info b ");
						hql.append(" on a.customer_info_id = b.customer_info_id ");
						hql.append(" join pfd_customer_info c ");
						hql.append(" on a.pfd_customer_info_id = c.customer_info_id ");
						hql.append(" join pfbx_customer_info d ");
						hql.append(" on a.pfbx_customer_info_id = d.customer_info_id ");
						hql.append(" left join pfbx_bonus_day_report f ");
						hql.append(" on a.pfbx_customer_info_id = f.pfb_id ");
						hql.append(" and a.ad_pvclk_date = f.report_date ");
						hql.append(" where 1 = 1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfdCustomerInfoId"))){
							hql.append(" and a.pfd_customer_info_id= '"+conditionMap.get("pfdCustomerInfoId")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
							hql.append(" and (a.customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
							hql.append(" or b.customer_info_title like '%"+conditionMap.get("searchAll")+"%')");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
							hql.append(" and a.customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfpCustomerInfoTitle"))) {
							hql.append(" and b.customer_info_title like '%"+conditionMap.get("pfpCustomerInfoTitle")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfpCustomerInfoId"))){
							hql.append(" and a.customer_info_id= '"+conditionMap.get("pfpCustomerInfoId")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("adPvclkDate"))){
							hql.append(" and a.ad_pvclk_date= '"+conditionMap.get("adPvclkDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}

						hql.append(" group by a.ad_pvclk_date, a.customer_info_id, a.pfbx_customer_info_id, a.ad_url, a.time_code ");
						hql.append(" order by a.ad_pvclk_date, a.customer_info_id, a.pfbx_customer_info_id, a.ad_url, a.time_code ");

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

						resultData = new ArrayList<PfpAdReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdReportVO vo = new PfpAdReportVO();

							Object[] objArray = (Object[]) dataList.get(i);

//							log.info("length: "+objArray.length);
//
//							log.info(">>>pvSum: "+objArray[0]);
//							log.info(">>>clkSum: "+objArray[1]);
//							log.info(">>>>price: "+objArray[2]);

							Long pvSum = new Long(objArray[0].toString());;
							Long clkSum = new Long(objArray[1].toString());;
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

							//單次點擊收益
							if (price==0 || clkSum==0) {
								vo.setAdClkAvgPrice(df.format(0));
							} else {
								vo.setAdClkAvgPrice(df3.format(price.doubleValue() / clkSum.doubleValue()));
							}

							//千次曝光收益
							if (price==0 || pvSum==0) {
								vo.setAdPvRate(df.format(0));
							} else {
								vo.setAdPvRate(df3.format(price.doubleValue()*1000 / pvSum));
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setTotalBonus((objArray[2]).toString());

							if(objArray[3] != null){
								vo.setAdPvclkDate((objArray[3]).toString());
							}

							if(objArray[4] != null){
								vo.setPfpCustomerInfoId((objArray[4]).toString());
							}

							if(objArray[5] != null){
								vo.setPfpCustomerInfoName((objArray[5]).toString());
							}

							if(objArray[6] != null){
								vo.setPfdCustomerInfoId((objArray[6]).toString());
							}

							if(objArray[7] != null){
								vo.setPfdCustomerInfoName((objArray[7]).toString());
							}

							if(objArray[8] != null){
								vo.setPfbCustomerInfoId((objArray[8]).toString());
							}

							if(objArray[9] != null){
								vo.setPfbWebsiteChineseName((objArray[9]).toString());
							}

							if(objArray[10] != null){
								vo.setPfbWebsiteDisplayUrl((objArray[10]).toString());
							}

							if(objArray[11] != null){
								for(EnumAdTimeCode enumAdTimeCode:EnumAdTimeCode.values()){
									if(StringUtils.equals(objArray[11].toString(), enumAdTimeCode.getCode())){
										vo.setTimeCode(enumAdTimeCode.getName());
									}
								}
							} else {
								vo.setTimeCode("全時段");
							}

							if(objArray[12] != null){
								vo.setPfbCustomerInfoName((objArray[12]).toString());
							}

							if(objArray[13] != null){
								vo.setPfbDefaultWebsiteChineseName((objArray[13]).toString());
							}

							resultData.add(vo);
//							log.info("resultData: "+resultData.size());
						}

						return resultData;
					}
				});
    	return result;
	}
}
