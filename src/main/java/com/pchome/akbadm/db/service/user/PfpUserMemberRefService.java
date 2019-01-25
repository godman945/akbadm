package com.pchome.akbadm.db.service.user;

import com.pchome.akbadm.db.dao.user.IPfpUserMemberRefDAO;
import com.pchome.akbadm.db.pojo.PfpUserMemberRef;
import com.pchome.akbadm.db.service.BaseService;

public class PfpUserMemberRefService extends BaseService<PfpUserMemberRef,String> implements IPfpUserMemberRefService{

	public PfpUserMemberRef getUserMemberRef(String userId) throws Exception{
		return ((IPfpUserMemberRefDAO)dao).getUserMemberRef(userId);
	}
	
	public Integer deletePfpUserMemberRef(String pcId) {
		return ((IPfpUserMemberRefDAO)dao).deletePfpUserMemberRef(pcId);
	}
}
