package com.pchome.akbadm.struts2.action.department;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.department.DepartmentVO;
import com.pchome.akbadm.db.pojo.AdmDepartment;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.department.DepartmentService;
import com.pchome.akbadm.struts2.BaseAction;

public class DepartmentAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private DepartmentService departmentService;

	private String deptId;

	private String parentDeptName;
	private String parentSort = "0";
	private String[] childDeptId;
	private String[] childDeptName;
	private String[] childDeptSort;

	private String message = "";

	private List<DepartmentVO> parentDeptList = new ArrayList<DepartmentVO>();
	private List<AdmDepartment> childDeptList = new ArrayList<AdmDepartment>();

	public String execute() throws Exception {
		List<AdmDepartment> tmpParentDeptList = departmentService.getAllParentDepartment();
		if (tmpParentDeptList!=null) {
			for (int i=0; i<tmpParentDeptList.size(); i++) {
				DepartmentVO deptVO = new DepartmentVO();
				AdmDepartment dept = tmpParentDeptList.get(i);
				deptVO.setDeptId(dept.getDepId());
				deptVO.setDeptName(dept.getDepName());
				deptVO.setSort(Integer.toString(dept.getSort()));
				List<AdmDepartment> tmpChildDeptList = departmentService.getChildDepartmentByParentId(dept.getDepId().toString());
				if (tmpChildDeptList!=null) {
					String strChildDept = "";
					for (int k=0; k<tmpChildDeptList.size(); k++) {
						strChildDept += tmpChildDeptList.get(k).getDepName() + ", ";
					}
					strChildDept = strChildDept.trim();
					log.info(">>> index = " + strChildDept.lastIndexOf(","));
					log.info(">>> index2 = " + strChildDept.length());
					if (strChildDept.lastIndexOf(",") == (strChildDept.length()-1)) {
						strChildDept = strChildDept.substring(0, strChildDept.length()-1);
					}
					log.info(">>> strChildDept = " + strChildDept);
					deptVO.setChildDept(strChildDept);
				}
				parentDeptList.add(deptVO);
			}
		}

		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		return SUCCESS;
	}

	public String doAdd() throws Exception {

		log.info(">>> parentDeptName = " + parentDeptName);
		log.info(">>> parentSort = " + parentSort);

		if (StringUtils.isEmpty(parentDeptName)) {
			message = "請輸入部門名稱！";
			return INPUT;
		}

		//排序有問題就給99
		int intParentSort = 0;
		if (StringUtils.isEmpty(parentSort)) {
			intParentSort = 99;
			this.parentSort = "99";
		} else {
			try {
				intParentSort = Integer.parseInt(parentSort);
			} catch (Exception e) {
				intParentSort = 99;
				this.parentSort = "99";
			}
		}

		if (childDeptName==null || childDeptSort==null) {
			message = "請設定所屬小組！";
			return INPUT;
		}

		if (childDeptName.length!=childDeptSort.length) {
			message = "小組設定資料錯誤！";
			return INPUT;
		}

		Date now = new Date();

		for (int i=0; i<childDeptName.length; i++) {
			if (StringUtils.isNotEmpty(childDeptName[i])) {
				AdmDepartment childDept = new AdmDepartment();
				childDept.setDepName(childDeptName[i]);
				int intChildSort = 0;
				if (StringUtils.isNotEmpty(childDeptSort[i])) {
					
					try {
						intChildSort = Integer.parseInt(childDeptSort[i]);
					} catch (Exception e) {
						intChildSort = 99;
					}
				} else {
					intChildSort = 99;
				}
				childDept.setSort(intChildSort);
				childDept.setCreateDate(now);
				childDept.setUpdateDate(now);

				childDeptList.add(childDept);
			}
		}

		//save to db
		AdmDepartment parentDept = new AdmDepartment();
		parentDept.setDepName(parentDeptName);
		parentDept.setSort(intParentSort);
		parentDept.setCreateDate(now);
		parentDept.setUpdateDate(now);

		Integer parentDeptId = departmentService.saveDepartment(parentDept);

		String strParentDeptId = parentDeptId.toString();
		for (int i=0; i<childDeptList.size(); i++) {
			AdmDepartment childDept = childDeptList.get(i);
			childDept.setParentDepId(strParentDeptId);
			departmentService.saveDepartment(childDept);
		}

		message = "新增成功！";

		return SUCCESS;
	}

	public String goUpdatePage() throws Exception {
		log.info(">>> deptId = " + deptId);

		AdmDepartment parentDept = departmentService.findParentDeptById(deptId);

		this.parentDeptName = parentDept.getDepName();
		this.parentSort =  Integer.toString(parentDept.getSort());

		this.childDeptList = departmentService.getChildDepartmentByParentId(deptId);

		return SUCCESS;
	}

	public String doUpdate() throws Exception {

		log.info(">>> deptId = " + deptId);
		log.info(">>> parentDeptName = " + parentDeptName);
		log.info(">>> parentSort = " + parentSort);

		if (StringUtils.isEmpty(parentDeptName)) {
			message = "請輸入部門名稱！";
			return INPUT;
		}

		//排序有問題就給99
		int intParentSort = 0;
		if (StringUtils.isEmpty(parentSort)) {
			intParentSort = 99;
			this.parentSort = "99";
		} else {
			try {
				intParentSort = Integer.parseInt(parentSort);
			} catch (Exception e) {
				intParentSort = 99;
				this.parentSort = "99";
			}
		}

		if (childDeptId==null || childDeptName==null || childDeptSort==null) {
			message = "請設定所屬小組！";
			return INPUT;
		}

log.info(">>> childDeptId.length = " + childDeptId.length);
log.info(">>> childDeptName.length = " + childDeptName.length);
log.info(">>> childDeptSort.length = " + childDeptSort.length);

		if (childDeptId.length!=childDeptName.length || childDeptId.length!=childDeptSort.length) {
			message = "小組設定資料錯誤！";
			return INPUT;
		}

		if (childDeptId.length<1) {
			message = "請設定所屬小組！";
			return INPUT;
		}

		Date now = new Date();

		//save parent Department
		AdmDepartment parentDept = departmentService.findParentDeptById(deptId);
		parentDept.setDepName(parentDeptName);
		parentDept.setSort(intParentSort);
		parentDept.setUpdateDate(now);

		departmentService.saveDepartment(parentDept);

		List<AdmDepartment> updateChildDept = new ArrayList<AdmDepartment>();
		List<AdmDepartment> insertChildDept = new ArrayList<AdmDepartment>();
		//save child Department
		for (int i=0; i<childDeptId.length; i++) {
			if (StringUtils.isNotEmpty(childDeptName[i])) {

				int intChildSort = 0;
				if (StringUtils.isNotEmpty(childDeptSort[i])) {
					try {
						intChildSort = Integer.parseInt(childDeptSort[i]);
					} catch (Exception e) {
						intChildSort = 99;
					}
				} else {
					intChildSort = 99;
				}

				AdmDepartment childDept = null;
				if (StringUtils.isNotEmpty(childDeptId[i])) { //舊的
					childDept = departmentService.getDeptById(childDeptId[i]);
					childDept.setDepName(childDeptName[i]);
					childDept.setSort(intChildSort);
					childDept.setUpdateDate(now);
					updateChildDept.add(childDept);
				} else {
					childDept = new AdmDepartment();
					log.info("++++++ deptId = " + deptId);
					childDept.setParentDepId(deptId); //加上這行就會新增失敗??
					childDept.setCreateDate(now);
					childDept.setDepName(childDeptName[i]);
					childDept.setSort(intChildSort);
					childDept.setUpdateDate(now);
					insertChildDept.add(childDept);
				}
			}
		}

		//撈出舊的 -> 比對要刪除的
		List<AdmDepartment> oldChildDept = departmentService.getChildDepartmentByParentId(deptId);
		for (int i=0; i<oldChildDept.size(); i++) {
			boolean needDelete = true;
			String oldChildDeptId = oldChildDept.get(i).getDepId().toString();
			String oldChildDeptName = oldChildDept.get(i).getDepName();
			for (int k=0; k<childDeptId.length; k++) {
				if (oldChildDeptId.equals(childDeptId[k])) {
					needDelete = false;
					break;
				}
			}
			if (needDelete) {
				List<AdmUser> userList = departmentService.deleteChildDeptById(oldChildDeptId);

				if (userList!=null) {
					message += "刪除失敗！ [" + oldChildDeptName + "]使用中, 小組成員: ";
					for (int z=0; z<userList.size(); z++) {
						message += userList.get(z).getUserEmail() + ", ";
					}
				}
			}
		}

		for (int i=0; i<updateChildDept.size(); i++) {
			departmentService.saveDepartment(updateChildDept.get(i));
		}

		for (int i=0; i<insertChildDept.size(); i++) {
			departmentService.saveDepartment(insertChildDept.get(i));
		}

		if (StringUtils.isNotEmpty(message)) {
			return INPUT;
		} else {
			message = "修改成功！";
			return SUCCESS;
		}
	}

	public String doDelete() throws Exception {
		log.info(">>> deptId = " + deptId);

		List<AdmUser> userList = departmentService.deleteParentDeptById(deptId);

		if (userList==null) {
			message = "刪除成功！";
		} else {
			message = "刪除失敗！ 此部門使用中, 部門成員: ";
			for (int i=0; i<userList.size(); i++) {
				message += userList.get(i).getUserEmail() + ", ";
			}
			message = message.trim();
			if (message.lastIndexOf(",") == (message.length()-1)) {
				message = message.substring(0, message.length()-1);
			}
		}

		return SUCCESS;
	}

	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getParentDeptName() {
		return parentDeptName;
	}

	public void setParentDeptName(String parentDeptName) {
		this.parentDeptName = parentDeptName;
	}

	public String getParentSort() {
		return parentSort;
	}

	public void setParentSort(String parentSort) {
		this.parentSort = parentSort;
	}

	public String[] getChildDeptName() {
		return childDeptName;
	}

	public void setChildDeptName(String[] childDeptName) {
		this.childDeptName = childDeptName;
	}

	public String[] getChildDeptSort() {
		return childDeptSort;
	}

	public void setChildDeptSort(String[] childDeptSort) {
		this.childDeptSort = childDeptSort;
	}

	public List<DepartmentVO> getParentDeptList() {
		return parentDeptList;
	}

	public void setParentDeptList(List<DepartmentVO> parentDeptList) {
		this.parentDeptList = parentDeptList;
	}

	public List<AdmDepartment> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<AdmDepartment> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String[] getChildDeptId() {
		return childDeptId;
	}

	public void setChildDeptId(String[] childDeptId) {
		this.childDeptId = childDeptId;
	}
}
