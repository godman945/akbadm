package com.pchome.akbadm.db.service.pfbx.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.report.IPfbxSizeGetAdClickDAO;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public class PfbxSizeGetAdClickService extends BaseService<PfpAdClickVO, String> implements IPfbxSizeGetAdClickService  {
	
	public List<PfpAdClickVO> getAdClickMouseDownList(final Map<String, String> conditionMap){
		return ((IPfbxSizeGetAdClickDAO) dao).getAdClickMouseDownList(conditionMap);
	}
	
}
