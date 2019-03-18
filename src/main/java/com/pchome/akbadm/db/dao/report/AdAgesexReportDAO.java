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
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.report.PfpAdAgesexReportVO;
import com.pchome.enumerate.report.EnumAdAgeCode;

public class AdAgesexReportDAO extends BaseDAO<PfpAdAgesexReportVO, String> implements IAdAgesexReportDAO  {

	List<PfpAdAgesexReportVO> resultData = new ArrayList<PfpAdAgesexReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.000");

	@Override
	public List<PfpAdAgesexReportVO> getAdAgesexReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdAgesexReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdAgesexReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdAgesexReportVO> doInHibernate(Session session) throws HibernateException {

						String searchAgesex = "A";
						if(StringUtils.isNotEmpty(conditionMap.get("searchAgesex"))){
							searchAgesex = conditionMap.get("searchAgesex");
						}
						String adPvclkDevice = "";
						if(StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							adPvclkDevice = conditionMap.get("searchDevice");
						}

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
				        hql.append(" r.ad_pvclk_date,");
				        hql.append(" r.age_code,");
						hql.append(" r.sex,");
						hql.append(" sum(r.ad_pv), ");
	                    hql.append(" sum(case when r.ad_clk_price_type = 'CPC' then r.ad_clk else r.ad_view end),"); // 產生pfp_ad_time_report 的時候，已經減過無效點擊數了，所以不用再減
						hql.append(" sum(r.ad_clk_price), ");		// 產生pfp_ad_time_report 的時候，已經減過無效點擊金額了，所以不用再減
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.ad_pvclk_device ");
						hql.append(" from pfp_ad_age_report as r ");
						hql.append(" where 1=1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and r.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							hql.append(" and r.ad_pvclk_device= '"+conditionMap.get("searchDevice")+"'");
						}

						if(StringUtils.isNotEmpty(searchAgesex) && StringUtils.equals(searchAgesex, "A") ){
							hql.append(" group by r.ad_pvclk_date, r.age_code");
							hql.append(" order by r.ad_pvclk_date, r.age_code");
						} else {
							hql.append(" group by r.ad_pvclk_date, r.sex");
							hql.append(" order by r.ad_pvclk_date, r.sex desc");
						}


						String sql = hql.toString();
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						resultData = new ArrayList<PfpAdAgesexReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdAgesexReportVO vo = new PfpAdAgesexReportVO();

							Object[] objArray = (Object[]) dataList.get(i);



							Long pvSum = new Long(objArray[3].toString());;
							Long clkSum = new Long(objArray[4].toString());;
							Double price = new Double(0);
							if(objArray[5] != null){
								price = (Double) objArray[5];
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

							if(objArray[0] != null){
								vo.setReportDate((objArray[0]).toString());
							}

							String ageCode = "I";
							if(objArray[1] != null){
								ageCode = objArray[1].toString();
							}

							for(EnumAdAgeCode enumAdAgeCode:EnumAdAgeCode.values()){
								if(StringUtils.equals(ageCode, enumAdAgeCode.getCode())){
									vo.setAge(enumAdAgeCode.getName());
								}
							}

							if(objArray[2] != null){
								String sex = objArray[2].toString();
								if(StringUtils.equals(sex, "M")){
									vo.setSex("男性");
								} else {
									vo.setSex("女性");
								}
							} else {
								vo.setSex("不分性別");
							}

							vo.setAdPvSum(df2.format(new Long((objArray[3]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[4]).toString())));
							vo.setAdPriceSum(df3.format(new Double((objArray[5]).toString())));

							String adDevice = "";
							if(objArray[8] != null){
								adDevice = objArray[8].toString();
							}

							if(StringUtils.isNotBlank(adPvclkDevice)) {
								if("PC".equals(adDevice)){
									vo.setAdDevice("電腦");
								}else if("mobile".equals(adDevice)){
									vo.setAdDevice("行動裝置");
								} else {
									vo.setAdDevice(adDevice);
								}
							} else {
								vo.setAdDevice("全部");
							}

							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}

	@Override
	public List<PfpAdAgesexReportVO> getAdAgesexReportTotalByCondition(final Map<String, String> conditionMap) {
		List<PfpAdAgesexReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdAgesexReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdAgesexReportVO> doInHibernate(Session session) throws HibernateException {

						String searchAgesex = "A";
						if(StringUtils.isNotEmpty(conditionMap.get("searchAgesex"))){
							searchAgesex = conditionMap.get("searchAgesex");
						}
						String adPvclkDevice = "";
						if(StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							adPvclkDevice = conditionMap.get("searchDevice");
						}

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
				        hql.append(" r.age_code,");
						hql.append(" r.sex,");
						hql.append(" sum(r.ad_pv), ");
	                    hql.append(" sum(case when r.ad_clk_price_type = 'CPC' then r.ad_clk else r.ad_view end),"); // 產生pfp_ad_time_report 的時候，已經減過無效點擊數了，所以不用再減
						hql.append(" sum(r.ad_clk_price), ");		// 產生pfp_ad_time_report 的時候，已經減過無效點擊金額了，所以不用再減
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.ad_pvclk_device ");
						hql.append(" from pfp_ad_age_report as r ");
						hql.append(" where 1=1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and r.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							hql.append(" and r.ad_pvclk_device= '"+conditionMap.get("searchDevice")+"'");
						}

						if(StringUtils.isNotEmpty(searchAgesex) && StringUtils.equals(searchAgesex, "A") ){
							hql.append(" group by r.age_code");
							hql.append(" order by r.age_code");
						} else {
							hql.append(" group by r.sex");
							hql.append(" order by r.sex desc");
						}


						String sql = hql.toString();
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						resultData = new ArrayList<PfpAdAgesexReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdAgesexReportVO vo = new PfpAdAgesexReportVO();

							Object[] objArray = (Object[]) dataList.get(i);



							Long pvSum = new Long(objArray[2].toString());;
							Long clkSum = new Long(objArray[3].toString());;
							Double price = new Double(0);
							if(objArray[5] != null){
								price = (Double) objArray[4];
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


							String ageCode = "I";
							if(objArray[0] != null){
								ageCode = objArray[0].toString();
							}

							for(EnumAdAgeCode enumAdAgeCode:EnumAdAgeCode.values()){
								if(StringUtils.equals(ageCode, enumAdAgeCode.getCode())){
									vo.setAge(enumAdAgeCode.getName());
								}
							}

							if(objArray[1] != null){
								String sex = objArray[1].toString();
								if(StringUtils.equals(sex, "M")){
									vo.setSex("男性");
								} else {
									vo.setSex("女性");
								}
							} else {
								vo.setSex("不分性別");
							}

							vo.setAdPvSum(df2.format(new Long((objArray[2]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[3]).toString())));
							vo.setAdPriceSum(df3.format(new Double((objArray[4]).toString())));

							String adDevice = "";
							if(objArray[7] != null){
								adDevice = objArray[7].toString();
							}

							if(StringUtils.isNotBlank(adPvclkDevice)) {
								if("PC".equals(adDevice)){
									vo.setAdDevice("電腦");
								}else if("mobile".equals(adDevice)){
									vo.setAdDevice("行動裝置");
								} else {
									vo.setAdDevice(adDevice);
								}
							} else {
								vo.setAdDevice("全部");
							}

							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}

}
