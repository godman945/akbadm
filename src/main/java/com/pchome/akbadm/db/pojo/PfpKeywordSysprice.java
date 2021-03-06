package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PfpKeywordSysprice generated by hbm2java
 */
@Entity
@Table(name = "pfp_keyword_sysprice")
public class PfpKeywordSysprice implements java.io.Serializable {

	private Integer id;
	private String keyword;
	private float sysprice;
	private int amount;

	public PfpKeywordSysprice() {
	}

	public PfpKeywordSysprice(String keyword, float sysprice, int amount) {
		this.keyword = keyword;
		this.sysprice = sysprice;
		this.amount = amount;
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

	@Column(name = "keyword", nullable = false, length = 50)
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Column(name = "sysprice", nullable = false, precision = 10)
	public float getSysprice() {
		return this.sysprice;
	}

	public void setSysprice(float sysprice) {
		this.sysprice = sysprice;
	}

	@Column(name = "amount", nullable = false)
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
