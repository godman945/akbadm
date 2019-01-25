package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.ad.IPfpAdInvalidDAO;
import com.pchome.akbadm.db.pojo.PfpAdInvalid;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdInvalidService extends BaseService<PfpAdInvalid, String> implements IPfpAdInvalidService{

	@Override
    public List<Object> accountInvalidSum(String customerInfoId, Date date) {
		return ((IPfpAdInvalidDAO)dao).accountInvalidSum(customerInfoId, date);
	}

	@Override
    public List<Object> adActionInvalidSum(String adActionSeq, Date date) {
		return ((IPfpAdInvalidDAO)dao).adActionInvalidSum(adActionSeq, date);
	}

	@Override
    public int deleteMalice(Date recordDate, int recordTime) {
        return ((IPfpAdInvalidDAO)dao).deleteMalice(recordDate, recordTime);
	}
}
