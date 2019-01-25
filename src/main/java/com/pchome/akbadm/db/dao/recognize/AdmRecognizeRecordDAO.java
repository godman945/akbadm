package com.pchome.akbadm.db.dao.recognize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.vo.AdActionDalilyReportVO;
import com.pchome.enumerate.recognize.EnumOrderType;

public class AdmRecognizeRecordDAO extends BaseDAO <AdmRecognizeRecord, String> implements IAdmRecognizeRecordDAO{

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findAdvanceRecognizeRecord(String customerInfoId){

		StringBuffer hql =  new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where orderRemainZero = 'N' ");
		hql.append(" and (orderType = ? or orderType = ? or orderType = ? ) ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by saveDate , recognizeRecordId ");

		list.add(EnumOrderType.SAVE.getTypeTag());
		list.add(EnumOrderType.GIFT.getTypeTag());
		list.add(EnumOrderType.FEEDBACK.getTypeTag());
        list.add(customerInfoId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findLaterRecognizeRecord(String customerInfoId){

		StringBuffer hql =  new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where orderRemainZero = 'N' ");
		hql.append(" and orderType = ? ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by saveDate , recognizeRecordId ");

		list.add(EnumOrderType.VIRTUAL.getTypeTag());
        list.add(customerInfoId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId){

		StringBuffer hql =  new StringBuffer();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by recognizeRecordId  desc ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findAdvanceRecognizeRecord(String customerInfoId, Date startDate) {
		StringBuffer hql =  new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append("from AdmRecognizeRecord ");
        hql.append("where (orderType = ? or orderType = ? or orderType = ? ) ");
		hql.append("and saveDate <= ? ");
		hql.append(" and updateDate >= ? ");
        hql.append("and pfpCustomerInfo.customerInfoId = ? ");
		hql.append("order by createDate asc ");

		list.add(EnumOrderType.SAVE.getTypeTag());
		list.add(EnumOrderType.GIFT.getTypeTag());
		list.add(EnumOrderType.FEEDBACK.getTypeTag());
        list.add(startDate);
        list.add(startDate);
        list.add(customerInfoId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findLaterRecognizeRecord(String customerInfoId, Date startDate) {
		StringBuffer hql =  new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmRecognizeRecord ");
        hql.append(" where orderType = ? ");
		hql.append(" and saveDate <= ? ");
		hql.append(" and updateDate >= ? ");
        hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by createDate asc ");

		list.add(EnumOrderType.VIRTUAL.getTypeTag());
        list.add(startDate);
        list.add(startDate);
        list.add(customerInfoId);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findAfterSaveDateRecognizeRecord(String customerInfoId){

		StringBuffer hql =  new StringBuffer();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" order by createDate asc ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId,	String recognizeOrderId, String orderType){

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and recognizeOrderId = ? ");
		hql.append(" and orderType = ? ");

		return super.getHibernateTemplate().find(hql.toString(), customerInfoId, recognizeOrderId, orderType);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId,	String recognizeOrderId, String orderType, int orderPrice, Date saveDate){

		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where 1=1");
		if(StringUtils.isNotBlank(customerInfoId)) {
			hql.append(" and pfpCustomerInfo.customerInfoId = ? ");
			params.add(customerInfoId);
		}
		if(StringUtils.isNotBlank(recognizeOrderId)) {
			hql.append(" and recognizeOrderId = ? ");
			params.add(recognizeOrderId);
		}
		if(StringUtils.isNotBlank(orderType)) {
			hql.append(" and orderType = ? ");
			params.add(orderType);
		}
		if(orderPrice >= 0) {
			hql.append(" and orderPrice >= ? ");
			params.add((float)orderPrice);
		}
		if(saveDate != null) {
			hql.append(" and saveDate = ? ");
			params.add(saveDate);
		}
		hql.append(" order by recognizeRecordId ");
		//System.out.println("hql = " + hql);

		return super.getHibernateTemplate().find(hql.toString(), params.toArray());
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(Date saveDate){

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where 1 = 1 ");
		hql.append(" and saveDate = ? ");
		hql.append(" order by recognizeRecordId ");

		return super.getHibernateTemplate().find(hql.toString(), saveDate);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<Object[]> findPfpRemain(Date saveDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select pfpCustomerInfo.customerInfoId, sum(orderPrice)  ");
		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where 1 = 1 ");
		hql.append(" and saveDate <= ? ");
		hql.append(" group by pfpCustomerInfo.customerInfoId ");

		return super.getHibernateTemplate().find(hql.toString(), saveDate);
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public List<Object[]> findRecognizeRemain(Date saveDate) {

		StringBuffer hql = new StringBuffer();

		/*
		 * 
		 * SELECT `customer_info_id`,sum(`order_remain`)  FROM `adm_recognize_record` WHERE  save_date <= '2016-06-22' and order_remain > 0
group by `customer_info_id`
		 */
		
		hql.append(" select pfpCustomerInfo.customerInfoId, sum(orderRemain)  ");
		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where 1 = 1 ");
		hql.append(" and saveDate <= ? ");
		hql.append(" and orderRemain > 0 ");
		hql.append(" group by pfpCustomerInfo.customerInfoId ");
		

		
		
		return super.getHibernateTemplate().find(hql.toString(), saveDate);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(Date sDate, Date eDate,
			String pfpCustInfoId) throws Exception {

		StringBuffer sb = new StringBuffer();

		sb.append(" from AdmRecognizeRecord ");
		sb.append(" where saveDate >= ? ");
		sb.append(" and saveDate <= ? ");
		if (StringUtils.isNotEmpty(pfpCustInfoId)) {
			sb.append(" and pfpCustomerInfo.customerInfoId in (" + pfpCustInfoId + ")");
		}
		sb.append(" order by recognizeRecordId desc ");

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return super.getHibernateTemplate().find(hql, sDate, eDate);
	}

	/**
	 * 在統計計算贈送廣告金之前，先刪除 "新戶好事成金(FRA201402240001)"、"續攤享禮金(FRA201402240002)"
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	@Override
    public Integer deleteRecognizeRecordBySaveDate(Date saveDate, String customerInfoId) throws Exception {

		StringBuffer hql = new StringBuffer();
		hql.append(" delete from AdmRecognizeRecord ");
		hql.append(" where recognizeOrderId in('FRA201402240001', 'FRA201402240002')");
		hql.append(" and orderType = 'G' ");
		hql.append(" and saveDate = ? ");
		hql.append(" and pfpCustomerInfo.customerInfoId = ?");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), saveDate, customerInfoId);
	}

	/**
	 * 刪除所有帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @return
	 */
	@Override
    public Integer deleteRecognizeRecordForG6000() throws Exception {
        Session session = getSession();

		StringBuffer hql = new StringBuffer();
		hql.append(" delete from adm_recognize_record ");
		hql.append(" where recognize_order_id in('FRA201402240003')");
		hql.append(" and order_type = 'G' ");
		hql.append(" and customer_info_id in (select customer_info_id from pfp_customer_info where activate_date is null )");
		int updatedEntities = session.createSQLQuery( hql.toString() )
		        .executeUpdate();

		session.flush();

		return Integer.valueOf(updatedEntities);
	}

	/**
	 * 依日期刪除帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @return
	 */
	@Override
    public Integer deleteRecognizeRecordForG6000(Date saveDate) throws Exception {
        Session session = getSession();

		StringBuffer hql = new StringBuffer();
		hql.append(" delete from adm_recognize_record ");
		hql.append(" where recognize_order_id in('FRA201402240003')");
		hql.append(" and order_type = 'G' ");
		hql.append(" and save_date = :saveDate ");
		hql.append(" and customer_info_id in (select customer_info_id from pfp_customer_info where activate_date is null )");
		int updatedEntities = session.createSQLQuery( hql.toString() )
		        .setDate( "saveDate", saveDate )
		        .executeUpdate();

		session.flush();

		return Integer.valueOf(updatedEntities);
	}

	/**
	 * 依日期、帳號條件來刪除帳戶未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	@Override
    public Integer deleteRecognizeRecordForG6000(Date saveDate, String customerInfoId) throws Exception {
        Session session = getSession();

		StringBuffer hql = new StringBuffer();
		hql.append(" delete from adm_recognize_record ");
		hql.append(" where recognize_order_id in('FRA201402240003')");
		hql.append(" and order_type = 'G' ");
		hql.append(" and save_date = :saveDate ");
		hql.append(" and customer_info_id = :customerInfoId");
		int updatedEntities = session.createSQLQuery( hql.toString() )
		        .setDate( "saveDate", saveDate )
		        .setString("customerInfoId", customerInfoId)
		        .executeUpdate();

		session.flush();

		return Integer.valueOf(updatedEntities);
	}

//	public Integer deleteRecordAfterDate(Date date){
//
//		StringBuffer hql = new StringBuffer();
//
//		hql.append(" delete from AdmRecognizeRecord ");
//		hql.append(" where saveDate >= ? ");
//		hql.append(" and orderType = ? ");
//
//		return this.getHibernateTemplate().bulkUpdate(hql.toString(), date, EnumOrderType.GIFT.getTypeTag());
//	}

	@Override
    @SuppressWarnings("unchecked")
	public TreeMap<String, List<AdmRecognizeRecord>> getRecognizeRecordByCustomerInfoIdList(String startDate, String endDate, List<String> pfpAdCustomerInfoIds, List<String> orderTypes) throws Exception {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		HashMap<String, Object> sqlParams = new HashMap<String, Object>();

		StringBuffer hql = new StringBuffer();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where 1 = 1 ");

		if (StringUtils.isNotEmpty(startDate)) {
			hql.append(" and saveDate >= :startDate");
			sqlParams.put("startDate", sdf.parse(startDate));
		}

		if (StringUtils.isNotEmpty(endDate)) {
			hql.append(" and saveDate <= :endDate");
			sqlParams.put("endDate", sdf.parse(endDate));
		}

		if (pfpAdCustomerInfoIds != null  && pfpAdCustomerInfoIds.size() > 0) {
			hql.append(" and pfpCustomerInfo.customerInfoId in (:pfpCustomerInfoIds)");
			sqlParams.put("pfpCustomerInfoIds", pfpAdCustomerInfoIds);
		}

		if(orderTypes != null && orderTypes.size() > 0) {
			hql.append(" and orderType in (:orderTypes) ");
			sqlParams.put("orderTypes", orderTypes);
		}
		hql.append(" order by pfpCustomerInfo.customerInfoId desc ");
		log.info("hql = " + hql.toString());
		log.info("startDate = " + startDate);
		log.info("endDate = " + endDate);
		log.info("orderTypes = " + orderTypes);
		log.info("pfpAdCustomerInfoIds = " + pfpAdCustomerInfoIds);

		Query query = this.getSession().createQuery(hql.toString());
        for (String paramName:sqlParams.keySet()) {
        	if(paramName.equals("pfpCustomerInfoIds") || paramName.equals("orderTypes")) {
        		query.setParameterList(paramName, (ArrayList<String>)sqlParams.get(paramName));
        	} else {
        		query.setParameter(paramName, sqlParams.get(paramName));
        	}
        }

		// 將得到的廣告成效結果，設定成 Map, 以方便用 adKeywordSeq 抓取資料
		TreeMap<String, List<AdmRecognizeRecord>> admRecognizeRecordMap = new TreeMap<String, List<AdmRecognizeRecord>>();
		List<AdmRecognizeRecord> admRecognizeRecords = query.list();
		List<AdmRecognizeRecord> admRecognizeRecordList = new ArrayList<AdmRecognizeRecord>();
		System.out.println("admRecognizeRecords.size() = " + admRecognizeRecords.size());
		String tmp_customerInfoId = "";		// 上一筆的customer_info_id
		for(AdmRecognizeRecord admRecognizeRecord:admRecognizeRecords) {
			System.out.println(tmp_customerInfoId +" => " + admRecognizeRecord.getRecognizeRecordId());
			// 上一筆的customer_info_id 如果跟這一筆的 customer_info_id 不同，就要 put 進 Map
			if(StringUtils.isNotBlank(tmp_customerInfoId) & !tmp_customerInfoId.equals(admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoId())) {
				admRecognizeRecordMap.put(tmp_customerInfoId, admRecognizeRecordList);
				admRecognizeRecordList = new ArrayList<AdmRecognizeRecord>();
			}
			admRecognizeRecordList.add(admRecognizeRecord);
			tmp_customerInfoId = admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoId();
		}
		// 最後一筆的資料
		if(admRecognizeRecords != null && admRecognizeRecords.size() > 0) {
			admRecognizeRecordMap.put(tmp_customerInfoId, admRecognizeRecordList);
		}


		return admRecognizeRecordMap;
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(Date sDate, Date eDate, String pfpCustomerInfoId, String OrderType){

		StringBuffer hql = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		hql.append(" from AdmRecognizeRecord ");
		hql.append(" where pfpCustomerInfo.customerInfoId = ? ");
		hql.append(" and orderType = ? ");
		hql.append(" and saveDate >= ? ");
		hql.append(" and saveDate <= ?");
		hql.append(" order by recognizeRecordId ");

		list.add(pfpCustomerInfoId);
		list.add(OrderType);
		list.add(sDate);
		list.add(eDate);

		return super.getHibernateTemplate().find(hql.toString(), list.toArray());
	}

	@Override
    public Integer deleteAdmRecognizeRecord(String orderId) {

		StringBuffer hql = new StringBuffer();

	    hql.append("delete from admRecognizeRecord ");
	    hql.append("where recognizeOrderId = ? ");

	    return this.getHibernateTemplate().bulkUpdate(hql.toString(), orderId);
	}

	@Override
    @SuppressWarnings("unchecked")
	public List<AdmRecognizeRecord> findRecognizeRecords(Map<String, Object> conditionMap) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuffer sb = new StringBuffer();
		List<Object> list = new ArrayList<Object>();

		sb.append(" from AdmRecognizeRecord where 1=1 ");
		if (conditionMap.containsKey("pfpCustomerInfoId")) {
			sb.append(" and pfpCustomerInfo.customerInfoId = ? ");
			list.add(conditionMap.get("pfpCustomerInfoId"));
		}
		if (conditionMap.containsKey("orderType")) {
			sb.append(" and orderType = ? ");
			list.add(conditionMap.get("orderType"));
		}
		if (conditionMap.containsKey("startDate")) {
			sb.append(" and saveDate >= ? ");
			list.add(sdf.parse(conditionMap.get("startDate") + " 00:00:00"));
		}
		if (conditionMap.containsKey("endDate")) {
			sb.append(" and saveDate <= ?");
			list.add(sdf.parse(conditionMap.get("endDate") + " 23:59:59"));
		}
		if (conditionMap.containsKey("orderBy")) {
			sb.append(" order by " + conditionMap.get("orderBy"));
		}
		if (conditionMap.containsKey("desc")) {
			sb.append(" desc");
		}

		String hql = sb.toString();
		log.info(">>> hql = " + hql);

		return super.getHibernateTemplate().find(hql, list.toArray());
	}

	@Override
    public Integer deleteRecordAfterDate(Date startDate) {

		StringBuffer hql = new StringBuffer();

		hql.append(" delete from AdmRecognizeRecord ");
		hql.append(" where saveDate >= ? ");

		return this.getHibernateTemplate().bulkUpdate(hql.toString(), startDate);
	}

	//取得每日廣告花費日誌
	@Override
    @SuppressWarnings("unchecked")
	public List<AdActionDalilyReportVO> getAdAdmRecognizeDalilyReport() throws Exception {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" SELECT ");
	    sql.append(" c.customer_info_id, ");
	    sql.append(" b.recognize_order_id, ");
	    sql.append(" c.customer_info_title, ");
	    sql.append(" a.order_type, ");
	    sql.append(" DATE_FORMAT(c.create_date,'%Y/%m/%d'), ");
	    sql.append(" c.total_add_money, ");
	    sql.append(" DATE_FORMAT(a.cost_date,'%Y/%m/%d'), ");
	    sql.append(" a.cost_price, ");
	    sql.append(" b.save_date, ");
	    sql.append(" b.order_price, ");
	    sql.append(" timestampdiff(day,DATE_FORMAT(c.create_date,'%Y/%m/%d'),DATE_FORMAT(a.cost_date,'%Y/%m/%d')), ");
	    sql.append(" c.remain ");
	    sql.append(" FROM  ");
	    sql.append(" adm_recognize_detail a,  ");
	    sql.append(" adm_recognize_record b, ");
	    sql.append(" pfp_customer_info c ");
	    sql.append(" WHERE ");
	    sql.append(" a.recognize_record_id = b.recognize_record_id ");
	    sql.append(" and a.customer_info_id = b.customer_info_id ");
	    sql.append(" and b.customer_info_id = c.customer_info_id ");
	    sql.append(" and a.customer_info_id =c.customer_info_id ");

	    //test
//	    sql.append(" and a.customer_info_id ='AC2013071700001' ");

	    sql.append(" order by c.customer_info_id ");

//	    sql.append(" select a.customerInfoId from PfpCustomerInfo a ");
//	    dataList = super.getHibernateTemplate().find(sql.toString());

	    Query query = super.getSession().createSQLQuery(sql.toString());
	    List<Object> dataList = null;
	    dataList =  query.list();


	    List<AdActionDalilyReportVO> voList = new ArrayList<AdActionDalilyReportVO>();

	    int sLagTime = 0;
	    int gLagTime = 0;
	    int fLagTime = 0;
	    int oLagTime = 0;
	    int minusMoney = 0;
	    boolean firstFlag = true;
//	    System.out.println(">>>>"+dataList.size());
	    for (Object object : dataList) {
	    Object objArray [] = (Object[]) object;
	    AdActionDalilyReportVO vo = new AdActionDalilyReportVO();
	    vo.setCustomerInfoId(objArray[0].toString());
	    vo.setRecognizeOrderId(objArray[1].toString());
	    vo.setCustomerInfoTitle(objArray[2].toString());

	    if(objArray[3].toString().equals("S")){
		vo.setOrderType("儲值金");
		if(firstFlag){
		    vo.setLagTime(objArray[10].toString());
		    sLagTime = Integer.parseInt(objArray[10].toString());
		}else{
		    int lagTime = Integer.parseInt(objArray[10].toString()) - sLagTime;
		    vo.setLagTime(String.valueOf(lagTime));
		}
	    }else if(objArray[3].toString().equals("G")){
		vo.setOrderType("廣告金");
		if(firstFlag){
		    vo.setLagTime(objArray[10].toString());
		    gLagTime = Integer.parseInt(objArray[10].toString());
		}else{
		    int lagTime = Integer.parseInt(objArray[10].toString()) - gLagTime;
		    vo.setLagTime(String.valueOf(lagTime));
		}
	    }else if(objArray[3].toString().equals("F")){
		vo.setOrderType("回饋金");
		if(firstFlag){
		    vo.setLagTime(objArray[10].toString());
		    fLagTime = Integer.parseInt(objArray[10].toString());
		}else{
		    int lagTime = Integer.parseInt(objArray[10].toString()) - fLagTime;
		    vo.setLagTime(String.valueOf(lagTime));
		}
	    }else if(objArray[3].toString().equals("V")){
		vo.setOrderType("其它");
		if(firstFlag){
		    vo.setLagTime(objArray[10].toString());
		    oLagTime = Integer.parseInt(objArray[10].toString());
		}else{
		    int lagTime = Integer.parseInt(objArray[10].toString()) - oLagTime;
		    vo.setLagTime(String.valueOf(lagTime));
		}
	    }

	    vo.setCreateDate(objArray[4].toString());
	    vo.setTotalAddMoney(objArray[5].toString());
	    vo.setCostDate(objArray[6].toString());
	    vo.setCostPrice(objArray[7].toString());
	    vo.setSaveDate(objArray[8].toString());
	    vo.setOrderPrice(objArray[9].toString());
	    vo.setRemain(objArray[11].toString());

	    if(firstFlag){
		String minus = String.valueOf(Integer.parseInt(objArray[5].toString().replace(".0", "").toString()));
		minusMoney = Integer.parseInt(minus);
		String cost = objArray[7].toString().replace(".0", "");
		int costInt = Integer.parseInt(cost);
		vo.setMinusMoney(String.valueOf(minusMoney - costInt) );
		minusMoney = minusMoney - costInt;
	    }else{
	    int cost = (int)Math.floor(Double.valueOf(objArray[7].toString()));
		vo.setMinusMoney(String.valueOf(minusMoney - cost) );
		minusMoney = minusMoney - cost;
	    }
	    voList.add(vo);
	    firstFlag = false;
	}
	    return voList;
	}
}
