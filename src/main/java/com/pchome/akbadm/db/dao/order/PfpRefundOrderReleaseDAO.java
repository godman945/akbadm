package com.pchome.akbadm.db.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpRefundOrderRelease;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfpRefundOrderReleaseDAO extends BaseDAO<PfpRefundOrderRelease, String> implements IPfpRefundOrderReleaseDAO {
	
	
	public List<Object> findPfpRefundOrderRelease(Map<String, String> conditionsMap) throws Exception {

		StringBuffer hql = new StringBuffer();
		
		hql.append(" select a.customer_info_id, a.billing_id, a.notify_date, ");
		hql.append(" a.order_price, a.tax, a.status, b.order_remain, b.tax_remain, ");
		hql.append(" c.refund_status, d.apply_time, d.status, e.customer_info_title, d.seq ,d.reject_reason ,d.refund_price_tax ");
		hql.append(" from pfp_order a ");
		hql.append(" join adm_recognize_record b ");
		hql.append(" on a.order_id = b.recognize_order_id ");
		hql.append(" join pfp_refund_order c ");
		hql.append(" on a.order_id = c.order_id ");
		hql.append(" join pfp_refund_order_release d ");
		hql.append(" on c.refund_order_id = d.refund_order_id ");
		hql.append(" join pfp_customer_info e ");
		hql.append(" on a.customer_info_id = e.customer_info_id ");
		hql.append(" where 1=1 ");
		hql.append(" and d.pay_type= '"+EnumPfdAccountPayType.ADVANCE.getPayType()+"' ");
		
		
		if (StringUtils.isNotBlank(conditionsMap.get("billingOrderId"))) {
			hql.append(" and a.billing_id = :billingOrderId ");
		}
		if (StringUtils.isNotBlank(conditionsMap.get("pfpCustomerInfoId"))) {
			hql.append(" and a.customer_info_id = :pfpCustomerInfoId ");
		}
		if (StringUtils.isNotBlank(conditionsMap.get("startDate"))) {
			hql.append(" and d.apply_time >= :startDate ");
		}
		if (StringUtils.isNotBlank(conditionsMap.get("endDate"))) {
			hql.append(" and d.apply_time <= :endDate ");
		}
		if (StringUtils.isNotBlank(conditionsMap.get("refundStatus"))) {
			hql.append(" and d.status = :refundStatus ");
		}
		hql.append(" order by apply_time ");
		//是否有分頁
		if (StringUtils.equals("Y", conditionsMap.get("havePage"))) {
			hql.append(conditionsMap.get("limit"));
		}
		
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);
		
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hqlStr);
		
		//塞where條件
		if (StringUtils.isNotBlank(conditionsMap.get("billingOrderId"))) {
			query.setString("billingOrderId", conditionsMap.get("billingOrderId"));
		}
		if (StringUtils.isNotBlank(conditionsMap.get("pfpCustomerInfoId"))) {
			query.setString("pfpCustomerInfoId", conditionsMap.get("pfpCustomerInfoId"));
		}

		if (StringUtils.isNotBlank(conditionsMap.get("startDate"))) {
			query.setString("startDate", conditionsMap.get("startDate") +" 00:00:00");
		}
		if (StringUtils.isNotBlank(conditionsMap.get("endDate"))) {
			query.setString("endDate", conditionsMap.get("endDate") +" 23:59:59");
		}
		
		if (StringUtils.isNotBlank(conditionsMap.get("refundStatus"))) {
			query.setString("refundStatus", conditionsMap.get("refundStatus"));
		}
		
		return query.list();
	}
	
	//更新審核後的狀態
	public void updatePfpRefundOrderReleaseStatus(String status, String seq, String verifyUserId, String rejectReason) throws Exception {
		log.info(">>> status = " + status);
		log.info(">>> seq = " + seq);
		log.info(">>> verifyUserId = " + verifyUserId);
		log.info(">>> rejectReason = " + rejectReason);
		
		String hql = " update PfpRefundOrderRelease set status=:status, checkUser=:verifyUserId, rejectReason=:rejectReason, checkTime=CURRENT_TIMESTAMP(), updateDate=CURRENT_TIMESTAMP() where seq = :seq) ";
		log.info(">>> hqlStr = " + hql);
		
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        Query query = session.createQuery(hql);
        query.setString("status", status);
        query.setString("verifyUserId", verifyUserId);
        query.setString("rejectReason", rejectReason);
        query.setString("seq", seq);
        query.executeUpdate();
        session.flush();
	}
	
}
