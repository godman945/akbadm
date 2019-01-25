package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;

public class PfbxWebsiteCategoryDAO extends BaseDAO<PfbxWebsiteCategory, String> implements IPfbxWebsiteCategoryDAO {
	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxWebsiteCategory> findPfbxWebsiteCategoryAll() {
		return getHibernateTemplate().find("FROM PfbxWebsiteCategory");
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxWebsiteCategory> findByCode(String code) {
		String hql = "from PfbxWebsiteCategory where code = ?";
        return this.getHibernateTemplate().find(hql, code);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxWebsiteCategory> findChildByCode(String parentId) {
		String hql = "from PfbxWebsiteCategory where parentId = ?";
        return this.getHibernateTemplate().find(hql, parentId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxWebsiteCategory> findById(String id) {
		String hql = "from PfbxWebsiteCategory where id = " + id + " ";
        return this.getHibernateTemplate().find(hql);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer getNewId() {
		Integer newId = 1;
    	String hql = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_schema='akb' and table_name='pfbx_website_category'  ";
    	
    	Query q = this.getSession().createSQLQuery(hql.toString());
		
		List<Object> list = q.list();
		if(!list.isEmpty()){
			newId = Integer.parseInt(list.get(0).toString());
		}
		
    	return newId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PfbxWebsiteCategory> getFirstLevelPfbxWebsiteCategory() {
		String hql = "from PfbxWebsiteCategory where level = 1 ";
    	return super.getHibernateTemplate().find(hql);
	}
}