package com.pchome.akbadm.db.service.order;

import com.pchome.akbadm.db.dao.order.IPfpOrderDetailDAO;
import com.pchome.akbadm.db.pojo.PfpOrderDetail;
import com.pchome.akbadm.db.service.BaseService;

public class PfpOrderDetailService extends BaseService <PfpOrderDetail, String> implements IPfpOrderDetailService {

	public Integer deletePfpOrderDetail(String orderId) {
		return ((IPfpOrderDetailDAO) dao).deletePfpOrderDetail(orderId);
	}
}
