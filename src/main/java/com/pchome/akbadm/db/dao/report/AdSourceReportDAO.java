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

public class AdSourceReportDAO extends BaseDAO<PfpAdReportVO, String> implements IAdSourceReportDAO {

	List<PfpAdReportVO> resultData = new ArrayList<PfpAdReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");

	@Override
    public List<PfpAdReportVO> getAdSourceReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
						
						hql.append(" SELECT a.ad_pv,  ");
						hql.append(" a.ad_clk,  ");
						hql.append(" Ifnull(( ( total_bonus * ad_clk ) / ( e.click_total ) ), 0.00), "); 
						hql.append(" a.ad_pvclk_date, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.category, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.count_customer_info_id, "); 
						hql.append(" Ifnull(a.ad_invalid_clk_price * Ifnull(a.pfb_bonus_percent, 0) / 100,0.00), "); 
						hql.append(" a.ad_clk_price, "); 
						hql.append(" CASE "); 
						hql.append(" WHEN a.category = '1' THEN a.contact_name "); 
						hql.append(" ELSE a.company_name "); 
						hql.append(" END, "); 
						hql.append(" a.ad_url, "); 
						hql.append(" a.website_chinese_name "); 
						hql.append(" FROM  (SELECT "); 
						hql.append(" Sum(a.ad_pv) ad_pv, "); 
						hql.append(" Sum(a.ad_clk - "); 
						hql.append(" a.ad_invalid_clk) ad_clk, "); 
						hql.append(" Ifnull((SELECT c.total_bonus  ");
						hql.append(" FROM   pfbx_bonus_day_report c  ");
						hql.append(" WHERE  c.report_date = a.ad_pvclk_date "); 
						hql.append(" AND c.pfb_id = a.pfbx_customer_info_id), 0.00) "); 
						hql.append(" total_bonus, "); 
						hql.append(" Ifnull((SELECT c.pfb_bonus_percent "); 
						hql.append(" FROM   pfbx_bonus_day_report c "); 
						hql.append(" WHERE  c.report_date = a.ad_pvclk_date "); 
						hql.append(" AND c.pfb_id = a.pfbx_customer_info_id), 0.00) "); 
						hql.append(" pfb_bonus_percent, "); 
						hql.append(" a.ad_pvclk_date, "); 
						hql.append(" a.ad_url, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" b.category, "); 
						hql.append(" b.contact_name,  ");
						hql.append(" b.company_name, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" b.website_chinese_name, "); 
						hql.append(" Sum(a.ad_clk_price - a.ad_invalid_clk_price) ad_clk_price, "); 
						hql.append(" Sum(a.ad_invalid_clk_price)  ");
						hql.append(" ad_invalid_clk_price,  ");
						hql.append(" Count(DISTINCT a.customer_info_id)  count_customer_info_id "); 
						hql.append(" FROM  (SELECT a.customer_info_id,  ");
						hql.append(" a.ad_clk,  ");
						hql.append(" a.ad_clk_price, "); 
						hql.append(" a.ad_pv, "); 
						hql.append(" a.ad_invalid_clk_price, "); 
						hql.append(" a.ad_invalid_clk, "); 
						hql.append(" a.ad_pvclk_date, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.ad_url "); 
						hql.append(" FROM   adm_pfpd_ad_pvclk_report a "); 
						hql.append(" WHERE  1 = 1 "); 
						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" AND a.ad_pvclk_date BETWEEN '"+conditionMap.get("startDate")+"'");
						}
						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" AND '"+conditionMap.get("endDate")+"'");
						}
						hql.append(" )a,  ");
						hql.append(" pfbx_customer_info b "); 
						hql.append(" WHERE  1 = 1 "); 
						hql.append(" AND a.pfbx_customer_info_id = b.customer_info_id "); 
						hql.append(" GROUP  BY a.ad_pvclk_date, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.ad_url "); 
						hql.append(" ORDER  BY a.ad_pvclk_date DESC, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.ad_url_name)a "); 
						hql.append(" LEFT JOIN (SELECT e.customer_info_id, "); 
						hql.append(" e.ad_pvclk_date,  ");
						hql.append(" e.pfbx_customer_info_id, "); 
						hql.append(" Sum(e.ad_clk - e.ad_invalid_clk) click_total "); 
						hql.append(" FROM   adm_pfpd_ad_pvclk_report e "); 
						hql.append("  WHERE  1 = 1 ");
						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" AND e.ad_pvclk_date BETWEEN '"+conditionMap.get("startDate")+"'");
						}
						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" AND '"+conditionMap.get("endDate")+"'");
						}
						hql.append(" GROUP  BY e.ad_pvclk_date, "); 
						hql.append(" e.pfbx_customer_info_id)e "); 
						hql.append(" ON a.pfbx_customer_info_id = e.pfbx_customer_info_id "); 
						hql.append(" AND a.ad_pvclk_date = e.ad_pvclk_date "); 
						

