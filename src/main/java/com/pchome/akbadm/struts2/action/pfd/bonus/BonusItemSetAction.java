package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.contract.EnumContractStatus;

public class BonusItemSetAction extends BaseAction{

	private IPfdContractService pfdContractService;
	
	private List<PfdContract> pfdContracts;					// 經銷商合約列表
	
	private EnumContractStatus[] enumContractStatus = EnumContractStatus.values();
	
	public String execute() {
		
		pfdContracts = pfdContractService.findPfdContract();

		return SUCCESS;
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public List<PfdContract> getPfdContracts() {
		return pfdContracts;
	}

	public EnumContractStatus[] getEnumContractStatus() {
		return enumContractStatus;
	}

	
}
