package com.pchome.akbadm.db.dao.pfbx.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.config.TestConfig;
import com.pchome.soft.util.DateValueUtil;


public class PfbxBonusSetSpecialDAO extends BaseDAO<PfbxBonusSetSpecial, Integer> implements IPfbxBonusSetSpecialDAO
{

	@SuppressWarnings("unchecked")
	public List<PfbxBonusSetSpecial> findPfbxBonusSets(String pfbId, Date todayDate)
	{

		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where pfbId = ? ");
		hql.append(" and (startDate <= ? ");
		hql.append(" and endDate >= ? )");
		hql.append(" order by id desc ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(pfbId);
		pos.add(todayDate);
		pos.add(todayDate);
		
		return (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialList(String pfbId){
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where pfbId = ? ");
		hql.append(" order by id ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(pfbId);
		
		return (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialListByFlag(String pfbId){
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where pfbId = ? ");
		hql.append(" and ifnull(deleteFlag,0) = 0 ");
		hql.append(" order by id ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(pfbId);
		
		return (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> findPfbxBonusSetSpecialCountMap(){
		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select pfb_id, count(*) ");
		sql.append(" from pfbx_bonus_set_special ");
		sql.append(" where ifnull(delete_flag,0) = 0 ");
		sql.append(" group by pfb_id ");
		
		Query q = session.createSQLQuery(sql.toString());
		
		Map<String, String> pfbxBonusSetSpecialMap = new HashMap<String, String>();
		List<Object> pfbxBonusSetSpecials = q.list();
		for(Object object:pfbxBonusSetSpecials) {
			Object[] ob = (Object[])object;
			pfbxBonusSetSpecialMap.put(ob[0].toString(), ob[1].toString());
		}
		
		return pfbxBonusSetSpecialMap;
	}
	
	@SuppressWarnings("unchecked")
	public PfbxBonusSetSpecial getPfbxBonusSetSpecial(Integer id){
		StringBuffer hql = new StringBuffer();

		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where id = ? ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(id);
		
		List<PfbxBonusSetSpecial> list = (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
		
		return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPfbxBonusSetSpecialNow(Date nowDate){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where deleteFlag = 0 ");
		hql.append(" and  ? between startDate and endDate ");
		hql.append(" order by  id desc ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(nowDate);
		
		List<PfbxBonusSetSpecial> list = (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
		
		Map<String, Object> pfbxBonusSetSpecialMap = new HashMap<String, Object>();
		for(PfbxBonusSetSpecial pfbxBonusSetSpecial:list) {
			pfbxBonusSetSpecialMap.put(pfbxBonusSetSpecial.getPfbId(), pfbxBonusSetSpecial);
		}
		
		return pfbxBonusSetSpecialMap;
	}
	
	@SuppressWarnings("unchecked")
	public List<PfbxBonusSetSpecial> findPfbxBonusSetSpecialbyDate(String pfbId, Date startDate, Date endDate){
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from PfbxBonusSetSpecial ");
		hql.append(" where deleteFlag = 0 ");
		hql.append(" and pfbId = ? ");
		hql.append(" and ( ( ? between startDate and endDate) ");
		hql.append(" 	or ( ? between startDate and endDate) ");
		hql.append(" 	or ( ? <= startDate and ? >= endDate )) ");
		
		List<Object> pos = new ArrayList<Object>();
		
		pos.add(pfbId);
		pos.add(startDate);
		pos.add(endDate);
		pos.add(startDate);
		pos.add(endDate);
		
		return (List<PfbxBonusSetSpecial>) super.getHibernateTemplate().find(hql.toString(),pos.toArray());
	}
	
	public static void main(String args[]){
		
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		PfbxBonusSetSpecialDAO pfbxBonusSetSpecialDAO = (PfbxBonusSetSpecialDAO) context.getBean("PfbxBonusSetSpecialDAO");
		
		List<PfbxBonusSetSpecial> list=pfbxBonusSetSpecialDAO.findPfbxBonusSets("PFBC20150519001",DateValueUtil.getInstance().stringToDate("2015-06-30"));
		
		System.out.println(list.size());
		
	}
	


}
