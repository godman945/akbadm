package com.pchome.akbadm.db.dao.customerInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.pfd.EnumPfdCategory;

public class PfdUserAdAccountRefDAO extends BaseDAO<PfdUserAdAccountRef, String> implements IPfdUserAdAccountRefDAO{

	@SuppressWarnings("unchecked")
    public List<PfdUserAdAccountRef> findPfpCustomerInfo(String pfdCustomerInfoId, Date startDate, Date endDate) {
		
    	StringBuffer hql = new StringBuffer();
    	List<Object> list = new ArrayList<Object>();
    	
    	hql.append(" from PfdUserAdAccountRef ");
    	hql.append(" where pfdCustomerInfo.customerInfoId = ? ");
    	hql.append(" and pfpCustomerInfo.activateDate >= ? ");
    	hql.append(" and pfpCustomerInfo.activateDate <= ? ");  
    	
    	list.add(pfdCustomerInfoId);
    	list.add(startDate);
    	list.add(endDate);
    	
    	return super.getHibernateTemplate().find(hql.toString(), list.toArray());
    }
	
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRef(String pfdCustomerInfoId) {
		return findPfdUserAdAccountRef(pfdCustomerInfoId, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRef(String pfdCustomerInfoId, List<String> status) {
		
		StringBuffer hql = new StringBuffer();
    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
    	
    	hql.append(" from PfdUserAdAccountRef ");
    	hql.append(" where 1 = 1 ");
    	if(StringUtils.isNotBlank(pfdCustomerInfoId)) {
    		hql.append(" and pfdCustomerInfo.customerInfoId = :pfdCustomerInfoId ");
    		sqlParams.put("pfdCustomerInfoId", pfdCustomerInfoId);
    	}
    	
    	if(status != null && status.size() > 0) {
    		hql.append(" and pfdCustomerInfo.status in (:status) ");
    		sqlParams.put("status", status);
    	}
    	//log.info("hql = " + hql.toString());
    	Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {
        	//System.out.println(paramName + " => " + sqlParams.get(paramName));
			if(paramName.equals("status")) {
				query.setParameterList(paramName, (List<String>)sqlParams.get(paramName));
			} else {
		  		query.setParameter(paramName, sqlParams.get(paramName));
			}
        }
    	
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<PfdUserAdAccountRef> findPfdUserIdByPfpCustomerInfoId(String pfpCustomerInfoId)
			throws Exception {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfdUserAdAccountRef ");
		hql.append(" where pfpCustomerInfo.customerInfoId = '" + pfpCustomerInfoId + "'");
		
		return super.getHibernateTemplate().find(hql.toString());
	}
	
	public Integer deletePfdUserAdAccountRef(String pfpCustomerInfoId){
		
		StringBuffer hql = new StringBuffer();

	    hql.append("delete from PfdUserAdAccountRef ");
	    hql.append("where pfpCustomerInfo.customerInfoId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), pfpCustomerInfoId);
	}	
	
	@SuppressWarnings("unchecked")
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRefs() {
		
		StringBuffer hql = new StringBuffer();
    	
    	hql.append(" from PfdUserAdAccountRef ");
    	
    	
    	return super.getHibernateTemplate().find(hql.toString());
	}
	
	public HashMap<String, PfdUserAdAccountRef> getPfdUserAdAccountRefBySeqList(List<String> customerInfoIdList) {
		
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfdUserAdAccountRef where 1=1");
		if (customerInfoIdList != null) {
			sql.append(" and pfpCustomerInfo.customerInfoId in (:customerInfoId)");
			sqlParams.put("customerInfoId", customerInfoIdList);
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("customerInfoId")) {
				q.setParameterList("customerInfoId", customerInfoIdList);
			}
        }        
		
		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfdUserAdAccountRef> pfdUserAdAccountRefMap = new HashMap<String, PfdUserAdAccountRef>();
		List<PfdUserAdAccountRef> pfdUserAdAccountRefs = q.list();
		
		for(PfdUserAdAccountRef ref:pfdUserAdAccountRefs) {
			pfdUserAdAccountRefMap.put(ref.getPfpCustomerInfo().getCustomerInfoId(), ref);
		}
		
		return pfdUserAdAccountRefMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdUserAdAccountRef> findPfdUserAdAccountRefByPfpId(String pfpCustomerInfoId) {
		
		StringBuffer hql = new StringBuffer();
    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
    	
    	hql.append(" from PfdUserAdAccountRef ");
    	hql.append(" where 1 = 1 ");
    	if(StringUtils.isNotBlank(pfpCustomerInfoId)) {
    		hql.append(" and pfpCustomerInfo.customerInfoId = :pfpCustomerInfoId ");
    		sqlParams.put("pfpCustomerInfoId", pfpCustomerInfoId);
    	}
    	
    	
    	//log.info("hql = " + hql.toString());
    	Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {
		  	query.setParameter(paramName, sqlParams.get(paramName));
        }
    	
		return query.list();
	}
	
	//取得經銷商下所有PFP帳戶
	@SuppressWarnings("unchecked")
	public List<PfpCustomerInfo> findPfdUserAdAccountRefByPfdId(List<String> customerInfoIdList){
		
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfdUserAdAccountRef where 1=1");
		sql.append(" and( pfpCustomerInfo.status =:status1");
		sql.append(" or pfpCustomerInfo.status =:status2");
		sql.append(" or pfpCustomerInfo.status =:status3)");
		sql.append(" and pfpCustomerInfo.activateDate != null ");
		sqlParams.put("status1", EnumAccountStatus.CLOSE.getStatus());
		sqlParams.put("status2", EnumAccountStatus.START.getStatus());
		sqlParams.put("status3", EnumAccountStatus.STOP.getStatus());

		if (customerInfoIdList != null) {
			sql.append(" and pfdCustomerInfo.customerInfoId in (:customerInfoId)");
			sqlParams.put("customerInfoId", customerInfoIdList);
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
        	if(paramName.equals("customerInfoId")) {
				q.setParameterList("customerInfoId", customerInfoIdList);
			} else {
				q.setParameter(paramName, sqlParams.get(paramName));
			}
        }        
		
        List<PfpCustomerInfo> pfpCustomerInfoList = new ArrayList<PfpCustomerInfo>();
		List<PfdUserAdAccountRef> pfdUserAdAccountRefs = q.list();
		
		for(PfdUserAdAccountRef ref:pfdUserAdAccountRefs) {
			pfpCustomerInfoList.add(ref.getPfpCustomerInfo());
		}
		
		return pfpCustomerInfoList;
	}
}
