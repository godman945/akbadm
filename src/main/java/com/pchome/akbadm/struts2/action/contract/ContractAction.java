package com.pchome.akbadm.struts2.action.contract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class ContractAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfdAccountService pfdAccountService;
	private IPfdContractService pfdContractService;
	private IAdmAccesslogService admAccesslogService;

	//查詢參數
	private String paramPfdCustomerInfoId;
	private String paramContractStatus;
	private String paramContractId;

	//查詢結果
	List<PfdContract> dataList;
	PfdContract pfdContract;

	//換頁參數
	private int pageNo = 1; //目前頁數
	private int pageSize = 10; //每頁幾筆
	private int pageCount = 0; //共幾頁
	private int totalCount = 0; //共幾筆

	//修改用 Id
	private String targetContractId;

	//輸入參數
	private String pfdCustomerInfoId; //經銷商序號
	private String contractId; //合約編號
	private String contractDate; //簽約日期
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String prePay; //預付
	private String payAfter; //後付
//	private String totalQuota; //總額度
	private String payDay; //付款條件(天)
	private String overdueFine; //逾期罰金(%)
	private String memo; //備註
	private String closeReason; //終止原因
	private String continueFlag = "n"; //自動續約

	//訊息
	private String message;

	public String execute() throws Exception {

		log.info(">>> paramPfdCustomerInfoId = " + paramPfdCustomerInfoId);
		log.info(">>> paramContractStatus = " + paramContractStatus);
		log.info(">>> paramContractId = " + paramContractId);

		Map<String, String> conditionsMap = new HashMap<String, String>();
		if (paramContractId!=null && !paramContractId.trim().equals("")) {
			conditionsMap.put("pfdContractId", paramContractId);
		}
		if (paramPfdCustomerInfoId!=null && !paramPfdCustomerInfoId.trim().equals("")) {
			conditionsMap.put("pfdCustomerInfoId", paramPfdCustomerInfoId);
		}
		if (paramContractStatus!=null && !paramContractStatus.trim().equals("")) {
			conditionsMap.put("status", paramContractStatus);
		}

		dataList = pfdContractService.findPfdContract(conditionsMap, pageNo, pageSize);
		log.info(">>> dataList.size() = " + dataList.size());

		if (dataList.size()==0) {

			this.message = "查無資料！";

		}

		return SUCCESS;
	}

	public String add() throws Exception {
		return SUCCESS;
	}

	public String doAdd() throws Exception {
		
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date now = new Date();

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("customerInfoId", pfdCustomerInfoId);

		PfdCustomerInfo pfdCustomerInfo = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap).get(0);

		if (pfdCustomerInfo!=null) {

			PfdContract pojo = new PfdContract();
			pojo.setPfdCustomerInfo(pfdCustomerInfo);
			pojo.setPfdContractId(contractId);
			pojo.setStatus(EnumContractStatus.WAIT.getStatusId());
			pojo.setContractDate(sdf0.parse(contractDate));
			pojo.setStartDate(sdf.parse(startDate + " 00:00:00"));
			pojo.setEndDate(sdf.parse(endDate + " 23:59:59"));
			String payType= null;
			if (prePay!=null && prePay.equals("true") && payAfter!=null && payAfter.equals("true")) {
				payType = EnumPfdAccountPayType.BOTH.getPayType();
			} else if (prePay!=null && prePay.equals("true")) {
				payType = EnumPfdAccountPayType.ADVANCE.getPayType();
			} else if (payAfter!=null && payAfter.equals("true")) {
				payType = EnumPfdAccountPayType.LATER.getPayType();
			} else {
				payType = EnumPfdAccountPayType.ADVANCE.getPayType();
			}
			pojo.setPfpPayType(payType);

			if (payAfter!=null && payAfter.equals("true")) {
				//pojo.setTotalQuota(Integer.parseInt(totalQuota));
				pojo.setPayDay(Integer.parseInt(payDay));
				pojo.setOverdueFine(Double.parseDouble(overdueFine));
			}

			if (memo!=null && !memo.trim().equals("")) {
				pojo.setMemo(memo);
			}

			pojo.setRemindFlag("n");
			pojo.setContinueFlag(continueFlag);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pfdContractService.save(pojo);
		}

		return SUCCESS;
		
	}
	
	public String update() throws Exception {
		log.info(">>> targetContractId = " + targetContractId);

		Map<String, String> conditionsMap = new HashMap<String, String>();
		if (targetContractId!=null && !targetContractId.trim().equals("")) {
			conditionsMap.put("pfdContractId", targetContractId);
		}

		List<PfdContract> pojoList = pfdContractService.findPfdContract(conditionsMap, pageNo, pageSize);

		if (pojoList.size() == 1) {

			pfdContract = pojoList.get(0);

		} else {
			this.message = "查無資料！";
		}

		return SUCCESS;
	}

	public String doUpdate() throws Exception {
		log.info(">>> targetContractId = " + targetContractId);

		Date now = new Date();

		Map<String, String> conditionsMap = new HashMap<String, String>();
		if (targetContractId!=null && !targetContractId.trim().equals("")) {
			conditionsMap.put("pfdContractId", targetContractId);
		}

		List<PfdContract> pojoList = pfdContractService.findPfdContract(conditionsMap, pageNo, pageSize);

		if (pojoList.size() == 1) {

			PfdContract pojo = pojoList.get(0);
			pojo.setContinueFlag(continueFlag);
			pojo.setMemo(memo);
			pojo.setUpdateDate(now);

			pfdContractService.save(pojo);
		}
		
		return SUCCESS;
	}

	public String doClose() throws Exception {
		log.info(">>> targetContractId = " + targetContractId);

		Date now = new Date();

		Map<String, String> conditionsMap = new HashMap<String, String>();
		if (targetContractId!=null && !targetContractId.trim().equals("")) {
			conditionsMap.put("pfdContractId", targetContractId);
		}

		List<PfdContract> pojoList = pfdContractService.findPfdContract(conditionsMap, pageNo, pageSize);

		if (pojoList.size() == 1) {
			
			PfdContract pojo = pojoList.get(0);
			pojo.setStatus(EnumContractStatus.CLOSE.getStatusId());
			pojo.setMemo(memo);
			pojo.setCloseReason(closeReason);
			pojo.setCloseDate(now);
			pojo.setUpdateDate(now);

			pfdContractService.save(pojo);
			
			//access log
			String logMsg = "合約作廢-->合約編號：" + pojo.getPfdContractId();
			
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.PFD, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
					super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, pojo.getPfdCustomerInfo().getCustomerInfoId(), 
					null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		}

		return SUCCESS;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
		this.contractDate = contractDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setDataList(List<PfdContract> dataList) {
		this.dataList = dataList;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public String getParamPfdCustomerInfoId() {
		return paramPfdCustomerInfoId;
	}

	public void setParamPfdCustomerInfoId(String paramPfdCustomerInfoId) {
		this.paramPfdCustomerInfoId = paramPfdCustomerInfoId;
	}

	public String getParamContractStatus() {
		return paramContractStatus;
	}

	public void setParamContractStatus(String paramContractStatus) {
		this.paramContractStatus = paramContractStatus;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public List<PfdContract> getDataList() {
		return dataList;
	}

	public void setTargetContractId(String targetContractId) {
		this.targetContractId = targetContractId;
	}

	public PfdContract getPfdContract() {
		return pfdContract;
	}

	public void setPfdContract(PfdContract pfdContract) {
		this.pfdContract = pfdContract;
	}

	public String getPrePay() {
		return prePay;
	}

	public void setPrePay(String prePay) {
		this.prePay = prePay;
	}

	public String getPayAfter() {
		return payAfter;
	}

	public void setPayAfter(String payAfter) {
		this.payAfter = payAfter;
	}

//	public String getTotalQuota() {
//		return totalQuota;
//	}

//	public void setTotalQuota(String totalQuota) {
//		this.totalQuota = totalQuota;
//	}

	public String getPayDay() {
		return payDay;
	}

	public void setPayDay(String payDay) {
		this.payDay = payDay;
	}

	public String getOverdueFine() {
		return overdueFine;
	}

	public void setOverdueFine(String overdueFine) {
		this.overdueFine = overdueFine;
	}

	public String getTargetContractId() {
		return targetContractId;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public String getParamContractId() {
		return paramContractId;
	}

	public void setParamContractId(String paramContractId) {
		this.paramContractId = paramContractId;
	}

	public String getContinueFlag() {
		return continueFlag;
	}

	public void setContinueFlag(String continueFlag) {
		this.continueFlag = continueFlag;
	}

	public Map<String, String> getPfdCustomerInfoMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();

		try {
			List<PfdCustomerInfo> list = pfdAccountService.getPfdCustomerInfoByCondition(new HashMap<String, String>());
			for (int i=0; i<list.size(); i++) {
				map.put(list.get(i).getCustomerInfoId(), list.get(i).getCompanyName());
			}
		} catch(Exception ex) {
			log.info("Exception :" + ex);
		}

		return map;
	}

	public Map<String, String> getContractStatusMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (EnumContractStatus status : EnumContractStatus.values()) {
			map.put(status.getStatusId(), status.getStatusName());
		}

		return map;
	}
}
