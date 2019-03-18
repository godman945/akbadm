package com.pchome.akbadm.db.dao.catalog;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalog;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public class PfpCatalogDAO extends BaseDAO<PfpCatalog, String> implements IPfpCatalogDAO {

	/**
	 * 取得商品目錄，自動排程上傳清單
	 */
	public List<Map<String, Object>> getCatalogJobList() {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT                                     ");
		hql.append("   pc.catalog_seq,                          ");
		hql.append("   pc.catalog_name,                         ");
		hql.append("   pc.catalog_type,                         ");
		hql.append("   pc.pfp_customer_info_id,                 ");
		hql.append("   pc.catalog_upload_type,                  ");
		hql.append("   pc.catalog_upload_content,               ");
		// 取得最新一筆更新記錄是用更新還是取代
		hql.append("   (SELECT DISTINCT                         ");
		hql.append("           pcul.update_way                  ");
		hql.append("    FROM pfp_catalog_upload_log pcul        ");
		hql.append("    WHERE pcul.catalog_seq = pc.catalog_seq ");
		hql.append("    ORDER BY pcul.update_date DESC          ");
		hql.append("    Limit 1) AS update_way                  ");
		hql.append(" FROM pfp_catalog pc                        ");
		hql.append(" WHERE catalog_upload_type = 2              ");
		
        Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
        return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 更新目錄資料
	 * 狀態更新為已完成，重新算商品count量
	 */
	@Override
	public void updatePfpCatalogProdNumAndUploadStatus(PfpCatalogVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog SET ");
		hql.append(" upload_status = :upload_status, ");
		hql.append(" catalog_prod_num = (SELECT COUNT(catalog_prod_seq) FROM pfp_catalog_prod_ec WHERE catalog_seq = :catalog_seq), ");
		hql.append(" update_date = CURRENT_TIMESTAMP() ");
		if (StringUtils.isNotBlank(vo.getCatalogUploadType())) { // 上傳方式
			hql.append(" ,catalog_upload_type = :catalog_upload_type ");
		}
		
		hql.append(" WHERE catalog_seq = :catalog_seq ");
		if (StringUtils.isNotBlank(vo.getPfpCustomerInfoId())) {
			hql.append(" AND pfp_customer_info_id = :pfp_customer_info_id ");
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_seq", vo.getCatalogSeq());
		query.setString("upload_status", vo.getUploadStatus());
		
		if (StringUtils.isNotBlank(vo.getCatalogUploadType())) { // 上傳方式
			query.setString("catalog_upload_type", vo.getCatalogUploadType());
		}
		
		if(StringUtils.isNotBlank(vo.getPfpCustomerInfoId())){
			query.setString("pfp_customer_info_id", vo.getPfpCustomerInfoId());
		}

		query.executeUpdate();
	}

	/**
	 * 更新目錄資料
	 * 依傳入狀態更新
	 * @param vo
	 */
	@Override
	public void updatePfpCatalogUploadStatus(PfpCatalogVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog SET ");
		hql.append(" upload_status = :upload_status, ");
		hql.append(" update_date = CURRENT_TIMESTAMP() ");
		hql.append(" WHERE catalog_seq = :catalog_seq ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_seq", vo.getCatalogSeq());
		query.setString("upload_status", vo.getUploadStatus());

		query.executeUpdate();
	}
	
}