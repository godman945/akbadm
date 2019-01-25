package com.pchome.enumerate.bonus;

/**
 * 
 * 獎金計算項目
 * 1. fileName 必需要有對應的檔案, 如果沒有會發生錯誤
 * 2. enumParseType 對應那個方式讀取檔案
 * 3. reportAction 和前台 PFD 系統 Action 有關係, 如果沒有相對應 Action , 前台會發生錯誤
 * 4. redirect 判斷是否另開新視窗 
 * 5. 除了 EnumParseType 外, 其餘欄位和數據都要與前台(pfd) EnumBonusItem 一樣  
 * 6. EnumBonusType 獎金類型
 */
public enum EnumBonusItem {

	EVERY_MONTH_BONUS(
			1,
			"每月-佣金獎金",
			"every-month-bonus.xml",
			null,
			null,
			"monthBonusBillDetail.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_FIXED,			
			false),
			
	EVERY_QUARTER_BONUS(
			2,
			"每季-佣金獎金",
			"every-quarter-bonus.xml",
			null,
			null,
			"quarterBonusBillDetail.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_FIXED,			
			false),
			
	EVERY_YEAR_BONUS(
			3,
			"每年-佣金獎金",
			"every-year-bonus.xml",
			null,
			null,
			"yearBonusBillDetail.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_FIXED,
			false),	
			
	EVERY_MONTH_DEVELOP_BONUS(
			4,
			"每月-開發獎金",
			"every-month-develope-bonus.xml",
			null,
			null,
			"reportCustInfo.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_DEVELOP,			
			true),
			
	EVERY_QUARTER_DEVELOP_BONUS(
			5,
			"每季-開發獎金",
			"every-quarter-develope-bonus.xml",
			null,
			null,
			"reportCustInfo.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_DEVELOP,			
			true),
						
	EVERY_YEAR_DEVELOP_BONUS(
			6,
			"每年-開發獎金",
			"every-year-develope-bonus.xml",
			null,
			null,
			"reportCustInfo.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_DEVELOP,			
			true),

	CASE_BONUS_ANNUAL_SALE(
			7,
			"專案獎金-週年慶促銷",
			"case-bonus-annual-sale.xml",
			"2013-11-08",
			"2013-11-28",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
			
	CASE_BONUS_YEAR_END_SALE(
			8,
			"專案獎金-年終慶促銷",
			"case-bonus-year-end-sale.xml",
			"2013-12-01",
			"2013-12-31",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	DRAGON_BOAT_FESTIVAL(
			9,
			"專案獎金-端午節",
			"dragon-boat-festival.xml",
			"2014-06-01",
			"2014-07-10",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_A(
			10,
			"A專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-A.xml",
			"2014-11-14",
			"2014-11-25",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_B(
			11,
			"B專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-B.xml",
			"2014-11-20",
			"2015-01-31",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_C(
			12,
			"C專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-C.xml",
			"2014-11-20",
			"2014-12-31",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_D(
			13,
			"D專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-D.xml",
			"2014-11-13",
			"2014-11-30",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_E(
			14,
			"E專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-E.xml",
			"2014-12-01",
			"2014-12-31",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true),
	CASE_BONUS_YEAR_END_SALE_F(
			15,
			"F專案獎金-年終慶促銷",
			"case-bonus-year-end-sale-F.xml",
			"2014-12-01",
			"2015-01-31",
			"reportAdDaily.html",
			EnumParseType.PARSE_GROUP_XML,			
			EnumBonusType.BONUS_CASE,			
			true);
	
	private final int itemId;						// 項目編號
	private final String itemName;					// 項目名稱
	private final String fileName;					// XML 檔案名稱
	private final String startDate;					// 奬金計算開始日期
	private final String endDate;					// 奬金計算結束日期
	private final String reportAction;				// 報表對應action
	private final EnumParseType enumParseType;		// 讀檔案方式	
	private final EnumBonusType enumBonusType;		// 獎金類別	
	private final boolean isRedirect;				// 另開新視窗
	
	private EnumBonusItem(int itemId, String itemName, String fileName, 
			String startDate, String endDate, String reportAction, 
			EnumParseType enumParseType, EnumBonusType enumBonusType, boolean isRedirect) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.fileName = fileName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reportAction = reportAction;
		this.enumParseType = enumParseType;
		this.enumBonusType = enumBonusType;
		this.isRedirect = isRedirect;
	}

	public int getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public String getFileName() {
		return fileName;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getReportAction() {
		return reportAction;
	}

	public EnumParseType getEnumParseType() {
		return enumParseType;
	}

	public EnumBonusType getEnumBonusType() {
		return enumBonusType;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

}
