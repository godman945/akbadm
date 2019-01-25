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
 * PfpChannelAdmin generated by hbm2java
 */
@Entity
@Table(name = "pfp_channel_admin")
public class PfpChannelAdmin implements java.io.Serializable {

	private Integer id;
	private String memberId;
	private String adminName;
	private String adminIp;
	private Date updateDate;
	private Date createDate;

	public PfpChannelAdmin() {
	}

	public PfpChannelAdmin(String memberId, String adminName, String adminIp, Date updateDate, Date createDate) {
		this.memberId = memberId;
		this.adminName = adminName;
		this.adminIp = adminIp;
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

	@Column(name = "member_id", nullable = false, length = 20)
	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Column(name = "admin_name", nullable = false, length = 20)
	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	@Column(name = "admin_ip", nullable = false, length = 20)
	public String getAdminIp() {
		return this.adminIp;
	}

	public void setAdminIp(String adminIp) {
		this.adminIp = adminIp;
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
