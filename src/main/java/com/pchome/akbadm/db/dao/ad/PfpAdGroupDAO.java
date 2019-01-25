package com.pchome.akbadm.db.dao.ad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.enumerate.ad.EnumAdStatus;


public class PfpAdGroupDAO extends BaseDAO<PfpAdGroup,String> implements IPfpAdGroupDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfpAdGroup> getPfpAdGroups(String adGroupSeq, String adActionSeq, String adGroupName, String adGroupSearchPrice, String adGroupChannelPrice, String adGroupStatus) throws Exception{
		StringBuffer sql = new StringBuffer("from PfpAdGroup where 1=1");
		if (StringUtils.isNotEmpty(adGroupSeq)) {
			sql.append(" and adGroupSeq like '%" + adGroupSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adActionSeq)) {
			sql.append(" and pfpAdAction.adActionSeq = '" + adActionSeq.trim() + "'");
		}

		if (StringUtils.isNotEmpty(adGroupName)) {
			sql.append(" and adGroupName like '%" + adGroupName.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adGroupSearchPrice)) {
			sql.append(" and adGroupSearchPrice like '%" + adGroupSearchPrice.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adGroupChannelPrice)) {
			sql.append(" and adGroupChannelPrice like '%" + adGroupChannelPrice.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adGroupStatus)) {
			sql.append(" and adGroupStatus like '%" + adGroupStatus.trim() + "%'");
		}
		System.out.println("getPfpAdGroups.SQL = " + sql);
		
