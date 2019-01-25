package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdKeywordPvclkDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.utils.ConvertUtil;

public class PfpAdKeywordPvclkService extends BaseService<PfpAdKeywordPvclk, String> implements IPfpAdKeywordPvclkService {
    @Override
    public Map<String, int[]> selectPfpAdKeywordPvclkSums(Date pvclkDate) {
        Map<String, int[]> map = new HashMap<String, int[]>();

        List<Object[]> list = ((IPfpAdKeywordPvclkDAO) dao).selectPfpAdKeywordPvclkSums(pvclkDate);
        String id = null;
        int[] sums = null;

        for (Object[] objs: list) {
            // ad_keyword_seq
            id = (String) objs[0];

            // sum(ad_keyword_pv), sum(ad_keyword_clk)
            sums = new int[objs.length-1];
            for (int j = 1; j < objs.length; j++) {
                sums[j-1] = ConvertUtil.convertInteger(objs[j]);
            }

            map.put(id, sums);
        }

        return map;
    }

    @Override
    public int[] selectPfpAdKeywordPvclkSum(String seq, Date pvclkDate) {
        return ((IPfpAdKeywordPvclkDAO) dao).selectPfpAdKeywordPvclkSum(seq, pvclkDate);
    }

    @Override
    public List<PfpAdKeywordPvclk> selectPfpAdKeywordPvclk(int firstResult, int maxResults) {
        return ((IPfpAdKeywordPvclkDAO)dao).selectPfpAdKeywordPvclk(firstResult, maxResults);
    }

    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        return ((IPfpAdKeywordPvclkDAO)dao).deleteMalice(recordDate, recordTime);
    }
}