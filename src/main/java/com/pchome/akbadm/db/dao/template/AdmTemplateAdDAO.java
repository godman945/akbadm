package com.pchome.akbadm.db.dao.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;

public class AdmTemplateAdDAO extends BaseDAO<AdmTemplateAd, String> implements IAdmTemplateAdDAO {

	@SuppressWarnings("unchecked")
	public List<AdmTemplateAd> getTemplateAdByCondition(String templateAdSeq, String templateAdName, String templateAdType, String templateAdWidth, String templateAdHeight) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmTemplateAd where 1=1");
		if (StringUtils.isNotEmpty(templateAdSeq)) {
			sql.append(" and templateAdSeq like '%" + templateAdSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(templateAdName)) {
			sql.append(" and templateAdName like '%" + templateAdName.trim() + "%'");
		}
		
		if (StringUtils.isNotEmpty(templateAdType)) {
			sql.append(" and adTemplateProductXType = '" + templateAdType.trim() + "'");
		}
		
		if (StringUtils.isNotEmpty(templateAdWidth)) {
			sql.append(" and templateAdWidth = '" + templateAdWidth.trim() + "'");
		}
		
		if (StringUtils.isNotEmpty(templateAdHeight)) {
			sql.append(" and templateAdHeight = '" + templateAdHeight.trim() + "'");
		}
		
		return super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmTemplateAd getTemplateAdById(String templateAdId) throws Exception {
		List<AdmTemplateAd> list = super.getHibernateTemplate().find("from AdmTemplateAd where templateAdId = '" + templateAdId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmTemplateAd getTemplateAdBySeq(String templateAdSeq) throws Exception {
		List<AdmTemplateAd> list = super.getHibernateTemplate().find("from AdmTemplateAd where templateAdSeq = '" + templateAdSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void insertTemplateAd(AdmTemplateAd templatead) throws Exception {
		this.save(templatead);
	}

	public void updateTemplateAd(AdmTemplateAd templatead) throws Exception {
		this.update(templatead);
	}

	public void deleteTemplateAd(String templateAdSeq) throws Exception {
	String sql = "delete from AdmTemplateAd where templateAdSeq = '" + templateAdSeq + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
        session.clear();
	}

	public String saveTemplateAd(AdmTemplateAd templatead) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(templatead);
		return templatead.getTemplateAdSeq();
	}
}
