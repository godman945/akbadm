package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public interface IPfbxSizeGetAdClickService extends IBaseService<PfpAdClickVO, String> {
	public List<PfpAdClickVO> getAdClickMouseDownList(final Map<String, String> conditionMap);
}
