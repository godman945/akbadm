package com.pchome.akbadm.db.service.pfd.bonus;

import com.pchome.akbadm.db.pojo.PfdBonusItem;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.bonus.EnumBonusItem;

public interface IPfdBonusItemService extends IBaseService<PfdBonusItem, Integer>{
	
	public PfdBonusItem findPfdBonusItem(EnumBonusItem enumBonusItem);
}
