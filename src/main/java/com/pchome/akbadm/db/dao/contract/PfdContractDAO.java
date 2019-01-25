package com.pchome.akbadm.db.dao.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

public class PfdContractDAO extends BaseDAO <PfdContract, String> implements IPfdContractDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfdContract> findPfdContract() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfdContract ");
		
		return super.getHibernateTemplate().find(hql.toString());
	}

//	@SuppressWarnings("unchecked")
//	public List<PfdContract> findValidPfdCustomerInfo() {
//		
//		StringBuffer hql = new StringBuffer();
//		List<Object> list = new ArrayList<Object>();
//		
//		hql.append(" from PfdContract ");
//		hql.append(" where  ");
//		hql.append(" (pfdCustomerInfo.status = ? or pfdCustomerInfo.status = ? or pfdCustomerInfo.status = ?) ");
//		hql.append(" and pfdCustomerInfo.activateDate != null ");
//		hql.append(" and status = ? ");
//		
//		list.add(EnumPfdAccountStatus.START.getStatus());
//		list.add(EnumPfdAccountStatus.STOP.getStatus());
//		list.add(EnumPfdAccountStatus.CLOSE.getStatus());
//		list.add(EnumContractStatus.WAIT.getStatusId());
//
//		
//		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
//	}
	
	@SuppressWarnings("unchecked")
	public  List<PfdContract> findPfdContract(String pfdContractId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdContract ");
		hql.append(" where pfdContractId = ? ");
		
		list.add(pfdContractId);		
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfdContract> findPfdContract(Map<String, String> conditionsMap,
			int pageNo, int pageSize) throws Exception {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfdContract where 1=1");
		if (conditionsMap.containsKey("pfdContractId")) {
			hql.append(" and pfdContractId = :pfdContractId");
		}
		if (conditionsMap.containsKey("pfdCustomerInfoId")) {
			hql.append(" and pfdCustomerInfo.customerInfoId = :pfdCustomerInfoId");
		}
		if (conditionsMap.containsKey("status")) {
			hql.append(" and status = :status");
		}
		hql.append(" order by contractDate desc");

		String strHQL = hql.toString();
		log.info(">>> strHQL = " + strHQL);

		Session session = getSession();
		Query q;
		if (pageNo==-1) {
			q = session.createQuery(strHQL);
		} else {
			q = session.createQuery(strHQL)
			.setFirstResult((pageNo-1)*pageSize)
			.setMaxResults(pageSize);
		}

		q.setProperties(conditionsMap);
		
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdContract> findValidPfdContract() {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdContract ");
		hql.append(" where  ");
		hql.append(" (pfdCustomerInfo.status = ? or pfdCustomerInfo.status = ? or pfdCustomerInfo.status = ?) ");
		hql.append(" and pfdCustomerInfo.activateDate != null ");
		hql.append(" and status = ? ");
		
		list.add(EnumPfdAccountStatus.START.getStatus());
		list.add(EnumPfdAccountStatus.STOP.getStatus());
		list.add(EnumPfdAccountStatus.CLOSE.getStatus());
		list.add(EnumContractStatus.USE.getStatusId());

		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@SuppressWarnings("unchecked")
	public List<PfdContract> findValidPfdContract(Date recordDate) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdContract ");
		hql.append(" where 1 = 1 ");
		hql.append(" and startDate <= ? ");
		hql.append(" and (status = ? or status = ?  or status = ?) ");
		
		list.add(recordDate);
		list.add(EnumContractStatus.USE.getStatusId());
		list.add(EnumContractStatus.OVERTIME.getStatusId());
		list.add(EnumContractStatus.CLOSE.getStatusId());
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdContract> checkPfdContractOverlap(String pfdCustomerInfo, Date newStartDate, Date newEndDate)throws Exception{
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdContract ");
		hql.append(" where 1=1 ");
		hql.append(" and pfdCustomerInfo.customerInfoId  = ? ");
		hql.append(" and (( ? >= startDate and  ? <= endDate and (status = ? or status = ? or status = ?)) or ");
		hql.append(" ( ? >= startDate and  ? <= closeDate and status = ?) or ");
		hql.append(" ( ? >= startDate and  ? <= endDate and (status = ? or status = ? or status = ?)) or ");
		hql.append(" ( ? >= startDate and  ? <= closeDate and status = ?)) ");

		list.add(pfdCustomerInfo);
		list.add(newStartDate);
		list.add(newStartDate);
		list.add(EnumContractStatus.USE.getStatusId());
		list.add(EnumContractStatus.WAIT.getStatusId());
		list.add(EnumContractStatus.OVERTIME.getStatusId());		
		list.add(newStartDate);
		list.add(newStartDate);
		list.add(EnumContractStatus.CLOSE.getStatusId());		
		
		list.add(newEndDate);
		list.add(newEndDate);
		list.add(EnumContractStatus.USE.getStatusId());
		list.add(EnumContractStatus.WAIT.getStatusId());
		list.add(EnumContractStatus.OVERTIME.getStatusId());
		list.add(newEndDate);
		list.add(newEndDate);
		list.add(EnumContractStatus.CLOSE.getStatusId());
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
