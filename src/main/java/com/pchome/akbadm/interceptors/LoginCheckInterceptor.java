package com.pchome.akbadm.interceptors;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import com.pchome.akbadm.config.cookie.CookieConstants;
import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.dao.menu.MenuVO;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.akbadm.db.service.user.UserService;
import com.pchome.akbadm.utils.CookieUtil;
import com.pchome.akbadm.utils.EncodeUtil;

public class LoginCheckInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getRootLogger();

    private static final String REDIRECT_LOGIN = "login";

    private UserService userService;
    private MenuService menuService;

    public String intercept(ActionInvocation invocation) throws Exception {

        HttpServletRequest request = (HttpServletRequest) invocation
                .getInvocationContext().get(org.apache.struts2.StrutsStatics.HTTP_REQUEST);

        String encodedCookie = CookieUtil.getCookie(request, CookieConstants.COOKIE_AKBADM, CookieConstants.COOKIE_USING_CODE);

        //log.info(" encodedCookie: "+encodedCookie);
        
        if (StringUtils.isEmpty(encodedCookie)) {
        	return REDIRECT_LOGIN;
        }

        String decodeCookie = EncodeUtil.getInstance().decryptAES(encodedCookie, CookieConstants.COOKIE_SECRET_KEY);
        //log.info(">>> decodeCookie = " + decodeCookie);

    	//hard code
    	//String decodeCookie = "test@gmail.com&&&9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30";

    	String[] tmp = decodeCookie.split(CookieConstants.COOKIE_SEPARATOR);

        String email = tmp[0];
        //log.info(">>> email = " + email);
        String strPrivilege = tmp[1];
        //log.info(">>> strPrivilege = " + strPrivilege);

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(strPrivilege)) {
        	return REDIRECT_LOGIN;
        }

        AdmUser user = userService.getUserById(email);

		if (user==null) {
			return REDIRECT_LOGIN;
		}

		String[] menuIdArray = strPrivilege.split(",");
		List<String> menuIdList = new ArrayList<String>();
		for (int i=0; i<menuIdArray.length; i++) {
			menuIdList.add(menuIdArray[i]);
		}

		List<MenuVO> allMenuList = menuService.getSortMenu(menuIdList);

		List<MenuVO> newMenuList = this.filterMenu(allMenuList);
		
		ServletActionContext.getContext().getSession().put(SessionConstants.SESSION_USER_ID, email);
		ServletActionContext.getContext().getSession().put(SessionConstants.SESSION_USER_PRIVILEGE, newMenuList);

//		log.info("...strPrivilege=" + strPrivilege);
//		log.info("...menuIdArray=" + menuIdArray);
//		log.info("...menuIdList=" + menuIdList);
//		for(MenuVO menu : newMenuList)
//		{
//			log.info("...menu id=" + menu.getMenuId());
//			log.info("...menu=" + menu.getDisplayName());
//			List<MenuVO> childMenuList = menu.getChildrenList();
//			for(MenuVO ch : childMenuList)
//			{
//				log.info("...chi id=" + ch.getMenuId());
//				log.info("...chi name=" + ch.getDisplayName());
//				log.info("...chi act=" + ch.getAction());
//				log.info("...chi flag=" + ch.getCheckedFlag());
//			}
//		}
		
		return invocation.invoke();
    }

    /**
     * 濾掉沒有子項目的 menu
     */
    private List<MenuVO> filterMenu(List<MenuVO> parentMenuList) {
    	List<MenuVO> newMenuList = new ArrayList<MenuVO>();
    	for (int i=0; i<parentMenuList.size(); i++) {
    		MenuVO parentMenu = parentMenuList.get(i);
    		List<MenuVO> childrenMenuList = parentMenu.getChildrenList();
    		boolean haveChildFlag = false;
    		for (int k=0; k<childrenMenuList.size(); k++) {
    			MenuVO childMenu = childrenMenuList.get(k);
    			if (StringUtils.isNotEmpty(childMenu.getCheckedFlag()) && childMenu.getCheckedFlag().equals("checked")) {
    				haveChildFlag = true;
    				break;
    			}
    		}
    		if (haveChildFlag) {
    			newMenuList.add(parentMenu);
    		}
    	}
    	return newMenuList;
    }

    public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}
}
