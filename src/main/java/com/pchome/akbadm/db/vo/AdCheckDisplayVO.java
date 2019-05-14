package com.pchome.akbadm.db.vo;

import java.util.ArrayList;
import java.util.List;

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
	private String thirdCode; //第三方監測
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

    // prod
    private String adbgType;
    private String previewTpro;
    private String adDetailBuybtnBgColor;
    private String adDetailBuybtnFontColor;
    private String adDetailBuybtnTxt;
    private String adDetailDisBgColor;
    private String adDetailDisFontColor;
    private String adDetailDisTxtType;
    private String adDetailLogoBgColor;
    private String adDetailLogoFontColor;
    private String adDetailLogoImgUrl;
    private String adDetailLogoTxt;
    private String adDetailLogoType;
    private String adDetailProdAdUrl;
    private String adDetailProdGroup;
    private String adDetailProdImgShowType;
    private String adDetailProdReportName;
    private String adDetailSaleImgShowType;
    private String adDetailSaleImg;
    private String adDetailSaleEndImg;
    private List<String> adDetailLogoSaleImgList = new ArrayList<>();
    private List<String> adDetailSaleImgList = new ArrayList<>();
    private List<String> adDetailSaleEndImgList = new ArrayList<>();

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

    public String getAdbgType() {
        return adbgType;
    }

    public void setAdbgType(String adbgType) {
        this.adbgType = adbgType;
    }

    public String getPreviewTpro() {
        return previewTpro;
    }

    public void setPreviewTpro(String previewTpro) {
        this.previewTpro = previewTpro;
    }

    public String getAdDetailBuybtnBgColor() {
        return adDetailBuybtnBgColor;
    }

    public void setAdDetailBuybtnBgColor(String adDetailBuybtnBgColor) {
        this.adDetailBuybtnBgColor = adDetailBuybtnBgColor;
    }

    public String getAdDetailBuybtnFontColor() {
        return adDetailBuybtnFontColor;
    }

    public void setAdDetailBuybtnFontColor(String adDetailBuybtnFontColor) {
        this.adDetailBuybtnFontColor = adDetailBuybtnFontColor;
    }

    public String getAdDetailBuybtnTxt() {
        return adDetailBuybtnTxt;
    }

    public void setAdDetailBuybtnTxt(String adDetailBuybtnTxt) {
        this.adDetailBuybtnTxt = adDetailBuybtnTxt;
    }

    public String getAdDetailDisBgColor() {
        return adDetailDisBgColor;
    }

    public void setAdDetailDisBgColor(String adDetailDisBgColor) {
        this.adDetailDisBgColor = adDetailDisBgColor;
    }

    public String getAdDetailDisFontColor() {
        return adDetailDisFontColor;
    }

    public void setAdDetailDisFontColor(String adDetailDisFontColor) {
        this.adDetailDisFontColor = adDetailDisFontColor;
    }

    public String getAdDetailDisTxtType() {
        return adDetailDisTxtType;
    }

    public void setAdDetailDisTxtType(String adDetailDisTxtType) {
        this.adDetailDisTxtType = adDetailDisTxtType;
    }

    public String getAdDetailLogoBgColor() {
        return adDetailLogoBgColor;
    }

    public void setAdDetailLogoBgColor(String adDetailLogoBgColor) {
        this.adDetailLogoBgColor = adDetailLogoBgColor;
    }

    public String getAdDetailLogoFontColor() {
        return adDetailLogoFontColor;
    }

    public void setAdDetailLogoFontColor(String adDetailLogoFontColor) {
        this.adDetailLogoFontColor = adDetailLogoFontColor;
    }

    public String getAdDetailLogoImgUrl() {
        return adDetailLogoImgUrl;
    }

    public void setAdDetailLogoImgUrl(String adDetailLogoImgUrl) {
        this.adDetailLogoImgUrl = adDetailLogoImgUrl;
    }

    public String getAdDetailLogoTxt() {
        return adDetailLogoTxt;
    }

    public void setAdDetailLogoTxt(String adDetailLogoTxt) {
        this.adDetailLogoTxt = adDetailLogoTxt;
    }

    public String getAdDetailLogoType() {
        return adDetailLogoType;
    }

    public void setAdDetailLogoType(String adDetailLogoType) {
        this.adDetailLogoType = adDetailLogoType;
    }

    public String getAdDetailProdAdUrl() {
        return adDetailProdAdUrl;
    }

    public void setAdDetailProdAdUrl(String adDetailProdAdUrl) {
        this.adDetailProdAdUrl = adDetailProdAdUrl;
    }

    public String getAdDetailProdGroup() {
        return adDetailProdGroup;
    }

    public void setAdDetailProdGroup(String adDetailProdGroup) {
        this.adDetailProdGroup = adDetailProdGroup;
    }

    public String getAdDetailProdImgShowType() {
        return adDetailProdImgShowType;
    }

    public void setAdDetailProdImgShowType(String adDetailProdImgShowType) {
        this.adDetailProdImgShowType = adDetailProdImgShowType;
    }

    public String getAdDetailProdReportName() {
        return adDetailProdReportName;
    }

    public void setAdDetailProdReportName(String adDetailProdReportName) {
        this.adDetailProdReportName = adDetailProdReportName;
    }

    public String getAdDetailSaleImgShowType() {
        return adDetailSaleImgShowType;
    }

    public void setAdDetailSaleImgShowType(String adDetailSaleImgShowType) {
        this.adDetailSaleImgShowType = adDetailSaleImgShowType;
    }

    public String getAdDetailSaleImg() {
        return adDetailSaleImg;
    }

    public void setAdDetailSaleImg(String adDetailSaleImg) {
        this.adDetailSaleImg = adDetailSaleImg;
    }

    public String getAdDetailSaleEndImg() {
        return adDetailSaleEndImg;
    }

    public void setAdDetailSaleEndImg(String adDetailSaleEndImg) {
        this.adDetailSaleEndImg = adDetailSaleEndImg;
    }

    public List<String> getAdDetailLogoSaleImgList() {
        return adDetailLogoSaleImgList;
    }

    public void setAdDetailLogoSaleImgList(List<String> adDetailLogoSaleImgList) {
        this.adDetailLogoSaleImgList = adDetailLogoSaleImgList;
    }

    public List<String> getAdDetailSaleImgList() {
        return adDetailSaleImgList;
    }

    public void setAdDetailSaleImgList(List<String> adDetailSaleImgList) {
        this.adDetailSaleImgList = adDetailSaleImgList;
    }

    public List<String> getAdDetailSaleEndImgList() {
        return adDetailSaleEndImgList;
    }

    public void setAdDetailSaleEndImgList(List<String> adDetailSaleEndImgList) {
        this.adDetailSaleEndImgList = adDetailSaleEndImgList;
    }

	public String getThirdCode() {
		return thirdCode;
	}

	public void setThirdCode(String thirdCode) {
		this.thirdCode = thirdCode;
	}
    
}
