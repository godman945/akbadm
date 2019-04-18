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
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;

public class PfbxCustomerReportDAO extends BaseDAO<PfbxCustomerReportVo, String> implements IPfbxCustomerReportDAO {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	List<PfbxCustomerReportVo> resultData = new ArrayList<PfbxCustomerReportVo>();
	DecimalFormat df = new DecimalFormat("#.##");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,###.##");
    public List<PfbxCustomerReportVo> getPfbxCustomerReportByCondition(final Map<String, String> conditionMap, final Map<String, String> conditionMap1) {
	List<PfbxCustomerReportVo> result = (List<PfbxCustomerReportVo>) getHibernateTemplate().execute(
			new HibernateCallback<List<PfbxCustomerReportVo>>() {
		@SuppressWarnings("unchecked")
		public List<PfbxCustomerReportVo> doInHibernate(Session session) throws HibernateException {
	    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
	        StringBuffer hql = new StringBuffer();
	        hql.append("select ");
			hql.append("  sum(ad.adPv), ");
			hql.append("  sum(ad.adClk), ");			
			hql.append("  sum(ad.adClkPrice), ");	
//			hql.append("  sum(adInvalidClk), ");
//			hql.append("  sum(adInvalidClkPrice), ");
			hql.append(" ad.adPvclkCustomer, ");	
			hql.append(" ad.customerInfoId, ");
			hql.append("  sum(bonus.totalCharge), ");			
			hql.append("  sum(bonus.totalBonus) ");
			hql.append(" from PfbxAdCustomerReport ad, PfbxBonusDayReport bonus ");
			hql.append(" where 1 = 1 ");
			hql.append(" and ad.adPvclkDate = bonus.reportDate ");
			hql.append(" and ad.customerInfoId = bonus.pfbxCustomerInfo.customerInfoId ");
			
			if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
				hql.append(" and ad.adPvclkDate between '"+conditionMap.get("startDate")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
				hql.append(" and '"+conditionMap.get("endDate")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap1.get("customer"))) {
				hql.append(" and ad.adPvclkCustomer = '"+conditionMap1.get("customer")+"'");
			}
			
			if (StringUtils.isNotEmpty(conditionMap1.get("PfbxCustomerInfoId"))) {
				hql.append(" and ad.customerInfoId = '" + conditionMap1.get("PfbxCustomerInfoId")+"'");
			}
//			hql.append("   and customerInfoId = :customerInfoId");
//			hql.append("   and adPvclkDate between :startDate and :endDate");
			
			/*try {
				sqlParams.put("customerInfoId", conditionMap1.get("PfbxCustomerInfoId"));
				sqlParams.put("startDate", sdf.parse(conditionMap.get("startDate")));
				sqlParams.put("endDate", sdf.parse(conditionMap.get("endDate")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			hql.append(" group by ad.adPvclkCustomer");
			hql.append(" order by ad.adPvclkCustomer");
			
			String sql = hql.toString();
			log.info(">>> sql = " + sql);
			List<Object> dataList = new ArrayList<Object>();
			try {
			Query query = session.createQuery(sql);
			dataList = query.list();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			log.info(">>> dataList.size() = " + dataList.size());
	
			resultData = new ArrayList<PfbxCustomerReportVo>();
			for (int i=0; i<dataList.size(); i++) {
				PfbxCustomerReportVo pfbxCustomerReportVo = new PfbxCustomerReportVo();
				
				Object[] objArray = (Object[]) dataList.get(i);
				
				log.info("length: "+objArray.length);
				
				log.info(">>>pvSum: "+objArray[0]);
				log.info(">>>clkSum: "+objArray[1]);
				log.info(">>>>price: "+objArray[2]);
				log.info(">>>3: "+objArray[3]);
				log.info(">>>4: "+objArray[4]);
				
				Long pvSum = (Long) objArray[0];
				Long clkSum = (Long) objArray[1];
				Double price = (Double) objArray[2];
				
//				log.info("pvSum: "+pvSum);
//				log.info("clkSum: "+clkSum);
//				log.info("price: "+price);
				
				if (clkSum==0 || pvSum==0) {
					pfbxCustomerReportVo.setAdClkRate(df.format(0) + "%");
				} else {
					pfbxCustomerReportVo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
				}
				
				if (price==0 || clkSum==0) {
					pfbxCustomerReportVo.setAdClkAvgPrice(df.format(0));
					pfbxCustomerReportVo.setAdPvRate(df.format(0));
//					pfbxCustomerReportVo.setAdClkPriceSum(df.format(0));
				} else {
					pfbxCustomerReportVo.setAdClkAvgPrice(df.format(price.doubleValue() / clkSum.doubleValue()));
					pfbxCustomerReportVo.setAdPvRate(df.format(price.doubleValue() / (pvSum/1000)) );
//					pfbxCustomerReportVo.setAdClkPriceSum(df.format(price));
				}
	
				pfbxCustomerReportVo.setAdPvSum(df2.format(Integer.parseInt((objArray[0]).toString())));
				pfbxCustomerReportVo.setAdClkSum(df2.format(Integer.parseInt((objArray[1]).toString())));
				pfbxCustomerReportVo.setAdClkPriceSum((df3.format((Double)objArray[2]).toString()));
				pfbxCustomerReportVo.setAdPvClkCustomer((objArray[3]).toString());
				pfbxCustomerReportVo.setCustomerInfoId((objArray[4]).toString());
				if(conditionMap1.size()>0){
					pfbxCustomerReportVo.setWebsiteChineseName(conditionMap1.get("websiteChineseName"));
					pfbxCustomerReportVo.setWebsiteDisplayUrl(conditionMap1.get("websiteDisplayUrl"));
				}
				
				pfbxCustomerReportVo.setTotalChargeSum(df3.format((Double)objArray[5]).toString());
				pfbxCustomerReportVo.setTotalBonusSum(df3.format((Double)objArray[6]).toString());
				
				resultData.add(pfbxCustomerReportVo);
				log.info("resultData: "+resultData.size());
			}
		
	
			return resultData;
		}
	}
);
    	return result;

    }
}
