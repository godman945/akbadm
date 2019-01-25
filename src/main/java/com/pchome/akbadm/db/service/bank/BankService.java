package com.pchome.akbadm.db.service.bank;

import java.util.List;

import com.pchome.akbadm.db.dao.bank.IBankDAO;
import com.pchome.akbadm.db.pojo.Bank;
import com.pchome.akbadm.db.service.BaseService;

public class BankService extends BaseService<Bank, Integer> implements IBankService{

	public List<Bank> findBank() {
		return ((IBankDAO) dao).findBank();
	}
	
	public Bank findMainBank(String code){
		List<Bank> list = ((IBankDAO) dao).findMainBank(code);
		log.info(list);
		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}
}
