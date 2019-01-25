package com.pchome.akbadm.db.service.department;

import java.util.List;

import com.pchome.akbadm.db.dao.department.DepartmentDAO;
import com.pchome.akbadm.db.dao.user.AdmUserDAO;
import com.pchome.akbadm.db.pojo.AdmDepartment;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.BaseService;

public class DepartmentService extends BaseService<AdmDepartment, String> implements IDepartmentService {

	private DepartmentDAO departmentDAO;
	private AdmUserDAO admuserDAO;

	public List<AdmDepartment> getAllParentDepartment() throws Exception {
		return departmentDAO.getParentDepartment();
	}

	public List<AdmDepartment> getChildDepartmentByParentId(String parentId) throws Exception {
		return departmentDAO.getChildDepartment(parentId);
	}

	public AdmDepartment getDeptById(String deptId) throws Exception {
		return departmentDAO.getDeptById(deptId);
	}

	public Integer saveDepartment(AdmDepartment department) throws Exception {
		return departmentDAO.saveDepartment(department);
	}

	public void addDepartment(AdmDepartment department) throws Exception {
		departmentDAO.save(department);
	}

	public void updateDepartment(AdmDepartment department) throws Exception {
		departmentDAO.update(department);
	}

	/**
	 * 刪除部門
	 */
	public List<AdmUser> deleteParentDeptById(String deptId) throws Exception {

		//檢查是否有使用者正在使用此部門 -> 有人使用時不准刪
		List<AdmUser> userList = admuserDAO.getUserByDeptId(deptId);

		if (userList.size()>0) {
			return userList;
		}

		//刪除組織
		departmentDAO.deleteParentDept(deptId);

		return null;
	}

	/**
	 * 刪除小組
	 */
	public List<AdmUser> deleteChildDeptById(String deptId) throws Exception {

		//檢查是否有使用者正在使用此組織設定 -> 有人使用時不准刪
		List<AdmUser> userList = admuserDAO.getUserByDeptId2(deptId);

		if (userList.size()>0) {
			return userList;
		}

		//刪除組織
		departmentDAO.deleteChildDept(deptId);

		return null;
	}

	public AdmDepartment findParentDeptById(String deptId) throws Exception {
		return departmentDAO.findParentDeptById(deptId);
	}

	public void setDepartmentDAO(DepartmentDAO departmentDAO) {
		this.departmentDAO = departmentDAO;
	}

	public void setAdmuserDAO(AdmUserDAO admuserDAO) {
		this.admuserDAO = admuserDAO;
	}
}
