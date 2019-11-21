package com.pchome.akbadm.struts2.action.check;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdCustomerReport;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdCustomerReportService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class LifeCheckAction extends BaseCookieAction {
    private static final long serialVersionUID = -7378516901249600180L;

    private IPfbxAdCustomerReportService pfbxAdCustomerReportService;

    private InputStream inputStream = new ByteArrayInputStream("err".getBytes());

    @Override
    public String execute() {
        try {
            List<PfbxAdCustomerReport> list = pfbxAdCustomerReportService.selectOneByUpdateDate();
            if (list == null) {
                return SUCCESS;
            }
            Calendar calendar = Calendar.getInstance();
            for (PfbxAdCustomerReport pfbxAdCustomerReport: list) {
                if (calendar.get(Calendar.HOUR_OF_DAY) <= 2) {
                    inputStream = new ByteArrayInputStream("ok".getBytes());
                }
                calendar.add(Calendar.HOUR_OF_DAY, -3);
                if (pfbxAdCustomerReport.getUpdateDate().getTime() > calendar.getTimeInMillis()) {
                    inputStream = new ByteArrayInputStream("ok".getBytes());
                }
                break;
            }
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return SUCCESS;
    }

    public void setPfbxAdCustomerReportService(IPfbxAdCustomerReportService pfbxAdCustomerReportService) {
        this.pfbxAdCustomerReportService = pfbxAdCustomerReportService;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
