package com.pchome.enumerate.ad;

public enum EnumPfpCatalog {
	
	/**
	 * 商品目錄類型 1:一般購物類
	 */
	CATALOG_SHOPPING("1", "一般購物"),
	/**
	 * 商品目錄類型 2:訂房住宿類
	 */
	CATALOG_BOOKING("2", "訂房住宿"),
	/**
	 * 商品目錄類型 3:交通航班類
	 */
	CATALOG_TRAFFIC("3", "交通航班"),
	/**
	 * 商品目錄類型 4:房產租售類
	 */
	CATALOG_RENT("4", "房產租售"),
	/**
	 * 上傳方式 1:檔案上傳
	 */
	CATALOG_UPLOAD_FILE_UPLOAD("1", "檔案上傳"), 
	/**
	 * 上傳方式 2:自動排程上傳
	 */
	CATALOG_UPLOAD_AUTOMATIC_SCHEDULING("2", "自動排程上傳"), 
	/**
	 * 上傳方式 3:賣場網址上傳
	 */
	CATALOG_UPLOAD_STORE_URL("3", "賣場網址上傳"), 
	/**
	 * 上傳方式 4:手動上傳
	 */
	CATALOG_UPLOAD_MANUAL_UPLOAD("4", "手動上傳"),
	/**
	 * 目錄圖檔顯示方式 1:全秀
	 */
	CATALOG_IMG_SHOW_SHOW_ALL("1", "全秀"),
	/**
	 * 目錄圖檔顯示方式 2:裁切
	 */
	CATALOG_IMG_SHOW_CUTTING("2", "裁切");
	
	
	private final String type;
	private final String typeName;

	private EnumPfpCatalog(String type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}

	public String getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

}