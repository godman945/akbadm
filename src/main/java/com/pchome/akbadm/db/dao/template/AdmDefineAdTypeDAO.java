package com.pchome.akbadm.db.dao.template;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAdType;

public class AdmDefineAdTypeDAO extends BaseDAO<AdmDefineAdType, String> implements IAdmDefineAdTypeDAO {

    @SuppressWarnings("unchecked")
	public List<AdmDefineAdType> getDefineAdType() throws Exception {
		return super.getHibernateTemplate().find("from AdmDefineAdType where defineAdTypeId is not null order by defineAdTypeId");
	}

	@SuppressWarnings("unchecked")
	public List<AdmDefineAdType> getDefineAdTypeByCondition(String defineAdTypeId, String defineAdTypeName) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmDefineAdType where 1=1");
		if (StringUtils.isNotEmpty(defineAdTypeId)) {
			sql.append(" and defineAdTypeId like '%" + defineAdTypeId.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(defineAdTypeName)) {
			sql.append(" and defineAdTypeName like '%" + defineAdTypeName.trim() + "%'");
		}
		return super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmDefineAdType 	getDefineAdTypeById(String defineAdTypeById) throws Exception {
		List<AdmDefineAdType> list = super.getHibernateTemplate().find("from AdmDefineAdType where defineAdTypeId = '" + defineAdTypeById + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}
}
