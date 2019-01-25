package com.pchome.akbadm.db.dao.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.enumerate.feedback.EnumFeedbackStatus;

public class AdmFeedbackRecordDAO extends BaseDAO<AdmFeedbackRecord, Integer> implements IAdmFeedbackRecordDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFeedbackRecord> findValidFeedbackRecord(String accountName, Date sDate, Date eDate) {

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmFeedbackRecord ");
		hql.append(" where ");

		if(StringUtils.isNotBlank(accountName)){
			hql.append(" (pfpCustomerInfo.customerInfoTitle like ? ");
			hql.append(" or pfpCustomerInfo.customerInfoId like ? ) ");
			list.add("%"+accountName+"%");
		}else{
			hql.append(" (pfpCustomerInfo.customerInfoTitle != ? ");
			hql.append(" or pfpCustomerInfo.customerInfoId != ? ) ");
			list.add(accountName);
		}

		hql.append(" and giftDate >= ? ");
		hql.append(" and giftDate <= ? ");
		hql.append(" and status != ? ");
		hql.append(" order by id desc ");

		list.add(accountName);
		list.add(sDate);
		list.add(eDate);
		list.add(EnumFeedbackStatus.DELECT.getStatus());


		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFeedbackRecord> findFeedbackRecord(String recordId) {

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmFeedbackRecord ");
		hql.append(" where feedbackRecordId = ? ");

		return super.getHibernateTemplate().find(hql.toString(), recordId);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFeedbackRecord> findFeedbackRecord(String customerInfoId, Date date) {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmFeedbackRecord ");
		hql.append(" where status = ? ");
		hql.append(" and giftDate = ? ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		//hql.append(" and calculateCategory = ? ");

		list.add(EnumFeedbackStatus.START.getStatus());
		list.add(date);
        list.add(customerInfoId);
		//list.add(EnumSelCalculateCategory.QUARTZS.getCategory());

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmFeedbackRecord> findFeedbackRecord(Map<String, String> conditionMap) throws Exception {

		List<Object> paramList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();
		hql.append(" from AdmFeedbackRecord where 1=1");

		if (conditionMap.containsKey("inviledDate")) {
			hql.append(" and inviledDate < '" + conditionMap.get("inviledDate") + "' ");
			//paramList.add(conditionMap.get("inviledDate"));
		}
		if (conditionMap.containsKey("retrievedFlag")) {
			hql.append(" and retrievedFlag = ? ");
			paramList.add(conditionMap.get("retrievedFlag"));
		}
		if (conditionMap.containsKey("orderBy")) {
			hql.append(" order by " + conditionMap.get("orderBy"));
		}
		if (conditionMap.containsKey("desc")) {
			hql.append(" desc");
		}

		return super.getHibernateTemplate().find(hql.toString(), paramList.toArray());
	}
}
