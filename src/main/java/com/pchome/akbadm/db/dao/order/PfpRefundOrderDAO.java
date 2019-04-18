package com.pchome.akbadm.db.dao.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfpRefundOrderDAO extends BaseDAO<PfpRefundOrder,String> implements IPfpRefundOrderDAO {

	@Override
    @SuppressWarnings("unchecked")
	public List<PfpRefundOrder> findTransRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from PfpRefundOrder ");
        hql.append("where refundDate = ? ");
		hql.append("and pfpCustomerInfo.customerInfoId = ? ");
		hql.append("and payType = ? ");
		hql.append("and refundStatus in (?,?) ");
		hql.append(" order by refundStatus desc ");

        list.add(refundDate);
		list.add(pfpCustomerInfoId);
		list.add(EnumPfdAccountPayType.LATER.getPayType());
		list.add(EnumPfpRefundOrderStatus.SUCCESS.getStatus());
		list.add(EnumPfpRefundOrderStatus.NOT_REFUND.getStatus());

		return (List<PfpRefundOrder>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@Override
	public float findRefundPrice(String customerInfoId, Date startDate, Date endDate, String payType, String refundStatus){
		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(refundPrice) ");
		hql.append(" from PfpRefundOrder ");
    	hql.append(" where pfpCustomerInfo.customerInfoId = :customerInfoId ");
        hql.append(" and payType = :payType");
    	hql.append(" and refundDate >= :startDate");
    	hql.append(" and refundDate <= :endDate");
    	hql.append(" and refundStatus = :refundStatus ");

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("customerInfoId", customerInfoId)
        .setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate)
    	.setString("refundStatus", refundStatus);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;
	}
	
	@Override
	public float findRefundPrice(List<String> pfpIdList, Date startDate, Date endDate, String payType, String refundStatus){
		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(refundPrice) ");
		hql.append(" from PfpRefundOrder ");
    	hql.append(" where pfpCustomerInfo.customerInfoId in (:pfpIdList) ");
        hql.append(" and payType = :payType");
    	hql.append(" and refundDate >= :startDate");
    	hql.append(" and refundDate <= :endDate");
    	hql.append(" and refundStatus = :refundStatus ");

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setParameterList("pfpIdList", pfpIdList)
        .setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate)
    	.setString("refundStatus", refundStatus);
    	
    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}
	
	@Override
	public float findTotalRefundPrice(Date startDate, Date endDate, String payType, String refundStatus){
		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(refundPrice) ");
		hql.append(" from PfpRefundOrder ");
    	hql.append(" where 1=1 ");
        hql.append(" and payType = :payType");
    	hql.append(" and refundDate >= :startDate");
    	hql.append(" and refundDate <= :endDate");
    	hql.append(" and refundStatus = :refundStatus ");

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("payType", payType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate)
    	.setString("refundStatus", refundStatus);

    	 Double result = (Double) query.uniqueResult();

         return result != null ? result.floatValue() : 0f;
	}
	
	@Override
	public List<PfpRefundOrder> findAdvanceRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from PfpRefundOrder ");
        hql.append("where refundDate = ? ");
        if (StringUtils.isNotBlank(pfpCustomerInfoId)){
        	hql.append("and pfpCustomerInfo.customerInfoId = ? ");
        }
		hql.append("and payType = ? ");
		hql.append("and refundStatus = ? ");
		hql.append(" order by refundStatus desc ");

		
        list.add(refundDate);
        if (StringUtils.isNotBlank(pfpCustomerInfoId)){
        	list.add(pfpCustomerInfoId);
        }
		list.add(EnumPfdAccountPayType.ADVANCE.getPayType());
		list.add(EnumPfpRefundOrderStatus.SUCCESS.getStatus());

		return (List<PfpRefundOrder>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@Override
	public List<PfpRefundOrder> findPfpRefundOrder(String pfpCustomerInfoId, String payType, String refundStatus) throws Exception {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from PfpRefundOrder ");
        hql.append(" where 1 = 1 ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and payType = ? ");
		hql.append(" and refundStatus = ? ");

		
        list.add(pfpCustomerInfoId);
		list.add(payType);
		list.add(refundStatus);

		return (List<PfpRefundOrder>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	public void saveOrUpdateWithCommit(PfpRefundOrder pfpRefundOrder) throws Exception{
		this.getHibernateTemplate().getSessionFactory().getCurrentSession().saveOrUpdate(pfpRefundOrder);
	}
	
	public List<Object> findPfpRefundPrice(String refundDate) throws Exception {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select a.customer_info_id, a.order_id, a.refund_price, b.refund_price_tax ");
		hql.append(" from pfp_refund_order a ");
		hql.append(" join pfp_refund_order_release b ");
		hql.append(" on a.refund_order_id = b.refund_order_id ");
		hql.append(" where 1=1 ");
		hql.append(" and a.pay_type = '" +EnumPfdAccountPayType.ADVANCE.getPayType()+ "' ");
		hql.append(" and a.refund_status = '" +EnumPfpRefundOrderStatus.SUCCESS.getStatus()+ "' ");
		hql.append(" and a.refund_date = '" +refundDate+ "' ");
		hql.append(" order by refund_status desc ");
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);
		
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hqlStr);
		
		return query.list();
	}
}
