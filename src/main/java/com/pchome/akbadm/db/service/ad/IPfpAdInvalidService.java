package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdInvalid;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdInvalidService extends IBaseService<PfpAdInvalid, String>{
	public List<Object> accountInvalidSum(String customerInfoId, Date date);

	public List<Object> adActionInvalidSum(String adActionSeq, Date date);

	public int deleteMalice(Date recordDate, int recordTime);
}
