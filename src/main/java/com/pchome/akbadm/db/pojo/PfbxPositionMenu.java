package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

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

/**
 * PfbxPositionMenu generated by hbm2java
 */
@Entity
@Table(name = "pfbx_position_menu")
public class PfbxPositionMenu implements java.io.Serializable {

	private Integer id;
	private String pfbxMenuName;
	private String pfbxAdTemplateProductXType;
	private int pfbxMenuProperties;
	private Set<PfbxCustomerInfoRefXType> pfbxCustomerInfoRefXTypes = new HashSet<PfbxCustomerInfoRefXType>(0);

	public PfbxPositionMenu() {
	}

	public PfbxPositionMenu(String pfbxMenuName, String pfbxAdTemplateProductXType, int pfbxMenuProperties) {
		this.pfbxMenuName = pfbxMenuName;
		this.pfbxAdTemplateProductXType = pfbxAdTemplateProductXType;
		this.pfbxMenuProperties = pfbxMenuProperties;
	}

	public PfbxPositionMenu(String pfbxMenuName, String pfbxAdTemplateProductXType, int pfbxMenuProperties,
			Set<PfbxCustomerInfoRefXType> pfbxCustomerInfoRefXTypes) {
		this.pfbxMenuName = pfbxMenuName;
		this.pfbxAdTemplateProductXType = pfbxAdTemplateProductXType;
		this.pfbxMenuProperties = pfbxMenuProperties;
		this.pfbxCustomerInfoRefXTypes = pfbxCustomerInfoRefXTypes;
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

	@Column(name = "pfbx_menu_name", nullable = false, length = 20)
	public String getPfbxMenuName() {
		return this.pfbxMenuName;
	}

	public void setPfbxMenuName(String pfbxMenuName) {
		this.pfbxMenuName = pfbxMenuName;
	}

	@Column(name = "pfbx_ad_template_product_x_type", nullable = false, length = 3)
	public String getPfbxAdTemplateProductXType() {
		return this.pfbxAdTemplateProductXType;
	}

	public void setPfbxAdTemplateProductXType(String pfbxAdTemplateProductXType) {
		this.pfbxAdTemplateProductXType = pfbxAdTemplateProductXType;
	}

	@Column(name = "pfbx_menu_properties", nullable = false)
	public int getPfbxMenuProperties() {
		return this.pfbxMenuProperties;
	}

	public void setPfbxMenuProperties(int pfbxMenuProperties) {
		this.pfbxMenuProperties = pfbxMenuProperties;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfbxPositionMenu")
	public Set<PfbxCustomerInfoRefXType> getPfbxCustomerInfoRefXTypes() {
		return this.pfbxCustomerInfoRefXTypes;
	}

	public void setPfbxCustomerInfoRefXTypes(Set<PfbxCustomerInfoRefXType> pfbxCustomerInfoRefXTypes) {
		this.pfbxCustomerInfoRefXTypes = pfbxCustomerInfoRefXTypes;
	}

}
