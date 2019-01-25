package com.pchome.akbadm.struts2.action.ad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;




import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfpAdCategoryNew;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryNewService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class PfpAdCategoryNewAction extends BaseCookieAction {

	private static final long serialVersionUID = 1L;
	
	private IPfpAdCategoryNewService pfpAdCategoryNewService;
	
	private String parentId = "";			//父層編號
	private String parentName = "無";		//父層名稱
	private String categoryName;
	private Integer level;
	private String updateId;
	
	private List<PfpAdCategoryNew> dataList;
	
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	
	public String getAdCategoryList(){
		dataList = new ArrayList<PfpAdCategoryNew>();
		dataList = pfpAdCategoryNewService.findChildByCode(parentId);
		
		return SUCCESS;
	}
	
	//新增分類
	public String addAdCategory(){
		
		level = 1;
		Integer newId = 1;
		newId = pfpAdCategoryNewService.getNewId();
		String newCode = getNewCode(newId);
		
		PfpAdCategoryNew data = new PfpAdCategoryNew();
		
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
		
		pfpAdCategoryNewService.saveOrUpdate(data);
		
		return SUCCESS;
	}
	
	//修改分類
	public String updateAdCategory(){
		
		PfpAdCategoryNew data = new PfpAdCategoryNew();
		data = pfpAdCategoryNewService.findById(updateId);
		
		if(data != null){
			data.setName(categoryName);
			data.setUpdateDate(new Date());
			data.setCreateDate(new Date());
			
			pfpAdCategoryNewService.saveOrUpdate(data);
		}
		
		return SUCCESS;
	}
	
	private String getNewCode(Integer id){
		String newCode = "";
		
		if(StringUtils.isNotEmpty(parentId)){
			PfpAdCategoryNew parentData = new PfpAdCategoryNew();
			parentData = pfpAdCategoryNewService.findById(parentId);
			newCode += parentData.getCode().substring(0, parentData.getLevel()*4);
			level += parentData.getLevel();
		}
		
		newCode += String.format("%04d", id);
		
		for(int i=newCode.length();i<16;i++){
			newCode += "0";
		}
		
		return newCode;
	}
	
	public void setPfpAdCategoryNewService(IPfpAdCategoryNewService pfpAdCategoryNewService) {
		this.pfpAdCategoryNewService = pfpAdCategoryNewService;
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

	public List<PfpAdCategoryNew> getDataList() {
		return dataList;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	
}
