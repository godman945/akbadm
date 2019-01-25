package com.pchome.akbadm.struts2.action.pfbx.website;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;
import com.pchome.akbadm.db.service.pfbx.IPfbxWebsiteCategoryService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class PfbxWebsiteCategoryAction extends BaseCookieAction {

private static final long serialVersionUID = 1L;
	
	private IPfbxWebsiteCategoryService pfbxWebsiteCategoryService;
	
	private String parentId = "";			//父層編號
	private String parentName = "無";		//父層名稱
	private String categoryName;
	private Integer level;
	private String updateId;
	
	private List<PfbxWebsiteCategory> dataList;
	
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	
	public String getWebsiteCategoryList(){
		dataList = new ArrayList<PfbxWebsiteCategory>();
		dataList = pfbxWebsiteCategoryService.findChildByCode(parentId);
		
		return SUCCESS;
	}
	
	//新增分類
	public String addWebsiteCategory(){
		
		level = 1;
		Integer newId = 1;
		newId = pfbxWebsiteCategoryService.getNewId();
		String newCode = getNewCode(newId);
		
		PfbxWebsiteCategory data = new PfbxWebsiteCategory();
		
		//data.setId(newId);
		
		if(StringUtils.isNotEmpty(parentId)){
			data.setParentId(parentId);
		} else {
			data.setParentId("");
		}
		
		data.setCode(newCode);
		data.setName(categoryName);
		data.setLevel(level);
		data.setUpdateDate(new Date());
		data.setCreateDate(new Date());
		
		pfbxWebsiteCategoryService.saveOrUpdate(data);
		
		return SUCCESS;
	}
	
	//修改分類
	public String updateWebsiteCategory(){
		
		PfbxWebsiteCategory data = new PfbxWebsiteCategory();
		data = pfbxWebsiteCategoryService.findById(updateId);
		
		if(data != null){
			data.setName(categoryName);
			data.setUpdateDate(new Date());
			data.setCreateDate(new Date());
			
			pfbxWebsiteCategoryService.saveOrUpdate(data);
		}
		
		return SUCCESS;
	}
	
	private String getNewCode(Integer id){
		String newCode = "";
		
		if(StringUtils.isNotEmpty(parentId)){
			PfbxWebsiteCategory parentData = new PfbxWebsiteCategory();
			parentData = pfbxWebsiteCategoryService.findById(parentId);
			newCode += parentData.getCode().substring(0, parentData.getLevel()*5);
			level += parentData.getLevel();
		}
		
		newCode += String.format("%05d", id);
		
		for(int i=newCode.length();i<20;i++){
			newCode += "0";
		}
		
		return newCode;
	}
	
	public void setPfbxWebsiteCategoryService(IPfbxWebsiteCategoryService pfbxWebsiteCategoryService) {
		this.pfbxWebsiteCategoryService = pfbxWebsiteCategoryService;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public List<PfbxWebsiteCategory> getDataList() {
		return dataList;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
}
