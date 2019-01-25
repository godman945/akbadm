package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdVideoReport;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public interface IPfpAdVideoReportService  extends IBaseService<PfpAdVideoReport, String> {
    public int selectCountByCondition(Map<String, Object> conditionMap);

    public List<PfpAdVideoReportVO> selectByCondition(Map<String, Object> conditionMap, int firstResult, int maxResults);
}
