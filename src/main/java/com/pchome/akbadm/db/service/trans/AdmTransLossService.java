package com.pchome.akbadm.db.service.trans;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.trans.IAdmTransLossDAO;
import com.pchome.akbadm.db.pojo.AdmTransLoss;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.soft.util.DateValueUtil;

public class AdmTransLossService extends BaseService <AdmTransLoss, String> implements IAdmTransLossService {

	public List<Object[]> getTransLossSumByTransDate(Date sDate , Date eDate) throws Exception
	{
		return ((IAdmTransLossDAO)dao).getTransLossSumByTransDate(sDate, eDate);
	}
	
	public Integer deleteRecordAfterDate(String startDate) {
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IAdmTransLossDAO)dao).deleteRecordAfterDate(date);
	}

	public float selectTransLossSumByTransDate(String transDate) {
	    return ((IAdmTransLossDAO)dao).selectTransLossSumByTransDate(transDate);
	}

	public float selectTransLossSumByTransDateAndCustInfoId(String transDate,
			String custInfoId) throws Exception {
		return ((IAdmTransLossDAO)dao).selectTransLossSumByTransDateAndCustInfoId(transDate, custInfoId);
	}

	public Map<String, Double> selectTransLossSumByTransDateMap(String transDate) throws Exception {
		return ((IAdmTransLossDAO)dao).selectTransLossSumByTransDateMap(transDate);
	}
}
