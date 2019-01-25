package com.pchome.akbadm.db.service.ad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.ad.IPfpAdCategoryNewDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategoryNew;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.struts2.ajax.ad.PfpAdCategoryNewAjaxAction;

public class PfpAdCategoryNewService extends BaseService<PfpAdCategoryNewAjaxAction, String> implements IPfpAdCategoryNewService {
    // 取得全部資料進行分類
    @Override
    @Transactional
    public Map<String, List<Map<String, String>>> findPfpAdCategoryNewAll() {

    	//log.info(">>>PfpAdCategoryNewService findPfpAdCategoryNewAll()");

    	List<PfpAdCategoryNew> PfpAdCategoryNewList = ((IPfpAdCategoryNewDAO) dao).findPfpAdCategoryNewAll();
    	Map<String, List<PfpAdCategoryNew>> pfpAdCategoryNewMap = new HashMap<String, List<PfpAdCategoryNew>>();

    	// 取父類別
    	for (int i = 0; i <= PfpAdCategoryNewList.size() - 1; i++) {
		    if (PfpAdCategoryNewList.get(i).getParentId().isEmpty()) {
		    	List<PfpAdCategoryNew> a = new ArrayList<PfpAdCategoryNew>();
		    	a.add(PfpAdCategoryNewList.get(i));
		    	pfpAdCategoryNewMap.put(PfpAdCategoryNewList.get(i).getName(),a);
		    } else {

				for (Object key : pfpAdCategoryNewMap.keySet()) {
				    for (int j = 0; j <= pfpAdCategoryNewMap.get(key).size() - 1; j++) {
					if(PfpAdCategoryNewList.get(i).getParentId().equals(String.valueOf(pfpAdCategoryNewMap.get(key).get(j).getId()))){
					    pfpAdCategoryNewMap.get(key).add(PfpAdCategoryNewList.get(i));
					}
				    }
				}
		    }
    	}
    	log.info(" pfpAdCategoryNewMap: "+pfpAdCategoryNewMap.size());

		//處理回傳格式
		Map<String,List<Map<String,String>>> map =new HashMap<String,List<Map<String,String>>>();
		for (Object key : pfpAdCategoryNewMap.keySet()) {


			for (int i = 0; i < pfpAdCategoryNewMap.get(key).size(); i++) {

				List<Map<String,String>> LlistMap =new ArrayList<Map<String,String>>();
				Map<String,String>  a = new HashMap<String,String>();
				a.put("level",String.valueOf(pfpAdCategoryNewMap.get(key).get(i).getLevel()));
				a.put("code", pfpAdCategoryNewMap.get(key).get(i).getCode());
				a.put("name", pfpAdCategoryNewMap.get(key).get(i).getName());
				a.put("parentId", pfpAdCategoryNewMap.get(key).get(i).getParentId());
				a.put("id", String.valueOf(pfpAdCategoryNewMap.get(key).get(i).getId()));
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

    public List<PfpAdCategoryNew> getPfpAdCategoryNewAll() {
    	return ((IPfpAdCategoryNewDAO) dao).findPfpAdCategoryNewAll();
    }

    @Override
    public Map<String,String> getPfpAdCategoryNewNameByCodeMap(){
    	Map<String,String>  PfpAdCategoryNewMap = new HashMap<String,String>();
    	List<PfpAdCategoryNew> PfpAdCategoryNewList = ((IPfpAdCategoryNewDAO) dao).findPfpAdCategoryNewAll();

    	for(PfpAdCategoryNew pfpAdCategoryNew:PfpAdCategoryNewList){
    		PfpAdCategoryNewMap.put(pfpAdCategoryNew.getCode(), pfpAdCategoryNew.getName());
    	}

    	return PfpAdCategoryNewMap;
    }

    @Override
    public PfpAdCategoryNew findByCode(String code) {
        PfpAdCategoryNew pfpAdCategoryNew = null;

        List<PfpAdCategoryNew> list = ((IPfpAdCategoryNewDAO) dao).findByCode(code);
        if (list.size() > 0) {
            pfpAdCategoryNew = list.get(0);
        }

        return pfpAdCategoryNew;
    }
    
    @Override
    public List<PfpAdCategoryNew> findChildByCode(String parentId) {
    	return ((IPfpAdCategoryNewDAO) dao).findChildByCode(parentId);
    }
    
    @Override
    public PfpAdCategoryNew findById(String id) {
    	PfpAdCategoryNew pfpAdCategoryNew = null;

        List<PfpAdCategoryNew> list = ((IPfpAdCategoryNewDAO) dao).findById(id);
        if (list.size() > 0) {
            pfpAdCategoryNew = list.get(0);
        }

        return pfpAdCategoryNew;
    }
    
    @Override
    public Integer getNewId() {
    	return ((IPfpAdCategoryNewDAO) dao).getNewId();
    }
    
    @Override
    public void saveOrUpdate(PfpAdCategoryNew pfpAdCategoryNew) {
    	((IPfpAdCategoryNewDAO) dao).saveOrUpdate(pfpAdCategoryNew);
    }
    
    @Override
    public List<PfpAdCategoryNew> getFirstLevelPfpAdCategoryNew() {
    	return ((IPfpAdCategoryNewDAO) dao).getFirstLevelPfpAdCategoryNew();
    }
}
