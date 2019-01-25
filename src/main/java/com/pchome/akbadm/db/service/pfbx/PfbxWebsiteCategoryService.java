package com.pchome.akbadm.db.service.pfbx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.IPfbxWebsiteCategoryDAO;
import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxWebsiteCategoryService extends BaseService<PfbxWebsiteCategory,String> implements IPfbxWebsiteCategoryService {
	@Override
	public Map<String, List<Map<String, String>>> findPfbxWebsiteCategoryAll() {
		List<PfbxWebsiteCategory> pfbxWebsiteCategoryList = ((IPfbxWebsiteCategoryDAO) dao).findPfbxWebsiteCategoryAll();
    	Map<String, List<PfbxWebsiteCategory>> pfbxWebsiteCategoryMap = new HashMap<String, List<PfbxWebsiteCategory>>();

    	// 取父類別
    	for (int i = 0; i <= pfbxWebsiteCategoryList.size() - 1; i++) {
		    if (pfbxWebsiteCategoryList.get(i).getParentId().isEmpty()) {
		    	List<PfbxWebsiteCategory> a = new ArrayList<PfbxWebsiteCategory>();
		    	a.add(pfbxWebsiteCategoryList.get(i));
		    	pfbxWebsiteCategoryMap.put(pfbxWebsiteCategoryList.get(i).getName(),a);
		    } else {

				for (Object key : pfbxWebsiteCategoryMap.keySet()) {
				    for (int j = 0; j <= pfbxWebsiteCategoryMap.get(key).size() - 1; j++) {
						if(pfbxWebsiteCategoryList.get(i).getParentId().equals(String.valueOf(pfbxWebsiteCategoryMap.get(key).get(j).getId()))){
							pfbxWebsiteCategoryMap.get(key).add(pfbxWebsiteCategoryList.get(i));
						}
				    }
				}
		    }
    	}
    	log.info(" pfbxWebsiteCategoryMap: "+pfbxWebsiteCategoryMap.size());

		//處理回傳格式
		Map<String,List<Map<String,String>>> map =new HashMap<String,List<Map<String,String>>>();
		for (Object key : pfbxWebsiteCategoryMap.keySet()) {


			for (int i = 0; i < pfbxWebsiteCategoryMap.get(key).size(); i++) {

				List<Map<String,String>> LlistMap =new ArrayList<Map<String,String>>();
				Map<String,String>  a = new HashMap<String,String>();
				a.put("level",String.valueOf(pfbxWebsiteCategoryMap.get(key).get(i).getLevel()));
				a.put("code", pfbxWebsiteCategoryMap.get(key).get(i).getCode());
				a.put("name", pfbxWebsiteCategoryMap.get(key).get(i).getName());
				a.put("parentId", pfbxWebsiteCategoryMap.get(key).get(i).getParentId());
				a.put("id", String.valueOf(pfbxWebsiteCategoryMap.get(key).get(i).getId()));
				LlistMap.add(a);

				if(map.get(key)!=null){
					map.get(key).addAll(LlistMap);
				}else{
					map.put(key.toString(), LlistMap);
				}
			}
		}

		log.info(" map: "+map.size());

		return map;
	}

	@Override
	public Map<String, String> getPfbxWebsiteCategoryNameByCodeMap() {
		Map<String,String>  pfbxWebsiteCategoryMap = new HashMap<String,String>();
    	List<PfbxWebsiteCategory> pfbxWebsiteCategoryList = ((IPfbxWebsiteCategoryDAO) dao).findPfbxWebsiteCategoryAll();

    	for(PfbxWebsiteCategory pfbxWebsiteCategory:pfbxWebsiteCategoryList){
    		pfbxWebsiteCategoryMap.put(pfbxWebsiteCategory.getCode(), pfbxWebsiteCategory.getName());
    	}

    	return pfbxWebsiteCategoryMap;
	}

	@Override
	public List<PfbxWebsiteCategory> getPfbxWebsiteCategoryAll() {
		return ((IPfbxWebsiteCategoryDAO) dao).findPfbxWebsiteCategoryAll();
	}

	@Override
	public PfbxWebsiteCategory findByCode(String code) {
		PfbxWebsiteCategory pfbxWebsiteCategory = null;

        List<PfbxWebsiteCategory> list = ((IPfbxWebsiteCategoryDAO) dao).findByCode(code);
        if (list.size() > 0) {
        	pfbxWebsiteCategory = list.get(0);
        }

        return pfbxWebsiteCategory;
	}

	@Override
	public List<PfbxWebsiteCategory> findChildByCode(String parentId) {
		return ((IPfbxWebsiteCategoryDAO) dao).findChildByCode(parentId);
	}

	@Override
	public PfbxWebsiteCategory findById(String id) {
		PfbxWebsiteCategory pfbxWebsiteCategory = null;

        List<PfbxWebsiteCategory> list = ((IPfbxWebsiteCategoryDAO) dao).findById(id);
        if (list.size() > 0) {
        	pfbxWebsiteCategory = list.get(0);
        }

        return pfbxWebsiteCategory;
	}

	@Override
	public Integer getNewId() {
		return ((IPfbxWebsiteCategoryDAO) dao).getNewId();
	}

	@Override
	public List<PfbxWebsiteCategory> getFirstLevelPfpPfbxWebsiteCategory() {
		return ((IPfbxWebsiteCategoryDAO) dao).getFirstLevelPfbxWebsiteCategory();
	}
}
