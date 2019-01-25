package com.pchome.akbadm.db.service.order;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpRefundOrderService extends IBaseService<PfpRefundOrder,String> {
	public List<PfpRefundOrder> findTransRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception;
	
	public float findRefundPrice(String customerInfoId, Date startDate, Date endDate, String payType, String refundStatus);
	
	public float findRefundPrice(List<String> pfpIdList, Date startDate, Date endDate, String payType, String refundStatus);
	
	public float findTotalRefundPrice(Date startDate, Date endDate, String payType, String refundStatus);
	
	public List<PfpRefundOrder> findAdvanceRefundOrder(String pfpCustomerInfoId, Date refundDate) throws Exception;
	
	public List<PfpRefundOrder> findPfpRefundOrder(String pfpCustomerInfoId, String payType, String refundStatus) throws Exception;
	
	public void saveOrUpdateWithCommit(PfpRefundOrder pfpRefundOrder) throws Exception;
	
	public List<Object> findPfpRefundPrice(String refundDate) throws Exception;
}
