package com.pchome.rmi.accesslog;

public enum EnumAccesslogAction {
	
	ERROR("0","錯誤訊息"),								// 錯誤訊息
	MEMBER_LOGIN("1","會員中心-登入"),					// 會員登入
	ACCOUNT_ADD_MONEY("2","帳戶儲值"),					// 帳戶儲值
	GET_JSON("3","取訂單資料"),							// 取訂單資料
	STATUS_NOTIFY("4","狀態通知"),						// 金流狀態通知
	ACCOUNT_MODIFY("5","帳戶資料-異動"),				// 帳戶資料異動
	USER_MODIFY("6","使用者資料-異動"),					// 使用者資料異動
	AD_STATUS_MODIFY("7","廣告狀態異動"),				// 廣告狀態異動
	AD_MONEY_MODIFY("8","廣告價格異動"),				// 廣告價格異動
	AD_DATE_MODIFY("9","廣告走期異動"),					// 廣告走期異動
	PLAY_MODIFY("11","播放管理異動"),					// 播放管理異動
	PFB_INVALID_TRAFFIC("12","PFB無效流量扣款"),		// PFB無效流量扣款
	WARNING("10","警告訊息"),							// 警告訊息
	PFB_CLICK("13","點擊"),		    //點擊行為
	PFP_CODE_MODIFY("14","代碼管理異動");
	private final String action;
	private final String message;
	
	private EnumAccesslogAction(String action, String message){
		this.action = action;
		this.message = message;
	}

	public String getAction() {
		return action;
	}

	public String getMessage() {
		return message;
	}
	
}