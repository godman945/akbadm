package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusBill;

public interface IPfbxBonusBillDAO extends IBaseDAO<PfbxBonusBill, Integer>
{

	public List<PfbxBonusBill> listByKeyWord(String keyword ,String category,String status);
	
	public List<PfbxBonusBill> listByPfbId(String pfbId);

	public List<PfbxBonusBill> findPfbxBonusBills(String pfbId);

}
