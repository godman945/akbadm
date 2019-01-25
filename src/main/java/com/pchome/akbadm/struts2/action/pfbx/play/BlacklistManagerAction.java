package com.pchome.akbadm.struts2.action.pfbx.play;

import com.pchome.akbadm.db.pojo.PfbxBoard;
import com.pchome.akbadm.db.pojo.PfbxPermission;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.pfbx.board.PfbxBoardService;
import com.pchome.akbadm.db.service.pfbx.play.IPfbxPermissionService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.enumerate.pfbx.board.EnumPfbBoardLimit;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumPfbBoardType;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BlacklistManagerAction extends BaseCookieAction {

    private static final long serialVersionUID = 1L;
    private IPfbxPermissionService pfbxPermissionService;
    private IAdmAccesslogService accesslogService;

    private List<PfbxPermission> pfbxPermissionList;
    private PfbxBoardService pfbxBoardService;
    private String result;
    private String rejectReason;
    private String searchUrl;
    private String blacklistManagerJsonObj;

    // 查詢總筆數
    private double totalColume;
    // 頁數
    private double page;
    // 總頁數
    private double totalPage;
    // 每頁筆數
    private double pageSize;
    // 頁數類型
    private String pageType;

    public String excute() {
        return SUCCESS;
    }

    /**
     * 查詢網址
     */
    public String searchUrl() throws Exception {
        log.info("Search Url : " + searchUrl);
        List<PfbxPermission> pfbxPermissionList = pfbxPermissionService
                .findPfbxPermissionListByUrl(searchUrl);
        for (PfbxPermission pfbxPermission : pfbxPermissionList) {
            PfbxPermission pfbxPermissionObj = pfbxPermissionService
                    .get(pfbxPermission.getId());
            pfbxPermission.setPfbxCustomerInfo(pfbxPermissionObj
                    .getPfbxCustomerInfo());
            pfbxPermission.setCreatDate(pfbxPermissionObj.getCreatDate());
        }
        processPageViewColumn(pfbxPermissionList);
        return SUCCESS;
    }

    /**
     * 處理顯示頁數
     */
    public void processPageViewColumn(List<PfbxPermission> pfbxPermissionList) {
        double page = 0;
        double size = this.pageSize;
        totalPage = Math.ceil(pfbxPermissionList.size() / size);
        if (pageType.equals("pageUp")) {
            page = this.page - 1;
        } else if (pageType.equals("firstPage")) {
            page = 1;
        } else if (pageType.equals("lastPage")) {
            page = totalPage;
        } else {
            page = this.page + 1;
        }
        this.page = page;
        double total = page * size;
        List<PfbxPermission> pfbxPermissionListObj = new ArrayList<PfbxPermission>();
        for (int i = 0; i < pfbxPermissionList.size(); i++) {
            if (i < total && i >= (page - 1) * size) {
                pfbxPermissionListObj.add(pfbxPermissionList.get(i));
            }
        }
        totalColume = pfbxPermissionList.size();
        this.pfbxPermissionList = pfbxPermissionListObj;
    }

    /**
     * 封鎖
     */
    public String blacklistManagerReject() throws Exception {
        if (StringUtils.isBlank(rejectReason)) {
            result = "未填寫拒絕原因";
            return SUCCESS;
        }
        JSONObject rejectUrlObject = new JSONObject(
                blacklistManagerJsonObj.toString());
        JSONArray rejectUrlObjectResult = new JSONArray(rejectUrlObject.get(
                "rejectUrl").toString());

        StringBuffer tmpUrlSb = new StringBuffer();

        if (rejectUrlObjectResult.length() >= 1) {
            List<PfbxPermission> pfbxPermissionList = new ArrayList<PfbxPermission>();
            Date now = new Date();
            for (int i = 0; i < rejectUrlObjectResult.length(); i++) {
                PfbxPermission pfbxPermission = pfbxPermissionService
                        .get(Integer.parseInt(rejectUrlObjectResult.get(i)
                                .toString()));
                String customerInfoId = pfbxPermission
                        .getPfbxCustomerInfo().getCustomerInfoId();

                pfbxPermission.setStatus(2);
                pfbxPermission.setRemark(rejectReason);
                pfbxPermissionList.add(pfbxPermission);

                tmpUrlSb.append(pfbxPermission.getContent() +",");

                // 新增公告
                String msgContent ="您的廣告網址 <a href='" + pfbxPermission.getContent() +"' >" + pfbxPermission.getContent() + "</a> 已被封鎖："+rejectReason+"，故廣告已停止撥放!";
                PfbxBoard board = new PfbxBoard();
                board.setBoardType(EnumPfbBoardType.REMIND.getType());
//                board.setBoardContent(EnumBoardContent.BOARD_CONTENT_3
//                        .getContent());
                board.setBoardContent(msgContent);
                board.setPfbxCustomerInfoId(customerInfoId);
                board.setStartDate(now);
                board.setEndDate(DateTimeUtils.shiftMontth(now, EnumPfbBoardLimit.Limit.getValue()));
                board.setHasUrl("n");
                board.setUrlAddress(null);
                board.setDeleteId(EnumBoardContent.BOARD_CONTENT_3.getId());
                board.setCreateDate(now);

                pfbxBoardService.save(board);

                //紀錄AccessLog
                String accessLogMsg = "網址審核-->封鎖" + pfbxPermission.getContent();
                accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
                        EnumAccesslogAction.ACCOUNT_MODIFY,
                        accessLogMsg,
                        null,
                        null,
                        customerInfoId,
                        null,
                        request.getRemoteAddr(),
                        EnumAccesslogEmailStatus.NO);

            }
            pfbxPermissionService.savePfbxPermissionList(pfbxPermissionList);
        }
        return SUCCESS;

    }

    /**
     * 取消封鎖
     */
    public String blacklistManagerCancel() throws Exception {
        JSONObject cancelRejectUrlObject = new JSONObject(
                blacklistManagerJsonObj.toString());
        JSONArray cancelRejectUrlObjectResult = new JSONArray(
                cancelRejectUrlObject.get("cancelRejectUrl").toString());
        StringBuffer tmpUrlSb = new StringBuffer();

        if (cancelRejectUrlObjectResult.length() >= 1) {
            List<PfbxPermission> pfbxPermissionList = new ArrayList<PfbxPermission>();
            Date now = new Date();

            for (int i = 0; i < cancelRejectUrlObjectResult.length(); i++) {
                PfbxPermission pfbxPermission = pfbxPermissionService
                        .get(Integer.parseInt(cancelRejectUrlObjectResult
                                .get(i).toString()));
                String customerInfoId = pfbxPermission
                        .getPfbxCustomerInfo().getCustomerInfoId();

                pfbxPermission.setStatus(1);
                pfbxPermission.setRemark(rejectReason);
                pfbxPermissionList.add(pfbxPermission);
                tmpUrlSb.append(pfbxPermission.getContent() + ",");

//                // 移除公告
//                pfbxBoardService.deleteBoard(pfbxPermission
//                                .getPfbxCustomerInfo().getCustomerInfoId(),
//                        EnumBoardContent.BOARD_CONTENT_3.getId());

                // 新增公告
                String msgContent ="<a class='boardContentLink' href='/pfb/play.html' >您的廣告播放網址 " + pfbxPermission.getContent() + " 已重新開通，廣告可正常播放。</a>";
                PfbxBoard board = new PfbxBoard();
                board.setBoardType(EnumPfbBoardType.REMIND.getType());
                board.setBoardContent(msgContent);
                board.setPfbxCustomerInfoId(customerInfoId);
                board.setStartDate(now);
                board.setEndDate(DateTimeUtils.shiftMontth(now, EnumPfbBoardLimit.Limit.getValue()));
                board.setHasUrl("n");
                board.setUrlAddress(null);
                board.setDeleteId(EnumBoardContent.BOARD_CONTENT_3.getId());
                board.setCreateDate(now);

                pfbxBoardService.save(board);

                //紀錄AccessLog
                String accessLogMsg = "網址審核-->開通" + pfbxPermission.getContent();
                accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
                        EnumAccesslogAction.ACCOUNT_MODIFY,
                        accessLogMsg,
                        null,
                        null,
                        customerInfoId,
                        null,
                        request.getRemoteAddr(),
                        EnumAccesslogEmailStatus.NO);
            }
            pfbxPermissionService.savePfbxPermissionList(pfbxPermissionList);
        }
        return SUCCESS;
    }

    public IPfbxPermissionService getPfbxPermissionService() {
        return pfbxPermissionService;
    }

    public void setPfbxPermissionService(
            IPfbxPermissionService pfbxPermissionService) {
        this.pfbxPermissionService = pfbxPermissionService;
    }

    public List<PfbxPermission> getPfbxPermissionList() {
        return pfbxPermissionList;
    }

    public void setPfbxPermissionList(List<PfbxPermission> pfbxPermissionList) {
        this.pfbxPermissionList = pfbxPermissionList;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBlacklistManagerJsonObj() {
        return blacklistManagerJsonObj;
    }

    public void setBlacklistManagerJsonObj(String blacklistManagerJsonObj) {
        this.blacklistManagerJsonObj = blacklistManagerJsonObj;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public double getTotalColume() {
        return totalColume;
    }

    public void setTotalColume(double totalColume) {
        this.totalColume = totalColume;
    }

    public double getPage() {
        return page;
    }

    public void setPage(double page) {
        this.page = page;
    }

    public double getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(double totalPage) {
        this.totalPage = totalPage;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public PfbxBoardService getPfbxBoardService() {
        return pfbxBoardService;
    }

    public void setPfbxBoardService(PfbxBoardService pfbxBoardService) {
        this.pfbxBoardService = pfbxBoardService;
    }

    public void setAccesslogService(IAdmAccesslogService accesslogService) {
        this.accesslogService = accesslogService;
    }

    public static void main(String args[]) {
        // for(EnumAdStatus adActionStatus:EnumAdStatus.values()){
        // System.out.println(adActionStatus);
        // System.out.println(adActionStatus.getStatusId());
        // System.out.println(adActionStatus.getStatusDesc());
        // }

        List<String> a = new ArrayList<String>();
        for (int i = 0; i <= 360; i++) {
            a.add(String.valueOf(i));
            // System.out.println(i);
        }

        double page = 2;
        double size = 20;
        double total = page * size;
        double pageSize = Math.ceil(a.size() / size);
        for (int i = 0; i < a.size() - 1; i++) {
            if (i < total && i >= (page - 1) * size) {
                System.out.println(a.get(i));
            }
        }

    }
}
