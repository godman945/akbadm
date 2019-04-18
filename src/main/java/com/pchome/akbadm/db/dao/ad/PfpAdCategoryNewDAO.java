package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategoryNew;

public class PfpAdCategoryNewDAO extends  BaseDAO<PfpAdCategoryNew,String> implements IPfpAdCategoryNewDAO {
    // 查詢所有廣告
    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryNew> findPfpAdCategoryNewAll() {
        return (List<PfpAdCategoryNew>) getHibernateTemplate().find("FROM PfpAdCategoryNew");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryNew> findByCode(String code) {
        String hql = "from PfpAdCategoryNew where code = ?";
        return (List<PfpAdCategoryNew>) this.getHibernateTemplate().find(hql, code);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryNew> findChildByCode(String parentId) {
        String hql = "from PfpAdCategoryNew where parentId = ?";
        return (List<PfpAdCategoryNew>) this.getHibernateTemplate().find(hql, parentId);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryNew> findById(String id) {
        String hql = "from PfpAdCategoryNew where id = " + id + " ";
        return (List<PfpAdCategoryNew>) this.getHibernateTemplate().find(hql);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Integer getNewId(){
    	Integer newId = 1;
    	String hql = "SELECT AUTO_INCREMENT FROM information_schema.tables WHERE table_schema='akb' and table_name='pfp_ad_category_new'  ";
    	
    	Query q = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		
		List<Object> list = q.list();
		if(!list.isEmpty()){
			newId = Integer.parseInt(list.get(0).toString());
		}
		
    	return newId;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void saveOrUpdate(PfpAdCategoryNew pfpAdCategoryNew) {
    	this.getHibernateTemplate().saveOrUpdate(pfpAdCategoryNew);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryNew> getFirstLevelPfpAdCategoryNew() {
    	String hql = "from PfpAdCategoryNew where level = 1 ";
    	return (List<PfpAdCategoryNew>) super.getHibernateTemplate().find(hql);
    }
}
