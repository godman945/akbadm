package com.pchome.akbadm.db.dao.privilege;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModelMenuRef;
import com.pchome.config.TestConfig;

public class PrivilegeModelMenuRefDAO extends BaseDAO<AdmPrivilegeModelMenuRef, String> implements IPrivilegeModelMenuRefDAO {

	@SuppressWarnings("unchecked")
	public List<String> getMenuIdByPrivilegeModelId(String modelId) throws Exception {
		List<String> menuIdList = new ArrayList<String>();

		List<AdmPrivilegeModelMenuRef> list = (List<AdmPrivilegeModelMenuRef>) this.getHibernateTemplate().find("from AdmPrivilegeModelMenuRef where id.modelId = '" + modelId + "'");
		if (list!=null) {
			for (int i=0; i<list.size(); i++) {
				menuIdList.add(Integer.toString(list.get(i).getId().getMenuId()));
			}
		}

		return menuIdList;
	}

	public void deletePrivilegeModelMenuRefById(String modelId) throws Exception {
		String sql = "delete from AdmPrivilegeModelMenuRef where id.modelId = '" + modelId + "'";
        Session session =  super.getHibernateTemplate().getSessionFactory().getCurrentSession();
        session.createQuery(sql).executeUpdate();
        session.flush();
	}

	public void insertPrivilegeModelMenuRef(List<AdmPrivilegeModel> privilegeModelList) throws Exception {
		this.getHibernateTemplate().saveOrUpdate(privilegeModelList);
	}

	public static void main(String args[]) {
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		PrivilegeModelMenuRefDAO refDao = (PrivilegeModelMenuRefDAO) context.getBean("PrivilegeModelMenuRefDAO");
	}
}
