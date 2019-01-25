package com.pchome.akbadm.db.dao.pfbx.invalidclick;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;

public class PfbxInvalidClickDAO extends BaseDAO<PfbxInvalidClickVO, String> implements IPfbxInvalidClickDAO {

	List<PfbxInvalidClickVO> resultData = new ArrayList<PfbxInvalidClickVO>();
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<PfbxInvalidClickVO> getInvalidClickByCondition(final Map<String, String> conditionMap) {
		List<PfbxInvalidClickVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfbxInvalidClickVO>>() {
					@Override
					public List<PfbxInvalidClickVO> doInHibernate(Session session) throws HibernateException, SQLException {
						
						resultData = new ArrayList<PfbxInvalidClickVO>();
						Integer selectType = Integer.parseInt(conditionMap.get("selectType"));
						
						switch (selectType) {
							case 1:		//會員編號重複
								resultData = findInvalidClickByMemId(conditionMap, session);
								break;
							case 2:		//uuid重複
								resultData = findInvalidClickByUuid(conditionMap, session);
								break;
							case 3:		//uuid重複(排除iphone)
								resultData = findInvalidClickByUuidNotForIphone(conditionMap, session);
								break;
							case 4:		//ip重複
								resultData = findInvalidClickByIp(conditionMap, session);
								break;
							case 5:		//referer重複
								resultData = findInvalidClickByReferer(conditionMap, session);
								break;
							case 6:		//mouse_move_flag重複
								resultData = findInvalidClickByMouseMoveFlag(conditionMap, session);
								break;
							case 7:		//uuid與remote_ip都重複
								resultData = findInvalidClickByUuidAndRemoteIp(conditionMap, session);
								break;
							case 8:		//user_agent重複
								resultData = findInvalidClickByUserAgent(conditionMap, session);
								break;
							default:
								resultData = findInvalidClickByMemId(conditionMap, session);
								break;
						}

						return resultData;
					}
				});
    	return result;
	}
	
	//會員編號重複
	private List<PfbxInvalidClickVO> findInvalidClickByMemId(Map<String, String> conditionMap, Session session) {
		
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("mem_id, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		/*hql.append("and ad_click_id not in ");
		
		hql.append("(select a.ad_click_id from pfbx_invalid_traffic_detail a ");
		hql.append(" join pfbx_invalid_traffic b ");
		hql.append(" on a.inv_id = b.inv_id ");
		hql.append("where 1=1 ");
		
		if(conditionMap.get("startDate") != null){
			hql.append("and b.inv_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and b.inv_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		hql.append(") ");*/
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, mem_id  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, mem_id  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
		log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setMemId((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//uuid重複
	private List<PfbxInvalidClickVO> findInvalidClickByUuid(Map<String, String> conditionMap, Session session) {
		
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("uuid, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, uuid  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, uuid  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//			log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//			log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setUuid((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//uuid重複(排除iphone)
	private List<PfbxInvalidClickVO> findInvalidClickByUuidNotForIphone(Map<String, String> conditionMap, Session session) {
		
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("uuid, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and user_agent not like '%iPhone%' ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, uuid  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, uuid  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//				log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//				log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setUuid((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//ip重複
	private List<PfbxInvalidClickVO> findInvalidClickByIp(Map<String, String> conditionMap, Session session) {
		
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("remote_ip, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, remote_ip  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, remote_ip  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//			log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//			log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setRemoteIp((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//referer重複
	private List<PfbxInvalidClickVO> findInvalidClickByReferer(Map<String, String> conditionMap, Session session) {
			
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("referer, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, referer  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, referer  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//				log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//				log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setReferer((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//mouse_move_flag重複
	private List<PfbxInvalidClickVO> findInvalidClickByMouseMoveFlag(Map<String, String> conditionMap, Session session) {
			
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("mouse_move_flag, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, mouse_move_flag  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, mouse_move_flag  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//					log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//					log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				if(StringUtils.equals((objArray[4]).toString(), "T")){
					vo.setMouseMoveFlag("成功");
				} else {
					vo.setMouseMoveFlag("失敗");
				}
			} else {
				vo.setMouseMoveFlag("失敗");
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//uuid與remote_ip都重複
	private List<PfbxInvalidClickVO> findInvalidClickByUuidAndRemoteIp(Map<String, String> conditionMap, Session session) {
		
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("uuid, ");
		hql.append("remote_ip, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, uuid, remote_ip  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, uuid, remote_ip  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//				log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//				log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setUuid((objArray[4]).toString());
			}
			
			if(objArray[5] != null){
				vo.setRemoteIp((objArray[5]).toString());
			}
			
			vo.setCount((objArray[6]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[7]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	//referer重複
	private List<PfbxInvalidClickVO> findInvalidClickByUserAgent(Map<String, String> conditionMap, Session session) {
			
		List<PfbxInvalidClickVO> dataList = new ArrayList<PfbxInvalidClickVO>();
		
		StringBuffer hql = new StringBuffer();
		hql.append("select ");
		hql.append("record_date, ");
		hql.append("record_time, ");
		hql.append("pfbx_customer_info_id, ");
		hql.append("pfbx_position_id, ");
		hql.append("user_agent, ");
		hql.append("count(*) AS ca, ");
		hql.append("sum(ad_price) as p ");
		hql.append("from pfp_ad_click ");
		hql.append("where 1=1 ");
		hql.append("and malice_type = 0 ");
		
		if(conditionMap.get("pfbxCustomerInfoId") != null){
			hql.append("and pfbx_customer_info_id = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
		}
		
		if(conditionMap.get("pfbxPositionId") != null){
			hql.append("and pfbx_position_id = '" + conditionMap.get("pfbxPositionId") + "' ");
		}
		
		if(conditionMap.get("startDate") != null){
			hql.append("and record_date >= '" + conditionMap.get("startDate") + "' ");
		}
		
		if(conditionMap.get("endDate") != null){
			hql.append("and record_date <= '" + conditionMap.get("endDate") + "' ");
		}
		
		if(conditionMap.get("groupPositionId") != null){
			hql.append(" group by record_date, pfbx_customer_info_id, referer  ");
		} else {
			hql.append(" group by record_date, pfbx_customer_info_id, pfbx_position_id, referer  ");
		}
		
		if(conditionMap.get("mount") != null){
			hql.append(" having ca > " + conditionMap.get("mount"));
		} else {
			hql.append(" having ca >5 ");
		}
		hql.append(" order by record_date, p desc ");
		
		String sql = hql.toString();
//					log.info(">>> sql = " + sql);
		List<Object> QueryList = new ArrayList<Object>();
		try {
		Query query = session.createSQLQuery(sql);
		QueryList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
//					log.info(">>> dataList.size() = " + dataList.size());
		
		dataList = new ArrayList<PfbxInvalidClickVO>();
		
		for (int i=0; i<QueryList.size(); i++) {
			PfbxInvalidClickVO vo = new PfbxInvalidClickVO();
			
			Object[] objArray = (Object[]) QueryList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime((objArray[1]).toString());
			vo.setPfbxCustomerInfoId((objArray[2]).toString());
			vo.setPfbxPositionId((objArray[3]).toString());
			
			if(objArray[4] != null){
				vo.setUserAgent((objArray[4]).toString());
			}
			
			vo.setCount((objArray[5]).toString());
			vo.setPrice(df.format(Double.parseDouble((objArray[6]).toString())));
			
			dataList.add(vo);
		}
		
		return dataList;
	}
	
	
	@Override
	public List<PfpAdClick> getInvalidData(final Map<String, String> conditionMap) throws Exception {
		List<PfpAdClick> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdClick>>() {
					@Override
					public List<PfpAdClick> doInHibernate(Session session) throws HibernateException, SQLException {
						
						List<PfpAdClick> dataList = new ArrayList<PfpAdClick>();
						Integer selectType = Integer.parseInt(conditionMap.get("invSelectType"));
						
						StringBuffer hql = new StringBuffer();
						
						hql.append("from PfpAdClick ");
						hql.append("where 1=1 ");
						hql.append("and maliceType = 0 ");
						
						if(conditionMap.get("invPfbId") != null){
							hql.append("and pfbxCustomerInfoId = '" + conditionMap.get("invPfbId") + "' ");
						}
						
						if(conditionMap.get("invPfbxPositionId") != null){
							hql.append("and pfbxPositionId = '" + conditionMap.get("invPfbxPositionId") + "' ");
						}
						
						if(conditionMap.get("invDate") != null){
							hql.append("and recordDate = '" + conditionMap.get("invDate") + "' ");
						}
						
						switch (selectType) {
							case 1:		//會員編號重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and memId = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (memId is null or memId = '') ");
								}
								break;
							case 2:		//uuid重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and uuid = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (uuid is null or uuid = '') ");
								}
								break;
							case 3:		//uuid重複(排除iphone)
								hql.append("and userAgent not like '%iPhone%' ");
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and uuid = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (uuid is null or uuid = '') ");
								}
								break;
							case 4:		//ip重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and remoteIp = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (remoteIp is null or remoteIp = '') ");
								}
								break;
							case 5:		//referer重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and referer = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (referer is null or referer = '') ");
								}
								break;
							case 6:		//mouse_move_flag重複
								if(conditionMap.get("invInvalidNote1") != null){
									if(StringUtils.equals(conditionMap.get("invInvalidNote1"), "成功")){
										hql.append("and mouseMoveFlag = 'T' ");
									} else {
										hql.append("and (mouseMoveFlag is null or mouseMoveFlag = '') ");
									}
								} else {
									hql.append("and (mouseMoveFlag is null or mouseMoveFlag = '') ");
								}
								break;
							case 7:		//uuid與remote_ip都重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and uuid = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (uuid is null or uuid = '') ");
								}
								if(conditionMap.get("invInvalidNote2") != null){
									hql.append("and remoteIp = '" + conditionMap.get("invInvalidNote2") + "' ");
								} else {
									hql.append("and (remoteIp is null or remoteIp = '') ");
								}
								break;
							case 8:		//user_agent重複
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and userAgent = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (userAgent is null or userAgent = '') ");
								}
								break;
							default:
								if(conditionMap.get("invInvalidNote1") != null){
									hql.append("and memId = '" + conditionMap.get("invInvalidNote1") + "' ");
								} else {
									hql.append("and (memId is null or memId = '') ");
								}
								break;
						}

						String sql = hql.toString();
						Query query = session.createQuery(sql);

						return query.list();
					}
				});
    	return result;
	}
	
	
	
}
