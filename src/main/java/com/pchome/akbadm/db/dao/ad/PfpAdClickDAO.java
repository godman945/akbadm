package com.pchome.akbadm.db.dao.ad;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public class PfpAdClickDAO extends BaseDAO<PfpAdClick, String> implements IPfpAdClickDAO {
   
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	
	@Override
    public List<PfpAdClick> findPfpAdClick(Date recordDate, int recordTime, int maliceType) {
        String hql = "from PfpAdClick where recordDate = ? and recordTime = ? and malice_type = ? order by record_minute";
        return this.getHibernateTemplate().find(hql, recordDate, recordTime, maliceType);
    }

    @Override
    public List<Object[]> findCostSum(Date recordDate, int recordTime) {
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append("    action.ad_action_seq, ");
        sql.append("    sum(click.ad_price) ");
        sql.append("from ");
        sql.append("    pfp_ad_click click, ");
        sql.append("    pfp_ad ad, ");
        sql.append("    pfp_ad_group group, ");
        sql.append("    pfp_ad_action action ");
        sql.append("where click.record_date = :recordDate ");
        sql.append("    and click.record_time <= :recordTime ");
        sql.append("    and click.malice_type = 0 ");
        sql.append("    and click.ad_id = ad.ad_seq ");
        sql.append("    and ad.ad_group_seq = group.ad_group_seq ");
        sql.append("    and group.ad_action_seq = action.ad_action_seq ");
        sql.append("group by action.ad_action_seq ");

        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("recordDate", recordDate);
        query.setParameter("recordTime", recordTime);

        return query.list();
    }
    
    @Override
    public List<PfpAdClick> findPfpAdClickByTraffic(List<Long> adClickIdList){
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append("from PfpAdClick ");
    	sql.append("where 1=1 ");
    	sql.append("and adClickId in (:adClickIdList) ");
    	
    	Query query = this.getSession().createQuery(sql.toString());
    	query.setParameterList("adClickIdList", adClickIdList);
    	
    	return query.list();
    }
    
    @Override
    public List<PfpAdClickVO> findMaliceClick(final Map<String, String> conditionMap){
    	
    	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
    	List<PfpAdClickVO> dataList = new ArrayList<PfpAdClickVO>();
    	StringBuffer sql = new StringBuffer();
    	
    	sql.append("select ");
    	sql.append("recordDate, ");
    	sql.append("recordTime, ");
    	sql.append("maliceType, ");
    	sql.append("pfbxCustomerInfoId, ");
    	sql.append("pfbxPositionId, ");
    	sql.append("memId, ");
    	sql.append("uuid, ");
    	sql.append("remoteIp, ");
    	sql.append("referer, ");
    	sql.append("userAgent, ");
    	sql.append("mouseMoveFlag, ");
    	sql.append("mouseAreaWidth, ");
    	sql.append("mouseAreaHeight, ");
    	sql.append("mouseDownX, ");
    	sql.append("mouseDownY, ");
    	sql.append("adPrice ");
    	sql.append("from PfpAdClick ");
    	sql.append("where 1=1 ");
    	sql.append("and maliceType > 0 ");
    	
    	if(StringUtils.isNotEmpty(conditionMap.get("startDate"))){
    		sql.append("and recordDate >= '" + conditionMap.get("startDate") + "' " );
    	}
    	
    	if(StringUtils.isNotEmpty(conditionMap.get("endDate"))){
    		sql.append("and recordDate <= '" + conditionMap.get("endDate") + "' " );
    	}
    	
    	if(StringUtils.isNotEmpty(conditionMap.get("pfbxCustomerInfoId"))){
    		sql.append("and pfbxCustomerInfoId = '" + conditionMap.get("pfbxCustomerInfoId") + "' " );
    	}
    	
    	if(StringUtils.isNotEmpty(conditionMap.get("pfbxPositionId"))){
    		sql.append("and pfbxPositionId = '" + conditionMap.get("pfbxPositionId") + "' " );
    	}
    	
    	if(StringUtils.isNotEmpty(conditionMap.get("maliceType"))){
    		sql.append("and maliceType = " + conditionMap.get("maliceType") + " " );
    	}
    	
    	sql.append("order by recordDate, recordTime, maliceType, pfbxCustomerInfoId, pfbxPositionId ");
    	
    	List<Object> ObjectList = new ArrayList<Object>();
		try {
		Query query = this.getSession().createQuery(sql.toString());
		ObjectList = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info(">>> ObjectList.size() = " + ObjectList.size());
		
		for (int i=0; i<ObjectList.size(); i++) {
			PfpAdClickVO vo = new PfpAdClickVO();
			
			Object[] objArray = (Object[]) ObjectList.get(i);
			
			vo.setRecordDate(dateFormate.format((objArray[0])));
			vo.setRecordTime(objArray[1].toString());
			vo.setMaliceType(objArray[2].toString());
			vo.setPfbxCustomerInfoId(objArray[3].toString());
			vo.setPfbxPositionId(objArray[4].toString());
			
			if(objArray[5] != null){
				vo.setMemId(objArray[5].toString());
			}
			
			if(objArray[6] != null){
				vo.setUuid(objArray[6].toString());
			}
			
			if(objArray[7] != null){
				vo.setRemoteIp(objArray[7].toString());
			}
			
			if(objArray[8] != null){
				vo.setReferer(objArray[8].toString());
			}
			
			if(objArray[9] != null){
				vo.setUserAgent(objArray[9].toString());
			}
			
			String mouseMoveFlag = "失敗";
			if(objArray[10] != null){
				if(StringUtils.equals("T", objArray[10].toString())){
					mouseMoveFlag = "正確";
				}
			}
			vo.setMouseMoveFlag(mouseMoveFlag);
			
			if(objArray[11] != null){
				vo.setMouseAreaWidth(objArray[11].toString());
			}
			
			if(objArray[12] != null){
				vo.setMouseAreaHeight(objArray[12].toString());
			}
			
			if(objArray[13] != null){
				vo.setMouseDownX(objArray[13].toString());
			}
			
			if(objArray[14] != null){
				vo.setMouseDownY(objArray[14].toString());
			}
			
			vo.setAdPrice(df.format(Double.parseDouble(objArray[15].toString())));
			
			dataList.add(vo);
		}
    	
    	return dataList;
    }
}
