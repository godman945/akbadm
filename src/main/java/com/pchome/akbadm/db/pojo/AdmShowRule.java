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
 * AdmShowRule generated by hbm2java
 */
@Entity
@Table(name = "adm_show_rule")
public class AdmShowRule implements java.io.Serializable {

	private Integer id;
	private String wayType;
	private String pfdCustomerInfoId;
	private String pfpCustomerInfoId;
	private String pfbCustomerInfoId;
	private Date updateDate;
	private Date createDate;

	public AdmShowRule() {
	}

	public AdmShowRule(String wayType, String pfdCustomerInfoId, String pfpCustomerInfoId, String pfbCustomerInfoId,
			Date updateDate, Date createDate) {
		this.wayType = wayType;
		this.pfdCustomerInfoId = pfdCustomerInfoId;
		this.pfpCustomerInfoId = pfpCustomerInfoId;
		this.pfbCustomerInfoId = pfbCustomerInfoId;
		this.updateDate = updateDate;
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

	@Column(name = "way_type", nullable = false, length = 1)
	public String getWayType() {
		return this.wayType;
	}

	public void setWayType(String wayType) {
		this.wayType = wayType;
	}

	@Column(name = "pfd_customer_info_id", nullable = false, length = 20)
	public String getPfdCustomerInfoId() {
		return this.pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	@Column(name = "pfp_customer_info_id", nullable = false, length = 20)
	public String getPfpCustomerInfoId() {
		return this.pfpCustomerInfoId;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}

	@Column(name = "pfb_customer_info_id", nullable = false, length = 20)
	public String getPfbCustomerInfoId() {
		return this.pfbCustomerInfoId;
	}

	public void setPfbCustomerInfoId(String pfbCustomerInfoId) {
		this.pfbCustomerInfoId = pfbCustomerInfoId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", nullable = false, length = 19)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
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
