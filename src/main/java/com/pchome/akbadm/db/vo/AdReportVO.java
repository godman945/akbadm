package com.pchome.akbadm.db.vo;

public class AdReportVO {

	private String adPvclkDate;		//日期
	private String adSeq; //廣告序號(取得預覽API需要)
	private String adTemplateSeq; //廣告版型序號(取得預覽API需要)
	private String customerId; //客戶帳號
	private String customerName; //客戶姓名
	private String pfdCustomerInfoId;
	private String adAction; //廣告活動
	private String adGroup; //廣告群組
	private String content; //廣告內容
	private String adTitle; //廣告title
	private String kwPvSum; //PV總和
	private String kwClkSum; //Click總和
	private String kwPriceSum; //價格總和
	private String clkPriceAvg; //平均點選出價
    private String pvPriceAvg; //千次曝光收益
	private String kwClkRate; //點選率 = Click總和 / PV總和
	private String realUrl; //廣告播放位置

	private String adStyle;	//廣告形態
	private String showUrl;	//廣告顯示網址
	private String imgWidth;	//圖像廣告寬度
	private String imgHeight;	//圖像廣告高度
	private String originalImg;	//圖像廣告預覽圖片位置
	private String title;		//廣告標題

	/** 圖像(html5)廣告預覽專用 **/
	private String html5Tag;
	private String zipTitle;

    // video
    private String adDetailTitle;
    private String adDetailContent;
    private String adDetailImg;
    private String adDetailMp4Path;
    private String adDetailVideoWidth;
    private String adDetailVideoHeight;
    private String adDetailVideoSeconds;
    private String adDetailVideoUrl;
    private String adDetailRealUrl;

	public String getAdPvclkDate() {
		return adPvclkDate;
	}

	public void setAdPvclkDate(String adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}

	public String getKwPvSum() {
		return kwPvSum;
	}

	public void setKwPvSum(String kwPvSum) {
		this.kwPvSum = kwPvSum;
	}

	public String getKwClkSum() {
		return kwClkSum;
	}

	public void setKwClkSum(String kwClkSum) {
		this.kwClkSum = kwClkSum;
	}

	public String getKwPriceSum() {
		return kwPriceSum;
	}

	public void setKwPriceSum(String kwPriceSum) {
		this.kwPriceSum = kwPriceSum;
	}

	public String getClkPriceAvg() {
		return clkPriceAvg;
	}

	public void setClkPriceAvg(String clkPriceAvg) {
		this.clkPriceAvg = clkPriceAvg;
	}

    public String getPvPriceAvg() {
        return pvPriceAvg;
    }

    public void setPvPriceAvg(String pvPriceAvg) {
        this.pvPriceAvg = pvPriceAvg;
    }

	public String getKwClkRate() {
		return kwClkRate;
	}

	public void setKwClkRate(String kwClkRate) {
		this.kwClkRate = kwClkRate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAdAction() {
		return adAction;
	}

	public void setAdAction(String adAction) {
		this.adAction = adAction;
	}

	public String getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAdSeq() {
		return adSeq;
	}

	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}

	public String getAdTemplateSeq() {
		return adTemplateSeq;
	}

	public void setAdTemplateSeq(String adTemplateSeq) {
		this.adTemplateSeq = adTemplateSeq;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public String getAdStyle() {
		return adStyle;
	}

	public void setAdStyle(String adStyle) {
		this.adStyle = adStyle;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getOriginalImg() {
		return originalImg;
	}

	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHtml5Tag() {
		return html5Tag;
	}

	public void setHtml5Tag(String html5Tag) {
		this.html5Tag = html5Tag;
	}

	public String getZipTitle() {
		return zipTitle;
	}

	public void setZipTitle(String zipTitle) {
		this.zipTitle = zipTitle;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

    public String getAdDetailTitle() {
        return adDetailTitle;
    }

    public void setAdDetailTitle(String adDetailTitle) {
        this.adDetailTitle = adDetailTitle;
    }

    public String getAdDetailContent() {
        return adDetailContent;
    }

    public void setAdDetailContent(String adDetailContent) {
        this.adDetailContent = adDetailContent;
    }

    public String getAdDetailImg() {
        return adDetailImg;
    }

    public void setAdDetailImg(String adDetailImg) {
        this.adDetailImg = adDetailImg;
    }

    public String getAdDetailMp4Path() {
        return adDetailMp4Path;
    }

    public void setAdDetailMp4Path(String adDetailMp4Path) {
        this.adDetailMp4Path = adDetailMp4Path;
    }

    public String getAdDetailVideoWidth() {
        return adDetailVideoWidth;
    }

    public void setAdDetailVideoWidth(String adDetailVideoWidth) {
        this.adDetailVideoWidth = adDetailVideoWidth;
    }

    public String getAdDetailVideoHeight() {
        return adDetailVideoHeight;
    }

    public void setAdDetailVideoHeight(String adDetailVideoHeight) {
        this.adDetailVideoHeight = adDetailVideoHeight;
    }

    public String getAdDetailVideoSeconds() {
        return adDetailVideoSeconds;
    }

    public void setAdDetailVideoSeconds(String adDetailVideoSeconds) {
        this.adDetailVideoSeconds = adDetailVideoSeconds;
    }

    public String getAdDetailVideoUrl() {
        return adDetailVideoUrl;
    }

    public void setAdDetailVideoUrl(String adDetailVideoUrl) {
        this.adDetailVideoUrl = adDetailVideoUrl;
    }

    public String getAdDetailRealUrl() {
        return adDetailRealUrl;
    }

    public void setAdDetailRealUrl(String adDetailRealUrl) {
        this.adDetailRealUrl = adDetailRealUrl;
    }

}
