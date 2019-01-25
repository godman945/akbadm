package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfbxBonusSet;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxBonusSetService extends IBaseService<PfbxBonusSet, Integer>{

	public PfbxBonusSet findPfbxBonusSet(String pfbId, Date startDate);
}
