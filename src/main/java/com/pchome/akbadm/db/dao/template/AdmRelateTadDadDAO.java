package com.pchome.akbadm.db.dao.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;

public class AdmRelateTadDadDAO extends BaseDAO<AdmRelateTadDad, String> implements IAdmRelateTadDadDAO {

	@SuppressWarnings("unchecked")
	public List<AdmRelateTadDad> getRelateTadDadByCondition(String templateAdSeq, String defineAdSeq) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmRelateTadDad where 1=1");
		if (StringUtils.isNotEmpty(templateAdSeq)) {
			sql.append(" and templateAdSeq like '%" + templateAdSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(defineAdSeq)) {
			sql.append(" and defineAdSeq like '%" + defineAdSeq.trim() + "%'");
		}
		return super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmRelateTadDad getRelateTadDadById(String relateTadDadId) throws Exception {
		List<AdmRelateTadDad> list = super.getHibernateTemplate().find("from AdmRelateTadDad where relateTadDadId = '" + relateTadDadId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmRelateTadDad getRelateTadDadBySeq(String relateTadDadSeq) throws Exception {
		List<AdmRelateTadDad> list = super.getHibernateTemplate().find("from AdmRelateTadDad where relateTadDadSeq = '" + relateTadDadSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void insertRelateTadDad(AdmRelateTadDad relateTadDad) throws Exception {
		this.saveOrUpdate(relateTadDad);
	}

	public void updateRelateTadDad(AdmRelateTadDad relateTadDad) throws Exception {
		this.update(relateTadDad);
	}

	public void deleteRelateTadDad(String relateTadDadSeq) throws Exception {
		String sql = "delete from AdmRelateTadDad where relateTadDadSeq = '" + relateTadDadSeq + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public void saveRelateTadDad(AdmRelateTadDadVO admRelateTadDadVO) throws Exception {
		final StringBuffer sql = new StringBuffer()
		.append("INSERT INTO adm_relate_tad_dad(relate_tad_dad_seq, template_ad_seq, define_ad_order, define_ad_seq, ad_pool_seq) ")
		.append("VALUES ( \'" + admRelateTadDadVO.getRelateTadDadSeq() + "\'")
		.append(", \'" + admRelateTadDadVO.getTemplateAdSeq() + "\'")
		.append(", " + admRelateTadDadVO.getDefineAdOrder() + "")
		.append(", \'" + admRelateTadDadVO.getDefineAdSeq() + "\'")
		.append(", \'" + admRelateTadDadVO.getAdPoolSeq() + "\')");
		System.out.println(sql);

        Session session = getSession();
        session.createSQLQuery(sql.toString()).executeUpdate();
        session.flush();
	}
}
