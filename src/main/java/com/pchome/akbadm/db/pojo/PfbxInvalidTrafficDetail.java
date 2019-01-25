package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PfbxInvalidTrafficDetail generated by hbm2java
 */
@Entity
@Table(name = "pfbx_invalid_traffic_detail")
public class PfbxInvalidTrafficDetail implements java.io.Serializable {

	private Integer id;
	private PfbxInvalidTraffic pfbxInvalidTraffic;
	private long adClickId;
	private String pfbxPositionId;
	private String adType;
	private Integer recordTime;
	private Integer adClk;
	private Float adPrice;
	private Date updateTime;
	private Date createDate;

	public PfbxInvalidTrafficDetail() {
	}

	public PfbxInvalidTrafficDetail(PfbxInvalidTraffic pfbxInvalidTraffic, long adClickId, Date updateTime,
			Date createDate) {
		this.pfbxInvalidTraffic = pfbxInvalidTraffic;
		this.adClickId = adClickId;
		this.updateTime = updateTime;
		this.createDate = createDate;
	}

	public PfbxInvalidTrafficDetail(PfbxInvalidTraffic pfbxInvalidTraffic, long adClickId, String pfbxPositionId,
			String adType, Integer recordTime, Integer adClk, Float adPrice, Date updateTime, Date createDate) {
		this.pfbxInvalidTraffic = pfbxInvalidTraffic;
		this.adClickId = adClickId;
		this.pfbxPositionId = pfbxPositionId;
		this.adType = adType;
		this.recordTime = recordTime;
		this.adClk = adClk;
		this.adPrice = adPrice;
		this.updateTime = updateTime;
		this.createDate = createDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inv_id", nullable = false)
	public PfbxInvalidTraffic getPfbxInvalidTraffic() {
		return this.pfbxInvalidTraffic;
	}

	public void setPfbxInvalidTraffic(PfbxInvalidTraffic pfbxInvalidTraffic) {
		this.pfbxInvalidTraffic = pfbxInvalidTraffic;
	}

	@Column(name = "ad_click_id", nullable = false)
	public long getAdClickId() {
		return this.adClickId;
	}

	public void setAdClickId(long adClickId) {
		this.adClickId = adClickId;
	}

	@Column(name = "pfbx_position_id", length = 20)
	public String getPfbxPositionId() {
		return this.pfbxPositionId;
	}

	public void setPfbxPositionId(String pfbxPositionId) {
		this.pfbxPositionId = pfbxPositionId;
	}

	@Column(name = "ad_type", length = 1)
	public String getAdType() {
		return this.adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	@Column(name = "record_time")
	public Integer getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Integer recordTime) {
		this.recordTime = recordTime;
	}

	@Column(name = "ad_clk")
	public Integer getAdClk() {
		return this.adClk;
	}

	public void setAdClk(Integer adClk) {
		this.adClk = adClk;
	}

	@Column(name = "ad_price", precision = 10)
	public Float getAdPrice() {
		return this.adPrice;
	}

	public void setAdPrice(Float adPrice) {
		this.adPrice = adPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", nullable = false, length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 19)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
