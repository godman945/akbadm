package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusSetDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxBonusSetService extends BaseService<PfbxBonusSet, Integer> implements IPfbxBonusSetService{
	
	public PfbxBonusSet findPfbxBonusSet(String pfbId, Date startDate) {
		
		List<PfbxBonusSet> list =  ((IPfbxBonusSetDAO)dao).findPfbxBonusSets(pfbId, startDate);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
		
	}

}
