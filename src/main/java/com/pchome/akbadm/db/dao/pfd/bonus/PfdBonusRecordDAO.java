package com.pchome.akbadm.db.dao.pfd.bonus;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusBill;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class PfdBonusRecordDAO extends BaseDAO<PfdBonusRecord, Integer> implements IPfdBonusRecordDAO{

	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdBonusRecord(String pfdId, String payType, int year, int month, String pfdBonusItem) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");		
		hql.append(" and payType = ? and bonusItem = ? ");	
		hql.append(" and year = ? and month = ? ");	
		hql.append(" order by id desc ");		
		
		list.add(pfdId);
		list.add(payType);
		list.add(pfdBonusItem);
		list.add(year);
		list.add(month);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdBonusRecordByContractId(String pfdContractId, String pfdId, String payType, int year, int month, String pfdBonusItem) {
		
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
		
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdContractId = ? ");	
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");
		hql.append(" and payType = ? and bonusItem = ? ");	
		hql.append(" and year = ? and month = ? ");	
		hql.append(" order by id desc ");		
		
		list.add(pfdContractId);
		list.add(pfdId);
		list.add(payType);
		list.add(pfdBonusItem);
		list.add(year);
		list.add(month);
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
//	@SuppressWarnings("unchecked")
//	public List<PfdBonusRecord> findPfdBonusRecordForQuarter(String pfdId, String payType, int year, int quarter, String pfdBonusItem) {
//		
//		StringBuffer hql = new StringBuffer();
//		List<Object> list = new ArrayList<Object>();
//		
//		hql.append(" from PfdBonusRecord ");
//		hql.append(" where 1 = 1 ");		
//		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");		
//		hql.append(" and payType = ? and bonusItem = ? ");	
//		hql.append(" and year = ? and quarter = ? ");	
//		hql.append(" order by id desc ");		
//		
//		list.add(pfdId);
//		list.add(payType);
//		list.add(pfdBonusItem);
//		list.add(year);
//		list.add(quarter);
//		
//		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
//	}
//	
//	@SuppressWarnings("unchecked")
//	public List<PfdBonusRecord> findPfdBonusRecordForYear(String pfdId, String payType, int year, String pfdBonusItem) {
//		
//		StringBuffer hql = new StringBuffer();
//		List<Object> list = new ArrayList<Object>();
//		
//		hql.append(" from PfdBonusRecord ");
//		hql.append(" where 1 = 1 ");		
//		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");		
//		hql.append(" and payType = ? and bonusItem = ? ");	
//		hql.append(" and year = ?  ");	
//		hql.append(" order by id desc ");		
//		
//		list.add(pfdId);
//		list.add(payType);
//		list.add(pfdBonusItem);
//		list.add(year);
//
//		
//		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
//	}
	
	public float findPfdBonusMoney(String pfdId, String payType, int year, int month) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("select sum(nowBonus) ");
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType ");	
		hql.append(" and year = :year and month = :month ");
		hql.append(" and closeMonth = :closeMonth ");
		hql.append(" order by id desc ");
		
		Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setInteger("year", year)
                .setInteger("month", month)
                .setInteger("closeMonth", month)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
	}
	
	public float findPfdBonusAdClkPrice(String pfdId, String payType, int year, int month, String pfdBonusItem) {
		
		StringBuffer hql = new StringBuffer();
	
		hql.append("select sum(nowClkPrice) ");
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType and bonusItem = :pfdBonusItem ");	
		hql.append(" and year = :year and month = :month ");	
		hql.append(" order by id desc ");		
		
		Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setString("pfdBonusItem", pfdBonusItem)
                .setInteger("year", year)
                .setInteger("month", month)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
	}
	
	public float findPayBonus(String pfdId, String payType, int year, int quarter, String pfdBonusItem) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("select sum(nowBonus) ");
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType and bonusItem = :pfdBonusItem ");	
		hql.append(" and year = :year and quarter = :quarter ");	
		hql.append(" order by id desc ");
		
		Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setString("pfdBonusItem", pfdBonusItem)
                .setInteger("year", year)
                .setInteger("quarter", quarter)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdBonusRecords(String pfdId, String payType, int year, int month, int quarter)throws Exception {
		//本期	
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();
	
		hql.append(" from PfdBonusRecord a ");
		hql.append(" where 1 = 1 ");		
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = ? ");		
		hql.append(" and year = closeYear ");
		hql.append(" and month = closeMonth ");
		hql.append(" and ( (year = ? and month = ? ) ");	
		
		if (quarter!=0){
			hql.append(" or ( year = ? and month < ? and bonusItem=2 and quarter =? ) ");
		}else if(quarter==0){
			hql.append(" and  bonusItem=1 ");
		}
		
		if (month==12 && quarter!=0){
			hql.append(" or ( year = ? and month < ? and bonusItem=3 ) ");
		}
		
		hql.append(" ) and nowBonus != 0.00 ");
		hql.append(" order by a.bonusItem asc, a.pfdContract.pfdContractId.startDate desc, a.month desc");
		
		list.add(pfdId);
		list.add(year);
		list.add(month);
		
		if (quarter!=0){
			list.add(year);
			list.add(month);
			list.add(quarter);
		}
		
		if (month==12){
			list.add(year);
			list.add(month);
		}
		
		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findnonClosePfdBonusRecords(final String pfdId, final int year, final int month)throws Exception {
		
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {
				//前期未結佣(獎)金
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.id, ");
				hql.append(" a.pfdContract, ");
				hql.append(" a.payType, ");
				hql.append(" a.year, ");
				hql.append(" a.month, ");
				hql.append(" a.quarter, ");
				hql.append(" a.closeYear, ");
				hql.append(" a.closeMonth, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.bonusName, ");
				hql.append(" a.startDate, ");
				hql.append(" a.endDate, ");
				hql.append(" a.nowClkPrice, ");
				hql.append(" a.nextClkPrice, ");
				hql.append(" a.nowPercent, ");
				hql.append(" a.nextPercent, ");
				hql.append(" a.deductBonus, ");
				hql.append(" a.nowAmount, ");
				hql.append(" a.nextAmount, ");
				hql.append(" a.nowBonus, ");
				hql.append(" a.nextBonus, ");
				hql.append(" a.reportActoin, ");
				hql.append(" a.bonusNote, ");
				hql.append(" a.createDate, ");
				hql.append(" a.updateDate ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.nowBonus != 0.00 ");
				hql.append(" and b.billStatus != '" + EnumPfdBonusBill.PAY_MONEY.getStatus() + "' ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and ((a.year = " + year + " and a.month < " + month + ") ");
				hql.append(" or (a.year < " + year + ")) ");
				hql.append(" order by a.bonusItem asc, a.pfdContract.pfdContractId.startDate desc, a.month desc ");
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setId((Integer) objArray[0]);
					data.setPfdContract((PfdContract) objArray[1]);
					data.setPayType((String) objArray[2]);
					data.setYear((Integer) objArray[3]);
					data.setMonth((Integer) objArray[4]);
					data.setQuarter((Integer) objArray[5]);
					data.setCloseYear((Integer) objArray[6]);
					data.setCloseMonth((Integer) objArray[7]);
					data.setBonusItem((String) objArray[8]);
					data.setBonusName((String) objArray[9]);
					data.setStartDate((Date) objArray[10]);
					data.setEndDate((Date) objArray[11]);
					data.setNowClkPrice(Float.parseFloat(objArray[12].toString()));
					data.setNextClkPrice(Float.parseFloat(objArray[13].toString()));
					data.setNowPercent(Float.parseFloat(objArray[14].toString()));
					data.setNextPercent(Float.parseFloat(objArray[15].toString()));
					data.setDeductBonus(Float.parseFloat(objArray[16].toString()));
					data.setNowAmount((Integer) objArray[17]);
					data.setNextAmount((Integer) objArray[18]);
					data.setNowBonus(Float.parseFloat(objArray[19].toString()));
					data.setNextBonus(Float.parseFloat(objArray[20].toString()));
					data.setReportActoin((String) objArray[21]);
					data.setBonusNote((String) objArray[22]);
					data.setCreateDate((Date) objArray[23]);
					data.setUpdateDate((Date) objArray[24]);
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	public float findPfdBonusMoneyByContract(String pfdId, String payType, int year, int month, String contractId) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append("select sum(nowBonus) ");
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");	
		hql.append(" and pfdContract.pfdContractId = :contractId ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType ");	
		hql.append(" and year = :year and month = :month ");
		hql.append(" and closeMonth = :closeMonth ");
		hql.append(" order by id desc ");
		
		Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("contractId", contractId)
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setInteger("year", year)
                .setInteger("month", month)
                .setInteger("closeMonth", month)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
	}
	
	public float findPfdBonusAdClkPriceByContract(String pfdId, String payType, int year, int month, String pfdBonusItem, String contractId) {
		
		StringBuffer hql = new StringBuffer();
	
		hql.append("select sum(nowClkPrice) ");
		hql.append(" from PfdBonusRecord ");
		hql.append(" where 1 = 1 ");	
		hql.append(" and pfdContract.pfdContractId = :contractId ");
		hql.append(" and pfdContract.pfdCustomerInfo.customerInfoId = :pfdId ");		
		hql.append(" and payType = :payType and bonusItem = :pfdBonusItem ");	
		hql.append(" and year = :year and month = :month ");	
		hql.append(" order by id desc ");		
		
		Double result = (Double) this.getSession()
                .createQuery(hql.toString())
                .setString("contractId", contractId)
                .setString("pfdId", pfdId)
                .setString("payType", payType)
                .setString("pfdBonusItem", pfdBonusItem)
                .setInteger("year", year)
                .setInteger("month", month)
                .uniqueResult();
		
		return result != null ? result.floatValue() : 0f;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdBonusRecordsInvoiceDetail(final String pfdId, final int year, final int month)throws Exception {
		
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {
				//本期+前期未結明細
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.id, ");
				hql.append(" a.pfdContract, ");
				hql.append(" a.payType, ");
				hql.append(" a.year, ");
				hql.append(" a.month, ");
				hql.append(" a.quarter, ");
				hql.append(" a.closeYear, ");
				hql.append(" a.closeMonth, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.bonusName, ");
				hql.append(" a.startDate, ");
				hql.append(" a.endDate, ");
				hql.append(" a.nowClkPrice, ");
				hql.append(" a.nextClkPrice, ");
				hql.append(" a.nowPercent, ");
				hql.append(" a.nextPercent, ");
				hql.append(" a.deductBonus, ");
				hql.append(" a.nowAmount, ");
				hql.append(" a.nextAmount, ");
				hql.append(" a.nowBonus, ");
				hql.append(" a.nextBonus, ");
				hql.append(" a.reportActoin, ");
				hql.append(" a.bonusNote, ");
				hql.append(" a.createDate, ");
				hql.append(" a.updateDate ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b");				
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract "); 
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");						 
				hql.append(" and a.year = b.closeYear ");   
				hql.append(" and a.month = b.closeMonth ");  
				hql.append(" and a.payType = b.payType ");  						   
				hql.append(" and a.year = a.closeYear ");   
				hql.append(" and a.month = a.closeMonth ");   
				hql.append(" and a.nowBonus != 0.00 ");		
				hql.append(" and b.billStatus != '" + EnumPfdBonusBill.PAY_MONEY.getStatus() + "' ");
				hql.append(" and ( ( a.year = " + year + " and a.month <= " + month + ") or (a.year < " + year + ") ) "); //本期+前期未結明細
				hql.append(" order by a.bonusItem asc, a.pfdContract.pfdContractId.startDate desc, a.month desc "); 
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setId((Integer) objArray[0]);
					data.setPfdContract((PfdContract) objArray[1]);
					data.setPayType((String) objArray[2]);
					data.setYear((Integer) objArray[3]);
					data.setMonth((Integer) objArray[4]);
					data.setQuarter((Integer) objArray[5]);
					data.setCloseYear((Integer) objArray[6]);
					data.setCloseMonth((Integer) objArray[7]);
					data.setBonusItem((String) objArray[8]);
					data.setBonusName((String) objArray[9]);
					data.setStartDate((Date) objArray[10]);
					data.setEndDate((Date) objArray[11]);
					data.setNowClkPrice(Float.parseFloat(objArray[12].toString()));
					data.setNextClkPrice(Float.parseFloat(objArray[13].toString()));
					data.setNowPercent(Float.parseFloat(objArray[14].toString()));
					data.setNextPercent(Float.parseFloat(objArray[15].toString()));
					data.setDeductBonus(Float.parseFloat(objArray[16].toString()));
					data.setNowAmount((Integer) objArray[17]);
					data.setNextAmount((Integer) objArray[18]);
					data.setNowBonus(Float.parseFloat(objArray[19].toString()));
					data.setNextBonus(Float.parseFloat(objArray[20].toString()));
					data.setReportActoin((String) objArray[21]);
					data.setBonusNote((String) objArray[22]);
					data.setCreateDate((Date) objArray[23]);
					data.setUpdateDate((Date) objArray[24]);
					
					resultData.add(data);
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdTotalAdClick(final String pfdId, final int year, final int month)throws Exception {
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {		
				
				//本期廣告費用(只算後付花費)
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.payType, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.nowClkPrice ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and a.year = " + year );
				hql.append(" and a.month= " + month );
				hql.append(" and a.payType = " +  EnumPfdAccountPayType.LATER.getPayType());
				hql.append(" and a.bonusItem = " + EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType());
				hql.append(" and b.billStatus = " + EnumPfdBonusBill.NOT_APPLY.getStatus());
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setPayType((String) objArray[0]);
					data.setBonusItem((String) objArray[1]);
					data.setNowClkPrice(Float.parseFloat(objArray[2].toString()));
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdInvoiceTotalAdClick(final String pfdId, final int year, final int month)throws Exception {
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {		
				
				//本期廣告費用(只算後付花費)
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.payType, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.nowClkPrice ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and a.year = " + year );
				hql.append(" and a.month= " + month );
				hql.append(" and a.payType = " +  EnumPfdAccountPayType.LATER.getPayType());
				hql.append(" and a.bonusItem = " + EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType());
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setPayType((String) objArray[0]);
					data.setBonusItem((String) objArray[1]);
					data.setNowClkPrice(Float.parseFloat(objArray[2].toString()));
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfdBonusRecord> findPfdNonCloseTotalAdClick(final String pfdId, final int year, final int month)throws Exception {
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {
				//前期未結廣告費用
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.payType, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.nowClkPrice ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and ((a.year = " + year + " and a.month < " + month + ") ");
				hql.append(" or (a.year < " + year + ")) ");
				hql.append(" and a.payType = " +  EnumPfdAccountPayType.LATER.getPayType());
				hql.append(" and a.bonusItem = " + EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType());
				hql.append(" and b.billStatus = " + EnumPfdBonusBill.NOT_APPLY.getStatus());
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setPayType((String) objArray[0]);
					data.setBonusItem((String) objArray[1]);
					data.setNowClkPrice(Float.parseFloat(objArray[2].toString()));
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	public List<PfdBonusRecord> findPfdBonusRecordsByPayment(final String pfdId, final int year, final int month) throws Exception{
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {
				//本期佣金與本期應付獎金
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.id, ");
				hql.append(" a.pfdContract, ");
				hql.append(" a.payType, ");
				hql.append(" a.year, ");
				hql.append(" a.month, ");
				hql.append(" a.quarter, ");
				hql.append(" a.closeYear, ");
				hql.append(" a.closeMonth, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.bonusName, ");
				hql.append(" a.startDate, ");
				hql.append(" a.endDate, ");
				hql.append(" a.nowClkPrice, ");
				hql.append(" a.nextClkPrice, ");
				hql.append(" a.nowPercent, ");
				hql.append(" a.nextPercent, ");
				hql.append(" a.deductBonus, ");
				hql.append(" a.nowAmount, ");
				hql.append(" a.nextAmount, ");
				hql.append(" a.nowBonus, ");
				hql.append(" a.nextBonus, ");
				hql.append(" a.reportActoin, ");
				hql.append(" a.bonusNote, ");
				hql.append(" a.createDate, ");
				hql.append(" a.updateDate ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.nowBonus != 0.00 ");
				hql.append(" and b.billStatus != '" + EnumPfdBonusBill.PAY_MONEY.getStatus() + "' ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and a.year = " + year );
				hql.append(" and a.month = " + month );
				hql.append(" order by a.bonusItem asc, a.pfdContract.pfdContractId.startDate desc, a.month desc ");
				
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setId((Integer) objArray[0]);
					data.setPfdContract((PfdContract) objArray[1]);
					data.setPayType((String) objArray[2]);
					data.setYear((Integer) objArray[3]);
					data.setMonth((Integer) objArray[4]);
					data.setQuarter((Integer) objArray[5]);
					data.setCloseYear((Integer) objArray[6]);
					data.setCloseMonth((Integer) objArray[7]);
					data.setBonusItem((String) objArray[8]);
					data.setBonusName((String) objArray[9]);
					data.setStartDate((Date) objArray[10]);
					data.setEndDate((Date) objArray[11]);
					data.setNowClkPrice(Float.parseFloat(objArray[12].toString()));
					data.setNextClkPrice(Float.parseFloat(objArray[13].toString()));
					data.setNowPercent(Float.parseFloat(objArray[14].toString()));
					data.setNextPercent(Float.parseFloat(objArray[15].toString()));
					data.setDeductBonus(Float.parseFloat(objArray[16].toString()));
					data.setNowAmount((Integer) objArray[17]);
					data.setNextAmount((Integer) objArray[18]);
					data.setNowBonus(Float.parseFloat(objArray[19].toString()));
					data.setNextBonus(Float.parseFloat(objArray[20].toString()));
					data.setReportActoin((String) objArray[21]);
					data.setBonusNote((String) objArray[22]);
					data.setCreateDate((Date) objArray[23]);
					data.setUpdateDate((Date) objArray[24]);
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
	public List<PfdBonusRecord> findPfdInvoiceBonusRecords(final String pfdId, final int year, final int month) throws Exception{
		List<PfdBonusRecord> result = getHibernateTemplate().execute(new HibernateCallback<List<PfdBonusRecord>>() {
			@Override
            public List<PfdBonusRecord> doInHibernate(Session session) throws HibernateException, SQLException {
				//本期佣金與本期應付獎金
				StringBuffer hql = new StringBuffer();
				
				hql.append(" select ");
				hql.append(" a.id, ");
				hql.append(" a.pfdContract, ");
				hql.append(" a.payType, ");
				hql.append(" a.year, ");
				hql.append(" a.month, ");
				hql.append(" a.quarter, ");
				hql.append(" a.closeYear, ");
				hql.append(" a.closeMonth, ");
				hql.append(" a.bonusItem, ");
				hql.append(" a.bonusName, ");
				hql.append(" a.startDate, ");
				hql.append(" a.endDate, ");
				hql.append(" a.nowClkPrice, ");
				hql.append(" a.nextClkPrice, ");
				hql.append(" a.nowPercent, ");
				hql.append(" a.nextPercent, ");
				hql.append(" a.deductBonus, ");
				hql.append(" a.nowAmount, ");
				hql.append(" a.nextAmount, ");
				hql.append(" a.nowBonus, ");
				hql.append(" a.nextBonus, ");
				hql.append(" a.reportActoin, ");
				hql.append(" a.bonusNote, ");
				hql.append(" a.createDate, ");
				hql.append(" a.updateDate ");
				hql.append(" from PfdBonusRecord a, PfdBonusInvoice b ");
				hql.append(" where 1 = 1 ");
				hql.append(" and a.pfdContract = b.pfdContract ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = b.pfdContract.pfdCustomerInfo.customerInfoId ");
				hql.append(" and a.year = b.closeYear ");
				hql.append(" and a.month = b.closeMonth ");
				hql.append(" and a.payType = b.payType ");
				hql.append(" and a.year = a.closeYear ");
				hql.append(" and a.month = a.closeMonth ");
				hql.append(" and a.nowBonus != 0.00 ");
				hql.append(" and a.pfdContract.pfdCustomerInfo.customerInfoId = '" + pfdId + "' ");
				hql.append(" and a.year = " + year );
				hql.append(" and a.month = " + month );
				hql.append(" order by a.bonusItem asc, a.pfdContract.pfdContractId.startDate desc, a.month desc ");
				
				
				String sql = hql.toString();
				
				Query query = session.createQuery(sql);
				List<Object> dataList = query.list();
				
				List<PfdBonusRecord> resultData = new ArrayList<PfdBonusRecord>();
				for (int i=0; i<dataList.size(); i++) {
					Object[] objArray = (Object[]) dataList.get(i);
					PfdBonusRecord data = new PfdBonusRecord();
					
					data.setId((Integer) objArray[0]);
					data.setPfdContract((PfdContract) objArray[1]);
					data.setPayType((String) objArray[2]);
					data.setYear((Integer) objArray[3]);
					data.setMonth((Integer) objArray[4]);
					data.setQuarter((Integer) objArray[5]);
					data.setCloseYear((Integer) objArray[6]);
					data.setCloseMonth((Integer) objArray[7]);
					data.setBonusItem((String) objArray[8]);
					data.setBonusName((String) objArray[9]);
					data.setStartDate((Date) objArray[10]);
					data.setEndDate((Date) objArray[11]);
					data.setNowClkPrice(Float.parseFloat(objArray[12].toString()));
					data.setNextClkPrice(Float.parseFloat(objArray[13].toString()));
					data.setNowPercent(Float.parseFloat(objArray[14].toString()));
					data.setNextPercent(Float.parseFloat(objArray[15].toString()));
					data.setDeductBonus(Float.parseFloat(objArray[16].toString()));
					data.setNowAmount((Integer) objArray[17]);
					data.setNextAmount((Integer) objArray[18]);
					data.setNowBonus(Float.parseFloat(objArray[19].toString()));
					data.setNextBonus(Float.parseFloat(objArray[20].toString()));
					data.setReportActoin((String) objArray[21]);
					data.setBonusNote((String) objArray[22]);
					data.setCreateDate((Date) objArray[23]);
					data.setUpdateDate((Date) objArray[24]);
					
					resultData.add(data);
					
				}
				
				return resultData;
			}
		});
		
		return result;
	}
	
}
