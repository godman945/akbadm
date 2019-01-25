package com.pchome.akbadm.utils.images;


public enum EnumInvImageData implements IEnumImageData{
	
	TX_YEAR(326,72,14,""),
	TX_MONTH(376,72,14,""),
	TX_DATE(418,72,14,""),
	TX_INVNO(94,95,14,""),
	TX_CHNO(460,95,14,""),
	TX_ORDERNO(589,163,12,""),

	TX_PRODNAME_0(32,211,12,""),
	TX_QTY_0(335,211,12,""),
	TX_ONEPRICE_0(395,211,12,""),
	TX_PRICE_0(469,211,12,""),
	
	TX_PRODNAME_1(32,228,12,""),
	TX_QTY_1(335,228,12,""),
	TX_ONEPRICE_1(395,228,12,""),
	TX_PRICE_1(469,228,12,""),
	
	TX_TOTALPRICEA(469,377,12,""),
	TX_TOTALPRICEB(469,448,12,""),
	
	TX_CHWORDA(434,478,14,""),
	TX_CHWORDB(385,478,14,""),
	TX_CHWORDC(336,478,14,""),
	TX_CHWORDD(287,478,14,""),
	TX_CHWORDE(238,478,14,""),
	TX_CHWORDF(189,478,14,""),
	TX_CHWORDG(140,478,14,""),
	TX_CHWORDH(91,478,14,""),
	
	TX_NOTEA(506,211,12,"客服專線:"),
	TX_NOTEB(22,529,12,"本發票一台北市稅捐稽徵處 91 年 03 月 01 日北市稽大安( 甲  )字第 09160866000 號函核准使用");
	
	private int x;
	private int y;
	private int size;
	private String defaultStr;

	EnumInvImageData(int x,int y,int size,String defaultStr){
		this.x=x;
		this.y=y;
		this.size=size;
		this.defaultStr=defaultStr;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public String getDefaultStr() {
		return defaultStr;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}



}
