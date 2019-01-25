package com.pchome.akbadm.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class IpInterceptor extends AbstractInterceptor{
	
	private Log log = LogFactory.getLog(getClass().getName());

	private String[] adminIp;

    //private static final String PAGE_NOT_FOUND = "pageNotFound";

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result = "login";
		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = request.getRemoteAddr();
		log.info(">>>login  ip = " + ip);
		
		for(String adm:adminIp){
			if(adm.equals(ip)){	
				log.info("login success ip: "+ip );
				result = invocation.invoke();
			}
		}
		//log.info("reject ip: "+ip );
		
		return result;
	}

	public void setAdminIp(String[] adminIp) {
		this.adminIp = adminIp;
	}
}
