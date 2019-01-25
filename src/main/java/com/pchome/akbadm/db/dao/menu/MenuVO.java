package com.pchome.akbadm.db.dao.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuVO {

	private String menuId;
	private String displayName;
	private String action;
	private List<MenuVO> childrenList = new ArrayList<MenuVO>();
	private String sort;
	private String checkedFlag;

	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<MenuVO> getChildrenList() {
		return childrenList;
	}
	public void addChildrenList(MenuVO menuVO) {
		this.childrenList.add(menuVO);
	}
	public void setChildrenList(List<MenuVO> childrenList) {
		this.childrenList = childrenList;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCheckedFlag() {
		return checkedFlag;
	}
	public void setCheckedFlag(String checkedFlag) {
		this.checkedFlag = checkedFlag;
	}
}
