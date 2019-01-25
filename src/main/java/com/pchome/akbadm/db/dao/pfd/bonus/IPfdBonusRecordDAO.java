package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;

public interface IPfdBonusRecordDAO extends IBaseDAO <PfdBonusRecord, Integer>{

	public List<PfdBonusRecord> findPfdBonusRecord(String pfdId, String pfdPayType, int year, int month, String pfdBonusItem);
	
	public List<PfdBonusRecord> findPfdBonusRecordByContractId(String pfdContractId, String pfdId, String payType, int year, int month, String pfdBonusItem);
	
//	public List<PfdBonusRecord> findPfdBonusRecordForQuarter(String pfdId, String pfdPayType, int year, int quarter, String pfdBonusItem);
//	
//	public List<PfdBonusRecord> findPfdBonusRecordForYear(String pfdId, String pfdPayType, int year, String pfdBonusItem);
	
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
