package com.pchome.enumerate.convert;

public enum EnumConvertClassType {
	ViewContent("1","查看內容"),
	Search("2","搜尋"),
	AddToCart("3","加到購物車"),
	AddToShoppingList("4","加到購物清單"),
	StartToPayUp("5","開始結帳"),
	AddPaymentInfo("6","新增付款資料"),
	Buy("7","完成購買"),
	CompleteRegistration("8","完成註冊"),
	AddWishList("9","加到願望清單");
	
	private final String type;
	private final String chName;
	
	private EnumConvertClassType(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}

}
