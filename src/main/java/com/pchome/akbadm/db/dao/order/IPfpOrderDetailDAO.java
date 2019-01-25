package com.pchome.akbadm.db.dao.order;


import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpOrderDetail;


public interface IPfpOrderDetailDAO extends IBaseDAO<PfpOrderDetail,String>{
	
	public Integer deletePfpOrderDetail(String orderId);
}
