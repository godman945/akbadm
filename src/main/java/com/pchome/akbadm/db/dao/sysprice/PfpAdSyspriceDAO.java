package com.pchome.akbadm.db.dao.sysprice;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdSysprice;

public class PfpAdSyspriceDAO extends BaseDAO<PfpAdSysprice, String> implements IPfpAdSyspriceDAO {
    @Override
    @SuppressWarnings("unchecked")
    public PfpAdSysprice getAdSysprice(String adPoolSeq) throws Exception{

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpAdSysprice ");
        hql.append(" where adPoolSeq = '"+adPoolSeq+"' ");

        List<PfpAdSysprice> list = (List<PfpAdSysprice>) super.getHibernateTemplate().find(hql.toString());

        if(list != null && list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdSysprice> getAdSyspriceList() throws Exception{

        StringBuffer hql = new StringBuffer();
        hql.append(" from PfpAdSysprice ");

        return (List<PfpAdSysprice>) super.getHibernateTemplate().find(hql.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PfpAdSysprice> selectAdSyspriceByPoolSeq(String adPoolSeq) {
        String hql = "from PfpAdSysprice where adPoolSeq = ?";
        return (List<PfpAdSysprice>) this.getHibernateTemplate().find(hql, adPoolSeq);
    }

    @Override
    public float getNewAdSysprice(String day) throws Exception{

    	StringBuffer hql = new StringBuffer();
        hql.append(" SELECT count(distinct customer_info_id)  FROM pfp_ad_pvclk ");
        hql.append(" WHERE ad_pvclk_date = '" + day + "' ");
        hql.append(" and ad_pvclk_time = 13 ");
        hql.append(" and template_product_seq = 'c_x05_po_tpro_0024' ");

        String sql = hql.toString();
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
        BigInteger result = (BigInteger) query.uniqueResult();
        return (result != null && result.intValue() > 0) ? result.floatValue() : 0f;
//		Double result = (Double) query.uniqueResult();
//      Double result = Double.valueOf(String.valueOf((BigInteger) query.uniqueResult()));
//	    return result != null ? result.floatValue() : 0f;
    }
}