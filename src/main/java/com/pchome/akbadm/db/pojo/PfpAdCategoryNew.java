package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PfpAdCategoryNew generated by hbm2java
 */
@Entity
@Table(name = "pfp_ad_category_new")
public class PfpAdCategoryNew implements java.io.Serializable {

	private Integer id;
	private String partnerId;
	private String parentId;
	private String code;
	private String name;
	private int level;
	private Date updateDate;
	private Date createDate;
	private Set<PfbxBlockIndustry> pfbxBlockIndustries = new HashSet<PfbxBlockIndustry>(0);
	private Set<PfbxAllowIndustry> pfbxAllowIndustries = new HashSet<PfbxAllowIndustry>(0);

	public PfpAdCategoryNew() {
	}

	public PfpAdCategoryNew(String code, String name, int level, Date updateDate, Date createDate) {
		this.code = code;
		this.name = name;
		this.level = level;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	public PfpAdCategoryNew(String partnerId, String parentId, String code, String name, int level, Date updateDate,
			Date createDate, Set<PfbxBlockIndustry> pfbxBlockIndustries, Set<PfbxAllowIndustry> pfbxAllowIndustries) {
		this.partnerId = partnerId;
		this.parentId = parentId;
		this.code = code;
		this.name = name;
		this.level = level;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.pfbxBlockIndustries = pfbxBlockIndustries;
		this.pfbxAllowIndustries = pfbxAllowIndustries;
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

	@Column(name = "partner_id", length = 20)
	public String getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	@Column(name = "parent_id", length = 20)
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "code", nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "level", nullable = false)
	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpAdCategoryNew")
	public Set<PfbxBlockIndustry> getPfbxBlockIndustries() {
		return this.pfbxBlockIndustries;
	}

	public void setPfbxBlockIndustries(Set<PfbxBlockIndustry> pfbxBlockIndustries) {
		this.pfbxBlockIndustries = pfbxBlockIndustries;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfpAdCategoryNew")
	public Set<PfbxAllowIndustry> getPfbxAllowIndustries() {
		return this.pfbxAllowIndustries;
	}

	public void setPfbxAllowIndustries(Set<PfbxAllowIndustry> pfbxAllowIndustries) {
		this.pfbxAllowIndustries = pfbxAllowIndustries;
	}

}
