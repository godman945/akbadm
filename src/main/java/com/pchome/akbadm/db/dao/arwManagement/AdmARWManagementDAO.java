package com.pchome.akbadm.db.dao.arwManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmArwValue;
import com.pchome.akbadm.db.vo.ad.AdmARWManagementVO;

public class AdmARWManagementDAO extends BaseDAO<AdmArwValue, Integer> implements IAdmARWManagementDAO{

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 取得ARW 廣告權重列表資料
	 */
	@Override
	public List<Object> getARWDataList(AdmARWManagementVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ");
		hql.append("  (SELECT pfd_customer_info_id ");
		hql.append("   FROM pfd_user_ad_account_ref uaar ");
		hql.append("   WHERE uaar.pfp_customer_info_id = av.customer_info_id ");
		hql.append("   )pfd_customer_info_id, ");
		hql.append("   customer_info_id, ");
		hql.append("   customer_info_title, ");
		hql.append("   arw_value, ");
		hql.append("   date_flag, ");
		hql.append("   start_date, ");
		hql.append("   end_date ");
		hql.append(" FROM adm_arw_value av");
		hql.append(" WHERE 1 = 1 ");
		if (StringUtils.isNotBlank(vo.getCustomerInfoId())) {
			hql.append(" AND av.customer_info_id = :customer_info_id ");
		}

		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hqlStr);

		// 塞入where參數值
		if (StringUtils.isNotBlank(vo.getCustomerInfoId())) {
			query.setString("customer_info_id", vo.getCustomerInfoId());
		}

		return query.list();
	}

	/**
	 * 刪除ARW 廣告權重資料
	 */
	@Override
	public void deleteARWData(AdmARWManagementVO vo) {
		String hql = "delete from AdmArwValue where customer_info_id = :customer_info_id";
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		session.createQuery(hql).setString("customer_info_id", vo.getCustomerInfoId()).executeUpdate();
		session.flush();
	}

	/**
	 * 新增ARW 廣告權重資料
	 */
	@Override
	public void addARWData(AdmARWManagementVO vo) throws ParseException {
		Date now = new Date();
		
		AdmArwValue admArwValue = new AdmArwValue();
		admArwValue.setCustomerInfoId(vo.getCustomerInfoId());
		admArwValue.setCustomerInfoTitle(vo.getCustomerInfoTitle());
		admArwValue.setArwValue(vo.getArwValue());
		admArwValue.setDateFlag(vo.getDateFlag());
		//有值為有設定啟用起迄，無值則預設現在時間
		if (StringUtils.isNotBlank(vo.getStartDate()) && StringUtils.isNotBlank(vo.getEndDate())) {
			admArwValue.setStartDate(dateFormat.parse(vo.getStartDate()));
			admArwValue.setEndDate(dateFormat.parse(vo.getEndDate()));
		} else {
			admArwValue.setStartDate(now);
			admArwValue.setEndDate(now);
		}
		admArwValue.setUpdateDate(now);
		admArwValue.setCreateDate(now);
		super.save(admArwValue);
	}

	/**
	 * 更新ARW 廣告權重資料
	 */
	@Override
	public void updateARWData(AdmARWManagementVO vo) throws ParseException {
		String hql =  "UPDATE AdmArwValue ";
		       hql += "SET arw_value = :arw_value, date_flag = :date_flag, ";
			   hql += "start_date = :start_date, end_date = :end_date, ";
		       hql += "update_date = CURRENT_TIMESTAMP() ";
		       hql += "WHERE customer_info_id = :customer_info_id ";
		       
		Date now = new Date();
		
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		query.setInteger("arw_value", vo.getArwValue());
		query.setInteger("date_flag", vo.getDateFlag());
		//有值為有設定啟用起迄，無值則預設現在時間
		if (StringUtils.isNotBlank(vo.getStartDate()) && StringUtils.isNotBlank(vo.getEndDate())) {
			query.setDate("start_date", dateFormat.parse(vo.getStartDate()));
			query.setDate("end_date", dateFormat.parse(vo.getEndDate()));
		} else {
			query.setDate("start_date", now);
			query.setDate("end_date", now);
		}
		query.setString("customer_info_id", vo.getCustomerInfoId());
		query.executeUpdate();
		session.flush();
	}

	/**
	 * 取得下拉選單PFP帳號名稱資料，排除已新增ARW權限的帳號
	 */
	@Override
	public List<Object> getPfpCustomerList(AdmARWManagementVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT                                                                              ");
		hql.append("   customer_info_id,                                                                 ");
		hql.append("   customer_info_title                                                               ");
		hql.append(" FROM(                                                                               ");
		hql.append("   SELECT                                                                            ");
		hql.append("     ci.customer_info_id,                                                            ");
		hql.append("     ci.customer_info_title                                                          ");
		hql.append("   FROM pfd_user_ad_account_ref uaar                                                 ");
		hql.append("   LEFT join pfp_customer_info ci ON uaar.pfp_customer_info_id = ci.customer_info_id ");
		hql.append("   WHERE                                                                             ");
		hql.append("   uaar.pfd_customer_info_id = :pfd_customer_info_id) t1                                 ");
		hql.append(" WHERE NOT EXISTS                                                                    ");
		hql.append(" (SELECT customer_info_id                                                            ");
		hql.append("  FROM adm_arw_value av                                                              ");
		hql.append("  WHERE t1.customer_info_id = av.customer_info_id                                    ");
		hql.append(" )                                                                                   ");
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hqlStr);
		// 塞入where參數值
		if (StringUtils.isNotBlank(vo.getPfdCustomerInfoId())) {
			query.setString("pfd_customer_info_id", vo.getPfdCustomerInfoId());
		}
		
		return query.list();
	}

	/**
	 * 取得下拉選單PFD經銷商帳號名稱資料
	 */
	@Override
	public List<Object> getPfdCustomerList() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT                 ");
		hql.append("   customer_info_id,    ");
		hql.append("   company_name         ");
		hql.append(" FROM pfd_customer_info ");
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hqlStr);
		
		return query.list();
	}

}
