package com.pchome.akbadm.db.service.ad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.ad.PfpAdGiftDAO;
import com.pchome.akbadm.db.dao.ad.PfpAdGroupDAO;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.pojo.PfpAdGift;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdGiftViewVO;
import com.pchome.akbadm.db.vo.ad.PfpAdGroupViewVO;
import com.pchome.enumerate.ad.EnumAdGiftStatus;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.ad.EnumAdType;

public class PfpAdGiftService extends BaseService<PfpAdGift,String> implements IPfpAdGiftService{

	public List<PfpAdGift> getAllPfpAdGifts() throws Exception{
		return ((PfpAdGiftDAO)dao).loadAll();
	}

	public List<PfpAdGift> getPfpAdGifts(String adGiftId, String adGiftName, String adGiftSno, String adGiftStatus, String adGiftEndDate) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGifts(adGiftId, adGiftName, adGiftSno, adGiftStatus, adGiftEndDate);
	}

	public List<Object> getPfpAdGifts(String dateType, String startDate, String endDate, String adGiftName, String adGiftSno, String customerInfoId, String orderId, String transDetail, String testSno, String adGiftStatus) throws Exception {
		return getPfpAdGifts(dateType, startDate, endDate, adGiftName, adGiftSno, customerInfoId, orderId, transDetail, testSno, adGiftStatus, -1, -1);
	}

	public List<Object> getPfpAdGifts(String dateType, String startDate, String endDate, String adGiftName, String adGiftSno, String customerInfoId, String orderId, String transDetail, String testSno, String adGiftStatus, int page, int pageSize) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGifts(dateType, startDate, endDate, adGiftName, adGiftSno, customerInfoId, orderId, transDetail, testSno, adGiftStatus, page, pageSize);
	}

	public List<AdmFreeRecord> getPfpAdGiftSubs(String customerInfoId, String startDate, String endDate, String actionId) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGiftSubs(customerInfoId, startDate, endDate, actionId);
	}
	
	public List<PfpAdGiftViewVO> getAdGiftView(String dateType, String startDate, String endDate, String adGiftName, String adGiftSno, String customerInfoId, String orderId, String transDetail, String testSno, String adGiftStatus, int page, int pageSize) throws Exception{

		List<PfpAdGiftViewVO> adGiftViewVOs = null;
		List<Object> objects = ((PfpAdGiftDAO)dao).getPfpAdGifts(dateType, startDate, endDate, adGiftName, adGiftSno, customerInfoId, orderId, transDetail, testSno, adGiftStatus, page, pageSize);
		
		for(Object object:objects){
			Object[] ob = (Object[])object;
			
			if(ob[0] != null){
				
				if(adGiftViewVOs == null){
					adGiftViewVOs = new ArrayList<PfpAdGiftViewVO>();
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				PfpAdGiftViewVO adGiftViewVO = new PfpAdGiftViewVO();
				adGiftViewVO.setAdGiftId(ob[0].toString());
				adGiftViewVO.setAdGiftName(ob[1].toString());
				adGiftViewVO.setAdGiftSno(ob[2].toString());
				adGiftViewVO.setAdGiftPrice(Integer.parseInt(ob[3].toString()));
				adGiftViewVO.setCustomerInfoId(ob[4] == null?null:ob[4].toString());
				adGiftViewVO.setOrderId(ob[5] == null?null:ob[5].toString());
				adGiftViewVO.setOpenDate(ob[6] == null?null:sdf.format(ob[6]));
				adGiftViewVO.setTransDetail(ob[7].toString());
				if(ob[7].toString().equals("Y")) {
					adGiftViewVO.setTransDetailDesc("已記錄");
				} else {
					adGiftViewVO.setTransDetailDesc("未記錄");
				}

				// 廣告狀態
				for(EnumAdGiftStatus status:EnumAdGiftStatus.values()){
					int ad_gift_status = Integer.parseInt(ob[8].toString());
					if(status.getStatusId() == ad_gift_status){
						adGiftViewVO.setAdGiftStatus(ob[8].toString());
						adGiftViewVO.setAdGiftStatusDesc(status.getStatusDesc());
					}
				}
				adGiftViewVO.setAdGiftEndDate(sdf.format(ob[9]));
				adGiftViewVO.setTestSno(ob[10].toString());
				if(ob[10].toString().equals("1")) {
					adGiftViewVO.setTestSnoDesc("正式");
				} else {
					adGiftViewVO.setTestSnoDesc("測試");
				}
				adGiftViewVO.setAdGiftCreateDate(sdf.format(ob[11]));
				adGiftViewVO.setAdGiftUpdateDate(sdf.format(ob[12]));
				
				adGiftViewVOs.add(adGiftViewVO);
			}
		}
		
		return adGiftViewVOs;		
	}

	public String chkAdGiftSnoExist(String adGiftSno) throws Exception {
		return ((PfpAdGiftDAO)dao).chkAdGiftSnoExist(adGiftSno);
	}
	
	public PfpAdGift getPfpAdGiftBySno(String adGiftSno) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGiftBySno(adGiftSno);
	}
	
	public PfpAdGift getPfpAdGiftByOrderId(String orderId) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGiftByOrderId(orderId);
	}
	
	public PfpAdGift getPfpAdGiftByCustomer(String customerInfoId) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGiftByCustomer(customerInfoId);
	}
	
	public PfpAdGift getPfpAdGiftOpenByCustomer(String customerInfoId) throws Exception {
		return ((PfpAdGiftDAO)dao).getPfpAdGiftOpenByCustomer(customerInfoId);
	}
	
	public void insertPfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		((PfpAdGiftDAO)dao).insertPfpAdGift(pfpAdGift);
	}

	public void insertPfpAdGift(List<PfpAdGift> dataList) throws Exception {
		((PfpAdGiftDAO)dao).insertPfpAdGift(dataList);
	}
	
	public void updatePfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		((PfpAdGiftDAO)dao).updatePfpAdGift(pfpAdGift);
	}

	public void updatePfpAdGiftStatus(String adGiftStatus, String adGroupSno) throws Exception {
		((PfpAdGiftDAO)dao).updatePfpAdGiftStatus(adGiftStatus, adGroupSno);
	}

	public void updateTransDetail(Integer adGiftId, char transDetail) throws Exception {
		((PfpAdGiftDAO)dao).updateTransDetail(adGiftId, transDetail);
	}

	public void savePfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		((PfpAdGiftDAO)dao).saveOrUpdatePfpAdGift(pfpAdGift);
	}
	
	public void saveOrUpdatePfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		((PfpAdGiftDAO)dao).saveOrUpdatePfpAdGift(pfpAdGift);
	}
	
	public void saveOrUpdateWithCommit(PfpAdGift pfpAdGift) throws Exception {
		((PfpAdGiftDAO)dao).saveOrUpdateWithCommit(pfpAdGift);
	}
}