//				        hql.append(" select ");
//						hql.append(" sum(a.ad_pv), ");
//						hql.append(" sum(a.ad_clk - a.ad_invalid_clk), ");
//						hql.append(" IFNULL(c.total_bonus*sum(a.ad_clk - a.ad_invalid_clk)/ ");
//						hql.append(" (select sum(e.ad_clk - e.ad_invalid_clk) from adm_pfpd_ad_pvclk_report e  ");
//						hql.append(" where a.pfbx_customer_info_id = e.pfbx_customer_info_id and a.ad_pvclk_date = e.ad_pvclk_date ),0.00), ");
//						hql.append(" a.ad_pvclk_date,");
//						hql.append(" a.pfbx_customer_info_id,");
//						hql.append(" b.category, ");
//						hql.append(" a.ad_url_name, ");
//						hql.append(" count(distinct a.customer_info_id), ");
//						hql.append(" IFNULL(sum(a.ad_invalid_clk_price)*IFNULL(c.pfb_bonus_percent,0)/100,0.00), ");
//						hql.append(" sum(a.ad_clk_price - a.ad_invalid_clk_price), ");
//						hql.append(" case when b.category = '1' then b.contact_name else b.company_name end, ");
//						hql.append(" a.ad_url, ");
//						hql.append(" b.website_chinese_name ");
//						hql.append(" from adm_pfpd_ad_pvclk_report a ");
//						hql.append(" join pfbx_customer_info b ");
//						hql.append(" on a.pfbx_customer_info_id = b.customer_info_id ");
//						hql.append(" left join pfbx_bonus_day_report c ");
//						hql.append(" on a.pfbx_customer_info_id = c.pfb_id ");
//						hql.append(" and a.ad_pvclk_date = c.report_date ");
//						hql.append(" where 1 = 1 ");

