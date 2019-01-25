package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.rmi.bonus.PfdBonusDetailVo;

public interface IPfdBonusRecordService extends IBaseService<PfdBonusRecord, Integer>{

	public PfdBonusRecord findPfdBonusRecord(String pfdId, String payType, int year, int month, String pfdBonusItem);
	
	public PfdBonusRecord findPfdBonusRecordByContractId(String pfdContractId, String pfdId, String payType, int year, int month, String pfdBonusItem);
	
//	public PfdBonusRecord findPfdBonusRecordForQuarter(String pfdId, String payType, int year, int quarter, String pfdBonusItem);
//	
//	public PfdBonusRecord findPfdBonusRecordForYear(String pfdId, String payType, int year, String pfdBonusItem);
	
	public float findPfdBonusMoney(String pfdId, String payType, int year, int month);
	
	public float findPfdBonusAdClkPrice(String pfdId, String payType, int year, int month, String pfdBonusItem);
	
	public float findPayBonus(String pfdId, String payType, int year, int quarter, String pfdBonusItem);
	
	public List<PfdBonusRecord> findPfdBonusRecords(String pfdId, String payType, int year, int month, int quarter)throws Exception;
	
	public List<PfdBonusRecord> findPfdBonusRecordsByPayment(String pfdId, int year, int month) throws Exception;
	
	public List<PfdBonusRecord> findPfdInvoiceBonusRecords(String pfdId, int year, int month) throws Exception;
	
	public List<PfdBonusRecord> findnonClosePfdBonusRecords(String pfdId, int year, int month)throws Exception;
	
	public float findPfdBonusMoneyByContract(String pfdId, String payType, int year, int month, String contractId);
	
	public float findPfdBonusAdClkPriceByContract(String pfdId, String payType, int year, int month, String pfdBonusItem, String contractId);
	
	public List<PfdBonusRecord> findPfdBonusRecordsInvoiceDetail(String pfdId, int year, int month)throws Exception;
	
	public List<PfdBonusRecord> findPfdTotalAdClick(String pfdId, int year, int month)throws Exception;
	
	public List<PfdBonusRecord> findPfdInvoiceTotalAdClick(String pfdId, int year, int month)throws Exception;
	
	public List<PfdBonusRecord> findPfdNonCloseTotalAdClick(String pfdId, int year, int month)throws Exception;
	
}
