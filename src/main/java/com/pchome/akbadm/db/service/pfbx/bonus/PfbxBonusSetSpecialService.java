package com.pchome.akbadm.db.service.pfbx.bonus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusSetSpecialDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.rmi.bonus.PfbxBonusSetSpecialVO;

public class PfbxBonusSetSpecialService extends BaseService<PfbxBonusSetSpecial, Integer> implements IPfbxBonusSetSpecialService{
	
	public PfbxBonusSetSpecial findPfbxBonusSet(String pfbId, Date todayDate) {
		
		List<PfbxBonusSetSpecial> list =  ((IPfbxBonusSetSpecialDAO)dao).findPfbxBonusSets(pfbId, todayDate);
		
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
		
	}

	public List<PfbxBonusSetSpecialVO> findPfbxBonusSetSpecialList(String pfbId) {
		List<PfbxBonusSetSpecialVO> PfbxBonusSetSpecialVOList = new ArrayList<PfbxBonusSetSpecialVO>();
		List<PfbxBonusSetSpecial> list = ((IPfbxBonusSetSpecialDAO)dao).findPfbxBonusSetSpecialListByFlag(pfbId);
		
		for(PfbxBonusSetSpecial pfbxBonusSetSpecial:list){
			PfbxBonusSetSpecialVO pfbxBonusSetSpecialVO = new PfbxBonusSetSpecialVO();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd 24H:mm:ss");
			pfbxBonusSetSpecialVO = new PfbxBonusSetSpecialVO();
			pfbxBonusSetSpecialVO.setId(pfbxBonusSetSpecial.getId());
			pfbxBonusSetSpecialVO.setSpecialName(pfbxBonusSetSpecial.getSpecialName());
			pfbxBonusSetSpecialVO.setPfbId(pfbxBonusSetSpecial.getPfbId());
			pfbxBonusSetSpecialVO.setStartDate(sdf.format(pfbxBonusSetSpecial.getStartDate()));
			pfbxBonusSetSpecialVO.setEndDate(sdf.format(pfbxBonusSetSpecial.getEndDate()));
			pfbxBonusSetSpecialVO.setPfbPercent(pfbxBonusSetSpecial.getPfbPercent());
			pfbxBonusSetSpecialVO.setPchomeChargePercent(pfbxBonusSetSpecial.getPchomeChargePercent());
			pfbxBonusSetSpecialVO.setDeleteFlag(pfbxBonusSetSpecial.getDeleteFlag());
			pfbxBonusSetSpecialVO.setUpdateDate(sdf2.format(pfbxBonusSetSpecial.getUpdateDate()));
			pfbxBonusSetSpecialVO.setCreateDate(sdf2.format(pfbxBonusSetSpecial.getCreateDate()));
			
			PfbxBonusSetSpecialVOList.add(pfbxBonusSetSpecialVO);
		}
		
		return PfbxBonusSetSpecialVOList;
	}
	
	public Map<String, String> findPfbxBonusSetSpecialCountMap() {
		return ((IPfbxBonusSetSpecialDAO)dao).findPfbxBonusSetSpecialCountMap();
	}
	
	public PfbxBonusSetSpecial getPfbxBonusSetSpecial(Integer id){
		return ((IPfbxBonusSetSpecialDAO)dao).getPfbxBonusSetSpecial(id);
	}
	
	public Map<String, Object> getPfbxBonusSetSpecialNow(Date nowDate){
		return ((IPfbxBonusSetSpecialDAO)dao).getPfbxBonusSetSpecialNow(nowDate);
	}
	
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialbyDate(String pfbId, Date startDate, Date endDate){
		return ((IPfbxBonusSetSpecialDAO)dao).findPfbxBonusSetSpecialbyDate(pfbId, startDate, endDate);
	}
}
