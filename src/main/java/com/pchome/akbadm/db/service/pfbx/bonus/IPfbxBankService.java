package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxBankVo;

public interface IPfbxBankService extends IBaseService <PfbxBank, Integer> {
	
	public List<PfbxBankVo> getPfbxBankVosByCustomerId(String customerId) throws Exception;
	
	public PfbxBank getPfbxMainUseBank(String pfbId);

}
