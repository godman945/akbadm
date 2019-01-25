package com.pchome.akbadm.struts2.action.check;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.pchome.akbadm.quartzs.CheckPvclkJob;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class CheckPvclkAction extends BaseCookieAction {
    private static final long serialVersionUID = -6636314080106649253L;

    private CheckPvclkJob checkPvclkJob;
    private InputStream result;

    @Override
    public String execute() {
        try {
            result = new ByteArrayInputStream((checkPvclkJob.process() ? "ok" : "err").getBytes("UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return SUCCESS;
    }

    public void setCheckPvclkJob(CheckPvclkJob checkPvclkJob) {
        this.checkPvclkJob = checkPvclkJob;
    }

    public InputStream getResult() {
        return result;
    }
}
