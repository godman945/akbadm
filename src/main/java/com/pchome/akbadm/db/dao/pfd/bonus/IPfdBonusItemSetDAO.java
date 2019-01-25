package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;

public interface IPfdBonusItemSetDAO extends IBaseDAO <PfdBonusItemSet, Integer>{

	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId);
	
	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId, String pfdBonusType);

	
	public Integer deletePfdBonusItemSet(String contractId, String pfdBonusType);
}
