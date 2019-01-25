package com.pchome.akbadm.db.dao.user;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.dao.user.IAdmUserDAO;
import com.pchome.akbadm.db.pojo.AdmUser;

public class AdmUserDAO extends BaseDAO<AdmUser, String> implements IAdmUserDAO {

	@SuppressWarnings("unchecked")
	public List<AdmUser> getUserByPrivilegeModelId(String privilegeModelId) throws Exception {
		return super.getHibernateTemplate().find("from AdmUser where modelId = '" + privilegeModelId + "'");
	}

	@SuppressWarnings("unchecked")
	public List<AdmUser> getUserByCondition(String userEmail, String userName) throws Exception {
		StringBuffer sql = new StringBuffer("from AdmUser where 1=1");
		if (StringUtils.isNotEmpty(userEmail)) {
			sql.append(" and userEmail like '" + userEmail.trim() + "%'");
		}

		if (StringUtils.isNotEmpty(userName)) {
			sql.append(" and userName like '%" + userName.trim() + "%'");
		}
		return super.getHibernateTemplate().find(sql.toString());
	}

	@SuppressWarnings("unchecked")
	public AdmUser getUserById(String userEmail) throws Exception {
		List<AdmUser> list = super.getHibernateTemplate().find("from AdmUser where userEmail = '" + userEmail + "'");
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void insertUser(AdmUser user) throws Exception {
		this.save(user);
	}

	public void updateUser(AdmUser user) throws Exception {
		this.update(user);
	}

	public void deleteUser(String userEmail) throws Exception {
		String sql = "delete from AdmUser where userEmail = '" + userEmail + "'";
        Session session = getSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	@SuppressWarnings("unchecked")
	public List<AdmUser> getUserByDeptId(String deptId) throws Exception {
		return super.getHibernateTemplate().find("from AdmUser where depId = '" + deptId + "'");
	}

	@SuppressWarnings("unchecked")
	public List<AdmUser> getUserByDeptId2(String deptId) throws Exception {
		return super.getHibernateTemplate().find("from AdmUser where depId2 = '" + deptId + "'");
	}
}
