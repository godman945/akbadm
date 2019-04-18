package com.pchome.akbadm.db.dao.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.AdReportVO;


public class AdReport2DAO extends BaseDAO<AdReportVO, String> implements IAdReport2DAO {

	List<AdReportVO> resultData = new ArrayList<AdReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.000");

	@Override
	public List<AdReportVO> getAdReportList(final Map<String, String> conditionMap, final int page, final int pageSize) throws Exception {
		List<AdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<AdReportVO> doInHibernate(Session session) throws HibernateException {
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();

						sb.append("select");
						sb.append(" kr.ad_seq,");
						sb.append(" kr.template_product_seq,");
						sb.append(" kr.pfp_customer_info_id,");
						sb.append(" sum(kr.ad_pv),");
						sb.append(" sum(case when kr.ad_clk_price_type = 'CPC' then kr.ad_clk else kr.ad_view end),");
						sb.append(" sum(kr.ad_clk_price),");
						sb.append(" pci.customer_info_title, ");
						sb.append(" kr.pfd_customer_info_id, ");
						sb.append(" sum(kr.convert_count), ");
						sb.append(" sum(kr.convert_price_count) ");
						sb.append(" from pfd_ad_report as kr ");
						sb.append(" left join pfp_customer_info pci ");
						sb.append(" on kr.pfp_customer_info_id = pci.customer_info_id ");
						sb.append(" where 1=1");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							sb.append(" and kr.ad_pvclk_date >= :startDate ");
							sqlParams.put("startDate", conditionMap.get("startDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							sb.append(" and kr.ad_pvclk_date <= :endDate ");
							sqlParams.put("endDate", conditionMap.get("endDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("adType"))) {
							sb.append(" and kr.ad_type = :adType ");
							sqlParams.put("adType", conditionMap.get("adType"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfdCustomerInfoId"))) {
							sb.append(" and kr.pfd_customer_info_id = :pfdCustomerInfoId ");
							sqlParams.put("pfdCustomerInfoId", conditionMap.get("pfdCustomerInfoId"));
						}

						if(StringUtils.isNotEmpty(conditionMap.get("searchText"))){
							if(StringUtils.isNotEmpty(conditionMap.get("searchType"))){
								if(StringUtils.equals("searchPfpId", conditionMap.get("searchType"))){
									sb.append(" and kr.pfp_customer_info_id like :searchText  ");
								} else {
									sb.append(" and pci.customer_info_title like :searchText ");
								}
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							} else {
								sb.append(" and (kr.pfp_customer_info_id like :searchText or ");
								sb.append("  pci.customer_info_title like :searchText ) ");
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							}
						}

						sb.append(" group by kr.pfd_customer_info_id, kr.pfp_customer_info_id, kr.ad_seq");
						sb.append(" order by kr.pfd_customer_info_id, kr.pfp_customer_info_id, kr.ad_seq");

						String sql = sb.toString();
						List<Object> dataList = null;

						Query query = session.createSQLQuery(sql);
				        for (String paramName:sqlParams.keySet()) {
				        	if(!paramName.equals("sql")) {
				        		query.setParameter(paramName, sqlParams.get(paramName));
				        	}
				        }

						//page > -1 取得分頁用於download
						if(page > -1){
							query.setFirstResult((page-1)*pageSize);
							query.setMaxResults(pageSize);
						}

						dataList = query.list();

						resultData = new ArrayList<AdReportVO>();
						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String adSeq = (String) objArray[0];
							String adTemplateSeq = (String) objArray[1];
							String customerId = (String) objArray[2];
							Long pvSum = new Long(objArray[3].toString());
							Long clkSum = new Long(objArray[4].toString());
							Double price = (Double) objArray[5];
							double convertCount =  ((BigDecimal)objArray[8]).doubleValue();		//總轉換數
							double convertPriceCount =  ((BigDecimal)objArray[9]).doubleValue();	//總轉換價值
							double convertCVR = 0;	//轉換率
							double convertCost = 0;	//平均轉換成本
							double convertInvestmentCost = 0;	//廣告投資報酬率
							String pfpCustomerInfoTitle = "";
							if(objArray[6] != null){
								pfpCustomerInfoTitle = objArray[6].toString();
							}
							String pfdCustomerInfoId = objArray[7].toString();

							AdReportVO adReportVO = new AdReportVO();
							adReportVO.setAdSeq(adSeq);
							adReportVO.setAdTemplateSeq(adTemplateSeq);
							adReportVO.setCustomerId(customerId);
							adReportVO.setKwPvSum(df2.format(pvSum));
							adReportVO.setKwClkSum(df2.format(clkSum));
							adReportVO.setKwPriceSum(df3.format(price));
							adReportVO.setCustomerName(pfpCustomerInfoTitle);
							adReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);
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
	                        //千次曝光收益
	                        if (price==0 || pvSum==0) {
	                            adReportVO.setPvPriceAvg(df.format(0));
	                        } else {
	                            adReportVO.setPvPriceAvg(df.format(price.doubleValue()*1000 / pvSum));
	                        }
	                        
	                        
	                        
	                        //商品廣告轉換
	                        //轉換次數
	                        adReportVO.setConvertCountSum(objArray[8].toString());
	                        
	                        //總轉換價值
	                        adReportVO.setConvertPriceCountSum(objArray[9].toString());
	                        
	                        //轉換率=轉換次數/互動數*100%
	                        if(convertCount > 0 && clkSum > 0){
	            				convertCVR  = (convertCount / clkSum) * 100;
	            			}
	                        adReportVO.setConvertCVR(df.format(convertCVR)+ "%");
	                        
	                        //平均轉換成本=費用/轉換次數
	                        if(price > 0 && convertCount > 0){
	            				convertCost = price / convertCount;
	            			}
	                        adReportVO.setConvertCost(df.format(convertCost));
	                        
	                        //廣告投資報酬率=總轉換價值/費用*100%
	            			if(convertPriceCount > 0 && price > 0){
	            				convertInvestmentCost = (convertPriceCount / price) * 100;
	            			}
	            			adReportVO.setConvertInvestmentCost(df.format(convertInvestmentCost)+ "%");
	                        
							resultData.add(adReportVO);
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
	public List<AdReportVO> getAdReportListSum(final Map<String, String> conditionMap) throws Exception {
		List<AdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<AdReportVO> doInHibernate(Session session) throws HibernateException {
						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();

						sb.append("select");
						sb.append(" ifnull(sum(kr.ad_pv),0),");
						sb.append(" ifnull(sum(case when kr.ad_clk_price_type = 'CPC' then kr.ad_clk else kr.ad_view end),0),");
						sb.append(" ifnull(sum(kr.ad_clk_price),0),");
						sb.append(" ifnull(sum(kr.convert_count),0), ");
						sb.append(" ifnull(sum(kr.convert_price_count),0) ");
						sb.append(" from pfd_ad_report as kr ");
						sb.append(" left join pfp_customer_info pci ");
						sb.append(" on kr.pfp_customer_info_id = pci.customer_info_id ");
						sb.append(" where 1=1");

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdSeq"))) {
							sb.append(" and kr.ad_seq = :searchAdSeq ");
							sqlParams.put("searchAdSeq", conditionMap.get("searchAdSeq"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							sb.append(" and kr.ad_pvclk_date >= :startDate ");
							sqlParams.put("startDate", conditionMap.get("startDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							sb.append(" and kr.ad_pvclk_date <= :endDate ");
							sqlParams.put("endDate", conditionMap.get("endDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("adType"))) {
							sb.append(" and kr.ad_type = :adType ");
							sqlParams.put("adType", conditionMap.get("adType"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfdCustomerInfoId"))) {
							sb.append(" and kr.pfd_customer_info_id = :pfdCustomerInfoId ");
							sqlParams.put("pfdCustomerInfoId", conditionMap.get("pfdCustomerInfoId"));
						}

						if(StringUtils.isNotEmpty(conditionMap.get("searchText"))){
							if(StringUtils.isNotEmpty(conditionMap.get("searchType"))){
								if(StringUtils.equals("searchPfpId", conditionMap.get("searchType"))){
									sb.append(" and kr.pfp_customer_info_id like :searchText  ");
								} else {
									sb.append(" and  pci.customer_info_title like :searchText ");
								}
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							} else {
								sb.append(" and (kr.pfp_customer_info_id like :searchText or ");
								sb.append("  pci.customer_info_title like :searchText ) ");
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							}
						}

						String sql = sb.toString();
						List<Object> dataList = null;

						Query query = session.createSQLQuery(sql);
				        for (String paramName:sqlParams.keySet()) {
				        	if(!paramName.equals("sql")) {
				        		query.setParameter(paramName, sqlParams.get(paramName));
				        	}
				        }

						dataList = query.list();

						resultData = new ArrayList<AdReportVO>();
						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);


							Long pvSum = new Long(objArray[0].toString());
							Long clkSum = new Long(objArray[1].toString());
							Double price = (Double) objArray[2];
							
							double convertCount = ((BigDecimal)objArray[3]).doubleValue();		//總轉換數
							double convertPriceCount = ((BigDecimal)objArray[4]).doubleValue();	//總轉換價值
							double convertCVR = 0;	//轉換率
							double convertCost = 0;	//平均轉換成本
							double convertInvestmentCost = 0;	//廣告投資報酬率
							
							AdReportVO adReportVO = new AdReportVO();

							adReportVO.setKwPvSum(df2.format(pvSum));
							adReportVO.setKwClkSum(df2.format(clkSum));
							adReportVO.setKwPriceSum(df3.format(price));

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
                            //千次曝光收益
                            if (price==0 || pvSum==0) {
                                adReportVO.setPvPriceAvg(df.format(0));
                            } else {
                                adReportVO.setPvPriceAvg(df.format(price.doubleValue()*1000 / pvSum));
                            }
                            
                            
                            //商品廣告轉換
	                        //轉換次數
                            adReportVO.setConvertCountSum(objArray[3].toString());
	                        
	                        //總轉換價值
                            adReportVO.setConvertPriceCountSum(objArray[4].toString());
	                        
	                        //轉換率=轉換次數/互動數*100%
	                        if(convertCount > 0 && clkSum > 0){
	            				convertCVR  = (convertCount / clkSum) * 100;
	            			}
	                        adReportVO.setConvertCVR(df.format(convertCVR)+ "%");
	                        
	                        //平均轉換成本=費用/轉換次數
	                        if(price > 0 && convertCount > 0){
	            				convertCost = price / convertCount;
	            			}
	                        adReportVO.setConvertCost(df.format(convertCost));
	                        
	                        //廣告投資報酬率=總轉換價值/費用*100%
	            			if(convertPriceCount > 0 && price > 0){
	            				convertInvestmentCost = convertPriceCount / price * 100;
	            			}
	            			adReportVO.setConvertInvestmentCost(df.format(convertInvestmentCost)+ "%");
                            
                            
							resultData.add(adReportVO);
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
	public List<AdReportVO> getAdReportDetailList(final Map<String, String> conditionMap) throws Exception {
		List<AdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<AdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<AdReportVO> doInHibernate(Session session) throws HibernateException {

						HashMap<String, Object> sqlParams = new HashMap<String, Object>();
						StringBuffer sb = new StringBuffer();

						sb.append("select");
						sb.append(" kr.ad_seq,");
						sb.append(" kr.template_product_seq,");
						sb.append(" kr.pfp_customer_info_id,");
						sb.append(" sum(kr.ad_pv),");
                        sb.append(" sum(case when kr.ad_clk_price_type = 'CPC' then kr.ad_clk else kr.ad_view end),");
						sb.append(" sum(kr.ad_clk_price),");
						sb.append(" pci.customer_info_title, ");
						sb.append(" kr.pfd_customer_info_id, ");
						sb.append(" kr.ad_pvclk_date, ");
						sb.append(" sum(kr.convert_count),");
						sb.append(" sum(kr.convert_price_count)");
						sb.append(" from pfd_ad_report as kr ");
						sb.append(" left join pfp_customer_info pci ");
						sb.append(" on kr.pfp_customer_info_id = pci.customer_info_id ");
						sb.append(" where 1=1");

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdSeq"))) {
							sb.append(" and kr.ad_seq = :searchAdSeq ");
							sqlParams.put("searchAdSeq", conditionMap.get("searchAdSeq"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							sb.append(" and kr.ad_pvclk_date >= :startDate ");
							sqlParams.put("startDate", conditionMap.get("startDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							sb.append(" and kr.ad_pvclk_date <= :endDate ");
							sqlParams.put("endDate", conditionMap.get("endDate"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("adType"))) {
							sb.append(" and kr.ad_type = :adType ");
							sqlParams.put("adType", conditionMap.get("adType"));
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfdCustomerInfoId"))) {
							sb.append(" and kr.pfd_customer_info_id = :pfdCustomerInfoId ");
							sqlParams.put("pfdCustomerInfoId", conditionMap.get("pfdCustomerInfoId"));
						}

						if(StringUtils.isNotEmpty(conditionMap.get("searchText"))){
							if(StringUtils.isNotEmpty(conditionMap.get("searchType"))){
								if(StringUtils.equals("searchPfpId", conditionMap.get("searchType"))){
									sb.append(" and kr.pfp_customer_info_id like :searchText  ");
								} else {
									sb.append(" and pci.customer_info_title like :searchText ");
								}
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							} else {
								sb.append(" and (kr.pfp_customer_info_id like :searchText or ");
								sb.append("  pci.customer_info_title like :searchText ) ");
								sqlParams.put("searchText", "%" + conditionMap.get("searchText") + "%");
							}
						}

						sb.append(" group by kr.pfd_customer_info_id, kr.pfp_customer_info_id, kr.ad_seq, kr.ad_pvclk_date ");
						sb.append(" order by kr.pfd_customer_info_id, kr.pfp_customer_info_id, kr.ad_seq, kr.ad_pvclk_date ");

						String sql = sb.toString();
						List<Object> dataList = null;

						Query query = session.createSQLQuery(sql);
				        for (String paramName:sqlParams.keySet()) {
				        	if(!paramName.equals("sql")) {
				        		query.setParameter(paramName, sqlParams.get(paramName));
				        	}
				        }

						dataList = query.list();

						resultData = new ArrayList<AdReportVO>();
						for (int i=0; i<dataList.size(); i++) {

							Object[] objArray = (Object[]) dataList.get(i);

							String adSeq = (String) objArray[0];
							String adTemplateSeq = (String) objArray[1];
							String customerId = (String) objArray[2];
							Long pvSum = new Long(objArray[3].toString());
							Long clkSum = new Long(objArray[4].toString());
							Double price = (Double) objArray[5];
							String pfpCustomerInfoTitle = "";
							if(objArray[6] != null){
								pfpCustomerInfoTitle = objArray[6].toString();
							}
							String pfdCustomerInfoId = objArray[7].toString();
							String adPvclkDate = objArray[8].toString();
							double convertCount =  ((BigDecimal)objArray[9]).doubleValue();		//總轉換數
							double convertPriceCount =  ((BigDecimal)objArray[10]).doubleValue();	//總轉換價值
							double convertCVR = 0;	//轉換率
							double convertCost = 0;	//平均轉換成本
							double convertInvestmentCost = 0;	//廣告投資報酬率

							AdReportVO adReportVO = new AdReportVO();
							adReportVO.setAdSeq(adSeq);
							adReportVO.setAdTemplateSeq(adTemplateSeq);
							adReportVO.setCustomerId(customerId);
							adReportVO.setKwPvSum(df2.format(pvSum));
							adReportVO.setKwClkSum(df2.format(clkSum));
							adReportVO.setKwPriceSum(df3.format(price));
							adReportVO.setCustomerName(pfpCustomerInfoTitle);
							adReportVO.setPfdCustomerInfoId(pfdCustomerInfoId);
							adReportVO.setAdPvclkDate(adPvclkDate);
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
                            //千次曝光收益
                            if (price==0 || pvSum==0) {
                                adReportVO.setPvPriceAvg(df.format(0));
                            } else {
                                adReportVO.setPvPriceAvg(df.format(price.doubleValue()*1000 / pvSum));
                            }

                            
                            //商品廣告轉換
	                        //轉換次數
	                        adReportVO.setConvertCountSum(df2.format(convertCount));
	                        
	                        //總轉換價值
	                        adReportVO.setConvertPriceCountSum(df2.format(convertPriceCount));
	                        
	                        //轉換率=轉換次數/互動數*100%
	                        if(convertCount > 0 && clkSum > 0){
	            				convertCVR  = (convertCount / clkSum) * 100;
	            			}
	                        adReportVO.setConvertCVR(df.format(convertCVR)+ "%");
	                        
	                        //平均轉換成本=費用/轉換次數
	                        if(price > 0 && convertCount > 0){
	            				convertCost = price / convertCount;
	            			}
	                        adReportVO.setConvertCost(df.format(convertCost));
	                        
	                        //廣告投資報酬率=總轉換價值/費用*100%
	            			if(convertPriceCount > 0 && price > 0){
	            				convertInvestmentCost = (convertPriceCount / price) * 100;
	            			}
	            			adReportVO.setConvertInvestmentCost(df.format(convertInvestmentCost)+ "%");

	            			
							resultData.add(adReportVO);
						}

						return resultData;
					}
				});
    	return result;
	}

}
