package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;

public interface IPfpAdKeywordPvclkDAO extends IBaseDAO<PfpAdKeywordPvclk, String> {
    public List<Object[]> selectPfpAdKeywordPvclkSums(Date pvclkDate);

    public int[] selectPfpAdKeywordPvclkSum(String seq, Date pvclkDate);

    public float selectAdKeywordClkPrice(String pvclkDate, String customerInfoId, String actionId);

    public List<Object[]> selectAdKeywordClkPriceByPfpCustomerInfoId(Date pvclkDate);

    public Map<String, Float> selectAdKeywordClkPriceMapByPfpCustomerInfoId(Date pvclkDate);

    public List<Object[]> selectAdKeywordClkPriceByPfpAdActionId(Date pvclkDate);

    public Map<String, Float> selectAdKeywordClkPriceMapByPfpAdActionId(Date pvclkDate);

    public List<PfpAdKeywordPvclk> selectPfpAdKeywordPvclk(int firstResult, int maxResults);

    public int deleteMalice(Date recordDate, int recordTime);
}