package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Sequence generated by hbm2java
 */
@Entity
@Table(name = "sequence")
public class Sequence implements java.io.Serializable {

	private String tableName;
	private String tableChar;
	private String tableDate;
	private int tableNo;
	private Date updateDate;
	private Date createDate;

	public Sequence() {
	}

	public Sequence(String tableName, String tableChar, String tableDate, int tableNo, Date updateDate,
			Date createDate) {
		this.tableName = tableName;
		this.tableChar = tableChar;
		this.tableDate = tableDate;
		this.tableNo = tableNo;
		this.updateDate = updateDate;
		this.createDate = createDate;
	}

	@Id

	@Column(name = "table_name", unique = true, nullable = false, length = 50)
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "table_char", nullable = false, length = 7)
	public String getTableChar() {
		return this.tableChar;
	}

	public void setTableChar(String tableChar) {
		this.tableChar = tableChar;
	}

	@Column(name = "table_date", nullable = false, length = 8)
	public String getTableDate() {
		return this.tableDate;
	}

	public void setTableDate(String tableDate) {
		this.tableDate = tableDate;
	}

	@Column(name = "table_no", nullable = false)
	public int getTableNo() {
		return this.tableNo;
	}

	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
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
