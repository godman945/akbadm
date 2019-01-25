package com.pchome.akbadm.db.service.bank;

import java.util.List;

import com.pchome.akbadm.db.pojo.Bank;
import com.pchome.akbadm.db.service.IBaseService;

public interface IBankService extends IBaseService<Bank, Integer>{

	public List<Bank> findBank();
	
	public Bank findMainBank(String code);
}
