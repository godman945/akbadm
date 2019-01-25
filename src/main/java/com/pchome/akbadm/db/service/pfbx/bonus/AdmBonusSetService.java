package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.bonus.IAdmBonusSetDAO;
import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.akbadm.db.service.BaseService;

public class AdmBonusSetService extends BaseService<AdmBonusSet, Integer> implements IAdmBonusSetService{

	public AdmBonusSet findLastAdmBonusSet(Date startDate) {
		List<AdmBonusSet> list = ((IAdmBonusSetDAO) dao).findLastAdmBonusSet(startDate);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
}
