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
 * PfbxPermission generated by hbm2java
 */
@Entity
@Table(name = "pfbx_permission")
public class PfbxPermission implements java.io.Serializable {

	private Integer id;
	private PfbxCustomerInfo pfbxCustomerInfo;
	private int type;
	private String pfbxApplyId;
	private String content;
	private int status;
	private String remark;
	private Date creatDate;

	public PfbxPermission() {
	}

	public PfbxPermission(PfbxCustomerInfo pfbxCustomerInfo, int type, String pfbxApplyId, int status, Date creatDate) {
		this.pfbxCustomerInfo = pfbxCustomerInfo;
		this.type = type;
		this.pfbxApplyId = pfbxApplyId;
		this.status = status;
		this.creatDate = creatDate;
	}

	public PfbxPermission(PfbxCustomerInfo pfbxCustomerInfo, int type, String pfbxApplyId, String content, int status,
			String remark, Date creatDate) {
		this.pfbxCustomerInfo = pfbxCustomerInfo;
		this.type = type;
		this.pfbxApplyId = pfbxApplyId;
		this.content = content;
		this.status = status;
		this.remark = remark;
		this.creatDate = creatDate;
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
	@JoinColumn(name = "customer_info_id", nullable = false)
	public PfbxCustomerInfo getPfbxCustomerInfo() {
		return this.pfbxCustomerInfo;
	}

	public void setPfbxCustomerInfo(PfbxCustomerInfo pfbxCustomerInfo) {
		this.pfbxCustomerInfo = pfbxCustomerInfo;
	}

	@Column(name = "type", nullable = false)
	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "pfbx_apply_id", nullable = false, length = 50)
	public String getPfbxApplyId() {
		return this.pfbxApplyId;
	}

	public void setPfbxApplyId(String pfbxApplyId) {
		this.pfbxApplyId = pfbxApplyId;
	}

	@Column(name = "content", length = 1024)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status", nullable = false)
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "remark", length = 1024)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creat_date", nullable = false, length = 19)
	public Date getCreatDate() {
		return this.creatDate;
	}

	public void setCreatDate(Date creatDate) {
		this.creatDate = creatDate;
	}

}
