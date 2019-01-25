package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.rmi.bonus.PfbxBonusSetSpecialVO;

public interface IPfbxBonusSetSpecialService extends IBaseService<PfbxBonusSetSpecial, Integer>{

	public PfbxBonusSetSpecial findPfbxBonusSet(String pfbId, Date todayDate);
	
	public List<PfbxBonusSetSpecialVO> findPfbxBonusSetSpecialList(String pfbId);
	
	public Map<String, String> findPfbxBonusSetSpecialCountMap();
	
	public PfbxBonusSetSpecial getPfbxBonusSetSpecial(Integer id);
	
	public Map<String, Object> getPfbxBonusSetSpecialNow(Date nowDate);
	
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialbyDate(String pfbId, Date startDate, Date endDate);
}
