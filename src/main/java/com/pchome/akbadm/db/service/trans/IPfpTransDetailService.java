package com.pchome.akbadm.db.service.trans;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.PfpTransDetailReportVO;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public interface IPfpTransDetailService extends IBaseService <PfpTransDetail, String>{
	public List<PfpTransDetail> findTransDetail(String customerInfo, String startDate, String endDate, String pfdCustomerInfoId) throws Exception;

	public List<PfpTransDetailReportVO> findPfpTransDetailReport(String startDate, String endDate, String pfdCustomerInfoId) throws Exception;

	public List<PfpTransDetailReportVO> findPfpTransDetailReportDetail(String reportDate, String pfdCustomerInfoId) throws Exception;

	public List<PfpTransDetail> checkExistentTransDetail(String customerInfoId, Date date, String transType) throws Exception;

	//public void createTransDetail(SettlementVO vo, PfpCustomerInfo customerInfo, float remain, float tax, EnumTransType enumTransType, float transPrice, Date date)throws Exception;

	@Deprecated
	public SettlementVO findLatestTransDetail(String customerInfoId);

	public SettlementVO findLatestTransDetail2(String customerInfoId);

	public Integer deleteRecordAfterDate(String startDate);

	public Integer deleteRecordAfterDateNotFeedback(String startDate);

	public Integer deleteRecordAfterDateForFeedback(String startDate);

	public float findTransDetailSpendCost(String customerInfoId, Date date);

	public float findTransDetailInvalidCost(String customerInfoId, Date date);

    public float selectActivatePriceSumByActivateDate(EnumTransType enumTransType, String activateDate);

    public float selectActivatePriceSumByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String activateDate);

    public int selectCustomerInfoCountByTransDate(EnumTransType enumTransType, String transDate);

    public int selectCustomerInfoCountByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate);

    public float selectTransPriceSumByTransDate(EnumTransType enumTransType, String transDate);

    public float selectTransPriceSumByTransDate(EnumPfdAccountPayType enumPfdAccountPayType, EnumTransType enumTransType, String transDate);

    public float selectRemainPriceSumByTransDate(String transDate);

    public PfpTransDetail findTransDetail(String customerInfoId, Date date, EnumTransType enumTransType);

//    public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String startDate, String endDate);
//
//    public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate);
}
