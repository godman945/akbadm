package com.pchome.akbadm.db.service.sysprice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.sysprice.IPfpKeywordSyspriceDAO;
import com.pchome.akbadm.db.dao.sysprice.PfpKeywordSyspriceDAO;
import com.pchome.akbadm.db.pojo.PfpKeywordSysprice;
import com.pchome.akbadm.db.service.BaseService;

public class PfpKeywordSyspriceService extends BaseService<PfpKeywordSysprice, String> implements IPfpKeywordSyspriceService {
	private String definePrice;

    @Override
    public PfpKeywordSysprice getKeywordSysprice(String keyword) throws Exception{

        PfpKeywordSysprice keywordSysprice = ((PfpKeywordSyspriceDAO) dao).getKeywordSysprice(keyword);

        if(keywordSysprice == null){

            keywordSysprice = new PfpKeywordSysprice();
            keywordSysprice.setKeyword(keyword);
            keywordSysprice.setSysprice(Float.parseFloat(definePrice));	// 廣告預設價錢
            keywordSysprice.setAmount(0);

            super.saveOrUpdate(keywordSysprice);
        }

        return keywordSysprice;
    }

    @Override
    public List<PfpKeywordSysprice> getKeywordSyspriceList() throws Exception{
        return ((PfpKeywordSyspriceDAO) dao).getKeywordSyspriceList();
    }

    @Override
    public Map<String, Float> getKeywordMap() {
        List<PfpKeywordSysprice> list = ((IPfpKeywordSyspriceDAO) dao).loadAll();
        Map<String, Float> map = new HashMap<String, Float>();

        for (PfpKeywordSysprice pfpKeywordSysprice: list) {
            map.put(pfpKeywordSysprice.getKeyword(), pfpKeywordSysprice.getSysprice());
        }

        return map;
    }

    @Override
    public PfpKeywordSysprice selectKeywordSyspriceByKeyword(String keyword, int firstResult, int maxResults) {
        List<PfpKeywordSysprice> list = ((IPfpKeywordSyspriceDAO) dao).selectKeywordSyspriceByKeyword(keyword, firstResult, maxResults);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

	public void setDefinePrice(String definePrice) {
		this.definePrice = definePrice;
	}
}