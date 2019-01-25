package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmBonusSetService extends IBaseService<AdmBonusSet,Integer>{

	public AdmBonusSet findLastAdmBonusSet(Date startDate);
}
