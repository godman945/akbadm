package com.pchome.rmi.accesslog;

import com.pchome.akbadm.db.service.accesslog.AdmAccesslogService;


public class AccesslogProviderImp implements IAccesslogProvider{
	
	private AdmAccesslogService admAccesslogService;
	
	public Integer addAccesslog(EnumAccesslogChannel channel, EnumAccesslogAction action, String message, String memberId,
			String orderId, String customerInfoId, String userId, String clientIp, 
			EnumAccesslogEmailStatus mailSend) throws Exception{
		
		return admAccesslogService.addAdmAccesslog(channel, action, message, memberId, orderId, customerInfoId, userId, clientIp, mailSend);
	}

	public void setAdmAccesslogService(AdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}


	



	
	
}
