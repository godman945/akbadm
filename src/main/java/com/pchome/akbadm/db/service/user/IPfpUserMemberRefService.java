package com.pchome.akbadm.db.service.user;

import com.pchome.akbadm.db.pojo.PfpUserMemberRef;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpUserMemberRefService extends IBaseService<PfpUserMemberRef,String>{

	public PfpUserMemberRef getUserMemberRef(String userId) throws Exception;
	
	public Integer deletePfpUserMemberRef(String pcId);
}
