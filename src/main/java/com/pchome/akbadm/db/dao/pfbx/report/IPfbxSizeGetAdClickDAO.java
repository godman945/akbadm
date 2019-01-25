package com.pchome.akbadm.db.dao.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public interface IPfbxSizeGetAdClickDAO extends IBaseDAO<PfpAdClickVO, String> {
	public List<PfpAdClickVO> getAdClickMouseDownList(final Map<String, String> conditionMap);
}
