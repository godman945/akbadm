package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusType;

public interface IPfdBonusItemSetService extends IBaseService <PfdBonusItemSet, Integer>{

	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId);
	
	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId, EnumPfdBonusType enumPfdBonusType);
	
	public Integer deletePfdBonusItemSet(String contractId, EnumPfdBonusType enumPfdBonusType);

}
