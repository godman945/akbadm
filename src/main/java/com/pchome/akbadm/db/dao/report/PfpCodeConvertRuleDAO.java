package com.pchome.akbadm.db.dao.report;


import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpCodeConvertRule;
import com.pchome.akbadm.db.vo.report.PfpConvertTrackingVO;

public class PfpCodeConvertRuleDAO extends BaseDAO<PfpCodeConvertRule,String> implements IPfpCodeConvertRuleDAO{
	
		
		@SuppressWarnings("unchecked")
		public List<Map<String,Object>> getPfpCodeConvertRuleByCondition(PfpConvertTrackingVO convertTrackingVO) throws Exception{

			StringBuffer hql = new StringBuffer();
			hql.append(" select * ");
			hql.append(" from pfp_code_convert_rule ");
			hql.append(" where 1=1 ");
			
			if (StringUtils.isNotBlank(convertTrackingVO.getConvertSeq())){
				hql.append(" and convert_seq = '"+convertTrackingVO.getConvertSeq()+"' ");
			}
			
			log.info(hql.toString());

			Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(hql.toString());
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP); 
			
			return query.list();
		}
		

}