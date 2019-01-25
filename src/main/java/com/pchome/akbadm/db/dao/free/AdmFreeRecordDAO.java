package com.pchome.akbadm.db.dao.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;

public class AdmFreeRecordDAO extends BaseDAO<AdmFreeRecord, String> implements IAdmFreeRecordDAO{

	@SuppressWarnings("unchecked")
	public  List<AdmFreeRecord> findRecords(String customerInfoId, String freeActionId){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from AdmFreeRecord  ");
		hql.append(" where customerInfoId = ? ");
		hql.append(" and admFreeAction.actionId = ? ");
		
		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, freeActionId);
	}
	
	@SuppressWarnings("unchecked")
	public List<AdmFreeRecord> findFreeActionRecord(String customerInfoId, Date date) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from AdmFreeRecord  ");
		hql.append(" where customerInfoId = ? ");
		hql.append(" and recordDate = ? ");
		
		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, date);
	}
	
	public Integer deleteRecordAfterDate(Date date) {
		
		StringBuffer hql = new StringBuffer();
		
        hql.append(" delete from AdmFreeRecord ");
        hql.append(" where recordDate >= ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), date);
	}
	
	public Integer deleteRecordAfterDate(Date date, String customerInfoId, String actionId) {
		
		StringBuffer hql = new StringBuffer();
		
        hql.append(" delete from AdmFreeRecord ");
        hql.append(" where recordDate >= ? ");
        hql.append(" and customerInfoId = ? ");
        hql.append(" and admFreeAction.actionId = ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), date, customerInfoId, actionId);
	}
}
