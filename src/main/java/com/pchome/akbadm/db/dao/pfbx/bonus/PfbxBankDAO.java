package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.config.TestConfig;

public class PfbxBankDAO extends BaseDAO <PfbxBank, Integer> implements IPfbxBankDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfbxBank> getListByCustomerId(String customerId)
	{
		StringBuffer hql = new StringBuffer();
		List<Object> Condition = new ArrayList<Object>();
		
		hql.append("from PfbxBank where pfbxCustomerInfo.customerInfoId = ? and deleteFlag = ?");
		
		Condition.add(customerId);
		Condition.add("0");
		
		return super.getHibernateTemplate().find(hql.toString() , Condition.toArray());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PfbxBank getPfbxMainUseBank(String pfbId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfbxBank where 1 = 1 ");
		hql.append(" and pfbxCustomerInfo.customerInfoId = ? ");
		hql.append(" and mainUse = 'Y' ");
		hql.append(" and deleteFlag  = 0 ");
		
		list.add(pfbId);
		
		List<PfbxBank> bankList=null;
		PfbxBank pfbxBank=null;
		bankList=super.getHibernateTemplate().find(hql.toString(),list.toArray());
		
		if(bankList.size()>0){
			pfbxBank=bankList.get(0);
		}
		
		return pfbxBank;
	}
	
	public static void main(String args[]) throws Exception{

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfbxBankDAO daoa = (PfbxBankDAO)context.getBean("PfbxBankDAO");
		
    	System.out.println(daoa.getPfbxMainUseBank("PFBC201504202").getBankName());
		
	}
	
	

}
