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
import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficDetailVO;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;
import com.pchome.enumerate.pfbx.invalidclick.EnumPfbInvalidTrafficType;

public class PfbxInvalidTrafficDetailDAO extends BaseDAO<PfbxInvalidTrafficDetail, String> implements IPfbxInvalidTrafficDetailDAO {

	List<PfbxInvalidTrafficDetailVO> resultData = new ArrayList<PfbxInvalidTrafficDetailVO>();
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByCondition(final Map<String, String> conditionMap) {
		List<PfbxInvalidTrafficDetailVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfbxInvalidTrafficDetailVO>>() {
					@Override
					public List<PfbxInvalidTrafficDetailVO> doInHibernate(Session session) throws HibernateException, SQLException {
						
						StringBuffer hql = new StringBuffer();
						hql.append("select ");
						hql.append("a.recordTime, ");
						hql.append("a.pfbxPositionId, ");
						hql.append("a.adType, ");
						hql.append("a.adClk, ");
						hql.append("a.adPrice ");
						hql.append("from PfbxInvalidTrafficDetail a ");
						hql.append("where 1=1 ");
						
						if(conditionMap.get("invId") != null){
							hql.append("and a.pfbxInvalidTraffic.invId = '" + conditionMap.get("invId") + "' ");
						}
						
						hql.append(" order by a.pfbxPositionId, a.recordTime, a.adType ");
						
						String sql = hql.toString();
						log.info(">>> sql = " + sql);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info(">>> dataList.size() = " + dataList.size());
						
						resultData = new ArrayList<PfbxInvalidTrafficDetailVO>();
						
						for (int i=0; i<dataList.size(); i++) {
							PfbxInvalidTrafficDetailVO vo = new PfbxInvalidTrafficDetailVO();
							
							Object[] objArray = (Object[]) dataList.get(i);
							
							vo.setRecordTime(objArray[0].toString());
							vo.setPfbxPositionId(objArray[1].toString());
							
							if(StringUtils.equals("1", objArray[2].toString())){
								vo.setAdType("關鍵字");
							}  else {
								vo.setAdType("聯播網");
							}
							
							vo.setAdClk(objArray[3].toString());
							vo.setAdPrice(df.format(Double.parseDouble((objArray[4]).toString())));
							
							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}
	
	@Override
	public List<PfbxInvalidTrafficDetailVO> getInvalidTrafficDetailByDownload(final Map<String, String> conditionMap) {
		List<PfbxInvalidTrafficDetailVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfbxInvalidTrafficDetailVO>>() {
					@Override
					public List<PfbxInvalidTrafficDetailVO> doInHibernate(Session session) throws HibernateException, SQLException {
						
						StringBuffer hql = new StringBuffer();
						hql.append("select ");
						hql.append("a.pfbxInvalidTraffic.invDate, ");
						hql.append("a.pfbxInvalidTraffic.pfbId, ");
						hql.append("a.pfbxPositionId, ");
						hql.append("a.recordTime, ");
						hql.append("a.adClk, ");
						hql.append("a.adPrice, ");
						hql.append("(a.adPrice*a.pfbxInvalidTraffic.invPfbBonus)/a.pfbxInvalidTraffic.invPrice, ");
						hql.append("a.pfbxInvalidTraffic.invType, ");
						hql.append("a.pfbxInvalidTraffic.invNote ");
						hql.append("from PfbxInvalidTrafficDetail a ");
						hql.append("where 1=1 ");
						
						if(conditionMap.get("pfbxCustomerInfoId") != null){
							hql.append("and a.pfbxInvalidTraffic.pfbId = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
						}
						
						if(conditionMap.get("startDate") != null){
							hql.append("and a.pfbxInvalidTraffic.invDate >= '" + conditionMap.get("startDate") + "' ");
						}
						
						if(conditionMap.get("endDate") != null){
							hql.append("and a.pfbxInvalidTraffic.invDate <= '" + conditionMap.get("endDate") + "' ");
						}
						
						if(conditionMap.get("closeStartDate") != null){
							hql.append("and a.pfbxInvalidTraffic.closeDate > '" + conditionMap.get("closeStartDate") + "' ");
						}
						
						if(conditionMap.get("closeEndDate") != null){
							hql.append("and a.pfbxInvalidTraffic.closeDate <= '" + conditionMap.get("closeEndDate") + "' ");
						}
						
						if(conditionMap.get("selectType") != null){
							hql.append("and a.pfbxInvalidTraffic.invType = '" + conditionMap.get("selectType") + "' ");
						}
						
						hql.append(" order by a.pfbxInvalidTraffic.invDate, a.pfbxInvalidTraffic.pfbId, a.pfbxPositionId, a.recordTime ");
						
						String sql = hql.toString();
						log.info(">>> sql = " + sql);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createQuery(sql);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info(">>> dataList.size() = " + dataList.size());
						
						resultData = new ArrayList<PfbxInvalidTrafficDetailVO>();
						
						for (int i=0; i<dataList.size(); i++) {
							PfbxInvalidTrafficDetailVO vo = new PfbxInvalidTrafficDetailVO();
							
							Object[] objArray = (Object[]) dataList.get(i);
							
							vo.setInvDate(dateFormate.format((objArray[0])));
							vo.setPfbId(objArray[1].toString());
							vo.setPfbxPositionId(objArray[2].toString());
							vo.setRecordTime(objArray[3].toString());
							vo.setAdClk(objArray[4].toString());
							vo.setAdPrice(df.format(Double.parseDouble((objArray[5]).toString())));
							vo.setInvPfbBonus(df2.format(Double.parseDouble((objArray[6]).toString())));
							
							for(EnumPfbInvalidTrafficType enumPfbInvalidTrafficType:EnumPfbInvalidTrafficType.values()){
								if(StringUtils.equals(enumPfbInvalidTrafficType.getType(), objArray[7].toString())){
									vo.setInvType(enumPfbInvalidTrafficType.getChName());
								}
							}
							
							if(objArray[8] != null){
								vo.setInvNote((objArray[8]).toString());
							}
							
							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}
}
