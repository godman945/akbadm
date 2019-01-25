package com.pchome.akbadm.db.dao.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeGift;

public interface IAdmFreeGiftDAO extends IBaseDAO<AdmFreeGift, Integer>{

	public List<AdmFreeGift> findAdmFreeGiftSno(String freeActionId, String customerInfoId, Date openDate);
	
	public List<AdmFreeGift> findAdmFreeGift(String actionId);
	
	public List<AdmFreeGift> findAdmFreeGiftToBeUsed(String actionId);
}
