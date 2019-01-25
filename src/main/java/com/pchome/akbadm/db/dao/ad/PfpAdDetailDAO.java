package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;

public class PfpAdDetailDAO extends BaseDAO<PfpAdDetail, String> implements IPfpAdDetailDAO {
	
	@SuppressWarnings("unchecked")
	public PfpAdDetail findAdContent(String adSeq, String defineAdSeq) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append("from PfpAdDetail ");
		hql.append("where pfpAd.adSeq = '"+adSeq+"' and defineAdSeq = '"+defineAdSeq+"' ");
		
		List<PfpAdDetail> list = super.getHibernateTemplate().find(hql.toString());
		
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getAdPriceList(String adPoolSeq, float sysprice, String today) throws Exception{
		
		int statusId = EnumAdStatus.Open.getStatusId();
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select pfpAd.adChannelPrice ");
		hql.append(" from PfpAdDetail ");
		hql.append(" where pfpAd.adStatus = "+statusId+" and pfpAd.pfpAdGroup.adGroupStatus = "+statusId+" ");
		hql.append(" and pfpAd.pfpAdGroup.pfpAdAction.adActionStatus =  "+statusId+" ");
		hql.append(" and pfpAd.pfpAdGroup.pfpAdAction.adActionEndDate >= '"+today+"' ");
		hql.append(" and pfpAd.pfpAdGroup.pfpAdAction.pfpCustomerInfo.status = '"+EnumAccountStatus.START.getStatus()+"' ");
		hql.append(" and adPoolSeq = '"+adPoolSeq+"' ");
		hql.append(" and pfpAd.adChannelPrice < "+sysprice+" ");
		hql.append(" group by pfpAd.adChannelPrice ");
		
		return super.getHibernateTemplate().find(hql.toString());
	}

	@SuppressWarnings("unchecked")
	public List<PfpAdDetail> getPfpAdDetails(String adDetailSeq, String adSeq, String adPoolSeq, String defineAdSeq) throws Exception{
		StringBuffer sql = new StringBuffer("from PfpAdDetail where 1=1");
		if (StringUtils.isNotEmpty(adDetailSeq)) {
			sql.append(" and adDetailSeq = '" + adDetailSeq.trim() + "'");
		}

		if (StringUtils.isNotEmpty(adSeq)) {
			sql.append(" and pfpAd.adSeq = '" + adSeq.trim() + "'");
		}

		if (StringUtils.isNotEmpty(adPoolSeq)) {
			sql.append(" and adPoolSeq = '" + adPoolSeq.trim() + "'");
		}

		if (StringUtils.isNotEmpty(defineAdSeq)) {
			sql.append(" and defineAdSeq = '" + defineAdSeq.trim() + "'");
		}
		
		return super.getHibernateTemplate().find(sql.toString());
	}
}