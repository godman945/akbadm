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
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidClickVO;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;
import com.pchome.enumerate.pfbx.invalidclick.EnumPfbInvalidTrafficType;

public class PfbxInvalidTrafficDAO extends BaseDAO<PfbxInvalidTraffic, String> implements IPfbxInvalidTrafficDAO {

	List<PfbxInvalidTrafficVO> resultData = new ArrayList<PfbxInvalidTrafficVO>();
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings("unchecked")
	public List<PfbxInvalidTraffic> findInvalidTraffic(String pfbId){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfbxInvalidTraffic ");
		hql.append(" where closeDate is null ");
		hql.append(" and pfbId =?  ");
		
		return (List<PfbxInvalidTraffic>) super.getHibernateTemplate().find(hql.toString(), pfbId);
	}
	
	@Override
	public List<PfbxInvalidTrafficVO> getInvalidTrafficByCondition(final Map<String, String> conditionMap) {
		List<PfbxInvalidTrafficVO> result = getHibernateTemplate().execute(
				new HibernateCallback<List<PfbxInvalidTrafficVO>>() {
					@Override
					public List<PfbxInvalidTrafficVO> doInHibernate(Session session) throws HibernateException {
						
						StringBuffer hql = new StringBuffer();
						hql.append("select ");
						hql.append("invId, ");
						hql.append("pfbId, ");
						hql.append("invDate, ");
						hql.append("invType, ");
						hql.append("invNote, ");
						hql.append("invPrice, ");
						hql.append("invPfbBonus, ");
						hql.append("closeDate, ");
						hql.append("updateTime ");
						hql.append("from PfbxInvalidTraffic ");
						hql.append("where 1=1 ");
						
						if(conditionMap.get("pfbxCustomerInfoId") != null){
							hql.append("and pfbId = '" + conditionMap.get("pfbxCustomerInfoId") + "' ");
						}
						
						if(conditionMap.get("startDate") != null){
							hql.append("and invDate >= '" + conditionMap.get("startDate") + "' ");
						}
						
						if(conditionMap.get("endDate") != null){
							hql.append("and invDate <= '" + conditionMap.get("endDate") + "' ");
						}
						
						if(conditionMap.get("closeStartDate") != null){
							hql.append("and closeDate > '" + conditionMap.get("closeStartDate") + "' ");
						}
						
						if(conditionMap.get("closeEndDate") != null){
							hql.append("and closeDate <= '" + conditionMap.get("closeEndDate") + "' ");
						}
						
						if(conditionMap.get("selectType") != null){
							hql.append("and invType = '" + conditionMap.get("selectType") + "' ");
						}
						
						hql.append(" order by invDate, pfbId, invType ");
						
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
						
						resultData = new ArrayList<PfbxInvalidTrafficVO>();
						
						for (int i=0; i<dataList.size(); i++) {
							PfbxInvalidTrafficVO vo = new PfbxInvalidTrafficVO();
							
							Object[] objArray = (Object[]) dataList.get(i);
							
							vo.setInvId(objArray[0].toString());
							vo.setPfbId(objArray[1].toString());
							vo.setInvDate(dateFormate.format((objArray[2])));
							
							for(EnumPfbInvalidTrafficType enumPfbInvalidTrafficType:EnumPfbInvalidTrafficType.values()){
								if(StringUtils.equals(enumPfbInvalidTrafficType.getType(), objArray[3].toString())){
									vo.setInvType(enumPfbInvalidTrafficType.getChName());
								}
							}
							
							if(objArray[4] != null){
								vo.setInvNote(objArray[4].toString());
							}
							
							vo.setInvPrice(df.format(Double.parseDouble((objArray[5]).toString())));
							vo.setInvPfbBonus(df2.format(Double.parseDouble((objArray[6]).toString())));
							
							if(objArray[7] != null){
								vo.setCloseDate(dateFormate.format((objArray[7])));
								vo.setDeleteFlag("N");
							} else {
								vo.setDeleteFlag("Y");
							}
							
							vo.setUpdateDate(dateFormate.format((objArray[8])));
							
							resultData.add(vo);
						}

						return resultData;
					}
				});
    	return result;
	}
	
	public List<PfbxInvalidTraffic> findInvalidTrafficById(String invId){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfbxInvalidTraffic ");
		hql.append(" where invId = ?  ");
		
		return (List<PfbxInvalidTraffic>) super.getHibernateTemplate().find(hql.toString(), invId);
	}
}
