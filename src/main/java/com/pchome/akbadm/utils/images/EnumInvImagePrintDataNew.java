package com.pchome.akbadm.utils.images;


public enum EnumInvImagePrintDataNew implements IEnumImageData{
	
	
	TX_ZIP(242,179,12,""),
	TX_ADDR(242,199,14,""),
	TX_NAME(242,229,20,""),
	TX_NAMETAG(330,229,16,""),
	
	//------------------------------
	TXC_PRODNAME_0(52,400,14,""),
	TXC_QTY_0(571,400,14,""),
	TXC_ONEPRICE_0(612,400,14,""),
	TXC_PRICE_0(666,400,14,""),
	
	TXC_PRODNAME_1(52,415,14,""),
	TXC_QTY_1(572,415,14,""),
	TXC_ONEPRICE_1(612,415,14,""),
	TXC_PRICE_1(666,415,14,""),
	
	TXC_QTY(110,726,14,""),
	//------------------------------
	TXA_YEAR(192,782,10,""),
	TXA_MONTH(219,782,10,""),
	TXA_DATE(241,782,10,""),
	
	TXA_INVNO(112,795,10,""),
	TXA_CHNO(270,795,10,""),
	TXA_ORDERNO(290,826,10,""),

	TXA_PRODNAME_0(70,858,12,""),
	TXA_QTY_0(186,858,12,""),
	TXA_ONEPRICE_0(222,858,12,""),
	TXA_PRICE_0(258,858,12,""),
	
	TXA_PRODNAME_1(70,874,12,""),
	TXA_QTY_1(186,874,12,""),
	TXA_ONEPRICE_1(222,874,12,""),
	TXA_PRICE_1(258,874,12,""),
	
	TXA_TOTALPRICEA(256,916,10,""),
	TXA_TOTALPRICEB(256,954,10,""),
	
	TXA_TAX(136,942,10,""),
	
	TXA_CHWORDA(105,974,12,""),
	TXA_CHWORDB(127,974,12,""),
	TXA_CHWORDC(149,974,12,""),
	TXA_CHWORDD(171,974,12,""),
	TXA_CHWORDE(191,974,12,""),
	TXA_CHWORDF(213,974,12,""),
	TXA_CHWORDG(235,974,12,""),
	TXA_CHWORDH(257,974,12,""),
	
	//------------------------------
	TXB_YEAR(527,782,10,""),
	TXB_MONTH(552,782,10,""),
	TXB_DATE(572,782,10,""),
	
	TXB_INVNO(442,795,10,""),
	TXB_CHNO(604,795,10,""),
	TXB_ORDERNO(622,826,10,""),

	TXB_PRODNAME_0(402,858,12,""),
	TXB_QTY_0(518,858,12,""),
	TXB_ONEPRICE_0(554,858,12,""),
	TXB_PRICE_0(590,858,12,""),
	
	TXB_PRODNAME_1(402,874,12,""),
	TXB_QTY_1(518,874,12,""),
	TXB_ONEPRICE_1(554,874,12,""),
	TXB_PRICE_1(590,874,12,""),
	
	TXB_TOTALPRICEA(588,916,10,""),
	TXB_TOTALPRICEB(588,954,10,""),
	
	TXB_TAX(470,942,10,""),
	
	TXB_CHWORDA(438,974,12,""),
	TXB_CHWORDB(460,974,12,""),
	TXB_CHWORDC(482,974,12,""),
	TXB_CHWORDD(504,974,12,""),
	TXB_CHWORDE(524,974,12,""),
	TXB_CHWORDF(546,974,12,""),
	TXB_CHWORDG(568,974,12,""),
	TXB_CHWORDH(590,974,12,"");
	
	
	private int x;
	private int y;
	private int size;
	private String defaultStr;

	EnumInvImagePrintDataNew(int x,int y,int size,String defaultStr){
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
