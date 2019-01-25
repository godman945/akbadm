package com.pchome.akbadm.db.dao.trans;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmTransLoss;

public interface IAdmTransLossDAO extends IBaseDAO<AdmTransLoss, String>{

	public List<Object[]> getTransLossSumByTransDate(Date sDate , Date eDate) throws Exception;
	
	public Integer deleteRecordAfterDate(Date startDate);

	public float selectTransLossSumByTransDate(String transDate);

	public float selectTransLossSumByTransDateAndCustInfoId(String transDate,
			String custInfoId) throws Exception;

	public Map<String, Double> selectTransLossSumByTransDateMap(String transDate) throws Exception;
	
	public List<Object[]> findTransLosses(Date transDate);
}
