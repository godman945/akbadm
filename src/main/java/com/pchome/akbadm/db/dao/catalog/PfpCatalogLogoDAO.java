package com.pchome.akbadm.db.dao.catalog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogLogo;

public class PfpCatalogLogoDAO extends BaseDAO<PfpCatalogLogo, String> implements IPfpCatalogLogoDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String status) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    pcl.pfp_customer_info_id, ");
        sql.append("    pci.customer_info_title, ");
        sql.append("    count(pcl.pfp_customer_info_id) ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog_logo pcl ");
        sql.append("WHERE pcl.status = :status ");
        sql.append("AND pci.customer_info_id = pcl.pfp_customer_info_id ");
        sql.append("GROUP BY pcl.pfp_customer_info_id ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("status", status);

        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectCatalogLogo(String pfpCustomerInfoId, String status, int pageNo, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    pcl.catalog_logo_seq, ");
        sql.append("    pcl.catalog_logo_url, ");
        sql.append("    pcl.catalog_logo_type, ");
        sql.append("    pcl.pfp_customer_info_id, ");
        sql.append("    pci.customer_info_title, ");
        sql.append("    pcl.logo_send_verify_time ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog_logo pcl ");
        sql.append("WHERE pcl.status = :status ");
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            sql.append("AND pcl.pfp_customer_info_id = :pfp_customer_info_id ");
        }
        sql.append("AND pci.customer_info_id = pcl.pfp_customer_info_id ");
        sql.append("ORDER BY pcl.pfp_customer_info_id, pcl.catalog_logo_seq DESC ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("status", status);
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            query.setString("pfp_customer_info_id", pfpCustomerInfoId);
        }

        query.setFirstResult((pageNo-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public int selectCount(String pfpCustomerInfoId, String status) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    count(pcl.catalog_logo_seq) ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog_logo pcl ");
        sql.append("WHERE pcl.status = :status ");
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            sql.append("AND pcl.pfp_customer_info_id = :pfp_customer_info_id ");
        }
        sql.append("AND pci.customer_info_id = pcl.pfp_customer_info_id ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("status", status);
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            query.setString("pfp_customer_info_id", pfpCustomerInfoId);
        }

        return ((BigInteger) query.uniqueResult()).intValue();
    }
}
