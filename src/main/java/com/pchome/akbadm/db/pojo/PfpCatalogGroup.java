package com.pchome.akbadm.db.pojo;
// Generated 2018/11/28 �U�� 02:59:23 by Hibernate Tools 3.4.0.CR1

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
 * PfpCatalogGroup generated by hbm2java
 */
@Entity
@Table(name = "pfp_catalog_group")
public class PfpCatalogGroup implements java.io.Serializable {

	private String catalogGroupSeq;
	private PfpCatalog pfpCatalog;
	private String catalogGroupName;
	private String catalogGroupDeleteStatus;
	private Date updateDate;
	private Date createDate;
	private Set<PfpCatalogGroupItem> pfpCatalogGroupItems = new HashSet<PfpCatalogGroupItem>(0);

	public PfpCatalogGroup() {
	}

	public PfpCatalogGroup(String catalogGroupSeq, PfpCatalog pfpCatalog, String catalogGroupName,
			String catalogGroupDeleteStatus, Date updateDate, Date createDate) {
		this.catalogGroupSeq = catalogGroupSeq;
		this.pfpCatalog = pfpCatalog;
		this.catalogGroupName = catalogGroupName;
		this.catalogGroupDeleteStatus = catalogGroupDeleteStatus;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	public PfpCatalogGroup(String catalogGroupSeq, PfpCatalog pfpCatalog, String catalogGroupName,
			String catalogGroupDeleteStatus, Date updateDate, Date createDate,
			Set<PfpCatalogGroupItem> pfpCatalogGroupItems) {
		this.catalogGroupSeq = catalogGroupSeq;
		this.pfpCatalog = pfpCatalog;
		this.catalogGroupName = catalogGroupName;
		this.catalogGroupDeleteStatus = catalogGroupDeleteStatus;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.pfpCatalogGroupItems = pfpCatalogGroupItems;
	}

	@Id

	@Column(name = "catalog_group_seq", unique = true, nullable = false, length = 20)
	public String getCatalogGroupSeq() {
		return this.catalogGroupSeq;
	}

	public void setCatalogGroupSeq(String catalogGroupSeq) {
		this.catalogGroupSeq = catalogGroupSeq;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catalog_seq", nullable = false)
	public PfpCatalog getPfpCatalog() {
		return this.pfpCatalog;
	}

	public void setPfpCatalog(PfpCatalog pfpCatalog) {
		this.pfpCatalog = pfpCatalog;
	}

	@Column(name = "catalog_group_name", nullable = false, length = 20)
	public String getCatalogGroupName() {
		return this.catalogGroupName;
	}

	public void setCatalogGroupName(String catalogGroupName) {
		this.catalogGroupName = catalogGroupName;
	}

	@Column(name = "catalog_group_delete_status", nullable = false, length = 1)
	public String getCatalogGroupDeleteStatus() {
		return this.catalogGroupDeleteStatus;
	}

	public void setCatalogGroupDeleteStatus(String catalogGroupDeleteStatus) {
		this.catalogGroupDeleteStatus = catalogGroupDeleteStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", nullable = false, length = 0)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 0)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpCatalogGroup")
	public Set<PfpCatalogGroupItem> getPfpCatalogGroupItems() {
		return this.pfpCatalogGroupItems;
	}

	public void setPfpCatalogGroupItems(Set<PfpCatalogGroupItem> pfpCatalogGroupItems) {
		this.pfpCatalogGroupItems = pfpCatalogGroupItems;
	}

}