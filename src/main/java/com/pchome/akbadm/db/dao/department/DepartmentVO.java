package com.pchome.akbadm.db.dao.department;

public class DepartmentVO {

	private Integer deptId;
	private String deptName;
	private String sort;
	private String childDept;
	private String selectedFlag;

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getChildDept() {
		return childDept;
	}

	public void setChildDept(String childDept) {
		this.childDept = childDept;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSelectedFlag() {
		return selectedFlag;
	}

	public void setSelectedFlag(String selectedFlag) {
		this.selectedFlag = selectedFlag;
	}
}
