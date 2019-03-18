package com.pchome.akbadm.db.dao.ad;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.vo.AdQueryConditionVO;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;

public class PfpAdDAO extends BaseDAO<PfpAd,String> implements IPfpAdDAO {

	@SuppressWarnings("unchecked")
	public PfpAd getPfpAdBySeq(String adSeq) throws Exception {

		StringBuffer sql = new StringBuffer("from PfpAd where 1=1");
		if (StringUtils.isNotEmpty(adSeq)) {
			sql.append(" and adSeq = '" + adSeq + "'");
		}

		List<PfpAd> list = (List<PfpAd>) super.getHibernateTemplate().find(sql.toString());

		if (list!=null && list.size()==1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String,PfpAd> getPfpAdMap(List<String> adSeqList){
		
		Map<String,PfpAd> pfpAdMap = new HashMap<String,PfpAd>();
		
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfpAd where 1=1");
		if(!adSeqList.isEmpty()){
			sql.append(" and adSeq in (:adSeqList) ");
			sqlParams.put("adSeqList",adSeqList);
		}
		Session session = super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(sql.toString());
		if(!adSeqList.isEmpty()){
        	query.setParameterList("adSeqList", adSeqList);
        }

        List<PfpAd> list = new ArrayList<PfpAd>(); 
        list = query.list();
		
        if(!list.isEmpty()){
        	for(PfpAd pfpAd:list){
        		pfpAdMap.put(pfpAd.getAdSeq(), pfpAd);
        	}
        }
		
		return pfpAdMap;
	}
	
	public int getPfpAdCountByConditions(AdQueryConditionVO vo) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.*  ");
		sql.append(" from( ");
		sql.append(" select  ");
		sql.append(" pfp_ad.ad_seq,  ");
		sql.append(" (select r.pfd_customer_info_id from pfd_user_ad_account_ref r where r.pfp_customer_info_id = pfp_ad_action.customer_info_id  ) pfd_customer_info_id  "); 
		sql.append(" from pfp_ad, ");
		sql.append(" pfp_ad_group, ");
		sql.append(" pfp_ad_action where 1=1 ");
		if (vo.getStatus()!=null && vo.getStatus().length>0) {
			sql.append(" and ad_status in (");
			String tmp = "";
			for (int i=0; i<vo.getStatus().length; i++) {
				tmp += "'" +  vo.getStatus()[i] + "',";
			}
			tmp = tmp.substring(0, tmp.length()-1);
			sql.append(tmp + ")");
		}
		
		if(StringUtils.isNotEmpty(vo.getStyle())) {
			sql.append(" and ad_style = '" + vo.getStyle() + "'");
		}
		sql.append(" and pfp_ad.ad_group_seq  = pfp_ad_group.ad_group_seq ");
		sql.append(" and pfp_ad.ad_group_seq = pfp_ad_group.ad_group_seq ");
		sql.append(" and pfp_ad_action.ad_action_seq = pfp_ad_group.ad_action_seq "); 
		sql.append(" )a, ");
		sql.append(" pfd_customer_info r ");
		sql.append(" where  ");
		sql.append(" r.customer_info_id = a.pfd_customer_info_id ");
		if (StringUtils.isNotBlank(vo.getPfdCustomerInfoId())) {
			sql.append(" and r.customer_info_id ='"+vo.getPfdCustomerInfoId()+"' ");
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		return query.list().size();
//		StringBuffer sb = new StringBuffer();
//		sb.append("select count(*) from PfpAd where 1=1");
//
//		if (vo.getStatus()!=null && vo.getStatus().length>0) {
//			sb.append(" and adStatus in (");
//			String tmp = "";
//			for (int i=0; i<vo.getStatus().length; i++) {
//				tmp += "'" +  vo.getStatus()[i] + "',";
//			}
//			tmp = tmp.substring(0, tmp.length()-1);
//			sb.append(tmp + ")");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getStyle())) {
//			sb.append(" and adStyle = '" + vo.getStyle() + "'");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getSendVerifyStartTime())) {
//			sb.append(" and adSendVerifyTime >= '" + vo.getSendVerifyStartTime() + " 00:00:00'");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getSendVerifyEndTime())) {
//			sb.append(" and adSendVerifyTime <= '" + vo.getSendVerifyEndTime() + " 23:59:59'");
//		}
//
//		String hql = sb.toString();
//		log.info(">>> hql = " + hql);
//
//		Long count = (Long) (super.getHibernateTemplate().find(hql).listIterator().next());

//		return count.intValue();
	}

	@SuppressWarnings("unchecked")
	public List<PfpAd> getPfpAdByConditions(AdQueryConditionVO vo, int pageNo, int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.*  ");
		sql.append(" from( ");
		sql.append(" select  ");
		sql.append(" pfp_ad.ad_seq,  ");
		sql.append(" (select r.pfd_customer_info_id from pfd_user_ad_account_ref r where r.pfp_customer_info_id = pfp_ad_action.customer_info_id  ) pfd_customer_info_id,  "); 
		sql.append(" pfp_ad.ad_send_verify_time  ");
		sql.append(" from pfp_ad, ");
		sql.append(" pfp_ad_group, ");
		sql.append(" pfp_ad_action where 1=1 ");
		if (vo.getStatus()!=null && vo.getStatus().length>0) {
			sql.append(" and ad_status in (");
			String tmp = "";
			for (int i=0; i<vo.getStatus().length; i++) {
				tmp += "'" +  vo.getStatus()[i] + "',";
			}
			tmp = tmp.substring(0, tmp.length()-1);
			sql.append(tmp + ")");
		}
		
		if(StringUtils.isNotEmpty(vo.getStyle())) {
			sql.append(" and ad_style = '" + vo.getStyle() + "'");
		}
		
		sql.append(" and pfp_ad.ad_group_seq  = pfp_ad_group.ad_group_seq ");
		sql.append(" and pfp_ad.ad_group_seq = pfp_ad_group.ad_group_seq ");
		sql.append(" and pfp_ad_action.ad_action_seq = pfp_ad_group.ad_action_seq ");
		sql.append(" )a, ");
		sql.append(" pfd_customer_info r ");
		sql.append(" where  ");
		sql.append(" r.customer_info_id = a.pfd_customer_info_id ");
		
		if (StringUtils.isNotBlank(vo.getPfdCustomerInfoId())) {
			sql.append(" and r.customer_info_id ='"+vo.getPfdCustomerInfoId()+"' ");
		}
		sql.append(" order by ad_send_verify_time desc ");
		
		
		Query query = null;
		if (pageNo==-1) {
			query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
		} else {
			query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
			query.setFirstResult((pageNo-1)*pageSize);
			query.setMaxResults(pageSize);
		}
		
		List<PfpAd> pfpAdList = new ArrayList<>();
		List<Object> result = query.list();
		for (Object object : result) {
			Object[] objArray = (Object[]) object;
			PfpAd pfpAd = getPfpAdBySeq(objArray[0].toString());
			
			
			if(pfpAd != null){
				pfpAdList.add(pfpAd);	
			}
		}
		
		return pfpAdList;
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("from PfpAd where 1=1");
//
//		if (vo.getStatus()!=null && vo.getStatus().length>0) {
//			sb.append(" and adStatus in (");
//			String tmp = "";
//			for (int i=0; i<vo.getStatus().length; i++) {
//				tmp += "'" +  vo.getStatus()[i] + "',";
//			}
//			tmp = tmp.substring(0, tmp.length()-1);
//			sb.append(tmp + ")");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getStyle())) {
//			sb.append(" and adStyle = '" + vo.getStyle() + "'");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getSendVerifyStartTime())) {
//			sb.append(" and adSendVerifyTime >= '" + vo.getSendVerifyStartTime() + " 00:00:00'");
//		}
//
//		if (StringUtils.isNotEmpty(vo.getSendVerifyEndTime())) {
//			sb.append(" and adSendVerifyTime <= '" + vo.getSendVerifyEndTime() + " 23:59:59'");
//		}
//
//		if (StringUtils.isNotBlank(vo.getPfdCustomerInfoId())) {
//			sb.append(" and pfpAdGroup.pfpAdAction.pfpCustomerInfo.pfdUserAdAccountRefs.pfdCustomerInfo.customerInfoId = 'alex'  ");
//		}
//		
//		
//		sb.append(" order by adSendVerifyTime desc");
//
//		String hql = sb.toString();
//		log.info(">>> hql = " + hql);
//
//		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
//		Query query;
//		if (pageNo==-1) {
//			query = session.createQuery(hql.toString());
//		} else {
//			query = session.createQuery(hql.toString())
//			.setFirstResult((pageNo-1)*pageSize)
//			.setMaxResults(pageSize);
//		}
//
//		return query.list();
	}

	/**
	 * 批次更新廣告人工審核後的狀態
	 */
	public void updateAdCheckStatus(String status, String[] seq, String verifyUserId) throws Exception {
		String hql = "update PfpAd set adStatus=:status, adVerifyUser=:verifyUserId, adUserVerifyTime=CURRENT_TIMESTAMP() where adSeq in (:ids)";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("status", status);
        query.setString("verifyUserId", verifyUserId);
        query.setParameterList("ids", seq);
        query.executeUpdate();
        session.flush();
        
	}

	/**
	 * 更新廣告人工審核後的狀態
	 */
	public void updateAdCheckStatus(String adStatus, String adSeq, String adCategorySeq,
		String verifyUserId, String rejectReason) throws Exception {
		String hql = "update PfpAd set adStatus=:adStatus, adCategorySeq=:adCategorySeq, adVerifyUser=:verifyUserId, adVerifyRejectReason=:rejectReason, adUserVerifyTime=CURRENT_TIMESTAMP() where adSeq = :adSeq)";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("adStatus", adStatus);
        query.setString("adCategorySeq", "");
        query.setString("verifyUserId", verifyUserId);
        query.setString("rejectReason", rejectReason = rejectReason == null? "": rejectReason);
        query.setString("adSeq", adSeq);
        query.executeUpdate();
        session.flush();
	}
	
	/**
	 * 新增人工審核後廣告對應表
	 */
	public void insertAdCategoryMapping(PfpAdCategoryMapping pfpAdCategoryMapping)throws Exception {
		getHibernateTemplate().saveOrUpdate(pfpAdCategoryMapping);
	}
	
	/**
	 * 取得廣告審核過類別Code
	 * */
	@SuppressWarnings("unchecked")
	public String getCategoryMappingCodeById(String pfpAdSeq)throws Exception{
	    StringBuffer sql = new StringBuffer("from PfpAdCategoryMapping where 1=1");
	    sql.append(" and adSeq = '" + pfpAdSeq + "'");
	    List<PfpAdCategoryMapping> list =  (List<PfpAdCategoryMapping>) super.getHibernateTemplate().find(sql.toString());
	    if(list.isEmpty()){
		return "";
	    }
	    return (list.get(0).getCode());
	}
	

	public void saveOrUpdateWithCommit(PfpAd adAd) throws Exception{
		super.getHibernateTemplate().getSessionFactory().getCurrentSession().saveOrUpdate(adAd);
	}

	@SuppressWarnings("unchecked")
	public List<Object> templateProductTotalPrice() throws Exception{

		StringBuffer sql = new StringBuffer();

		sql.append(" select template_product_seq, sum(distinct ad_channel_price) ");
		sql.append(" from pfp_ad inner join pfp_ad_group using (ad_group_seq) ");
		sql.append(" 			inner join pfp_ad_action using(ad_action_seq) ");
		sql.append(" 			inner join pfp_customer_info using (customer_info_id) ");
		sql.append(" where ad_status = 4 ");
		sql.append(" and ad_group_status = 4 ");
		sql.append(" and ad_action_status =4 ");
		sql.append(" and  status = '1' ");
		sql.append(" group by template_product_seq ");

		return this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

	}

	//改善廣告報表效能
	@SuppressWarnings("unchecked")
	public List<Object> getAdViewReport(final Map<String,String> adViewConditionMap) throws Exception{
	    final List<Object> getAdVewObjList = getHibernateTemplate().execute(
		    new HibernateCallback<List<Object>>() {
			    public List<Object> doInHibernate(Session session) throws HibernateException {
				String adStartDate = null;
				String adEndDate = null;
				if(adViewConditionMap.get("startDate")!= null && adViewConditionMap.get("startDate").trim().length()>0){
					adStartDate = adViewConditionMap.get("startDate").trim();
				}
				if(adViewConditionMap.get("endDate")!= null && adViewConditionMap.get("endDate").trim().length()>0){
					adEndDate = adViewConditionMap.get("endDate").trim();
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				StringBuffer hql = new StringBuffer();
				hql.append(" SELECT "); 
				hql.append(" a.pfpAdGroup.pfpAdAction.adActionCreatTime, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.adActionName, ");
				hql.append(" a.adStatus, ");
				hql.append(" b.adPvclkDevice, ");
				hql.append(" sum(b.adPv), ");
				hql.append(" sum(b.adClk), ");
				hql.append(" sum(b.adClkPrice), ");
				hql.append(" sum(b.adInvalidClk), ");
				hql.append(" sum(b.adInvalidClkPrice), ");
				hql.append(" a.pfpAdGroup.adGroupName, ");
				hql.append(" a.pfpAdGroup.adGroupStatus, ");
				hql.append(" a.adSeq,  ");
				hql.append(" a.templateProductSeq,  ");
				hql.append(" a.pfpAdGroup.adGroupSeq,  ");
				hql.append(" a.pfpAdGroup.pfpAdAction.adActionStartDate,  ");
				hql.append(" a.pfpAdGroup.pfpAdAction.adActionEndDate,  ");
				hql.append(" COALESCE(a.adVerifyRejectReason,''),  ");
				hql.append(" a.adStyle,  ");
				hql.append(" a.adAssignTadSeq  ");
				hql.append(" FROM PfpAd a,PfpAdReport b "); 
				hql.append(" where 1=1 ");
				hql.append(" and a.adSeq = b.adSeq ");
				//判斷加上查詢條件
				if(adViewConditionMap.get("userAccount")!= null && adViewConditionMap.get("userAccount").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adViewConditionMap.get("userAccount").trim()+"'");
				}
				if(adViewConditionMap.get("adType")!= null && adViewConditionMap.get("adType").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adViewConditionMap.get("adType").trim()+"'");
				}
				if(adViewConditionMap.get("adGroupSeq")!= null && adViewConditionMap.get("adGroupSeq").trim().length()>0){
					hql.append(" and a.pfpAdGroup.adGroupSeq ='"+adViewConditionMap.get("adGroupSeq").trim()+"'");
				}
				if(adViewConditionMap.get("searchAdStatus")!= null && adViewConditionMap.get("searchAdStatus").trim().length()>0){
					hql.append(" and a.adStatus ='"+adViewConditionMap.get("searchAdStatus").trim()+"'");
				}
				if(adViewConditionMap.get("adAdViewName")!= null && adViewConditionMap.get("adAdViewName").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.adActionName like '%"+adViewConditionMap.get("adAdViewName").trim()+"%'");
				}
				
				if(!StringUtils.isBlank(adViewConditionMap.get("adSeq")) && adViewConditionMap.get("adSeq").trim().length()>0){
					hql.append(" and a.adSeq = '"+adViewConditionMap.get("adSeq").trim()+"'");
				}

				if(!StringUtils.isBlank(adViewConditionMap.get("adSeqListString")) && adViewConditionMap.get("adSeqListString").trim().length()>0){
					hql.append(" and a.adSeq in ("+adViewConditionMap.get("adSeqListString").trim()+")");
				}
				
				if(StringUtils.equals(adViewConditionMap.get("dateType"), "adResult")){
					if(adStartDate != null){
						hql.append(" and b.adPvclkDate >= '" + adStartDate + "'");
					}
					if(adEndDate != null){
						hql.append(" and b.adPvclkDate <= '" + adEndDate + "'");
					}
				}
				
				if(!StringUtils.isBlank(adViewConditionMap.get("adPvclkDevice")) && adViewConditionMap.get("adPvclkDevice").equals("ALL Device")){
				    hql.append(" group by a.adSeq");
				}else{
				    hql.append(" and  b.adPvclkDevice ='"+ adViewConditionMap.get("adPvclkDevice")+"'" );
				    hql.append(" group by a.adSeq ,b.adPvclkDevice ");
				}

				Query query = session.createQuery(hql.toString());
				List<Object> adViewObjList =  query.list();
				
				List<Object> adViewObjListData =  new ArrayList<Object>();
				
				//判斷是否在時間區間
				if(StringUtils.equals(adViewConditionMap.get("dateType"), "adActionDate")){
					if(adStartDate != null && adEndDate != null){
						for (Object adViewObject : adViewObjList) {
						    Object[] obj = (Object[]) adViewObject;
						    try{
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) <= 0 &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adStartDate)) >= 0 ){
									adViewObjListData.add(adViewObject);
									continue;
								}
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0   &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) <= 0){
								    adViewObjListData.add(adViewObject);
								    continue;
								}
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0    &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) > 0){
								    adViewObjListData.add(adViewObject);
								}
						    }catch(Exception e){
							e.printStackTrace();
						    }
						}
					} else {
						adViewObjListData = adViewObjList;
					}
					
					
					
					
					StringBuffer hql_ = new StringBuffer();
					hql_.append(" SELECT "); 
					hql_.append(" a.pfpAdGroup.pfpAdAction.adActionCreatTime, ");
					hql_.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
					hql_.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
					hql_.append(" a.pfpAdGroup.pfpAdAction.adActionName, ");
					hql_.append(" a.adStatus, ");
					hql_.append(" 'No Click', ");
					hql_.append(" '0', ");
					hql_.append(" '0', ");
					hql_.append(" '0', ");
					hql_.append(" '0', ");
					hql_.append(" '0', ");
					hql_.append(" a.pfpAdGroup.adGroupName, ");
					hql_.append(" a.pfpAdGroup.adGroupStatus, ");
					hql_.append(" a.adSeq,  ");
					hql_.append(" a.templateProductSeq,  ");
					hql_.append(" a.pfpAdGroup.adGroupSeq,  ");
					hql_.append(" a.pfpAdGroup.pfpAdAction.adActionStartDate,  ");
					hql_.append(" a.pfpAdGroup.pfpAdAction.adActionEndDate,  ");
					hql_.append(" COALESCE(a.adVerifyRejectReason,''),  ");
					hql_.append(" a.adStyle,  ");
					hql_.append(" a.adAssignTadSeq  ");
					hql_.append(" FROM PfpAd a "); 
					hql_.append(" where 1=1 ");
					//判斷加上查詢條件
					if(adViewConditionMap.get("userAccount")!= null && adViewConditionMap.get("userAccount").trim().length()>0){
						hql_.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adViewConditionMap.get("userAccount").trim()+"'");
					}
					if(adViewConditionMap.get("adType")!= null && adViewConditionMap.get("adType").trim().length()>0){
						hql_.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adViewConditionMap.get("adType").trim()+"'");
					}
					if(adViewConditionMap.get("adGroupSeq")!= null && adViewConditionMap.get("adGroupSeq").trim().length()>0){
						hql_.append(" and a.pfpAdGroup.adGroupSeq ='"+adViewConditionMap.get("adGroupSeq").trim()+"'");
					}
					if(adViewConditionMap.get("searchAdStatus")!= null && adViewConditionMap.get("searchAdStatus").trim().length()>0){
						hql_.append(" and a.adStatus ='"+adViewConditionMap.get("searchAdStatus").trim()+"'");
					}
					if(adViewConditionMap.get("adAdViewName")!= null && adViewConditionMap.get("adAdViewName").trim().length()>0){
						hql_.append(" and a.pfpAdGroup.pfpAdAction.adActionName like '%"+adViewConditionMap.get("adAdViewName").trim()+"%'");
					}
					
					if(!StringUtils.isBlank(adViewConditionMap.get("adSeq")) && adViewConditionMap.get("adSeq").trim().length()>0){
						hql_.append(" and a.adSeq = '"+adViewConditionMap.get("adSeq").trim()+"'");
					}
					
					if(!StringUtils.isBlank(adViewConditionMap.get("adSeqListString")) && adViewConditionMap.get("adSeqListString").trim().length()>0){
						hql_.append(" and a.adSeq in ("+adViewConditionMap.get("adSeqListString").trim()+")");
					}

					Query query_ = session.createQuery(hql_.toString());
					List<Object> adObjListData =  new ArrayList<Object>();
					adObjListData = query_.list();
					List<Object> adObjList2 =  new ArrayList<Object>();
					if(adStartDate != null && adEndDate != null){
						for (Object adObject : adObjListData) {
						    Object[] obj = (Object[]) adObject;
						    try{
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) <= 0 &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adStartDate)) >= 0 ){
									adObjList2.add(adObject);
									continue;
								}
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0   &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) <= 0){
								    adObjList2.add(adObject);
								    continue;
								}
								if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0    &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) > 0){
								    adObjList2.add(adObject);
								}
						    }catch(Exception e){
							e.printStackTrace();
						    }
						}
					} else {
						adObjList2 = adObjListData;
					}
					
					
					for (Object object : adViewObjListData) {
					    Object[] obj = (Object[]) object;
					    for (Object object2 : adObjList2) {
						  Object[] obj2 = (Object[]) object2;
						  if(obj2[13].equals(obj[13])){
						      adObjList2.remove(obj2);
						      break;
						  }
					    }
					}
					for (Object object : adObjList2) {
					    adViewObjListData.add(object);
					}
					
				} else {
					adViewObjListData = adViewObjList;
				}
				
				
				
				double page = Integer.valueOf(adViewConditionMap.get("pageNo"));
				double size = Integer.valueOf(adViewConditionMap.get("pageSize"));
				double total = page * size;
				List<Object> padeDataList = new ArrayList<Object>();
				for (int i = 0; i <= adViewObjListData.size()-1; i++) {
				    Object[] obj = (Object[]) adViewObjListData.get(i);
				    if(page != -1 && size != -1){
				    	if( i<total && i >= (page-1)*size){
				    		padeDataList.add(adViewObjListData.get(i));
				    	}
				    } else {
				    	padeDataList.add(adViewObjListData.get(i));
				    }
				}
			return padeDataList;
			    }
		        }
	    );
	    return getAdVewObjList;
	}
	
	public int getAdViewReportSize(Map<String,String> adViewConditionMap) throws Exception{
	    String adStartDate = null;
		String adEndDate = null;
		if(adViewConditionMap.get("startDate")!= null && adViewConditionMap.get("startDate").trim().length()>0){
			adStartDate = adViewConditionMap.get("startDate").trim();
		}
		if(adViewConditionMap.get("endDate")!= null && adViewConditionMap.get("endDate").trim().length()>0){
			adEndDate = adViewConditionMap.get("endDate").trim();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT "); 
		hql.append(" a.pfpAdGroup.pfpAdAction.adActionCreatTime, ");
		hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
		hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
		hql.append(" a.pfpAdGroup.pfpAdAction.adActionName, ");
		hql.append(" a.adStatus, ");
		hql.append(" b.adPvclkDevice, ");
		hql.append(" sum(b.adPv), ");
		hql.append(" sum(b.adClk), ");
		hql.append(" sum(b.adClkPrice), ");
		hql.append(" sum(b.adInvalidClk), ");
		hql.append(" sum(b.adInvalidClkPrice), ");
		hql.append(" a.pfpAdGroup.adGroupName, ");
		hql.append(" a.pfpAdGroup.adGroupStatus, ");
		hql.append(" a.adSeq,  ");
		hql.append(" a.templateProductSeq,  ");
		hql.append(" a.pfpAdGroup.adGroupSeq,  ");
		hql.append(" a.pfpAdGroup.pfpAdAction.adActionStartDate,  ");
		hql.append(" a.pfpAdGroup.pfpAdAction.adActionEndDate,  ");
		hql.append(" COALESCE(a.adVerifyRejectReason,'')  ");
		hql.append(" FROM PfpAd a,PfpAdReport b "); 
		hql.append(" where 1=1 ");
		hql.append(" and a.adSeq = b.adSeq ");
		//判斷加上查詢條件
		if(adViewConditionMap.get("userAccount")!= null && adViewConditionMap.get("userAccount").trim().length()>0){
			hql.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adViewConditionMap.get("userAccount").trim()+"'");
		}
		if(adViewConditionMap.get("adType")!= null && adViewConditionMap.get("adType").trim().length()>0){
			hql.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adViewConditionMap.get("adType").trim()+"'");
		}
		if(adViewConditionMap.get("adGroupSeq")!= null && adViewConditionMap.get("adGroupSeq").trim().length()>0){
			hql.append(" and a.pfpAdGroup.adGroupSeq ='"+adViewConditionMap.get("adGroupSeq").trim()+"'");
		}
		if(adViewConditionMap.get("searchAdStatus")!= null && adViewConditionMap.get("searchAdStatus").trim().length()>0){
			hql.append(" and a.adStatus ='"+adViewConditionMap.get("searchAdStatus").trim()+"'");
		}
		if(adViewConditionMap.get("adAdViewName")!= null && adViewConditionMap.get("adAdViewName").trim().length()>0){
			hql.append(" and a.pfpAdGroup.pfpAdAction.adActionName like '%"+adViewConditionMap.get("adAdViewName").trim()+"%'");
		}
		
		if(!StringUtils.isBlank(adViewConditionMap.get("adSeq")) && adViewConditionMap.get("adSeq").trim().length()>0){
			hql.append(" and a.adSeq = '"+adViewConditionMap.get("adSeq").trim()+"'");
		}
		
		if(!StringUtils.isBlank(adViewConditionMap.get("adSeqListString")) && adViewConditionMap.get("adSeqListString").trim().length()>0){
			hql.append(" and a.adSeq in ("+adViewConditionMap.get("adSeqListString").trim()+")");
		}
		
		if(StringUtils.equals(adViewConditionMap.get("dateType"), "adResult")){
			if(adStartDate != null){
				hql.append(" and b.adPvclkDate >= '" + adStartDate + "'");
			}
			if(adEndDate != null){
				hql.append(" and b.adPvclkDate <= '" + adEndDate + "'");
			}
		}
		
		if(!StringUtils.isBlank(adViewConditionMap.get("adPvclkDevice")) && adViewConditionMap.get("adPvclkDevice").equals("ALL Device")){
		    hql.append(" group by a.adSeq");
		}else{
		    hql.append(" and  b.adPvclkDevice ='"+ adViewConditionMap.get("adPvclkDevice")+"'" );
		    hql.append(" group by a.adSeq ,b.adPvclkDevice ");
		}
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		List<Object> adViewObjList =  query.list();
		
		List<Object> adViewObjListData =  new ArrayList<Object>();
		
		//判斷是否在時間區間
		if(StringUtils.equals(adViewConditionMap.get("dateType"), "adActionDate")){
			if(adStartDate != null  && adEndDate != null){
				for (Object adViewObject : adViewObjList) {
				    Object[] obj = (Object[]) adViewObject;
				    try{
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) <= 0 &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adStartDate)) >= 0 ){
							adViewObjListData.add(adViewObject);
							continue;
						}
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0   &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) <= 0){
						    adViewObjListData.add(adViewObject);
						    continue;
						}
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0    &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) > 0){
						    adViewObjListData.add(adViewObject);
						}
				    }catch(Exception e){
					e.printStackTrace();
				    }
				}
			} else {
				adViewObjListData = adViewObjList;
			}
			
			
			
			
			StringBuffer hql_ = new StringBuffer();
			hql_.append(" SELECT "); 
			hql_.append(" a.pfpAdGroup.pfpAdAction.adActionCreatTime, ");
			hql_.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
			hql_.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
			hql_.append(" a.pfpAdGroup.pfpAdAction.adActionName, ");
			hql_.append(" a.adStatus, ");
			hql_.append(" 'No Click', ");
			hql_.append(" '0', ");
			hql_.append(" '0', ");
			hql_.append(" '0', ");
			hql_.append(" '0', ");
			hql_.append(" '0', ");
			hql_.append(" a.pfpAdGroup.adGroupName, ");
			hql_.append(" a.pfpAdGroup.adGroupStatus, ");
			hql_.append(" a.adSeq,  ");
			hql_.append(" a.templateProductSeq,  ");
			hql_.append(" a.pfpAdGroup.adGroupSeq,  ");
			hql_.append(" a.pfpAdGroup.pfpAdAction.adActionStartDate,  ");
			hql_.append(" a.pfpAdGroup.pfpAdAction.adActionEndDate,  ");
			hql_.append(" COALESCE(a.adVerifyRejectReason,'')  ");
			hql_.append(" FROM PfpAd a "); 
			hql_.append(" where 1=1 ");
			//判斷加上查詢條件
			if(adViewConditionMap.get("userAccount")!= null && adViewConditionMap.get("userAccount").trim().length()>0){
				hql_.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adViewConditionMap.get("userAccount").trim()+"'");
			}
			if(adViewConditionMap.get("adType")!= null && adViewConditionMap.get("adType").trim().length()>0){
				hql_.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adViewConditionMap.get("adType").trim()+"'");
			}
			if(adViewConditionMap.get("adGroupSeq")!= null && adViewConditionMap.get("adGroupSeq").trim().length()>0){
				hql_.append(" and a.pfpAdGroup.adGroupSeq ='"+adViewConditionMap.get("adGroupSeq").trim()+"'");
			}
			if(adViewConditionMap.get("searchAdStatus")!= null && adViewConditionMap.get("searchAdStatus").trim().length()>0){
				hql_.append(" and a.adStatus ='"+adViewConditionMap.get("searchAdStatus").trim()+"'");
			}
			if(adViewConditionMap.get("adAdViewName")!= null && adViewConditionMap.get("adAdViewName").trim().length()>0){
				hql_.append(" and a.pfpAdGroup.pfpAdAction.adActionName like '%"+adViewConditionMap.get("adAdViewName").trim()+"%'");
			}
			
			if(!StringUtils.isBlank(adViewConditionMap.get("adSeq")) && adViewConditionMap.get("adSeq").trim().length()>0){
				hql_.append(" and a.adSeq = '"+adViewConditionMap.get("adSeq").trim()+"'");
			}
			
			if(!StringUtils.isBlank(adViewConditionMap.get("adSeqListString")) && adViewConditionMap.get("adSeqListString").trim().length()>0){
				hql_.append(" and a.adSeq in ("+adViewConditionMap.get("adSeqListString").trim()+")");
			}
			
			Query query_ = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql_.toString());
			List<Object> adObjListData =  new ArrayList<Object>();
			adObjListData = query_.list();
			List<Object> adObjList2 =  new ArrayList<Object>();
			if(adStartDate != null && adEndDate != null){
				for (Object adObject : adObjListData) {
				    Object[] obj = (Object[]) adObject;
				    try{
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) <= 0 &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adStartDate)) >= 0 ){
							adObjList2.add(adObject);
							continue;
						}
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0   &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) <= 0){
						    adObjList2.add(adObject);
						    continue;
						}
						if(sdf.parse(obj[16].toString()).compareTo(sdf.parse(adStartDate)) >= 0    &&  sdf.parse(obj[17].toString()).compareTo(sdf.parse(adEndDate)) > 0){
						    adObjList2.add(adObject);
						}
				    }catch(Exception e){
					e.printStackTrace();
				    }
				}
			} else {
				adObjList2 = adObjListData;
			}
			
			
			for (Object object : adViewObjListData) {
			    Object[] obj = (Object[]) object;
			    for (Object object2 : adObjList2) {
				  Object[] obj2 = (Object[]) object2;
				  if(obj2[13].equals(obj[13])){
				      adObjList2.remove(obj2);
				      break;
				  }
			    }
			}
			for (Object object : adObjList2) {
			    adViewObjListData.add(object);
			}
		} else {
			adViewObjListData = adViewObjList;
		}
		
		
		return adViewObjListData.size();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdAdPvclk(final String userAccount, final String adStatus, final int adType, final String keyword, final String adGroupSeq, final String adSeq, final String adPvclkDevice, final String dateType, final Date startDate, final Date endDate, final int page, final int pageSize) throws Exception{
		List<Object> result = getHibernateTemplate().execute(

                new HibernateCallback<List<Object> >() {

					public List<Object>  doInHibernate(Session session) throws HibernateException {

						StringBuffer hql = new StringBuffer();

						hql.append(" select paa.pfpAdGroup.pfpAdAction.adActionSeq, ");
						hql.append(" 		paa.pfpAdGroup.pfpAdAction.adActionName, ");
						hql.append(" 		paa.pfpAdGroup.pfpAdAction.adType, ");
						hql.append(" 		paa.pfpAdGroup.adGroupSeq, ");
						hql.append(" 		paa.pfpAdGroup.adGroupName, ");
						hql.append(" 		paa.pfpAdGroup.adGroupStatus, ");
						hql.append(" 		paa.adSeq, ");
						hql.append(" 		paa.templateProductSeq, ");
						hql.append(" 		paa.adStatus, ");
						hql.append("		papc.adPvclkDevice, ");
						hql.append(" 		COALESCE(paa.adVerifyRejectReason,''), ");
						hql.append(" 		COALESCE(sum(papc.adPv),0), ");
						hql.append(" 		COALESCE(sum(papc.adClk),0), ");
						hql.append(" 		COALESCE(sum(papc.adClkPrice),0), ");
						hql.append("  		paa.adCreateTime, ");
						hql.append("  		paa.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
						hql.append("  		paa.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
						hql.append("		COALESCE(sum(papc.adInvalidClk),0), ");
						hql.append("		COALESCE(sum(papc.adInvalidClkPrice),0) ");
						hql.append(" from PfpAd as paa ");
						hql.append(" 		left join paa.pfpAdPvclks papc ");

						if(adType > 0){
							hql.append(" 			with (papc.adType = :adType ");
						}else{
							hql.append(" 			with (papc.adType != :adType ");
						}
						hql.append(")");
						hql.append(" 		left join paa.pfpAdDetails paad ");
						hql.append(" 			with (paad.adDetailId = 'title') ");

						hql.append(" where 1 = 1");
						if(StringUtils.isNotEmpty(userAccount)) {
							hql.append(" and paa.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId like :userAccount");
						}
						if(StringUtils.isNotEmpty(adStatus)) {
							hql.append(" and paa.adStatus = :adStatus ");
						}
						if(StringUtils.isNotEmpty(keyword)) {
							hql.append(" and (paa.pfpAdGroup.pfpAdAction.adActionName like :keyword ");
							hql.append(" 	  or paa.pfpAdGroup.pfpAdAction.adActionDesc like :keyword");
							hql.append(" 	  or paa.pfpAdGroup.adGroupName like :keyword");
							hql.append(" 	  or paad.adDetailContent like :keyword)");
						}
						if(StringUtils.isNotEmpty(adGroupSeq)) {
							hql.append(" and paa.pfpAdGroup.adGroupSeq = :adGroupSeq ");
						}
						if(StringUtils.isNotEmpty(adSeq)) {
							hql.append(" and paa.adSeq = :adSeq ");
						}

						if(StringUtils.isNotBlank(adPvclkDevice)) {
							hql.append(" and papc.adPvclkDevice = :adPvclkDevice ");
						}

						//if(startDate != null && endDate != null) {
						//	hql.append(" 					and paa.adCreateTime >= :startDate ");
						//	hql.append(" 					and paa.adCreateTime <= :endDate ");
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
								hql.append(" 					and ((paa.pfpAdGroup.pfpAdAction.adActionStartDate between :startDate and :endDate)");
								hql.append(" 					or (paa.pfpAdGroup.pfpAdAction.adActionEndDate between :startDate and :endDate)");
								hql.append(" 					or (paa.pfpAdGroup.pfpAdAction.adActionStartDate < :startDate and paa.pfpAdGroup.pfpAdAction.adActionEndDate > :endDate)) ");
							}
						}
						hql.append(" group by paa.adSeq ");
						hql.append(" order by paa.adCreateTime desc ");

						log.info(hql);
						/*
						log.info(" startDate = "+startDate);
						log.info(" endDate = "+endDate);
						log.info(" adType = "+adType);
						log.info(" customerInfoId = "+customerInfoId);
						log.info(" EnumStatus.Close.getStatusId() = "+EnumStatus.Close.getStatusId());
						log.info(" adGroupSeq = "+adGroupSeq);
						log.info(" keyword = "+keyword);
						*/
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

						if(StringUtils.isNotEmpty(adGroupSeq)) {
							q.setString("adGroupSeq", adGroupSeq);
						}
						if(StringUtils.isNotEmpty(adSeq)) {
							q.setString("adSeq", adSeq);
						}

						if(StringUtils.isNotBlank(adPvclkDevice)) {
							q.setString("adPvclkDevice", adPvclkDevice);
						}

						// status 不是空的才使用這個條件
						if(StringUtils.isNotEmpty(adStatus)) {
							q.setString("adStatus", adStatus);
						}
//									.setDate("startDate", startDate)
//									.setDate("endDate", endDate)
//									.setInteger("adType", adType)
//									.setString("userAccount", userAccount)
//									.setInteger("adStatus", EnumAdStatus.Close.getStatusId())
//									.setString("adGroupSeq", adGroupSeq)
//									.setString("keyword", "%"+keyword+"%");


						//page=-1 取得全部不分頁用於download
						if(page!=-1){
							q.setFirstResult((page-1)*pageSize)
								.setMaxResults(pageSize);
						}

						//log.info(" resultData size  = "+ q.list().size());

						return q.list();
                    }
                }
        );

        return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getValidAd(String customerInfoId, Date today) throws Exception{

		StringBuffer hql = new StringBuffer();

		int statusId = EnumAdStatus.Open.getStatusId();

		hql.append(" select pfpAdGroup.pfpAdAction.adActionSeq,  ");
		hql.append(" 		pfpAdGroup.pfpAdAction.adActionMax, ");
		hql.append(" 		pfpAdGroup.pfpAdAction.adActionControlPrice ");
		hql.append(" from PfpAd ");
		hql.append(" where pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = ? ");
        hql.append(" and adStatus = ? ");
        hql.append(" and pfpAdGroup.adGroupStatus = ? ");
        hql.append(" and pfpAdGroup.pfpAdAction.adActionStatus = ? ");
        hql.append(" and pfpAdGroup.pfpAdAction.adActionStartDate <= ? ");
		hql.append(" and pfpAdGroup.pfpAdAction.adActionEndDate >= ? ");
        hql.append(" group by pfpAdGroup.pfpAdAction.adActionSeq ");

        //log.info(" hql = "+hql);

        Object[] values = new Object[]{customerInfoId, statusId, statusId, statusId, today, today};

        return (List<Object>) super.getHibernateTemplate().find(hql.toString(), values);
	}

	@SuppressWarnings("unchecked")
	public List<PfpAd> validAdAd(String adGroupSeq) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAd ");
		hql.append(" where pfpAdGroup.adGroupSeq = ? ");
		hql.append(" and adStatus != ? ");


		return (List<PfpAd>) super.getHibernateTemplate().find(hql.toString(),adGroupSeq,EnumAdStatus.Close.getStatusId());
	}

	public int selectAdNewByUserVerifyDate(String userVerifyDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(adSeq) ");
        hql.append("from PfpAd ");
        hql.append("where adStatus = :adStatus ");
        hql.append("    and adUserVerifyTime >= :startDate ");
        hql.append("    and adUserVerifyTime <= :endDate ");
        hql.append("    and pfpAdGroup.adGroupStatus = :groupStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStatus = :actionStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = :customerStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");

        return ((Long) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("adStatus", EnumAdStatus.Open.getStatusId())
                .setString("startDate", userVerifyDate + " 00:00:00")
                .setString("endDate", userVerifyDate + " 23:59:59")
                .setInteger("groupStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("actionStatus", EnumAdStatus.Open.getStatusId())
                .setString("customerStatus", EnumAccountStatus.START.getStatus())
                .uniqueResult())
                .intValue();
	}

	public int selectAdNumByActionDate(String actionDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(adSeq) ");
        hql.append("from PfpAd ");
        hql.append("where adStatus = :adStatus ");
        hql.append("    and pfpAdGroup.adGroupStatus = :groupStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStatus = :actionStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStartDate <= :startDate ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionEndDate >= :endDate ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = :customerStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");

        return ((Long) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("adStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("groupStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("actionStatus", EnumAdStatus.Open.getStatusId())
                .setString("startDate", actionDate)
                .setString("endDate", actionDate)
                .setString("customerStatus", EnumAccountStatus.START.getStatus())
                .uniqueResult())
                .intValue();
	}

	public int selectAdReadyByActionDate(String actionDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(adSeq) ");
        hql.append("from PfpAd ");
        hql.append("where adStatus = :adStatus ");
        hql.append("    and pfpAdGroup.adGroupStatus = :groupStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStatus = :actionStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStartDate > :actionDate ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = :customerStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");

        return ((Long) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("adStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("groupStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("actionStatus", EnumAdStatus.Open.getStatusId())
                .setString("actionDate", actionDate)
                .setString("customerStatus", EnumAccountStatus.START.getStatus())
                .uniqueResult())
                .intValue();
	}

	public int selectAdDueByActionDate(String startDate, String endDate) {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(adSeq) ");
        hql.append("from PfpAd ");
        hql.append("where adStatus = :adStatus ");
        hql.append("    and pfpAdGroup.adGroupStatus = :groupStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionStatus = :actionStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionEndDate >= :startDate ");
        hql.append("    and pfpAdGroup.pfpAdAction.adActionEndDate <= :endDate ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = :customerStatus ");
        hql.append("    and pfpAdGroup.pfpAdAction.pfpCustomerInfo.recognize = 'Y' ");

        return ((Long) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("adStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("groupStatus", EnumAdStatus.Open.getStatusId())
                .setInteger("actionStatus", EnumAdStatus.Open.getStatusId())
                .setString("startDate", startDate)
                .setString("endDate", endDate)
                .setString("customerStatus", EnumAccountStatus.START.getStatus())
                .uniqueResult())
                .intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAdSeq() throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("select distinct ad_seq from pfp_ad_pvclk ");

		//sql.append("select distinct ad_keyword_seq from pfp_ad_keyword_pvclk ");

		return this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	}

	public void updatePfpAdPvclk(final String adSeq, final String adGroupSeq, final String adActionSeq) throws Exception{

		getHibernateTemplate().execute(
	            new HibernateCallback<List<Object>>() {
	                public List<Object> doInHibernate(Session session) throws HibernateException {

	            		StringBuffer sql = new StringBuffer();

	            		sql.append("update pfp_ad_pvclk set ad_group_seq = '" + adGroupSeq + "', ad_action_seq  = '" + adActionSeq + "'");
	            		sql.append(" where ad_seq = '" + adSeq + "'");

//	            		sql.append("update pfp_ad_keyword_pvclk set ad_group_seq = '" + adGroupSeq + "', ad_action_seq  = '" + adActionSeq + "'");
//	            		sql.append(" where ad_keyword_seq = '" + adSeq + "'");

	            		session.createSQLQuery(sql.toString()).executeUpdate();

	                	return new ArrayList<Object>();
	                }
	            });
	}

	/**
	 * 手動補資料 2013/11/8 
	 */
	public static void main(String args[]) throws Exception{

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfpAdDAO adDao = (PfpAdDAO) context.getBean("PfpAdDAO");
//		PfpAdKeywordDAO keywordDao = (PfpAdKeywordDAO) context.getBean("PfpAdKeywordDAO");
		PfpAdGroupDAO adGroupDao = (PfpAdGroupDAO) context.getBean("PfpAdGroupDAO");

		System.out.println(">>> start");

		List<Object> objList = adDao.getAdSeq();
		System.out.println(">>> objList.size() = " + objList.size());

		String adSeq = null;
		String adGroupSeq = null;
		String adActionSeq = null;
		for (int i=0; i<objList.size(); i++) {

			adSeq = (String) objList.get(i);
			System.out.println(">>> adSeq = " + adSeq);

			PfpAd pfpAd = adDao.getPfpAdBySeq(adSeq);
			//PfpAdKeyword pfpAdKeyword = keywordDao.findPfpAdKeywordBySeq(adSeq);

			PfpAdGroup pfpAdGroup = pfpAd.getPfpAdGroup();
			//PfpAdGroup pfpAdGroup = pfpAdKeyword.getPfpAdGroup();
			adGroupSeq = pfpAdGroup.getAdGroupSeq();
			System.out.println(">>> adGroupSeq = " + adGroupSeq);

			PfpAdGroup pfpAdGroup2 = adGroupDao.getPfpAdGroupBySeq(adGroupSeq);
			PfpAdAction pfpAdAction = pfpAdGroup2.getPfpAdAction();
			adActionSeq = pfpAdAction.getAdActionSeq();
			System.out.println(">>> adActionSeq = " + adActionSeq);

			adDao.updatePfpAdPvclk(adSeq, adGroupSeq, adActionSeq);
		}

		System.out.println(">>> end");
	}


	// 2014-04-24 
	@SuppressWarnings("unchecked")
	public List<String> getPfpAdSeqByAdStyle(String adStyle) throws Exception {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select adSeq from PfpAd where 1=1");
		if (StringUtils.isNotBlank(adStyle)) {
			sql.append(" and adStyle = :adStyle");
			sqlParams.put("adStyle", adStyle);
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("adStyle")) {
				q.setParameter("adStyle", sqlParams.get(paramName));
			}
        }
		
		List<String> adSeqList = q.list();
		System.out.println("adSeqList.size() = " + adSeqList.size());
		
		return adSeqList;
	}
}
