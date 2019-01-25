package com.pchome.akbadm.db.dao.customerInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdVirtualRecord;

public class PfdVirtualRecordDAO extends BaseDAO<PfdVirtualRecord, Integer> implements IPfdVirtualRecordDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<PfdVirtualRecord> findPfdVirtualRecord(String pfpCustomerInfoId, Date addDate) {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from PfdVirtualRecord ");
        hql.append("where addDate = ? ");
		hql.append("and pfpCustomerInfo.customerInfoId = ? ");

        list.add(addDate);
		list.add(pfpCustomerInfoId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
}
