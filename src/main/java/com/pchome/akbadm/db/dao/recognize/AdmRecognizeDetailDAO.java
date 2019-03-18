package com.pchome.akbadm.db.dao.recognize;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class AdmRecognizeDetailDAO extends BaseDAO <AdmRecognizeDetail, String> implements IAdmRecognizeDetailDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findRecognizeDetail(Date date) throws Exception{

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where costDate = ? ");
		hql.append(" and costPrice > 0 ");
		hql.append(" and orderType = ? ");

		Object[] ob = new Object[]{date, EnumOrderType.SAVE.getTypeTag()};

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), ob);
	}

//	@SuppressWarnings("unchecked")
//	public List<AdmRecognizeDetail> findBeforeCostDateRecognizeDetail(String customerInfoId, Date date){
//
//		StringBuffer hql = new StringBuffer();
//
//		hql.append(" from AdmRecognizeDetail ");
//		hql.append(" where costDate <= ? ");
//		hql.append(" and customerInfoId = ? ");
//		hql.append(" and orderType != ? ");
//		hql.append(" order by recordDetailId desc ");
//
//		return super.getHibernateTemplate().find(hql.toString(), date, customerInfoId, EnumOrderType.TAX.getTypeTag());
//
//	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findAdvanceRecognizeDetail(String customerInfoId, Date startDate, String payType) {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from AdmRecognizeDetail ");
		hql.append("where customerInfoId = ? ");
		hql.append("and (orderType = ? or orderType = ? or orderType = ? ) ");
        hql.append("and costDate < ? ");
		hql.append("order by recordDetailId desc ");

		list.add(customerInfoId);
		list.add(EnumOrderType.SAVE.getTypeTag());
		list.add(EnumOrderType.GIFT.getTypeTag());
		list.add(EnumOrderType.FEEDBACK.getTypeTag());
        list.add(startDate);

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findLaterRecognizeDetail(String customerInfoId, Date startDate, String payType) {
		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from AdmRecognizeDetail ");
		hql.append("where customerInfoId = ? ");
		hql.append("and orderType = ? ");
        hql.append("and costDate < ? ");
		hql.append("order by recordDetailId desc ");

		list.add(customerInfoId);
		list.add(EnumOrderType.VIRTUAL.getTypeTag());
        list.add(startDate);

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    public Integer deleteRecordAfterDate(Date date){

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from AdmRecognizeDetail ");
		hql.append(" where costDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), date);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date){

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where costDate = ? ");
		hql.append(" order by recordDetailId desc ");

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(),date);
	}

	/**
	 * 依據日期及pfp帳號id 查詢 攤提明細(adm_recognize_detail)
	 * @param date 攤提日期
	 * @param pfpCustomerInfoIds pfp帳號id 的 list
	 * @return List<AdmRecognizeDetail>
	 */
	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date, List<Object> pfpCustomerInfoIds){

		StringBuffer hql = new StringBuffer();
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where costDate = :costDate ");

		String[] key = {"costDate"};
		Object[] val = {date};
		// 如果總廣告成效有選擇經銷歸屬的話，就要帶入經銷商下轄所有的 pfp 帳號
		if(pfpCustomerInfoIds.size() > 0) {
			hql.append(" and customerInfoId in (:pfpCustomerInfoId) ");
			key = new String[]{"costDate", "pfpCustomerInfoId"};
			val = new Object[]{date, pfpCustomerInfoIds};
		}

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().findByNamedParam(hql.toString(),key, val);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findRecognizeDetail(String customerInfoId, Date date, String orderType){
		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where customerInfoId = ? ");
		hql.append(" and costDate = ? ");
		hql.append(" and orderType = ? ");
		hql.append(" order by recordDetailId desc ");

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), customerInfoId, date, orderType);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findRecognizeDetail(String recordId, String customerInfoId, String orderType){
		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where admRecognizeRecord.recognizeRecordId = ? ");
		hql.append(" and customerInfoId = ? ");
		hql.append(" and orderType = ? ");
		hql.append(" order by recordDetailId desc ");

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), recordId, customerInfoId, orderType);
	}

	@Override
    public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, Date startDate, Date endDate, String orderType) {

		StringBuffer hql = new StringBuffer();

    	hql.append(" select sum(costPrice) ");
    	hql.append(" from AdmRecognizeDetail ");
    	hql.append(" where pfdCustomerInfoId = :pfdCustomerInfoId ");
    	hql.append(" and orderType = :orderType ");
    	hql.append(" and costDate >= :startDate");
    	hql.append(" and costDate <= :endDate");

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("pfdCustomerInfoId", pfdCustomerInfoId)
    	.setString("orderType", orderType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate);

    	Object result = query.uniqueResult();
    	if (result == null) {
    		return 0f;
    	}

    	return ((Double) result).floatValue();
	}

	@Override
    public float findPfpCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, Date startDate, Date endDate, String orderType) {

		StringBuffer hql = new StringBuffer();

    	hql.append(" select sum(costPrice) ");
    	hql.append(" from AdmRecognizeDetail ");
    	hql.append(" where pfdCustomerInfoId = :pfdCustomerInfoId ");
    	hql.append(" and customerInfoId = :pfpCustomerInfoId ");
    	hql.append(" and orderType = :orderType ");
    	hql.append(" and costDate >= :startDate");
    	hql.append(" and costDate <= :endDate");

    	Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("pfdCustomerInfoId", pfdCustomerInfoId)
    	.setString("pfpCustomerInfoId", pfpCustomerInfoId)
    	.setString("orderType", orderType)
    	.setDate("startDate", startDate)
    	.setDate("endDate", endDate);

    	Object result = query.uniqueResult();
    	if (result == null) {
    		return 0f;
    	}

    	return ((Double) result).floatValue();
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeDetail> findAdmRecognizeDetails(Date spendDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");
		hql.append(" and costDate = ? ");
		hql.append(" order by recordDetailId desc ");

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), spendDate);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object[]> findRecognizePrice(Date spendDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" select customerInfoId, sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");
		hql.append(" and costDate = ? ");
		hql.append(" group by customerInfoId ");

		return (List<Object[]>) super.getHibernateTemplate().find(hql.toString(), spendDate);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object[]> findRecognizePriceBeforeDate(Date spendDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select customerInfoId, sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");
		hql.append(" and costDate <= ? ");
		hql.append(" group by customerInfoId ");

		return (List<Object[]>) super.getHibernateTemplate().find(hql.toString(), spendDate);
	}

	@Override
    public float findPfpRecognizeDetail(String pfpId, String orderType, Date startDate, Date endDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");
		hql.append(" and pfdCustomerInfoId = null ");

		if(StringUtils.isNotBlank(orderType)){
			hql.append(" and orderType = :orderType ");
		}

		hql.append(" and costDate between :startDate and :endDate ");

		if(StringUtils.isNotBlank(pfpId)){
			hql.append(" and customerInfoId = :pfpId ");
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		if(StringUtils.isNotBlank(orderType)){
			query.setString("orderType", orderType);
		}

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));

    	if(StringUtils.isNotBlank(pfpId)){
    		query.setString("pfpId", pfpId);
		}

    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}

	@Override
    public float findPfpRecognizeDetailForFree(String pfpId, Date startDate, Date endDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");
        hql.append(" and pfdCustomerInfoId = null ");
		hql.append(" and (orderType != :SAVE and orderType != :VIRTUAL) ");
		hql.append(" and costDate between :startDate and :endDate ");

		if(StringUtils.isNotBlank(pfpId)){
			hql.append(" and customerInfoId = :pfpId ");
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		// 儲值金
		query.setString("SAVE", EnumOrderType.SAVE.getTypeTag());
		// P幣
		query.setString("VIRTUAL", EnumOrderType.VIRTUAL.getTypeTag());

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));

    	if(StringUtils.isNotBlank(pfpId)){
    		query.setString("pfpId", pfpId);
		}


    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}

	@Override
    public float findPfdAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");

		if(StringUtils.isNotBlank(pfdId)){
			hql.append(" and pfdCustomerInfoId = :pfdId ");
		}else{
			hql.append(" and pfdCustomerInfoId != null ");
		}

		if(StringUtils.isNotBlank(orderType)){
			hql.append(" and orderType = :orderType ");
		}

		hql.append(" and costDate between :startDate and :endDate ");


		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		if(StringUtils.isNotBlank(pfdId)){
			query.setString("pfdId", pfdId);
		}

		if(StringUtils.isNotBlank(orderType)){
			query.setString("orderType", orderType);
		}

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));

    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}

	@Override
    public float findPfdRecognizeDetailForFree(String pfdId, Date startDate, Date endDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");

		if(StringUtils.isNotBlank(pfdId)){
			hql.append(" and pfdCustomerInfoId = :pfdId ");
		}else{
			hql.append(" and pfdCustomerInfoId != null ");
		}

		hql.append(" and (orderType != :SAVE and orderType != :VIRTUAL) ");
		hql.append(" and costDate between :startDate and :endDate ");


		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		if(StringUtils.isNotBlank(pfdId)){
			query.setString("pfdId", pfdId);
		}

		// 儲值金
		query.setString("SAVE", EnumOrderType.SAVE.getTypeTag());
		// P幣
		query.setString("VIRTUAL", EnumOrderType.VIRTUAL.getTypeTag());

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));


    	Double result=0.0d;


    	 result = (Double) query.uniqueResult();



        return result != null ? result.floatValue() : 0f;
	}

  @Override
  public float findPfbAdPvClkPrice(String pfpId, String orderType, Date startDate, Date endDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");


		if(StringUtils.isNotBlank(orderType)){
			hql.append(" and orderType = :orderType ");
		}

		hql.append(" and costDate between :startDate and :endDate ");

		if(StringUtils.isNotBlank(pfpId)){
			hql.append(" and customerInfoId = :pfpId ");
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		if(StringUtils.isNotBlank(orderType)){
			query.setString("orderType", orderType);
		}

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));

    	if(StringUtils.isNotBlank(pfpId)){
    		query.setString("pfpId", pfpId);
		}

    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}

   @Override
	public float findPfbRecognizeDetailForFree(String pfpId, Date startDate, Date endDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" select sum(costPrice) ");
		hql.append(" from AdmRecognizeDetail ");
		hql.append(" where 1 = 1 ");

		hql.append(" and (orderType != :SAVE and orderType != :VIRTUAL) ");
		hql.append(" and costDate between :startDate and :endDate ");

		if(StringUtils.isNotBlank(pfpId)){
			hql.append(" and customerInfoId = :pfpId ");
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());

		// 儲值金
		query.setString("SAVE", EnumOrderType.SAVE.getTypeTag());
		// P幣
		query.setString("VIRTUAL", EnumOrderType.VIRTUAL.getTypeTag());

		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    	query.setString("startDate", sdFormat.format(startDate));
    	query.setString("endDate", sdFormat.format(endDate));

    	if(StringUtils.isNotBlank(pfpId)){
    		query.setString("pfpId", pfpId);
		}


    	Double result = (Double) query.uniqueResult();

        return result != null ? result.floatValue() : 0f;
	}

   @Override
   @SuppressWarnings("unchecked")
   public List<AdmRecognizeDetail> findThisDayAdmRecognizeDetail(String recognizeRecordId, String pfpCustomerInfoId, Date costDate, String orderType){
	   StringBuffer hql = new StringBuffer();
	   
	   	hql.append(" from AdmRecognizeDetail ");
		hql.append(" where customerInfoId = ? ");
		hql.append(" and admRecognizeRecord.recognizeRecordId = ? ");
		hql.append(" and costDate = ? ");
		hql.append(" and orderType = ? ");
		hql.append(" order by recordDetailId desc ");

		return (List<AdmRecognizeDetail>) super.getHibernateTemplate().find(hql.toString(), pfpCustomerInfoId, recognizeRecordId, costDate, orderType);
   }
   
   
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findRecognizeDetailExceptRefund(final String date) throws Exception{
		List<Object> result = (List<Object> ) getHibernateTemplate().execute(
				 
               new HibernateCallback<List<Object> >() {
               	
					public List<Object>  doInHibernate(Session session) throws HibernateException {
						
						StringBuffer sql = new StringBuffer();
						
						sql.append(" select b.recognize_order_id,a.cost_date,a.cost_price,a.tax,a.customer_info_id ");
						sql.append(" from adm_recognize_detail a ");
						sql.append(" join adm_recognize_record b ");
						sql.append(" on a.recognize_record_id = b.recognize_record_id ");
						sql.append(" where a.cost_date = :cost_date  ");
						sql.append(" and a.cost_price > 0  ");
						sql.append(" and a.order_type = :advanceOrderType ");
						sql.append(" and b.recognize_order_id not in( select order_id from pfp_refund_order where 1=1 and pay_type = :advancePayType and refund_status = :advanceRefundSuccess and refund_date= :cost_date ) ");
						
						
						log.info(" sql : "+sql);
				    	
						Query q = session.createSQLQuery(sql.toString())
								.setString("cost_date", date)
								.setString("advanceOrderType", EnumOrderType.SAVE.getTypeTag())
								.setString("advancePayType", EnumPfdAccountPayType.ADVANCE.getPayType())
								.setString("advanceRefundSuccess", EnumPfpRefundOrderStatus.SUCCESS.getStatus())
								.setString("cost_date", date);
								
						
						return q.list();
					}
               }
		);
   
		return result;
	}


}
