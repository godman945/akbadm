package com.pchome.enumerate.ad;

public enum EnumIndexField {
    pk (
        1.0f,
        "pk"
    ),
    adActionId (
        1.0f,
        "ad_action_id"
    ),
    adGroupId (
        1.0f,
        "ad_group_id"
    ),
    adId (
        1.0f,
        "ad_id"
    ),
    adKeywordId (
        1.0f,
        "ad_keyword_id"
    ),
    adKeyword (
        1.0f,
        "ad_keyword"
    ),
    adExcludeKeyword (
        1.0f,
        "ad_exclude_keyword"
    ),
    adActionControlPrice (
        1.0f,
        "ad_action_control_price"
    ),
    adKeywordSearchPrice (
        1.0f,
        "ad_keyword_search_price"
    ),
    adKeywordSearchPhrasePrice (
        1.0f,
        "ad_keyword_search_phrase_price"
    ),
    adKeywordSearchPrecisionPrice (
        1.0f,
        "ad_keyword_search_precision_price"
    ),
    adKeywordChannelPrice (
        1.0f,
        "ad_keyword_channel_price"
    ),
    adKeywordTempPrice (
        1.0f,
        "ad_keyword_temp_price"
    ),
    adKeywordOpen (
        1.0f,
        "ad_keyword_open"
    ),
    adKeywordPhraseOpen (
        1.0f,
        "ad_keyword_phrase_open"
    ),
    adKeywordPrecisionOpen (
        1.0f,
        "ad_keyword_precision_open"
    ),
    adKeywordPv (
        1.0f,
        "ad_keyword_pv"
    ),
    adKeywordClk (
        1.0f,
        "ad_keyword_clk"
    ),
    recognize (
        1.0f,
        "recognize"
    ),
    updateDate (
        1.0f,
        "update_date"
    ),
    createDate (
        1.0f,
        "create_date"
    );

    private float boost;
    private String underLine;

    EnumIndexField(float boost, String underLine) {
        this.boost = boost;
        this.underLine = underLine;
    }

    public float getBoost() {
        return boost;
    }

    public String getUnderLine() {
        return underLine;
    }
}