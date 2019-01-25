package com.pchome.akbadm.db.dao.manager;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmChannelAccount;
import com.pchome.enumerate.manager.EnumChannelCategory;

public interface IAdmChannelAccountDAO extends IBaseDAO<AdmChannelAccount, Integer>{
	
	public List<AdmChannelAccount> findAdmChannelAccount(String memberId, String channelCategory);
	
	public Integer deleteAdmChannelAccount(String memberId, String channelCategory);
}
