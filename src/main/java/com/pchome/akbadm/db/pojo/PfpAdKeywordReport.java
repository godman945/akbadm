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
 * PfpAdKeywordReport generated by hbm2java
 */
@Entity
@Table(name = "pfp_ad_keyword_report")
public class PfpAdKeywordReport implements java.io.Serializable {

	private Integer adKeywordReportSeq;
	private Date adKeywordPvclkDate;
	private String customerInfoId;
	private String adActionSeq;
	private String adGroupSeq;
	private String adKeywordSeq;
	private int adKeywordType;
	private String adKeyword;
	private int adKeywordPv;
	private int adKeywordPhrasePv;
	private int adKeywordPrecisionPv;
	private int adKeywordClk;
	private int adKeywordPhraseClk;
	private int adKeywordPrecisionClk;
	private int adKeywordInvalidClk;
	private int adKeywordPhraseInvalidClk;
	private int adKeywordPrecisionInvalidClk;
	private float adKeywordPvPrice;
	private float adKeywordClkPrice;
	private float adKeywordPhraseClkPrice;
	private float adKeywordPrecisionClkPrice;
	private float adKeywordInvalidClkPrice;
	private float adKeywordPhraseInvalidClkPrice;
	private float adKeywordPrecisionInvalidClkPrice;
	private String adKeywordPvclkDevice;
	private String adKeywordPvclkOs;
	private Date createDate;
	private Date updateDate;

	public PfpAdKeywordReport() {
	}

