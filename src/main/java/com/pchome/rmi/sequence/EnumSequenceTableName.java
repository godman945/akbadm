package com.pchome.rmi.sequence;

public enum EnumSequenceTableName {

	ADM_AD("adm_ad", "ad"),	// 廣告 table ID
	ADM_FEEDBACK_RECORD("adm_feedback_record", "FBR"),	// 回饋金流水號
	ADM_FREE_ACTION("adm_free_action","FRA"), // 免費活動 id
	ADM_AD_FRAME("adm_ad_frame", "adf"),	// 
	ADM_AD_POOL("adm_ad_pool", "adp"),	// 
	ADM_DEFINE_AD("adm_define_ad", "dad"),	// 廣告定義 table ID
	ADM_RELATE_FRM_DAD("adm_relate_frm_dad", "rfrmdad"),	// 
	ADM_RELATE_TAD_DAD("adm_relate_tad_dad", "rtaddad"),	// 廣告樣板廣告定義關聯 table ID
	ADM_RELATE_TPRO_TAD("adm_relate_tpro_tad", "rtprota"),	// 商品樣板廣告樣板關聯 table ID
	ADM_TEMPLATE_AD("adm_template_ad", "tad"),	// 廣告樣板 table ID
	ADM_TEMPLATE_PRODUCT("adm_template_product", "tpro"),	// 商品樣板 table ID
	ADM_RECOGNIZE_RECORD("adm_recognize_record","RER"), // 加值金記錄 id
	PFD_ACCOUNT("pfd_customerInfo","PFDC"), // PFD 帳戶編號 id
	PFD_USER("pfd_user","PFDU"), //PFD 帳號 id
	PFBX_APPLY_ORDER("pfbx_apply_order","AO"),//PFBX 申請單 ID
	PFBX_USER_OPTION("pfbx_user_option","uo"),//廣告版位條件
	PFBX_BLOCK_CUSURL("pfbx_block_cusurl","bc"), //封鎖網址
	PFBX_INVALID_TRAFFIC("pfbx_invalid_traffic","PIT"), //無效流量
	PFP_CATALOG_UPLOAD_LOG("catalog_upload_log_seq", "PCUL"); // 商品目錄更新紀錄id

	private String snoName;
	private String charName;

	public String getSnoName() {
		return snoName;
	}

	public void setSnoName(String snoName) {
		this.snoName = snoName;
	}

	private EnumSequenceTableName(String snoName, String charName) {
		this.snoName = snoName;
		this.charName = charName;
	}

	public String getCharName() {
		return charName;
	}

	public void setCharName(String charName) {
		this.charName = charName;
	}

}