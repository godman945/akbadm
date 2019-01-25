package com.pchome.akbadm.db.service.accesslog;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.accesslog.EnumSearchType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public interface IAdmAccesslogService extends IBaseService<AdmAccesslog, Integer> {

	public Integer addAdmAccesslog(EnumAccesslogChannel channel, EnumAccesslogAction action, String message, String memberId,
								String orderId, String customerInfoId, String userId, String clientIp,
								EnumAccesslogEmailStatus mailSend);

	public List<AdmAccesslog> findAdmAccesslog(String id, EnumSearchType type) throws Exception;

	public int selectAdmAccesslogCount(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate);

	public List<AdmAccesslog> selectAdmAccesslog(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate, int pageNo, int pageSize);
	
	public List<AdmAccesslog> findSendMail();
}
