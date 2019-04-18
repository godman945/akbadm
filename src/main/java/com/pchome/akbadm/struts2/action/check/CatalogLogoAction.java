package com.pchome.akbadm.struts2.action.check;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCatalogLogo;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.catalog.IPfpCatalogLogoService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.catalog.EnumCatalogLogoStatus;

@Transactional
public class CatalogLogoAction extends BaseAction {
    private static final long serialVersionUID = 1768923753947876208L;

    private IPfpCatalogLogoService pfpCatalogLogoService;
    private IPfpCustomerInfoService pfpCustomerInfoService;

    private String pfpCustomerInfoId = null;
    private String[] catalogLogoSeqs = null;
    private String allRejectReason = null;

	private List<Map<String, Object>> pfpCustomerInfoList = new ArrayList<>();
	private List<Map<String, Object>> catalogLogoList = new ArrayList<>();

	private String message;
    private int pageNo = 1;
    private int pageSize = 10;
    private int pageCount = 0;
    private int totalCount = 0;

    @Override
    public String execute() throws Exception {
        pfpCustomerInfoList = pfpCatalogLogoService.selectPfpCustomerInfoByStatus(EnumCatalogLogoStatus.VERIFYING.getStatus());

        catalogLogoList = pfpCatalogLogoService.selectCatalogLogo(pfpCustomerInfoId, EnumCatalogLogoStatus.VERIFYING.getStatus(), pageNo, pageSize);

        PfpCustomerInfo pfpCustomerInfo = null;
        PfdCustomerInfo pfdCustomerInfo = null;
        Set<PfdUserAdAccountRef> pfdUserAdAccountRefSet = null;
        for (Map<String, Object> map: catalogLogoList) {
            pfpCustomerInfo = pfpCustomerInfoService.get((String) map.get("pfp_customer_info_id"));
            if (pfpCustomerInfo != null) {
                pfdUserAdAccountRefSet = pfpCustomerInfo.getPfdUserAdAccountRefs();
                for (PfdUserAdAccountRef pfdUserAdAccountRef : pfdUserAdAccountRefSet) {
                    pfdCustomerInfo = pfdUserAdAccountRef.getPfdCustomerInfo();
                    map.put("pfd_customer_info_id", pfdCustomerInfo.getCustomerInfoId());
                    map.put("pfd_company_name", pfdCustomerInfo.getCompanyName());

                    break;
                }
            }
        }

        totalCount = pfpCatalogLogoService.selectCount(pfpCustomerInfoId, EnumCatalogLogoStatus.VERIFY.getStatus());
        pageCount = (int) Math.ceil(((float)totalCount / pageSize));

        if (catalogLogoList.isEmpty()) {
            message = "查無資料！";
        }

        return SUCCESS;
    }

    public String doApprove() throws Exception {
        if (catalogLogoSeqs == null || catalogLogoSeqs.length == 0) {
            message = "請選擇要審核的項目！";
            return INPUT;
        }

        Date date = Calendar.getInstance().getTime();
        String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

        PfpCatalogLogo pfpCatalogLogo = null;
        for (String seq: catalogLogoSeqs) {
            pfpCatalogLogo = pfpCatalogLogoService.get(seq);

            if (pfpCatalogLogo == null) {
                continue;
            }

            pfpCatalogLogo.setStatus(EnumCatalogLogoStatus.APPROVE.getStatus());
            pfpCatalogLogo.setLogoUserVerifyTime(date);
            pfpCatalogLogo.setLogoVerifyUser(userId);
            pfpCatalogLogo.setUpdateDate(date);

            pfpCatalogLogoService.update(pfpCatalogLogo);
        }

        return SUCCESS;
    }

    public String doReject() throws Exception {
        if (catalogLogoSeqs == null || catalogLogoSeqs.length == 0) {
            message = "請選擇要審核的項目！";
            return INPUT;
        }

        if (StringUtils.isBlank(allRejectReason)) {
            message = "請填寫退件原因！";
            return INPUT;
        }

        Date date = Calendar.getInstance().getTime();
        String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

        PfpCatalogLogo pfpCatalogLogo = null;
        for (String seq: catalogLogoSeqs) {
            pfpCatalogLogo = pfpCatalogLogoService.get(seq);

            if (pfpCatalogLogo == null) {
                continue;
            }

            pfpCatalogLogo.setStatus(EnumCatalogLogoStatus.REJECT.getStatus());
            pfpCatalogLogo.setLogoUserVerifyTime(date);
            pfpCatalogLogo.setLogoVerifyUser(userId);
            pfpCatalogLogo.setLogoVerifyRejectReason(allRejectReason);
            pfpCatalogLogo.setUpdateDate(date);

            pfpCatalogLogoService.update(pfpCatalogLogo);
        }

        return SUCCESS;
    }

    public void setPfpCatalogLogoService(IPfpCatalogLogoService pfpCatalogLogoService) {
        this.pfpCatalogLogoService = pfpCatalogLogoService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public String getPfpCustomerInfoId() {
        return pfpCustomerInfoId;
    }

    public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
        this.pfpCustomerInfoId = pfpCustomerInfoId;
    }

    public void setCatalogLogoSeqs(String[] catalogLogoSeqs) {
        this.catalogLogoSeqs = catalogLogoSeqs;
    }

    public void setAllRejectReason(String allRejectReason) {
        this.allRejectReason = allRejectReason;
    }

    public List<Map<String, Object>> getPfpCustomerInfoList() {
        return pfpCustomerInfoList;
    }

    public List<Map<String, Object>> getCatalogLogoList() {
        return catalogLogoList;
    }

    public String getMessage() {
        return message;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
