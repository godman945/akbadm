package com.pchome.akbadm.db.vo;

import com.pchome.enumerate.ad.EnumAdStatus;

/**
 * 廣告審核畫面用
 */
public class AdCheckDisplayVO {

	private String adSeq; //廣告序號
	private String customerName; //客戶名稱
	private String sendVerifyTime; //送審時間
	private String adPreview; //廣告預覽(打 API 獲得)
	private String illegalKeyWord; //禁用字違規項目
	private String adGroupName; //廣告群組名稱
	private String adActionName; //廣告活動名稱
	private int status; //狀態
	private String adCategory; //廣告類別

	private String adVerifyRejectReason; //拒絕理由

	private String adStyle;		//廣告類別

	/** 圖像廣告預覽專用 **/
	private String realUrl;
	private String originalImg;
	private String imgWidth;
	private String imgHeight;
	private String showUrl;
	private String title;

	/** 圖像(html5)廣告預覽專用 **/
	private String html5Tag;
	private String zipTitle;
	private String zipFile;

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

	public String getAdSeq() {
		return adSeq;
	}

	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}

	public String getSendVerifyTime() {
		return sendVerifyTime;
	}

	public void setSendVerifyTime(String sendVerifyTime) {
		this.sendVerifyTime = sendVerifyTime;
	}

	public String getAdPreview() {
		return adPreview;
	}

	public void setAdPreview(String adPreview) {
		this.adPreview = adPreview;
	}

	public String getIllegalKeyWord() {
		return illegalKeyWord;
	}

	public void setIllegalKeyWord(String illegalKeyWord) {
		this.illegalKeyWord = illegalKeyWord;
	}

	public String getAdGroupName() {
		return adGroupName;
	}

	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}

	public String getAdActionName() {
		return adActionName;
	}

	public void setAdActionName(String adActionName) {
		this.adActionName = adActionName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStatus() {
		if (status == EnumAdStatus.Verify_sys_pass.getStatusId()) {
			return EnumAdStatus.Verify_sys_pass.getStatusDesc();
		} else if (status == EnumAdStatus.Verify_sys_regect.getStatusId()) {
			return EnumAdStatus.Verify_sys_regect.getStatusDesc();
		}
		return null;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(String adCategory) {
		this.adCategory = adCategory;
	}

	public String getAdVerifyRejectReason() {
	    return adVerifyRejectReason;
	}

	public void setAdVerifyRejectReason(String adVerifyRejectReason) {
	    this.adVerifyRejectReason = adVerifyRejectReason;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}

	public String getOriginalImg() {
		return originalImg;
	}

	public void setOriginalImg(String originalImg) {
		this.originalImg = originalImg;
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

	public String getZipFile() {
		return zipFile;
	}

	public void setZipFile(String zipFile) {
		this.zipFile = zipFile;
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
