package com.pchome.akbadm.db.dao.catalog.uploadList;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate4.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEcError;
import com.pchome.akbadm.db.pojo.PfpCatalogUploadErrLog;
import com.pchome.akbadm.db.pojo.PfpCatalogUploadLog;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public class PfpCatalogUploadListDAO extends BaseDAO<String, String> implements IPfpCatalogUploadListDAO {

	/**
	 * 從商品目錄更新紀錄 給匯入資料判斷用
	 * @param catalogSeq
	 * @param catalogUploadContent
	 * @param updateDatetime
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPfpCatalogUploadLogForImportData(String catalogSeq, String catalogUploadContent,
			String updateDatetime) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ");
		hql.append(" catalog_upload_log_seq, update_way, update_content, update_datetime");
		hql.append(" FROM pfp_catalog_upload_log ");
		hql.append(" WHERE catalog_seq = :catalog_seq ");
		
		if (StringUtils.isNotBlank(catalogUploadContent)) {
			hql.append(" AND update_content = :update_content ");
		}
		
		hql.append(" AND REPLACE(REPLACE(REPLACE(update_datetime, '-', ''), ':', ''), ' ', '') = :update_datetime ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_seq", catalogSeq);
		
		if (StringUtils.isNotBlank(catalogUploadContent)) {
			query.setString("update_content", catalogUploadContent);
		}
		
		query.setString("update_datetime", updateDatetime);
		
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	/**
	 * 取得一般購物類商品清單 資料
	 */
	@Override
	public List<Map<String, Object>> getPfpCatalogProdEc(String catalogSeq, String catalogProdSeq) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ");
		hql.append(" id, catalog_prod_seq, catalog_seq, ec_name, ec_title, ");
		hql.append(" ec_img, ec_img_region, ec_img_md5, ec_url, ec_price, ");
		hql.append(" ec_discount_price, ec_stock_status, ec_use_status, ec_category, ec_status, ");
		hql.append(" ec_check_status, update_date, create_date ");
		hql.append(" FROM pfp_catalog_prod_ec ");
		hql.append(" WHERE 1 = 1 ");
		
		if (StringUtils.isNotBlank(catalogSeq)) {
			hql.append(" AND catalog_seq = :catalog_seq ");
		}
		
		if (StringUtils.isNotBlank(catalogProdSeq)) {
			hql.append(" AND catalog_prod_seq = :catalog_prod_seq ");
		}
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		
		if (StringUtils.isNotBlank(catalogSeq)) {
			query.setString("catalog_seq", catalogSeq);
		}
		
		if (StringUtils.isNotBlank(catalogProdSeq)) {
			query.setString("catalog_prod_seq", catalogProdSeq);
		}
		
		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
	/**
	 * 取得一般購物類商品清單 資料
	 */
//	@Override
//	public List<Map<String, Object>> getPfpCatalogProdEc(String catalogSeq, PfpCatalogProdEc pfpCatalogProdEc) {
//		StringBuffer hql = new StringBuffer();
//		hql.append(" SELECT ");
//		hql.append(" id, catalog_prod_seq, catalog_seq, ec_name, ec_title, ");
//		hql.append(" ec_img, ec_img_region, ec_img_md5, ec_url, ec_price, ");
//		hql.append(" ec_discount_price, ec_stock_status, ec_use_status, ec_category, ec_status, ");
//		hql.append(" ec_check_status, update_date, create_date ");
//		hql.append(" FROM pfp_catalog_prod_ec ");
//		hql.append(" WHERE 1 = 1 ");
//		
//		if (StringUtils.isNotBlank(catalogSeq)) {
//			hql.append(" AND catalog_seq = :catalog_seq ");
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcName())) {
//			hql.append(" AND ec_name = :ec_name ");
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcImg())) {
//			hql.append(" AND ec_img = :ec_img ");
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcUrl())) {
//			hql.append(" AND ec_url = :ec_url ");
//		}
//		
//		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
//		
//		if (StringUtils.isNotBlank(catalogSeq)) {
//			query.setString("catalog_seq", catalogSeq);
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcName())) {
//			query.setString("ec_name", pfpCatalogProdEc.getEcName());
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcImg())) {
//			query.setString("ec_img", pfpCatalogProdEc.getEcImg());
//		}
//		
//		if (StringUtils.isNotBlank(pfpCatalogProdEc.getEcUrl())) {
//			query.setString("ec_url", pfpCatalogProdEc.getEcUrl());
//		}
//		
//		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
//	}
	
	/**
	 * 更新一般購物類資料
	 * @param pfpCatalogProdEc
	 * @return 更新筆數
	 */
	public int updatePfpCatalogProdEc(PfpCatalogProdEc pfpCatalogProdEc) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog_prod_ec ");
		hql.append(" SET ec_name = :ec_name, ec_title = :ec_title, ec_img = :ec_img, ec_img_region = :ec_img_region, ec_img_md5 = :ec_img_md5, ");
		hql.append(" ec_url = :ec_url, ec_price = :ec_price, ec_discount_price = :ec_discount_price, ec_stock_status = :ec_stock_status, ec_use_status = :ec_use_status, ");
		hql.append(" ec_category = :ec_category, ec_status = :ec_status, ec_check_status = :ec_check_status, ");
		if ("0".equals(pfpCatalogProdEc.getEcCheckStatus())) { // 審核中則更新 商品送審時間
			hql.append(" ec_send_verify_time = CURRENT_TIMESTAMP(), ");
		}
		hql.append(" update_date = CURRENT_TIMESTAMP() ");
		hql.append(" WHERE catalog_seq = :catalog_seq AND catalog_prod_seq = :catalog_prod_seq");

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hql.toString());
		query.setString("ec_name", pfpCatalogProdEc.getEcName());
		query.setString("ec_title", pfpCatalogProdEc.getEcTitle());
		query.setString("ec_img", pfpCatalogProdEc.getEcImg());
		query.setString("ec_img_region", pfpCatalogProdEc.getEcImgRegion());
		query.setString("ec_img_md5", pfpCatalogProdEc.getEcImgMd5());
		query.setString("ec_url", pfpCatalogProdEc.getEcUrl());
		query.setInteger("ec_price", pfpCatalogProdEc.getEcPrice());
		query.setInteger("ec_discount_price", pfpCatalogProdEc.getEcDiscountPrice());
		query.setString("ec_stock_status", pfpCatalogProdEc.getEcStockStatus());
		query.setString("ec_use_status", pfpCatalogProdEc.getEcUseStatus());
		query.setString("ec_category", pfpCatalogProdEc.getEcCategory());
		query.setString("ec_status", pfpCatalogProdEc.getEcStatus());
		query.setString("ec_check_status", pfpCatalogProdEc.getEcCheckStatus());
		
		// where條件
		query.setString("catalog_seq", pfpCatalogProdEc.getPfpCatalog().getCatalogSeq());
		query.setString("catalog_prod_seq", pfpCatalogProdEc.getCatalogProdSeq());
		
		return query.executeUpdate();
	}
	
	/**
	 * 新增一般購物類資料
	 * @param pfpCatalogProdEc
	 */
	public void savePfpCatalogProdEc(PfpCatalogProdEc pfpCatalogProdEc) {
		this.getHibernateTemplate().getSessionFactory().getCurrentSession().save(pfpCatalogProdEc);
	}
	
	/**
	 * 刪除不在catalogProdEcSeqList列表內的資料
	 * @param catalogSeq 商品目錄ID
	 * @param catalogProdEcSeqList 不被刪除的名單
	 */
	public void deleteNotInPfpCatalogProdEc(String catalogSeq, List<String> catalogProdSeqList) {
		String hql = "DELETE FROM pfp_catalog_prod_ec ";
			   hql += "WHERE catalog_seq = :catalog_seq ";
			   if (catalogProdSeqList.size() != 0) {
				   hql += "AND catalog_prod_seq NOT IN(:catalog_prod_seq_list)";
			   }

		Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(hql);
		query.setString("catalog_seq", catalogSeq);
		if (catalogProdSeqList.size() != 0) {
			query.setParameterList("catalog_prod_seq_list", catalogProdSeqList);
		}
		query.executeUpdate();
	}
	
	/**
	 * 新增商品目錄更新紀錄
	 * @param pfpCatalogUploadLog
	 */
	public void savePfpCatalogUploadLog(PfpCatalogUploadLog pfpCatalogUploadLog) {
		this.getHibernateTemplate().getSessionFactory().getCurrentSession().save(pfpCatalogUploadLog);
	}
	
	/**
	 * 更新商品目錄更新紀錄
	 * @param pfpCatalogUploadLog
	 */
	@Override
	public void updatePfpCatalogProdEc(PfpCatalogUploadLog pfpCatalogUploadLog) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog_upload_log SET ");
		hql.append(" error_num = :error_num, ");
		hql.append(" success_num = :success_num, ");
		hql.append(" update_date = CURRENT_TIMESTAMP() ");
		hql.append(" WHERE catalog_upload_log_seq = :catalog_upload_log_seq ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_upload_log_seq", pfpCatalogUploadLog.getCatalogUploadLogSeq());
		query.setInteger("error_num", pfpCatalogUploadLog.getErrorNum());
		query.setInteger("success_num", pfpCatalogUploadLog.getSuccessNum());

		query.executeUpdate();
	}
	
	/**
	 * 刪除 商品目錄更新錯誤紀錄
	 * @param vo
	 */
	@Override
	public void deletePfpCatalogUploadErrLog(PfpCatalogVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" DELETE FROM pfp_catalog_upload_err_log ");
		hql.append(" WHERE catalog_upload_log_seq IN ");
		hql.append(" (SELECT catalog_upload_log_seq FROM pfp_catalog_upload_log WHERE catalog_seq = :catalog_seq) ");

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_seq", vo.getCatalogSeq());

		query.executeUpdate();
	}
	
	/**
	 * 刪除 一般購物類商品上傳錯誤清單
	 * @param vo
	 */
	@Override
	public void deletePfpCatalogProdEcError(PfpCatalogVO vo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" DELETE FROM pfp_catalog_prod_ec_error ");
		hql.append(" WHERE catalog_upload_log_seq IN ");
		hql.append(" (SELECT catalog_upload_log_seq FROM pfp_catalog_upload_log WHERE catalog_seq = :catalog_seq) ");

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("catalog_seq", vo.getCatalogSeq());

		query.executeUpdate();
	}
	
	/**
	 * 新增商品目錄更新錯誤紀錄
	 * @param pfpCatalogUploadErrLog
	 */
	public void savePfpCatalogUploadErrLog(PfpCatalogUploadErrLog pfpCatalogUploadErrLog) {
		this.getHibernateTemplate().getSessionFactory().getCurrentSession().save(pfpCatalogUploadErrLog);
	}
	
	/**
	 * 新增商品目錄更新錯誤紀錄
	 * @param pfpCatalogUploadErrLog
	 */
	@Override
	public void savePfpCatalogProdEcError(PfpCatalogProdEcError pfpCatalogProdEcError) {
		this.getHibernateTemplate().getSessionFactory().getCurrentSession().save(pfpCatalogProdEcError);
	}

	@Override
	public void updatePfpCatalogUploadLogForAutoJobDownloadFail(String catalogSeq, String updateContent,
			String updateDatetime) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog_upload_log SET ");
		hql.append(" update_content = :update_content ");
		hql.append(" WHERE catalog_seq = :catalog_seq ");
		hql.append(" AND REPLACE(REPLACE(REPLACE(update_datetime, '-', ''), ':', ''), ' ', '') = :update_datetime ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("update_content", updateContent);
		query.setString("catalog_seq", catalogSeq);
		query.setString("update_datetime", updateDatetime);

		query.executeUpdate();
	}

	/**
	 * 取得自動排程上傳要建立url.txt的清單
	 * 條件:
	 * 商品目錄狀態 0：未刪除
	 * 上傳方式  autoJobAndStoreUrl(2, 3) 2:自動排程上傳  3:賣場網址上傳
	 * 帳戶餘額 > 0 (有錢)
	 * 廣告狀態:4 開啟
	 * 廣告樣式上稿流程:'PROD' 商品
	 * @return
	 */
	@Override
	public List<Object> getCreateUrlTxtList(final String type) {
		
		List<Object> result = getHibernateTemplate().execute(
			new HibernateCallback<List<Object>>() {
				@Override
                public List<Object> doInHibernate(Session session) throws HibernateException {

					StringBuffer hql = new StringBuffer();
					hql.append(" SELECT                                                                                                           ");
					hql.append("   T1.catalog_upload_log_seq,                                                                                     ");
					hql.append("   T1.catalog_seq,                                                                                                ");
					hql.append("   T1.update_datetime,                                                                                            ");
					hql.append("   pc.pfp_customer_info_id,                                                                                       ");
					hql.append("   pc.catalog_upload_content,                                                                                     ");
					hql.append("   pc.catalog_upload_type                                                                                         ");
					hql.append(" FROM(                                                                                                            ");
					hql.append(" 	SELECT                                                                                                        ");
					hql.append(" 	  T1.catalog_upload_log_seq,                                                                                  ");
					hql.append(" 	  T1.catalog_seq,                                                                                             ");
					hql.append(" 	  CAST(T1.update_datetime AS CHAR)AS update_datetime                                                          ");
					hql.append(" 	FROM (                                                                                                        ");
					hql.append(" 		SELECT                                                                                                    ");
					hql.append(" 		  pcul.catalog_upload_log_seq,                                                                            ");
					hql.append(" 		  pcul.catalog_seq,                                                                                       ");
					hql.append(" 		  pcul.update_content,                                                                                    ");
					hql.append(" 		  REPLACE(REPLACE(REPLACE(pcul.update_datetime, '-', ''), ':', ''), ' ', '')AS update_datetime            ");
					hql.append(" 		FROM pfp_catalog_upload_log pcul                                                                          ");
					hql.append(" 		WHERE pcul.catalog_seq in (                                                                               ");
					hql.append(" 			SELECT                                                                                                ");
					hql.append(" 			  catalog_seq                                                                                         ");
					hql.append(" 			FROM pfp_catalog pc LEFT JOIN pfp_customer_info pci ON pc.pfp_customer_info_id = pci.customer_info_id ");
					hql.append(" 			LEFT JOIN pfp_ad_action paa ON pc.pfp_customer_info_id = paa.customer_info_id                         ");
					hql.append(" 			WHERE                                                                                                 ");
					hql.append(" 			pc.catalog_delete_status = '0'                                                                        ");
					if ("autoJobAndStoreUrl".equalsIgnoreCase(type)) {
						hql.append(" 			AND pc.catalog_upload_type IN ('2', '3')                                                          ");
					} else {
						hql.append(" 			AND pc.catalog_upload_type = '" + type + "'                                                       ");
					}
					hql.append(" 			AND pci.remain > 0                                                                                    ");
					hql.append(" 			AND paa.ad_action_status = 4                                                                          ");
					hql.append(" 			AND paa.ad_operating_rule = 'PROD'                                                                    ");
					hql.append(" 		)                                                                                                         ");
					hql.append(" 	)T1,(SELECT MAX(pcul2.catalog_upload_log_seq)AS catalog_upload_log_seq, pcul2.catalog_seq                     ");
					hql.append(" 		FROM pfp_catalog_upload_log pcul2                                                                         ");
					hql.append(" 		GROUP BY pcul2.catalog_seq)T2                                                                             ");
					hql.append(" 	WHERE T1.catalog_upload_log_seq = T2.catalog_upload_log_seq                                                   ");
					hql.append(" )T1 LEFT JOIN pfp_catalog pc ON T1.catalog_seq = pc.catalog_seq                                                  ");
					
					return session.createSQLQuery(hql.toString()).list();
				}
			}
		);
		
		return result;
	}

	@Override
	public void updatePfpCatalogUploadLogForCsvFileFail(String catalogUploadLogSeq, String errMsgUpdateContent) {
		StringBuffer hql = new StringBuffer();
		hql.append(" UPDATE pfp_catalog_upload_log SET ");
		hql.append(" update_content = :update_content ");
		hql.append(" WHERE catalog_upload_log_seq  = :catalog_upload_log_seq ");
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setString("update_content", errMsgUpdateContent);
		query.setString("catalog_upload_log_seq", catalogUploadLogSeq);

		query.executeUpdate();
	}

}