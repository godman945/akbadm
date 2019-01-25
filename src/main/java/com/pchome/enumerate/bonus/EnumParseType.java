package com.pchome.enumerate.bonus;

/**
 * 讀取檔案類型
 * 1. PARSE_GROUP_XML 排程計算獎金
 * 2. PARSE_MONTH_BONUS_XML 後台設定月獎金類型
 * 3. PARSE_ALL_XML 後台設定獎金類型
 */
public enum EnumParseType {

	PARSE_GROUP_XML(1),
	PARSE_MONTH_BONUS_XML(2),
	PARSE_ALL_XML(3);
	
	private final int type;

	private EnumParseType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
