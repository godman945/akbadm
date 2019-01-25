package com.pchome.akbadm.db.service.order;


import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.OrderReportVO;

public interface IPfpOrderService extends IBaseService<PfpOrder,String>{
	
	public List<PfpOrder> findPfpOrder(String orderId, String account, String sDate, String eDate, String status) throws Exception;
	
	public List<OrderReportVO> findPfpOrderByDate(String startDate, String endDate) throws Exception;
	
	public List<PfpOrder> successOrder(String customerInfoId, Date date);
	
	public List<PfpOrder> successOrder(String customerInfoId, Date date, String OrderBy);
	
	public PfpOrder findSuccessOrder(String orderId);
	
	public List<PfpOrder> findRefundOrder(String customerInfoId, Date date);
	
	public List<PfpOrder> findPfpOrder(String pfpCustomerInfoId);
	
	public Integer deletePfpOrder(String orderId);
	
	public PfpOrder findOrderInfo(String orderId) throws Exception;
}
