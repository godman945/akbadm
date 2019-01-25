package com.pchome.akbadm.db.dao.customerInfoRefXtype;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfoRefXType;

public class PfbxCustomerInfoRefXTypeDAO extends BaseDAO<PfbxCustomerInfoRefXType,Integer> implements IPfbxCustomerInfoRefXTypeDAO {

//	@SuppressWarnings("unchecked")
//	public List<PfbxCustomerInfo> findPfbCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception {
//
//		StringBuffer sb = new StringBuffer();
//		sb.append("from PfbxCustomerInfo where 1=1");
//		if (conditionMap.containsKey("status")) {
//			sb.append(" and status in (" + conditionMap.get("status") + ")");
//		}
//		if (conditionMap.containsKey("customerInfoId")) {
//			sb.append(" and customerInfoId = '" + conditionMap.get("customerInfoId") + "'");
//		}
//		if (conditionMap.containsKey("websiteDisplayUrl")) {
//			sb.append(" and websiteDisplayUrl = '" + conditionMap.get("websiteDisplayUrl") + "'");
//		}
//
//		String hql = sb.toString();
//		log.info(">>> hql = " + hql);
//
//		return super.getHibernateTemplate().find(hql);
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<PfbxCustomerInfo> findPfbxCustomerInfo(String pfbId) {
//		
//		StringBuffer hql = new StringBuffer();
//		List<Object> list = new ArrayList<Object>();
//		
//		hql.append(" from PfbxCustomerInfo ");
//		hql.append(" where 1 = 1 ");
//		
//		if(StringUtils.isNotBlank(pfbId)){
//			hql.append(" and customerInfoId = ? ");
//			
//			list.add(pfbId);
//		}
//		
//		hql.append(" order by customerInfoId desc ");
//		
//		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
//	}

}
