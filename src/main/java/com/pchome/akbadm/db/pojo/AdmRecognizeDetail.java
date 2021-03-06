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
 * AdmRecognizeDetail generated by hbm2java
 */
@Entity
@Table(name = "adm_recognize_detail")
public class AdmRecognizeDetail implements java.io.Serializable {

	private Integer recordDetailId;
	private AdmRecognizeRecord admRecognizeRecord;
	private String customerInfoId;
	private String pfdUserId;
	private String pfdCustomerInfoId;
	private float costPrice;
	private float tax;
	private String orderType;
	private Date costDate;
	private float recordRemain;
	private float taxRemain;
	private String note;
	private Date updateDate;
	private Date createDate;

	public AdmRecognizeDetail() {
	}

	public AdmRecognizeDetail(AdmRecognizeRecord admRecognizeRecord, String customerInfoId, float costPrice, float tax,
			String orderType, Date costDate, float recordRemain, float taxRemain, Date updateDate, Date createDate) {
		this.admRecognizeRecord = admRecognizeRecord;
		this.customerInfoId = customerInfoId;
		this.costPrice = costPrice;
		this.tax = tax;
		this.orderType = orderType;
		this.costDate = costDate;
		this.recordRemain = recordRemain;
		this.taxRemain = taxRemain;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	public AdmRecognizeDetail(AdmRecognizeRecord admRecognizeRecord, String customerInfoId, String pfdUserId,
			String pfdCustomerInfoId, float costPrice, float tax, String orderType, Date costDate, float recordRemain,
			float taxRemain, String note, Date updateDate, Date createDate) {
		this.admRecognizeRecord = admRecognizeRecord;
		this.customerInfoId = customerInfoId;
		this.pfdUserId = pfdUserId;
		this.pfdCustomerInfoId = pfdCustomerInfoId;
		this.costPrice = costPrice;
		this.tax = tax;
		this.orderType = orderType;
		this.costDate = costDate;
		this.recordRemain = recordRemain;
		this.taxRemain = taxRemain;
		this.note = note;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "record_detail_id", unique = true, nullable = false)
	public Integer getRecordDetailId() {
		return this.recordDetailId;
	}

	public void setRecordDetailId(Integer recordDetailId) {
		this.recordDetailId = recordDetailId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recognize_record_id", nullable = false)
	public AdmRecognizeRecord getAdmRecognizeRecord() {
		return this.admRecognizeRecord;
	}

	public void setAdmRecognizeRecord(AdmRecognizeRecord admRecognizeRecord) {
		this.admRecognizeRecord = admRecognizeRecord;
	}

	@Column(name = "customer_info_id", nullable = false, length = 20)
	public String getCustomerInfoId() {
		return this.customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	@Column(name = "pfd_user_id", length = 20)
	public String getPfdUserId() {
		return this.pfdUserId;
	}

	public void setPfdUserId(String pfdUserId) {
		this.pfdUserId = pfdUserId;
	}

	@Column(name = "pfd_customer_info_id", length = 20)
	public String getPfdCustomerInfoId() {
		return this.pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	@Column(name = "cost_price", nullable = false, precision = 10)
	public float getCostPrice() {
		return this.costPrice;
	}

	public void setCostPrice(float costPrice) {
		this.costPrice = costPrice;
	}

	@Column(name = "tax", nullable = false, precision = 10)
	public float getTax() {
		return this.tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	@Column(name = "order_type", nullable = false, length = 1)
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "cost_date", nullable = false, length = 10)
	public Date getCostDate() {
		return this.costDate;
	}

	public void setCostDate(Date costDate) {
		this.costDate = costDate;
	}

	@Column(name = "record_remain", nullable = false, precision = 10)
	public float getRecordRemain() {
		return this.recordRemain;
	}

	public void setRecordRemain(float recordRemain) {
		this.recordRemain = recordRemain;
	}

	@Column(name = "tax_remain", nullable = false, precision = 10)
	public float getTaxRemain() {
		return this.taxRemain;
	}

	public void setTaxRemain(float taxRemain) {
		this.taxRemain = taxRemain;
	}

	@Column(name = "note", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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
