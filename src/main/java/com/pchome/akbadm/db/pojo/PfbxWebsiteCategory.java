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
 * PfbxWebsiteCategory generated by hbm2java
 */
@Entity
@Table(name = "pfbx_website_category")
public class PfbxWebsiteCategory implements java.io.Serializable {

	private Integer id;
	private String parentId;
	private String code;
	private String name;
	private int level;
	private Date updateDate;
	private Date createDate;
	private Set<PfpAdSpecificWebsite> pfpAdSpecificWebsites = new HashSet<PfpAdSpecificWebsite>(0);

	public PfbxWebsiteCategory() {
	}

	public PfbxWebsiteCategory(String code, String name, int level, Date updateDate, Date createDate) {
		this.code = code;
		this.name = name;
		this.level = level;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	public PfbxWebsiteCategory(String parentId, String code, String name, int level, Date updateDate, Date createDate,
			Set<PfpAdSpecificWebsite> pfpAdSpecificWebsites) {
		this.parentId = parentId;
		this.code = code;
		this.name = name;
		this.level = level;
		this.updateDate = updateDate;
		this.createDate = createDate;
		this.pfpAdSpecificWebsites = pfpAdSpecificWebsites;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfbxWebsiteCategory")
	public Set<PfpAdSpecificWebsite> getPfpAdSpecificWebsites() {
		return this.pfpAdSpecificWebsites;
	}

	public void setPfpAdSpecificWebsites(Set<PfpAdSpecificWebsite> pfpAdSpecificWebsites) {
		this.pfpAdSpecificWebsites = pfpAdSpecificWebsites;
	}

}
