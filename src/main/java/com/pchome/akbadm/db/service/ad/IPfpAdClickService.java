package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public interface IPfpAdClickService extends IBaseService<PfpAdClick, String> {
    public List<PfpAdClick> findPfpAdClick(Date recordDate, int recordTime, int maliceType);

    public Map<String, Float> findCostSum(Date recordDate, int recordTime);
    
    public List<PfpAdClick> findPfpAdClickByTraffic(List<Long> adClickIdList);
    
    public List<PfpAdClickVO> findMaliceClick(final Map<String, String> conditionMap);
}
