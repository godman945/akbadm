package com.pchome.akbadm.db.dao.ad;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.pojo.PfpAdGift;
import com.pchome.enumerate.factory.EnumActivityType;

public class PfpAdGiftDAO extends BaseDAO<PfpAdGift,String> implements IPfpAdGiftDAO{
	
	@SuppressWarnings("unchecked")
	public List<PfpAdGift> getPfpAdGifts(String adGiftId, String adGiftName, String adGiftSno, String adGiftStatus, String adGiftEndDate) throws Exception{
		StringBuffer sql = new StringBuffer("from PfpAdGift where 1=1");
		if (StringUtils.isNotBlank(adGiftId)) {
			sql.append(" and adGiftId = '" + adGiftId.trim() + "'");
		}

		if (StringUtils.isNotBlank(adGiftName)) {
			sql.append(" and adGiftName like '%" + adGiftName.trim() + "%'");
		}

		if (StringUtils.isNotBlank(adGiftSno)) {
			sql.append(" and adGiftSno = '" + adGiftSno.trim() + "'");
		}

		if (StringUtils.isNotBlank(adGiftStatus)) {
			sql.append(" and adGiftStatus = '" + adGiftStatus.trim() + "'");
		}

		if (StringUtils.isNotBlank(adGiftEndDate)) {
			sql.append(" and adGiftEndDate = '" + adGiftEndDate.trim() + "'");
		}
		log.info("getPfpAdGifts.SQL = " + sql);
		
		return super.getHibernateTemplate().find(sql.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getPfpAdGifts(final String dateType, final String startDate, final String endDate, final String adGiftName, final String adGiftSno, final String customerInfoId, final String orderId, final String transDetail, final String testSno, final String adGiftStatus, final int page, final int pageSize) throws Exception{
		List<Object> result = (List<Object> ) getHibernateTemplate().execute(
                new HibernateCallback<List<Object> >() {
					public List<Object>  doInHibernate(Session session) throws HibernateException, SQLException {
						String sql = adGiftSQL(dateType, startDate, endDate, adGiftName, adGiftSno, customerInfoId, orderId, transDetail, testSno, adGiftStatus);
						//System.out.println(hql);
						
						List<Object> resultData = null;

						//page=-1 取得全部不分頁用於download
						if(page==-1){
							resultData = session.createSQLQuery(sql).list();
						}else{
							resultData = session.createSQLQuery(sql)
							.setFirstResult((page-1)*pageSize)  
							.setMaxResults(pageSize)
							.list();
						}
						//System.out.println(">> resultData_size  = "+resultData.size());
                        return resultData;
                    }
                }
        );
        
        return result;
	}

	private String adGiftSQL(String dateType, String startDate, String endDate, String adGiftName, String adGiftSno, String customerInfoId, String orderId, String transDetail, String testSno, String adGiftStatus){
		StringBuffer sql = new StringBuffer("select * from pfp_ad_gift where 1=1");
		if (StringUtils.isNotBlank(dateType)) {
			String qry_DateType = "ad_gift_end_date";
			if(dateType.equals("openDate")) {
				qry_DateType = "open_date";
			}
			
			if(startDate.equals(endDate)) {
				sql.append(" and " + qry_DateType + " = '"+startDate+"' ");
			} else {
				sql.append(" and (" + qry_DateType + " between '"+startDate+"' and '"+endDate+"') ");
			}
		}

		if (StringUtils.isNotBlank(adGiftName)) {
			sql.append(" and ad_gift_name like '%" + adGiftName.trim() + "%'");
		}

		if (StringUtils.isNotBlank(adGiftSno)) {
			sql.append(" and ad_gift_sno = '" + adGiftSno.trim() + "'");
		}

		if (StringUtils.isNotBlank(customerInfoId)) {
			sql.append(" and customer_info_id = '" + customerInfoId.trim() + "'");
		}

		if (StringUtils.isNotBlank(orderId)) {
			sql.append(" and order_id = '" + orderId.trim() + "'");
		}

		if (StringUtils.isNotBlank(transDetail)) {
			sql.append(" and trans_detail = '" + transDetail.trim() + "'");
		}

		if (StringUtils.isNotBlank(testSno)) {
			sql.append(" and test_sno = '" + testSno.trim() + "'");
		}

		if (StringUtils.isNotBlank(adGiftStatus)) {
			sql.append(" and ad_gift_status = '" + adGiftStatus.trim() + "'");
		}
		log.info("getPfpAdGifts.SQL = " + sql);
		
		return sql.toString();
	}

	/**
	 * 讀取優惠名單
	 * @param customerInfoId
	 * @param startDate
	 * @param endDate
	 * @param actionId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AdmFreeRecord> getPfpAdGiftSubs(final String customerInfoId, final String startDate, final String endDate, final String actionId) throws Exception{
		List<AdmFreeRecord> result = (List<AdmFreeRecord> ) getHibernateTemplate().execute(
                new HibernateCallback<List<AdmFreeRecord> >() {
					public List<AdmFreeRecord>  doInHibernate(Session session) throws HibernateException, SQLException {
						String sql = adGiftSubSQL(customerInfoId, startDate, endDate, actionId);
						System.out.println(sql);

						List<AdmFreeRecord> resultData = null;
						resultData = session.createQuery(sql).list();
						//System.out.println(">> resultData_size  = "+resultData.size());

						return resultData;
                    }
                }
        );
        
        return result;
	}

	/**
	 * 讀取優惠名單的SQL
	 * @param customerInfoId
	 * @param startDate
	 * @param endDate
	 * @param actionId
	 * @return
	 */
	private String adGiftSubSQL(String customerInfoId, String startDate, String endDate, String actionId){
		StringBuffer sql = new StringBuffer("from AdmFreeRecord where 1=1");
		if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			if(startDate.equals(endDate)) {
				sql.append(" and recordDate = '"+startDate+"' ");
			} else {
				sql.append(" and (recordDate between '"+startDate+"' and '"+endDate+"') ");
			}
		}

		if (StringUtils.isNotBlank(actionId)) {
			sql.append(" and admFreeAction.actionId = '" + actionId.trim() + "'");
		}

		if (StringUtils.isNotBlank(customerInfoId)) {
			sql.append(" and customerInfoId = '" + customerInfoId.trim() + "'");
		}
		//log.info("getAdmFreeRecords.SQL = " + sql);
		
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public String chkAdGiftSnoExist(String adGiftSno) throws Exception {
		System.out.println("adGiftSno = " + adGiftSno);
		String existStatus = "noExist";
		if(StringUtils.isNotBlank(adGiftSno)) {
			String hql = "from PfpAdGift where adGiftSno = ?";
			//log.info("chkAdGroupNameByAdActionSeq.hql = " + hql);
	
			List<PfpAdGift> list = super.getHibernateTemplate().find(hql, adGiftSno);

			if (list != null && list.size() > 0) {
				for(PfpAdGift pfpAdGift:list){
					if(StringUtils.isBlank(pfpAdGift.getCustomerInfoId()) && StringUtils.isBlank(pfpAdGift.getOrderId())) {
						String type = pfpAdGift.getAdGiftSno().substring(0,2);
						existStatus = "exist;" + (EnumActivityType.valueOf(type).getCondition()) + ";" + pfpAdGift.getAdGiftName();
					} else {
						existStatus = "used";
					}
				}
			} else {
				existStatus = "noExist";
			}
		} else {
			existStatus = "noAdGiftSno";
		}
		return existStatus;
	}

	@SuppressWarnings("unchecked")
	public PfpAdGift getPfpAdGiftBySno(String adGiftSno) throws Exception {
		Object[] obj = new Object[]{adGiftSno};
		List<PfpAdGift> list = super.getHibernateTemplate().find("from PfpAdGift where adGiftSno = ?", obj);
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PfpAdGift getPfpAdGiftByOrderId(String orderId) throws Exception {
		Object[] obj = new Object[]{orderId};
		List<PfpAdGift> list = super.getHibernateTemplate().find("from PfpAdGift where orderId = ?", obj);
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PfpAdGift getPfpAdGiftByCustomer(String customerInfoId) throws Exception {
		Object[] obj = new Object[]{customerInfoId};
		List<PfpAdGift> list = super.getHibernateTemplate().find("from PfpAdGift where adGiftStatus <> 4 and customerInfoId = ?", obj);
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public PfpAdGift getPfpAdGiftOpenByCustomer(String customerInfoId) throws Exception {
		Object[] obj = new Object[]{customerInfoId};
		List<PfpAdGift> list = super.getHibernateTemplate().find("from PfpAdGift where adGiftStatus = 4 and customerInfoId = ?", obj);
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PfpAdGift> getAdGifts() throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" from PfpAdGift ");
		hql.append(" where adGiftStatus not in (8,9,10)");
		hql.append(" and adGiftEndDate >= CURDATE()");		// 走期的結束日期要大於今天
		
		return super.getHibernateTemplate().find(hql.toString());
	}

	public void updateTransDetail(Integer adGiftId, char transDetail) throws Exception {
		final StringBuffer sql = new StringBuffer()
		.append("UPDATE pfp_ad_gift set trans_detail = '" + transDetail + "' where ad_gift_id = '" + adGiftId + "'");

        Session session = getSession();
        session.createSQLQuery(sql.toString()).executeUpdate();
        session.flush();
	}
	
	public void saveOrUpdatePfpAdGift(PfpAdGift pfpAdGift) throws Exception{
		super.getHibernateTemplate().saveOrUpdate(pfpAdGift);
	}

	public void insertPfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		this.saveOrUpdate(pfpAdGift);
	}

	public void insertPfpAdGift(List<PfpAdGift> dataList) throws Exception {
		for (int i=0; i<dataList.size(); i++) {
			this.save(dataList.get(i));
		}
	}

	public void updatePfpAdGift(PfpAdGift pfpAdGift) throws Exception {
		this.update(pfpAdGift);
	}

	public void updatePfpAdGiftStatus(String adGiftStatus, String adGroupSno) throws Exception {
		final StringBuffer sql = new StringBuffer()
		.append("UPDATE pfp_ad_gift set ad_gift_status = '" + adGiftStatus + "' where ad_gift_sno = '" + adGroupSno + "'");
		//log.info("updatePfpAdGiftStatus.sql = " + sql);

        Session session = getSession();
        session.createSQLQuery(sql.toString()).executeUpdate();
        session.flush();
	}
	
	public void saveOrUpdateWithCommit(PfpAdGift pfpAdGift) throws Exception{
		super.getSession().saveOrUpdate(pfpAdGift);
		super.getSession().beginTransaction().commit();
	}
}
