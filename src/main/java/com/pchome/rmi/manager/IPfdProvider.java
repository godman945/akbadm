package com.pchome.rmi.manager;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IPfdProvider {

	// 列出 PFd 帳戶
	public List<PfdAccountVO> findPfdAccount(String memberId, String ip);
	// 是否帳戶管理者
	public boolean isManager(String memberId, String ip);
	
	public Map<String,String> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate) throws Exception;
}
