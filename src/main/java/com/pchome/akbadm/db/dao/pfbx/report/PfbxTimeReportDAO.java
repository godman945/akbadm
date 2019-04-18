package com.pchome.akbadm.db.dao.pfbx.report;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxTimeReportDAO extends BaseDAO<PfbxReportVO, String> implements IPfbxTimeReportDAO {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	List<PfbxReportVO> resultData = new ArrayList<PfbxReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    public List<PfbxReportVO> getPfbxTimeReportByCondition(final Map<String, String> conditionMap) {
	List<PfbxReportVO> result = (List<PfbxReportVO>) getHibernateTemplate().execute(
			new HibernateCallback<List<PfbxReportVO>>() {
		@SuppressWarnings("unchecked")
		public List<PfbxReportVO> doInHibernate(Session session) throws HibernateException {
	    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
	    	
	    	String startDate = "";
	    	String endDate = "";
	    	String period = conditionMap.get("period");
	        StringBuffer hql = new StringBuffer();
	        hql.append("select ");
			hql.append("  sum(a.ad_pv), ");
			hql.append("  sum(a.ad_clk - a.ad_invalid_clk), ");			
			hql.append("  sum(a.ad_clk_price), ");	
//			hql.append("  sum(adInvalidClk), ");
//			hql.append("  sum(adInvalidClkPrice), ");
			hql.append(" a.ad_pvclk_date,");
//			hql.append(" adPvclkCustomer, ");	
			hql.append(" a.customer_info_id,");
			hql.append(" sum(b.total_bonus),");
			hql.append(" c.website_chinese_name,");
			hql.append(" c.website_display_url,");
			hql.append(" c.category ");
			if (period.equals("Month")){
				hql.append(", MONTHNAME(a.ad_pvclk_date) AS month");
			}
			hql.append(" from pfbx_ad_time_report a");
			hql.append(" join pfbx_customer_info c ");
			hql.append(" on a.customer_info_id = c.customer_info_id ");
			hql.append(" left join pfbx_bonus_day_report b ");
			hql.append(" on a.customer_info_id = b.pfb_id ");
			hql.append(" and a.ad_pvclk_date = b.report_date ");
			hql.append(" where 1 = 1 ");
			
			if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
				hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
				startDate = conditionMap.get("startDate");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
				hql.append(" and '"+conditionMap.get("endDate")+"'");
				endDate = conditionMap.get("endDate");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("searchCategory"))) {
				hql.append(" and c.category = '"+conditionMap.get("searchCategory")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
				hql.append(" and (c.customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
				hql.append(" or c.website_display_url like '%"+conditionMap.get("searchAll")+"%'");
				hql.append(" or c.website_chinese_name like '%"+conditionMap.get("searchAll")+"%')");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
				hql.append(" and c.customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("websiteDisplayUrl"))) {
				hql.append(" and c.website_display_url like '%"+conditionMap.get("websiteDisplayUrl")+"%'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("websiteChineseName"))) {
				hql.append(" and c.website_chinese_name like '%"+conditionMap.get("websiteChineseName")+"%'");
			}
			
			if(period.equals("Day")){
				hql.append(" group by a.ad_pvclk_date, a.customer_info_id");
				hql.append(" order by a.ad_pvclk_date, a.customer_info_id");
			}else if(period.equals("Week")){
				
				//hql.append(" group by WEEKOFYEAR(adPvclkDate )");
				//hql.append(" order by WEEKOFYEAR(adPvclkDate )");
				
				hql.append(" group by WEEK(a.ad_pvclk_date ), a.customer_info_id");
				hql.append(" order by WEEK(a.ad_pvclk_date ), a.customer_info_id");
				
			}else if(period.equals("Month")){
				hql.append(" group by month (a.ad_pvclk_date), a.customer_info_id");
				hql.append(" order by month (a.ad_pvclk_date), a.customer_info_id");
			}
			
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
			log.info(">>> dataList.size() = " + dataList.size());
	
			resultData = new ArrayList<PfbxReportVO>();
			for (int i=0; i<dataList.size(); i++) {
				PfbxReportVO vo = new PfbxReportVO();
				
				Object[] objArray = (Object[]) dataList.get(i);
				
				log.info("length: "+objArray.length);
				
				log.info(">>>pvSum: "+objArray[0]);
				log.info(">>>clkSum: "+objArray[1]);
				log.info(">>>>price: "+objArray[2]);
				log.info(">>>3: "+objArray[3]);
				log.info(">>>4: "+objArray[4]);
				log.info(">>>5: "+objArray[5]);
				
				Long pvSum = new Long(objArray[0].toString());
				Long clkSum = new Long(objArray[1].toString());
				
				Double price = new Double(0);
				if(objArray[5] != null){
					price = (Double) objArray[5];
				}
				
				if (clkSum==0 || pvSum==0) {
					vo.setAdClkRate(df.format(0) + "%");
				} else {
					vo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
				}
				
				if (price==0 || clkSum==0) {
					vo.setAdClkAvgPrice(df.format(0));
					vo.setAdPvRate(df.format(0));
				} else {
					vo.setAdClkAvgPrice(df3.format(price.doubleValue() / clkSum.doubleValue()));
					vo.setAdPvRate(df3.format(price.doubleValue()*1000 / pvSum) );
				}
	
				vo.setAdPvSum(df2.format(new Long(objArray[0].toString())));
				vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
				vo.setAdClkPriceSum(((Double)objArray[2]).toString());
				
				if(period.equals("Week")){
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					String startDayForWeek = "";
					String endDayForWeek = "";
					String date = "";
					
					String[] timeArray = (objArray[3]).toString().split("-");
					cal.set(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) -1, Integer.parseInt(timeArray[2]));
					log.info("日期："+df.format(cal.getTime()));
					cal.add(Calendar.DAY_OF_WEEK,(-1)*cal.get(Calendar.DAY_OF_WEEK)+1);
					startDayForWeek = df.format(cal.getTime());
					log.info("本週第一天："+startDayForWeek);
			        cal.add(Calendar.DAY_OF_WEEK,7-cal.get(Calendar.DAY_OF_WEEK));
			        endDayForWeek = df.format(cal.getTime());
			        log.info("本週最後一天："+endDayForWeek);
					
			        if((!startDate.equals("")) && startDate.compareTo(startDayForWeek) > 0 ){
			        	date = startDate;
			        } else {
			        	date = startDayForWeek;
			        }
					
			        date += "~";
			        
			        if((!endDate.equals("")) && endDate.compareTo(endDayForWeek) < 0 ){
			        	date += endDate;
			        } else {
			        	date += endDayForWeek;
			        }
			        
			        vo.setAdPvclkDate(date);
				} else if(period.equals("Month")){
					DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					String startDayForMonth = "";
					String endDayForMonth = "";
					String date = "";
					
					String[] timeArray = (objArray[3]).toString().split("-");
					cal.set(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]) -1, Integer.parseInt(timeArray[2]));
					log.info("日期："+df.format(cal.getTime()));
					cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
					startDayForMonth = df.format(cal.getTime());
					log.info("本月第一天："+startDayForMonth);
					cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					endDayForMonth = df.format(cal.getTime());
			        log.info("本月最後一天："+endDayForMonth);
					
			        if((!startDate.equals("")) && startDate.compareTo(startDayForMonth) > 0 ){
			        	date = startDate;
			        } else {
			        	date = startDayForMonth;
			        }
					
			        date += "~";
			        
			        if((!endDate.equals("")) && endDate.compareTo(endDayForMonth) < 0 ){
			        	date += endDate;
			        } else {
			        	date += endDayForMonth;
			        }
			        
			        vo.setAdPvclkDate(date);
				} else {
					vo.setAdPvclkDate((objArray[3]).toString());	
				}
				
				vo.setCustomerInfoId((objArray[4]).toString());
				
				if(objArray[5] != null){
					vo.setTotalBonus(df3.format(Double.parseDouble(objArray[5].toString())));	
				}else {
					vo.setTotalBonus("0.00");
				}
				
				if(objArray[6] != null){
					vo.setWebsiteChineseName((objArray[6]).toString());
				}
				
				if(objArray[7] != null){
					vo.setWebsiteDisplayUrl((objArray[7]).toString());
				}
				
				if(objArray[8] != null){
					if("1".equals((objArray[8]).toString())){
    					vo.setWebsiteCategory("個人戶");
    				} else {
    					vo.setWebsiteCategory("公司戶");
    				}
				}
				
				resultData.add(vo);
				log.info("resultData: "+resultData.size());
			}
		
	
			return resultData;
		}
	}
);
    	return result;

    }
}
