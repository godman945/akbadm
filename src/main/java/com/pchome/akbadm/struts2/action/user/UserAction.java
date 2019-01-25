package com.pchome.akbadm.struts2.action.user;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.user.UserConstants;
import com.pchome.akbadm.db.dao.menu.MenuVO;
import com.pchome.akbadm.db.dao.privilege.PrivilegeModelVO;
import com.pchome.akbadm.db.pojo.AdmDepartment;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.department.DepartmentService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.akbadm.db.service.privilege.PrivilegeModelService;
import com.pchome.akbadm.db.service.user.UserService;
import com.pchome.akbadm.struts2.BaseAction;

public class UserAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private PrivilegeModelService privilegeModelService;
	private MenuService menuService;
	private UserService userService;
	private DepartmentService departmentService;
	private String departmentInfo;
	private String message = "";

	//查詢條件(查詢頁面用)
	private String queryUserId; //帳號
	private String queryUserName; //姓名

	//查詢結果(查詢頁面用)
	private List<AdmUser> userList;

	//權限模組代碼對照表(全頁面通用)
	private Map<String, String> privilegeModelMap = null;

	//帳號狀態對照表(全頁面通用)
	private Map<String, String> userStatusMap = UserConstants.getUserStatusMap();

	//指定user Id(查詢頁面點修改或刪除時使用)
	private String userId;

	//指定model Id(權限預覽 AJAX 使用)
	private String modelId;

	//組織 Id(置換小組清單 AJAX 使用)
	private String parentDeptId;

	//元件參數(新增及修改頁面用)
	private String paramUserEmail;
	private String paramUserPassword;
	private String paramUserName;
	private String paramStatus;
	private String paramNote;
	private String paramModelId = "0"; //權限模組
	private String paramParentDeptId = "0"; //部門
	private String paramChildDeptId = "0"; //小組

	//權限預覽 AJAX 輸出字串
	private String outputHtml;

	//更換小組 AJAX 輸出字串
	private String outputHtml2;

	//部門組織下拉選單
	private List<AdmDepartment> parentDeptList;
	private List<AdmDepartment> childDeptList;

	public String execute() throws Exception {
		this.init();
		this.userList = userService.getAllUser();
		return SUCCESS;
	}

	public String query() throws Exception {
		System.out.println(">>> queryUserId = " + queryUserId);
		System.out.println(">>> queryUserName = " + queryUserName);
		this.init();
		this.userList = userService.getUserByCondition(queryUserId, queryUserName);
		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		this.init();
		this.initDeptComponent();
		this.outputHtml = this.getShowPrivilegeHtml(paramModelId);
		return SUCCESS;
	}

	public String doAdd() throws Exception {
		this.init();
		this.initDeptComponent();
		this.outputHtml = this.getShowPrivilegeHtml(paramModelId);

		if (StringUtils.isEmpty(paramUserEmail)) {
			message = "請輸入帳號！";
			return INPUT;
		} else {
			paramUserEmail = paramUserEmail.trim();
			if (paramUserEmail.length() > 50) {
				message = "帳號不可超過 50 字元！";
				return INPUT;
			}
			if (userService.getUserById(paramUserEmail)!=null) {
				message = "此 e-mail 已被申請過帳號！";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramUserPassword)) {
			message = "請輸入密碼！";
			return INPUT;
		} else {
			paramUserPassword = paramUserPassword.trim();
			if (paramUserPassword.length() > 20) {
				message = "密碼不可超過 20 字元！";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramUserName)) {
			message = "請輸入姓名！";
			return INPUT;
		} else {
			paramUserName = paramUserName.trim();
			if (paramUserName.length() > 20) {
				message = "姓名不可超過 20 字元！";
				return INPUT;
			}
		}

		if (paramParentDeptId==null || paramParentDeptId.equals("0")) {
			message = "請選擇部門！";
			return INPUT;
		}

		if (paramChildDeptId==null || paramChildDeptId.equals("0")) {
			message = "請選擇小組！";
			return INPUT;
		}

		if (paramModelId.equals("0")) {
			message = "請選擇權限範本！";
			return INPUT;
		}

		if (StringUtils.isNotEmpty(paramNote)) {
			paramNote = paramNote.trim();
			if (paramNote.length() > 50) {
				message = "帳號備註不可超過 50 字元！";
				return INPUT;
			}
		}

		AdmUser user = new AdmUser();
		user.setUserEmail(paramUserEmail);
		user.setUserPassword(paramUserPassword);
		user.setUserName(paramUserName);
		user.setDepId(paramParentDeptId);
		user.setDepId2(paramChildDeptId);
		user.setModelId(paramModelId);
		user.setStatus(paramStatus);
		user.setNote(paramNote);
		Date now = new Date();
		user.setCreateDate(now);
		user.setUpdateDate(now);

		userService.insertUser(user);

		message = "新增成功！";

		return SUCCESS;
	}

	public String goUpdatePage() throws Exception {
		System.out.println(">>> userId = " + userId);
		this.init();

		AdmUser user = userService.getUserById(userId);

		this.paramUserEmail = user.getUserEmail();
		this.paramUserPassword = user.getUserPassword();
		this.paramUserName = user.getUserName();
		this.paramStatus = user.getStatus();
		this.paramNote = user.getNote();
		this.paramModelId = user.getModelId();
		this.paramParentDeptId = user.getDepId();
		this.paramChildDeptId = user.getDepId2();

		this.initDeptComponent();
		this.outputHtml = this.getShowPrivilegeHtml(paramModelId);

		return SUCCESS;
	}

	public String doUpdate() throws Exception {
		this.init();
		this.outputHtml = this.getShowPrivilegeHtml(paramModelId);

		if (StringUtils.isEmpty(paramUserPassword)) {
			message = "請輸入密碼！";
			return INPUT;
		} else {
			paramUserPassword = paramUserPassword.trim();
			if (paramUserPassword.length() > 20) {
				message = "密碼不可超過 20 字元！";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramUserName)) {
			message = "請輸入姓名！";
			return INPUT;
		} else {
			paramUserName = paramUserName.trim();
			if (paramUserName.length() > 20) {
				message = "姓名不可超過 20 字元！";
				return INPUT;
			}
		}

		if (paramParentDeptId==null || paramParentDeptId.equals("0")) {
			message = "請選擇部門！";
			return INPUT;
		}

		if (paramChildDeptId==null || paramChildDeptId.equals("0")) {
			message = "請選擇小組！";
			return INPUT;
		}

		if (paramModelId.equals("0")) {
			message = "請選擇權限範本！";
			return INPUT;
		}

		if (StringUtils.isNotEmpty(paramNote)) {
			paramNote = paramNote.trim();
			if (paramNote.length() > 50) {
				message = "帳號備註不可超過 50 字元！";
				return INPUT;
			}
		}
System.out.println(">>> paramParentDeptId = " + paramParentDeptId + ", paramChildDeptId = " + paramChildDeptId);
AdmUser user = userService.getUserById(paramUserEmail);
		user.setUserPassword(paramUserPassword);
		user.setUserName(paramUserName);
		user.setDepId(paramParentDeptId);
		user.setDepId2(paramChildDeptId);
		user.setModelId(paramModelId);
		user.setStatus(paramStatus);
		user.setNote(paramNote);
		Date now = new Date();
		user.setUpdateDate(now);

		userService.updateUser(user);

		message = "修改成功！";

		return SUCCESS;
	}

	public String doDelete() throws Exception {
		System.out.println(">>> userId = " + userId);

		userService.deleteUser(userId);

		message = "刪除成功！";

		return SUCCESS;
	}

	private Map<String, String> transPrivilegeModel(List<AdmPrivilegeModel> privilegeModelList) {
		Map<String, String> map = new TreeMap<String, String>();
		for (int i=0; i<privilegeModelList.size(); i++) {
			AdmPrivilegeModel privilegeModel = privilegeModelList.get(i);
			map.put(privilegeModel.getModelId().toString(), privilegeModel.getModelName());
		}
		return map;
	}

	private void init() throws Exception {
		List<AdmPrivilegeModel> privilegeModelList = privilegeModelService.getAllPrivilegeModel();
		this.privilegeModelMap = this.transPrivilegeModel(privilegeModelList);
	}

	public String showPrivilegeAJAX() throws Exception {
		System.out.println(">>> modelId = " + modelId);

		this.outputHtml = this.getShowPrivilegeHtml(modelId);

		return SUCCESS;
	}

	private String getShowPrivilegeHtml(String modelId) throws Exception {

		List<MenuVO> allMenuList = null;
		if (modelId.equals("0")) {
			allMenuList = menuService.getSortMenu(null);
		} else {
			PrivilegeModelVO privilegeModelVO = privilegeModelService.getPrivilegeModelById(modelId);
			List<String> dbMenuIdList = privilegeModelVO.getDbMenuIdList();
			allMenuList = menuService.getSortMenu(dbMenuIdList);
		}

		StringBuffer sbr = new StringBuffer();
		sbr.append("<table width='600' class='table01'>");
		for (int i=0; i<allMenuList.size(); i++) {
			MenuVO menuVO = allMenuList.get(i);
			sbr.append("<tr>");
			sbr.append("<td class='td01' width='100'>");
			sbr.append(menuVO.getDisplayName());
			sbr.append("</td>");
			sbr.append("<td class='td02'>");
			List<MenuVO> childrenList = menuVO.getChildrenList();
			for (int k=0; k<childrenList.size(); k++) {
				MenuVO menuVO2 = childrenList.get(k);
				sbr.append("<input type='checkbox' disabled='disabled' " + menuVO2.getCheckedFlag() + "> " + menuVO2.getDisplayName());
				sbr.append("&nbsp;&nbsp;");
			}
			sbr.append("</td>");
			sbr.append("</tr>");
		}
		sbr.append("</table>");

		return sbr.toString();
	}

	public String changeChildDeptAJAX() throws Exception {
		System.out.println(">>> parentDeptId = " + parentDeptId);

		this.outputHtml2 = this.getChildDeptOptionHtml();

		return SUCCESS;
	}

	private String getChildDeptOptionHtml() throws Exception {

		if (!parentDeptId.equals("0")) {
			childDeptList = departmentService.getChildDepartmentByParentId(parentDeptId);
		}

		StringBuffer sbr = new StringBuffer();
		sbr.append("<select name='paramChildDeptId' id='paramChildDeptId'>");
		sbr.append("<option value='0'>-- 請選擇小組 --</option>");
		for (int i=0; i<childDeptList.size(); i++) {
			AdmDepartment dept = childDeptList.get(i);
			sbr.append("<option value='" + dept.getDepId() + "'>" + dept.getDepName() + "</option>");
		}
		sbr.append("</select>");

		return sbr.toString();
	}

	private void initDeptComponent() throws Exception {
		this.parentDeptList = departmentService.getAllParentDepartment();
		
		
		if (paramParentDeptId!=null && !this.paramParentDeptId.equals("0")) {
			this.childDeptList = departmentService.getChildDepartmentByParentId(this.paramParentDeptId);
		}
	}

	public void setPrivilegeModelService(PrivilegeModelService privilegeModelService) {
		this.privilegeModelService = privilegeModelService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getQueryUserId() {
		return queryUserId;
	}

	public void setQueryUserId(String queryUserId) {
		this.queryUserId = queryUserId;
	}

	public String getQueryUserName() {
		return queryUserName;
	}

	public void setQueryUserName(String queryUserName) {
		this.queryUserName = queryUserName;
	}

	public List<AdmUser> getUserList() {
		return userList;
	}

	public void setUserList(List<AdmUser> userList) {
		this.userList = userList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Map<String, String> getPrivilegeModelMap() {
		return privilegeModelMap;
	}

	public Map<String, String> getUserStatusMap() {
		return userStatusMap;
	}

	public String getOutputHtml() {
		return outputHtml;
	}

	public void setOutputHtml(String outputHtml) {
		this.outputHtml = outputHtml;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getParamUserEmail() {
		return paramUserEmail;
	}

	public void setParamUserEmail(String paramUserEmail) {
		this.paramUserEmail = paramUserEmail;
	}

	public String getParamUserPassword() {
		return paramUserPassword;
	}

	public void setParamUserPassword(String paramUserPassword) {
		this.paramUserPassword = paramUserPassword;
	}

	public String getParamUserName() {
		return paramUserName;
	}

	public void setParamUserName(String paramUserName) {
		this.paramUserName = paramUserName;
	}

	public String getParamStatus() {
		return paramStatus;
	}

	public void setParamStatus(String paramStatus) {
		this.paramStatus = paramStatus;
	}

	public String getParamNote() {
		return paramNote;
	}

	public void setParamNote(String paramNote) {
		this.paramNote = paramNote;
	}

	public String getParamModelId() {
		return paramModelId;
	}

	public void setParamModelId(String paramModelId) {
		this.paramModelId = paramModelId;
	}

	public List<AdmDepartment> getParentDeptList() {
		return parentDeptList;
	}

	public List<AdmDepartment> getChildDeptList() {
		return childDeptList;
	}

	public String getParamParentDeptId() {
		return paramParentDeptId;
	}

	public void setParamParentDeptId(String paramParentDeptId) {
		this.paramParentDeptId = paramParentDeptId;
	}

	public String getParamChildDeptId() {
		return paramChildDeptId;
	}

	public void setParamChildDeptId(String paramChildDeptId) {
		this.paramChildDeptId = paramChildDeptId;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public String getOutputHtml2() {
		return outputHtml2;
	}

	public void setOutputHtml2(String outputHtml2) {
		this.outputHtml2 = outputHtml2;
	}

	public String getDepartmentInfo() {
		return departmentInfo;
	}

	public void setDepartmentInfo(String departmentInfo) {
		this.departmentInfo = departmentInfo;
	}
	
	
}
