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
import com.pchome.akbadm.db.vo.report.PfpAdTimeReportVO;
import com.pchome.enumerate.report.EnumAdTimeCode;

public class PfpAdWeektimeReportDAO extends BaseDAO<PfpAdTimeReportVO, String> implements IPfpAdWeektimeReportDAO  {

	List<PfpAdTimeReportVO> resultData = new ArrayList<PfpAdTimeReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.000");

	@Override
	public List<PfpAdTimeReportVO> getAdWeektimeReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdTimeReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdTimeReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdTimeReportVO> doInHibernate(Session session) throws HibernateException {

						String searchTime = "W";
						if(StringUtils.isNotEmpty(conditionMap.get("searchTime"))){
							searchTime = conditionMap.get("searchTime");
						}
						String adPvclkDevice = "";
						if(StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							adPvclkDevice = conditionMap.get("searchDevice");
						}

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
				        hql.append(" r.ad_pvclk_date,");
				        hql.append(" DAYOFWEEK(r.ad_pvclk_date),");
						hql.append(" r.time_code,");
						hql.append(" sum(r.ad_pv), ");
	                    hql.append(" sum(case when r.ad_clk_price_type = 'CPC' then r.ad_clk else r.ad_view end),"); // 產生pfp_ad_time_report 的時候，已經減過無效點擊數了，所以不用再減
						hql.append(" sum(r.ad_clk_price), ");		// 產生pfp_ad_time_report 的時候，已經減過無效點擊金額了，所以不用再減
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.ad_pvclk_device ");
						hql.append(" from pfp_ad_time_report as r ");
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

						if(StringUtils.isNotEmpty(searchTime) && StringUtils.equals(searchTime, "W") ){
							hql.append(" group by r.ad_pvclk_date, DAYOFWEEK(r.ad_pvclk_date)");
							hql.append(" order by r.ad_pvclk_date, DAYOFWEEK(r.ad_pvclk_date)");
						} else {
							hql.append(" group by r.ad_pvclk_date, r.time_code");
							hql.append(" order by r.ad_pvclk_date, r.time_code");
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

						resultData = new ArrayList<PfpAdTimeReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdTimeReportVO vo = new PfpAdTimeReportVO();

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

							String weekCode = (objArray[1]).toString();
							vo.setWeek(getWeekName(weekCode));

							String timeCode = "A";
							if(objArray[2] != null){
								timeCode = objArray[2].toString();
							}

							for(EnumAdTimeCode enumAdTimeCode:EnumAdTimeCode.values()){
								if(StringUtils.equals(timeCode, enumAdTimeCode.getCode())){
									vo.setTime(enumAdTimeCode.getName());
								}
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
	public List<PfpAdTimeReportVO> getAdWeektimeReportTotalByCondition(final Map<String, String> conditionMap) {
		List<PfpAdTimeReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdTimeReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdTimeReportVO> doInHibernate(Session session) throws HibernateException {

						String searchTime = "W";
						if(StringUtils.isNotEmpty(conditionMap.get("searchTime"))){
							searchTime = conditionMap.get("searchTime");
						}
						String adPvclkDevice = "";
						if(StringUtils.isNotEmpty(conditionMap.get("searchDevice"))){
							adPvclkDevice = conditionMap.get("searchDevice");
						}

						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
				        hql.append(" DAYOFWEEK(r.ad_pvclk_date),");
						hql.append(" r.time_code,");
						hql.append(" sum(r.ad_pv), ");
	                    hql.append(" sum(case when r.ad_clk_price_type = 'CPC' then r.ad_clk else r.ad_view end),"); // 產生pfp_ad_time_report 的時候，已經減過無效點擊數了，所以不用再減
						hql.append(" sum(r.ad_clk_price), ");		// 產生pfp_ad_time_report 的時候，已經減過無效點擊金額了，所以不用再減
						hql.append(" sum(r.ad_invalid_clk), ");
						hql.append(" sum(r.ad_invalid_clk_price), ");
						hql.append(" r.ad_pvclk_device ");
						hql.append(" from pfp_ad_time_report as r ");
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

						if(StringUtils.isNotEmpty(searchTime) && StringUtils.equals(searchTime, "W") ){
							hql.append(" group by DAYOFWEEK(r.ad_pvclk_date)");
							hql.append(" order by DAYOFWEEK(r.ad_pvclk_date)");
						} else {
							hql.append(" group by r.time_code");
							hql.append(" order by r.time_code");
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

						resultData = new ArrayList<PfpAdTimeReportVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdTimeReportVO vo = new PfpAdTimeReportVO();

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


							String weekCode = (objArray[0]).toString();
							vo.setWeek(getWeekName(weekCode));

							String timeCode = "A";
							if(objArray[1] != null){
								timeCode = objArray[1].toString();
							}

							for(EnumAdTimeCode enumAdTimeCode:EnumAdTimeCode.values()){
								if(StringUtils.equals(timeCode, enumAdTimeCode.getCode())){
									vo.setTime(enumAdTimeCode.getName());
								}
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

	private String getWeekName(String weekCode){
		String name = "";

		switch (Integer.valueOf(weekCode)) {
		case 1:
			name = "星期日";
			break;
		case 2:
			name = "星期一";
			break;
		case 3:
			name = "星期二";
			break;
		case 4:
			name = "星期三";
			break;
		case 5:
			name = "星期四";
			break;
		case 6:
			name = "星期五";
			break;
		case 7:
			name = "星期六";
			break;
		default:
			break;
		}

		return name;
	}

}
