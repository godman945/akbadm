package com.pchome.akbadm.db.service.manager;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmChannelAccount;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.manager.EnumChannelCategory;

public interface IAdmChannelAccountService extends IBaseService<AdmChannelAccount, Integer>{
	
	//public List<AdmChannelAccount> findAdmChannelAccount(String memberId, EnumChannelCategory enumChannelCategory);
	
	public Integer deleteAdmChannelAccount(String memberId, String channelCategory);
}
