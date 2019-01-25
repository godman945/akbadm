package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;

public interface IPfdBonusInvoiceDAO extends IBaseDAO <PfdBonusInvoice, Integer>{

	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month, String payType);
	
	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month);
	
	public List<PfdBonusInvoice> findPfdLastInvoiceByMonthPayment(String pfdId, int closeYear, int closeMonth);
	
	public List<PfdBonusInvoice> findPfdBonusInvoices(String pfdId, Date strDate, Date endDate);
	
	public PfdBonusInvoice findPfdBonusInvoice(int id);
	
	public float findNonCloseBonusMoney(String pfdId, String payType, Date applyDate, Integer payId);
	
	public List<PfdBonusInvoice> findNonCloseBonus(String pfdId, String payType, Date applyDate);
	
	public Map<String,PfdBonusInvoiceVO> getCloseTheLastInvoice();
	
	public List<PfdBonusInvoice> findPfdBonusInvoiceByContract(String pfdId, int year, int month, String payType, String contractId);
	
	public List<PfdBonusRecordInvoiceVO> findPfdBonusInvoicesByPayment(String pfdId, String strDate, String endDate)throws Exception;
	
	public List<PfdBonusInvoice> findLaterTotalAdClkPrice(String pfdId, int closeYear, int closeMonth) throws Exception;
	
}
