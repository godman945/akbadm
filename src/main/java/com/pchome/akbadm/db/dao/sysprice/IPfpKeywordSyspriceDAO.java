package com.pchome.akbadm.db.dao.sysprice;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpKeywordSysprice;

public interface IPfpKeywordSyspriceDAO extends IBaseDAO<PfpKeywordSysprice, String> {
    public PfpKeywordSysprice getKeywordSysprice(String keyword) throws Exception;

    public List<PfpKeywordSysprice> getKeywordSyspriceList() throws Exception;

    public List<PfpKeywordSysprice> selectKeywordSyspriceByKeyword(String keyword, int firstResult, int maxResults);
}