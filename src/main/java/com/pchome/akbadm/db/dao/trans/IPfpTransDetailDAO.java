package com.pchome.akbadm.db.dao.trans;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public interface IPfpTransDetailDAO extends IBaseDAO<PfpTransDetail, String>{
	public List<PfpTransDetail> findTransDetail(String customerInfo, String startDate, String endDate, String pfpCustInfoId) throws Exception;

	public List<PfpTransDetail> findPfpTransDetail(String startDate, String endDate, String pfpCustInfoId) throws Exception;

	public TreeMap<String, List<PfpTransDetail>> getPfpTransDetailByCustomerInfoIdList(String startDate, String endDate, List<String> pfpAdCustomerInfoIds, List<String> transType) throws Exception;

	public List<PfpTransDetail> checkExistentTransDetail(String customerInfoId, Date date, String transType) throws Exception;

	@Deprecated
    public List<PfpTransDetail> sortTransDetailDesc(String customerInfoId);

	public List<PfpTransDetail> sortTransDetailDesc(String customerInfoId, int firstResult, int maxResult);

	public Integer deleteRecordAfterDate(Date startDate);

	public Integer deleteRecordAfterDateNotFeedback(Date startDate);

    public Integer deleteRecordAfterDateForFeedback(Date startDate);

	public List<PfpTransDetail> findTransDetailSpendCost(String customerInfoId, Date date);

	public List<PfpTransDetail> findTransDetailInvalidCost(String customerInfoId, Date date);

    public float selectActivatePriceSumByActivateDate(EnumTransType enumTransType, String activateDate);

    public float selectActivatePriceSumByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String activateDate);

    public int selectCustomerInfoCountByTransDate(EnumTransType enumTransType, String transDate);

    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate);

    public float selectTransPriceSumByTransDate(EnumTransType enumTransType, String transDate);

    public float selectTransPriceSumByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate);

    public float selectRemainPriceSumByTransDate(String transDate);

	public List<PfpTransDetail> findTransDetail(String customerInfoId, Date date, String typeId);

	public List<PfpTransDetail> findPfpLastTransDetails(Date date);
}
