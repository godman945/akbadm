package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PfpUser generated by hbm2java
 */
@Entity
@Table(name = "pfp_user")
public class PfpUser implements java.io.Serializable {

	private String userId;
	private PfpCustomerInfo pfpCustomerInfo;
	private int privilegeId;
	private String userName;
	private String userEmail;
	private String status;
	private Date lastLoginDate;
	private String lastLoginIp;
	private Date inviteDate;
	private Date activateDate;
	private Date createDate;
	private Date updateDate;
	private Set<PfpUserMemberRef> pfpUserMemberRefs = new HashSet<PfpUserMemberRef>(0);
	private Set<PfpOrder> pfpOrders = new HashSet<PfpOrder>(0);
	private Set<PfdUserAdAccountRef> pfdUserAdAccountRefs = new HashSet<PfdUserAdAccountRef>(0);

	public PfpUser() {
	}

	public PfpUser(String userId, int privilegeId, String userEmail, String status, Date inviteDate, Date createDate,
			Date updateDate) {
		this.userId = userId;
		this.privilegeId = privilegeId;
		this.userEmail = userEmail;
		this.status = status;
		this.inviteDate = inviteDate;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public PfpUser(String userId, PfpCustomerInfo pfpCustomerInfo, int privilegeId, String userName, String userEmail,
			String status, Date lastLoginDate, String lastLoginIp, Date inviteDate, Date activateDate, Date createDate,
			Date updateDate, Set<PfpUserMemberRef> pfpUserMemberRefs, Set<PfpOrder> pfpOrders,
			Set<PfdUserAdAccountRef> pfdUserAdAccountRefs) {
		this.userId = userId;
		this.pfpCustomerInfo = pfpCustomerInfo;
		this.privilegeId = privilegeId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.status = status;
		this.lastLoginDate = lastLoginDate;
		this.lastLoginIp = lastLoginIp;
		this.inviteDate = inviteDate;
		this.activateDate = activateDate;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.pfpUserMemberRefs = pfpUserMemberRefs;
		this.pfpOrders = pfpOrders;
		this.pfdUserAdAccountRefs = pfdUserAdAccountRefs;
	}

	@Id

	@Column(name = "user_id", unique = true, nullable = false, length = 20)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_info_id")
	public PfpCustomerInfo getPfpCustomerInfo() {
		return this.pfpCustomerInfo;
	}

	public void setPfpCustomerInfo(PfpCustomerInfo pfpCustomerInfo) {
		this.pfpCustomerInfo = pfpCustomerInfo;
	}

	@Column(name = "privilege_id", nullable = false)
	public int getPrivilegeId() {
		return this.privilegeId;
	}

	public void setPrivilegeId(int privilegeId) {
		this.privilegeId = privilegeId;
	}

	@Column(name = "user_name", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_email", nullable = false, length = 50)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "status", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_login_date", length = 19)
	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Column(name = "last_login_ip", length = 20)
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "invite_date", nullable = false, length = 10)
	public Date getInviteDate() {
		return this.inviteDate;
	}

	public void setInviteDate(Date inviteDate) {
		this.inviteDate = inviteDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "activate_date", length = 19)
	public Date getActivateDate() {
		return this.activateDate;
	}

	public void setActivateDate(Date activateDate) {
		this.activateDate = activateDate;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpUser")
	public Set<PfpUserMemberRef> getPfpUserMemberRefs() {
		return this.pfpUserMemberRefs;
	}

	public void setPfpUserMemberRefs(Set<PfpUserMemberRef> pfpUserMemberRefs) {
		this.pfpUserMemberRefs = pfpUserMemberRefs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpUser")
	public Set<PfpOrder> getPfpOrders() {
		return this.pfpOrders;
	}

	public void setPfpOrders(Set<PfpOrder> pfpOrders) {
		this.pfpOrders = pfpOrders;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpUser")
	public Set<PfdUserAdAccountRef> getPfdUserAdAccountRefs() {
		return this.pfdUserAdAccountRefs;
	}

	public void setPfdUserAdAccountRefs(Set<PfdUserAdAccountRef> pfdUserAdAccountRefs) {
		this.pfdUserAdAccountRefs = pfdUserAdAccountRefs;
	}

}
