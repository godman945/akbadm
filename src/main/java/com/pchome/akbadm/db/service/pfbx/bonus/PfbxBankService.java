package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBankDAO;
import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxBankVo;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckMain;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckStatus;

public class PfbxBankService extends BaseService <PfbxBank, Integer> implements IPfbxBankService{
	
	public List<PfbxBankVo> getPfbxBankVosByCustomerId(String customerId) throws Exception
	{
		List<PfbxBankVo> vos = new ArrayList<PfbxBankVo>(); 
		
		List<PfbxBank> banks = ((IPfbxBankDAO)dao).getListByCustomerId(customerId);
		
		PfbxBankVo vo =null;
		for(PfbxBank bank : banks)
		{
			vo = new PfbxBankVo();
			vo.setId(bank.getId());
			vo.setBankName(bank.getBankName());
			vo.setAccountNumber(bank.getAccountNumber());
			
			//審核狀態組成
			vo.setCheckStatus(bank.getCheckStatus());
			vo.setCheckNote(bank.getCheckNote());
			
			//是否為主要銀行
			if(StringUtils.equals(EnumPfbxCheckMain.MAIN.getCode(), bank.getMainUse()))
			{
				vo.setIsMainBank(EnumPfbxCheckMain.MAIN.getcName());
			}
			else
			{
				vo.setIsMainBank("");
			}
			
			vos.add(vo);
		}
		
		return vos;
	}
	
	@Override
	public PfbxBank getPfbxMainUseBank(String pfbId) {
		return ((IPfbxBankDAO) dao).getPfbxMainUseBank(pfbId);
	}

}
