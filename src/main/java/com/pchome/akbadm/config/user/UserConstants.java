package com.pchome.akbadm.config.user;

import java.util.Map;
import java.util.TreeMap;

public class UserConstants {

	public static final String USER_STATUS_ACTIVE = "1";
	public static final String USER_STATUS_INACTIVE = "2";
	public static final String USER_STATUS_DELETED = "3";

	public static final String USER_STATUS_ACTIVE_DESC = "正常";
	public static final String USER_STATUS_INACTIVE_DESC = "停權";
	public static final String USER_STATUS_DELETED_DESC = "刪除";

	/**
	 * 帳號狀態集合
	 */
	public static Map<String, String> getUserStatusMap() {

		Map<String, String> statusMap = new TreeMap<String, String>();
		statusMap.put(USER_STATUS_ACTIVE, USER_STATUS_ACTIVE_DESC);
		statusMap.put(USER_STATUS_INACTIVE, USER_STATUS_INACTIVE_DESC);
		statusMap.put(USER_STATUS_DELETED, USER_STATUS_DELETED_DESC);

		return statusMap;
	}
}
