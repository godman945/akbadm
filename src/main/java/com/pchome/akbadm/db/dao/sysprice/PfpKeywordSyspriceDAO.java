package com.pchome.akbadm.db.dao.sysprice;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpKeywordSysprice;

public class PfpKeywordSyspriceDAO extends BaseDAO<PfpKeywordSysprice, String> implements IPfpKeywordSyspriceDAO {
    @SuppressWarnings("unchecked")
    public PfpKeywordSysprice getKeywordSysprice(String keyword) throws Exception{

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpKeywordSysprice ");
        hql.append(" where keyword = '"+keyword+"' ");

        List<PfpKeywordSysprice> list = super.getHibernateTemplate().find(hql.toString());

        if(list != null && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<PfpKeywordSysprice> getKeywordSyspriceList() throws Exception{

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpKeywordSysprice ");

        return super.getHibernateTemplate().find(hql.toString());
    }

    @SuppressWarnings("unchecked")
    public List<PfpKeywordSysprice> selectKeywordSyspriceByKeyword(String keyword, int firstResult, int maxResults) {
        String hql = "from PfpKeywordSysprice where keyword = :keyword";
        return getSession().createQuery(hql)
            .setString("keyword", keyword)
            .setFirstResult(firstResult)
            .setMaxResults(maxResults)
            .list();
    }
}