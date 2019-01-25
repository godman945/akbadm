package com.pchome.akbadm.struts2.action.order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpBoard;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.pojo.PfpRefundOrderRelease;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.board.IPfpBoardService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.order.IPfpOrderService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderReleaseService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.pfd.user.IPfdUserService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.user.IPfpUserService;
import com.pchome.akbadm.db.vo.PfpRefundOrderReleaseVo;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.applyFor.EnumPfdApplyForBusiness;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.pfbx.user.EnumUserPrivilege;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

public class AdvanceOrderRefundReleaseAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	public static final String MAIL_API_NO = "P159";

	private IPfpRefundOrderService pfpRefundOrderService;
	private IPfpRefundOrderReleaseService pfpRefundOrderReleaseService;
	private IPfpOrderService pfpOrderService;
	private IPfpCustomerInfoService pfpCustomerInfoService;
	private IAdmRecognizeRecordService admRecognizeRecordService;
	private SpringEmailUtil springEmailUtil;
	private IAdmAccesslogService admAccesslogService;
	private IPfdBoardService pfdBoardService;
	private IPfpBoardService pfpBoardService;
	private IPfpUserService pfpUserService;
	private IPfdUserService pfdUserService;
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	
	//查詢結果
	private List<PfpRefundOrderReleaseVo> dataList;
	//查詢參數
	private String billingOrderId;
	private String pfpCustomerInfoId;
	private String startDate;
	private String endDate;
	private String refundStatus;
	//要審核的廣告序號
	private String[] releaseSeqs;
	private String[] rejectReason;
	//換頁參數
	private int pageNo = 1; //目前頁數
	private int pageSize = 10; //每頁幾筆
	private int pageCount = 0; //共幾頁
	private int totalCount = 0; //共幾筆
	//訊息
	private String message;
	//上次查詢退款狀態
	private String lastQueryStatus;
	

	public String execute() throws Exception{
		return SUCCESS;
	}

	/**
	 * 查詢 
	 */
	public String doQuery() throws Exception {
		try{
			log.info("billingOrderId: "+billingOrderId);
			log.info("pfpCustomerInfoId: "+pfpCustomerInfoId);
			log.info("startDate: "+startDate);
			log.info("endDate: "+endDate);
			log.info("refundStatus: "+refundStatus);
			log.info("pageNo: "+pageNo);
			log.info("pageSize: "+pageSize);
			
			if  (StringUtils.isBlank(refundStatus)){
				refundStatus = "y";
			}
			
			log.info("refundStatus: "+refundStatus);
			
			Map<String, String> conditionsMap = new HashMap<String, String>();
			
			if (billingOrderId!=null && !billingOrderId.trim().equals("")) {
				conditionsMap.put("billingOrderId", billingOrderId);			
			}
			if (pfpCustomerInfoId!=null && !pfpCustomerInfoId.trim().equals("")) {
				conditionsMap.put("pfpCustomerInfoId", pfpCustomerInfoId);			
			}
			if (startDate!=null && !startDate.trim().equals("")) {
				conditionsMap.put("startDate", startDate);			
			}
			if (endDate!=null && !endDate.trim().equals("")) {
				conditionsMap.put("endDate", endDate);			
			}
			conditionsMap.put("refundStatus", refundStatus);
			
	
			//先撈全部筆數，故無分頁
			conditionsMap.put("havePage", "N");
			this.totalCount = pfpRefundOrderReleaseService.findPfpRefundOrderReleaseCount(conditionsMap);
			this.pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	
			//數量有變時，更新目前所在頁碼
			if (totalCount <= (pageNo-1)*pageSize) {
				this.pageNo = 1;
			}
	
			log.info("pageNo: "+pageNo);
			log.info("pageSize: "+pageSize);
			
			//有分頁
			int pageIndex = (pageNo-1) * pageSize;
			int fetchSize = pageSize;
			conditionsMap.put("havePage", "Y");
			conditionsMap.put("limit"," limit " + Integer.toString(pageIndex) + "," + Integer.toString(fetchSize));
		
			dataList = pfpRefundOrderReleaseService.findPfpRefundOrderRelease(conditionsMap);
	
			log.info("dataList size: "+dataList.size());
			
			if (dataList.size()==0) {
				this.message = "查無資料！";
			}
			
			lastQueryStatus = refundStatus;
			
		}catch (Exception e){			
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
	}

	/**
	 * 核准 
	 */
	public String doApprove() throws Exception {
		try{
			
			if (releaseSeqs==null || releaseSeqs.length==0) {
				this.message = "請選擇審核資料！";
				return INPUT;
			}
			
			log.info("releaseSeqs length : "+releaseSeqs.length);
	
			String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);
	
			AdmRecognizeRecord admRecognizeRecord = null;
			PfpRefundOrderRelease pfpRefundOrderRelease = null;
			PfpRefundOrder pfpRefundOrder = null;
			String orderId;
			String pfpCustomerId;
			Date today = new Date();
			
			for (int i=0; i<releaseSeqs.length; i++) {
	
				admRecognizeRecord = new AdmRecognizeRecord();
				pfpRefundOrderRelease = new PfpRefundOrderRelease();
				pfpRefundOrder = new PfpRefundOrder();;
				orderId = "";
				pfpCustomerId = "";
				
				//撈取剩餘金額與稅額
				pfpRefundOrderRelease = pfpRefundOrderReleaseService.get(Integer.valueOf(releaseSeqs[i]));
				pfpRefundOrder = pfpRefundOrderRelease.getPfpRefundOrder();
				orderId = pfpRefundOrder.getOrderId();
				pfpCustomerId = pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId();
				admRecognizeRecord = admRecognizeRecordService.findRecognizeRecords(pfpCustomerId, orderId, EnumOrderType.SAVE).get(0);
				
				log.info("pfpCustomerId : "+pfpCustomerId);
				log.info("orderId : "+orderId);
				
				
				//更新 RefundOrderRelease 資料
				pfpRefundOrderRelease.setRefundPriceTax(admRecognizeRecord.getOrderRemain() + admRecognizeRecord.getTaxRemain());
				pfpRefundOrderRelease.setStatus(EnumPfdApplyForBusiness.PASS.getType());
				pfpRefundOrderRelease.setCheckUser(userId);
				pfpRefundOrderRelease.setCheckTime(new Date());
				pfpRefundOrderRelease.setUpdateDate(new Date());
				pfpRefundOrderReleaseService.saveOrUpdate(pfpRefundOrderRelease);
				
				
				//更新 pfpRefundOrder 退款資料
				pfpRefundOrder.setRefundStatus(EnumPfpRefundOrderStatus.SUCCESS.getStatus());
				pfpRefundOrder.setRefundPrice(admRecognizeRecord.getOrderRemain());
				pfpRefundOrder.setRefundDate(today);
				pfpRefundOrder.setUpdateDate(today);
				pfpRefundOrderService.saveOrUpdateWithCommit(pfpRefundOrder);
				
				
				//查金流訂單編號
				PfpOrder order = pfpOrderService.findOrderInfo(orderId);
				String billingOrderId = order.getBillingId();
				
				log.info("OrderRemain : "+admRecognizeRecord.getOrderRemain());
				log.info("TaxRemain : "+admRecognizeRecord.getTaxRemain());
				
				
				//寫公告
				PfdCustomerInfo pfdCustomerInfo = new PfdCustomerInfo();
				PfdUser pfdUser = new PfdUser();
				PfpUser pfpUser = new PfpUser();
				
				PfdUserAdAccountRef pfdUserAdAccountRef = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId()).get(0);
				pfdCustomerInfo = pfdUserAdAccountRef.getPfdCustomerInfo();
				
				pfdUser = pfdUserService.findRootPfdUser(pfdCustomerInfo.getCustomerInfoId()).get(0);
				pfpUser = pfpUserService.findOpenAccountUser(pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId());
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String pfdBoardContent = "廣告帳戶： " + pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId()
						+ " ， PChome聯播網預付儲值金退款已核准 。 " ;
				String pfpBoardContent = "PChome聯播網預付儲值金退款已核准 。 " ;
			
				this.writePfdBoard(pfdCustomerInfo, pfdUser, pfdBoardContent);
				this.writePfpBoard(pfpRefundOrder.getPfpCustomerInfo(), pfpBoardContent);
				

				
				//Send mail
				Mail mail = null;
				String mailContent = "";
				float refundOrderTaxRemain = 0;
				refundOrderTaxRemain = admRecognizeRecord.getOrderRemain() + admRecognizeRecord.getTaxRemain();
				
				mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
				mailContent = "<html><body>" + "<img src=\"http://show.pchome.com.tw/html/img/logo_pchome.png\" />"
						+ "<br>親愛的 " + pfdCustomerInfo.getCompanyName() + " 您好: " + "<br>您的PChome聯播網預付儲值金退款已核准:" + "<br>帳戶名稱: "
						+ pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoTitle() + "<br>帳戶編號: "
						+ pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId() + "<br>訂單編號: " + billingOrderId
						+ "<br>退款金額:NT$" + refundOrderTaxRemain + "<p>提醒您: 款項退回需7-14個工作天(不含例假日)，如您有任何問題，請與客服中心聯絡，謝謝!"
						+ "<p>PChome聯播網小組 敬上" + "<br>=============================================================="
						+ "<br>注意: 本郵件是由PChome聯播網系統自動產生與發送，請勿直接回覆。"
						+ "<br>==============================================================" + "</body></html>";

				mail.setMsg("<html><body>" + mailContent + "</body></html>");
				
				String pfdRootUserMail = pfdUser.getUserEmail();
				String pfpRootUserMail = pfpUser.getUserEmail();

				String[] to = { pfdRootUserMail,pfpRootUserMail };
				springEmailUtil.sendHtmlEmail("PChome聯播網廣告帳戶退款核准通知信", mail.getMailFrom(), to, mail.getMailTo(), mail.getMsg());
	
	//			//access log
	//			String logMsg = "申請開發審核：核淮(" + releaseSeqs[i] + ")";
	//			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg, 
	//											super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
	//											null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
			
				
				//即時扣退款成功的 PFP 帳戶餘額與總加值金額
				PfpCustomerInfo pfpCustomerInfo = pfpCustomerInfoService.getCustomerInfo(pfpCustomerId);
				pfpCustomerInfo.setRemain(pfpCustomerInfo.getRemain()-admRecognizeRecord.getOrderRemain());
				pfpCustomerInfo.setLaterRemain(pfpCustomerInfo.getLaterRemain()-admRecognizeRecord.getOrderRemain());
				pfpCustomerInfo.setTotalAddMoney(pfpCustomerInfo.getTotalAddMoney()-admRecognizeRecord.getOrderRemain());
				pfpCustomerInfo.setUpdateDate(new Date());
				pfpCustomerInfoService.saveOrUpdateWithCommit(pfpCustomerInfo);
				
				
				//adm系統核准退款後，將 adm_recognize_record 攤提歸零flag 設為Y
				admRecognizeRecord.setOrderRemainZero("Y");
				admRecognizeRecordService.saveOrUpdate(admRecognizeRecord);

				
				//如沒有申請中的預付退款，即開啟pfp廣告活動
				List<PfpRefundOrder> notRefundableOrder = pfpRefundOrderService.findPfpRefundOrder(pfpCustomerId, EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfpRefundOrderStatus.NOT_REFUND.getStatus());
				if ( notRefundableOrder.isEmpty() ){
					pfpCustomerInfo.setStatus(EnumAccountStatus.START.getStatus());
					pfpCustomerInfoService.saveOrUpdate(pfpCustomerInfo);
				}
			}
	
			lastQueryStatus = refundStatus;
			
		}catch (Exception e){			
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * 拒絕
	 */
	public String doReject() throws Exception {
		try{
			if (releaseSeqs==null || releaseSeqs.length==0) {
				this.message = "請選擇審核資料！";
				return INPUT;
			}
	
			if (rejectReason==null || rejectReason.length==0) {
				this.message = "請填寫退件原因！";
				return INPUT;
			}
	
			if (releaseSeqs.length != rejectReason.length) {
				this.message = "系統錯誤！";
				return INPUT;
			}
			
			log.info("releaseSeqs length : "+releaseSeqs.length);
			log.info("rejectReason length : "+rejectReason.length);
	
			String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);
			
			AdmRecognizeRecord admRecognizeRecord = null;
			PfpRefundOrderRelease pfpRefundOrderRelease = null;
			PfpRefundOrder pfpRefundOrder = null;
			String orderId;
			String pfpCustomerId;
			Date today = new Date();
			
			for (int i=0; i<releaseSeqs.length; i++) {
				
				admRecognizeRecord = new AdmRecognizeRecord();
				pfpRefundOrderRelease = new PfpRefundOrderRelease();
				pfpRefundOrder = new PfpRefundOrder();
				orderId = "";
				pfpCustomerId = "";
				
				//更新 RefundOrderRelease 資料
				pfpRefundOrderReleaseService.updatePfpRefundOrderReleaseStatus(EnumPfdApplyForBusiness.REJECT.getType(), releaseSeqs[i], userId, rejectReason[i]);
				pfpRefundOrderRelease = pfpRefundOrderReleaseService.get(Integer.valueOf(releaseSeqs[i]));
				pfpRefundOrder = pfpRefundOrderRelease.getPfpRefundOrder();
				orderId = pfpRefundOrder.getOrderId();
				pfpCustomerId = pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId();
				admRecognizeRecord = admRecognizeRecordService.findRecognizeRecords(pfpCustomerId, orderId, EnumOrderType.SAVE).get(0);
				
				log.info("pfpCustomerId : "+pfpCustomerId);
				log.info("orderId : "+orderId);

				//更新 pfpRefundOrder 退款資料
				pfpRefundOrder.setRefundStatus(EnumPfpRefundOrderStatus.FAIL.getStatus());
				pfpRefundOrder.setRefundPrice(admRecognizeRecord.getOrderRemain());
				pfpRefundOrder.setRefundNote(rejectReason[i]);
				pfpRefundOrder.setRefundDate(today);
				pfpRefundOrder.setUpdateDate(today);
				pfpRefundOrderService.saveOrUpdateWithCommit(pfpRefundOrder);
				
				
				//查金流訂單編號
				PfpOrder order = pfpOrderService.findOrderInfo(orderId);
				String billingOrderId = order.getBillingId();
				
				log.info("OrderRemain : "+admRecognizeRecord.getOrderRemain());
				log.info("TaxRemain : "+admRecognizeRecord.getTaxRemain());
				
				
				//寫公告
				PfdCustomerInfo pfdCustomerInfo = new PfdCustomerInfo();
				PfdUser pfdUser = new PfdUser();
				PfpUser pfpUser = new PfpUser();
				
				PfdUserAdAccountRef pfdUserAdAccountRef = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId()).get(0);
				pfdCustomerInfo = pfdUserAdAccountRef.getPfdCustomerInfo();
				
				pfdUser = pfdUserService.findRootPfdUser(pfdCustomerInfo.getCustomerInfoId()).get(0);
				pfpUser = pfpUserService.findOpenAccountUser(pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId());

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String pfdBoardContent = "廣告帳戶： " + pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId()
						+ " ， PChome聯播網預付儲值金退款失敗 。 " ;
				String pfpBoardContent = "PChome聯播網預付儲值金退款失敗 。 " ;
				
				this.writePfdBoard(pfdCustomerInfo, pfdUser, pfdBoardContent);
				this.writePfpBoard(pfpRefundOrder.getPfpCustomerInfo(), pfpBoardContent);
				
				
				//Send mail
				Mail mail = null;
				String mailContent = "";

				mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
				mailContent = "<html><body>" + "<img src=\"http://show.pchome.com.tw/html/img/logo_pchome.png\" />"
						+ "<br>親愛的 " + pfdCustomerInfo.getCompanyName() + " 您好: " + "<br>您的PChome聯播網預付儲值金退款失敗，失敗原因: " + rejectReason[i] + "<br>帳戶名稱: "
						+ pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoTitle() + "<br>帳戶編號: "
						+ pfpRefundOrder.getPfpCustomerInfo().getCustomerInfoId() + "<br>訂單編號: " + billingOrderId
						+ "<p>如您有任何問題，請與客服中心聯絡，謝謝!" + "<p>PChome聯播網小組 敬上"
						+ "<br>=============================================================="
						+ "<br>注意: 本郵件是由PChome聯播網系統自動產生與發送，請勿直接回覆。"
						+ "<br>==============================================================" + "</body></html>";

				mail.setMsg("<html><body>" + mailContent + "</body></html>");
				
				String pfdRootUserMail = pfdUser.getUserEmail();
				String pfpRootUserMail = pfpUser.getUserEmail();

				String[] to = { pfdRootUserMail,pfpRootUserMail };
				springEmailUtil.sendHtmlEmail("PChome聯播網廣告帳戶退款失敗通知信", mail.getMailFrom(), to, mail.getMailTo(), mail.getMsg());

				
	//			//access log
	//			String logMsg = "申請開發審核：拒絕(" + releaseSeqs[i] + ")";
	//			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg, 
	//											super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
	//											null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
					
				
				//如沒有申請中的預付退款，即開啟pfp廣告活動
				List<PfpRefundOrder> notRefundableOrder = pfpRefundOrderService.findPfpRefundOrder(pfpCustomerId, EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfpRefundOrderStatus.NOT_REFUND.getStatus());
				if ( notRefundableOrder.isEmpty() ){
					PfpCustomerInfo pfpCustomerInfo = pfpRefundOrder.getPfpCustomerInfo();
					pfpCustomerInfo.setStatus(EnumAccountStatus.START.getStatus());
					pfpCustomerInfoService.saveOrUpdate(pfpCustomerInfo);
				}
				
			}
	
			lastQueryStatus = refundStatus;
		
		}catch (Exception e){			
			log.error(e.getMessage(), e);
		}
		
		return SUCCESS;
	}

	private void writePfdBoard(PfdCustomerInfo pfdCustomerInfo, PfdUser pfdUser, String boardContent) throws Exception{
		
		if(pfdCustomerInfo != null && pfdUser != null){
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Date createDate = new Date();
	        Date startDate = sdf.parse(sdf.format(createDate));
	        Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 6);
			Date endDate = sdf.parse(sdf.format(c.getTime()));
			
			PfdBoard pfdBoard = new PfdBoard();
			
			pfdBoard.setBoardType(EnumPfdBoardType.FINANCE.getType());
			pfdBoard.setBoardContent(boardContent);
			pfdBoard.setPfdCustomerInfoId(pfdCustomerInfo.getCustomerInfoId());
			pfdBoard.setPfdUserId(pfdUser.getUserId());
			pfdBoard.setStartDate(startDate);
			pfdBoard.setEndDate(endDate);
			pfdBoard.setIsSysBoard("n");
			pfdBoard.setHasUrl("n");
			pfdBoard.setUrlAddress(null);
			pfdBoard.setDeleteId(null);
    		
    		//觀看權限(總管理者)
			Integer msgPrivilege = EnumUserPrivilege.ROOT_USER.getPrivilegeId();
			pfdBoard.setMsgPrivilege(Integer.toString(msgPrivilege));
    		
			pfdBoard.setCreateDate(createDate);
    		pfdBoardService.save(pfdBoard);
		}
	}
	
	private void writePfpBoard(PfpCustomerInfo pfpCustomerInfo, String boardContent) throws Exception{ 
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();

        PfpBoard board = new PfpBoard();
        board.setCustomerInfoId(pfpCustomerInfo.getCustomerInfoId());
        board.setBoardType(EnumBoardType.FINANCE.getType());
        board.setCategory(EnumCategory.OTHER.getCategory());
        board.setDeleteId(null);
        board.setContent(boardContent);
        board.setStartDate(df.format(date));
        board.setEndDate(df.format(endDate));
        board.setDisplay("Y");
        board.setUrlAddress("");
        board.setAuthor("SYSTEM");
        board.setEditor("SYSTEM");
        board.setUpdateDate(date);
        board.setCreateDate(date);
        board.setHasUrl("N");

        pfpBoardService.save(board);
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

	public void setPfpRefundOrderReleaseService(IPfpRefundOrderReleaseService pfpRefundOrderReleaseService) {
		this.pfpRefundOrderReleaseService = pfpRefundOrderReleaseService;
	}
	
	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}

	public void setAdmRecognizeRecordService(IAdmRecognizeRecordService admRecognizeRecordService) {
		this.admRecognizeRecordService = admRecognizeRecordService;
	}
	
	public void setPfpOrderService(IPfpOrderService pfpOrderService) {
		this.pfpOrderService = pfpOrderService;
	}

	public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
		this.springEmailUtil = springEmailUtil;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}
	
	public void setPfpBoardService(IPfpBoardService pfpBoardService) {
		this.pfpBoardService = pfpBoardService;
	}

	public void setPfpUserService(IPfpUserService pfpUserService) {
		this.pfpUserService = pfpUserService;
	}

	public void setPfdUserService(IPfdUserService pfdUserService) {
		this.pfdUserService = pfdUserService;
	}
	
	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public List<PfpRefundOrderReleaseVo> getDataList() {
		return dataList;
	}

	public String getBillingOrderId() {
		return billingOrderId;
	}

	public void setBillingOrderId(String billingOrderId) {
		this.billingOrderId = billingOrderId;
	}

	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
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

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public String[] getReleaseSeqs() {
		return releaseSeqs;
	}

	public void setReleaseSeqs(String[] releaseSeqs) {
		this.releaseSeqs = releaseSeqs;
	}

	public String[] getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String[] rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getLastQueryStatus() {
		return lastQueryStatus;
	}

	public void setLastQueryStatus(String lastQueryStatus) {
		this.lastQueryStatus = lastQueryStatus;
	}
	
}
