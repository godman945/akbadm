package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmBonusSet;

public interface IAdmBonusSetDAO extends IBaseDAO<AdmBonusSet, Integer>{

	public List<AdmBonusSet> findLastAdmBonusSet(Date startDate);
}
