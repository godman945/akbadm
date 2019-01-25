package com.pchome.akbadm.db.dao.privilege;

import java.util.List;

import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.dao.privilege.IPrivilegeModelDAO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;

public class PrivilegeModelDAO extends BaseDAO<AdmPrivilegeModel, String> implements IPrivilegeModelDAO {

	@SuppressWarnings("unchecked")
	public AdmPrivilegeModel findPrivilegeModelById(String id) throws Exception {
		List<AdmPrivilegeModel> list = this.getHibernateTemplate().find("from AdmPrivilegeModel where modelId = '" + id + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Integer savePrivilegeModel(AdmPrivilegeModel privilegeModel) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(privilegeModel);
		return privilegeModel.getModelId();
	}

	public void deletePrivilegeModelById(String id) throws Exception {
		String sql = "delete from AdmPrivilegeModel where modelId = '" + id + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}
}
