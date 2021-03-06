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
 * PfbxAdUnitReport generated by hbm2java
 */
@Entity
@Table(name = "pfbx_ad_unit_report")
public class PfbxAdUnitReport implements java.io.Serializable {

	private Integer adUnitReportSeq;
	private Date adPvclkDate;
	private String adPvclkUnit;
	private String customerInfoId;
	private int adPv;
	private int adClk;
	private int adInvalidClk;
	private float adPvPrice;
	private float adClkPrice;
	private float adInvalidClkPrice;
	private Date createDate;
	private Date updateDate;

	public PfbxAdUnitReport() {
	}

	public PfbxAdUnitReport(Date adPvclkDate, String customerInfoId, int adPv, int adClk, int adInvalidClk,
			float adPvPrice, float adClkPrice, float adInvalidClkPrice, Date createDate, Date updateDate) {
		this.adPvclkDate = adPvclkDate;
		this.customerInfoId = customerInfoId;
		this.adPv = adPv;
		this.adClk = adClk;
		this.adInvalidClk = adInvalidClk;
		this.adPvPrice = adPvPrice;
		this.adClkPrice = adClkPrice;
		this.adInvalidClkPrice = adInvalidClkPrice;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public PfbxAdUnitReport(Date adPvclkDate, String adPvclkUnit, String customerInfoId, int adPv, int adClk,
			int adInvalidClk, float adPvPrice, float adClkPrice, float adInvalidClkPrice, Date createDate,
			Date updateDate) {
		this.adPvclkDate = adPvclkDate;
		this.adPvclkUnit = adPvclkUnit;
		this.customerInfoId = customerInfoId;
		this.adPv = adPv;
		this.adClk = adClk;
		this.adInvalidClk = adInvalidClk;
		this.adPvPrice = adPvPrice;
		this.adClkPrice = adClkPrice;
		this.adInvalidClkPrice = adInvalidClkPrice;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "ad_unit_report_seq", unique = true, nullable = false)
	public Integer getAdUnitReportSeq() {
		return this.adUnitReportSeq;
	}

	public void setAdUnitReportSeq(Integer adUnitReportSeq) {
		this.adUnitReportSeq = adUnitReportSeq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ad_pvclk_date", nullable = false, length = 10)
	public Date getAdPvclkDate() {
		return this.adPvclkDate;
	}

	public void setAdPvclkDate(Date adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
	}

	@Column(name = "ad_pvclk_unit", length = 20)
	public String getAdPvclkUnit() {
		return this.adPvclkUnit;
	}

	public void setAdPvclkUnit(String adPvclkUnit) {
		this.adPvclkUnit = adPvclkUnit;
	}

	@Column(name = "customer_info_id", nullable = false, length = 20)
	public String getCustomerInfoId() {
		return this.customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
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

	@Column(name = "ad_invalid_clk", nullable = false)
	public int getAdInvalidClk() {
		return this.adInvalidClk;
	}

	public void setAdInvalidClk(int adInvalidClk) {
		this.adInvalidClk = adInvalidClk;
	}

	@Column(name = "ad_pv_price", nullable = false, precision = 10)
	public float getAdPvPrice() {
		return this.adPvPrice;
	}

	public void setAdPvPrice(float adPvPrice) {
		this.adPvPrice = adPvPrice;
	}

	@Column(name = "ad_clk_price", nullable = false, precision = 10)
	public float getAdClkPrice() {
		return this.adClkPrice;
	}

	public void setAdClkPrice(float adClkPrice) {
		this.adClkPrice = adClkPrice;
	}

	@Column(name = "ad_invalid_clk_price", nullable = false, precision = 10)
	public float getAdInvalidClkPrice() {
		return this.adInvalidClkPrice;
	}

	public void setAdInvalidClkPrice(float adInvalidClkPrice) {
		this.adInvalidClkPrice = adInvalidClkPrice;
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
