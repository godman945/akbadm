package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;

public interface IPfdBonusInvoiceService extends IBaseService <PfdBonusInvoice, Integer>{

	public PfdBonusInvoice findPfdBonusInvoice(String pfdId, int year, int month, String payType);
	
	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month);
	
	public PfdBonusInvoice findPfdLastInvoiceByMonthPayment(String pfdId, int closeYear, int closeMonth);
	
	public List<PfdBonusInvoice> findPfdBonusInvoices(String pfdId, String strDate, String endDate);
	
	public PfdBonusInvoice findPfdBonusInvoice(String bonusInvoiceId);
	
	public float findNonCloseBonusMoney(String pfdId, String payType, String applyDate, Integer payId);
	
	public List<PfdBonusInvoice> findNonCloseBonus(String pfdId, String payType, String applyDate);
	
	public Map<String,PfdBonusInvoiceVO> getCloseTheLastInvoice();
	
	public PfdBonusInvoice findPfdBonusInvoiceByContract(String pfdId, int year, int month, String payType, String contractId);
	
	public List<PfdBonusRecordInvoiceVO> findPfdBonusInvoicesByPayment(String pfdId, String strDate, String endDate)throws Exception;
	
	public List<PfdBonusInvoice> findLaterTotalAdClkPrice(String pfdId, int closeYear, int closeMonth) throws Exception;
	
}
