package com.pchome.akbadm.struts2.action.check;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.service.catalog.IPfpCatalogProdEcService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.catalog.EnumCatalogDeleteStatus;
import com.pchome.enumerate.catalog.EnumCatalogProdEcCheckStatus;
import com.pchome.enumerate.catalog.EnumCatalogProdEcStatus;
import com.pchome.enumerate.catalog.EnumCatalogUploadStatus;

@Transactional
public class CatalogProdEcAction extends BaseAction {
	private IPfpCatalogProdEcService pfpCatalogProdEcService;

    private String pfpCustomerInfoId = "";
    private String catalogSeq = null;
    private String[] catalogProdIds = null;
    private String allRejectReason = null;

    private List<Map<String, Object>> pfpCustomerInfoList = new ArrayList<>();
    private List<Map<String, Object>> catalogList = new ArrayList<>();
    private List<Map<String, Object>> catalogProdEcList = new ArrayList<>();

    private String message;
    private int pageNo = 1;
    private int pageSize = 10;
    private int pageCount = 0;
    private int totalCount = 0;

    @Override
    public String execute() throws Exception {
        pfpCustomerInfoList = pfpCatalogProdEcService.selectPfpCustomerInfoByStatus(EnumCatalogUploadStatus.COMPLETE.getStatus(), EnumCatalogDeleteStatus.UNDELETE.getStatus(), EnumCatalogProdEcStatus.OPEN.getStatus(), EnumCatalogProdEcCheckStatus.VERIFYING.getStatus());
        catalogList = pfpCatalogProdEcService.selectPfpCatalogByStatus(pfpCustomerInfoId, EnumCatalogUploadStatus.COMPLETE.getStatus(), EnumCatalogDeleteStatus.UNDELETE.getStatus(), EnumCatalogProdEcStatus.OPEN.getStatus(), EnumCatalogProdEcCheckStatus.VERIFYING.getStatus());

        catalogProdEcList = pfpCatalogProdEcService.selectPfpCatalogProdEc(pfpCustomerInfoId, catalogSeq, EnumCatalogUploadStatus.COMPLETE.getStatus(), EnumCatalogDeleteStatus.UNDELETE.getStatus(), EnumCatalogProdEcStatus.OPEN.getStatus(), EnumCatalogProdEcCheckStatus.VERIFYING.getStatus(), pageNo, pageSize);

        totalCount = pfpCatalogProdEcService.selectCount(pfpCustomerInfoId, catalogSeq, EnumCatalogUploadStatus.COMPLETE.getStatus(), EnumCatalogDeleteStatus.UNDELETE.getStatus(), EnumCatalogProdEcStatus.OPEN.getStatus(), EnumCatalogProdEcCheckStatus.VERIFYING.getStatus());
        pageCount = (int) Math.ceil(((float)totalCount / pageSize));

        if (catalogProdEcList.isEmpty()) {
            message = "查無資料！";
        }

        return SUCCESS;
    }

    public String doApprove() throws Exception {
        if (catalogProdIds == null || catalogProdIds.length == 0) {
            message = "請選擇要審核的項目！";
            return INPUT;
        }

        Date date = Calendar.getInstance().getTime();
        String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

        PfpCatalogProdEc pfpCatalogProdEc = null;
        for (String id: catalogProdIds) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            if (!StringUtils.isNumeric(id)) {
                continue;
            }

            pfpCatalogProdEc = pfpCatalogProdEcService.get(Integer.valueOf(id));
            if (pfpCatalogProdEc == null) {
                continue;
            }

            pfpCatalogProdEc.setEcCheckStatus(EnumCatalogProdEcCheckStatus.APPROVE.getStatus());
            pfpCatalogProdEc.setEcUserVerifyTime(date);
            pfpCatalogProdEc.setEcVerifyUser(userId);
            pfpCatalogProdEc.setUpdateDate(date);

            pfpCatalogProdEcService.update(pfpCatalogProdEc);
        }

        return SUCCESS;
    }

    public String doReject() throws Exception {
        if (catalogProdIds == null || catalogProdIds.length == 0) {
            message = "請選擇要審核的項目！";
            return INPUT;
        }

        if (StringUtils.isBlank(allRejectReason)) {
            message = "請填寫退件原因！";
            return INPUT;
        }

        Date date = Calendar.getInstance().getTime();
        String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

        PfpCatalogProdEc pfpCatalogProdEc = null;
        for (String id: catalogProdIds) {
            if (StringUtils.isBlank(id)) {
                continue;
            }
            if (!StringUtils.isNumeric(id)) {
                continue;
            }

            pfpCatalogProdEc = pfpCatalogProdEcService.get(Integer.valueOf(id));
            if (pfpCatalogProdEc == null) {
                continue;
            }

            pfpCatalogProdEc.setEcCheckStatus(EnumCatalogProdEcCheckStatus.REJECT.getStatus());
            pfpCatalogProdEc.setEcUserVerifyTime(date);
            pfpCatalogProdEc.setEcVerifyUser(userId);
            pfpCatalogProdEc.setEcVerifyRejectReason(allRejectReason);
            pfpCatalogProdEc.setUpdateDate(date);

            pfpCatalogProdEcService.update(pfpCatalogProdEc);
        }

        return SUCCESS;
    }

    public void setPfpCatalogProdEcService(IPfpCatalogProdEcService pfpCatalogProdEcService) {
        this.pfpCatalogProdEcService = pfpCatalogProdEcService;
    }

    public String getPfpCustomerInfoId() {
        return pfpCustomerInfoId;
    }

    public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
        this.pfpCustomerInfoId = pfpCustomerInfoId;
    }

    public String getCatalogSeq() {
        return catalogSeq;
    }

    public void setCatalogSeq(String catalogSeq) {
        this.catalogSeq = catalogSeq;
    }

    public void setCatalogProdIds(String[] catalogProdIds) {
        this.catalogProdIds = catalogProdIds;
    }

    public void setAllRejectReason(String allRejectReason) {
        this.allRejectReason = allRejectReason;
    }

    public List<Map<String, Object>> getPfpCustomerInfoList() {
        return pfpCustomerInfoList;
    }

    public List<Map<String, Object>> getCatalogList() {
        return catalogList;
    }

    public List<Map<String, Object>> getCatalogProdEcList() {
        return catalogProdEcList;
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
