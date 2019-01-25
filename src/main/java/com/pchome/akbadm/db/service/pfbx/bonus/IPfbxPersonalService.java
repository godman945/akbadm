package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxPersonalVo;

public interface IPfbxPersonalService extends IBaseService <PfbxPersonal, Integer>{
	
	public List<PfbxPersonalVo> getPfbxPersonalVosByCustomerId(String customerId) throws Exception;
	
	public PfbxPersonal getPfbxMainUsePersonal(String pfbId);

}
