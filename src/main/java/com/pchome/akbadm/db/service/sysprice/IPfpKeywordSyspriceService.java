package com.pchome.akbadm.db.service.sysprice;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpKeywordSysprice;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpKeywordSyspriceService extends IBaseService<PfpKeywordSysprice, String> {
    public PfpKeywordSysprice getKeywordSysprice(String keyword) throws Exception;

    public List<PfpKeywordSysprice> getKeywordSyspriceList() throws Exception;

    public Map<String, Float> getKeywordMap();

    public PfpKeywordSysprice selectKeywordSyspriceByKeyword(String keyword, int firstResult, int maxResults);
}