package com.pchome.akbadm.db.dao.pfbx.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.factory.pfbx.bonus.EveryDayPfbBonus;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;

public class PfbxCustomerInfoDAO extends BaseDAO<PfbxCustomerInfo, String> implements IPfbxCustomerInfoDAO {

	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> getList_Bykey(String keyword , String category , String accStatus) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> con = new ArrayList<Object>();
		
		hql.append("from PfbxCustomerInfo where 1=1 ");
		if(StringUtils.isNotBlank(keyword))
		{
			hql.append("and (customerInfoId = ? ");
			hql.append("or memberId = ? ");
			hql.append("or companyName like ? ");
			hql.append("or websiteDisplayUrl = ? ");
			hql.append("or taxId = ?) ");
			con.add(keyword);
			con.add(keyword);
			con.add("%" + keyword + "%");
			con.add(keyword);
			con.add(keyword);
		}
		if(StringUtils.isNotBlank(category))
		{
			hql.append("and category = ? ");
			con.add(category);
		}
		if(StringUtils.isNotBlank(accStatus))
		{
			hql.append("and status = ? ");
			con.add(accStatus);
		}
		
		//只抓帳戶狀態為 開通 關閉 封鎖
		hql.append("and status in ('"+EnumPfbAccountStatus.START.getStatus()+"' , '"+EnumPfbAccountStatus.STOP.getStatus()+"' , '"+EnumPfbAccountStatus.CLOSE.getStatus()+"') ");
		
		List<PfbxCustomerInfo> list = (List<PfbxCustomerInfo>) this.getHibernateTemplate().find(hql.toString() , con.toArray());
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> getPfbxCustomerInfoByCondition(Map<String, String> conditionMap) throws Exception {

		StringBuffer sql = new StringBuffer("from PfbxCustomerInfo where 1=1");

		if (conditionMap.containsKey("customerInfoId")) {
			sql.append(" and customerInfoId = '" + conditionMap.get("customerInfoId") + "'");
		}
		if (conditionMap.containsKey("websiteChineseName")) {
			sql.append(" and websiteChineseName = '" + conditionMap.get("websiteChineseName") + "'");
		}
		if (conditionMap.containsKey("websiteDisplayUrl")) {
			sql.append(" and websiteDisplayUrl = '" + conditionMap.get("websiteDisplayUrl") + "'");
		}
		if (conditionMap.containsKey("taxId")) {
			sql.append(" and taxId = '" + conditionMap.get("taxId") + "'");
		}
		if (conditionMap.containsKey("contactName")) {
			sql.append(" and contactName like '%" + conditionMap.get("contactName") + "%'");
		}
		if (conditionMap.containsKey("status")) {
			sql.append(" and status in (" + conditionMap.get("status") + ")");
		}
		if (conditionMap.containsKey("status_Not")) {
			sql.append(" and status not in (" + conditionMap.get("status_Not") + ")");
		}
		if (conditionMap.containsKey("companyName")) {
			sql.append(" and companyName like '%" + conditionMap.get("companyName") + "%'");
		}
		if (conditionMap.containsKey("createStartDate")) {
			sql.append(" and createDate >= '" + conditionMap.get("createStartDate") + " 00:00:00'");
		}
		if (conditionMap.containsKey("createEndDate")) {
			sql.append(" and createDate <= '" + conditionMap.get("createEndDate") + " 23:59:59'");
		}
		if (conditionMap.containsKey("closeDate")) {
			sql.append(" and closeDate <= '" + conditionMap.get("closeDate") + " 00:00:00'");
		}
		if (conditionMap.containsKey("category")) {
			sql.append(" and category = '" + conditionMap.get("category") + "'");
		}
		if (conditionMap.containsKey("memberId")) {
			sql.append(" and memberId = '" + conditionMap.get("memberId") + "'");
		}

		return (List<PfbxCustomerInfo>) super.getHibernateTemplate().find(sql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> findPfbxCustomerInfo(String pfbId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxCustomerInfo where 1 = 1 ");
		hql.append(" and customerInfoId = ? ");
		hql.append(" order by customerInfoId ");
		
		list.add(pfbId);
		
		return (List<PfbxCustomerInfo>) super.getHibernateTemplate().find(hql.toString(),list.toArray());
	}

	@SuppressWarnings("unchecked")
	public static List<String> pfdIdList = new ArrayList<String>();
	public List<PfbxCustomerInfo> findQuartzsPfbxCustomerInfo() {
		StringBuffer hql = new StringBuffer();
		List<PfbxCustomerInfo> pfbxCustomerInfoList = new ArrayList<PfbxCustomerInfo>();
		hql.append(" select pfpAdPvclk.pfbxCustomerInfoId from PfpAdPvclk pfpAdPvclk where 1= 1 and pfpAdPvclk.adPvclkDate = '"+EveryDayPfbBonus.statrDate+"' group by pfpAdPvclk.pfbxCustomerInfoId ");
		List<String> pfpAdPvclkList = (List<String>) super.getHibernateTemplate().find(hql.toString());
		for (String string : pfpAdPvclkList) {
			pfbxCustomerInfoList.add(findPfbxCustomerInfo(string).get(0));
		}
		return pfbxCustomerInfoList;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxCustomerInfo> getDemoPfbxCustomerInfo() {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxCustomerInfo where 1 = 1 ");
		hql.append(" and status = ? ");
		hql.append(" order by customerInfoId ");
		
		list.add(EnumPfbAccountStatus.START.getStatus());
		
		return (List<PfbxCustomerInfo>) super.getHibernateTemplate().find(hql.toString(),list.toArray());
	}
	
	public List<String> getPfbxCustomerInfoListByReport(String monthValue) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		sql.append("  r.customer_info_id ");
		sql.append(" FROM pfbx_ad_time_report r ");
		sql.append(" where 1=1    ");
		sql.append(" and  r.customer_info_id != ''   ");
		sql.append(" and DATE_FORMAT(r.ad_pvclk_date,'%Y-%m') = :month  ");
		sql.append(" group by r.customer_info_id ");
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		query.setString("month", monthValue);
		return query.list();
	}
}
