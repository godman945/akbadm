package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdClickDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public class PfpAdClickService extends BaseService<PfpAdClick, String> implements IPfpAdClickService {
    @Override
    public List<PfpAdClick> findPfpAdClick(Date recordDate, int recordTime, int maliceType) {
        return ((IPfpAdClickDAO) dao).findPfpAdClick(recordDate, recordTime, maliceType);
    }

    @Override
    public Map<String, Float> findCostSum(Date recordDate, int recordTime) {
        Map<String, Float> map = new HashMap<String, Float>();

        List<Object[]> list = ((IPfpAdClickDAO) dao).findCostSum(recordDate, recordTime);
        for (Object[] objs: list) {
            if (objs.length < 2) {
                continue;
            }

            map.put((String) objs[0], (Float) objs[1]);
        }

        return map;
    }
    
    @Override
    public List<PfpAdClick> findPfpAdClickByTraffic(List<Long> adClickIdList) {
    	return ((IPfpAdClickDAO) dao).findPfpAdClickByTraffic(adClickIdList);
    }
    
    @Override
    public List<PfpAdClickVO> findMaliceClick(final Map<String, String> conditionMap) {
    	return ((IPfpAdClickDAO) dao).findMaliceClick(conditionMap);
    }
}
