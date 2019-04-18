package com.pchome.servlet;

import java.io.Writer;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

//import com.pchome.member.MemberConstants;
//import com.pchome.member.utils.email.EmailUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MemberFreemarkerTemplateHandler implements TemplateExceptionHandler {
    private static final String subject = "PCHome廣告管理 Exception";
    private static final String to = "johnwei johnwei@staff.pchome.com.tw";
    private static final String from = "PCHome廣告管理 member@msx.pchome.com.tw";

    private static final Logger log = LogManager.getRootLogger();

    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        String temp = ExceptionUtils.getFullStackTrace(te).replaceAll("\n", "<br/>");
        try {
            //EmailUtil.sendHtmlMail(temp, subject, to, from, null, null, MemberConstants.ENCODE);
        } catch (Exception e) {
            log.info("freemarker exception could not be send : " + e.getMessage());
        }
    }
}