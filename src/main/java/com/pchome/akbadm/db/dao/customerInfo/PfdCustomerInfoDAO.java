package com.pchome.akbadm.db.dao.customerInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerStatus;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

public class PfdCustomerInfoDAO extends BaseDAO <PfdCustomerInfo, String> implements IPfdCustomerInfoDAO{

	@SuppressWarnings("unchecked")
	public List<PfdCustomerInfo> findPfdValidCustomerInfo() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfdCustomerInfo ");
		hql.append(" where activateDate != null ");
		hql.append(" and (status = ? or status = ? or status = ?) ");
		
		List<Object> list = new ArrayList<Object>();
		
		list.add(EnumPfdAccountStatus.START.getStatus());
		list.add(EnumPfdAccountStatus.CLOSE.getStatus());
		list.add(EnumPfdAccountStatus.STOP.getStatus());
		
		return (List<PfdCustomerInfo>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfdCustomerInfo> findPfdCustomerInfo(String pfdCustomerInfoId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdCustomerInfo ");
		hql.append(" where customer_info_id = ? ");
		
		
		list.add(pfdCustomerInfoId);
		
		return (List<PfdCustomerInfo>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	// 2014-04-24 
	@SuppressWarnings("unchecked")
	public HashMap<String, PfdCustomerInfo> getPfdCustomerInfoBySeqList(List<String> customerInfoIdList) throws Exception {

		StringBuffer sql = new StringBuffer("from PfdCustomerInfo where 1=1");
		if (customerInfoIdList != null && customerInfoIdList.size()>0) {
			sql.append(" and customerInfoId in (:customerInfoId)");
		}

		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());

		if (customerInfoIdList != null && customerInfoIdList.size()>0) {
			q.setParameterList("customerInfoId", customerInfoIdList);
		}

        List<PfdCustomerInfo> pfdCustomerInfos = q.list();

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfdCustomerInfo> pfdCustomerInfoMap = new HashMap<String, PfdCustomerInfo>();
		for(PfdCustomerInfo pfdCustomerInfo:pfdCustomerInfos) {
			pfdCustomerInfoMap.put(pfdCustomerInfo.getCustomerInfoId(), pfdCustomerInfo);
		}
		
		return pfdCustomerInfoMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findManagerPfdAccount(final String memberId, final Date startDate, final Date endDate) {
		
		List<Object> result = getHibernateTemplate().execute(

		        new HibernateCallback<List<Object> >() {
		        	
		        	public List<Object>  doInHibernate(Session session) throws HibernateException {
		        		
		        		StringBuffer sql = new StringBuffer();
		        		Query q = null;
		        		
		        		// COALESCE(FIRST, '')
		        		
		        		sql.append(" select pfdc.customer_info_id, pfdc.company_name, pfdc.status, coalesce(sum(pfdr.ad_clk_price),0), ");	 	        			        		
		        		sql.append(" 		coalesce(pfdu.amount,0), pfdc.total_quota ");
		        		sql.append(" from pfd_customer_info as pfdc ");
		        		sql.append(" 		left join pfd_ad_action_report as pfdr ");
		        		sql.append("			on pfdr.pfd_customer_info_id = pfdc.customer_info_id ");
		        		sql.append(" 			and pfdr.ad_pvclk_date between :startDate and :endDate ");
		        		sql.append(" 		left join (  ");		        		
		        		sql.append(" 					select ref.pfd_customer_info_id, coalesce(count(ref.ref_id), 0) as amount ");
		        		sql.append(" 					from pfd_user_ad_account_ref as ref ");
		        		sql.append(" 							left join pfp_customer_info as pfpc ");
		        		sql.append(" 								on ref.pfp_customer_info_id = pfpc.customer_info_id ");
		        		sql.append(" 					group by ref.pfd_customer_info_id ");
		        		sql.append(" 					) as pfdu ");
		        		sql.append(" 			on pfdc.customer_info_id = pfdu.pfd_customer_info_id ");
		        		
		        		/* 2014.11.04 舊的語法
		        		sql.append(" select pfdc.customer_info_id, pfdc.company_name, pfdc.status, coalesce(sum(pfdr.ad_clk_price),0), ");	 	        			        		
		        		sql.append(" 		coalesce(pfdu.remain,0), coalesce(pfdu.later_remain,0), coalesce(pfdu.amount,0), pfdc.total_quota ");
		        		sql.append(" from pfd_customer_info as pfdc ");
		        		sql.append(" 		left join pfd_ad_action_report as pfdr ");
		        		sql.append("			on pfdr.pfd_customer_info_id = pfdc.customer_info_id ");
		        		sql.append(" 			and pfdr.ad_pvclk_date between :startDate and :endDate ");
		        		sql.append(" 		left join (  ");		        		
		        		sql.append(" 					select ref.pfd_customer_info_id, coalesce(sum(pfpc.remain), 0) as remain, ");
		        		sql.append(" 							coalesce(sum(pfpc.later_remain), 0) as later_remain, ");
		        		sql.append(" 							coalesce(count(ref.ref_id), 0) as amount ");
		        		sql.append(" 					from pfd_user_ad_account_ref as ref ");
		        		sql.append(" 							left join pfp_customer_info as pfpc ");
		        		sql.append(" 								on ref.pfp_customer_info_id = pfpc.customer_info_id ");
		        		sql.append(" 					group by ref.pfd_customer_info_id ");
		        		sql.append(" 					) pfdu ");
		        		sql.append(" 			on pfdc.customer_info_id = pfdu.pfd_customer_info_id ");
		        		sql.append("		left join adm_channel_account as admc ");
		        		sql.append("			on pfdc.customer_info_id = admc.account_id ");
		        		sql.append("		left join adm_manager_detail as admd ");
		        		sql.append("			on admc.member_id = admd.member_id ");   
		        		sql.append(" 			and admd.manager_channel = :channelCategory ");
		        		*/
		        		
		        		if(StringUtils.isNotBlank(memberId)){ 
		        			sql.append(" left join adm_channel_account as admc ");
			        		sql.append(" on pfdc.customer_info_id = admc.account_id ");
			        		sql.append(" left join adm_manager_detail as admd ");
			        		sql.append(" on admc.member_id = admd.member_id ");   
			        		sql.append(" and admd.manager_channel = :channelCategory ");
		        			sql.append(" where admc.member_id = :memberId ");
		        			sql.append(" and admd.manager_status = :status ");
		        			sql.append(" group by pfdc.customer_info_id ");
		        			
		        			q = session.createSQLQuery(sql.toString())			        					
		        					.setDate("startDate", startDate)
		        					.setDate("endDate", endDate)		        					
			        				.setString("channelCategory", EnumChannelCategory.PFD.getCategory())	
			        				
			        				
			        				.setString("memberId", memberId)
			        				.setString("status", EnumManagerStatus.START.getStatus());
		        		}
		        		else{
		        			sql.append(" left join ( select * from adm_channel_account where 1 = 1 ");
		        			sql.append(" and channel_category = "+EnumChannelCategory.PFD.getCategory() );
		        			sql.append(" group by account_id ");
		        			sql.append("  ) as admc ");
		        			sql.append(" on pfdc.customer_info_id = admc.account_id ");
			        		sql.append(" left join adm_manager_detail as admd ");
			        		sql.append(" on admc.member_id = admd.member_id ");   
			        		sql.append(" and admd.manager_channel = :channelCategory ");
		        			
		        			sql.append(" group by pfdc.customer_info_id ");
		        			
		        			q = session.createSQLQuery(sql.toString())		        					
		        					.setDate("startDate", startDate)
		        					.setDate("endDate", endDate)
		        					.setString("channelCategory", EnumChannelCategory.PFD.getCategory());
		        			
		        		}
		        		
		        		//log.info(" sql "+sql);
		        		//log.info(" startDate "+startDate);
		        		//log.info(" endDate "+endDate);
		        	            	
		            	return q.list();
		        	}
		        		
		        }
	        );
			
			return result;
	}
	
}
