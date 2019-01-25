package com.pchome.akbadm.struts2.action.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class CostAction extends BaseCookieAction {
    private IPfpAdActionService pfpAdActionService;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpCustomerInfoService pfpCustomerInfoService;

    private String actionId;
    private String customerInfoId;

    private InputStream inputStream = new ByteArrayInputStream("".getBytes());

    @Override
    public String execute() throws Exception {
        JSONObject jsonObject = new JSONObject();

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date pvclkDate = calendar.getTime();

            jsonObject.put("action", getActionObject(pvclkDate));
            jsonObject.put("customerInfo", getCustomerObject(pvclkDate));

            inputStream = new ByteArrayInputStream(jsonObject.toString().getBytes());
        }
        catch (Exception e) {
            log.error(actionId + " " + customerInfoId, e);
        }

        return SUCCESS;
    }

    private JSONObject getActionObject(Date pvclkDate) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isBlank(actionId)) {
            return jsonObject;
        }

        PfpAdAction pfpAdAction = pfpAdActionService.get(actionId);
        if (pfpAdAction == null) {
            return jsonObject;
        }

        float actionCost = pfpAdPvclkService.actionCost(pfpAdAction.getAdActionSeq(), pvclkDate);

        jsonObject.put("actionId", actionId);
        jsonObject.put("actionCost", actionCost);
        jsonObject.put("actionMax", pfpAdAction.getAdActionMax());

        return jsonObject;
    }

    private JSONObject getCustomerObject(Date pvclkDate) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        if (StringUtils.isBlank(customerInfoId)) {
            return jsonObject;
        }

        PfpCustomerInfo pfpCustomerInfo = pfpCustomerInfoService.get(customerInfoId);
        if (pfpCustomerInfo == null) {
            return jsonObject;
        }

        float customerCost = pfpAdPvclkService.customerCost(customerInfoId, pvclkDate);

        jsonObject.put("customerInfoId", customerInfoId);
        jsonObject.put("customerCost", customerCost);
        jsonObject.put("customerRemain", pfpCustomerInfo.getLaterRemain());

        return jsonObject;
    }

    public void setPfpAdActionService(IPfpAdActionService pfpAdActionService) {
        this.pfpAdActionService = pfpAdActionService;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public void setCustomerInfoId(String customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
