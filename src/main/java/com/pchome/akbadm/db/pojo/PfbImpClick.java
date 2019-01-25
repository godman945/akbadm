package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PfbImpClick generated by hbm2java
 */
@Entity
@Table(name = "pfb_imp_click")
public class PfbImpClick implements java.io.Serializable {

	private PfbImpClickId id;
	private int imp;
	private int click;

	public PfbImpClick() {
	}

	public PfbImpClick(PfbImpClickId id, int imp, int click) {
		this.id = id;
		this.imp = imp;
		this.click = click;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "styleNo", column = @Column(name = "style_no", nullable = false, length = 30)),
			@AttributeOverride(name = "tproId", column = @Column(name = "tpro_id", nullable = false, length = 20)),
			@AttributeOverride(name = "date", column = @Column(name = "date", nullable = false, length = 10)),
			@AttributeOverride(name = "hour", column = @Column(name = "hour", nullable = false)) })
	public PfbImpClickId getId() {
		return this.id;
	}

	public void setId(PfbImpClickId id) {
		this.id = id;
	}

	@Column(name = "imp", nullable = false)
	public int getImp() {
		return this.imp;
	}

	public void setImp(int imp) {
		this.imp = imp;
	}

	@Column(name = "click", nullable = false)
	public int getClick() {
		return this.click;
	}

	public void setClick(int click) {
		this.click = click;
	}

}
