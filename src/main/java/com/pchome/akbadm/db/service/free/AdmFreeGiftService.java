package com.pchome.akbadm.db.service.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.free.AdmFreeGiftDAO;
import com.pchome.akbadm.db.pojo.AdmFreeGift;
import com.pchome.akbadm.db.service.BaseService;

public class AdmFreeGiftService extends BaseService<AdmFreeGift, Integer> implements IAdmFreeGiftService{

	public AdmFreeGift findAdmFreeGiftSno(String freeActionId, String customerInfoId, Date openDate) {
		
		List<AdmFreeGift> list = ((AdmFreeGiftDAO)dao).findAdmFreeGiftSno(freeActionId,customerInfoId, openDate);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	public List<AdmFreeGift> findAdmFreeGift(String actionId){
		return ((AdmFreeGiftDAO)dao).findAdmFreeGift(actionId);
	}
	
	public List<AdmFreeGift> findAdmFreeGiftToBeUsed(String actionId){
		return ((AdmFreeGiftDAO)dao).findAdmFreeGiftToBeUsed(actionId);
	}
}