		return super.getHibernateTemplate().find(sql.toString());
	}
	
	public List<Object> findAdGroupView(final String adActionSeq, final String adType, final String adGroupName, final String startDate, final String endDate, final int page, final int pageSize, final String customerInfoId) throws Exception{
		return findAdGroupView(adActionSeq, adType, null, adGroupName, null, startDate, endDate, page, pageSize, customerInfoId);
		
	}
	//改善廣告報表效能
	@SuppressWarnings("unchecked")
	public List<Object> getAdGroupReport(final Map<String,String> adGroupConditionMap) throws Exception{
	    final List<Object> getAdGroupObjList = getHibernateTemplate().execute(
		    new HibernateCallback<List<Object>>() {
			    public List<Object> doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer();
				hql.append(" SELECT "); 
				hql.append(" a.adGroupCreateTime, ");
				hql.append(" a.pfpAdAction.pfpCustomerInfo.memberId, ");
				hql.append(" a.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
				hql.append(" a.adGroupName, ");
				hql.append(" b.adPvclkDevice, ");
				hql.append(" a.adGroupStatus, ");
				hql.append(" a.adGroupSearchPrice, ");
				hql.append(" a.adGroupChannelPrice, ");
				hql.append(" sum(b.adPv), ");
				hql.append(" sum(b.adClk), ");
				hql.append(" sum(b.adClkPrice),");
				hql.append(" sum(b.adInvalidClk), ");
				hql.append(" sum(b.adInvalidClkPrice), ");
				hql.append(" a.adGroupSeq, ");
				hql.append(" a.pfpAdAction.adActionStatus, ");
				hql.append(" a.adGroupSearchPriceType, ");
				hql.append(" a.pfpAdAction.adActionName ");
				hql.append(" from PfpAdGroup a,PfpAdGroupReport b");
				hql.append(" where 1=1 ");
				hql.append(" and a.adGroupSeq = b.adGroupSeq  ");
				//判斷加上查詢條件
				String adStartDate = null;
				String adEndDate = null;
				if(adGroupConditionMap.get("startDate")!= null && adGroupConditionMap.get("startDate").trim().length()>0){
					adStartDate = adGroupConditionMap.get("startDate").trim();
				}
				if(adGroupConditionMap.get("endDate")!= null && adGroupConditionMap.get("endDate").trim().length()>0){
					adEndDate = adGroupConditionMap.get("endDate").trim();
				}
				if(adGroupConditionMap.get("userAccount")!= null && adGroupConditionMap.get("userAccount").trim().length()>0){
					hql.append(" and a.pfpAdAction.pfpCustomerInfo.memberId = '"+adGroupConditionMap.get("userAccount").trim()+"'");
				}
				if(adGroupConditionMap.get("adGroupStatus")!= null && adGroupConditionMap.get("adGroupStatus").trim().length()>0){
					hql.append(" and a.adGroupStatus ='"+adGroupConditionMap.get("adGroupStatus").trim()+"'");
				}
				if(adGroupConditionMap.get("adGroupName")!= null && adGroupConditionMap.get("adGroupName").trim().length()>0){
					hql.append(" and a.adGroupName like '%"+adGroupConditionMap.get("adGroupName").trim()+"%'");
				}
				if(adGroupConditionMap.get("adActionSeq")!= null && adGroupConditionMap.get("adActionSeq").trim().length()>0){
					hql.append(" and b.adActionSeq = '"+adGroupConditionMap.get("adActionSeq").trim()+"'");
				}
				if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
					if(!adGroupConditionMap.get("adPvclkDevice").equals("ALL Device")){
						hql.append(" and b.adPvclkDevice = '"+adGroupConditionMap.get("adPvclkDevice").trim()+"'");
					}
				}
				if(adGroupConditionMap.get("adType")!= null && adGroupConditionMap.get("adType").trim().length()>0){
					hql.append(" and a.pfpAdAction.adType = '"+adGroupConditionMap.get("adType").trim()+"'");
				}
//				if(!StringUtils.isEmpty(adGroupConditionMap.get("adPvclkDate")) && adGroupConditionMap.get("adPvclkDate").equals("adPvclkDate")){
//					hql.append(" and b.adPvclkDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//				}	
//				if(!StringUtils.isEmpty(adGroupConditionMap.get("adActionDate")) && adGroupConditionMap.get("adActionDate").equals("adActionDate")){
//					hql.append(" and a.pfpAdAction.adActionStartDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//				}
				hql.append("and a.pfpAdAction.adActionStartDate >='"+adGroupConditionMap.get("startDate")+"'");
				hql.append("and a.pfpAdAction.adActionStartDate <='"+adGroupConditionMap.get("endDate")+"'");
				if(!Boolean.valueOf(adGroupConditionMap.get("changeSelect"))){
				    if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
				    	hql.append(" group by b.adGroupSeq,b.adPvclkDevice");
						hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq ");
				    }else{
				    	hql.append(" group by b.adGroupSeq");
						hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq ");  
				    }
				}else if(Boolean.valueOf(adGroupConditionMap.get("changeSelect"))){
				    if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
				    	hql.append(" group by b.adGroupSeq,b.adPvclkDevice");
				    	hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
				    }else{
					 hql.append(" group by b.adGroupSeq");
					 hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
				    }
				}
				int pageSize = Integer.valueOf(adGroupConditionMap.get("pageSize"));
				int page = Integer.valueOf(adGroupConditionMap.get("pageNo"));
				Query query = session.createQuery(hql.toString());
				if(page!=-1){
				    query.setFirstResult((page-1)*pageSize).setMaxResults(pageSize);
				}
				
				return query.list();
			    }
		        }
	    );
	    return getAdGroupObjList;
	}
	    
	public int getAdGroupReportSize(Map<String,String> adGroupConditionMap) throws Exception{
	    StringBuffer hql = new StringBuffer();
	    hql.append(" SELECT count(*) "); 
	    hql.append(" from PfpAdGroup a,PfpAdGroupReport b");
	    hql.append(" where 1=1 ");
	    hql.append(" and a.adGroupSeq = b.adGroupSeq  ");
		//判斷加上查詢條件
		String adStartDate = null;
		String adEndDate = null;
		if(adGroupConditionMap.get("startDate")!= null && adGroupConditionMap.get("startDate").trim().length()>0){
			adStartDate = adGroupConditionMap.get("startDate").trim();
		}
		if(adGroupConditionMap.get("endDate")!= null && adGroupConditionMap.get("endDate").trim().length()>0){
			adEndDate = adGroupConditionMap.get("endDate").trim();
		}
		if(adGroupConditionMap.get("userAccount")!= null && adGroupConditionMap.get("userAccount").trim().length()>0){
			hql.append(" and a.pfpAdAction.pfpCustomerInfo.memberId = '"+adGroupConditionMap.get("userAccount").trim()+"'");
		}
		if(adGroupConditionMap.get("adGroupStatus")!= null && adGroupConditionMap.get("adGroupStatus").trim().length()>0){
			hql.append(" and a.adGroupStatus ='"+adGroupConditionMap.get("adGroupStatus").trim()+"'");
		}
		if(adGroupConditionMap.get("adGroupName")!= null && adGroupConditionMap.get("adGroupName").trim().length()>0){
			hql.append(" and a.adGroupName like '%"+adGroupConditionMap.get("adGroupName").trim()+"%'");
		}
		if(adGroupConditionMap.get("adActionSeq")!= null && adGroupConditionMap.get("adActionSeq").trim().length()>0){
			hql.append(" and b.adActionSeq = '"+adGroupConditionMap.get("adActionSeq").trim()+"'");
		}
		if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
			if(!adGroupConditionMap.get("adPvclkDevice").equals("ALL Device")){
				hql.append(" and b.adPvclkDevice = '"+adGroupConditionMap.get("adPvclkDevice").trim()+"'");
			}
		}
		if(adGroupConditionMap.get("adType")!= null && adGroupConditionMap.get("adType").trim().length()>0){
			hql.append(" and a.pfpAdAction.adType = '"+adGroupConditionMap.get("adType").trim()+"'");
		}
