package com.pchome.akbadm.db.dao.catalog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;

public class PfpCatalogProdEcDAO extends BaseDAO<PfpCatalogProdEc, String> implements IPfpCatalogProdEcDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    pc.pfp_customer_info_id, ");
        sql.append("    pci.customer_info_title, ");
        sql.append("    count(pc.pfp_customer_info_id) ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog pc, ");
        sql.append("    pfp_catalog_prod_ec pcpe ");
        sql.append("WHERE pcpe.ec_status = :ec_status ");
        sql.append("AND pcpe.ec_check_status = :ec_check_status ");
        sql.append("AND pc.upload_status = :catalog_upload_status ");
        sql.append("AND pc.catalog_delete_status = :catalog_delete_status ");
        sql.append("AND pcpe.catalog_seq = pc.catalog_seq ");
        sql.append("AND pci.customer_info_id = pc.pfp_customer_info_id ");
        sql.append("GROUP BY pc.pfp_customer_info_id ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("ec_status", catalogProdEcStatus);
        query.setString("ec_check_status", catalogProdEcCheckStatus);
        query.setString("catalog_upload_status", catalogUploadStatus);
        query.setString("catalog_delete_status", catalogDeleteStatus);

        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectPfpCatalogByStatus(String pfpCustomerInfoId, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    pc.catalog_seq, ");
        sql.append("    pc.catalog_name, ");
        sql.append("    count(pc.catalog_seq) ");
        sql.append("FROM ");
        sql.append("    pfp_catalog pc, ");
        sql.append("    pfp_catalog_prod_ec pcpe ");
        sql.append("WHERE pcpe.ec_status = :ec_status ");
        sql.append("AND pc.pfp_customer_info_id = :pfp_customer_info_id ");
        sql.append("AND pcpe.ec_check_status = :ec_check_status ");
        sql.append("AND pc.upload_status = :catalog_upload_status ");
        sql.append("AND pc.catalog_delete_status = :catalog_delete_status ");
        sql.append("AND pcpe.catalog_seq = pc.catalog_seq ");
        sql.append("GROUP BY pcpe.catalog_seq ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("pfp_customer_info_id", pfpCustomerInfoId);
        query.setString("ec_status", catalogProdEcStatus);
        query.setString("ec_check_status", catalogProdEcCheckStatus);
        query.setString("catalog_upload_status", catalogUploadStatus);
        query.setString("catalog_delete_status", catalogDeleteStatus);

        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> selectPfpCatalogProdEc(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus, int pageNo, int pageSize) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    pc.pfp_customer_info_id, ");
        sql.append("    pci.customer_info_title, ");
        sql.append("    pc.catalog_seq, ");
        sql.append("    pc.catalog_name, ");
        sql.append("    pcpe.id, ");
        sql.append("    pcpe.catalog_prod_seq, ");
        sql.append("    pcpe.ec_name, ");
        sql.append("    pcpe.ec_img, ");
        sql.append("    pcpe.ec_url, ");
        sql.append("    pcpe.ec_price, ");
        sql.append("    pcpe.ec_discount_price, ");
        sql.append("    pcpe.ec_send_verify_time ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog pc, ");
        sql.append("    pfp_catalog_prod_ec pcpe ");
        sql.append("WHERE pcpe.ec_status = :ec_status ");
        sql.append("AND pcpe.ec_check_status = :ec_check_status ");
        if (StringUtils.isNotBlank(catalogSeq)) {
            sql.append("AND pcpe.catalog_seq = :catalog_seq ");
        }
        sql.append("AND pc.upload_status = :catalog_upload_status ");
        sql.append("AND pc.catalog_delete_status = :catalog_delete_status ");
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            sql.append("AND pc.pfp_customer_info_id = :pfp_customer_info_id ");
        }
        sql.append("AND pcpe.catalog_seq = pc.catalog_seq ");
        sql.append("AND pci.customer_info_id = pc.pfp_customer_info_id ");
        sql.append("ORDER BY pcpe.ec_send_verify_time DESC ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("ec_status", catalogProdEcStatus);
        query.setString("ec_check_status", catalogProdEcCheckStatus);
        if (StringUtils.isNotBlank(catalogSeq)) {
            query.setString("catalog_seq", catalogSeq);
        }
        query.setString("catalog_upload_status", catalogUploadStatus);
        query.setString("catalog_delete_status", catalogDeleteStatus);
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            query.setString("pfp_customer_info_id", pfpCustomerInfoId);
        }

        query.setFirstResult((pageNo-1) * pageSize);
        query.setMaxResults(pageSize);

        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public int selectCount(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("    count(pcpe.catalog_prod_seq) ");
        sql.append("FROM ");
        sql.append("    pfp_customer_info pci, ");
        sql.append("    pfp_catalog pc, ");
        sql.append("    pfp_catalog_prod_ec pcpe ");
        sql.append("WHERE pcpe.ec_status = :ec_status ");
        sql.append("AND pcpe.ec_check_status = :ec_check_status ");
        if (StringUtils.isNotBlank(catalogSeq)) {
            sql.append("AND pcpe.catalog_seq = :catalog_seq ");
        }
        sql.append("AND pc.upload_status = :catalog_upload_status ");
        sql.append("AND pc.catalog_delete_status = :catalog_delete_status ");
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            sql.append("AND pc.pfp_customer_info_id = :pfp_customer_info_id ");
        }
        sql.append("AND pcpe.catalog_seq = pc.catalog_seq ");
        sql.append("AND pci.customer_info_id = pc.pfp_customer_info_id ");

        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.setString("ec_status", catalogProdEcStatus);
        query.setString("ec_check_status", catalogProdEcCheckStatus);
        if (StringUtils.isNotBlank(catalogSeq)) {
            query.setString("catalog_seq", catalogSeq);
        }
        query.setString("catalog_upload_status", catalogUploadStatus);
        query.setString("catalog_delete_status", catalogDeleteStatus);
        if (StringUtils.isNotBlank(pfpCustomerInfoId)) {
            query.setString("pfp_customer_info_id", pfpCustomerInfoId);
        }

        return ((BigInteger) query.uniqueResult()).intValue();
    }
}
