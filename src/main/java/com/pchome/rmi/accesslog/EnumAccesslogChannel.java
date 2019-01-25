package com.pchome.rmi.accesslog;

public enum EnumAccesslogChannel {
	
	MEMBER("member"),			// 會員中心
	PFP("pfp"),					// 聯播網前台
	ADM("adm"),					// 聯播網後台
	BILLING("billing"),			// 金流中心
	PFD("pfd"),                 // 經銷商前台
	PFD_ADM("pfd_adm"),         // 經銷商後台
	PFD_MEMBER("pfd_member"),	// 經銷商前台透過會員中心登入
	PFB("pfb");   				// pfb 聯播網

	private final String channel;

	private EnumAccesslogChannel(String channel) {
		this.channel = channel;
	}

	public String getChannel() {
		return channel;
	}
}
