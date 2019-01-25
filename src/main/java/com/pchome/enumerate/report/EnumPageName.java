package com.pchome.enumerate.report;

public enum EnumPageName {
    KEYWORD_ACTIVATE_NUM (
        "新開通戶數總計",
        false
    ),
    KEYWORD_ACTIVATE_PRICE (
        "新開通儲值金額總計",
        true
    ),
    KEYWORD_CUSTOMER_NUM (
        "儲值戶數總計",
        false
    ),
    KEYWORD_CUSTOMER_PRICE (
        "儲值金額總計",
        true
    ),
    KEYWORD_ADVANCE_ACTIVATE_NUM (
        "預付新開通戶數",
        false
    ),
    KEYWORD_ADVANCE_ACTIVATE_PRICE (
        "預付新開通儲值金額",
        true
    ),
    KEYWORD_ADVANCE_CUSTOMER_NUM (
        "預付儲值戶數",
        false
    ),
    KEYWORD_ADVANCE_CUSTOMER_PRICE (
        "預付儲值金額",
        true
    ),
    KEYWORD_LATER_ACTIVATE_NUM (
        "後付新開通戶數",
        false
    ),
    KEYWORD_LATER_ACTIVATE_PRICE (
        "後付新開通儲值金額",
        true
    ),
    KEYWORD_LATER_CUSTOMER_NUM (
        "後付儲值戶數",
        false
    ),
    KEYWORD_LATER_CUSTOMER_PRICE (
        "後付儲值金額",
        true
    ),
    KEYWORD_GIFT_PRICE (
        "贈送禮金",
        true
    ),
    KEYWORD_REFUND_PRICE (
        "退款金額",
        true
    ),
    KEYWORD_AD_CLK_PRICE (
        "點擊費用",
        true
    ),
    KEYWORD_REMAIN_PRICE (
        "廣告餘額",
        true
    ),
    KEYWORD_AD_NEW (
        "新增廣告明細",
        false
    ),
    KEYWORD_AD_NUM (
        "線上廣告明細",
        false
    ),
    KEYWORD_AD_READY (
        "待播放廣告明細",
        false
    ),
    KEYWORD_AD_DUE (
        "7日內即將到期廣告明細",
        false
    ),
    KEYWORD_AD_PV (
        "廣告曝光",
        false
    ),
    KEYWORD_AD_CLK (
        "廣告點擊",
        false
    ),
    KEYWORD_CPC (
        "點選數成本 (%)",
        true
    ),
    KEYWORD_CPM (
        "每千次曝光成本",
        true
    ),
    KEYWORD_CTR (
        "點閱率",
        true
    ),
    KEYWORD_AD_UNDER_MAX (
        "未達預算廣告數",
        false
    ),
    KEYWORD_REACH_RATE (
        "客戶預算達成率",
        true
    ),
    KEYWORD_OVER_PRICE (
        "營運費用(超播)",
        true
    ),
    KEYWORD_AD_INVALID_CLK (
        "惡意點擊數",
        false
    ),
    KEYWORD_AD_INVALID_CLK_PRICE (
        "惡意點擊費用",
        true
    );

    private String title;
    private boolean isDecimal;

    private EnumPageName(String title, boolean isDecimal) {
        this.title = title;
        this.isDecimal = isDecimal;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDecimal() {
        return isDecimal;
    }
}