package com.pchome.akbadm.db.service.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.dao.pfd.bonus.IPfdBonusItemDAO;
import com.pchome.akbadm.db.pojo.PfdBonusItem;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.bonus.EnumBonusItem;

public class PfdBonusItemService extends BaseService<PfdBonusItem, Integer> implements IPfdBonusItemService{

	public PfdBonusItem findPfdBonusItem(EnumBonusItem enumBonusItem) {
		
		List<PfdBonusItem> list = ((IPfdBonusItemDAO)dao).findPfdBonusItem(enumBonusItem.getItemId());
		
		if(list.isEmpty()){
			
			return null;
		}else{
			return list.get(0);
		}
		 
	}
}
