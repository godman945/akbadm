package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PfdAdVideoReport generated by hbm2java
 */
@Entity
@Table(name = "pfd_ad_video_report")
public class PfdAdVideoReport implements java.io.Serializable {

	private Integer adVideoReportSeq;
	private Date adVideoDate;
	private int adVideoTime;
	private String pfdCustomerInfoId;
	private String pfdUserId;
	private String pfpCustomerInfoId;
	private String adSeq;
	private String templateProductSeq;
	private String adPriceType;
	private float adPrice;
	private String adPvclkDevice;
	private int adVpv;
	private int adPv;
	private int adClk;
	private int adView;
	private int adVideoPlay;
	private int adVideoMusic;
	private int adVideoReplay;
	private int adVideoProcess25;
	private int adVideoProcess50;
	private int adVideoProcess75;
	private int adVideoProcess100;
	private int adVideoUniq;
	private Date createDate;
	private Date updateDate;

	public PfdAdVideoReport() {
	}

	public PfdAdVideoReport(Date adVideoDate, int adVideoTime, String pfdCustomerInfoId, String pfpCustomerInfoId,
			String adSeq, String templateProductSeq, String adPriceType, float adPrice, String adPvclkDevice, int adVpv,
			int adPv, int adClk, int adView, int adVideoPlay, int adVideoMusic, int adVideoReplay, int adVideoProcess25,
			int adVideoProcess50, int adVideoProcess75, int adVideoProcess100, int adVideoUniq, Date createDate,
			Date updateDate) {
		this.adVideoDate = adVideoDate;
		this.adVideoTime = adVideoTime;
		this.pfdCustomerInfoId = pfdCustomerInfoId;
		this.pfpCustomerInfoId = pfpCustomerInfoId;
		this.adSeq = adSeq;
		this.templateProductSeq = templateProductSeq;
		this.adPriceType = adPriceType;
		this.adPrice = adPrice;
		this.adPvclkDevice = adPvclkDevice;
		this.adVpv = adVpv;
		this.adPv = adPv;
		this.adClk = adClk;
		this.adView = adView;
		this.adVideoPlay = adVideoPlay;
		this.adVideoMusic = adVideoMusic;
		this.adVideoReplay = adVideoReplay;
		this.adVideoProcess25 = adVideoProcess25;
		this.adVideoProcess50 = adVideoProcess50;
		this.adVideoProcess75 = adVideoProcess75;
		this.adVideoProcess100 = adVideoProcess100;
		this.adVideoUniq = adVideoUniq;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public PfdAdVideoReport(Date adVideoDate, int adVideoTime, String pfdCustomerInfoId, String pfdUserId,
			String pfpCustomerInfoId, String adSeq, String templateProductSeq, String adPriceType, float adPrice,
			String adPvclkDevice, int adVpv, int adPv, int adClk, int adView, int adVideoPlay, int adVideoMusic,
			int adVideoReplay, int adVideoProcess25, int adVideoProcess50, int adVideoProcess75, int adVideoProcess100,
			int adVideoUniq, Date createDate, Date updateDate) {
		this.adVideoDate = adVideoDate;
		this.adVideoTime = adVideoTime;
		this.pfdCustomerInfoId = pfdCustomerInfoId;
		this.pfdUserId = pfdUserId;
		this.pfpCustomerInfoId = pfpCustomerInfoId;
		this.adSeq = adSeq;
		this.templateProductSeq = templateProductSeq;
		this.adPriceType = adPriceType;
		this.adPrice = adPrice;
		this.adPvclkDevice = adPvclkDevice;
		this.adVpv = adVpv;
		this.adPv = adPv;
		this.adClk = adClk;
		this.adView = adView;
		this.adVideoPlay = adVideoPlay;
		this.adVideoMusic = adVideoMusic;
		this.adVideoReplay = adVideoReplay;
		this.adVideoProcess25 = adVideoProcess25;
		this.adVideoProcess50 = adVideoProcess50;
		this.adVideoProcess75 = adVideoProcess75;
		this.adVideoProcess100 = adVideoProcess100;
		this.adVideoUniq = adVideoUniq;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ad_video_report_seq", unique = true, nullable = false)
	public Integer getAdVideoReportSeq() {
		return this.adVideoReportSeq;
	}

	public void setAdVideoReportSeq(Integer adVideoReportSeq) {
		this.adVideoReportSeq = adVideoReportSeq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ad_video_date", nullable = false, length = 10)
	public Date getAdVideoDate() {
		return this.adVideoDate;
	}

	public void setAdVideoDate(Date adVideoDate) {
		this.adVideoDate = adVideoDate;
	}

	@Column(name = "ad_video_time", nullable = false)
	public int getAdVideoTime() {
		return this.adVideoTime;
	}

	public void setAdVideoTime(int adVideoTime) {
		this.adVideoTime = adVideoTime;
	}

	@Column(name = "pfd_customer_info_id", nullable = false, length = 20)
	public String getPfdCustomerInfoId() {
		return this.pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	@Column(name = "pfd_user_id", length = 20)
	public String getPfdUserId() {
		return this.pfdUserId;
	}

	public void setPfdUserId(String pfdUserId) {
		this.pfdUserId = pfdUserId;
	}

	@Column(name = "pfp_customer_info_id", nullable = false, length = 20)
	public String getPfpCustomerInfoId() {
		return this.pfpCustomerInfoId;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}

	@Column(name = "ad_seq", nullable = false, length = 20)
	public String getAdSeq() {
		return this.adSeq;
	}

	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}

	@Column(name = "template_product_seq", nullable = false, length = 50)
	public String getTemplateProductSeq() {
		return this.templateProductSeq;
	}

	public void setTemplateProductSeq(String templateProductSeq) {
		this.templateProductSeq = templateProductSeq;
	}

	@Column(name = "ad_price_type", nullable = false, length = 3)
	public String getAdPriceType() {
		return this.adPriceType;
	}

	public void setAdPriceType(String adPriceType) {
		this.adPriceType = adPriceType;
	}

	@Column(name = "ad_price", nullable = false, precision = 10, scale = 3)
	public float getAdPrice() {
		return this.adPrice;
	}

	public void setAdPrice(float adPrice) {
		this.adPrice = adPrice;
	}

	@Column(name = "ad_pvclk_device", nullable = false, length = 20)
	public String getAdPvclkDevice() {
		return this.adPvclkDevice;
	}

	public void setAdPvclkDevice(String adPvclkDevice) {
		this.adPvclkDevice = adPvclkDevice;
	}

	@Column(name = "ad_vpv", nullable = false)
	public int getAdVpv() {
		return this.adVpv;
	}

	public void setAdVpv(int adVpv) {
		this.adVpv = adVpv;
	}

	@Column(name = "ad_pv", nullable = false)
	public int getAdPv() {
		return this.adPv;
	}

	public void setAdPv(int adPv) {
		this.adPv = adPv;
	}

	@Column(name = "ad_clk", nullable = false)
	public int getAdClk() {
		return this.adClk;
	}

	public void setAdClk(int adClk) {
		this.adClk = adClk;
	}

	@Column(name = "ad_view", nullable = false)
	public int getAdView() {
		return this.adView;
	}

	public void setAdView(int adView) {
		this.adView = adView;
	}

	@Column(name = "ad_video_play", nullable = false)
	public int getAdVideoPlay() {
		return this.adVideoPlay;
	}

	public void setAdVideoPlay(int adVideoPlay) {
		this.adVideoPlay = adVideoPlay;
	}

	@Column(name = "ad_video_music", nullable = false)
	public int getAdVideoMusic() {
		return this.adVideoMusic;
	}

	public void setAdVideoMusic(int adVideoMusic) {
		this.adVideoMusic = adVideoMusic;
	}

	@Column(name = "ad_video_replay", nullable = false)
	public int getAdVideoReplay() {
		return this.adVideoReplay;
	}

	public void setAdVideoReplay(int adVideoReplay) {
		this.adVideoReplay = adVideoReplay;
	}

	@Column(name = "ad_video_process_25", nullable = false)
	public int getAdVideoProcess25() {
		return this.adVideoProcess25;
	}

	public void setAdVideoProcess25(int adVideoProcess25) {
		this.adVideoProcess25 = adVideoProcess25;
	}

	@Column(name = "ad_video_process_50", nullable = false)
	public int getAdVideoProcess50() {
		return this.adVideoProcess50;
	}

	public void setAdVideoProcess50(int adVideoProcess50) {
		this.adVideoProcess50 = adVideoProcess50;
	}

	@Column(name = "ad_video_process_75", nullable = false)
	public int getAdVideoProcess75() {
		return this.adVideoProcess75;
	}

	public void setAdVideoProcess75(int adVideoProcess75) {
		this.adVideoProcess75 = adVideoProcess75;
	}

	@Column(name = "ad_video_process_100", nullable = false)
	public int getAdVideoProcess100() {
		return this.adVideoProcess100;
	}

	public void setAdVideoProcess100(int adVideoProcess100) {
		this.adVideoProcess100 = adVideoProcess100;
	}

	@Column(name = "ad_video_uniq", nullable = false)
	public int getAdVideoUniq() {
		return this.adVideoUniq;
	}

	public void setAdVideoUniq(int adVideoUniq) {
		this.adVideoUniq = adVideoUniq;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", nullable = false, length = 19)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
