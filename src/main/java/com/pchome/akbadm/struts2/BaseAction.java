package com.pchome.akbadm.struts2;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements ServletContextAware, ServletRequestAware, ServletResponseAware, SessionAware {
    private static final long serialVersionUID = -8920422948462490447L;

    protected Logger log = LogManager.getRootLogger();

    protected ServletContext servletContext;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Map<String, Object> session;
    protected Map<String, String> cookiesMap;

	@Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    protected String getHost(String host) {
        if (StringUtils.isBlank(host)) {
            return host;
        }

        try {
            host = new String(host.trim().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(host, e);
        }
        host = host.replaceAll("https://", "");
        host = host.replaceAll("http://", "");
        host = host.replaceAll("/.*$", "");
        host = host.replaceAll(":.*$", "");
        host = host.toLowerCase().trim();

        return host;
    }

    public void printFieldError() {
        log.info("====field error start====");
        for (String str: getFieldErrors().keySet()) {
            log.info(str + " = " + getFieldErrors().get(str));
        }
        log.info("====field error end====");
    }

    public void printParameter() {
        log.info("====parameter start====");
        for (Object str: request.getParameterMap().keySet()) {
            log.info(str + " = " + request.getParameter((String)str));
        }
        log.info("====parameter end====");
    }
}