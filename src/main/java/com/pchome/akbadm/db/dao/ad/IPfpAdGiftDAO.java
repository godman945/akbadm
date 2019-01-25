package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdGift;

public interface IPfpAdGiftDAO extends IBaseDAO<PfpAdGift,String>{
	
	public List<PfpAdGift> getPfpAdGifts(String adGiftId, String adGiftName, String adGiftSno, String adGiftStatus, String adGiftEndDate) throws Exception;
	
	public List<Object> getPfpAdGifts(String dateType, String startDate, String endDate, String adGiftName, String adGiftSno, String customerInfoId, String orderId, String transDetail, String testSno, String adGiftStatus, int page, int pageSize) throws Exception;

	public String chkAdGiftSnoExist(String adGiftSno) throws Exception;
	
	public PfpAdGift getPfpAdGiftBySno(String adGiftSno) throws Exception;
	
	public PfpAdGift getPfpAdGiftByOrderId(String orderId) throws Exception;
	
	public PfpAdGift getPfpAdGiftByCustomer(String customerInfoId) throws Exception;

	public PfpAdGift getPfpAdGiftOpenByCustomer(String customerInfoId) throws Exception;
	
	public List<PfpAdGift> getAdGifts() throws Exception;
	
	public void saveOrUpdatePfpAdGift(PfpAdGift pfpAdGift) throws Exception;
	
	public void insertPfpAdGift(PfpAdGift pfpAdGift) throws Exception;
	
	public void insertPfpAdGift(List<PfpAdGift> dataList) throws Exception;
	
	public void updatePfpAdGift(PfpAdGift pfpAdGift) throws Exception;
	
	public void updatePfpAdGiftStatus(String adGiftStatus, String adGroupSno) throws Exception;
	
	public void updateTransDetail(Integer adGiftId, char transDetail) throws Exception;
	
	public void saveOrUpdateWithCommit(PfpAdGift pfpAdGift) throws Exception;
}
