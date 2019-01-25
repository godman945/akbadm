package com.pchome.akbadm.struts2.action.account;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.pojo.PfpUserMemberRef;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.user.IPfpUserMemberRefService;
import com.pchome.akbadm.db.service.user.IPfpUserService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.user.EnumUserStatus;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.depot.utils.MemberAPIUtils;

public class AccountAction extends BaseCookieAction {

    private static final long serialVersionUID = 1L;
    private List<PfdCustomerInfo> accountList = new ArrayList<PfdCustomerInfo>();
    private IAdmAccesslogService admAccesslogService;
    private IPfpCustomerInfoService pfpCustomerInfoService;
    private IPfpUserService pfpUserService;
    private IPfpUserMemberRefService pfpUserMemberRefService;
    private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
    private IPfdAccountService pfdAccountService;
    
    private String customerInfoId;                    // 帳戶編號
    private PfpCustomerInfo customerInfo;            // 帳戶資訊
    private EnumAccountStatus[] accountStatus;        // 帳戶狀態
    private String status;                            // 帳戶修改狀態
    private String message;                            // 帳戶修改原因
    private List<PfpUser> users;                    // 帳戶下所有使用者
    private String userId;                            // 帳戶下使用者id
    private PfpUser user;                            // 帳戶使用者
    private Map<String, String> memberMap;            // 使用者資料
    private EnumUserStatus[] userStatus;            // 使用者狀態
    private EnumPfdAccountPayType[] enumPfdAccountPayType = EnumPfdAccountPayType.values();    // 付款狀態
    private PfdUserAdAccountRef pfdUserAdAccountRef;

    private String black; //黑名單 y/n
    private String blackReason; //黑名單原因
    private String blackTime; //黑名單時間

    public String execute() throws Exception {
        accountStatus = EnumAccountStatus.values();
        this.accountList = pfdAccountService.loadAll();
        return SUCCESS;
    }

    public String accountModifyAction() throws Exception {

        customerInfo = pfpCustomerInfoService.getCustomerInfo(customerInfoId);
        accountStatus = EnumAccountStatus.values();
        users = pfpUserService.getCustomerInfoUsers(customerInfoId);
        List<PfdUserAdAccountRef> pfdUserAdAccountRefs = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(customerInfoId);

        if (!pfdUserAdAccountRefs.isEmpty()) {
            pfdUserAdAccountRef = pfdUserAdAccountRefs.get(0);
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.black = customerInfo.getBlack();
        if (StringUtils.isNotBlank(customerInfo.getBlackReason())) {
            this.blackReason = customerInfo.getBlackReason();
        }
        if (customerInfo.getBlackTime() != null) {
            this.blackTime = dateFormat.format(customerInfo.getBlackTime());
        }

        return SUCCESS;
    }

    public String accountUpdateAction() throws Exception {

        customerInfo = pfpCustomerInfoService.getCustomerInfo(customerInfoId);

        customerInfo.setStatus(status);

        if (StringUtils.isNotBlank(black) && black.equals("y") &&
                StringUtils.isNotBlank(blackReason)) { //加入黑名單
            customerInfo.setBlack("y");
            customerInfo.setBlackReason(blackReason);
            customerInfo.setBlackTime(new Date());
        } else { //移除黑名單
            customerInfo.setBlack("n");
            customerInfo.setBlackReason(null);
            customerInfo.setBlackTime(null);
        }
        customerInfo.setUpdateDate(new Date());
        pfpCustomerInfoService.saveOrUpdate(customerInfo);

        // 帳戶異動記錄
        message = EnumAccesslogAction.ACCOUNT_MODIFY.getMessage() + "：" + message;
        admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.ACCOUNT_MODIFY, message,
                super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, customerInfoId,
                null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);

        return SUCCESS;
    }

    public String accountUserModifyAction() throws Exception {
        log.info("userId = " + userId);

        PfpUserMemberRef userMemberRef = pfpUserMemberRefService.getUserMemberRef(userId);

        user = userMemberRef.getPfpUser();

        log.info("memberId = " + userMemberRef.getId().getMemberId());
        memberMap = MemberAPIUtils.getInstance().findMemberDetail(userMemberRef.getId().getMemberId());
        log.info("memberMap.values() = " + memberMap.values());
        userStatus = EnumUserStatus.values();

        return SUCCESS;
    }

    public String accountUserUpdateAction() throws Exception {

        user = pfpUserService.getCustomerInfoUser(userId);
        user.setStatus(status);
        user.setUpdateDate(new Date());
        pfpUserService.saveOrUpdate(user);

        // 帳號異動記錄
        message = EnumAccesslogAction.USER_MODIFY.getMessage() + "：" + message;
        admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.USER_MODIFY, message,
                super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, customerInfoId,
                user.getUserId(), request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);

        return SUCCESS;
    }

//	public String accountTransAction() throws Exception{
//		
//		transEndDate = DateValueUtil.getInstance().dateToString(new Date());
//		transStartDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);
//		
//		return SUCCESS;
//	}


    public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
        this.admAccesslogService = admAccesslogService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public void setPfpUserService(IPfpUserService pfpUserService) {
        this.pfpUserService = pfpUserService;
    }

    public void setPfpUserMemberRefService(IPfpUserMemberRefService pfpUserMemberRefService) {
        this.pfpUserMemberRefService = pfpUserMemberRefService;
    }

    public void setPfdUserAdAccountRefService(
            IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
        this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
    }

    public void setCustomerInfoId(String customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public String getCustomerInfoId() {
        return customerInfoId;
    }

    public PfpCustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public EnumAccountStatus[] getAccountStatus() {
        return accountStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PfpUser> getUsers() {
        return users;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PfpUser getUser() {
        return user;
    }

    public Map<String, String> getMemberMap() {
        return memberMap;
    }

    public EnumUserStatus[] getUserStatus() {
        return userStatus;
    }

    public EnumPfdAccountPayType[] getEnumPfdAccountPayType() {
        return enumPfdAccountPayType;
    }

    public PfdUserAdAccountRef getPfdUserAdAccountRef() {
        return pfdUserAdAccountRef;
    }

    public String getBlack() {
        return black;
    }

    public void setBlack(String black) {
        this.black = black;
    }

    public String getBlackReason() {
        return blackReason;
    }

    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason;
    }

    public String getBlackTime() {
        return blackTime;
    }

    public void setBlackTime(String blackTime) {
        this.blackTime = blackTime;
    }

	public List<PfdCustomerInfo> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<PfdCustomerInfo> accountList) {
		this.accountList = accountList;
	}

	public IPfdAccountService getPfdAccountService() {
		return pfdAccountService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}
    
    
}
