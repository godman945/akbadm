package com.pchome.akbadm.db.dao.pfbx.report;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxStyleReportDAO extends BaseDAO<PfbxReportVO, String> implements IPfbxStyleReportDAO {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	List<PfbxReportVO> resultData = new ArrayList<PfbxReportVO>();
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    public List<PfbxReportVO> getPfbxStyleReportByCondition(final Map<String, String> conditionMap) {
	List<PfbxReportVO> result = (List<PfbxReportVO>) getHibernateTemplate().execute(
			new HibernateCallback<List<PfbxReportVO>>() {
		@SuppressWarnings("unchecked")
		public List<PfbxReportVO> doInHibernate(Session session) throws HibernateException {
	    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
	    	
	        StringBuffer hql = new StringBuffer();
	        hql.append("select ");
			hql.append("  sum(a.ad_pv), ");
			hql.append("  sum(a.ad_clk - a.ad_invalid_clk), ");			
			hql.append("  sum(a.ad_clk_price), ");	
//			hql.append("  sum(adInvalidClk), ");
//			hql.append("  sum(adInvalidClkPrice), ");
			hql.append(" (sum(a.ad_clk_price) - sum(a.ad_invalid_clk_price)) ");
			hql.append(" /(select sum(d.ad_clk_price) - sum(d.ad_invalid_clk_price) from pfbx_ad_style_report d ");
			hql.append(" where a.customer_info_id = d.customer_info_id and a.ad_pvclk_date = d.ad_pvclk_date ) ");
			hql.append(" *(b.total_bonus),");
			hql.append(" a.ad_pvclk_date,");
//			hql.append(" adPvclkCustomer, ");
			hql.append(" a.ad_pvclk_style,");
			hql.append(" a.customer_info_id,");
			hql.append(" c.website_chinese_name,");
			hql.append(" c.website_display_url,");
			hql.append(" c.category ");
			hql.append(" from pfbx_ad_style_report a");
			hql.append(" join pfbx_customer_info c ");
			hql.append(" on a.customer_info_id = c.customer_info_id ");
			hql.append(" left join pfbx_bonus_day_report b ");
			hql.append(" on a.customer_info_id = b.pfb_id ");
			hql.append(" and a.ad_pvclk_date = b.report_date ");
			hql.append(" where 1 = 1 ");
			
			if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
				hql.append(" and a.ad_pvclk_date between '"+conditionMap.get("startDate")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
				hql.append(" and '"+conditionMap.get("endDate")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("style"))) {
				hql.append(" and a.ad_pvclk_style= '"+conditionMap.get("style")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("searchCategory"))) {
				hql.append(" and c.category = '"+conditionMap.get("searchCategory")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("searchAll"))) {
				hql.append(" and (c.customer_info_id like '%"+conditionMap.get("searchAll")+"%'");
				hql.append(" or c.website_chinese_name like '%"+conditionMap.get("searchAll")+"%')");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("customerInfoId"))) {
				hql.append(" and c.customer_info_id like '%"+conditionMap.get("customerInfoId")+"%'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("websiteChineseName"))) {
				hql.append(" and c.website_chinese_name like '%"+conditionMap.get("websiteChineseName")+"%'");
			}
			
			hql.append(" group by a.ad_pvclk_date, a.customer_info_id, a.ad_pvclk_style ");
			hql.append(" order by a.ad_pvclk_date, a.customer_info_id, a.ad_pvclk_style ");
			
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
				
				Long pvSum = new Long(objArray[0].toString());;
				Long clkSum = new Long(objArray[1].toString());;
				Double price = new Double(0);
				if(objArray[3] != null){
					price = (Double) objArray[3];
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
	
				vo.setAdPvSum(df2.format(new Long((objArray[0]).toString())));
				vo.setAdClkSum(df2.format(new Long((objArray[1]).toString())));
				vo.setAdClkPriceSum(((Double)objArray[2]).toString());
				if(objArray[3] != null){
					vo.setTotalBonus(df3.format(Double.parseDouble(objArray[3].toString())));	
				}else {
					vo.setTotalBonus("0.00");
				}
				vo.setAdPvclkDate((objArray[4]).toString());
				vo.setAdPvclkStyle((objArray[5]).toString() + "廣告");
				vo.setCustomerInfoId((objArray[6]).toString());
				if(objArray[7] != null){
					vo.setWebsiteChineseName((objArray[7]).toString());
				}
				
				if(objArray[8] != null){
					vo.setWebsiteDisplayUrl((objArray[8]).toString());
				}
				
				if(objArray[9] != null){
					if("1".equals((objArray[9]).toString())){
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
