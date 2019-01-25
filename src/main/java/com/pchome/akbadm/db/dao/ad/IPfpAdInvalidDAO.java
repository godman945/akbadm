package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdInvalid;

public interface IPfpAdInvalidDAO extends IBaseDAO<PfpAdInvalid, String>{
	public List<Object> accountInvalidSum(String customerInfoId, Date date);

	public List<Object> adActionInvalidSum(String adActionSeq, Date date);

	public int deleteMalice(Date recordDate, int recordTime);
}
