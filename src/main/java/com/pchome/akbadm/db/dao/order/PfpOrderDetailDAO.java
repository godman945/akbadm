package com.pchome.akbadm.db.dao.order;


import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpOrderDetail;

public class PfpOrderDetailDAO extends BaseDAO<PfpOrderDetail,String> implements IPfpOrderDetailDAO{

	public Integer deletePfpOrderDetail(String orderId) {
		
		StringBuffer hql = new StringBuffer();

	    hql.append("delete from PfpOrderDetail ");
	    hql.append("where id.orderId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), orderId);
	}
	
}
