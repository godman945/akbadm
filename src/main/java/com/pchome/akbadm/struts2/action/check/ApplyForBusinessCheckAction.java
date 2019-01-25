package com.pchome.akbadm.struts2.action.check;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.vo.PfdApplyForBusinessVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.db.pojo.PfdApplyForBusiness;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.applyFor.IPfdApplyForBusinessService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.enumerate.applyFor.EnumPfdApplyForBusiness;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumPfdBoardType;

public class ApplyForBusinessCheckAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfdApplyForBusinessService applyForBusinessService;
	private IAdmAccesslogService admAccesslogService;
	private IPfdBoardService pfdBoardService;

	//查詢參數
	private String startDate;
	private String endDate;
	private String taxId;

	//換頁參數
	private int pageNo = 1; //目前頁數
	private int pageSize = 10; //每頁幾筆
	private int pageCount = 0; //共幾頁
	private int totalCount = 0; //共幾筆

	//查詢結果
	private List<PfdApplyForBusinessVO> dataList;

	//要審核的廣告序號
	private String[] applyForSeqs;
	private String[] rejectReason;

	//訊息
	private String message;

	public String execute() throws Exception{
		return SUCCESS;
	}

	/**
	 * 查詢 
	 */
	public String doQuery() throws Exception {

		Map<String, String> conditionsMap = new HashMap<String, String>();
		
		if (taxId!=null && !taxId.trim().equals("")) {
			conditionsMap.put("taxId", taxId);			
		}
		if (startDate!=null && !startDate.trim().equals("")) {
			conditionsMap.put("startDate", startDate);			
		}
		if (endDate!=null && !endDate.trim().equals("")) {
			conditionsMap.put("endDate", endDate);			
		}
		conditionsMap.put("status", EnumPfdApplyForBusiness.YET.getType());
		conditionsMap.put("orderBy", "applyForTime");

		this.totalCount = applyForBusinessService.findPfdApplyForBusinessCount(conditionsMap);
		this.pageCount = (int) Math.ceil(((float)totalCount / pageSize));

		//數量有變時，更新目前所在頁碼
		if (totalCount <= (pageNo-1)*pageSize) {
			this.pageNo = 1;
		}

		dataList = applyForBusinessService.findPfdApplyForBusinessVO(conditionsMap, pageNo, pageSize);

		if (dataList.size()==0) {

			this.message = "查無資料！";

		}

		return SUCCESS;
	}

	/**
	 * 核准 
	 */
	public String doApprove() throws Exception {

		if (applyForSeqs==null || applyForSeqs.length==0) {
			this.message = "請選擇審核資料！";
			return INPUT;
		}

		String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date now = new Date();
		Date startDate = sdf.parse(sdf.format(now));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 6);
		Date endDate = sdf.parse(sdf.format(c.getTime()));

		//通過
		PfdApplyForBusiness pfdApplyForBusiness = null;
		String seq = null;
		String pfdCustomerInfoId = null;
		String pfdUserId = null;
		
		for (int i=0; i<applyForSeqs.length; i++) {

			//改變狀態
			applyForBusinessService.updatePfdApplyForBusinessStatus(EnumPfdApplyForBusiness.PASS.getType(), applyForSeqs[i], userId, null);

			//寫公告
			Map<String, String> conditionsMap = new HashMap<String, String>();
			conditionsMap.put("seq", applyForSeqs[i]);

			pfdApplyForBusiness = applyForBusinessService.findPfdApplyForBusiness(conditionsMap, -1, 0).get(0);

			seq = pfdApplyForBusiness.getSeq().toString();
			pfdCustomerInfoId = pfdApplyForBusiness.getPfdCustomerInfoId();
			pfdUserId = pfdApplyForBusiness.getPfdUserId();

			String content = "廣告客戶 <a href=\"./adAccount.html\">" + pfdApplyForBusiness.getTargetTaxId() + "/" + pfdApplyForBusiness.getTargetAdUrl() + "</a>，開發申請核准。";
			//content += "<a href=\"http://show.pchome.com.tw/xxx.html?seq=" + ??? + "\">查看</a>";

			PfdBoard board = new PfdBoard();
			board.setBoardType(EnumPfdBoardType.VERIFY.getType());
			board.setBoardContent(content);
			board.setPfdCustomerInfoId(pfdCustomerInfoId);
			board.setIsSysBoard("n");
			board.setPfdUserId(pfdUserId);
			board.setStartDate(startDate);
			board.setEndDate(endDate);
			board.setHasUrl("n");
			board.setUrlAddress(null);
			board.setDeleteId(pfdApplyForBusiness.getTargetTaxId() + "/" + pfdApplyForBusiness.getTargetAdUrl());
			
			//觀看權限(總管理者/帳戶管理/行政管理)
			String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege()
					 + "||" + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege();
			board.setMsgPrivilege(msgPrivilege);
			
			board.setUpdateDate(now);
			board.setCreateDate(now);

			pfdBoardService.save(board);

			//給業務看的
			PfdBoard board2 = new PfdBoard();
			board2.setBoardType(EnumPfdBoardType.VERIFY.getType());
			board2.setBoardContent("廣告客戶 " + pfdApplyForBusiness.getTargetTaxId() + "/" + pfdApplyForBusiness.getTargetAdUrl() + "，開發申請核准。");
			board2.setPfdCustomerInfoId(pfdCustomerInfoId);
			board2.setIsSysBoard("n");
			board2.setPfdUserId(pfdUserId);
			board2.setStartDate(startDate);
			board2.setEndDate(endDate);
			board2.setHasUrl("n");
			board2.setUrlAddress(null);
			board2.setDeleteId(pfdApplyForBusiness.getTargetTaxId() + "/" + pfdApplyForBusiness.getTargetAdUrl());
			board2.setMsgPrivilege(EnumPfdPrivilege.SALES_MANAGER.getPrivilege().toString());
			board2.setUpdateDate(now);
			board2.setCreateDate(now);
			
			pfdBoardService.save(board2);
			
			//access log
			String logMsg = "申請開發審核：核淮(" + applyForSeqs[i] + ")";
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg, 
											super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
											null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		}

		return SUCCESS;
	}

	/**
	 * 拒絕
	 */
	public String doReject() throws Exception {

		if (applyForSeqs==null || applyForSeqs.length==0) {
			this.message = "請選擇審核資料！";
			return INPUT;
		}

		if (rejectReason==null || rejectReason.length==0) {
			this.message = "請填寫退件原因！";
			return INPUT;
		}

		if (applyForSeqs.length != rejectReason.length) {
			this.message = "系統錯誤！";
			return INPUT;
		}

		String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		Date now = new Date();
		Date startDate = sdf.parse(sdf.format(now));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 6);
		Date endDate = sdf.parse(sdf.format(c.getTime()));

		//拒絕
		PfdApplyForBusiness pfdApplyForBusiness = null;
		String seq = null;
		String pfdCustomerInfoId = null;
		String pfdUserId = null;

		for (int i=0; i<applyForSeqs.length; i++) {

			//改變狀態
			applyForBusinessService.updatePfdApplyForBusinessStatus(EnumPfdApplyForBusiness.REJECT.getType(), applyForSeqs[i], userId, rejectReason[i]);

			//寫公告
			Map<String, String> conditionsMap = new HashMap<String, String>();
			conditionsMap.put("seq", applyForSeqs[i]);

			pfdApplyForBusiness = applyForBusinessService.findPfdApplyForBusiness(conditionsMap, -1, 0).get(0);

			seq = pfdApplyForBusiness.getSeq().toString();
			pfdCustomerInfoId = pfdApplyForBusiness.getPfdCustomerInfoId();
			pfdUserId = pfdApplyForBusiness.getPfdUserId();

			String content = "廣告客戶 <a href=\"./applyForBusiness.html\">" + pfdApplyForBusiness.getTargetTaxId() + "/" + pfdApplyForBusiness.getTargetAdUrl() + "</a>，開發申請拒絕。";
			//content += "<a href=\"http://show.pchome.com.tw/xxx.html?seq=" + ??? + "\">查看</a>";

			PfdBoard board = new PfdBoard();
			board.setBoardType(EnumPfdBoardType.VERIFY.getType());
			board.setBoardContent(content);
			board.setPfdCustomerInfoId(pfdCustomerInfoId);
			board.setIsSysBoard("n");
			board.setPfdUserId(pfdUserId);
			board.setStartDate(startDate);
			board.setEndDate(endDate);
			board.setHasUrl("n");
			board.setUrlAddress(null);
			
			//觀看權限(總管理者/帳戶管理/行政管理/業務管理)
			String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege()
					 + "||" + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.SALES_MANAGER.getPrivilege();
			board.setMsgPrivilege(msgPrivilege);
			
			board.setUpdateDate(now);
			board.setCreateDate(now);

			pfdBoardService.save(board);

			//access log
			String logMsg = "申請開發審核：拒絕(" + applyForSeqs[i] + ")";
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg, 
											super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
											null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		}

		return SUCCESS;
	}

	public void setApplyForBusinessService(IPfdApplyForBusinessService applyForBusinessService) {
		this.applyForBusinessService = applyForBusinessService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<PfdApplyForBusinessVO> getDataList() {
		return dataList;
	}

	public String getMessage() {
		return message;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public String[] getApplyForSeqs() {
		return applyForSeqs;
	}

	public void setApplyForSeqs(String[] applyForSeqs) {
		this.applyForSeqs = applyForSeqs;
	}

	public String[] getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String[] rejectReason) {
		this.rejectReason = rejectReason;
	}
}
