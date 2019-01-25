package com.pchome.akbadm.db.service.order;

import com.pchome.akbadm.db.pojo.PfpOrderDetail;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpOrderDetailService extends IBaseService<PfpOrderDetail, String>{

	public Integer deletePfpOrderDetail(String orderId);
}
