package com.pchome.akbadm.struts2.action.login;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.cookie.CookieConstants;
import com.pchome.akbadm.config.user.UserConstants;
import com.pchome.akbadm.db.dao.privilege.PrivilegeModelVO;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.privilege.PrivilegeModelService;
import com.pchome.akbadm.db.service.user.UserService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.CookieUtil;
import com.pchome.akbadm.utils.EncodeUtil;

public class LoginAction extends BaseAction {

	private static final long serialVersionUID = 3144207523708239249L;

	private String email;
	private String password;

	private String message = "";

	private UserService userService;
	private PrivilegeModelService privilegeModelService;

	public String execute() throws Exception {
		return SUCCESS;
	}

	public String loginCheck() throws Exception {

		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			message = "帳號密碼不能為空白！";
			return INPUT;
		} else{
			email = email.trim();
			password = password.trim();
		}

		AdmUser user = userService.getUserById(email);
		if (user == null) {
			message = "帳號密碼錯誤！";
			return INPUT;
		} else {
			if (!password.equals(user.getUserPassword())) {
				message = "帳號密碼錯誤！";
				return INPUT;
			}
		}

		if (user.getStatus().equals(UserConstants.USER_STATUS_DELETED)) {
			message = "此帳號已被刪除！";
			return INPUT;
		}

		if (user.getStatus().equals(UserConstants.USER_STATUS_INACTIVE)) {
			message = "此帳號已被停權！";
			return INPUT;
		}

		String modelId = user.getModelId();
		PrivilegeModelVO privilegeModelVO = privilegeModelService.getPrivilegeModelById(modelId);
		List<String> dbMenuIdList = privilegeModelVO.getDbMenuIdList();
		String strPrivilege = "";
		for (int i=0; i<dbMenuIdList.size(); i++) {
			strPrivilege += dbMenuIdList.get(i) + ",";
		}
        strPrivilege = strPrivilege.substring(0, strPrivilege.length()-1);

		//登入成功 -> 寫入 cookie
		String cookieContent = email + CookieConstants.COOKIE_SEPARATOR + strPrivilege;
		//log.info(">>> cookieContent: " + cookieContent);
		String encodeCookie = EncodeUtil.getInstance().encryptAES(cookieContent, CookieConstants.COOKIE_SECRET_KEY);

		//log.info(">>> write cookie: " + CookieConstants.COOKIE_BILLINGADM);
		CookieUtil.writeCookie(this.getResponse(), CookieConstants.COOKIE_AKBADM, encodeCookie,
				CookieConstants.COOKIE_DOMAIN, CookieConstants.COOKIE_MAX_AGE, null);

/*
		//更新最後登入時間及IP
		MemberProfile memberProfile = memberService.findMemberProfileByEmail(email);
		memberProfile.setLastLoginIp(request.getRemoteAddr());
		memberProfile.setLastLoginTime(new Date());
		memberService.updateMemberProfile(memberProfile);
*/

		return SUCCESS;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setPrivilegeModelService(PrivilegeModelService privilegeModelService) {
		this.privilegeModelService = privilegeModelService;
	}
}
