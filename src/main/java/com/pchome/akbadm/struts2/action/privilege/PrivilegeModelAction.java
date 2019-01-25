package com.pchome.akbadm.struts2.action.privilege;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.menu.MenuVO;
import com.pchome.akbadm.db.dao.privilege.PrivilegeModelVO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.akbadm.db.service.privilege.PrivilegeModelService;
import com.pchome.akbadm.struts2.BaseAction;

public class PrivilegeModelAction extends BaseAction {

	private PrivilegeModelService privilegeModelService;
	private MenuService menuService;

	private String message = "";

	private List<AdmPrivilegeModel> privilegeModelList;
	private String modelId;
	private String modelName;
	private String note;
	private String[] newMenuIds;
	private List<MenuVO> allMenuList; //功能總清單
	private List<String> dbMenuIdList; //資料庫讀取出來的舊權限
	private PrivilegeModelVO privilegeModelVO;

	public String execute() throws Exception {
		this.privilegeModelList = privilegeModelService.getAllPrivilegeModel();
		return SUCCESS;
	}

	public String detail() throws Exception {
		System.out.println(">>> modelId = " + modelId);
		this.privilegeModelVO = privilegeModelService.getPrivilegeModelById(modelId);
		List<String> dbMenuIdList = privilegeModelVO.getDbMenuIdList();
		this.allMenuList = menuService.getSortMenu(dbMenuIdList);
		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		this.allMenuList = menuService.getSortMenu(null);
		return SUCCESS;
	}

	public String doAdd() throws Exception {

		List<String> dbMenuIdList = new ArrayList<String>();
		if (newMenuIds==null || newMenuIds.length == 0) {
			message = "請選擇權限設定！";
			this.allMenuList = menuService.getSortMenu(null);
			return INPUT;
		} else {
			for (int i=0; i<newMenuIds.length; i++) {
				dbMenuIdList.add(newMenuIds[i]);
			}
		}

		if (StringUtils.isEmpty(modelName)) {
			message = "請輸入範本名稱！";
			this.allMenuList = menuService.getSortMenu(dbMenuIdList);
			return INPUT;
		} else {
			modelName = modelName.trim();
			if (modelName.length() > 20) {
				message = "範本名稱不可超過 20 字元！";
				this.allMenuList = menuService.getSortMenu(dbMenuIdList);
				return INPUT;
			}
		}

		if (StringUtils.isNotEmpty(note)) {
			note = note.trim();
			if (note.length() > 20) {
				message = "範本備註不可超過 50 字元！";
				this.allMenuList = menuService.getSortMenu(dbMenuIdList);
				return INPUT;
			}
		}

		PrivilegeModelVO privilegeModelVO = new PrivilegeModelVO();
		privilegeModelVO.setModelName(modelName);
		privilegeModelVO.setNote(note);
		privilegeModelVO.setNewMenuIds(newMenuIds);

		privilegeModelService.insertPrivilegeModel(privilegeModelVO);

		message = "新增成功！";

		return SUCCESS;
	}

	public String goUpdatePage() throws Exception {
		System.out.println(">>> modelId = " + modelId);
		this.privilegeModelVO = privilegeModelService.getPrivilegeModelById(modelId);
		this.modelId = this.privilegeModelVO.getModelId().toString();
		this.modelName = this.privilegeModelVO.getModelName();
		this.note = this.privilegeModelVO.getNote();
		List<String> dbMenuIdList = privilegeModelVO.getDbMenuIdList();
		this.allMenuList = menuService.getSortMenu(dbMenuIdList);
		return SUCCESS;
	}

	public String doUpdate() throws Exception {
		System.out.println(">>> modelId = " + modelId);
		List<String> dbMenuIdList = new ArrayList<String>();
		if (newMenuIds==null || newMenuIds.length == 0) {
			message = "請選擇權限設定！";
			this.allMenuList = menuService.getSortMenu(null);
			return INPUT;
		} else {
			for (int i=0; i<newMenuIds.length; i++) {
				dbMenuIdList.add(newMenuIds[i]);
			}
		}

		if (StringUtils.isEmpty(modelName)) {
			message = "請輸入範本名稱！";
			this.allMenuList = menuService.getSortMenu(dbMenuIdList);
			return INPUT;
		} else {
			modelName = modelName.trim();
			if (modelName.length() > 20) {
				message = "範本名稱不可超過 20 字元！";
				this.allMenuList = menuService.getSortMenu(dbMenuIdList);
				return INPUT;
			}
		}

		if (StringUtils.isNotEmpty(note)) {
			note = note.trim();
			if (note.length() > 20) {
				message = "範本備註不可超過 50 字元！";
				this.allMenuList = menuService.getSortMenu(dbMenuIdList);
				return INPUT;
			}
		}

		PrivilegeModelVO privilegeModelVO2 = new PrivilegeModelVO();
		privilegeModelVO2.setModelId(modelId);
		privilegeModelVO2.setModelName(modelName);
		privilegeModelVO2.setNote(note);
		privilegeModelVO2.setNewMenuIds(newMenuIds);

		privilegeModelService.updatePrivilegeModel(privilegeModelVO2);

		message = "修改成功！";

		return SUCCESS;
	}

	public String doDelete() throws Exception {
		System.out.println(">>> modelId = " + modelId);

		List<AdmUser> userList = privilegeModelService.deletePrivilegeModel(modelId);

		if (userList==null) {
			message = "刪除成功！";
		} else {
			message = "刪除失敗！ 此權限範本使用中, 使用者: ";
			for (int i=0; i<userList.size(); i++) {
				message += userList.get(i).getUserEmail() + ", ";
			}
			message = message.trim();
			if (message.lastIndexOf(",") == (message.length()-1)) {
				message = message.substring(0, message.length()-1);
			}
		}

		return SUCCESS;
	}

	public void setPrivilegeModelService(PrivilegeModelService privilegeModelService) {
		this.privilegeModelService = privilegeModelService;
	}

	public List<AdmPrivilegeModel> getPrivilegeModelList() {
		return privilegeModelList;
	}

	public void setPrivilegeModelList(List<AdmPrivilegeModel> privilegeModelList) {
		this.privilegeModelList = privilegeModelList;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<String> getDbMenuIdList() {
		return dbMenuIdList;
	}

	public void setDbMenuIdList(List<String> dbMenuIdList) {
		this.dbMenuIdList = dbMenuIdList;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public List<MenuVO> getAllMenuList() {
		return allMenuList;
	}

	public void setAllMenuList(List<MenuVO> allMenuList) {
		this.allMenuList = allMenuList;
	}

	public String[] getNewMenuIds() {
		return newMenuIds;
	}

	public void setNewMenuIds(String[] newMenuIds) {
		this.newMenuIds = newMenuIds;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PrivilegeModelVO getPrivilegeModelVO() {
		return privilegeModelVO;
	}

	public void setPrivilegeModelVO(PrivilegeModelVO privilegeModelVO) {
		this.privilegeModelVO = privilegeModelVO;
	}
}
