package com.pchome.servlet;

import javax.servlet.ServletException;


import freemarker.ext.servlet.FreemarkerServlet;

public class MemberFreemarkerServlet extends FreemarkerServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        getConfiguration().setTemplateExceptionHandler(new MemberFreemarkerTemplateHandler());
    }
}