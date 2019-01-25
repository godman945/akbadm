package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBank;

public interface IPfbxBankDAO extends IBaseDAO <PfbxBank, Integer>{

	public List<PfbxBank> getListByCustomerId(String customerInfoId);
	
	public PfbxBank getPfbxMainUseBank(String pfbId);
}
