package com.pchome.akbadm.db.service.accesslog;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.accesslog.IAdmAccesslogDAO;
import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.accesslog.EnumSearchType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class AdmAccesslogService extends BaseService<AdmAccesslog, Integer> implements IAdmAccesslogService {

	/**
	 * channel:頻道名稱
	 * action:頻道動作
	 * message:訊息
	 * memberId:會員帳號
	 * orderId:訂單編號
	 * customerInfoId:帳戶編號
	 * userId:使用者編號
	 * clientIp:連線 IP
	 * mailSend:信件通知
	 * */
	public Integer addAdmAccesslog(EnumAccesslogChannel channel, EnumAccesslogAction action, String message, String memberId,
			String orderId, String customerInfoId, String userId, String clientIp,
			EnumAccesslogEmailStatus mailSend) {

		AdmAccesslog log = new AdmAccesslog();

		log.setChannel(channel.getChannel());
		log.setAction(action.getAction());
		log.setMessage(message);
		log.setMemberId(memberId);
		log.setOrderId(orderId);
		log.setCustomerInfoId(customerInfoId);
		log.setUserId(userId);
		log.setClientIp(clientIp);
		log.setMailSend(mailSend.getStatus());
		log.setCreateDate(new Date());
		((IAdmAccesslogDAO) dao).saveOrUpdate(log);
		return log.getAccesslogId();
	}

	public List<AdmAccesslog> findAdmAccesslog(String id, EnumSearchType type) throws Exception{
		return ((IAdmAccesslogDAO) dao).findAdmAccesslog(id, type);
	}

    public int selectAdmAccesslogCount(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate) {
        return ((IAdmAccesslogDAO) dao).selectAdmAccesslogCount(channel, action, memberId, orderId, customerInfoId, userId, clientIp, message, startDate, endDate);
    }

	public List<AdmAccesslog> selectAdmAccesslog(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate, int pageNo, int pageSize) {
	    return ((IAdmAccesslogDAO) dao).selectAdmAccesslog(channel, action, memberId, orderId, customerInfoId, userId, clientIp, message, startDate, endDate, (pageNo - 1) * pageSize, pageSize);
	}
	
	public List<AdmAccesslog> findSendMail() {
		return ((IAdmAccesslogDAO) dao).findSendMail();
	}
}