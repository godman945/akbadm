package com.pchome.enumerate.pfp;

public enum EnumPfpCatalogUploadType {
	
	REPLACE("1", "檔案取代"),
	UPDATE("2", "檔案更新");
	
	private final String type;
	private final String typeName;

	private EnumPfpCatalogUploadType(String type, String typeName) {
		this.type = type;
		this.typeName = typeName;
	}

	public String getType() {
		return type;
	}

	public String getTypeName() {
		return typeName;
	}

}