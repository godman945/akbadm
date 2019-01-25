package com.pchome.akbadm.db.dao.customerInfo;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdVirtualRecord;

public interface IPfdVirtualRecordDAO extends IBaseDAO<PfdVirtualRecord, Integer>{

	public List<PfdVirtualRecord> findPfdVirtualRecord(String pfpCustomerInfoId, Date addDate);
}
