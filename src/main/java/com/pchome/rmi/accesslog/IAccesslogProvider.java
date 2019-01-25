package com.pchome.rmi.accesslog;

public interface IAccesslogProvider {
		
	public Integer addAccesslog(EnumAccesslogChannel channel, EnumAccesslogAction action, String message, String memberId,
			String orderId, String customerInfoId, String userId, String clientIp, 
			EnumAccesslogEmailStatus mailSend) throws Exception;
}
