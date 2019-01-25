package com.pchome.akbadm.db.dao.bank;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.Bank;

public interface IBankDAO extends IBaseDAO<Bank, Integer>{

	public List<Bank> findBank();
	
	public List<Bank> findMainBank(String code);
}