	public PfpAdKeywordReport(Date adKeywordPvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq,
			String adKeywordSeq, int adKeywordType, String adKeyword, int adKeywordPv, int adKeywordPhrasePv,
			int adKeywordPrecisionPv, int adKeywordClk, int adKeywordPhraseClk, int adKeywordPrecisionClk,
			int adKeywordInvalidClk, int adKeywordPhraseInvalidClk, int adKeywordPrecisionInvalidClk,
			float adKeywordPvPrice, float adKeywordClkPrice, float adKeywordPhraseClkPrice,
			float adKeywordPrecisionClkPrice, float adKeywordInvalidClkPrice, float adKeywordPhraseInvalidClkPrice,
			float adKeywordPrecisionInvalidClkPrice, Date createDate, Date updateDate) {
		this.adKeywordPvclkDate = adKeywordPvclkDate;
		this.customerInfoId = customerInfoId;
		this.adActionSeq = adActionSeq;
		this.adGroupSeq = adGroupSeq;
		this.adKeywordSeq = adKeywordSeq;
		this.adKeywordType = adKeywordType;
		this.adKeyword = adKeyword;
		this.adKeywordPv = adKeywordPv;
		this.adKeywordPhrasePv = adKeywordPhrasePv;
		this.adKeywordPrecisionPv = adKeywordPrecisionPv;
		this.adKeywordClk = adKeywordClk;
		this.adKeywordPhraseClk = adKeywordPhraseClk;
		this.adKeywordPrecisionClk = adKeywordPrecisionClk;
		this.adKeywordInvalidClk = adKeywordInvalidClk;
		this.adKeywordPhraseInvalidClk = adKeywordPhraseInvalidClk;
		this.adKeywordPrecisionInvalidClk = adKeywordPrecisionInvalidClk;
		this.adKeywordPvPrice = adKeywordPvPrice;
		this.adKeywordClkPrice = adKeywordClkPrice;
		this.adKeywordPhraseClkPrice = adKeywordPhraseClkPrice;
		this.adKeywordPrecisionClkPrice = adKeywordPrecisionClkPrice;
		this.adKeywordInvalidClkPrice = adKeywordInvalidClkPrice;
		this.adKeywordPhraseInvalidClkPrice = adKeywordPhraseInvalidClkPrice;
		this.adKeywordPrecisionInvalidClkPrice = adKeywordPrecisionInvalidClkPrice;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public PfpAdKeywordReport(Date adKeywordPvclkDate, String customerInfoId, String adActionSeq, String adGroupSeq,
			String adKeywordSeq, int adKeywordType, String adKeyword, int adKeywordPv, int adKeywordPhrasePv,
			int adKeywordPrecisionPv, int adKeywordClk, int adKeywordPhraseClk, int adKeywordPrecisionClk,
			int adKeywordInvalidClk, int adKeywordPhraseInvalidClk, int adKeywordPrecisionInvalidClk,
			float adKeywordPvPrice, float adKeywordClkPrice, float adKeywordPhraseClkPrice,
			float adKeywordPrecisionClkPrice, float adKeywordInvalidClkPrice, float adKeywordPhraseInvalidClkPrice,
			float adKeywordPrecisionInvalidClkPrice, String adKeywordPvclkDevice, String adKeywordPvclkOs,
			Date createDate, Date updateDate) {
		this.adKeywordPvclkDate = adKeywordPvclkDate;
		this.customerInfoId = customerInfoId;
		this.adActionSeq = adActionSeq;
		this.adGroupSeq = adGroupSeq;
		this.adKeywordSeq = adKeywordSeq;
		this.adKeywordType = adKeywordType;
		this.adKeyword = adKeyword;
		this.adKeywordPv = adKeywordPv;
		this.adKeywordPhrasePv = adKeywordPhrasePv;
		this.adKeywordPrecisionPv = adKeywordPrecisionPv;
		this.adKeywordClk = adKeywordClk;
		this.adKeywordPhraseClk = adKeywordPhraseClk;
		this.adKeywordPrecisionClk = adKeywordPrecisionClk;
		this.adKeywordInvalidClk = adKeywordInvalidClk;
		this.adKeywordPhraseInvalidClk = adKeywordPhraseInvalidClk;
		this.adKeywordPrecisionInvalidClk = adKeywordPrecisionInvalidClk;
		this.adKeywordPvPrice = adKeywordPvPrice;
		this.adKeywordClkPrice = adKeywordClkPrice;
		this.adKeywordPhraseClkPrice = adKeywordPhraseClkPrice;
		this.adKeywordPrecisionClkPrice = adKeywordPrecisionClkPrice;
		this.adKeywordInvalidClkPrice = adKeywordInvalidClkPrice;
		this.adKeywordPhraseInvalidClkPrice = adKeywordPhraseInvalidClkPrice;
		this.adKeywordPrecisionInvalidClkPrice = adKeywordPrecisionInvalidClkPrice;
		this.adKeywordPvclkDevice = adKeywordPvclkDevice;
		this.adKeywordPvclkOs = adKeywordPvclkOs;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ad_keyword_report_seq", unique = true, nullable = false)
	public Integer getAdKeywordReportSeq() {
		return this.adKeywordReportSeq;
	}

	public void setAdKeywordReportSeq(Integer adKeywordReportSeq) {
		this.adKeywordReportSeq = adKeywordReportSeq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ad_keyword_pvclk_date", nullable = false, length = 10)
	public Date getAdKeywordPvclkDate() {
		return this.adKeywordPvclkDate;
	}

	public void setAdKeywordPvclkDate(Date adKeywordPvclkDate) {
		this.adKeywordPvclkDate = adKeywordPvclkDate;
	}

	@Column(name = "customer_info_id", nullable = false, length = 20)
	public String getCustomerInfoId() {
		return this.customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	@Column(name = "ad_action_seq", nullable = false, length = 20)
	public String getAdActionSeq() {
		return this.adActionSeq;
	}

	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}

	@Column(name = "ad_group_seq", nullable = false, length = 20)
	public String getAdGroupSeq() {
		return this.adGroupSeq;
	}

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}

	@Column(name = "ad_keyword_seq", nullable = false, length = 20)
	public String getAdKeywordSeq() {
		return this.adKeywordSeq;
	}

	public void setAdKeywordSeq(String adKeywordSeq) {
		this.adKeywordSeq = adKeywordSeq;
	}

	@Column(name = "ad_keyword_type", nullable = false)
	public int getAdKeywordType() {
		return this.adKeywordType;
	}

	public void setAdKeywordType(int adKeywordType) {
		this.adKeywordType = adKeywordType;
	}

	@Column(name = "ad_keyword", nullable = false, length = 50)
	public String getAdKeyword() {
		return this.adKeyword;
	}

	public void setAdKeyword(String adKeyword) {
		this.adKeyword = adKeyword;
	}

	@Column(name = "ad_keyword_pv", nullable = false)
	public int getAdKeywordPv() {
		return this.adKeywordPv;
	}

	public void setAdKeywordPv(int adKeywordPv) {
		this.adKeywordPv = adKeywordPv;
	}

	@Column(name = "ad_keyword_phrase_pv", nullable = false)
	public int getAdKeywordPhrasePv() {
		return this.adKeywordPhrasePv;
	}

	public void setAdKeywordPhrasePv(int adKeywordPhrasePv) {
		this.adKeywordPhrasePv = adKeywordPhrasePv;
	}

	@Column(name = "ad_keyword_precision_pv", nullable = false)
	public int getAdKeywordPrecisionPv() {
		return this.adKeywordPrecisionPv;
	}

	public void setAdKeywordPrecisionPv(int adKeywordPrecisionPv) {
		this.adKeywordPrecisionPv = adKeywordPrecisionPv;
	}

	@Column(name = "ad_keyword_clk", nullable = false)
	public int getAdKeywordClk() {
		return this.adKeywordClk;
	}

	public void setAdKeywordClk(int adKeywordClk) {
		this.adKeywordClk = adKeywordClk;
	}

	@Column(name = "ad_keyword_phrase_clk", nullable = false)
	public int getAdKeywordPhraseClk() {
		return this.adKeywordPhraseClk;
	}

	public void setAdKeywordPhraseClk(int adKeywordPhraseClk) {
		this.adKeywordPhraseClk = adKeywordPhraseClk;
	}

	@Column(name = "ad_keyword_precision_clk", nullable = false)
	public int getAdKeywordPrecisionClk() {
		return this.adKeywordPrecisionClk;
	}

	public void setAdKeywordPrecisionClk(int adKeywordPrecisionClk) {
		this.adKeywordPrecisionClk = adKeywordPrecisionClk;
	}

	@Column(name = "ad_keyword_invalid_clk", nullable = false)
	public int getAdKeywordInvalidClk() {
		return this.adKeywordInvalidClk;
	}

	public void setAdKeywordInvalidClk(int adKeywordInvalidClk) {
		this.adKeywordInvalidClk = adKeywordInvalidClk;
	}

	@Column(name = "ad_keyword_phrase_invalid_clk", nullable = false)
	public int getAdKeywordPhraseInvalidClk() {
		return this.adKeywordPhraseInvalidClk;
	}

	public void setAdKeywordPhraseInvalidClk(int adKeywordPhraseInvalidClk) {
		this.adKeywordPhraseInvalidClk = adKeywordPhraseInvalidClk;
	}

	@Column(name = "ad_keyword_precision_invalid_clk", nullable = false)
	public int getAdKeywordPrecisionInvalidClk() {
		return this.adKeywordPrecisionInvalidClk;
	}

	public void setAdKeywordPrecisionInvalidClk(int adKeywordPrecisionInvalidClk) {
		this.adKeywordPrecisionInvalidClk = adKeywordPrecisionInvalidClk;
	}

	@Column(name = "ad_keyword_pv_price", nullable = false, precision = 10)
	public float getAdKeywordPvPrice() {
		return this.adKeywordPvPrice;
	}

	public void setAdKeywordPvPrice(float adKeywordPvPrice) {
		this.adKeywordPvPrice = adKeywordPvPrice;
	}

	@Column(name = "ad_keyword_clk_price", nullable = false, precision = 10)
	public float getAdKeywordClkPrice() {
		return this.adKeywordClkPrice;
	}

	public void setAdKeywordClkPrice(float adKeywordClkPrice) {
		this.adKeywordClkPrice = adKeywordClkPrice;
	}

	@Column(name = "ad_keyword_phrase_clk_price", nullable = false, precision = 10)
	public float getAdKeywordPhraseClkPrice() {
		return this.adKeywordPhraseClkPrice;
	}

	public void setAdKeywordPhraseClkPrice(float adKeywordPhraseClkPrice) {
		this.adKeywordPhraseClkPrice = adKeywordPhraseClkPrice;
	}

	@Column(name = "ad_keyword_precision_clk_price", nullable = false, precision = 10)
	public float getAdKeywordPrecisionClkPrice() {
		return this.adKeywordPrecisionClkPrice;
	}

	public void setAdKeywordPrecisionClkPrice(float adKeywordPrecisionClkPrice) {
		this.adKeywordPrecisionClkPrice = adKeywordPrecisionClkPrice;
	}

	@Column(name = "ad_keyword_invalid_clk_price", nullable = false, precision = 10)
	public float getAdKeywordInvalidClkPrice() {
		return this.adKeywordInvalidClkPrice;
	}

	public void setAdKeywordInvalidClkPrice(float adKeywordInvalidClkPrice) {
		this.adKeywordInvalidClkPrice = adKeywordInvalidClkPrice;
	}

	@Column(name = "ad_keyword_phrase_invalid_clk_price", nullable = false, precision = 10)
	public float getAdKeywordPhraseInvalidClkPrice() {
		return this.adKeywordPhraseInvalidClkPrice;
	}

	public void setAdKeywordPhraseInvalidClkPrice(float adKeywordPhraseInvalidClkPrice) {
		this.adKeywordPhraseInvalidClkPrice = adKeywordPhraseInvalidClkPrice;
	}

	@Column(name = "ad_keyword_precision_invalid_clk_price", nullable = false, precision = 10)
	public float getAdKeywordPrecisionInvalidClkPrice() {
		return this.adKeywordPrecisionInvalidClkPrice;
	}

	public void setAdKeywordPrecisionInvalidClkPrice(float adKeywordPrecisionInvalidClkPrice) {
		this.adKeywordPrecisionInvalidClkPrice = adKeywordPrecisionInvalidClkPrice;
	}

	@Column(name = "ad_keyword_pvclk_device", length = 20)
	public String getAdKeywordPvclkDevice() {
		return this.adKeywordPvclkDevice;
	}

	public void setAdKeywordPvclkDevice(String adKeywordPvclkDevice) {
		this.adKeywordPvclkDevice = adKeywordPvclkDevice;
	}

	@Column(name = "ad_keyword_pvclk_os", length = 20)
	public String getAdKeywordPvclkOs() {
		return this.adKeywordPvclkOs;
	}

	public void setAdKeywordPvclkOs(String adKeywordPvclkOs) {
		this.adKeywordPvclkOs = adKeywordPvclkOs;
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
