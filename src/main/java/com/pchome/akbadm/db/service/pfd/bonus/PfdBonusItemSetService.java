package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.pfd.bonus.IPfdBonusItemSetDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusType;

public class PfdBonusItemSetService extends BaseService <PfdBonusItemSet, Integer> implements IPfdBonusItemSetService{

	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId){		
		return ((IPfdBonusItemSetDAO) dao).findPfdBonusItemSets(contractId);
	}
	
	public List<PfdBonusItemSet> findPfdBonusItemSets(String contractId, EnumPfdBonusType enumPfdBonusType) {
		return ((IPfdBonusItemSetDAO) dao).findPfdBonusItemSets(contractId, enumPfdBonusType.getType());

	}
	
	public Integer deletePfdBonusItemSet(String contractId, EnumPfdBonusType enumPfdBonusType){
		return ((IPfdBonusItemSetDAO) dao).deletePfdBonusItemSet(contractId, enumPfdBonusType.getType());
	}
}
