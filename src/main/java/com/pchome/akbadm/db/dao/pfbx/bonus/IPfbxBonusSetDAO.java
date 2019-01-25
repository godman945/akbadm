package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;

public interface IPfbxBonusSetDAO extends IBaseDAO<PfbxBonusSet, Integer>{

	public List<PfbxBonusSet> findPfbxBonusSets(String pfbId, Date startDate);
	
	public List<PfbxBonusSet> getListOrderByStartDate();
	
	public List<PfbxBonusSet> getListByPfbIdOrderSDate(String pfbid);
}
