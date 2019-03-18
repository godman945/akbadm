package com.pchome.akbadm.db.dao.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmAdPool;

public class AdmAdPoolDAO extends BaseDAO<AdmAdPool, String> implements IAdmAdPoolDAO {

    @SuppressWarnings("unchecked")
	public List<AdmAdPool> getAdPoolByCondition(String adPoolSeq, String aPoolName, String diffComapny) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmAdPool where 1=1");
		if (StringUtils.isNotEmpty(adPoolSeq)) {
			sql.append(" and adPoolSeq like '%" + adPoolSeq.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(aPoolName)) {
			sql.append(" and adPoolName like '%" + aPoolName.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(diffComapny)) {
			sql.append(" and diffCompany like '%" + diffComapny.trim() + "%'");
		}
		return (List<AdmAdPool>) super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmAdPool getAdPoolBySeq(String adPoolSeq) throws Exception {
		List<AdmAdPool> list = (List<AdmAdPool>) super.getHibernateTemplate().find("from AdmAdPool where adPoolSeq = '" + adPoolSeq + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
