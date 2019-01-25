package com.pchome.akbadm.db.dao.template;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;

public class AdmRelateTproTadDAO extends BaseDAO<AdmRelateTproTad, String> implements IAdmRelateTproTadDAO {

	@SuppressWarnings("unchecked")
	public List<AdmRelateTproTad> getRelateTproTadByCondition(String templateProductSeq, String templateAdSeq) throws Exception {
		final StringBuffer sql = new StringBuffer()
		.append("select * from adm_relate_tpro_tad")
		.append(" where 1=1");
		if (StringUtils.isNotEmpty(templateProductSeq)) {
			sql.append(" and template_product_seq like '%" + templateProductSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(templateAdSeq)) {
			sql.append(" and template_ad_seq like '%" + templateAdSeq.trim() + "%'");
		}
		sql.append(" order by relate_tpro_tad_seq");
		System.out.println("sql = " + sql);
	
		return getHibernateTemplate().execute(
				new HibernateCallback<List<AdmRelateTproTad>>() {
					public List<AdmRelateTproTad> doInHibernate(Session session) throws HibernateException, SQLException {
						return session.createSQLQuery(sql.toString())
						.addEntity(AdmRelateTproTad.class)
						.list();
					}
				});
	}

//	@SuppressWarnings("unchecked")
//	public List<AdmRelateTproTad> getRelateTproTadByCondition(String templateProductSeq, String templateAdSeq) throws Exception {
//		StringBuffer sql = new StringBuffer("from AdmRelateTproTad where 1=1");
//		if (StringUtils.isNotEmpty(templateProductSeq)) {
//			sql.append(" and templateProductSeq like '%" + templateProductSeq.trim() + "%'");
//		}
//
//		if (StringUtils.isNotEmpty(templateAdSeq)) {
//			sql.append(" and templateAdSeq like '%" + templateAdSeq.trim() + "%'");
//		}
//		System.out.println("sql = " + sql);
//		return super.getHibernateTemplate().find(sql.toString());
//	}

	@SuppressWarnings("unchecked")
	public AdmRelateTproTad getRelateTproTadById(String relateTproTadId) throws Exception {
		List<AdmRelateTproTad> list = super.getHibernateTemplate().find("from AdmRelateTproTad where relateTproTadId = '" + relateTproTadId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmRelateTproTad getRelateTproTadBySeq(String relateTproTadSeq) throws Exception {
		List<AdmRelateTproTad> list = super.getHibernateTemplate().find("from AdmRelateTproTad where relateTproTadSeq = '" + relateTproTadSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void insertRelateTproTad(AdmRelateTproTad relateTproTad) throws Exception {
		this.saveOrUpdate(relateTproTad);
	}

	public void updateRelateTproTad(AdmRelateTproTad relateTproTad) throws Exception {
		this.update(relateTproTad);
	}

	public void deleteRelateTproTad(String relateTproTadSeq) throws Exception {
		String sql = "delete from AdmRelateTproTad where relateTproTadSeq = '" + relateTproTadSeq + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public void saveRelateTproTad(AdmRelateTproTadVO admRelateTproTadVO) throws Exception {
		String tpSeqSub = null;
		String tadSeq = null;
		if(admRelateTproTadVO.getTemplateProductSeqSub() != null) {
			tpSeqSub = "\'" + admRelateTproTadVO.getTemplateProductSeqSub() + "\'";
		}
		if(admRelateTproTadVO.getTemplateAdSeq() != null) {
			tadSeq = "\'" + admRelateTproTadVO.getTemplateAdSeq() + "\'";
		}
		final StringBuffer sql = new StringBuffer()
		.append("INSERT INTO adm_relate_tpro_tad(relate_tpro_tad_seq, template_product_seq, template_product_seq_sub, template_ad_order, template_ad_seq) ")
		.append("VALUES ( \'" + admRelateTproTadVO.getRelateTproTadSeq() + "\'")
		.append(", \'" + admRelateTproTadVO.getTemplateProductSeq() + "\'")
		.append(", " + tpSeqSub + "")
		.append(", " + admRelateTproTadVO.getTemplateAdOrder() + "")
		.append(", " + tadSeq + ")");
		System.out.println(sql);

        Session session = getSession();
        session.createSQLQuery(sql.toString()).executeUpdate();
        session.flush();
	}
}
