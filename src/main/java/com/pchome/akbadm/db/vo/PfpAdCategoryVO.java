package com.pchome.akbadm.db.vo;

import java.util.HashMap;
import java.util.Map;

public class PfpAdCategoryVO {

	private String name;
	private String code;
	private Map<String, PfpAdCategoryVO> children = new HashMap<String, PfpAdCategoryVO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, PfpAdCategoryVO> getChildren() {
		return children;
	}

	public void addChildren(String id, PfpAdCategoryVO vo) {
		this.children.put(id, vo);
	}
}
