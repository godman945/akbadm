package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;

public interface IPfbxBonusSetSpecialDAO extends IBaseDAO<PfbxBonusSetSpecial, Integer>{

	public List<PfbxBonusSetSpecial> findPfbxBonusSets(String pfbId, Date todayDate);
	
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialList(String pfbId);
	
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialListByFlag(String pfbId);
	
	public Map<String, String> findPfbxBonusSetSpecialCountMap();
	
	public PfbxBonusSetSpecial getPfbxBonusSetSpecial(Integer id);
	
	public Map<String, Object> getPfbxBonusSetSpecialNow(Date nowDate);
	
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialbyDate(String pfbId, Date startDate, Date endDate);
	
}
