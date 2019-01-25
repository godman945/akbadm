package com.pchome.akbadm.db.service.trans;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmTransLoss;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmTransLossService extends IBaseService<AdmTransLoss, String> {
	
	public List<Object[]> getTransLossSumByTransDate(Date sDate , Date eDate) throws Exception;

	public Integer deleteRecordAfterDate(String startDate);

	public float selectTransLossSumByTransDate(String transDate);

	public float selectTransLossSumByTransDateAndCustInfoId(String transDate,
			String custInfoId) throws Exception;

	public Map<String, Double> selectTransLossSumByTransDateMap(String transDate) throws Exception;
}
