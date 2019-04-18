package com.pchome.akbadm.db.dao.menu;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.dao.menu.IMenuDAO;
import com.pchome.akbadm.db.pojo.AdmMenu;

public class MenuDAO extends BaseDAO<AdmMenu, String> implements IMenuDAO {

	@SuppressWarnings("unchecked")
	public List<AdmMenu> getParentMenu() throws Exception {
		return (List<AdmMenu>) super.getHibernateTemplate().find("from AdmMenu where parentMenuId is null order by sort");
	}

	@SuppressWarnings("unchecked")
	public List<AdmMenu> getChildMenu() throws Exception {
		return (List<AdmMenu>) super.getHibernateTemplate().find("from AdmMenu where parentMenuId is not null order by sort");
	}

}
