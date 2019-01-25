package com.pchome.akbadm.db.dao.privilege;

import java.util.List;

public class PrivilegeModelVO {

	private String modelId;
	private String modelName;
	private String note;
	private List<String> dbMenuIdList; //資料庫讀取出來的舊權限
	private String[] newMenuIds; //更新後的新權限

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
	public String[] getNewMenuIds() {
		return newMenuIds;
	}
	public void setNewMenuIds(String[] newMenuIds) {
		this.newMenuIds = newMenuIds;
	}
}
