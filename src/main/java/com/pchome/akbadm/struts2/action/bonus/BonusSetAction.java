package com.pchome.akbadm.struts2.action.bonus;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.contract.EnumContractStatus;

public class BonusSetAction extends BaseAction{

	private IPfdContractService pfdContractService;
	private List<PfdContract> pfdContract;
	private EnumContractStatus[] enumContractStatus = EnumContractStatus.values();
	
	public String execute() {
		
		pfdContract = pfdContractService.findPfdContract();

		return SUCCESS;
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public List<PfdContract> getPfdContract() {
		return pfdContract;
	}

	public EnumContractStatus[] getEnumContractStatus() {
		return enumContractStatus;
	}
	
}
