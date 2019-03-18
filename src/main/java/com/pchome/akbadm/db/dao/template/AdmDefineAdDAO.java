package com.pchome.akbadm.db.dao.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAd;

public class AdmDefineAdDAO extends BaseDAO<AdmDefineAd, String> implements IAdmDefineAdDAO {

    @SuppressWarnings("unchecked")
	public List<AdmDefineAd> getDefineAdByCondition(String defineAdSeq, String defineAdName, String adPoolSeq) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmDefineAd where 1=1");
		if (StringUtils.isNotEmpty(defineAdSeq)) {
			sql.append(" and defineAdSeq like '%" + defineAdSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(defineAdName)) {
			sql.append(" and defineAdName like '%" + defineAdName.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(adPoolSeq)) {
			sql.append(" and adPoolSeq like '%" + adPoolSeq.trim() + "%'");
		}
		return (List<AdmDefineAd>) super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmDefineAd getDefineAdById(String defineAdId) throws Exception {
		List<AdmDefineAd> list = (List<AdmDefineAd>) super.getHibernateTemplate().find("from AdmDefineAd where defineAdId = '" + defineAdId + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmDefineAd getDefineAdBySeq(String defineAdSeq) throws Exception {
		List<AdmDefineAd> list = (List<AdmDefineAd>) super.getHibernateTemplate().find("from AdmDefineAd where defineAdSeq = '" + defineAdSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AdmDefineAd getDefineAdByPoolSeq(String adPoolSeq) throws Exception {
		List<AdmDefineAd> list = (List<AdmDefineAd>) super.getHibernateTemplate().find("from AdmDefineAd where adPoolSeq = '" + adPoolSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

    public int deleteDefineAdByPoolSeq(String adPoolSeq) throws Exception {
        String hql = "delete from AdmDefineAd where adPoolSeq = :adPoolSeq";
        return this.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql).setString("adPoolSeq", adPoolSeq).executeUpdate();
    }
}
