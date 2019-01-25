package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public interface IPfpAdClickDAO extends IBaseDAO<PfpAdClick, String> {
    public List<PfpAdClick> findPfpAdClick(Date recordDate, int recordTime, int maliceType);

    public List<Object[]> findCostSum(Date recordDate, int recordTime);
    
    public List<PfpAdClick> findPfpAdClickByTraffic(List<Long> adClickIdList);
    
    public List<PfpAdClickVO> findMaliceClick(final Map<String, String> conditionMap);
}
