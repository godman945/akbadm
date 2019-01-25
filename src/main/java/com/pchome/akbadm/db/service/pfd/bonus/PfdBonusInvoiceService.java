package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfd.bonus.IPfdBonusInvoiceDAO;
import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;
import com.pchome.soft.util.DateValueUtil;

public class PfdBonusInvoiceService extends BaseService <PfdBonusInvoice, Integer> implements IPfdBonusInvoiceService{

	public PfdBonusInvoice findPfdBonusInvoice(String pfdId, int year, int month, String payType){
		List<PfdBonusInvoice> pfdBonusInvoices = ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoice(pfdId, year, month, payType);
		
		if(pfdBonusInvoices.isEmpty()){
			return null;
		}else{
			return pfdBonusInvoices.get(0);
		}
	}
	
	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month){
		log.info(">>> pfdId = "+pfdId);
		log.info(">>> year = "+year);
		log.info(">>> month = "+month);
		return ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoice(pfdId, year, month);
	}
	
	public PfdBonusInvoice findPfdLastInvoiceByMonthPayment(String pfdId, int closeYear, int closeMonth){
		List<PfdBonusInvoice> pfdBonusInvoices = ((IPfdBonusInvoiceDAO)dao).findPfdLastInvoiceByMonthPayment(pfdId, closeYear, closeMonth);
		
		if(pfdBonusInvoices.isEmpty()){
			return null;
		}else{
			return pfdBonusInvoices.get(0);
		}
	}
	
	public List<PfdBonusInvoice> findPfdBonusInvoices(String pfdId, String strDate, String endDate) {
		
		return ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoices(pfdId, DateValueUtil.getInstance().stringToDate(strDate), DateValueUtil.getInstance().stringToDate(endDate));	
	}
	
	public PfdBonusInvoice findPfdBonusInvoice(String bonusInvoiceId) {	
		return ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoice(Integer.valueOf(bonusInvoiceId));
	}
	
	public float findNonCloseBonusMoney(String pfdId, String payType, String applyDate, Integer payId) {
		return ((IPfdBonusInvoiceDAO)dao).findNonCloseBonusMoney(pfdId, payType, DateValueUtil.getInstance().stringToDate(applyDate),payId);
	}
	
	public List<PfdBonusInvoice> findNonCloseBonus(String pfdId, String payType, String applyDate) {
		return ((IPfdBonusInvoiceDAO)dao).findNonCloseBonus(pfdId, payType, DateValueUtil.getInstance().stringToDate(applyDate));
	}
	
	public Map<String,PfdBonusInvoiceVO> getCloseTheLastInvoice() {
		return ((IPfdBonusInvoiceDAO)dao).getCloseTheLastInvoice();
	}
	
	public PfdBonusInvoice findPfdBonusInvoiceByContract(String pfdId, int year, int month, String payType, String contractId){
		List<PfdBonusInvoice> pfdBonusInvoices = ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoiceByContract(pfdId, year, month, payType, contractId);
		
		if(pfdBonusInvoices.isEmpty()){
			return null;
		}else{
			return pfdBonusInvoices.get(0);
		}
	}
	
	public List<PfdBonusRecordInvoiceVO> findPfdBonusInvoicesByPayment(String pfdId, String strDate, String endDate) throws Exception{
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> strDate = " + strDate);
		log.info(">>> endDate = " + endDate);
		return ((IPfdBonusInvoiceDAO)dao).findPfdBonusInvoicesByPayment(pfdId, strDate, endDate);	
	}
	
	public List<PfdBonusInvoice> findLaterTotalAdClkPrice(String pfdId, int closeYear, int closeMonth) throws Exception{
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> closeYear = " + closeYear);
		log.info(">>> closeMonth = " + closeMonth);
		return ((IPfdBonusInvoiceDAO)dao).findLaterTotalAdClkPrice(pfdId, closeYear, closeMonth);
	}
	
}
