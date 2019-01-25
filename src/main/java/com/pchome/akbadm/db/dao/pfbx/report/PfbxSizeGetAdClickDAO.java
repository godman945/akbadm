package com.pchome.akbadm.db.dao.pfbx.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;

public class PfbxSizeGetAdClickDAO extends BaseDAO<PfpAdClickVO, String> implements IPfbxSizeGetAdClickDAO {
	List<PfpAdClickVO> resultData = new ArrayList<PfpAdClickVO>();
	
	public List<PfpAdClickVO> getAdClickMouseDownList(final Map<String, String> conditionMap) {
		List<PfpAdClickVO> result = (List<PfpAdClickVO>) getHibernateTemplate().execute(
				new HibernateCallback<List<PfpAdClickVO>>() {
					@SuppressWarnings("unchecked")
					public List<PfpAdClickVO> doInHibernate(Session session) throws HibernateException, SQLException {
				    	HashMap<String, Object> sqlParams = new HashMap<String, Object>();
				    	
				    	StringBuffer sql = new StringBuffer();
				    	sql.append(" select ");
				    	sql.append(" a.pfbx_customer_info_id, ");
				    	sql.append(" a.tpro_id, ");
				    	sql.append(" b.template_product_width, ");
				    	sql.append(" b.template_product_height, ");
				    	sql.append(" (case when ifnull(a.mouse_move_flag,'N') = '' then 'N' else ifnull(a.mouse_move_flag,'N') end), ");
				    	sql.append(" ifnull(a.mouse_down_x,0), ");
				    	sql.append(" ifnull(a.mouse_down_y,0) ");
				    	sql.append(" from pfp_ad_click a ");
				    	sql.append(" join adm_template_product b ");
				    	sql.append(" on a.tpro_id = b.template_product_seq ");
				    	sql.append(" where 1=1 ");
				    	
				    	if (StringUtils.isNotEmpty(conditionMap.get("pfbxCustomerInfoId"))) {
				    		sql.append(" and a.pfbx_customer_info_id = '"+conditionMap.get("pfbxCustomerInfoId")+"'");
						}
				    	
				    	if (StringUtils.isNotEmpty(conditionMap.get("adPvclkDate"))) {
				    		sql.append(" and a.record_date = '"+conditionMap.get("adPvclkDate")+"'");
						}
				    	
				    	if (StringUtils.isNotEmpty(conditionMap.get("width"))) {
				    		sql.append(" and b.template_product_width = '"+conditionMap.get("width")+"'");
						}
				    	
				    	if (StringUtils.isNotEmpty(conditionMap.get("height"))) {
				    		sql.append(" and b.template_product_height = '"+conditionMap.get("height")+"'");
						}
				    	
				    	String sqlString = sql.toString();
						log.info(">>> sql = " + sqlString);
						List<Object> dataList = new ArrayList<Object>();
						try {
						Query query = session.createSQLQuery(sqlString);
						dataList = query.list();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						log.info(">>> dataList.size() = " + dataList.size());
				    	
						resultData = new ArrayList<PfpAdClickVO>();
						for (int i=0; i<dataList.size(); i++) {
							PfpAdClickVO vo = new PfpAdClickVO();
							
							Object[] objArray = (Object[]) dataList.get(i);
							
							vo.setPfbxCustomerInfoId(objArray[0].toString());
							vo.setTproId(objArray[1].toString());
							vo.setWidth(objArray[2].toString());
							vo.setHeight(objArray[3].toString());
							vo.setMouseMoveFlag(objArray[4].toString());
							vo.setMouseDownX(objArray[5].toString());
							vo.setMouseDownY(objArray[6].toString());
							
							resultData.add(vo);
							log.info("resultData: "+resultData.size());
						}
						
				    	return resultData;
					}
				}
		);
		return result;
		
	}
}
