package com.pchome.enumerate.ad;

public enum EnumProdAdBtnText {
	BTN_BUY("1","立即購買"),
	BTN_BOOK("2","立即預定"),
	BTN_DOWNLOAD("3","立即下載"),
	BTN_MORE("4","了解更多"),
	BTN_LINK("5","開起連結"),
	BTN_REGIST("6","立即註冊");
	
	private final String btnType;
	private final String btnText;
	
	private EnumProdAdBtnText(String btnType, String btnText) {
		this.btnType = btnType;
		this.btnText = btnText;
	}

	public String getBtnType() {
		return btnType;
	}

	public String getBtnText() {
		return btnText;
	}



	
}
