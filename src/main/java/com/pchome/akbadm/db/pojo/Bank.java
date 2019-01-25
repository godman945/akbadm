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
 * Bank generated by hbm2java
 */
@Entity
@Table(name = "bank")
public class Bank implements java.io.Serializable {

	private Integer id;
	private String bankName;
	private String bankCode;
	private String parentBank;
	private Date createDate;
	private Date updateDate;

	public Bank() {
	}

	public Bank(String bankName, String bankCode, Date createDate, Date updateDate) {
		this.bankName = bankName;
		this.bankCode = bankCode;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public Bank(String bankName, String bankCode, String parentBank, Date createDate, Date updateDate) {
		this.bankName = bankName;
		this.bankCode = bankCode;
		this.parentBank = parentBank;
		this.createDate = createDate;
		this.updateDate = updateDate;
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

	@Column(name = "bank_name", nullable = false, length = 50)
	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "bank_code", nullable = false, length = 5)
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "parent_bank", length = 5)
	public String getParentBank() {
		return this.parentBank;
	}

	public void setParentBank(String parentBank) {
		this.parentBank = parentBank;
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
