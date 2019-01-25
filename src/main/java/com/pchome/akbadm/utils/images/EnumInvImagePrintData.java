package com.pchome.akbadm.utils.images;


public enum EnumInvImagePrintData implements IEnumImageData{
	
	
	TX_ZIP(242,179,12,""),
	TX_ADDR(242,199,14,""),
	TX_NAME(242,229,20,""),
	TX_NAMETAG(330,229,16,""),
	
	
	TXA_YEAR(344,422,12,""),
	TXA_MONTH(382,422,12,""),
	TXA_DATE(415,422,12,""),
	
	TXA_INVNO(186,436,12,""),
	TXA_CHNO(458,436,12,""),
	TXA_ORDERNO(603,472,10,""),

	TXA_PRODNAME_0(136,514,12,""),
	TXA_QTY_0(359,514,12,""),
	TXA_ONEPRICE_0(409,514,12,""),
	TXA_PRICE_0(487,514,12,""),
	
	TXA_PRODNAME_1(136,528,12,""),
	TXA_QTY_1(359,528,12,""),
	TXA_ONEPRICE_1(409,528,12,""),
	TXA_PRICE_1(487,528,12,""),
	
	TXA_TOTALPRICEA(487,583,12,""),
	TXA_TOTALPRICEB(487,623,12,""),
	
	TXA_TAX(254,609,10,""),
	
	TXA_CHWORDA(198,641,12,""),
	TXA_CHWORDB(234,641,12,""),
	TXA_CHWORDC(273,641,12,""),
	TXA_CHWORDD(314,641,12,""),
	TXA_CHWORDE(357,641,12,""),
	TXA_CHWORDF(397,641,12,""),
	TXA_CHWORDG(436,641,12,""),
	TXA_CHWORDH(477,641,12,""),
	
	//------------------------------
	TXB_YEAR(344,743,12,""),
	TXB_MONTH(382,743,12,""),
	TXB_DATE(415,743,12,""),
	
	TXB_INVNO(186,758,12,""),
	TXB_CHNO(458,757,12,""),
	TXB_ORDERNO(603,797,10,""),

	TXB_PRODNAME_0(136,836,12,""),
	TXB_QTY_0(359,836,12,""),
	TXB_ONEPRICE_0(409,836,12,""),
	TXB_PRICE_0(487,836,12,""),
	
	TXB_PRODNAME_1(136,848,12,""),
	TXB_QTY_1(359,848,12,""),
	TXB_ONEPRICE_1(409,848,12,""),
	TXB_PRICE_1(487,848,12,""),
	
	TXB_TOTALPRICEA(487,904,12,""),
	TXB_TOTALPRICEB(487,931,12,""),
	
	TXB_TAX(254,931,10,""),
	
	TXB_CHWORDA(198,961,12,""),
	TXB_CHWORDB(234,961,12,""),
	TXB_CHWORDC(273,961,12,""),
	TXB_CHWORDD(314,961,12,""),
	TXB_CHWORDE(357,961,12,""),
	TXB_CHWORDF(397,961,12,""),
	TXB_CHWORDG(436,961,12,""),
	TXB_CHWORDH(477,961,12,"");
	
	
	private int x;
	private int y;
	private int size;
	private String defaultStr;

	EnumInvImagePrintData(int x,int y,int size,String defaultStr){
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
