package com.pchome.akbadm.db.dao.pfbx.invalidclick;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficDetailVO;

public interface IPfbxInvalidTrafficDetailDAO  extends IBaseDAO<PfbxInvalidTrafficDetail, String> {
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByCondition(final Map<String, String> conditionMap);
	
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByDownload(final Map<String, String> conditionMap);
}
