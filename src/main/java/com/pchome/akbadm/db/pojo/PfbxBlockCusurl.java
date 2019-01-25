package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * PfbxBlockCusurl generated by hbm2java
 */
@Entity
@Table(name = "pfbx_block_cusurl")
public class PfbxBlockCusurl implements java.io.Serializable {

	private String cuId;
	private PfbxUserOption pfbxUserOption;
	private String url;
	private String boardMesg;

	public PfbxBlockCusurl() {
	}

	public PfbxBlockCusurl(String cuId, PfbxUserOption pfbxUserOption, String url) {
		this.cuId = cuId;
		this.pfbxUserOption = pfbxUserOption;
		this.url = url;
	}

	public PfbxBlockCusurl(String cuId, PfbxUserOption pfbxUserOption, String url, String boardMesg) {
		this.cuId = cuId;
		this.pfbxUserOption = pfbxUserOption;
		this.url = url;
		this.boardMesg = boardMesg;
	}

	@Id

	@Column(name = "cu_id", unique = true, nullable = false, length = 20)
	public String getCuId() {
		return this.cuId;
	}

	public void setCuId(String cuId) {
		this.cuId = cuId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "o_id", nullable = false)
	public PfbxUserOption getPfbxUserOption() {
		return this.pfbxUserOption;
	}

	public void setPfbxUserOption(PfbxUserOption pfbxUserOption) {
		this.pfbxUserOption = pfbxUserOption;
	}

	@Column(name = "url", nullable = false, length = 100)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "board_mesg", length = 200)
	public String getBoardMesg() {
		return this.boardMesg;
	}

	public void setBoardMesg(String boardMesg) {
		this.boardMesg = boardMesg;
	}

}
