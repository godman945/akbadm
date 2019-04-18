package com.pchome.akbadm.db.vo;

import java.util.ArrayList;
import java.util.List;

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
	private String convertCountSum;		//總轉換數
	private String convertPriceCountSum;	//總轉換價值
	private String convertCVR;	//轉換率
	private String convertCost;	//平均轉換成本
	private String convertInvestmentCost;	//廣告投資報酬率
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
    
    // 商品廣告PROD
    private String prodReportName;
    private String prodAdUrl;
    private List<String> adDetailLogoSaleImgList = new ArrayList<>();	//行銷圖
    private List<String> adDetailSaleImgList = new ArrayList<>();		//結尾行銷圖
    private String adDetailLogoTxt;	//LOGO標題文字		
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
    private String adDetailLogoType;
    private String adDetailProdAdUrl;
    private String adDetailProdGroup;
    private String adDetailProdImgShowType;
    private String adDetailProdReportName;
    private String adDetailSaleImgShowType;
    private String adDetailSaleImg;
    private String adDetailSaleEndImg;
    private List<String> adDetailSaleEndImgList = new ArrayList<>();
    
    

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

	public String getProdReportName() {
		return prodReportName;
	}

	public void setProdReportName(String prodReportName) {
		this.prodReportName = prodReportName;
	}

	public String getProdAdUrl() {
		return prodAdUrl;
	}

	public void setProdAdUrl(String prodAdUrl) {
		this.prodAdUrl = prodAdUrl;
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

	public String getAdDetailLogoTxt() {
		return adDetailLogoTxt;
	}

	public void setAdDetailLogoTxt(String adDetailLogoTxt) {
		this.adDetailLogoTxt = adDetailLogoTxt;
	}

	public String getConvertCountSum() {
		return convertCountSum;
	}

	public void setConvertCountSum(String convertCountSum) {
		this.convertCountSum = convertCountSum;
	}

	public String getConvertPriceCountSum() {
		return convertPriceCountSum;
	}

	public void setConvertPriceCountSum(String convertPriceCountSum) {
		this.convertPriceCountSum = convertPriceCountSum;
	}

	public String getConvertCVR() {
		return convertCVR;
	}

	public void setConvertCVR(String convertCVR) {
		this.convertCVR = convertCVR;
	}

	public String getConvertCost() {
		return convertCost;
	}

	public void setConvertCost(String convertCost) {
		this.convertCost = convertCost;
	}

	public String getConvertInvestmentCost() {
		return convertInvestmentCost;
	}

	public void setConvertInvestmentCost(String convertInvestmentCost) {
		this.convertInvestmentCost = convertInvestmentCost;
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

	public List<String> getAdDetailSaleEndImgList() {
		return adDetailSaleEndImgList;
	}

	public void setAdDetailSaleEndImgList(List<String> adDetailSaleEndImgList) {
		this.adDetailSaleEndImgList = adDetailSaleEndImgList;
	}

}
