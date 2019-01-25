package com.pchome.akbadm.db.dao.department;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmDepartment;

public interface IDepartmentDAO extends IBaseDAO<AdmDepartment, String> {

	public List<AdmDepartment> getParentDepartment() throws Exception;

	public List<AdmDepartment> getChildDepartment(String parentId) throws Exception;

	public Integer saveDepartment(AdmDepartment department) throws Exception;

	public AdmDepartment findParentDeptById(String deptId) throws Exception;

	public void deleteParentDept(String deptId) throws Exception;

	public void deleteChildDept(String deptId) throws Exception;

	public AdmDepartment getDeptById(String deptId) throws Exception;

}
