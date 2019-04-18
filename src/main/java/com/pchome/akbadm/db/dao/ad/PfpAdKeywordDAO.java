package com.pchome.akbadm.db.dao.ad;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.ad.EnumAdType;

public class PfpAdKeywordDAO extends BaseDAO<PfpAdKeyword, String> implements IPfpAdKeywordDAO {

	@SuppressWarnings("unchecked")
	public List<PfpAdKeyword> findPfpAdKeywordByKeyword(String keyword) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfpAdKeyword ");
		hql.append(" where adKeyword = '"+keyword+"' ");
		hql.append(" and  adKeywordStatus = "+EnumAdStatus.Open.getStatusId()+" ");
		hql.append(" and  pfpAdGroup.adGroupStatus = "+EnumAdStatus.Open.getStatusId()+" ");
		hql.append(" and  pfpAdGroup.pfpAdAction.adActionStatus = "+EnumAdStatus.Open.getStatusId()+" ");
		hql.append(" and  pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = '"+EnumAccountStatus.START.getStatus()+"' ");
		hql.append(" order by adKeywordUpdateTime ");
		
		log.info(" hql  = "+hql);

		return (List<PfpAdKeyword>) super.getHibernateTemplate().find(hql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public PfpAdKeyword findPfpAdKeywordBySeq(String adKeywordSeq) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAdKeyword where adKeywordSeq = '"+adKeywordSeq+"' ");
		
		List<PfpAdKeyword> list = (List<PfpAdKeyword>) super.getHibernateTemplate().find(hql.toString());
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		else{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAdKeywordPriceList(String keyword, float sysprice, String today) throws Exception{
		
		int statusId = EnumAdStatus.Open.getStatusId();
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select adKeywordSearchPrice ");
		hql.append(" from PfpAdKeyword ");
		hql.append(" where adKeywordStatus = "+statusId+" and pfpAdGroup.adGroupStatus = "+statusId+" ");
		hql.append(" and pfpAdGroup.pfpAdAction.adActionStatus = "+statusId+" ");
		hql.append(" and pfpAdGroup.pfpAdAction.adActionEndDate >= '"+today+"' ");
		hql.append(" and pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = '"+EnumAccountStatus.START.getStatus()+"' ");
		hql.append(" and adKeywordSearchPrice < "+sysprice+" ");
		hql.append(" group by adKeywordSearchPrice ");
		
		return (List<Object>) super.getHibernateTemplate().find(hql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdRank(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String dateType, Date startDate, Date endDate) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select pakar.pfpAdKeyword.adKeywordSeq, ");
		hql.append(" 		COALESCE(sum(pakar.adRankAvg)/count(pakar.adRankAvg),0) ");
		hql.append(" from PfpAdKeyword as pak ");
		hql.append(" 		left join pak.pfpAdRanks pakar ");
		hql.append("			with (pakar.adRankDate >= :startDate ");
		hql.append("					and pakar.adRankDate <= :endDate ");
		hql.append(" 					and pakar.adType = :adRankType)  ");
		hql.append(" where pak.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoId = :customerInfoId ");
		hql.append(" and pak.adKeywordStatus != :status ");
		hql.append(" and pak.pfpAdGroup.adGroupSeq = :adGroupSeq ");
		hql.append(" and pak.adKeyword like :keyword ");
		hql.append(" group by pak.adKeywordSeq ");
		hql.append(" order by pak.adKeywordCreateTime desc ");
		
		Query q = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString())
							.setDate("startDate", startDate)
							.setDate("endDate", endDate)
							.setInteger("adRankType", EnumAdType.AD_SEARCH.getType())									
							.setString("customerInfoId", userAccount)
							.setInteger("status", EnumAdStatus.Close.getStatusId())									
							.setString("adGroupSeq", adGroupSeq)
							.setString("keyword", "%"+keyword+"%");
		
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdKeywordReport(final Map<String,String> adKeywordViewConditionMap) throws Exception{
	    final List<Object> getAdKeywordReportObjList = getHibernateTemplate().execute(
		    new HibernateCallback<List<Object>>() {
			    public List<Object> doInHibernate(Session session) throws HibernateException {
			    
			    StringBuffer hql = new StringBuffer();
			    	
			    hql.append(" select ");
			    hql.append(" a.adKeywordCreateTime, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
				hql.append(" a.pfpAdGroup.pfpAdAction.adActionName, ");
				hql.append(" a.pfpAdGroup.adGroupName, ");
				hql.append(" a.pfpAdGroup.adGroupSeq, ");
				hql.append(" a.adKeyword, ");
				hql.append(" a.adKeywordStatus, ");
				hql.append(" a.adKeywordOpen, ");						//廣泛比對狀態
				hql.append(" a.adKeywordPhraseOpen, ");					//詞組比對狀態
				hql.append(" a.adKeywordPrecisionOpen, ");				//精準比對狀態
				hql.append(" a.adKeywordSearchPrice, ");				//廣泛比對出價
				hql.append(" a.adKeywordSearchPhrasePrice, ");			//詞組比對出價
				hql.append(" a.adKeywordSearchPrecisionPrice, ");		//精準比對出價
				hql.append(" a.adKeywordSeq ");
				
			   
				hql.append(" FROM PfpAdKeyword a "); 
				hql.append(" where 1=1 ");
			    
			    //判斷加上查詢條件
				/*if(adKeywordViewConditionMap.get("startDate")!= null && adKeywordViewConditionMap.get("startDate").trim().length()>0){
					hql.append("and IFNULL(b.adKeywordPvclkDate,'"+adKeywordViewConditionMap.get("startDate")+"') >='"+adKeywordViewConditionMap.get("startDate")+"'");
				}*/
				if(adKeywordViewConditionMap.get("endDate")!= null && adKeywordViewConditionMap.get("endDate").trim().length()>0){
					hql.append("and a.adKeywordCreateTime <='"+adKeywordViewConditionMap.get("endDate")+" 23:59:59'");
				}
				if(adKeywordViewConditionMap.get("userAccount")!= null && adKeywordViewConditionMap.get("userAccount").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adKeywordViewConditionMap.get("userAccount").trim()+"'");
				}
				if(adKeywordViewConditionMap.get("adType")!= null && adKeywordViewConditionMap.get("adType").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adKeywordViewConditionMap.get("adType").trim()+"'");
				}
				if(adKeywordViewConditionMap.get("keyword")!= null && adKeywordViewConditionMap.get("keyword").trim().length()>0){
					hql.append(" and a.adKeyword like '%"+adKeywordViewConditionMap.get("keyword").trim()+"%'");
				}
				if(adKeywordViewConditionMap.get("adGroupSeq")!= null && adKeywordViewConditionMap.get("adGroupSeq").trim().length()>0){
					hql.append(" and a.pfpAdGroup.adGroupSeq ='"+adKeywordViewConditionMap.get("adGroupSeq").trim()+"'");
				}
				
				if(adKeywordViewConditionMap.get("searchAdStatus")!= null && adKeywordViewConditionMap.get("searchAdStatus").trim().length()>0){
					hql.append(" and a.adKeywordStatus ='"+adKeywordViewConditionMap.get("searchAdStatus").trim()+"'");
				}
				
				hql.append(" group by a.adKeywordSeq");
			    
				
				int pageSize = Integer.valueOf(adKeywordViewConditionMap.get("pageSize"));
				int page = Integer.valueOf(adKeywordViewConditionMap.get("pageNo"));
				Query query = session.createQuery(hql.toString());
				if(page!=-1){
				    query.setFirstResult((page-1)*pageSize).setMaxResults(pageSize);
				}
				return query.list();
			    }
		        }
	    );
	    return getAdKeywordReportObjList;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getAdKeywordReportData(final Map<String,String> adKeywordViewConditionMap) throws Exception{
		Map<String,Object> reportData = new HashMap<String,Object>();
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select ");
		hql.append(" adKeywordSeq, ");
		
		//廣泛比對
		hql.append(" sum(adKeywordPv), ");
		hql.append(" sum(adKeywordClk), ");
		hql.append(" sum(adKeywordClkPrice), ");
		hql.append(" sum(adKeywordInvalidClk), ");
		hql.append(" sum(adKeywordInvalidClkPrice), ");
	    
	    //詞組比對
		hql.append(" sum(adKeywordPhrasePv), ");
		hql.append(" sum(adKeywordPhraseClk), ");
		hql.append(" sum(adKeywordPhraseClkPrice), ");
		hql.append(" sum(adKeywordPhraseInvalidClk), ");
		hql.append(" sum(adKeywordPhraseInvalidClkPrice), ");
	    
	    //精準比對
		hql.append(" sum(adKeywordPrecisionPv), ");
		hql.append(" sum(adKeywordPrecisionClk), ");
		hql.append(" sum(adKeywordPrecisionClkPrice), ");
		hql.append(" sum(adKeywordPrecisionInvalidClk), ");
		hql.append(" sum(adKeywordPrecisionInvalidClkPrice) ");
		hql.append(" FROM PfpAdKeywordReport "); 
		hql.append(" where 1=1 ");
		//判斷加上查詢條件
		if(adKeywordViewConditionMap.get("startDate")!= null && adKeywordViewConditionMap.get("startDate").trim().length()>0){
			hql.append("and adKeywordPvclkDate >='"+adKeywordViewConditionMap.get("startDate")+"'");
		}
		if(adKeywordViewConditionMap.get("endDate")!= null && adKeywordViewConditionMap.get("endDate").trim().length()>0){
			hql.append("and adKeywordPvclkDate <='"+adKeywordViewConditionMap.get("endDate")+"'");
		}
		if(adKeywordViewConditionMap.get("adKeywordPvclkDevice")!= null && adKeywordViewConditionMap.get("adKeywordPvclkDevice").trim().length()>0){
			if(!adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("ALL Device")){
				hql.append(" and adKeywordPvclkDevice ='"+adKeywordViewConditionMap.get("adKeywordPvclkDevice").trim()+"'");
			}
		}
		
		hql.append(" group by adKeywordSeq");
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		List<Object> list = query.list();
		if(!list.isEmpty()){
			for(int i = 0; i < list.size(); i++){
				Object[] obj = (Object[]) list.get(i);
				String adKeywordSeq = obj[0].toString();
				reportData.put(adKeywordSeq, list.get(i));
			}
		}
		
		return reportData;
	}
	
	public List<Object> getAdKeywordReportSeq(final Map<String,String> adKeywordViewConditionMap) throws Exception{
	    final List<Object> getAdKeywordReportObjList = getHibernateTemplate().execute(
		    new HibernateCallback<List<Object>>() {
			    public List<Object> doInHibernate(Session session) throws HibernateException {
			    
			    StringBuffer hql = new StringBuffer();
			    	
			    hql.append(" select ");
			    
				hql.append(" a.adKeywordSeq ");
				
			   
				hql.append(" FROM PfpAdKeyword a "); 
				hql.append(" where 1=1 ");
			    
			    //判斷加上查詢條件
				/*if(adKeywordViewConditionMap.get("startDate")!= null && adKeywordViewConditionMap.get("startDate").trim().length()>0){
					hql.append("and IFNULL(b.adKeywordPvclkDate,'"+adKeywordViewConditionMap.get("startDate")+"') >='"+adKeywordViewConditionMap.get("startDate")+"'");
				}*/
				if(adKeywordViewConditionMap.get("endDate")!= null && adKeywordViewConditionMap.get("endDate").trim().length()>0){
					hql.append("and a.adKeywordCreateTime <='"+adKeywordViewConditionMap.get("endDate")+" 23:59:59'");
				}
				if(adKeywordViewConditionMap.get("userAccount")!= null && adKeywordViewConditionMap.get("userAccount").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adKeywordViewConditionMap.get("userAccount").trim()+"'");
				}
				if(adKeywordViewConditionMap.get("adType")!= null && adKeywordViewConditionMap.get("adType").trim().length()>0){
					hql.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adKeywordViewConditionMap.get("adType").trim()+"'");
				}
				if(adKeywordViewConditionMap.get("keyword")!= null && adKeywordViewConditionMap.get("keyword").trim().length()>0){
					hql.append(" and a.adKeyword like '%"+adKeywordViewConditionMap.get("keyword").trim()+"%'");
				}
				if(adKeywordViewConditionMap.get("adGroupSeq")!= null && adKeywordViewConditionMap.get("adGroupSeq").trim().length()>0){
					hql.append(" and a.pfpAdGroup.adGroupSeq ='"+adKeywordViewConditionMap.get("adGroupSeq").trim()+"'");
				}
				
				if(adKeywordViewConditionMap.get("searchAdStatus")!= null && adKeywordViewConditionMap.get("searchAdStatus").trim().length()>0){
					hql.append(" and a.adKeywordStatus ='"+adKeywordViewConditionMap.get("searchAdStatus").trim()+"'");
				}
				
				hql.append(" group by a.adKeywordSeq");
			    
				int pageSize = Integer.valueOf(adKeywordViewConditionMap.get("pageSize"));
				int page = Integer.valueOf(adKeywordViewConditionMap.get("pageNo"));
				Query query = session.createQuery(hql.toString());
				if(page!=-1){
				    query.setFirstResult((page-1)*pageSize).setMaxResults(pageSize);
				}
				return query.list();
			    }
		        }
	    );
	    return getAdKeywordReportObjList;
	}
	
	public int getAdKeywordReportSize(Map<String,String> adKeywordViewConditionMap) throws Exception{
	    StringBuffer hql = new StringBuffer();
		hql.append(" SELECT count(*) "); 
		hql.append(" FROM PfpAdKeyword a,PfpAdKeywordReport b "); 
		hql.append(" where 1=1 ");
		hql.append(" and a.adKeywordSeq = b.adKeywordSeq ");
		//判斷加上查詢條件
		String adStartDate = null;
		String adEndDate = null;
		if(adKeywordViewConditionMap.get("startDate")!= null && adKeywordViewConditionMap.get("startDate").trim().length()>0){
			adStartDate = adKeywordViewConditionMap.get("startDate").trim();
		}
		if(adKeywordViewConditionMap.get("endDate")!= null && adKeywordViewConditionMap.get("endDate").trim().length()>0){
			adEndDate = adKeywordViewConditionMap.get("endDate").trim();
		}
		if(adKeywordViewConditionMap.get("userAccount")!= null && adKeywordViewConditionMap.get("userAccount").trim().length()>0){
			hql.append(" and a.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId = '"+adKeywordViewConditionMap.get("userAccount").trim()+"'");
		}
		if(adKeywordViewConditionMap.get("adType")!= null && adKeywordViewConditionMap.get("adType").trim().length()>0){
			hql.append(" and a.pfpAdGroup.pfpAdAction.adType ='"+adKeywordViewConditionMap.get("adType").trim()+"'");
		}
		if(adKeywordViewConditionMap.get("searchAdStatus")!= null && adKeywordViewConditionMap.get("searchAdStatus").trim().length()>0){
			hql.append(" and a.adKeywordStatus ='"+adKeywordViewConditionMap.get("searchAdStatus").trim()+"'");
		}
		if(adKeywordViewConditionMap.get("keyword")!= null && adKeywordViewConditionMap.get("keyword").trim().length()>0){
			hql.append(" and a.adKeyword ='"+adKeywordViewConditionMap.get("keyword").trim()+"'");
		}
		if(adKeywordViewConditionMap.get("adGroupSeq")!= null && adKeywordViewConditionMap.get("adGroupSeq").trim().length()>0){
			hql.append(" and a.pfpAdGroup.adGroupSeq ='"+adKeywordViewConditionMap.get("adGroupSeq").trim()+"'");
		}
		if(adKeywordViewConditionMap.get("adKeywordPvclkDevice")!= null && adKeywordViewConditionMap.get("adKeywordPvclkDevice").trim().length()>0){
			if(!adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("ALL Device")){
				hql.append(" and b.adKeywordPvclkDevice ='"+adKeywordViewConditionMap.get("adKeywordPvclkDevice").trim()+"'");
			}
		}
//		if(!StringUtils.isEmpty(adKeywordViewConditionMap.get("adPvclkDate")) && adKeywordViewConditionMap.get("adPvclkDate").equals("adPvclkDate")){
//			hql.append(" and b.adKeywordPvclkDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}	
//		if(!StringUtils.isEmpty(adKeywordViewConditionMap.get("adActionDate")) &&   adKeywordViewConditionMap.get("adActionDate").equals("adActionDate")){
//			hql.append(" and a.pfpAdGroup.pfpAdAction.adActionStartDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}
		hql.append("and a.pfpAdGroup.pfpAdAction.adActionStartDate >='"+adKeywordViewConditionMap.get("startDate")+"'");
		hql.append("and a.pfpAdGroup.pfpAdAction.adActionStartDate <='"+adKeywordViewConditionMap.get("endDate")+"'");
		if(!Boolean.valueOf(adKeywordViewConditionMap.get("changeSelect"))){
		    if(adKeywordViewConditionMap.get("adKeywordPvclkDevice").equals("ALL Device")){
			hql.append(" group by b.adKeywordPvclkDevice ,b.adKeywordSeq");
		    }else{
			hql.append(" group by b.adKeywordPvclkDate, b.adKeywordPvclkDevice");
		    }
		}else if(Boolean.valueOf(adKeywordViewConditionMap.get("changeSelect"))){
		    hql.append(" group by b.adKeywordPvclkDate, b.adKeywordPvclkDevice");
		}
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return query.list().size();
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdKeywordPvclk(final String userAccount, final String adStatus, final int adType, final String keyword, final String adGroupSeq, final String adKeywordPvclkDevice, final String dateType, final Date startDate, final Date endDate, final int page, final int pageSize) throws Exception{
		List<Object> result = (List<Object> ) getHibernateTemplate().execute(
				 
                new HibernateCallback<List<Object> >() {
                	
					public List<Object>  doInHibernate(Session session) throws HibernateException {
						
						StringBuffer hql = new StringBuffer();
						
						hql.append(" select pak.pfpAdGroup.pfpAdAction.adActionSeq, ");
						hql.append(" 		pak.pfpAdGroup.pfpAdAction.adActionName, ");
						hql.append(" 		pak.pfpAdGroup.pfpAdAction.adActionStatus, ");
						hql.append(" 		pak.pfpAdGroup.adGroupSeq, ");
						hql.append(" 		pak.pfpAdGroup.adGroupName, ");
						hql.append(" 		pak.pfpAdGroup.adGroupStatus, ");
						hql.append(" 		pak.adKeywordSeq, ");
						hql.append(" 		pak.adKeyword, ");
						hql.append(" 		pak.adKeywordStatus, ");
						hql.append(" 		pak.adKeywordSearchPrice, ");
						hql.append(" 		pak.adKeywordChannelPrice, ");
						hql.append("		pakpc.adKeywordPvclkDevice, ");
						hql.append(" 		COALESCE(sum(pakpc.adKeywordPv),0), ");
						hql.append(" 		COALESCE(sum(pakpc.adKeywordClk),0), ");
						hql.append(" 		COALESCE(sum(pakpc.adKeywordClkPrice),0), ");
						hql.append("  		pak.adKeywordCreateTime, ");
						hql.append("  		pak.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId, ");
						hql.append("  		pak.pfpAdGroup.pfpAdAction.pfpCustomerInfo.customerInfoTitle, ");
						hql.append("		COALESCE(sum(pakpc.adKeywordInvalidClk),0), ");
						hql.append("		COALESCE(sum(pakpc.adKeywordInvalidClkPrice),0) ");
						hql.append(" from PfpAdKeyword as pak ");
						hql.append(" 		left join pak.pfpAdKeywordPvclks pakpc ");

						if(adType > 0){
							hql.append(" 			with (pakpc.adKeywordType = :adType ");
						}else{
							hql.append(" 			with (pakpc.adKeywordType != :adType ");
						}
						hql.append(")");

						hql.append(" where 1 = 1");
						if(StringUtils.isNotEmpty(userAccount)) {
							hql.append(" and pak.pfpAdGroup.pfpAdAction.pfpCustomerInfo.memberId like :userAccount");
						}
						if(StringUtils.isNotEmpty(adStatus)) {
							hql.append(" and pak.adKeywordStatus = :adStatus ");
						}
						if(StringUtils.isNotEmpty(keyword)) {
							hql.append(" and (pak.pfpAdGroup.pfpAdAction.adActionName like :keyword ");
							hql.append(" 	  or pak.pfpAdGroup.pfpAdAction.adActionDesc like :keyword");
							hql.append(" 	  or pak.pfpAdGroup.adGroupName like :keyword");
							hql.append(" 	  or pak.adKeyword like :keyword)");
						}
						if(StringUtils.isNotEmpty(adGroupSeq)) {
							hql.append(" and pak.pfpAdGroup.adGroupSeq = :adGroupSeq ");
						}

						if(StringUtils.isNotBlank(adKeywordPvclkDevice)) {
							hql.append(" and pakpc.adKeywordPvclkDevice = :adKeywordPvclkDevice ");
						}

						//if(startDate != null && endDate != null) {
						//	hql.append(" 					and pak.adKeywordCreateTime >= :startDate ");
						//	hql.append(" 					and pak.adKeywordCreateTime <= :endDate ");
						//}
						if(StringUtils.isNotBlank(dateType)) {
							if(dateType.equals("adPvclkDate")) {
								System.out.println("startDate = " + startDate);
								System.out.println("endDate = " + endDate);
								if(startDate == endDate) {
									hql.append(" 					and (pakpc.adKeywordPvclkDate = :startDate ");
								} else {
									hql.append(" 					and (pakpc.adKeywordPvclkDate >= :startDate ");
									hql.append(" 					and pakpc.adKeywordPvclkDate <= :endDate) ");
								}
							} else{
								hql.append(" 					and ((pak.pfpAdGroup.pfpAdAction.adActionStartDate between :startDate and :endDate)");
								hql.append(" 					or (pak.pfpAdGroup.pfpAdAction.adActionEndDate between :startDate and :endDate)");
								hql.append(" 					or (pak.pfpAdGroup.pfpAdAction.adActionStartDate < :startDate and pak.pfpAdGroup.pfpAdAction.adActionEndDate > :endDate)) ");
							}
						}
						hql.append(" group by pak.adKeywordSeq ");
						hql.append(" order by pak.adKeywordCreateTime desc ");

						log.info(hql);	
						//log.info(" startDate = "+startDate);
						//log.info(" endDate = "+endDate);
						//log.info(" adType = "+adType);
						//log.info(" adRankType = "+EnumAdType.AD_SEARCH.getType());
						//log.info(" customerInfoId = "+customerInfoId);
						//log.info(" status = "+EnumStatus.Close.getStatusId());
						//log.info(" adGroupSeq = "+adGroupSeq);
						//log.info(" keyword = "+keyword);
						
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

						if(StringUtils.isNotBlank(adKeywordPvclkDevice)) {
							q.setString("adKeywordPvclkDevice", adKeywordPvclkDevice);
						}
	
						// status 不是空的才使用這個條件
						if(StringUtils.isNotEmpty(adStatus)) {
							q.setString("adStatus", adStatus);
						}
						
						
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
	public List<PfpAdKeyword> validAdKeyword(String adGroupSeq) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAdKeyword ");
		hql.append(" where pfpAdGroup.adGroupSeq = ? ");
		hql.append(" and adKeywordStatus != ? ");
		
		return (List<PfpAdKeyword>) super.getHibernateTemplate().find(hql.toString(),adGroupSeq,EnumAdStatus.Close.getStatusId());
	}
}
