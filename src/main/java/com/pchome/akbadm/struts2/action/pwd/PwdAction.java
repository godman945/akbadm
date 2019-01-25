package com.pchome.akbadm.struts2.action.pwd;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.user.UserService;
import com.pchome.akbadm.struts2.BaseAction;

public class PwdAction extends BaseAction {

	private UserService userService;

	private String message = "";

	private String userId;

	//元件參數(新增及修改頁面用)
	private String paramPassword;
	private String paramRePassword;

	public String execute() throws Exception {
		Object sessionObj = request.getSession().getAttribute(SessionConstants.SESSION_USER_ID);
		if (sessionObj!=null) {
			userId = (String) sessionObj;
		} else {
			return LOGIN;
		}
		log.info(">>> userId = " + userId);

		return SUCCESS;
	}

	public String doUpdatePwd() throws Exception {

		if (StringUtils.isEmpty(paramPassword)) {
			message = "請輸入密碼！";
			return INPUT;
		} else {
			paramPassword = paramPassword.trim();
			if (paramPassword.length() > 20) {
				message = "密碼不可超過 20 字元！";
				return INPUT;
			}
		}

		if (StringUtils.isEmpty(paramRePassword)) {
			message = "請輸入確認密碼！";
			return INPUT;
		} else {
			if (!paramPassword.equals(paramRePassword)) {
				message = "確認密碼輸入錯誤，請再試一次！";
				return INPUT;
			}
		}

		AdmUser user = userService.getUserById(userId);

		user.setUserPassword(paramPassword);
		user.setUpdateDate(new Date());

		userService.updateUser(user);

		message = "修改成功！";

		return SUCCESS;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getParamPassword() {
		return paramPassword;
	}

	public void setParamPassword(String paramPassword) {
		this.paramPassword = paramPassword;
	}

	public String getParamRePassword() {
		return paramRePassword;
	}

	public void setParamRePassword(String paramRePassword) {
		this.paramRePassword = paramRePassword;
	}
}
