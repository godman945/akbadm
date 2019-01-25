package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPersonal;

public interface IPfbxPersonalDAO extends IBaseDAO<PfbxPersonal, Integer> {
	
	public List<PfbxPersonal> getListByCustomerId(String customerId);
	
	public PfbxPersonal getPfbxMainUsePersonal(String pfbId);

}
