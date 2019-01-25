package com.pchome.akbadm.db.dao.accesslog;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.enumerate.accesslog.EnumSearchType;

public interface IAdmAccesslogDAO extends IBaseDAO<AdmAccesslog, Integer> {

	public List<AdmAccesslog> findAdmAccesslog(String id, EnumSearchType type) throws Exception;

	public int selectAdmAccesslogCount(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate);

	public List<AdmAccesslog> selectAdmAccesslog(String channel, String action, String memberId, String orderId, String customerInfoId, String userId, String clientIp, String message, Date startDate, Date endDate, int firstResult, int maxResults);

	public List<AdmAccesslog> findSendMail();
}
