package com.pchome.enumerate.pfbx.board;

public enum EnumBoardContent {
	
	BOARD_CONTENT_1("1", "感謝您加入PChome廣告聯播網，您可以立即新增廣告版位，累積廣告收益。"),
	BOARD_CONTENT_2("2", "您的廣告帳戶目前已被關閉，廣告無法正常播放。"),
	BOARD_CONTENT_3("3", "您的廣告播放網址不符合刊登規範遭檢舉，請三天內立即檢視並更新。"),
	BOARD_CONTENT_4("4", "您的廣告收益已達請款門檻，可立即申請付款。"),
	BOARD_CONTENT_5("5", "申請中。"),
	BOARD_CONTENT_6("6", "已完成匯款。"),
	BOARD_CONTENT_7("7", "請款失敗。"),
	BOARD_CONTENT_8("8", "您的廣告帳戶已重新開通，廣告已正常播放。"),
	//帳務公告
	BOARD_CONTENT_9("9", "您的收款銀行資料審核失敗，請再次修改後送出審核。"),
	BOARD_CONTENT_10("10", "您的收款人資料審核失敗，請再次修改後送出審核。"),
	BOARD_CONTENT_11("11", "您的請款收據審核失敗，請再次修改後送出審核。"),
	BOARD_CONTENT_12("12", "您的請款發票審核失敗，請再次修改後送出審核。"),
	
	//廣告封鎖解放公告!!
	BOARD_CONTENT_13("13", "您的廣告播放網址〈網址〉已被封鎖:〈封鎖原因〉，故廣告已停止撥放！"),
	BOARD_CONTENT_14("14", " 您的廣告播放網址〈網址〉已重新開通，廣告可正常撥放。"),

	//廣告播放網站公告
	BOARD_CONTENT_15("15", "您新申請的廣告播放網站：〈網站〉審核失敗。"),
	BOARD_CONTENT_16("16", "新申請的廣告播放網站：〈網站〉已開通，您可以立即新增廣告版位，累計廣告收益。"),
	BOARD_CONTENT_17("17", "您的廣告播放網站：〈網站〉已被封鎖，廣告已暫停播放。"),
	BOARD_CONTENT_18("18", "您的廣告播放網站：〈網站〉已重新開通，廣告已正常播放。");
	
	private final String id;
	private final String content;
	
	private EnumBoardContent(String id, String content) {
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
