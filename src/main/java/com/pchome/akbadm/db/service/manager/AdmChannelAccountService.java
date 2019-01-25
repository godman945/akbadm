package com.pchome.akbadm.db.service.manager;

import com.pchome.akbadm.db.dao.manager.IAdmChannelAccountDAO;
import com.pchome.akbadm.db.pojo.AdmChannelAccount;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.manager.EnumChannelCategory;

public class AdmChannelAccountService extends BaseService<AdmChannelAccount, Integer> implements IAdmChannelAccountService{
	
//	public List<AdmChannelAccount> findAdmChannelAccount(String memberId, EnumChannelCategory enumChannelCategory){
//		return ((IAdmChannelAccountDAO)dao).findAdmChannelAccount(memberId, enumChannelCategory.getCategory());
//	}
	
	public Integer deleteAdmChannelAccount(String memberId, String channelCategory) {
		return ((IAdmChannelAccountDAO)dao).deleteAdmChannelAccount(memberId, channelCategory);
	}
}
