package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoReport;

public interface IPfpAdVideoReportDAO extends IBaseDAO<PfpAdVideoReport, String> {
    public int selectCountByCondition(Map<String, Object> conditionMap);

    public List<Object[]> selectByCondition(Map<String, Object> conditionMap, int firstResult, int maxResults);
}
