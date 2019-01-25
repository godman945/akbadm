package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdKeywordPvclkService extends IBaseService<PfpAdKeywordPvclk, String> {
    public Map<String, int[]> selectPfpAdKeywordPvclkSums(Date pvclkDate);

    public int[] selectPfpAdKeywordPvclkSum(String seq, Date pvclkDate);

    public List<PfpAdKeywordPvclk> selectPfpAdKeywordPvclk(int firstResult, int maxResults);

    public int deleteMalice(Date recordDate, int recordTime);
}