//		if(!StringUtils.isEmpty(adGroupConditionMap.get("adPvclkDate")) && adGroupConditionMap.get("adPvclkDate").equals("adPvclkDate")){
//			hql.append(" and b.adPvclkDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}	
//		if(!StringUtils.isEmpty(adGroupConditionMap.get("adActionDate")) && adGroupConditionMap.get("adActionDate").equals("adActionDate")){
//			hql.append(" and a.pfpAdAction.adActionStartDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}
		hql.append("and a.pfpAdAction.adActionStartDate >='"+adGroupConditionMap.get("startDate")+"'");
		hql.append("and a.pfpAdAction.adActionStartDate <='"+adGroupConditionMap.get("endDate")+"'");
		if(!Boolean.valueOf(adGroupConditionMap.get("changeSelect"))){
		    if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
		    	hql.append(" group by b.adGroupSeq,b.adPvclkDevice");
		    	hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
		    }else{
		    	hql.append(" group by b.adGroupSeq");
		    	hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
		    }
		}else if(Boolean.valueOf(adGroupConditionMap.get("changeSelect"))){
		    if(adGroupConditionMap.get("adPvclkDevice")!= null && adGroupConditionMap.get("adPvclkDevice").trim().length()>0){
		    	hql.append(" group by b.adGroupSeq,b.adPvclkDevice");
		    	hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
		    }else{
		    	hql.append(" group by b.adGroupSeq");
		    	hql.append(" order by a.pfpAdAction.adActionName, b.adGroupSeq "); 
		    }
		}
		Query query = super.getSession().createQuery(hql.toString());
		int totalSize = query.list().size();
	return totalSize;
	}
	@SuppressWarnings("unchecked")
	public List<Object> findAdGroupView(final String adActionSeq, final String adType, final String adGroupSeq, final String adGroupName, final String adGroupStatus, final String startDate, final String endDate, final int page, final int pageSize, final String customerInfoId) throws Exception{
		List<Object> result = (List<Object> ) getHibernateTemplate().execute(
                new HibernateCallback<List<Object> >() {
					public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {
						String sql = adGroupViewSQL(adActionSeq, adType, adGroupSeq, adGroupName, adGroupStatus, startDate, endDate, customerInfoId);
						//System.out.println(hql);
						
						List<Object> resultData = null;

						//page=-1 取得全部不分頁用於download
						if(page==-1){
							resultData = session.createSQLQuery(sql).list();
						}else{
							resultData = session.createSQLQuery(sql)
							.setFirstResult((page-1)*pageSize)  
							.setMaxResults(pageSize)
							.list();
						}
						//System.out.println(">> resultData_size  = "+resultData.size());
                        return resultData;
                    }
                }
        );
        
        return result;
	}
	
	public String getCount(final String adActionSeq, final String adType, final String adGroupSeq, final String adGroupName, final String adGroupStatus, final String startDate, final String endDate, final int page, final int pageSize, final String customerInfoId) throws Exception{
		String result = (String) getHibernateTemplate().execute(
				new HibernateCallback<String>() {
					public String doInHibernate(Session session) throws HibernateException, SQLException {
						String sql = adGroupViewSQL(adActionSeq, adType, adGroupSeq, adGroupName, adGroupStatus, startDate, endDate, customerInfoId);
						
						String resultData = Integer.toString(session.createSQLQuery(sql).list().size());
						System.out.println(">> resultData_size  = "+ resultData);
                        return resultData;
                    }
                }
        );
        
        return result;
	}
	
	private String adGroupViewSQL(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, String customerInfoId){
		// 統計帳戶下所有廣告成本
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append("select");
		sqlStr.append("	 paa.ad_action_seq,");
		sqlStr.append("	 paa.ad_action_name,");
		sqlStr.append("	 pag.ad_group_seq,");
		sqlStr.append("	 pag.ad_group_name,");
		sqlStr.append("	 pag.ad_group_search_price,");
		sqlStr.append("	 pag.ad_group_channel_price,");
		sqlStr.append("	 pag.ad_group_status,");
		sqlStr.append("	 pa.ad_seq,");
		sqlStr.append("	 papc.ad_pv,");
		sqlStr.append("	 papc.ad_clk,");
		sqlStr.append("	 papc.ad_pv_price,");
		sqlStr.append("	 papc.ad_clk_price");
		sqlStr.append(" from");
		sqlStr.append("	  pfp_ad_action as paa,");
		sqlStr.append("	  pfp_ad_group as pag");
		sqlStr.append("	  left outer join pfp_ad as pa on pag.ad_group_seq = pa.ad_group_seq");
		sqlStr.append("	  left outer join ");
		sqlStr.append("	       (SELECT ad_seq, customer_info_id, sum(ad_pv) as ad_pv, sum(ad_clk) as ad_clk, sum(ad_pv_price) as ad_pv_price, sum(ad_clk_price) as ad_clk_price FROM pfp_ad_pvclk group by ad_seq, customer_info_id) as papc on pa.ad_seq = papc.ad_seq");
		sqlStr.append(" where paa.ad_action_seq is not null");
		sqlStr.append("   and pag.ad_action_seq = paa.ad_action_seq");
		sqlStr.append("   and paa.ad_action_seq = '" + adActionSeq + "'");
		if(StringUtils.isNotEmpty(adGroupSeq)) {
			sqlStr.append(" and pag.ad_group_seq = " + adGroupSeq);
		}
		if(StringUtils.isNotEmpty(adGroupStatus)) {
			sqlStr.append(" and pag.ad_group_status = " + adGroupStatus);
		}
		if(StringUtils.isNotEmpty(adGroupName)) {
			sqlStr.append(" and pag.ad_group_name like '%" + adGroupName + "%'");
		}
		if(StringUtils.isNotEmpty(adType)) {
			sqlStr.append(" and paa.ad_type = " + adType);
		}
		if(StringUtils.isNotEmpty(customerInfoId)) {
			sqlStr.append(" and paa.customer_info_id = '" + customerInfoId +"'");
		}
		// 如果查詢開始日期或查詢結束日期有一個為空值
		if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			String strDate = null;
			if(StringUtils.isEmpty(startDate))		strDate = endDate;
			else 									strDate = startDate;
			if(StringUtils.isNotEmpty(strDate)) {
				sqlStr.append(" and (paa.ad_action_start_date < '" + strDate + "'");
				sqlStr.append(" and paa.ad_action_end_date > '" + strDate + "')");
			}
		} else {		// 如果查詢開始日期或查詢結束日期皆有值
			sqlStr.append(" and ((paa.ad_action_start_date between '" + startDate + "' and '" + endDate + "')");
			sqlStr.append(" or (paa.ad_action_end_date between '" + startDate + "' and '" + endDate + "')");
			sqlStr.append(" or (paa.ad_action_start_date < '" + startDate + "'");
			sqlStr.append(" and paa.ad_action_end_date > '" + endDate + "'))");
		}
		sqlStr.append(" group by paa.ad_action_seq, paa.customer_info_id, pag.ad_group_seq");
		System.out.println("sqlStr = " + sqlStr);
	
		return sqlStr.toString();
	}

	@SuppressWarnings("unchecked")
	public boolean chkAdGroupNameByAdActionSeq(String adGroupName, String adGroupSeq, String adActionSeq) throws Exception {
		String hql = "from PfpAdGroup where pfpAdAction.adActionSeq = '" + adActionSeq + "' and adGroupName = '"+adGroupName+"'";
		if(StringUtils.isNotEmpty(adGroupSeq)) {
			hql += " and adGroupSeq <> '" + adGroupSeq + "'";
		}
		System.out.println("hql = " + hql);
		List<PfpAdAction> list = super.getHibernateTemplate().find(hql);
		
		if (list!=null && list.size()>0) {
			System.out.println("list.size() = " + list.size());
			return true;
		} else {
			System.out.println("chkAdGroupNameByAdActionSeq false");
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PfpAdGroup getPfpAdGroupBySeq(String adGroupSeq) throws Exception {
		List<PfpAdGroup> list = super.getHibernateTemplate().find("from PfpAdGroup where adGroupSeq = '" + adGroupSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public void saveOrUpdatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception{
		super.getHibernateTemplate().saveOrUpdate(pfpAdGroup);
	}

	public void insertPfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception {
		this.saveOrUpdate(pfpAdGroup);
	}

	public void updatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception {
		this.update(pfpAdGroup);
	}

	public void updatePfpAdGroupStatus(String pfpAdGroupStatus, String adGroupSeq) throws Exception {
		final StringBuffer sql = new StringBuffer()
		.append("UPDATE pfp_ad_group set ad_group_status = '" + pfpAdGroupStatus + "' where ad_group_seq = '" + adGroupSeq + "'");
		System.out.println(sql);

        Session session = getSession();
        session.createSQLQuery(sql.toString()).executeUpdate();
        session.flush();
	}
	
	public void saveOrUpdateWithCommit(PfpAdGroup adGroup) throws Exception{
		super.getSession().saveOrUpdate(adGroup);
		super.getSession().beginTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdGroupPvclk(final String userAccount, final String adStatus, final int adType, final String keyword, final String adActionSeq, final String adPvclkDevice, final String dateType, final Date startDate, final Date endDate, final int page, final int pageSize) throws Exception{
		List<Object> result = (List<Object> ) getHibernateTemplate().execute(
				 
                new HibernateCallback<List<Object> >() {
                	
					public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {
						
						StringBuffer hql = new StringBuffer();
						
						hql.append(" select pag.pfpAdAction.adActionSeq, ");
						hql.append("  		pag.pfpAdAction.adActionName, ");
						hql.append("		pag.pfpAdAction.adType, ");
						hql.append("  		pag.pfpAdAction.adActionStatus, ");
						hql.append("  		pag.pfpAdAction.pfpCustomerInfo.memberId, ");
						hql.append("  		pag.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
						hql.append("  		pag.adGroupSeq, ");
						hql.append("  		pag.adGroupName, ");
						hql.append("  		pag.adGroupStatus, ");
						hql.append("  		pag.adGroupSearchPriceType, ");
						hql.append("  		pag.adGroupSearchPrice, ");
						hql.append("  		pag.adGroupChannelPrice, ");
						hql.append("  		pag.adGroupCreateTime, ");
						hql.append("  		papc.adPvclkDevice, ");
						hql.append("  		COALESCE(sum(papc.adPv),0), ");
						hql.append("  		COALESCE(sum(papc.adClk),0), ");
						hql.append("		COALESCE(sum(papc.adClkPrice),0), ");
						hql.append("		COALESCE(sum(papc.adInvalidClk),0), ");
						hql.append("		COALESCE(sum(papc.adInvalidClkPrice),0) ");
						hql.append(" from PfpAdGroup as pag ");
						hql.append(" 		left join pag.pfpAds paa  ");
						hql.append(" 		left join paa.pfpAdPvclks papc ");

						if(adType > 0){
							hql.append(" 			with (papc.adType = :adType ");
						}else{
							hql.append(" 			with (papc.adType != :adType ");
						}

						//if(startDate != null && endDate != null) {
						//	hql.append(" 					and papc.adPvclkDate >= :startDate ");
						//	hql.append(" 					and papc.adPvclkDate <= :endDate ");
						//}
						hql.append(")");

						hql.append(" where 1 = 1");
						if(StringUtils.isNotEmpty(userAccount)) {
							//hql.append(" and (pa.pfpCustomerInfo.customerInfoId = :userAccount or pa.pfpCustomerInfo.memberId = :userAccount)");
							hql.append(" and pag.pfpAdAction.pfpCustomerInfo.memberId like :userAccount");
						}
						if(StringUtils.isNotEmpty(adStatus)) {
							hql.append(" and pag.adGroupStatus = :adStatus ");
						}
						if(StringUtils.isNotEmpty(keyword)) {
							hql.append(" and (pag.pfpAdAction.adActionName like :keyword ");
							hql.append(" 	  or pag.pfpAdAction.adActionDesc like :keyword");
							hql.append(" 	  or pag.adGroupName like :keyword)");
						}
						if(StringUtils.isNotEmpty(adActionSeq)) {
							hql.append(" and pag.pfpAdAction.adActionSeq = :adActionSeq ");
						}
						
						if(StringUtils.isNotBlank(adPvclkDevice)) {
							hql.append(" and papc.adPvclkDevice = :adPvclkDevice ");
						}

						//if(startDate != null && endDate != null) {
						//	hql.append(" 					and pag.pfpAdAction.adActionCreatTime >= :startDate ");
						//	hql.append(" 					and pag.pfpAdAction.adActionCreatTime <= :endDate ");
						//}

						if(StringUtils.isNotBlank(dateType)) {
							if(dateType.equals("adPvclkDate")) {
								if(startDate == endDate) {
									hql.append(" 					and (papc.adPvclkDate = :startDate ");
								} else {
									hql.append(" 					and (papc.adPvclkDate >= :startDate ");
									hql.append(" 					and papc.adPvclkDate <= :endDate) ");
								}
							} else{
								hql.append(" 					and ((pag.pfpAdAction.adActionStartDate between :startDate and :endDate)");
								hql.append(" 					or (pag.pfpAdAction.adActionEndDate between :startDate and :endDate)");
								hql.append(" 					or (pag.pfpAdAction.adActionStartDate < :startDate and pag.pfpAdAction.adActionEndDate > :endDate)) ");
							}
						}
						
						hql.append(" group by pag.adGroupSeq ");
						hql.append(" order by pag.adGroupCreateTime desc ");
						
						log.info(hql);
						
						Query q = session.createQuery(hql.toString())
								.setInteger("adType", adType);
						if(startDate != null && endDate != null) {
							q.setDate("startDate", startDate);
							q.setDate("endDate", endDate);
						}
						if(StringUtils.isNotEmpty(userAccount)) {
							q.setString("userAccount", userAccount);
						}
						
						if(StringUtils.isNotEmpty(keyword)) {
							q.setString("keyword", "%"+keyword+"%");
						}

						if(StringUtils.isNotEmpty(adActionSeq)) {
							q.setString("adActionSeq", adActionSeq);
						}
	
						// status 不是空的才使用這個條件
						if(StringUtils.isNotEmpty(adStatus)) {
							q.setString("adStatus", adStatus);
						}

						if(StringUtils.isNotBlank(adPvclkDevice)) {
							q.setString("adPvclkDevice", adPvclkDevice);
						}
						
						//page=-1 取得全部不分頁用於download
						if(page!=-1){
							q.setFirstResult((page-1)*pageSize)  
								.setMaxResults(pageSize);
						}      

						log.info(" resultData size  = "+ q.list().size());
                        
						return q.list();
                    }
                }
        );
        
        return result;
	}

	// 2014-04-24 
	@SuppressWarnings("unchecked")
	public HashMap<String, PfpAdGroup> getPfpAdGroupsBySeqList(List<String> adGroupSeqList) throws Exception {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfpAdGroup where 1=1");
		if (adGroupSeqList != null) {
			sql.append(" and adGroupSeq in (:adGroupSeq)");
			sqlParams.put("adGroupSeq", adGroupSeqList);
		}

		//List<PfpAd> list = super.getHibernateTemplate().find(sql.toString(), adActionSeqList);

		// 將條件資料設定給 Query，準備 query
		Query q = this.getSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("adGroupSeq")) {
				q.setParameterList("adGroupSeq", new ArrayList((ArrayList<String>)sqlParams.get(paramName)));
			}
        }
		
		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfpAdGroup> adGroupMap = new HashMap<String, PfpAdGroup>();
		List<PfpAdGroup> pfpAdGroups = q.list();
		System.out.println("pfpAdGroups.size() = " + pfpAdGroups.size());
		for(PfpAdGroup pfpAdGroup:pfpAdGroups) {
			adGroupMap.put(pfpAdGroup.getAdGroupSeq(), pfpAdGroup);
		}
		
		return adGroupMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpAdGroup> validAdGroup(String adActionSeq) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAdGroup ");
		hql.append(" where pfpAdAction.adActionSeq = ? ");
		hql.append(" and adGroupStatus != ? ");

		
		return super.getHibernateTemplate().find(hql.toString(),adActionSeq,EnumAdStatus.Close.getStatusId());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpAdGroup> findAdGroup(int statusId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfpAdGroup ");
		hql.append(" where adGroupStatus = ? ");
		
		list.add(statusId);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
