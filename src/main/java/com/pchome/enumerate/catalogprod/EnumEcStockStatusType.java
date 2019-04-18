package com.pchome.enumerate.catalogprod;

public enum EnumEcStockStatusType {
	/**
	 * 0:無庫存
	 */
	Out_Of_Stock("0","無庫存"),		
	/**
	 * 1:有庫存
	 */
	In_Stock("1","有庫存"),			
	/**
	 * 2:預購
	 */
	Pre_Order("2","預購"),
	/**
	 * 3:停售
	 */
	Discontinued("3","停售");
	
	private final String type;
	private final String chName;
	
	private EnumEcStockStatusType(String type, String chName) {
		this.type = type;
		this.chName = chName;
	}

	public String getType() {
		return type;
	}

	public String getChName() {
		return chName;
	}

}
