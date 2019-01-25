package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItem;

public interface IPfdBonusItemDAO extends IBaseDAO<PfdBonusItem, Integer>{
	
	public List<PfdBonusItem> findPfdBonusItem(int bonusItemId);
}
