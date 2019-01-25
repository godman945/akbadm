package com.pchome.enumerate.pfd.bonus;

import com.pchome.rmi.bonus.EnumPfdAccountPayType;


public enum EnumPfdBonusItem {

	EVERY_MONTH_BONUS("1",
			"月-佣金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-month-bonus.xml",
			"monthBonusBillDetail.html",
			null,
			null,
			EnumPfdAccountPayType.BOTH,
			"N",
			EnumPfdBonusType.BONUS_FIXED),
						
	EVERY_QUARTER_BONUS("2",
			"季-獎金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-quarter-bonus.xml",
			"quarterBonusBillDetail.html",
			null,
			null,
			EnumPfdAccountPayType.BOTH,
			"N",
			EnumPfdBonusType.BONUS_FIXED),
	
	EVERY_YEAR_BONUS("3",
			"年度-獎金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-year-bonus.xml",
			"yearBonusBillDetail.html",
			null,
			null,
			EnumPfdAccountPayType.BOTH,
			"N",
			EnumPfdBonusType.BONUS_FIXED),
			
	EVERY_MONTH_DEVELOP_BONUS("4",
			"每月-開發獎金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-month-develope-bonus.xml",
			"reportCustInfo.html",
			null,
			null,
			EnumPfdAccountPayType.ADVANCE,
			"Y",
			EnumPfdBonusType.BONUS_DEVELOP),
			
	EVERY_QUARTER_DEVELOP_BONUS("5",
			"每季-開發獎金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-quarter-develope-bonus.xml",
			"reportCustInfo.html",
			null,
			null,
			EnumPfdAccountPayType.ADVANCE,
			"Y",
			EnumPfdBonusType.BONUS_DEVELOP),
			
	EVERY_YEAR_DEVELOP_BONUS("6",
			"每年-開發獎金",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"every-year-develope-bonus.xml",
			"reportCustInfo.html",
			null,
			null,
			EnumPfdAccountPayType.ADVANCE,
			"Y",
			EnumPfdBonusType.BONUS_DEVELOP),
			
	CASE_BONUS_YEAR_END_SALE_A("10",
			"A專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-A.xml",
			"reportAdDaily.html",
			"2014-11-14",
			"2014-11-25",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE),
			
	CASE_BONUS_YEAR_END_SALE_B("11",
			"B專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-B.xml",
			"reportAdDaily.html",
			"2014-11-20",
			"2015-01-31",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE),
			
	CASE_BONUS_YEAR_END_SALE_C("12",
			"C專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-C.xml",
			"reportAdDaily.html",
			"2014-11-20",
			"2014-12-31",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE),
			
	CASE_BONUS_YEAR_END_SALE_D("13",
			"D專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-D.xml",
			"reportAdDaily.html",
			"2014-11-13",
			"2014-11-30",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE),
			
	CASE_BONUS_YEAR_END_SALE_E("14",
			"E專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-E.xml",
			"reportAdDaily.html",
			"2014-12-01",
			"2014-12-31",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE),
	
	CASE_BONUS_YEAR_END_SALE_F("15",
			"F專案獎金-年終慶促銷",
			EnumPfdXmlParse.PARSE_GROUP_XML,
			"case-bonus-year-end-sale-F.xml",
			"reportAdDaily.html",
			"2014-12-01",
			"2015-01-31",
			EnumPfdAccountPayType.BOTH,
			"Y",
			EnumPfdBonusType.BONUS_CASE);
	
	private final String itemType;						// 獎金計算項目
	private final String ItemChName;					// 獎金計算名稱
	private final EnumPfdXmlParse enumPfdXmlParse;		// 讀檔案方式
	private final String fileName;						// 對應檔案名稱
	private final String reportAction;					// 報表對應action
	private final String startDate;						// 開始日期
	private final String endDate;						// 結束日期
	private final EnumPfdAccountPayType pfdPayType; 	// 付款類型
	private final String redirect;						// 另開新視窗
	private EnumPfdBonusType pfdBonusType;				// 獎金屬性

	

	private EnumPfdBonusItem(String itemType, String itemChName,
			EnumPfdXmlParse enumPfdXmlParse, String fileName,
			String reportAction, String startDate, String endDate,
			EnumPfdAccountPayType pfdPayType, String redirect,
			EnumPfdBonusType pfdBonusType) {
		this.itemType = itemType;
		ItemChName = itemChName;
		this.enumPfdXmlParse = enumPfdXmlParse;
		this.fileName = fileName;
		this.reportAction = reportAction;
		this.startDate = startDate;
		this.endDate = endDate;
		this.pfdPayType = pfdPayType;
		this.redirect = redirect;
		this.pfdBonusType = pfdBonusType;
	}

	public String getItemType() {
		return itemType;
	}

	public String getItemChName() {
		return ItemChName;
	}

	public EnumPfdXmlParse getEnumPfdXmlParse() {
		return enumPfdXmlParse;
	}

	public String getFileName() {
		return fileName;
	}

	public String getReportAction() {
		return reportAction;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public EnumPfdAccountPayType getPfdPayType() {
		return pfdPayType;
	}

	public String getRedirect() {
		return redirect;
	}

	public EnumPfdBonusType getPfdBonusType() {
		return pfdBonusType;
	}

	public void setPfdBonusType(EnumPfdBonusType pfdBonusType) {
		this.pfdBonusType = pfdBonusType;
	}
		
}
