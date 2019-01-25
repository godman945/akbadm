package com.pchome.akbadm.db.dao.order;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpOrder;

public interface IPfpOrderDAO extends IBaseDAO<PfpOrder,String>{

	public List<PfpOrder> findPfpOrder(String orderId, String account, Date sDate, Date eDate, String status);
			
	public List<PfpOrder> findActivateOrder(String customerInfoId, String startDate, String endDate) throws Exception;
	
	public List<PfpOrder> findPfpOrderByDate(String startDate, String endDate) throws Exception;
	
	public List<PfpOrder> successOrder(String customerInfoId, Date startDate, Date endDate);
	
	public List<PfpOrder> successOrder(String customerInfoId, Date date, String OrderBy);
	
	public List<PfpOrder> findSuccessOrder(String orderId);
	
	public List<PfpOrder> findRefundOrder(String customerInfoId, Date date);
	
	public List<PfpOrder> findPfpOrder(String pfpCustomerInfoId);
	
	public Integer deletePfpOrder(String orderId);
	
	public List<PfpOrder> findOrder(String orderId) throws Exception;
}
