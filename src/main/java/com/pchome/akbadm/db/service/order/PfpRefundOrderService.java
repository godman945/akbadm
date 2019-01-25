package com.pchome.akbadm.db.service.order;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.order.IPfpRefundOrderDAO;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.service.BaseService;

public class PfpRefundOrderService extends BaseService<PfpRefundOrder,String> implements IPfpRefundOrderService {

	@Override
	public List<PfpRefundOrder> findTransRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception {
		return ((IPfpRefundOrderDAO) dao).findTransRefundOrder(pfpCustomerInfoId, refundDate);
	}

	@Override
	public float findRefundPrice(String customerInfoId, Date startDate, Date endDate, String payType, String refundStatus) {
		return ((IPfpRefundOrderDAO) dao).findRefundPrice(customerInfoId, startDate, endDate, payType, refundStatus);
	}
	
	@Override
	public float findTotalRefundPrice(Date startDate, Date endDate, String payType, String refundStatus) {
		return ((IPfpRefundOrderDAO) dao).findTotalRefundPrice(startDate, endDate, payType, refundStatus);
	}
	
	@Override
	public float findRefundPrice(List<String> pfpIdList, Date startDate, Date endDate, String payType, String refundStatus) {
		return ((IPfpRefundOrderDAO) dao).findRefundPrice(pfpIdList, startDate, endDate, payType, refundStatus);
	}
	
	@Override
	public List<PfpRefundOrder> findAdvanceRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception {
		return ((IPfpRefundOrderDAO) dao).findAdvanceRefundOrder(pfpCustomerInfoId, refundDate);
	}
	
	@Override
	public List<PfpRefundOrder> findPfpRefundOrder(String pfpCustomerInfoId, String payType, String refundStatus) throws Exception {
		return ((IPfpRefundOrderDAO) dao).findPfpRefundOrder(pfpCustomerInfoId, payType, refundStatus);
	}
	
	@Override
	public void saveOrUpdateWithCommit(PfpRefundOrder pfpRefundOrder) throws Exception{
		((IPfpRefundOrderDAO)dao).saveOrUpdateWithCommit(pfpRefundOrder);
	}
	
	@Override
	public List<Object> findPfpRefundPrice(String refundDate) throws Exception{
		return ((IPfpRefundOrderDAO) dao).findPfpRefundPrice(refundDate);
	}
}
