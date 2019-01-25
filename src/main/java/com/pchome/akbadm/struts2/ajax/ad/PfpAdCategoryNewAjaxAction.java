package com.pchome.akbadm.struts2.ajax.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.pchome.akbadm.db.service.ad.PfpAdCategoryNewService;
import com.pchome.akbadm.struts2.BaseAction;
public class PfpAdCategoryNewAjaxAction  extends BaseAction {
	private static final long serialVersionUID = 1L;
	private PfpAdCategoryNewService pfpAdCategoryNewService;
	private String result;
	Map<String, List<Map<String, String>>> pfpAdCategoryNewListMap = new HashMap<String, List<Map<String, String>>>();
	
	// 取得全部資料進行分類
	public String getPfpAdCategoryNewAll() throws JSONException {
		//log.info(">>> getPfpAdCategoryNewAll()");
	    pfpAdCategoryNewListMap  = pfpAdCategoryNewService.findPfpAdCategoryNewAll();
	    
		JSONObject jsonObjectJacky = new JSONObject(pfpAdCategoryNewListMap);
		
		result = jsonObjectJacky.toString();
		
		return SUCCESS;
	}

	public PfpAdCategoryNewService getPfpAdCategoryNewService() {
		return pfpAdCategoryNewService;
	}

	public void setPfpAdCategoryNewService(
			PfpAdCategoryNewService pfpAdCategoryNewService) {
		this.pfpAdCategoryNewService = pfpAdCategoryNewService;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	public Map<String, List<Map<String, String>>> getPfpAdCategoryNewListMap() {
	    return pfpAdCategoryNewListMap;
	}
	public void setPfpAdCategoryNewListMap(
		Map<String, List<Map<String, String>>> pfpAdCategoryNewListMap) {
	    this.pfpAdCategoryNewListMap = pfpAdCategoryNewListMap;
	}


}
