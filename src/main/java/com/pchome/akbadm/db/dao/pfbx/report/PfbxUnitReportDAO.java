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
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;

public class PfbxUnitReportDAO extends BaseDAO<PfbxReportVO, String> implements IPfbxUnitReportDAO {
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	List<PfbxReportVO> resultData = new ArrayList<PfbxReportVO>();
	DecimalFormat df = new DecimalFormat("#.##");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
    @Override
    public List<PfbxReportVO> getPfbxUnitReportByCondition(final Map<String, String> conditionMap, final Map<String, String> conditionMap1) {
	List<PfbxReportVO> result = getHibernateTemplate().execute(
			new HibernateCallback<List<PfbxReportVO>>() {
		@Override
        @SuppressWarnings("unchecked")
		public List<PfbxReportVO> doInHibernate(Session session) throws HibernateException, SQLException {
	    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();

	    	String period = conditionMap.get("period");
	        StringBuffer hql = new StringBuffer();
	        hql.append("select ");
			hql.append("  sum(adPv), ");
			hql.append("  sum(adClk), ");
			hql.append("  sum(adClkPrice), ");
//			hql.append("  sum(adInvalidClk), ");
//			hql.append("  sum(adInvalidClkPrice), ");
//			hql.append(" adPvclkDate,");
//			hql.append(" adPvclkCustomer, ");
			hql.append(" adPvclkUnit, ");
			hql.append(" customerInfoId ");
			hql.append(" from PfbxAdUnitReport");
			hql.append(" where 1 = 1 ");

			if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
				hql.append(" and adPvclkDate between '"+conditionMap.get("startDate")+"'");
			}

			if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
				hql.append(" and '"+conditionMap.get("endDate")+"'");
			}

			if (StringUtils.isNotEmpty(conditionMap1.get("unit"))) {
				hql.append(" and adPvclkUnit = '"+conditionMap1.get("unit")+"'");
			}

			if (StringUtils.isNotEmpty(conditionMap.get("PfbxCustomerInfoId"))) {
				hql.append(" and customerInfoId = '" + conditionMap.get("PfbxCustomerInfoId")+"'");
			}

			hql.append(" group by adPvclkUnit");
			hql.append(" order by adPvclkUnit");


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

				Long pvSum = (Long) objArray[0];
				Long clkSum = (Long) objArray[1];
				Double price = (Double) objArray[2];

				if (clkSum==0 || pvSum==0) {
					vo.setAdClkRate(df.format(0) + "%");
				} else {
					vo.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
				}

				if (price==0 || clkSum==0) {
					vo.setAdClkAvgPrice(df.format(0));
					vo.setAdPvRate(df.format(0));
				} else {
					vo.setAdClkAvgPrice(df.format(price.doubleValue() / clkSum.doubleValue()));
					vo.setAdPvRate(df.format(price.doubleValue() * 1000 / pvSum) );
				}

				vo.setAdPvSum((objArray[0]).toString());
				vo.setAdClkSum((objArray[1]).toString());
				vo.setAdClkPriceSum(((Double)objArray[2]).toString());
				vo.setAdPvclkUnit((objArray[3]).toString());
				vo.setCustomerInfoId((objArray[4]).toString());
				if(conditionMap1.size()>0){
					vo.setWebsiteChineseName(conditionMap1.get("websiteChineseName"));
//					vo.setWebsiteDisplayUrl(conditionMap1.get("websiteDisplayUrl"));
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
