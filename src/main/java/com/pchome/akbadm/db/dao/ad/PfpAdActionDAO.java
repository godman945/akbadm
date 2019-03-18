package com.pchome.akbadm.db.dao.ad;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.ad.EnumAdStatus;

public class PfpAdActionDAO extends BaseDAO<PfpAdAction, String> implements IPfpAdActionDAO {
    @Override
    @SuppressWarnings("unchecked")
    public PfpAdAction getAdAction(String adActionSeq){

    	StringBuffer hql = new StringBuffer();
        hql.append("from PfpAdAction ");
        hql.append("where adActionSeq = ?");
        List<PfpAdAction> list = (List<PfpAdAction>) super.getHibernateTemplate().find(hql.toString(), adActionSeq);

        if(list != null && list.size() > 0){
        	return list.get(0);
        }else{
        	return null;
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdAction> getValidAdAction(String customerInfoId) {

    	StringBuffer hql = new StringBuffer();
        hql.append(" from PfpAdAction ");
        hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
        hql.append(" and adActionStatus != ? ");

        Object[] ob = new Object[]{customerInfoId, EnumAdStatus.Close.getStatusId()};

        return (List<PfpAdAction>) super.getHibernateTemplate().find(hql.toString(), ob);
    }

    //改善廣告報表效能
    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getAdActionReport(final Map<String,String> adActionConditionMap){
    	final List<Object> getAdActionReportObjList =  getHibernateTemplate().execute(
    	new HibernateCallback<List<Object>>() {
	    @Override
        public List<Object> doInHibernate(Session session) throws HibernateException {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT ");
		hql.append("a.adActionCreatTime, ");
		hql.append("a.pfpCustomerInfo.memberId, ");
		hql.append("a.pfpCustomerInfo.customerInfoTitle, ");
		hql.append("a.pfpCustomerInfo.status, ");
		hql.append("b.adPvclkDevice, ");
		hql.append("a.pfpCustomerInfo.remain, ");
		hql.append("a.adActionName, ");
		hql.append("a.adActionStatus, ");
		hql.append("a.adActionStartDate, ");
		hql.append("a.adActionEndDate, ");
		hql.append("a.adActionMax, ");
		hql.append("a.adActionControlPrice, ");
		hql.append("sum(b.adPv), ");
		hql.append("sum(b.adClk), ");
		hql.append("sum(b.adInvalidClk) , ");
		hql.append("sum(b.adInvalidClkPrice),  ");
		hql.append("b.adPvclkDate,  ");
		hql.append("sum(b.adClkPrice),  ");
		hql.append("a.adType, ");
		hql.append("a.adActionSeq ");
		hql.append("FROM ");
		hql.append("PfpAdAction a,PfpAdActionReport b ");
		hql.append("where 1=1");
		hql.append("and a.adActionSeq = b.adActionSeq  ");
		//判斷加上查詢條件
		String adStartDate =null;
		String adEndDate =null;
		boolean adPvclkDeviceFlag = false;
		if(adActionConditionMap.get("startDate")!= null && adActionConditionMap.get("startDate").trim().length()>0){
			adStartDate = adActionConditionMap.get("startDate").trim();
		}
		if(adActionConditionMap.get("endDate")!= null && adActionConditionMap.get("endDate").trim().length()>0){
			adEndDate =adActionConditionMap.get("endDate").trim();
		}
		if(adActionConditionMap.get("searchAdStatus")!= null && adActionConditionMap.get("searchAdStatus").trim().length()>0){
			hql.append(" and  a.adActionStatus ='"+adActionConditionMap.get("searchAdStatus").trim()+"'");
		}
		if(adActionConditionMap.get("adType")!= null && adActionConditionMap.get("adType").trim().length()>0){
			hql.append(" and a.adType ='"+adActionConditionMap.get("adType").trim()+"'");
		}
		if(adActionConditionMap.get("adActionName")!= null && adActionConditionMap.get("adActionName").trim().length()>0){
			hql.append(" and a.adActionName like '%"+adActionConditionMap.get("adActionName").trim()+"%'");
		}

		if(adActionConditionMap.get("userAccount")!= null && adActionConditionMap.get("userAccount").trim().length()>0){
			hql.append(" and a.pfpCustomerInfo.memberId like '%"+adActionConditionMap.get("userAccount").trim()+"%'");
		}


		if(adActionConditionMap.get("adPvclkDevice")!= null && adActionConditionMap.get("adPvclkDevice").trim().length()>0){
			if(!adActionConditionMap.get("adPvclkDevice").equals("ALL Device")){
				hql.append(" and b.adPvclkDevice = '"+adActionConditionMap.get("adPvclkDevice").trim()+"'");
				adPvclkDeviceFlag = true;
			}
		}
		hql.append("and a.adActionStartDate >='"+adActionConditionMap.get("startDate")+"'");
		hql.append("and a.adActionStartDate <='"+adActionConditionMap.get("endDate")+"'");
//		hql.append("and a.adActionStartDate, ");
//		hql.append("a.adActionStartDate, ");
//		hql.append("a.adActionEndDate, ");

//		if(!StringUtils.isEmpty(adActionConditionMap.get("adPvclkDate")) && adActionConditionMap.get("adPvclkDate").equals("adPvclkDate")){
//			hql.append(" and b.adPvclkDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}
//		if(!StringUtils.isEmpty(adActionConditionMap.get("adActionDate")) && adActionConditionMap.get("adActionDate").equals("adActionDate")){
//			hql.append(" and a.adActionStartDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}

		if(adPvclkDeviceFlag){
		    hql.append(" group by b.adPvclkDevice,b.adActionSeq");
		    if(adActionConditionMap.get("adPvclkDate") != null && adActionConditionMap.get("adPvclkDate").trim().length()>0){
			hql.append(" order by a.adActionName ");
		    }else{
			 hql.append(" order by a.adActionStartDate ");
		    }
		}else if(!adPvclkDeviceFlag){
		    hql.append(" group by b.adActionSeq");
		    hql.append(" order by a.adActionStartDate ");
		}
		int pageSize = Integer.valueOf(adActionConditionMap.get("pageSize"));
		int page = Integer.valueOf(adActionConditionMap.get("pageNo"));

		Query query = session.createQuery(hql.toString());
		if(page!=-1){
		    query.setFirstResult((page-1)*pageSize).setMaxResults(pageSize);
		}
		return query.list();
	    }
    }
	);
	return getAdActionReportObjList;
    }
    @Override
    public int getAdActionReportSize(Map<String,String> adActionConditionMap) throws Exception{
    	StringBuffer hql = new StringBuffer();
    	hql.append("SELECT count(*) ");
    	hql.append("FROM ");
    	hql.append("PfpAdAction a,PfpAdActionReport b ");
    	hql.append("where 1=1");
    	hql.append("and a.adActionSeq = b.adActionSeq  ");
		//判斷加上查詢條件
		String adStartDate =null;
		String adEndDate =null;
		boolean adPvclkDeviceFlag = false;
		if(adActionConditionMap.get("startDate")!= null && adActionConditionMap.get("startDate").trim().length()>0){
			adStartDate = adActionConditionMap.get("startDate").trim();
		}
		if(adActionConditionMap.get("endDate")!= null && adActionConditionMap.get("endDate").trim().length()>0){
			adEndDate =adActionConditionMap.get("endDate").trim();
		}
		if(adActionConditionMap.get("searchAdStatus")!= null && adActionConditionMap.get("searchAdStatus").trim().length()>0){
			hql.append(" and  a.adActionStatus ='"+adActionConditionMap.get("searchAdStatus").trim()+"'");
		}
		if(adActionConditionMap.get("adType")!= null && adActionConditionMap.get("adType").trim().length()>0){
			hql.append(" and a.adType ='"+adActionConditionMap.get("adType").trim()+"'");
		}
		if(adActionConditionMap.get("adActionName")!= null && adActionConditionMap.get("adActionName").trim().length()>0){
			hql.append(" and a.adActionName like '%"+adActionConditionMap.get("adActionName").trim()+"%'");
		}

		if(adActionConditionMap.get("userAccount")!= null && adActionConditionMap.get("userAccount").trim().length()>0){
			hql.append(" and a.pfpCustomerInfo.memberId like '%"+adActionConditionMap.get("userAccount").trim()+"%'");
		}


		if(adActionConditionMap.get("adPvclkDevice")!= null && adActionConditionMap.get("adPvclkDevice").trim().length()>0){
			if(!adActionConditionMap.get("adPvclkDevice").equals("ALL Device")){
				hql.append(" and b.adPvclkDevice = '"+adActionConditionMap.get("adPvclkDevice").trim()+"'");
				adPvclkDeviceFlag = true;
			}
		}
//		if(!StringUtils.isEmpty(adActionConditionMap.get("adPvclkDate")) && adActionConditionMap.get("adPvclkDate").equals("adPvclkDate")){
//			hql.append(" and b.adPvclkDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}
//		if(!StringUtils.isEmpty(adActionConditionMap.get("adActionDate")) && adActionConditionMap.get("adActionDate").equals("adActionDate")){
//			hql.append(" and a.adActionStartDate between '"+adStartDate+"' "+" and "+"'"+adEndDate+"'");
//		}
		hql.append("and a.adActionStartDate >='"+adActionConditionMap.get("startDate")+"'");
		hql.append("and a.adActionStartDate <='"+adActionConditionMap.get("endDate")+"'");
		if(adPvclkDeviceFlag){
		    hql.append(" group by b.adPvclkDevice,b.adActionSeq");
		    if(adActionConditionMap.get("adPvclkDate") != null && adActionConditionMap.get("adPvclkDate").trim().length()>0){
			hql.append(" order by a.adActionName ");
		    }else{
			 hql.append(" order by a.adActionStartDate ");
		    }
		}else if(!adPvclkDeviceFlag){
		    hql.append(" group by b.adActionSeq");
		    hql.append(" order by a.adActionStartDate ");
		}
		
		Query query = super.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		int totalSize = query.list().size() !=0 ? query.list().size() : 0;
    	return totalSize;
    }



	@Override
    @SuppressWarnings("unchecked")
	public List<Object> getAdActionPvclk(final String userAccount, final String adStatus, final int adType, final String adActionName, final String adPvclkDevice, final String dateType, final Date startDate, final Date endDate, final int page, final int pageSize) throws Exception{
		List<Object> result = getHibernateTemplate().execute(
                new HibernateCallback<List<Object> >() {
					@Override
                    public List<Object>  doInHibernate(Session session) throws HibernateException {
						StringBuffer hql = new StringBuffer();
						hql.append(" select pa.adActionSeq, ");
						hql.append("		pa.adActionName, ");
						hql.append("		pa.adType, ");
						hql.append("		pa.adActionStatus, ");
						hql.append("		pa.adActionMax, ");
						hql.append("		pa.adActionControlPrice, ");
						hql.append("		pa.adActionStartDate, ");
						hql.append("		pa.adActionEndDate, ");
						hql.append("		pa.adActionCreatTime, ");
						hql.append("		pa.pfpCustomerInfo.memberId, ");
						hql.append("		pa.pfpCustomerInfo.customerInfoTitle, ");
						hql.append("		pa.pfpCustomerInfo.remain, ");
						hql.append("		pa.pfpCustomerInfo.status ");
//						hql.append("		papc.adPvclkDevice, ");
//						hql.append("		COALESCE(pag.adGroupSeq,''), ");
//						hql.append("		COALESCE(sum(papc.adPv),0), ");
//						hql.append("		COALESCE(sum(papc.adClk),0), ");
//						hql.append("		COALESCE(sum(papc.adClkPrice),0), ");
//						hql.append("		COALESCE(sum(papc.adInvalidClk),0), ");
//						hql.append("		COALESCE(sum(papc.adInvalidClkPrice),0) ");
						hql.append(" from PfpAdAction as pa ");
//						hql.append(" 		left join pa.pfpAdGroups pag ");
//						hql.append(" 		left join pag.pfpAds paa  ");
//						hql.append(" 		left join paa.pfpAdPvclks papc ");
//
//						if(adType > 0){
//							hql.append(" 			with (papc.adType = :adType ");
//						}else{
//							hql.append(" 			with (papc.adType != :adType ");
//						}
//
//						//if(startDate != null && endDate != null) {
//						//	hql.append(" 					and papc.adPvclkDate >= :startDate ");
//						//	hql.append(" 					and papc.adPvclkDate <= :endDate ");
//						//}
//						hql.append(")");
//
//						hql.append(" where 1 = 1");
//						if(StringUtils.isNotEmpty(userAccount)) {
//							//hql.append(" and (pa.pfpCustomerInfo.customerInfoId = :userAccount or pa.pfpCustomerInfo.memberId = :userAccount)");
//							hql.append(" and pa.pfpCustomerInfo.memberId like :userAccount");
//						}
//						if(StringUtils.isNotEmpty(adStatus)) {
//							hql.append(" and pa.adActionStatus = :adStatus ");
//
//							if(adStatus.equals("5")) {
//								hql.append(" and pa.adActionStartDate <= :today ");
//								hql.append(" and pa.adActionEndDate >= :today ");
//							} else if(adStatus.equals("7")) {
//								hql.append(" and pa.adActionStartDate > :today ");
//							} else if(adStatus.equals("8")) {
//								hql.append(" and pa.adActionEndDate < :today ");
//							}
//						}
//						if(StringUtils.isNotEmpty(adActionName)) {
//							hql.append(" and (pa.adActionName like :adActionName ");
//							hql.append(" 	  or pa.adActionDesc like :adActionName)");
//						}
//
//						if(StringUtils.isNotBlank(adPvclkDevice)) {
//							hql.append(" and papc.adPvclkDevice = :adPvclkDevice ");
//						}
//
//						if(StringUtils.isNotBlank(dateType)) {
//							if(dateType.equals("adPvclkDate")) {
//								if(startDate == endDate) {
//									hql.append(" 					and (papc.adPvclkDate = :startDate ");
//								} else {
//									hql.append(" 					and (papc.adPvclkDate >= :startDate ");
//									hql.append(" 					and papc.adPvclkDate <= :endDate) ");
//								}
//							} else{
//								hql.append(" 					and ((pa.adActionStartDate between :startDate and :endDate)");
//								hql.append(" 					or (pa.adActionEndDate between :startDate and :endDate)");
//								hql.append(" 					or (pa.adActionStartDate < :startDate and pa.adActionEndDate > :endDate)) ");
//							}
//						}
//
//						hql.append(" group by pa.adActionSeq ");
//						hql.append(" order by pa.adActionCreatTime desc ");
//
//						log.info(">>> hql = " + hql);
						Query q = session.createQuery(hql.toString());
//						Query q = session.createQuery(hql.toString()).setInteger("adType", adType);
//						if(startDate != null && endDate != null) {
//							q.setDate("startDate", startDate);
//							q.setDate("endDate", endDate);
//						}
//						if(StringUtils.isNotEmpty(userAccount)) {
//							q.setString("userAccount", userAccount);
//						}
//
//						if(StringUtils.isNotEmpty(adActionName)) {
//							q.setString("adActionName", "%"+adActionName+"%");
//						}
//
//						// status 不是空的才使用這個條件
//						if(StringUtils.isNotEmpty(adStatus)) {
//							if(adStatus.equals("5") || adStatus.equals("7") || adStatus.equals("8")) {
//								q.setString("adStatus", Integer.toString(EnumAdStatus.Open.getStatusId()));
//								q.setDate("today", DateValueUtil.getInstance().getNowDateTime());
//							} else {
//								q.setString("adStatus", adStatus);
//							}
//						}
//
//						if(StringUtils.isNotBlank(adPvclkDevice)) {
//							q.setString("adPvclkDevice", adPvclkDevice);
//						}

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

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdAction> selectValidAdAction(Date date) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpAdAction ");
        hql.append("where adActionStatus = ?");
        hql.append("    and pfpCustomerInfo.status = ? ");
        hql.append("    and pfpCustomerInfo.remain > 0");

        Object[] values = new Object[]{EnumAdStatus.Open.getStatusId(), EnumAccountStatus.START.getStatus()};

        return (List<PfpAdAction>) super.getHibernateTemplate().find(hql.toString(), values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdAction> selectAdActionByStatus(int status) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpAdAction ");
        hql.append("where adActionStatus = :status ");
        hql.append("    and pfpCustomerInfo.recognize = 'Y' ");

        return super.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("status", EnumAdStatus.Open.getStatusId())
                .list();
    }

    @Override
    public float selectActionMaxSum() {
        StringBuffer hql = new StringBuffer();
        hql.append("select sum(adActionMax) ");
        hql.append("from PfpAdAction ");
        hql.append("where adActionStatus = :status ");
        hql.append("    and pfpCustomerInfo.recognize = 'Y' ");

        Double result = (Double) super.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setInteger("status", EnumAdStatus.Open.getStatusId())
                .uniqueResult();
        return result != null ? result.floatValue() : 0f;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdAction> getAdActionByConditions(Map<String, String> conditionMap) throws Exception {

    	StringBuffer hql = new StringBuffer();
        hql.append("from PfpAdAction ");
        hql.append(" where 1=1 ");
        if (conditionMap.containsKey("customerInfoId")) {
        	hql.append(" and pfpCustomerInfo.customerInfoId = '" + conditionMap.get("customerInfoId") + "'");
		}
        if (conditionMap.containsKey("adActionStatus")) {
        	hql.append(" and adActionStatus = '" + conditionMap.get("adActionStatus") + "'");
		}
        if (conditionMap.containsKey("adActionEndDate")) {
        	hql.append(" and adActionEndDate <= '" + conditionMap.get("adActionEndDate") + "'");
		}

        List<PfpAdAction> list = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString()).list();

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdAction> findBroadcastAdAction(String customerInfoId) {
    	Date today = new Date();
    	StringBuffer hql = new StringBuffer();

    	hql.append("from PfpAdAction ");
    	hql.append("where adActionStatus = ? ");
    	hql.append("and adActionEndDate >= ? ");
    	hql.append("and adActionStartDate <= ? ");
        hql.append("and pfpCustomerInfo.customerInfoId = ? ");

    	Object[] ob = new Object[]{EnumAdStatus.Open.getStatusId(), today, today, customerInfoId};

    	return (List<PfpAdAction>) super.getHibernateTemplate().find(hql.toString(), ob);
    }

	// 2014-04-24
	@Override
    @SuppressWarnings("unchecked")
	public HashMap<String, PfpAdAction> getPfpAdActionsBySeqList(List<String> adActionSeqList) throws Exception {
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfpAdAction where 1=1");
		if (adActionSeqList != null) {
			sql.append(" and adActionSeq in (:adActionSeq)");
			sqlParams.put("adActionSeq", adActionSeqList);
		}

		//List<PfpAd> list = super.getHibernateTemplate().find(sql.toString(), adActionSeqList);

		// 將條件資料設定給 Query，準備 query
		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			if(paramName.equals("adActionSeq")) {
				q.setParameterList("adActionSeq", adActionSeqList);
			}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfpAdAction> adActionMap = new HashMap<String, PfpAdAction>();
		List<PfpAdAction> pfpAdActions = q.list();
		System.out.println("pfpAdActions.size() = " + pfpAdActions.size());
		for(PfpAdAction pfpAdAction:pfpAdActions) {
			adActionMap.put(pfpAdAction.getAdActionSeq(), pfpAdAction);
		}

		return adActionMap;
	}

	// 2014-04-24
	@Override
    @SuppressWarnings("unchecked")
	public HashMap<String, PfpAdAction> getPfpAdActionsByActionDate(String startDate, String endDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		HashMap<String, Object> sqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from PfpAdAction where 1=1");
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" and adActionEndDate >= :startDate");
			sqlParams.put("startDate", sdf.parse(startDate));
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" and adActionEndDate <= :endDate");
			sqlParams.put("endDate", sdf.parse(endDate));
		}

		// 將條件資料設定給 Query，準備 query
		Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());
        for (String paramName:sqlParams.keySet()) {
			q.setParameter(paramName, sqlParams.get(paramName));
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		HashMap<String, PfpAdAction> adActionMap = new HashMap<String, PfpAdAction>();
		List<PfpAdAction> pfpAdActions = q.list();
		System.out.println("pfpAdActions.size() = " + pfpAdActions.size());
		for(PfpAdAction pfpAdAction:pfpAdActions) {
			adActionMap.put(pfpAdAction.getAdActionSeq(), pfpAdAction);
		}

		return adActionMap;
	}
}
