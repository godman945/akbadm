package com.pchome.akbadm.db.dao.pfd.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.enumerate.privilege.EnumPrivilegeModel;

public class PfdUserDAO extends BaseDAO<PfdUser, String> implements IPfdUserDAO {

	@SuppressWarnings("unchecked")
	public List<PfdUser> getPfdUserByCondition(Map<String, String> conditionMap) throws Exception {

		StringBuffer sbr = new StringBuffer("from PfdUser where 1=1");

		if (conditionMap.containsKey("userId")) {
			sbr.append(" and userId = '" + conditionMap.get("userId") + "'");
		}
		if (conditionMap.containsKey("userEmail")) {
			sbr.append(" and userEmail = '" + conditionMap.get("userEmail") + "'");
		}
		if (conditionMap.containsKey("status")) {
			sbr.append(" and status in (" + conditionMap.get("status") + ")");
		}
		if (conditionMap.containsKey("privilegeId")) {
			sbr.append(" and privilegeId = " + conditionMap.get("privilegeId"));
		}
		if (conditionMap.containsKey("pfdCustomerInfoId")) {
			sbr.append(" and pfdCustomerInfo.customerInfoId = '" + conditionMap.get("pfdCustomerInfoId") + "'");
		}
		String hql = sbr.toString();
		log.info(">>> hql = " + hql);

		return super.getHibernateTemplate().find(hql);
	}

	// 2014-04-24 
	@SuppressWarnings("unchecked")
	public HashMap<String, PfdUser> getPfdUserBySeqList(List<String> userIdList) throws Exception {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfdUser where 1=1");
		if (userIdList != null) {
			sql.append(" and userId in (:userId)");
			sqlParams.put("userId", userIdList);
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("userId")) {
				q.setParameterList("userId", (List<String>)sqlParams.get(paramName));
			}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfdUser> pfdUserMap = new HashMap<String, PfdUser>();
		List<PfdUser> pfdUsers = q.list();
		System.out.println("pfdUsers.size() = " + pfdUsers.size());
		for(PfdUser pfdUser:pfdUsers) {
			pfdUserMap.put(pfdUser.getUserId(), pfdUser);
		}
		
		return pfdUserMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdUser> findRootPfdUser(String pfdCustomerInfoId) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfdUser ");
		hql.append(" where pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and privilegeId = ? ");
		hql.append(" order by userId ");

		list.add(pfdCustomerInfoId);
		list.add(EnumPrivilegeModel.ROOT_USER.getPrivilegeId());
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
