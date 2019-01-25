package com.pchome.akbadm.db.service.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmFreeGift;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmFreeGiftService extends IBaseService<AdmFreeGift, Integer>{

	public AdmFreeGift findAdmFreeGiftSno(String freeActionId, String customerInfoId, Date openDate);
	
	public List<AdmFreeGift> findAdmFreeGift(String actionId);
	
	public List<AdmFreeGift> findAdmFreeGiftToBeUsed(String actionId);
}
