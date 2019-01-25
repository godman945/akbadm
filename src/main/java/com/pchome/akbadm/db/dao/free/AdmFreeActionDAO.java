package com.pchome.akbadm.db.dao.free;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.vo.ad.AdmFreeActionVO;

public class AdmFreeActionDAO extends BaseDAO<AdmFreeAction, String> implements IAdmFreeActionDAO{

	@SuppressWarnings("unchecked")
	public List<AdmFreeAction> findFreeAction(String actionId) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from AdmFreeAction where actionId = ? ");
		
		return super.getHibernateTemplate().find(hql.toString(), actionId);
	}

	@SuppressWarnings("unchecked")
	public List<AdmFreeAction> findFreeAction(Map<String, String> conditionMap) throws Exception {

		List<Object> paramList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer();
		hql.append(" from AdmFreeAction where 1=1");

		if (conditionMap.containsKey("inviledDate")) {
			hql.append(" and inviledDate < '" + conditionMap.get("inviledDate") + "' ");
			//paramList.add(conditionMap.get("inviledDate"));
		}
		if (conditionMap.containsKey("retrievedFlag")) {
			hql.append(" and retrievedFlag = ? ");
			paramList.add(conditionMap.get("retrievedFlag"));
		}
		if (conditionMap.containsKey("orderBy")) {
			hql.append(" order by " + conditionMap.get("orderBy"));
		}
		if (conditionMap.containsKey("desc")) {
			hql.append(" desc");
		}

		return super.getHibernateTemplate().find(hql.toString(), paramList.toArray());
	}
	
	@Override
	public List<AdmFreeActionVO> getAdmFreeActionData(final Map<String, String> conditionMap){
		List<AdmFreeActionVO> result = (List<AdmFreeActionVO>) getHibernateTemplate().execute(
				new HibernateCallback<List<AdmFreeActionVO>>() {
					@SuppressWarnings("unchecked")
					public List<AdmFreeActionVO> doInHibernate(Session session) throws HibernateException, SQLException {
						
						List<AdmFreeActionVO> list = new ArrayList<AdmFreeActionVO>();
						DecimalFormat df = new DecimalFormat("###,###,###,###");
						SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
						
						StringBuffer sql = new StringBuffer();
						
						sql.append(" select ");
						sql.append(" action_id, ");
						sql.append(" action_name, ");
						sql.append(" ifnull(payment,'N'), ");
						sql.append(" gift_condition, ");
						sql.append(" gift_money, ");
						sql.append(" action_start_date, ");
						sql.append(" action_end_date, ");
						sql.append(" inviled_date, ");
						sql.append(" retrieved_flag, ");
						sql.append(" note, ");
						sql.append(" ifnull(gift_style,'N'), ");
						sql.append(" (select count(*) from adm_free_gift where adm_free_action = action_id) * gift_money,  ");
						sql.append(" ifnull(shared,'N'), ");
						sql.append(" (select count(*) from adm_free_record where action_id = adm_free_action.action_id) * gift_money ");
						sql.append(" from adm_free_action  ");
						sql.append(" where 1=1 ");
						
						if (conditionMap.get("searchStartDate") != null) {
							sql.append(" and action_start_date >= '" + conditionMap.get("searchStartDate") + "' ");
						}
						
						if (conditionMap.get("searchEndDate") != null) {
							sql.append(" and action_start_date <= '" + conditionMap.get("searchEndDate") + "' ");
						}
						
						if (conditionMap.get("inviledStartDate") != null) {
							sql.append(" and inviled_date >= '" + conditionMap.get("inviledStartDate") + "' ");
						}
						
						if (conditionMap.get("inviledEndDate") != null) {
							sql.append(" and inviled_date <= '" + conditionMap.get("inviledEndDate") + "' ");
						}
						
						if (conditionMap.get("payment") != null) {
							sql.append(" and ifnull(payment,'N') = '" + conditionMap.get("payment") + "'");
						}
						
						if (conditionMap.get("giftStyle") != null) {
							sql.append(" and ifnull(gift_style,'1') = '" + conditionMap.get("giftStyle") + "'");
						}
						
						sql.append(" order by action_id desc ");
						
						int page = -1;
						int pageSize = 50;
						
						if (conditionMap.get("page") != null) {
							page = Integer.parseInt(conditionMap.get("page"));
						}
						
						if (conditionMap.get("pageSize") != null) {
							pageSize = Integer.parseInt(conditionMap.get("pageSize"));
						}
						
						List<Object> dataList = new ArrayList<Object>();
						try {
							Query query = session.createSQLQuery(sql.toString());
							dataList = query.list();
							if(page==-1){
								dataList = session.createSQLQuery(sql.toString()).list();
							}else{
								dataList = session.createSQLQuery(sql.toString())
								.setFirstResult((page-1)*pageSize)  
								.setMaxResults(pageSize)
								.list();
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info(">>> dataList.size() = " + dataList.size());
						
						for (int i=0; i<dataList.size(); i++) {
							AdmFreeActionVO vo = new AdmFreeActionVO();
							
							Object[] objArray = (Object[]) dataList.get(i);
							
							vo.setActionId((objArray[0]).toString());
							vo.setActionName((objArray[1]).toString());
							
							String payment = "免儲值";
							if(StringUtils.equals((objArray[2]).toString(), "Y")){
								payment = "要儲值";
							}
							vo.setPayment(payment);
							
							vo.setGiftCondition(df.format(new Double((objArray[3]).toString())));
							vo.setGiftMoney(df.format(new Double((objArray[4]).toString())));
							
							if(objArray[5] != null){
								vo.setActionStartDate(dateFormate.format((Date) objArray[5]));
							}
							
							if(objArray[6] != null){
								vo.setActionEndDate(dateFormate.format((Date) objArray[6]));
							}
							
							if(objArray[7] != null){
								vo.setInviledDate((objArray[7]).toString());
							}
							
							String retrievedFlag = "未回收";
							if(StringUtils.equals((objArray[8]).toString(), "y")){
								retrievedFlag = "已回收";
							}
							vo.setRetrievedFlag(retrievedFlag);
							
							if(objArray[9] != null){
								vo.setNote((objArray[9]).toString());	
							}
							
							String giftStyle = "帳戶註冊頁";
							if(StringUtils.equals((objArray[10]).toString(), "2")){
								giftStyle = "帳戶儲值頁";
							}
							vo.setGiftStyle(giftStyle);
							
							vo.setTotalGiftMoney(df.format(new Double((objArray[11]).toString())));
							
							String shared = "N";
							String sharedNote = "否";
							if(StringUtils.equals((objArray[12]).toString(), "Y")){
								shared = (objArray[12]).toString();
								sharedNote = "是";
							}
							vo.setShared(shared);
							vo.setSharedNote(sharedNote);
							
							vo.setTotalRecordMoney(df.format(new Double((objArray[13]).toString())));
							
							list.add(vo);
						}
						
						return list;
					}
				});
		
		return result;
	}
}
