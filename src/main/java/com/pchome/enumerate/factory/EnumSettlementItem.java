package com.pchome.enumerate.factory;

public enum EnumSettlementItem {
	
	SAVE_MONEY,				// 儲值金	
	ADVANCE_REFUND,			// 儲值金退款(預付退款)
	LATER_REFUND,			// 後付退款
	FEEDBACK_MONEY,			// 回饋金	
	GIFT_SNO_MONEY,			// 廣告金(禮金)
	INVALID_COST,			// 無效點擊費用
	SPEND_COST,				// 廣告花費
	LATER_SAVE,				// 後付加值
	LATER_INVALID,			// 後付無效點擊
	LATER_SPEND;			// 後付廣告花費
	
	
	//RETRIEVE;               // 廣告金&回饋金到期回收 - 有問題暫時註解掉
	
	//GIFT_MONEY,			// 廣告金 - 活動過期	
	//GIFT_IQ,				// 廣告金(開戶即贈) - 活動過期
	//GIFT_201404;			// 廣告金(201404廣告金回饋) - 活動過期
}
