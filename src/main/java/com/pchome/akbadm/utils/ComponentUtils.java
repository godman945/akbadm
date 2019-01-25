package com.pchome.akbadm.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pchome.enumerate.ad.EnumAdStatus;

public class ComponentUtils {

	/**
	 * 廣告型態 pfp_ad.ad_style
	 */
	public static Map<String, String> getAdStyleSelectOptionsMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("txt", "文字廣告");
		map.put("tmg", "圖文廣告");
		map.put("img", "圖片廣告");
		return map;
	}

	/**
	 * 關鍵字廣告形式 pfp_ad_keyword_pvclk.ad_keyword_type  
	 */
	public static Map<String, String> getKeywordTypeSelectOptionsMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("1", "找東西廣告");
		map.put("2", "PChome頻道廣告");
		return map;
	}

	/**
	 * 廣告形式 pfp_ad_pvclk.ad_type  
	 */
	public static Map<String, String> getAdTypeSelectOptionsMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("1", "找東西廣告");
		map.put("2", "PChome頻道廣告");
		return map;
	}

	public static Map<String, String> getAdStatusDescMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (EnumAdStatus e : EnumAdStatus.values()) {
			map.put(Integer.toString(e.getStatusId()), e.getStatusDesc());
		}
		return map;
	}
}
