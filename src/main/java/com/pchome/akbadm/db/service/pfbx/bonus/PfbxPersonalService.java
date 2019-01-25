package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxPersonalDAO;
import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxPersonalVo;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckMain;

public class PfbxPersonalService extends BaseService <PfbxPersonal, Integer> implements IPfbxPersonalService{
	
	public List<PfbxPersonalVo> getPfbxPersonalVosByCustomerId(String customerId) throws Exception
	{
		List<PfbxPersonalVo> vos = new ArrayList<PfbxPersonalVo>();
		List<PfbxPersonal> personals = ((IPfbxPersonalDAO)dao).getListByCustomerId(customerId);
		
		PfbxPersonalVo vo = null;
		for(PfbxPersonal personal : personals)
		{
			vo = new PfbxPersonalVo();
			vo.setId(personal.getId());
			vo.setName(personal.getName());
			vo.setIdCard(personal.getIdCard());
			vo.setCheckStatus(personal.getCheckStatus());
			vo.setCheckNote(personal.getCheckNote());
			
			//是否為主要
			if(StringUtils.equals(EnumPfbxCheckMain.MAIN.getCode(), personal.getMainUse()))
			{
				vo.setIsMainUse(EnumPfbxCheckMain.MAIN.getcName());
			}
			else
			{
				vo.setIsMainUse("");
			}
			
			vos.add(vo);
		}
		
		return vos;
	}
	
	@Override
	public PfbxPersonal getPfbxMainUsePersonal(String pfbId){
		return ((IPfbxPersonalDAO) dao).getPfbxMainUsePersonal(pfbId);
	}

}
