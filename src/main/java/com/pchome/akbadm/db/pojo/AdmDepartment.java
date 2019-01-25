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
 * AdmDepartment generated by hbm2java
 */
@Entity
@Table(name = "adm_department")
public class AdmDepartment implements java.io.Serializable {

	private Integer depId;
	private String depName;
	private String parentDepId;
	private int sort;
	private Date createDate;
	private Date updateDate;

	public AdmDepartment() {
	}

	public AdmDepartment(String depName, int sort, Date createDate, Date updateDate) {
		this.depName = depName;
		this.sort = sort;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	public AdmDepartment(String depName, String parentDepId, int sort, Date createDate, Date updateDate) {
		this.depName = depName;
		this.parentDepId = parentDepId;
		this.sort = sort;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "dep_id", unique = true, nullable = false)
	public Integer getDepId() {
		return this.depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	@Column(name = "dep_name", nullable = false, length = 20)
	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	@Column(name = "parent_dep_id", length = 4)
	public String getParentDepId() {
		return this.parentDepId;
	}

	public void setParentDepId(String parentDepId) {
		this.parentDepId = parentDepId;
	}

	@Column(name = "sort", nullable = false)
	public int getSort() {
		return this.sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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
