package com.pchome.akbadm.db.dao.menu;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmMenu;

public interface IMenuDAO extends IBaseDAO<AdmMenu, String> {

	public List<AdmMenu> getParentMenu() throws Exception;

	public List<AdmMenu> getChildMenu() throws Exception;

}
