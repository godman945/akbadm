package com.pchome.akbadm.factory.ad;

public enum EnumAdTag {
	
	START_TAG("<#"),
	END_TAG(">"),
	TPRO_TAG("tpro"),
	TAD_TAG("tad"),
	DAD_TAG("dad");
	//PRODSEQ_TAG("ProdSeq:null")
	
	private final String adTag;

	private EnumAdTag(String adTag) {
		this.adTag = adTag;
	}

	public String getAdTag() {
		return adTag;
	}
	
	
	
	
}
