package com.pchome.akbadm.db.service.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficDetailVO;

public interface IPfbxInvalidTrafficDetailService extends IBaseService<PfbxInvalidTrafficDetail,String> {
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByCondition(final Map<String, String> conditionMap);
	
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByDownload(final Map<String, String> conditionMap);
}
