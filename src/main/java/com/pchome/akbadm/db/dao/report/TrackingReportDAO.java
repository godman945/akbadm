package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.vo.report.PfpCodeTrackingVO;
import com.pchome.soft.depot.utils.CommonUtils;

public class TrackingReportDAO extends BaseDAO<PfpCodeTracking, String> implements ITrackingReportDAO {

	@Override
	public List<Map<String, Object>> getTrackingReportDetail(PfpCodeTrackingVO pfpCodeTrackingVO) {
		StringBuffer hql = new StringBuffer();
		hql.append(" SELECT                                                                                           ");
		hql.append("   pfdci.customer_info_id,                                                                        ");
		hql.append("   pfdci.company_name,                                                                            ");
		hql.append("   pct.pfp_customer_info_id,                                                                      ");
		hql.append("   pci.customer_info_title,                                                                       ");
		hql.append("   pct.tracking_seq,                                                                              ");
		hql.append("   pct.tracking_name,                                                                             ");
		hql.append("   pct.tracking_status,                                                                           ");
		hql.append("   pct.code_type,                                                                                 ");
		hql.append("   pct.tracking_range_date                                                                        ");
		hql.append(" FROM pfp_code_tracking pct                                                                       ");
		hql.append(" LEFT JOIN pfp_customer_info pci ON pct.pfp_customer_info_id = pci.customer_info_id               ");
		hql.append(" LEFT JOIN pfd_user_ad_account_ref puaar ON pct.pfp_customer_info_id = puaar.pfp_customer_info_id ");
		hql.append(" LEFT JOIN pfd_customer_info pfdci ON puaar.pfd_customer_info_id = pfdci.customer_info_id         ");
		hql.append(" WHERE 1 = 1                                                                                      ");
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfdCustomerInfoId())) {
			hql.append(" AND pfdci.customer_info_id = :pfd_customer_info_id ");
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfpCustomerInfoId())) {
			hql.append(" AND pct.pfp_customer_info_id = :pfp_customer_info_id ");
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfpCustomerInfoName())) {
			hql.append(" AND pci.customer_info_title = :pfp_customer_info_name ");
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getStatus())) {
			hql.append(" AND pct.tracking_status = :tracking_status ");
		}

		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		
        if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfdCustomerInfoId())) {
        	query.setString("pfd_customer_info_id", pfpCodeTrackingVO.getPfdCustomerInfoId());
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfpCustomerInfoId())) {
			query.setString("pfp_customer_info_id", pfpCodeTrackingVO.getPfpCustomerInfoId());
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getPfpCustomerInfoName())) {
			query.setString("pfp_customer_info_name", pfpCodeTrackingVO.getPfpCustomerInfoName());
		}
		
		if (StringUtils.isNotBlank(pfpCodeTrackingVO.getStatus())) {
			query.setString("tracking_status", pfpCodeTrackingVO.getStatus());
		}
        
		// 記錄總筆數
        pfpCodeTrackingVO.setTotalCount(query.list().size());
		// 計算總頁數
        pfpCodeTrackingVO.setPageCount(CommonUtils.getTotalPage(pfpCodeTrackingVO.getTotalCount(), pfpCodeTrackingVO.getPageSize()));
		
		// 取得分頁
		query.setFirstResult((pfpCodeTrackingVO.getPageNo() - 1) * pfpCodeTrackingVO.getPageSize());
		query.setMaxResults(pfpCodeTrackingVO.getPageSize());

		return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}
	
}