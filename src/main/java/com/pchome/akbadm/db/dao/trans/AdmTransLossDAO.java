package com.pchome.akbadm.db.dao.trans;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmTransLoss;

public class AdmTransLossDAO extends BaseDAO <AdmTransLoss, String> implements IAdmTransLossDAO {
	
	public List<Object[]> getTransLossSumByTransDate(Date sDate , Date eDate) throws Exception
	{
		StringBuffer hql = new StringBuffer();
		List<Object> pos = new ArrayList<Object>();
		
		hql.append("select ");
		hql.append("transDate, ");
		hql.append("sum(lossCost) ");
	    hql.append("from AdmTransLoss ");
	    hql.append("where transDate between ? and ? ");
	    hql.append("group by transDate");
	    pos.add(sDate);
	    pos.add(eDate);
	    
	    return super.getHibernateTemplate().find(hql.toString() , pos.toArray());
	}

	public Integer deleteRecordAfterDate(Date startDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from AdmTransLoss ");
		hql.append(" where transDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), startDate);
	}

	public float selectTransLossSumByTransDate(String transDate) {
	    StringBuffer hql = new StringBuffer();
	    hql.append("select sum(lossCost) ");
	    hql.append("from AdmTransLoss ");
	    hql.append("where transDate = :transDate ");

	    Double result = (Double) this.getSession()
	            .createQuery(hql.toString())
	            .setString("transDate", transDate)
	            .uniqueResult();
	    return result != null ? result.floatValue() : 0;
	}

	public float selectTransLossSumByTransDateAndCustInfoId(String transDate,
			String custInfoId) throws Exception {

		StringBuffer hql = new StringBuffer();
	    hql.append("select sum(lossCost) ");
	    hql.append("from AdmTransLoss ");
	    hql.append("where transDate = :transDate ");
	    hql.append(" and customerInfoId = :custInfoId");

	    Double result = (Double) this.getSession()
	            .createQuery(hql.toString())
	            .setString("transDate", transDate)
	            .setString("custInfoId", custInfoId)
	            .uniqueResult();

	    return result != null ? result.floatValue() : 0;
	}

	public Map<String, Double> selectTransLossSumByTransDateMap(String transDate) throws Exception {

		StringBuffer hql = new StringBuffer();
	    hql.append("select customerInfoId, sum(lossCost) ");
	    hql.append("from AdmTransLoss ");
	    hql.append("where transDate = '" + transDate + "'");
	    hql.append(" group by customerInfoId");

	    Query query = this.getSession().createQuery(hql.toString());

		List<Object> dataList = query.list();

		Map<String, Double> resultMap = new HashMap<String, Double>();

		for (int i=0; i<dataList.size(); i++) {

			Object[] objArray = (Object[]) dataList.get(i);

			String customerInfoId = (String) objArray[0];
			Double overPrice = (Double) objArray[1];

			resultMap.put(customerInfoId, overPrice);
		}

		return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findTransLosses(Date transDate) {
		
		StringBuffer hql = new StringBuffer();
	    hql.append(" select customerInfoId, sum(lossCost) ");
	    hql.append(" from AdmTransLoss ");
	    hql.append(" where transDate = ? ");
	    hql.append(" group by customerInfoId ");
	    
	    return super.getHibernateTemplate().find(hql.toString(), transDate);
	}
}
