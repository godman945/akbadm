package com.pchome.akbadm.struts2.action.bank;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.Bank;
import com.pchome.akbadm.db.service.bank.IBankService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class BankAction extends BaseCookieAction{

	private IBankService bankService;
	
	private String bankCode;
	private String bankName;
	private String branchCode;
	private String branchName;
	
	
	private List<Bank> banks;
	
	public String execute(){
		
		log.info("execute");
		
		return SUCCESS;
	}
	
	public String addBankAction(){
		
		Date today = new Date();
		
		log.info(">> bankCode: "+bankCode);
		log.info(">> bankName: "+bankName);		
		log.info(">> branchCode: "+branchCode);
		log.info(">> branchName: "+branchName);
		
		if(StringUtils.isNotBlank(bankCode) && StringUtils.isNotBlank(bankName) &&
				StringUtils.isBlank(branchCode) && StringUtils.isBlank(branchName)){

			// 建立本行
			Bank bank = new Bank();
			
			bank.setBankCode(bankCode);
			bank.setBankName(bankName);			
		
			bank.setCreateDate(today);
			bank.setUpdateDate(today);
			
			bankService.saveOrUpdate(bank);
		}
		
		if(StringUtils.isNotBlank(bankCode) && StringUtils.isNotBlank(bankName) &&
				StringUtils.isNotBlank(branchCode) && StringUtils.isNotBlank(branchName)){
			
			// 建立分行
			Bank mainbank = bankService.findMainBank(bankCode);
			String parentBank = mainbank.getBankCode();
			
			Bank bank = new Bank();
			
			bank.setBankCode(branchCode);
			bank.setBankName(branchName);			
			bank.setParentBank(parentBank);			
			bank.setCreateDate(today);
			bank.setUpdateDate(today);
			
			bankService.saveOrUpdate(bank);
		}
		
		banks = bankService.findBank();
		
		return SUCCESS;
	}

	public void setBankService(IBankService bankService) {
		this.bankService = bankService;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public List<Bank> getBanks() {
		return banks;
	}
	
}
