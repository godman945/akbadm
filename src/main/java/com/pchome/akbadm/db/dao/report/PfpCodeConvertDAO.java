package com.pchome.akbadm.db.dao.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpCodeConvert;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;
import com.pchome.enumerate.convert.EnumConvertStatusType;

public class PfpCodeConvertDAO extends BaseDAO<PfpCodeConvert, String> implements IPfpCodeConvertDAO {
	
	@SuppressWarnings("unchecked")
	public String getConvertTrackingCount(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		StringBuffer hql = new StringBuffer();
		hql.append(" select count(*) ");
		hql.append(" from pfp_code_convert pfpconvert ");
		hql.append(" left join pfp_customer_info pci ");
		hql.append(" on pfpconvert.pfp_customer_info_id = pci.customer_info_id ");
		hql.append(" left join pfd_user_ad_account_ref puaar ");
		hql.append(" on pfpconvert.pfp_customer_info_id = puaar.pfp_customer_info_id ");
		hql.append(" left join pfd_customer_info pfdci ");
		hql.append(" on puaar.pfd_customer_info_id = pfdci.customer_info_id ");
		hql.append(" where  1 = 1 ");
		hql.append(" and pfpconvert.convert_status = '"+convertTrackingVO.getStatus()+"' ");
		
		//pfdId
		if (StringUtils.isNotBlank(convertTrackingVO.getPfdCustomerInfoId())){
			hql.append(" and pfdci.customer_info_id = '"+convertTrackingVO.getPfdCustomerInfoId()+"' ");
		}
		
		//pfpId
		if (StringUtils.isNotBlank(convertTrackingVO.getPfpCustomerInfoId())){
			hql.append(" and pfpconvert.pfp_customer_info_id = '"+convertTrackingVO.getPfpCustomerInfoId()+"' ");
		}
		
		//pfpName
		if (StringUtils.isNotBlank(convertTrackingVO.getPfpCustomerInfoName())){
			hql.append(" and pci.customer_info_title = '"+convertTrackingVO.getPfpCustomerInfoName()+"' ");
		}
		
		log.info(hql.toString());

		Query query = null;
		query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		String retargetingTrackingCount = String.valueOf(query.list().get(0));
		
		return retargetingTrackingCount;
	}
	
	
	public List<Map<String,Object>> findPfpCodeConvertList(PfpConvertTrackingVO convertTrackingVO) throws Exception{
		
		StringBuffer hql = new StringBuffer();
		hql.append(" select ");
		hql.append(" pfpconvert.convert_seq, ");
		hql.append(" pfpconvert.click_range_date, ");
		hql.append(" pfpconvert.imp_range_date, ");
		hql.append(" pfpconvert.pfp_customer_info_id, ");
		hql.append(" pci.customer_info_title, ");
		hql.append(" pfdci.customer_info_id, ");
		hql.append(" pfdci.company_name ");
		hql.append(" from pfp_code_convert pfpconvert ");
		hql.append(" left join pfp_customer_info pci ");
		hql.append(" on pfpconvert.pfp_customer_info_id = pci.customer_info_id ");
		hql.append(" left join pfd_user_ad_account_ref puaar ");
		hql.append(" on pfpconvert.pfp_customer_info_id = puaar.pfp_customer_info_id ");
		hql.append(" left join pfd_customer_info pfdci ");
		hql.append(" on puaar.pfd_customer_info_id = pfdci.customer_info_id ");
		hql.append(" where  1 = 1 ");
		hql.append(" and pfpconvert.convert_status = '"+convertTrackingVO.getStatus()+"' ");
		
		//pfdId
		if (StringUtils.isNotBlank(convertTrackingVO.getPfdCustomerInfoId())){
			hql.append(" and pfdci.customer_info_id = '"+convertTrackingVO.getPfdCustomerInfoId()+"' ");
		}
		
		//pfpId
		if (StringUtils.isNotBlank(convertTrackingVO.getPfpCustomerInfoId())){
			hql.append(" and pfpconvert.pfp_customer_info_id = '"+convertTrackingVO.getPfpCustomerInfoId()+"' ");
		}
		
		//pfpName
		if (StringUtils.isNotBlank(convertTrackingVO.getPfpCustomerInfoName())){
			hql.append(" and pci.customer_info_title = '"+convertTrackingVO.getPfpCustomerInfoName()+"' ");
		}

		log.info(hql.toString());
		
		Query query = null;
		query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setFirstResult((convertTrackingVO.getPageNo()-1)*convertTrackingVO.getPageSize());
		query.setMaxResults(convertTrackingVO.getPageSize());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
		
		return query.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getConvertTrackingList(PfpConvertTrackingVO convertTrackingVO) throws Exception{

		StringBuffer hql = new StringBuffer();
		
		hql.append(" SELECT a.convert_seq,  ");
		hql.append(" 		a.convert_name, ");
		hql.append(" 		a.pa_id, ");
		hql.append(" 		a.pfp_customer_info_id, ");
		hql.append(" 		a.convert_type, ");
		hql.append(" 		a.click_range_date, ");
		hql.append(" 		a.imp_range_date, ");
		hql.append(" 		a.convert_class, ");
		hql.append(" 		a.convert_price_type, ");
		hql.append("		a.convert_price, ");
		hql.append("		a.convert_status, ");
		hql.append(" 		a.convert_belong, ");
		hql.append(" 		a.convert_num_type,  ");
		hql.append(" 		a.convert_rule_num,  ");
		hql.append(" 		Ifnull(b.trans_convert_price, 0)trans_convert_price, ");
		hql.append(" 		Ifnull(c.ck_convert_count, 0)trans_ck_convert_count,  ");
		hql.append(" 		Ifnull(d.pv_convert_count, 0)trans_pv_convert_count,  ");
		hql.append(" 		pfdci.customer_info_id, ");
		hql.append(" 		pfdci.company_name,  ");
		hql.append(" 		pci.customer_info_title  ");
		hql.append(" FROM   (SELECT *   ");
		hql.append("   		 FROM   pfp_code_convert ");
		hql.append("  		 WHERE  1 = 1	");
		hql.append("  		 AND convert_seq = '"+convertTrackingVO.getConvertSeq()+"' ");
		hql.append(" 		 AND convert_status != '"+EnumConvertStatusType.Delete.getType()+"') AS a  ");
		hql.append(" 		 LEFT JOIN (SELECT convert_seq,  ");
		hql.append("  					Sum(convert_price) AS trans_convert_price ");
		hql.append("  					FROM   pfp_code_convert_trans ");
		hql.append("  					WHERE  1 = 1 ");
		hql.append("  					AND convert_trigger_type = 'CK'  ");
		hql.append("  					AND convert_seq = '"+convertTrackingVO.getConvertSeq()+"' ");
		hql.append(" 					AND convert_date >= '"+convertTrackingVO.getCkStartDate()+"' ");
		hql.append(" 					AND convert_date <= '"+convertTrackingVO.getCkEndDate()+"' ");
		hql.append(" 				   ) AS b ");
		hql.append("		ON a.convert_seq = b.convert_seq ");
		hql.append("  		LEFT JOIN (SELECT convert_seq, ");
		hql.append(" 				   Sum(convert_count)AS ck_convert_count ");
		hql.append(" 				   FROM   pfp_code_convert_trans ");
		hql.append(" 				   WHERE  1 = 1 	 ");
		hql.append("  				   AND convert_trigger_type = 'CK' 	");
		hql.append(" 				   AND convert_seq = '"+convertTrackingVO.getConvertSeq()+"' ");
		hql.append(" 				   AND convert_date >= '"+convertTrackingVO.getCkStartDate()+"' ");
		hql.append(" 				   AND convert_date <= '"+convertTrackingVO.getCkEndDate()+"' ");
		hql.append(" 				  ) AS c 	");
		hql.append("		ON a.convert_seq = c.convert_seq ");
		hql.append(" 		LEFT JOIN (SELECT convert_seq, ");
		hql.append(" 				   Sum(convert_count)AS pv_convert_count ");
		hql.append(" 				   FROM   pfp_code_convert_trans  ");
		hql.append("				   WHERE  1 = 1 ");
		hql.append("				   AND convert_trigger_type = 'PV' 	  ");
		hql.append(" 				   AND convert_seq = '"+convertTrackingVO.getConvertSeq()+"' ");
		hql.append(" 				   AND convert_date >= '"+convertTrackingVO.getPvStartDate()+"' ");
		hql.append(" 				   AND convert_date <= '"+convertTrackingVO.getPvEndDate()+"' ");
		hql.append(" 				  ) AS d  ");
		hql.append("  		ON a.convert_seq = d.convert_seq ");
		hql.append("		LEFT JOIN pfp_customer_info pci   ");
		hql.append("  		ON a.pfp_customer_info_id = pci.customer_info_id  ");
		hql.append("		LEFT JOIN pfd_user_ad_account_ref puaar   ");
		hql.append(" 		ON a.pfp_customer_info_id = puaar.pfp_customer_info_id  ");
		hql.append(" 		LEFT JOIN pfd_customer_info pfdci  ");
		hql.append(" 		ON puaar.pfd_customer_info_id = pfdci.customer_info_id ");
		
		log.info(hql.toString());
		
		Query query = null;
		query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
		
		return query.list();
	}
	
}