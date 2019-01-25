package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.pfd.bonus.IPfdBonusRecordDAO;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.service.BaseService;

public class PfdBonusRecordService extends BaseService<PfdBonusRecord, Integer> implements IPfdBonusRecordService{

	public PfdBonusRecord findPfdBonusRecord(String pfdId, String payType, int year, int month, String pfdBonusItem) {
		
		List<PfdBonusRecord> pfdBonusRecords = ((IPfdBonusRecordDAO) dao).findPfdBonusRecord(pfdId, payType, year, month, pfdBonusItem);
		
		if(pfdBonusRecords.isEmpty()){
			return null;
		}else{
			return pfdBonusRecords.get(0);
		}
	}
	
	public PfdBonusRecord findPfdBonusRecordByContractId(String pfdContractId, String pfdId, String payType, int year, int month, String pfdBonusItem) {
		
		List<PfdBonusRecord> pfdBonusRecords = ((IPfdBonusRecordDAO) dao).findPfdBonusRecordByContractId(pfdContractId, pfdId, payType, year, month, pfdBonusItem);
		
		if(pfdBonusRecords.isEmpty()){
			return null;
		}else{
			return pfdBonusRecords.get(0);
		}
	}
	
//	public PfdBonusRecord findPfdBonusRecordForQuarter(String pfdId, String pfdPayType, int year, int quarter, String pfdBonusItem) {
//		
//		List<PfdBonusRecord> pfdBonusRecords = ((IPfdBonusRecordDAO) dao).findPfdBonusRecordForQuarter(pfdId, pfdPayType, year, quarter, pfdBonusItem);
//		
//		if(pfdBonusRecords.isEmpty()){
//			return null;
//		}else{
//			return pfdBonusRecords.get(0);
//		}
//	}
//
//	public PfdBonusRecord findPfdBonusRecordForYear(String pfdId, String pfdPayType, int year, String pfdBonusItem) {
//	
//	List<PfdBonusRecord> pfdBonusRecords = ((IPfdBonusRecordDAO) dao).findPfdBonusRecordForYear(pfdId, pfdPayType, year, pfdBonusItem);
//	
//	if(pfdBonusRecords.isEmpty()){
//		return null;
//	}else{
//		return pfdBonusRecords.get(0);
//	}
//	}
	
	public float findPfdBonusMoney(String pfdId, String payType, int year, int month) {
		return ((IPfdBonusRecordDAO) dao).findPfdBonusMoney(pfdId, payType, year, month);
	}
	
	public float findPfdBonusAdClkPrice(String pfdId, String payType, int year, int month, String pfdBonusItem) {
		return ((IPfdBonusRecordDAO) dao).findPfdBonusAdClkPrice(pfdId, payType, year, month, pfdBonusItem);
	}
	
	public float findPayBonus(String pfdId, String payType, int year, int quarter, String pfdBonusItem) {
		return ((IPfdBonusRecordDAO) dao).findPayBonus(pfdId, payType, year, quarter, pfdBonusItem);
	}
	
	public List<PfdBonusRecord> findPfdBonusRecords(String pfdId, String payType, int year, int month, int quarter) throws Exception{		
		return ((IPfdBonusRecordDAO) dao).findPfdBonusRecords(pfdId, payType, year, month, quarter);
	}
	
	public List<PfdBonusRecord> findPfdBonusRecordsByPayment(String pfdId, int year, int month) throws Exception{	
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdBonusRecordsByPayment(pfdId, year, month);
	}
	
	public List<PfdBonusRecord> findPfdInvoiceBonusRecords(String pfdId, int year, int month) throws Exception{	
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdInvoiceBonusRecords(pfdId, year, month);
	}
	
	public List<PfdBonusRecord> findnonClosePfdBonusRecords(String pfdId, int year, int month) throws Exception{
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findnonClosePfdBonusRecords(pfdId, year, month);
	}
	
	public float findPfdBonusMoneyByContract(String pfdId, String payType, int year, int month, String contractId) {
		return ((IPfdBonusRecordDAO) dao).findPfdBonusMoneyByContract(pfdId, payType, year, month, contractId);
	}
	
	public float findPfdBonusAdClkPriceByContract(String pfdId, String payType, int year, int month, String pfdBonusItem, String contractId) {
		return ((IPfdBonusRecordDAO) dao).findPfdBonusAdClkPriceByContract(pfdId, payType, year, month, pfdBonusItem, contractId);
	}
	
	public List<PfdBonusRecord> findPfdBonusRecordsInvoiceDetail(String pfdId, int year, int month)throws Exception {
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdBonusRecordsInvoiceDetail(pfdId, year, month);
	}
	
	public List<PfdBonusRecord> findPfdTotalAdClick(String pfdId, int year, int month) throws Exception{	
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdTotalAdClick(pfdId, year, month);
	}
	
	public List<PfdBonusRecord> findPfdInvoiceTotalAdClick(String pfdId, int year, int month) throws Exception{	
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdInvoiceTotalAdClick(pfdId, year, month);
	}
	
	public List<PfdBonusRecord> findPfdNonCloseTotalAdClick(String pfdId, int year, int month) throws Exception{	
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		return ((IPfdBonusRecordDAO) dao).findPfdNonCloseTotalAdClick(pfdId, year, month);
	}
	
}
