package com.pchome.akbadm.struts2.action.logout;

import javax.servlet.http.Cookie;

import org.apache.struts2.ServletActionContext;

import com.pchome.akbadm.config.cookie.CookieConstants;
import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.struts2.BaseAction;

public class LogoutAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	public String execute() throws Exception {

		//刪除 cookie
		Cookie deleteCookie = new Cookie(CookieConstants.COOKIE_AKBADM, null);
		deleteCookie.setDomain(".pchome.com.tw");
		deleteCookie.setMaxAge(0);
		deleteCookie.setPath("/");
		response.addCookie(deleteCookie); 

		//刪除 session
		ServletActionContext.getContext().getSession().remove(SessionConstants.SESSION_USER_ID);
		ServletActionContext.getContext().getSession().remove(SessionConstants.SESSION_USER_PRIVILEGE);

		return SUCCESS;
	}
}
