package com.pchome.akbadm.db.service.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdVirtualRecord;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfdVirtualRecordService extends IBaseService<PfdVirtualRecord, Integer> {

	public List<PfdVirtualRecord> findPfdVirtualRecord(String pfpCustomerInfoId, Date addDate);
}
