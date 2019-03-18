package com.pchome.akbadm.db.dao.ad;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkProd;
import com.pchome.akbadm.db.vo.ProdAdReportVO;

public class PfpAdPvclkProdDAO extends BaseDAO<PfpAdPvclkProd, String> implements IPfpAdPvclkProdDAO{
	
	public List<Map<String,Object>> getProdAdDetailReport(Map<String, String> conditionMap) throws Exception{

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select a.pfp_customer_info_id,a.ad_seq,a.catalog_seq,a.catalog_prod_seq,sum(a.catalog_prod_clk)clk,sum(a.catalog_prod_pv)pv,b.ec_name,b.ec_img ");
		hql.append(" from pfp_ad_pvclk_prod a ");
		hql.append(" left join pfp_catalog_prod_ec b ");
		hql.append(" on a.catalog_seq = b.catalog_seq ");
		hql.append(" and a.catalog_prod_seq  = b.catalog_prod_seq ");
		hql.append(" where 1=1 ");
		
		
		if (StringUtils.isNotEmpty(conditionMap.get("pfpCustomerInfoId"))) {
			hql.append(" and a.pfp_customer_info_id = '"+conditionMap.get("pfpCustomerInfoId")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("adSeq"))) {
			hql.append(" and a.ad_seq = '"+conditionMap.get("adSeq")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
			hql.append(" and a.record_date >= '"+conditionMap.get("startDate")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
			hql.append(" and a.record_date <= '"+conditionMap.get("endDate")+"' ");
		}
		
		hql.append(" group by a.catalog_seq,a.catalog_prod_seq ");
		
		log.info(hql.toString());
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
		
		return query.list();
	}
	
	
	public List<Map<String,Object>> getSumProdAdDetailReport(Map<String, String> conditionMap) throws Exception{
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select a.pfp_customer_info_id,a.ad_seq,a.catalog_seq,a.catalog_prod_seq,sum(a.catalog_prod_clk)clk,sum(a.catalog_prod_pv)pv,b.ec_name,b.ec_img ");
		hql.append(" from pfp_ad_pvclk_prod a ");
		hql.append(" left join pfp_catalog_prod_ec b ");
		hql.append(" on a.catalog_seq = b.catalog_seq ");
		hql.append(" and a.catalog_prod_seq  = b.catalog_prod_seq ");
		hql.append(" where 1=1 ");
		
		
		if (StringUtils.isNotEmpty(conditionMap.get("pfpCustomerInfoId"))) {
			hql.append(" and a.pfp_customer_info_id = '"+conditionMap.get("pfpCustomerInfoId")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("adSeq"))) {
			hql.append(" and a.ad_seq = '"+conditionMap.get("adSeq")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("startDate"))) {
			hql.append(" and a.record_date >= '"+conditionMap.get("startDate")+"' ");
		}
		if (StringUtils.isNotEmpty(conditionMap.get("endDate"))) {
			hql.append(" and a.record_date <= '"+conditionMap.get("endDate")+"' ");
		}
		
		hql.append(" group by a.catalog_seq,a.catalog_prod_seq ");
		
		log.info(hql.toString());
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
		
		return query.list();
	}
	
}
