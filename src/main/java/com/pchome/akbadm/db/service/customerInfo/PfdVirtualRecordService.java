package com.pchome.akbadm.db.service.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.customerInfo.PfdVirtualRecordDAO;
import com.pchome.akbadm.db.pojo.PfdVirtualRecord;
import com.pchome.akbadm.db.service.BaseService;

public class PfdVirtualRecordService extends BaseService<PfdVirtualRecord, Integer> implements IPfdVirtualRecordService{

	public List<PfdVirtualRecord> findPfdVirtualRecord(String pfpCustomerInfoId, Date addDate) {
		return ((PfdVirtualRecordDAO)dao).findPfdVirtualRecord(pfpCustomerInfoId, addDate);
	}
}
