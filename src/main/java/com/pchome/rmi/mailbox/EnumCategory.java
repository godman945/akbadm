package com.pchome.rmi.mailbox;

public enum EnumCategory {
    OTHER(
        "0",
        "",
        "",
        "",
        "",
        false,
        false
    ),
    REMAIN_TOO_LOW(
        "1",
        "帳戶餘額偏低",
        "帳戶餘額過低，請進行儲值以便繼續播出廣告！",
        "您的PChome關鍵字廣告帳戶餘額偏低",
        "show.pchome.com.tw/accountRemain.html",
        true,
        true
    ),
    REMAIN_NOT_ENOUGH(
        "2",
        "帳戶餘額不足",
        "帳戶餘額不足已停止播放廣告，請為帳戶儲值金額，讓廣告恢復上線",
        "您的PChome關鍵字廣告帳戶目前餘額不足",
        "show.pchome.com.tw/accountRemain.html",
        true,
        true
    ),
    ACCOUNT_CLOSED(
        "3",
        "帳戶狀態設為「關閉」",
        "您已將廣告帳戶設為關閉，故無法播放廣告。如要繼續播出廣告，請檢視帳戶狀態設定，並將帳戶狀態設定為開啟",
        "帳戶狀態設為「關閉」",
        "show.pchome.com.tw/accountInfoModify.html",
        true,
        false
    ),
    MONEY_ENTERED(
        "4",
        "廣告金已入帳",
        "您的廣告點選金額已達$500，帳戶已贈送$1500廣告金",
        "廣告金已入帳",
        "show.pchome.com.tw/transDetail.html",
        true,
        false
    ),
    ENABLE_REMINDED(
        "5",
        "啟用提醒",
        "提醒您：帳戶中目前沒有執行中的廣告",
        "刊登PChome關鍵字廣告！吸引更多新客戶",
        "",
        true,
        true
    ),
    VERIFY_DENIED(
        "6",
        "審核拒絕",
        "廣告審核遭拒絕",
        "您的 PChome聯播網廣告已拒登",
        "",
        true,
        true
    ),
    INVALID_DOWN(
        "7",
        "管理者將廣告「違規下架」",
        "ad_Title廣告違反刊登規範，已下架拒刊",
        "您的 PChome關鍵字廣告已拒登",
        "",
        true,
        true
    ),
    AD_EXPIRE(
        "8",
        "廣告即將到期",
        "提醒您：廣告即將到期",
        "廣告即將到期通知",
        "",
        true,
        true
    );

	private String category;
	private String chName;
    private String boardContent;
    private String mailTitle;
    private String urlAddress;
	private boolean board;
	private boolean mailbox;

    private EnumCategory(String category, String chName, String boardContent, String mailTitle, String urlAddress, boolean board, boolean mailbox) {
        this.category = category;
        this.chName = chName;
        this.boardContent = boardContent;
        this.mailTitle = mailTitle;
        this.urlAddress = urlAddress;
        this.board = board;
        this.mailbox = mailbox;
    }

    public String getCategory() {
        return category;
    }

    public String getChName() {
        return chName;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public boolean isBoard() {
        return board;
    }

    public boolean isMailbox() {
        return mailbox;
    }
}