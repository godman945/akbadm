package com.pchome.akbadm.struts2.action.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class TransReportAction extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IPfdAccountService pfdAccountService;

	private String startDate;
	private String endDate;

	private String message = "";

	public String execute() throws Exception{

		this.message = "請選擇日期開始查詢！";

		return SUCCESS;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getMessage() {
		return message;
	}

	public List<PfdCustomerInfo> getCompanyList() {

		List<PfdCustomerInfo> pfdCustomerInfos = new ArrayList<PfdCustomerInfo>();

		try {

			String status = "'" + EnumPfdAccountStatus.START.getStatus() +
					"','" + EnumPfdAccountStatus.CLOSE.getStatus() +
					"','" + EnumPfdAccountStatus.STOP.getStatus() + "'";

			log.info(">>> status = " + status);

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("status", status);

			pfdCustomerInfos = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap);

		} catch(Exception ex) {
			log.info("Exception :" + ex);
		}

		return pfdCustomerInfos;
	}

	public Map<String, String> getPayTypeMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "全部");
		map.put(EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfdAccountPayType.ADVANCE.getPayName());
		map.put(EnumPfdAccountPayType.LATER.getPayType(), EnumPfdAccountPayType.LATER.getPayName());
		return map;
	}
}
