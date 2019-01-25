package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.invalidclick.IPfbxInvalidTrafficDetailDAO;
import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficDetailVO;

public class PfbxInvalidTrafficDetailService extends BaseService<PfbxInvalidTrafficDetail,String> implements IPfbxInvalidTrafficDetailService {

	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByCondition(final Map<String, String> conditionMap) {
		return ((IPfbxInvalidTrafficDetailDAO) dao).getInvalidTrafficDetailByCondition(conditionMap);
	}
	
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByDownload(final Map<String, String> conditionMap) {
		return ((IPfbxInvalidTrafficDetailDAO) dao).getInvalidTrafficDetailByDownload(conditionMap);
	}
}
