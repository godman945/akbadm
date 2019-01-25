package com.pchome.akbadm.db.service.department;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmDepartment;
import com.pchome.akbadm.db.service.IBaseService;

public interface IDepartmentService extends IBaseService<AdmDepartment, String>{

	public List<AdmDepartment> getAllParentDepartment() throws Exception;

	public List<AdmDepartment> getChildDepartmentByParentId(String parentId) throws Exception;

	public Integer saveDepartment(AdmDepartment department) throws Exception;

	public AdmDepartment findParentDeptById(String deptId) throws Exception;

	public AdmDepartment getDeptById(String deptId) throws Exception;

}
