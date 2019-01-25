package com.pchome.akbadm.db.dao.admBrandCorrespond;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmBrandCorrespond;
import com.pchome.akbadm.db.vo.ad.AdmBrandCorrespondVO;
import com.pchome.soft.depot.utils.CommonUtils;

public class AdmBrandCorrespondDAO extends BaseDAO<AdmBrandCorrespond, Integer> implements IAdmBrandCorrespondDAO{

	/**
	 * 取得 品牌對應關鍵字 資料
	 * 功能查詢與api共用
	 * @param admBrandCorrespondVO
	 * @return
	 */
	@Override
	public List<Object> getBrandCorrespondList(AdmBrandCorrespondVO admBrandCorrespondVO) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT                    ");
		hql.append("   id                      ");
		hql.append("   ,brand_eng              ");
		hql.append("   ,brand_ch               ");
		hql.append("   ,update_date            ");
		hql.append("   ,create_date            ");
		hql.append(" FROM adm_brand_correspond abc");
		hql.append(" WHERE 1 = 1 ");
		
		if (admBrandCorrespondVO.getId() != 0) { // table流水號從1開始
			hql.append(" AND abc.id = :id ");
		}
		
		if (StringUtils.isNotBlank(admBrandCorrespondVO.getQueryString())
				&& !"*".equals(admBrandCorrespondVO.getQueryString())) {
			hql.append(" AND (abc.brand_eng = :queryString OR abc.brand_ch = :queryString) ");
		}

		// 依據勾選的checkbox來撈相對應資料
		if (StringUtils.isNotBlank(admBrandCorrespondVO.getCheckboxButton())) {
			if ("all".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())) {
				hql.append(" AND abc.brand_eng != 'NA' AND abc.brand_ch != 'NA' ");
			} else if ("eng".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())) {
				hql.append(" AND abc.brand_eng != 'NA' ");
			} else if ("ch".equalsIgnoreCase(admBrandCorrespondVO.getCheckboxButton())) {
				hql.append(" AND abc.brand_ch != 'NA' ");
			}
		}
		
		hql.append(" ORDER BY id DESC ");
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);

		Session session = getSession();
		Query query = session.createSQLQuery(hqlStr);

		// 塞入where參數值
		if (admBrandCorrespondVO.getId() != 0) {
			query.setInteger("id", admBrandCorrespondVO.getId());
		}
		
		if (StringUtils.isNotBlank(admBrandCorrespondVO.getQueryString())
				&& !"*".equals(admBrandCorrespondVO.getQueryString())) {
			query.setString("queryString", admBrandCorrespondVO.getQueryString());
		}
		
		// 記錄總筆數
		admBrandCorrespondVO.setTotalCount(query.list().size());
		
		// 計算總頁數
		admBrandCorrespondVO.setTotalPage(CommonUtils.getTotalPage(admBrandCorrespondVO.getTotalCount(), admBrandCorrespondVO.getPageSize()));
		
		// 下載和api時flag會塞值，需資料全撈，沒有值時表示為一般功能查詢才做處理分頁
		if(StringUtils.isBlank(admBrandCorrespondVO.getFlag())){
			// 處理分頁
			query.setFirstResult((admBrandCorrespondVO.getPage() - 1) * admBrandCorrespondVO.getPageSize());
			query.setMaxResults(admBrandCorrespondVO.getPageSize());
		}
		
		return query.list();
	}

	/**
	 * 檢查新增的資料是否重複
	 * @param admBrandCorrespondVO
	 * @return
	 */
	@Override
	public List<AdmBrandCorrespondVO> checkBrandCorrespondTableData(AdmBrandCorrespond admBrandCorrespond) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT ");
		hql.append("   brand_eng ");
		hql.append("   ,brand_ch ");
		hql.append(" FROM adm_brand_correspond abc ");
		hql.append(" WHERE abc.brand_eng = :brandEng ");
		hql.append(" AND abc.brand_ch = :brandCh ");
		
		// 更新會傳id進來，不檢查自己是否相同
		if (admBrandCorrespond.getId() != null) {
			hql.append(" AND abc.id != :id ");
		}
		
		String hqlStr = hql.toString();
		log.info(">>> hqlStr = " + hqlStr);

		Session session = getSession();
		Query query = session.createSQLQuery(hqlStr);

		// 塞入where參數值
		if(admBrandCorrespond.getBrandEng().isEmpty()){
			query.setString("brandEng", "NA");
		}else{
			query.setString("brandEng", admBrandCorrespond.getBrandEng());
		}

		if(admBrandCorrespond.getBrandCh().isEmpty()){
			query.setString("brandCh", "NA");
		}else{
			query.setString("brandCh", admBrandCorrespond.getBrandCh());
		}
		
		if (admBrandCorrespond.getId() != null) {
			query.setInteger("id", admBrandCorrespond.getId());
		}
		
		return query.list();
	}

	/**
	 * 新增品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void insertBrandCorrespond(AdmBrandCorrespond admBrandCorrespond) {
		super.save(admBrandCorrespond);
	}

	/**
	 * 更新品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void updateBrandCorrespond(AdmBrandCorrespond admBrandCorrespond) {
		String hql =  "UPDATE AdmBrandCorrespond ";
			   hql += "SET brand_eng = :brand_eng, brand_ch = :brand_ch, ";
			   hql += "update_date = CURRENT_TIMESTAMP() ";
			   hql += "WHERE id = :id ";

		Session session = getSession();
		Query query = session.createQuery(hql);
		
		query.setString("brand_eng", admBrandCorrespond.getBrandEng());
		query.setString("brand_ch", admBrandCorrespond.getBrandCh());
		query.setInteger("id", admBrandCorrespond.getId());
		query.executeUpdate();
		session.flush();
	}

	/**
	 * 刪除品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	@Override
	public void deleteBrandCorrespondData(AdmBrandCorrespond admBrandCorrespond) {
		String hql = "DELETE FROM AdmBrandCorrespond WHERE id = :id";
		Session session = getSession();
		session.createQuery(hql).setInteger("id", admBrandCorrespond.getId()).executeUpdate();
		session.flush();
	}

}