//						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
//							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
//							hql.append(" and '"+conditionMap.get("endDate")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("category"))){
//							hql.append(" and b.category= '"+conditionMap.get("category")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
//							hql.append(" and (a.pfbx_customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
//							hql.append(" or a.ad_url_name like '%"+conditionMap.get("searchAll")+"%')");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
//							hql.append(" and a.pfbx_customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("websiteChineseName"))) {
//							hql.append(" and a.ad_url_name like '%"+conditionMap.get("websiteChineseName")+"%'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
//							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
//						}
//						
//						hql.append(" group by a.ad_pvclk_date, a.pfbx_customer_info_id, a.ad_url_name, a.ad_url ");
//
//						if(StringUtils.isNotEmpty(conditionMap.get("emailReport"))){
//							hql.append(" order by a.ad_pvclk_date desc, a.pfbx_customer_info_id, a.ad_url_name ");
//						} else {
//							hql.append(" order by a.ad_pvclk_date, a.pfbx_customer_info_id, a.ad_url_name ");
//						}


						String sql = hql.toString();
						log.info(">>> sql = " + sql);
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
								vo.setAdPvRate(df3.format(price.doubleValue()*1000 / pvSum) );
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setTotalBonus(df3.format(new Double((objArray[2]).toString())));

							if(objArray[3] != null){
								vo.setAdPvclkDate((objArray[3]).toString());
							}

							if(objArray[4] != null){
								vo.setPfbCustomerInfoId((objArray[4]).toString());
							}

							if(objArray[5] != null){
								if("1".equals((objArray[5]).toString())){
			    					vo.setPfbCategory("個人戶");
			    				} else {
			    					vo.setPfbCategory("公司戶");
			    				}
							}

							if(objArray[6] != null){
								vo.setPfbWebsiteChineseName((objArray[6]).toString());
							}

							if(objArray[7] != null){
								vo.setPfpCustomerInfoId(df2.format(new Long((objArray[7]).toString())));
							}

							if(objArray[8] != null){
								vo.setInvalidBonus(df3.format(new Double((objArray[8]).toString())));
							}

							if(objArray[9] != null){
								vo.setAdClkPriceSum(df2.format(new Double((objArray[9]).toString())));
							}

							if(objArray[10] != null){
								vo.setPfbCustomerInfoName(objArray[10].toString());
							}
							
							if(objArray[11] != null){
								vo.setPfbWebsiteDisplayUrl(objArray[11].toString());
							}
							
							if(objArray[12] != null){
								vo.setPfbDefaultWebsiteChineseName(objArray[12].toString());
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
    public List<PfpAdReportVO> getAdSourceDetalReportByCondition(final Map<String, String> conditionMap) {
		List<PfpAdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
				        hql.append("select ");
						hql.append(" sum(a.ad_pv), ");
						hql.append(" sum(a.ad_clk - a.ad_invalid_clk), ");
						hql.append(" sum(a.ad_clk_price - a.ad_invalid_clk_price), ");
						hql.append(" a.ad_pvclk_date,");
						hql.append(" a.customer_info_id, ");
						hql.append(" b.customer_info_title, ");
						hql.append(" a.pfd_customer_info_id, ");
						hql.append(" d.category, ");
						hql.append(" a.pfbx_customer_info_id, ");
						hql.append(" a.ad_url_name, ");
						hql.append(" a.ad_url, ");
						hql.append(" a.time_code, ");
						hql.append(" case when d.category = '1' then d.contact_name else d.company_name end, ");
						hql.append(" d.website_chinese_name ");
						hql.append(" from adm_pfpd_ad_pvclk_report a ");
						hql.append(" join pfp_customer_info b ");
						hql.append(" on a.customer_info_id = b.customer_info_id ");
						hql.append(" join pfbx_customer_info d ");
						hql.append(" on a.pfbx_customer_info_id = d.customer_info_id ");
						hql.append(" where 1 = 1 ");

						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" and '"+conditionMap.get("endDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("category"))){
							hql.append(" and b.category= '"+conditionMap.get("category")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
							hql.append(" and (a.pfbx_customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
							hql.append(" or a.ad_url_name like '%"+conditionMap.get("searchAll")+"%')");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
							hql.append(" and a.pfbx_customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("websiteChineseName"))) {
							hql.append(" and a.ad_url_name like '%"+conditionMap.get("websiteChineseName")+"%'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("pfbCustomerInfoId"))){
							hql.append(" and a.pfbx_customer_info_id= '"+conditionMap.get("pfbCustomerInfoId")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("adPvclkDate"))){
							hql.append(" and a.ad_pvclk_date= '"+conditionMap.get("adPvclkDate")+"'");
						}

						if (StringUtils.isNotEmpty(conditionMap.get("searchAdDevice"))){
							hql.append(" and a.ad_pvclk_device= '"+conditionMap.get("searchAdDevice")+"'");
						}
						
						if (StringUtils.isNotEmpty(conditionMap.get("allowUrl"))){
							hql.append(" and a.ad_url = '"+conditionMap.get("allowUrl")+"'");
						}
						
						if (StringUtils.isNotEmpty(conditionMap.get("allowUrlName"))){
							hql.append(" and a.ad_url_name = '"+conditionMap.get("allowUrlName")+"'");
						}
						
						hql.append(" group by a.ad_pvclk_date, a.customer_info_id, a.pfbx_customer_info_id, a.time_code, a.ad_url_name, a.ad_url ");
						hql.append(" order by a.ad_pvclk_date, a.customer_info_id, a.pfbx_customer_info_id, a.time_code, a.ad_url_name, a.ad_url ");

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
								if("1".equals((objArray[7]).toString())){
			    					vo.setPfbCategory("個人戶");
			    				} else {
			    					vo.setPfbCategory("公司戶");
			    				}
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
								vo.setPfbCustomerInfoName(objArray[12].toString());
							}
							
							if(objArray[13] != null){
								vo.setPfbDefaultWebsiteChineseName(objArray[13].toString());
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
    public List<PfpAdReportVO> getAdSourceReportSumByCondition(final Map<String, String> conditionMap) {
		List<PfpAdReportVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdReportVO>>() {
					@Override
                    @SuppressWarnings("unchecked")
					public List<PfpAdReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer();
						
						hql.append(" SELECT Sum(a.ad_pv),  ");
						hql.append(" Sum(a.ad_clk),  ");
						hql.append(" Sum(a.total_bonus),  ");
						hql.append(" a.pfbx_customer_info_id,  ");
						hql.append(" a.category,  ");
						hql.append(" a.ad_url_name,  ");
						hql.append(" Sum(DISTINCT a.count_customer_info_id),  ");
						hql.append(" Sum(a.invalid_bonus),  ");
						hql.append(" Sum(a.ad_clk_price),  ");
						hql.append(" a.pfb_customer_info_name  ");
						hql.append(" FROM   (SELECT a.ad_pv,  ");
						hql.append(" a.ad_clk,  ");
						hql.append(" a.total_bonus,  ");
						hql.append(" a.pfbx_customer_info_id,  ");
						hql.append(" a.category,  ");
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.count_customer_info_id, "); 
						hql.append(" Ifnull(a.ad_invalid_clk_price * Ifnull(a.pfb_bonus_percent, 0) /100, 0.00) invalid_bonus, "); 
						hql.append(" a.ad_clk_price, "); 
						hql.append(" (CASE "); 
						hql.append(" WHEN a.category = '1' THEN a.contact_name "); 
						hql.append(" ELSE a.company_name "); 
						hql.append(" END )pfb_customer_info_name, "); 
						hql.append(" a.ad_url, "); 
						hql.append(" a.website_chinese_name "); 
						hql.append(" FROM  (SELECT "); 
						hql.append(" Sum(a.ad_pv)  ad_pv, "); 
						hql.append(" Sum(a.ad_clk - a.ad_invalid_clk)  ad_clk, "); 
						hql.append(" Ifnull((SELECT c.total_bonus "); 
						hql.append(" FROM   pfbx_bonus_day_report c "); 
						hql.append(" WHERE  c.report_date = a.ad_pvclk_date "); 
						hql.append(" AND c.pfb_id = a.pfbx_customer_info_id), 0.00) "); 
						hql.append(" total_bonus, "); 
						hql.append(" Ifnull((SELECT c.pfb_bonus_percent "); 
						hql.append(" FROM   pfbx_bonus_day_report c "); 
						hql.append(" WHERE  c.report_date = a.ad_pvclk_date "); 
						hql.append(" AND c.pfb_id = a.pfbx_customer_info_id), 0.00) "); 
						hql.append(" pfb_bonus_percent, "); 
						hql.append(" a.ad_pvclk_date, "); 
						hql.append(" a.ad_url,  ");
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" b.category, "); 
						hql.append(" b.contact_name,  ");
						hql.append(" b.company_name, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" b.website_chinese_name, "); 
						hql.append(" Sum(a.ad_clk_price - "); 
						hql.append(" a.ad_invalid_clk_price)                  ad_clk_price, "); 
						hql.append(" Sum(a.ad_invalid_clk_price) "); 
						hql.append(" ad_invalid_clk_price, "); 
						hql.append(" Count(DISTINCT "); 
						hql.append(" a.customer_info_id)                            count_customer_info_id "); 
						hql.append(" FROM  (SELECT a.customer_info_id, "); 
						hql.append(" a.ad_clk, "); 
						hql.append(" a.ad_clk_price, "); 
						hql.append(" a.ad_pv, "); 
						hql.append(" a.ad_invalid_clk_price, "); 
						hql.append(" a.ad_invalid_clk, "); 
						hql.append(" a.ad_pvclk_date, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.ad_url "); 
						hql.append(" FROM   adm_pfpd_ad_pvclk_report a "); 
						hql.append(" WHERE  1 = 1 "); 
						
						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" AND a.ad_pvclk_date BETWEEN '"+conditionMap.get("startDate")+"'");
						}
						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" AND '"+conditionMap.get("endDate")+"'");
						}
						hql.append(" )a, "); 
						hql.append(" pfbx_customer_info b "); 
						hql.append(" WHERE  1 = 1 "); 
						hql.append(" AND a.pfbx_customer_info_id = b.customer_info_id "); 
						hql.append(" GROUP  BY a.ad_pvclk_date, "); 
						hql.append(" a.pfbx_customer_info_id, "); 
						hql.append(" a.ad_url_name, "); 
						hql.append(" a.ad_url)a "); 
						hql.append(" LEFT JOIN (SELECT e.customer_info_id, "); 
						hql.append(" e.ad_pvclk_date, "); 
						hql.append(" e.pfbx_customer_info_id, "); 
						hql.append(" Sum(e.ad_clk - e.ad_invalid_clk) click_total "); 
						hql.append(" FROM   adm_pfpd_ad_pvclk_report e "); 
						hql.append(" WHERE  1 = 1 "); 
						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
							hql.append(" AND e.ad_pvclk_date BETWEEN '"+conditionMap.get("startDate")+"'");
						}
						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
							hql.append(" AND '"+conditionMap.get("endDate")+"'");
						}
						hql.append(" GROUP  BY e.ad_pvclk_date, "); 
						hql.append(" e.pfbx_customer_info_id)e "); 
						hql.append(" ON a.pfbx_customer_info_id = e.pfbx_customer_info_id "); 
						hql.append(" AND a.ad_pvclk_date = e.ad_pvclk_date)a "); 
						hql.append(" GROUP  BY a.pfbx_customer_info_id "); 
						hql.append(" ORDER  BY a.pfbx_customer_info_id  ");
						
						
//						hql.append(" select ");
//						hql.append(" sum(ad_pv), ");
//						hql.append(" sum(ad_clk), ");
//						hql.append(" sum(total_bonus), ");
//						hql.append(" pfbx_customer_info_id,");
//						hql.append(" category, ");
//						hql.append(" ad_url_name, ");
//						hql.append(" sum(pfp_count), ");
//						hql.append(" sum(invalid_bonus), ");
//						hql.append(" sum(ad_clk_price), ");
//						hql.append(" pfb_customer_info_name ");
//						hql.append(" from ( ");
//				        hql.append("select ");
//						hql.append(" sum(a.ad_pv) ad_pv, ");
//						hql.append(" sum(a.ad_clk - a.ad_invalid_clk) ad_clk, ");
//						hql.append(" IFNULL(c.total_bonus,0.00) total_bonus, ");
//						hql.append(" a.ad_pvclk_date,");
//						hql.append(" a.pfbx_customer_info_id,");
//						hql.append(" b.category, ");
//						hql.append(" a.ad_url_name, ");
//						hql.append(" count(distinct a.customer_info_id) pfp_count, ");
//						hql.append(" IFNULL(sum(a.ad_invalid_clk_price)*IFNULL(c.pfb_bonus_percent,0)/100,0.00) invalid_bonus, ");
//						hql.append(" sum(a.ad_clk_price - a.ad_invalid_clk_price) ad_clk_price, ");
//						hql.append(" (case when b.category = '1' then b.contact_name else b.company_name end) pfb_customer_info_name ");
//						hql.append(" from adm_pfpd_ad_pvclk_report a ");
//						hql.append(" join pfbx_customer_info b ");
//						hql.append(" on a.pfbx_customer_info_id = b.customer_info_id ");
//						hql.append(" left join pfbx_bonus_day_report c ");
//						hql.append(" on a.pfbx_customer_info_id = c.pfb_id ");
//						hql.append(" and a.ad_pvclk_date = c.report_date ");
//						hql.append(" where 1 = 1 ");
//
//						if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
//							hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
//							hql.append(" and '"+conditionMap.get("endDate")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("category"))){
//							hql.append(" and b.category= '"+conditionMap.get("category")+"'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
//							hql.append(" and (a.pfbx_customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
//							hql.append(" or a.ad_url_name like '%"+conditionMap.get("searchAll")+"%')");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
//							hql.append(" and a.pfbx_customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
//						}
//
//						if (StringUtils.isNotEmpty(conditionMap.get("websiteChineseName"))) {
//							hql.append(" and a.ad_url_name like '%"+conditionMap.get("websiteChineseName")+"%'");
//						}
//
//						hql.append(" group by a.ad_pvclk_date, a.pfbx_customer_info_id ");
//						hql.append(" order by a.ad_pvclk_date, a.pfbx_customer_info_id ");
//						hql.append(" ) count_table ");
//						hql.append(" group by pfbx_customer_info_id ");
//						hql.append(" order by pfbx_customer_info_id ");

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
								vo.setAdPvRate(df3.format(price.doubleValue()*1000 / pvSum) );
							}

							vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
							vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
							vo.setTotalBonus(df3.format(new Double((objArray[2]).toString())));

							if(objArray[3] != null){
								vo.setPfbCustomerInfoId((objArray[3]).toString());
							}

							if(objArray[4] != null){
								if("1".equals((objArray[4]).toString())){
			    					vo.setPfbCategory("個人戶");
			    				} else {
			    					vo.setPfbCategory("公司戶");
			    				}
							}

							if(objArray[5] != null){
								vo.setPfbWebsiteChineseName((objArray[5]).toString());
							}

							if(objArray[6] != null){
								vo.setPfpCustomerInfoId(df2.format(new Long((objArray[6]).toString())));
							}

							if(objArray[7] != null){
								vo.setInvalidBonus(df3.format(new Double((objArray[7]).toString())));
							}

							if(objArray[8] != null){
								vo.setAdClkPriceSum(df2.format(new Double((objArray[8]).toString())));
							}

							if(objArray[9] != null){
								vo.setPfbCustomerInfoName(objArray[9].toString());
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
