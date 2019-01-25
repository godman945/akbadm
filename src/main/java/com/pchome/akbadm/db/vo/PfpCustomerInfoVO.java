package com.pchome.akbadm.db.vo;

import com.pchome.akbadm.db.pojo.PfpCustomerInfo;

public class PfpCustomerInfoVO extends PfpCustomerInfo {

	private static final long serialVersionUID = 1L;

	private String remainDays;

	public String getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(String remainDays) {
		this.remainDays = remainDays;
	}
}
