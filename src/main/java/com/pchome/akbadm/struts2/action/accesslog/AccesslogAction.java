package com.pchome.akbadm.struts2.action.accesslog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.accesslog.EnumSearchType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class AccesslogAction extends BaseCookieAction{
    private static final long serialVersionUID = 4082095470867959149L;

    private EnumAccesslogChannel[] enumAccesslogChannels = EnumAccesslogChannel.values();
    private EnumAccesslogAction[] enumAccesslogActions = EnumAccesslogAction.values();
    private EnumAccesslogEmailStatus[] enumAccesslogEmailStatuses = EnumAccesslogEmailStatus.values();

	private IAdmAccesslogService admAccesslogService;

	private String channel;
	private String motion;
	private String memberId;
	private String orderId;
	private String customerInfoId;
	private String userId;
	private String clientIp;
	private String start;
	private String end;
    private String message;
    private int pageNo = 1;
	private int pageSize = 20;
	private int totalCount = 0;
	private int totalPage = 0;

	private List<AdmAccesslog> accesslogs;

	public String accountAccesslogAction() throws Exception {

		accesslogs = admAccesslogService.findAdmAccesslog(customerInfoId, EnumSearchType.LOG_CUSTOMERINFO_ID);

		return SUCCESS;
	}

	public String accountUserAccesslogAction() throws Exception {

		accesslogs = admAccesslogService.findAdmAccesslog(userId, EnumSearchType.LOG_USER_ID);

		return SUCCESS;
	}

	public String admAccesslogAction() throws Exception {
	    if (StringUtils.isBlank(channel) &&
	            StringUtils.isBlank(motion) &&
	            StringUtils.isBlank(memberId) &&
	            StringUtils.isBlank(orderId) &&
	            StringUtils.isBlank(customerInfoId) &&
	            StringUtils.isBlank(userId) &&
	            StringUtils.isBlank(clientIp) &&
                StringUtils.isBlank(message) &&
	            StringUtils.isBlank(start) &&
	            StringUtils.isBlank(end)) {
	        return SUCCESS;
	    }

//	    message = StringUtils.isNotBlank(message) ? new String(message.trim().getBytes("ISO-8859-1"), "UTF-8") : message;

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date startDate = StringUtils.isNotBlank(start) ? sdf.parse(start + " 00:00:00") : null;
	    Date endDate = StringUtils.isNotBlank(end) ? sdf.parse(end + " 23:59:59") : null;

	    totalCount = admAccesslogService.selectAdmAccesslogCount(channel, motion, memberId, orderId, customerInfoId, userId, clientIp, message, startDate, endDate);
	    if (totalCount > 0) {
	        totalPage = (int) Math.ceil(totalCount / (double) pageSize);
	    }
	    accesslogs = admAccesslogService.selectAdmAccesslog(channel, motion, memberId, orderId, customerInfoId, userId, clientIp, message, startDate, endDate, pageNo, pageSize);

	    for(AdmAccesslog admAccesslog:accesslogs){
	    	String message = admAccesslog.getMessage();
	    	message = message.replaceAll("<", "&lt;");
	    	message = message.replaceAll(">", "&gt;");
	    	admAccesslog.setMessage(message);
	    }
	    	
	    
	    return SUCCESS;
	}

    public EnumAccesslogChannel[] getEnumAccesslogChannels() {
        return enumAccesslogChannels;
    }

    public EnumAccesslogAction[] getEnumAccesslogActions() {
        return enumAccesslogActions;
    }

    public EnumAccesslogEmailStatus[] getEnumAccesslogEmailStatuses() {
        return enumAccesslogEmailStatuses;
    }

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerInfoId() {
        return customerInfoId;
    }

    public void setCustomerInfoId(String customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        if (StringUtils.isNotBlank(pageNo) && StringUtils.isNumeric(pageNo)) {
            this.pageNo = Integer.valueOf(pageNo);
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        if (StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)) {
            this.pageSize = Integer.valueOf(pageSize);
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<AdmAccesslog> getAccesslogs() {
        return accesslogs;
    }
}