package com.pchome.akbadm.db.service.menu;

import java.util.List;

import com.pchome.akbadm.db.dao.menu.MenuVO;
import com.pchome.akbadm.db.pojo.AdmMenu;
import com.pchome.akbadm.db.service.IBaseService;

public interface IMenuService extends IBaseService<AdmMenu, String>{

	public List<MenuVO> getSortMenu(List<String> selectedIdList) throws Exception;
	
}
