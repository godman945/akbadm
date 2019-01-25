package com.pchome.enumerate.pfbx.customerInfo;

public enum EnumPfbxCustomerInfo {
    	AD_TYPE_1("1", "搜尋廣告"),
    	AD_TYPE_2("2", "內容廣告"),
    	AD_TYPE_3("3", "全部"),
    	AD_TYPE_4("4", "無權限"),
    	MENU_PROPERTIES_0("0", "屬性");
	private final String id;
	private final String content;
	
	private EnumPfbxCustomerInfo(String id, String content) {
		this.id = id;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
