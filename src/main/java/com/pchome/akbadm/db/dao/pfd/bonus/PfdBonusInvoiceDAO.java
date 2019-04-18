package com.pchome.akbadm.db.dao.pfd.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusBill;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfdBonusInvoiceDAO extends BaseDAO <PfdBonusInvoice, Integer> implements IPfdBonusInvoiceDAO{

	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month, String payType){
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and closeYear = ? and closeMonth = ? ");
		hql.append(" and payType = ? ");
		hql.append(" order by id ");
		
		list.add(pfdId);
		list.add(year);
		list.add(month);
		list.add(payType);
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findPfdBonusInvoice(String pfdId, int year, int month){
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and closeYear = ? and closeMonth = ? ");
		hql.append(" order by id ");
		
		list.add(pfdId);
		list.add(year);
		list.add(month);
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findPfdLastInvoiceByMonthPayment(String pfdId, int closeYear, int closeMonth){
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and closeYear = ? and closeMonth = ? ");
		hql.append(" order by id desc");
		
		list.add(pfdId);
		list.add(closeYear);
		list.add(closeMonth);
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findPfdBonusInvoices(String pfdId, Date strDate, Date endDate){
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		
		if(StringUtils.isNotBlank(pfdId)){
			hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
			list.add(pfdId);
		}
		
		hql.append(" and recordDate between ? and ? ");
		hql.append(" order by id ");
		
		list.add(strDate);
		list.add(endDate);
		
		log.info(" hql: "+hql);
		
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	public PfdBonusInvoice findPfdBonusInvoice(int id) {
		return super.get(id);
	}
	
	public float findNonCloseBonusMoney(String pfdId, String payType, Date applyDate,Integer payId){
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select sum(totalBonus) ");
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType ");	

		hql.append(" and billStatus = :billStatus ");	
		hql.append(" and recordDate <= :applyDate ");
		
		if(payId == null){
			hql.append(" and payId is null ");
		} else {
			hql.append(" and payId = " + payId);
		}
		
		hql.append(" order by id desc ");		
		
		Double result = (Double) this.getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery(hql.toString())
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setString("billStatus", EnumPfdBonusBill.NOT_APPLY.getStatus())
                .setDate("applyDate", applyDate)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findNonCloseBonus(String pfdId, String payType, Date applyDate) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
			
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and payId is null ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");		
		hql.append(" and payType = ? ");	

		hql.append(" and billStatus = ? ");	
		hql.append(" and recordDate <= ? ");
		
		list.add(pfdId);
		list.add(payType);
		list.add(EnumPfdBonusBill.NOT_APPLY.getStatus());
		list.add(applyDate);
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());	
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,PfdBonusInvoiceVO> getCloseTheLastInvoice(){
		Map<String,PfdBonusInvoiceVO> map = new HashMap<String,PfdBonusInvoiceVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select distinct contract.pfd_customer_info_id, a.close_year, a.close_month ");
		hql.append(" from pfd_bonus_invoice a ");
		hql.append(" join pfd_contract contract ");
		hql.append(" on a.pfd_contract = contract.pfd_contract_id ");
		hql.append(" where 1 = 1 ");
		hql.append(" and concat(a.close_year,if(length(a.close_month)=1,concat(0,a.close_month),a.close_month)) = ");
		hql.append(" (select max(concat(b.close_year,if(length(b.close_month)=1,concat(0,b.close_month),b.close_month))) from pfd_bonus_invoice b where b.pfd_contract  = a.pfd_contract) ");
		hql.append(" and contract.pfd_customer_info_id not in ");
		hql.append(" (select distinct(pfd_customer_info_id) from pfd_contract where status = '1' ) order by a.id ");
		
		String sqlStr = hql.toString();
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
		List<Object> dataList = query.list();
		
		for (Object object : dataList) {
			Object[] objArray = (Object[]) object;
			PfdBonusInvoiceVO vo = new PfdBonusInvoiceVO();
			
			String pfdId = objArray[0].toString();
			int closeYear = Integer.parseInt(objArray[1].toString());
			int closeMonth = Integer.parseInt(objArray[2].toString());
			
			vo.setCloseYear(closeYear);
			vo.setCloseMonth(closeMonth);
			
			map.put(pfdId, vo);
		}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusInvoice> findPfdBonusInvoiceByContract(String pfdId, int year, int month, String payType, String contractId) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdContract.pfdContractId = ? ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and closeYear = ? and closeMonth = ? ");
		hql.append(" and payType = ? ");
		hql.append(" order by id ");
		
		list.add(contractId);
		list.add(pfdId);
		list.add(year);
		list.add(month);
		list.add(payType);
		
		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecordInvoiceVO> findPfdBonusInvoicesByPayment(String pfdId, String strDate, String endDate)throws Exception{
		//adm請款明細
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" a.id, ");  
		sql.append(" a.pfd_contract, ");  
		sql.append(" a.pay_type, ");  
		sql.append(" a.bonus_item, ");  
		sql.append(" a.quarter, ");  
		sql.append(" a.now_clk_price, ");  
		sql.append(" a.now_bonus, ");  
		sql.append(" b.record_date, ");  
		sql.append(" b.close_year, ");  
		sql.append(" b.close_month, ");  
		sql.append(" COALESCE(b.bonus_type,''), ");  
		sql.append(" b.download, ");  
		sql.append(" b.bill_status, ");  
		sql.append(" COALESCE(b.bill_note,''), ");  
		sql.append(" COALESCE(b.finance_invoice_sno,''), ");  
		sql.append(" b.finance_invoice_date, ");  
		sql.append(" COALESCE(b.finance_invoice_money,0), ");  
		sql.append(" b.finance_pay_date, ");  
		sql.append(" COALESCE(b.pfd_invoice_sno,''), ");  
		sql.append(" b.pfd_invoice_date, ");  
		sql.append(" COALESCE(b.pfd_invoice_money,0), ");  
		sql.append(" COALESCE(b.pfd_pay_category,''), ");  
		sql.append(" COALESCE(b.pfd_check_sno,''), "); 
		sql.append(" b.pfd_check_close_date, ");  
		sql.append(" COALESCE(b.debit_money,0), ");  
		sql.append(" b.debit_date, ");  
		sql.append(" b.balance_date, ");  
		sql.append(" b.pay_id, ");  
		sql.append(" b.create_date, ");  
		sql.append(" b.update_date, ");  
		sql.append(" c.pfd_contract_id, ");
		sql.append(" c.pfp_pay_type,");
		sql.append(" c.pfd_customer_info_id,");
		sql.append(" d.company_name,");
		sql.append(" d.company_tax_id");
		sql.append(" from pfd_bonus_record a, "); 
		sql.append(" pfd_bonus_invoice b , ");
		sql.append(" pfd_contract c , ");
		sql.append(" pfd_customer_info d ");
		sql.append(" where 1 = 1 "); 
		sql.append(" and a.pfd_contract = c.pfd_contract_id ");
		sql.append(" and b.pfd_contract = c.pfd_contract_id ");
		sql.append(" and c.pfd_customer_info_id = d.customer_info_id ");   
		sql.append(" and a.year = b.close_year ");  
		sql.append(" and a.month = b.close_month ");  
		sql.append(" and a.pay_type = b.pay_type ");  
		sql.append(" and a.year = a.close_year ");  
		sql.append(" and a.month = a.close_month ");     
		
		if(StringUtils.isNotBlank(pfdId)){
			sql.append(" and c.pfd_customer_info_id = '"+pfdId+"' ");
		}
		
		sql.append(" and case length(a.close_month) ");
		sql.append(" when 1 then concat(a.close_year,'0',a.close_month) >= '"+strDate+"' ");
		sql.append(" else concat(a.close_year,a.close_month) >= '"+strDate+"' ");
		sql.append(" end ");
		sql.append(" and case length (a.close_month) ");
		sql.append(" when 1 then concat (a.close_year,'0',a.close_month) <= '"+endDate+"' ");
		sql.append(" else concat (a.close_year,a.close_month) <= '"+endDate+"' ");
		sql.append(" end ");
		sql.append(" order by c.pfd_customer_info_id, a.id desc");
		
		
		log.info(" sql: "+sql);

		String sqlStr = sql.toString();
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlStr);
		List<Object> dataList = query.list();
		
		List<PfdBonusRecordInvoiceVO> resultData = new ArrayList<PfdBonusRecordInvoiceVO>();
		for (int i=0; i<dataList.size(); i++) {
			
			Object[] objArray = (Object[]) dataList.get(i);
			PfdBonusRecordInvoiceVO data = new PfdBonusRecordInvoiceVO();
			
			data.setId((Integer) objArray[0]);
			data.setPayType((String) objArray[2]);
			data.setBonusItem((String) objArray[3]);
			data.setQuarter((Integer) objArray[4]);
			data.setNowClkPrice(Float.parseFloat(objArray[5].toString()));
			data.setNowBonus(Float.parseFloat(objArray[6].toString()));
			data.setRecordDate((Date) objArray[7]);
			data.setCloseYear((Integer) objArray[8]);
			data.setCloseMonth((Integer) objArray[9]);
			data.setBonusType((String) objArray[10]);
			data.setDownload((String) objArray[11]);
			data.setBillStatus((String) objArray[12]);
			data.setBillNote((String) objArray[13]);
			data.setFinanceInvoiceSno((String) objArray[14]);
			data.setFinanceInvoiceDate((Date) objArray[15]);
			data.setFinanceInvoiceMoney(Float.parseFloat(objArray[16].toString()));
			data.setFinancePayDate((Date) objArray[17]);
			data.setPfdInvoiceSno((String) objArray[18]);
			data.setPfdInvoiceDate((Date) objArray[19]);
			data.setPfdInvoiceMoney(Float.parseFloat(objArray[20].toString()));
			data.setPfdPayCategory((String) objArray[21]);
			data.setPfdCheckSno((String) objArray[22]);
			data.setPfdCheckCloseDate((Date) objArray[23]);
			data.setDebitMoney(Float.parseFloat(objArray[24].toString()));
			data.setDebitDate((Date) objArray[25]);
			data.setBalanceDate((Date) objArray[26]);
			data.setPayId((Integer) objArray[27]);
			data.setCreateDate((Date) objArray[28]);
			data.setUpdateDate((Date) objArray[29]);
			data.setPfdContractId((String) objArray[30]);
			data.setPfpPayType((String) objArray[31]);
			data.setPfdCustomerInfoId((String) objArray[32]);
			data.setCompanyName((String) objArray[33]);
			data.setCompanyTaxId((String) objArray[34]);
			
			resultData.add(data);
		}
		
		return resultData;
	}
	
	public List<PfdBonusInvoice> findLaterTotalAdClkPrice(String pfdId, int closeYear, int closeMonth) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusInvoice ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and closeYear = ? ");
		hql.append(" and closeMonth = ? ");
		hql.append(" and payType = ? ");
		hql.append(" order by id desc");
		
		list.add(pfdId);
		list.add(closeYear);
		list.add(closeMonth);
		list.add(EnumPfdAccountPayType.LATER.getPayType());

		return (List<PfdBonusInvoice>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
}
