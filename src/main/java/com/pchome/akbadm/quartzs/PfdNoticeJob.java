package com.pchome.akbadm.quartzs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;

@Transactional
public class PfdNoticeJob {

	private Log log = LogFactory.getLog(getClass().getName());

    private IPfdBoardService pfdBoardService;
    private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfpAdActionService pfpAdActionService;
	protected IBoardProvider boardProvider;
	private IPfdBonusInvoiceService pfdBonusInvoiceService;

	private String akbPfdServer;

    /**
     * 每月1自動設定每月1提醒開放對帳，時間過後下架
     */
    public void invoiceNotice() {
        log.info("====PfdNoticeJob.invoiceNotice() start====");

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    	Calendar now = Calendar.getInstance();
    	int nowYear = now.get(Calendar.YEAR);
    	int nowMonth = now.get(Calendar.MONTH) + 1;
    	int nextMonth = now.get(Calendar.MONTH) + 1;

    	Date createDate = new Date(now.getTimeInMillis());

    	String d1 = "" + nowYear;
		if (nowMonth<10) {
			d1 += "0" + nowMonth + "01";
		} else {
			d1 += nowMonth + "01";
		}

		String d2 = "" + nowYear;
		if (nextMonth<10) {
			d2 += "0" + nextMonth + now.getActualMaximum(Calendar.DATE);
		} else {
			d2 += nextMonth + now.getActualMaximum(Calendar.DATE);
		}
		
		try {

			Date startDate = sdf.parse(d1);
			Date endDate = sdf.parse(d2);

			String content = "<a href=\"" + akbPfdServer + "bonusBill.html\">當月廣告費與佣金對帳單</a> 已開放下載，請立即查詢確認!";
			String content2 = "請於本月15號前，寄出佣金發票及用印後之請款單；可隨時查看您的";
			content2 += "<a href=\"" + akbPfdServer + "bonusBill.html\">請款進度狀態</a>。";

			List<PfdBonusInvoice> dataList = pfdBonusInvoiceService.findPfdBonusInvoices(null, sdf2.format(new Date()), sdf2.format(new Date()));
			
			if(!dataList.isEmpty()){
				for(PfdBonusInvoice data:dataList){
					String pfdCustomerInfoId = data.getPfdContract().getPfdCustomerInfo().getCustomerInfoId();
					
					PfdBoard board = new PfdBoard();
					board.setPfdCustomerInfoId(pfdCustomerInfoId);
					board.setBoardType(EnumPfdBoardType.FINANCE.getType());
					board.setBoardContent(content);
					board.setStartDate(startDate);
					board.setEndDate(endDate);
					board.setHasUrl("n");
					board.setIsSysBoard("n");
					
					//觀看權限(總管理者/帳戶管理/行政管理/帳務管理)
					String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege()
							 + "||" + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.BILL_MANAGER.getPrivilege();
					board.setMsgPrivilege(msgPrivilege);
					
					board.setCreateDate(createDate);
					
					pfdBoardService.save(board);
					
					//預付要先發這個訊息
					if(StringUtils.equals(data.getPayType(), "1")){
						PfdBoard board2 = new PfdBoard();
						board2.setPfdCustomerInfoId(pfdCustomerInfoId);
						board2.setBoardType(EnumPfdBoardType.FINANCE.getType());
						board2.setBoardContent(content2);
						board2.setStartDate(startDate);
						board2.setEndDate(endDate);
						board2.setHasUrl("n");
						board2.setIsSysBoard("n");
						board2.setDeleteId(data.getId().toString());
						board.setMsgPrivilege(msgPrivilege);
						
						board2.setCreateDate(createDate);
						
						pfdBoardService.save(board2);
					}
					
				}
			}
			

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

        log.info("====PfdNoticeJob.invoiceNotice() end====");
    }

    /**
     * 每日確認 PFP 廣告是否到期，廣告結束日 (前三日)，寫 PFD 公告通知所屬經銷商
     */
    public void adOvertimeNotice() {
        log.info("====PfdNoticeJob.adOvertimeNotice() start====");

    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	String targetDate = sdf.format(new Date(new Date().getTime() + 1000*60*60*24*3)); //3天後

    	//PFD 公告時間
    	Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 1000*60*60*24*7);

    	//撈出即將下線的廣告
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("adActionStatus", "" + EnumAdStatus.Open.getStatusId());
		conditionMap.put("adActionEndDate", targetDate);

		try {
			log.info(" pfpAdActionService: "+pfpAdActionService);
			List<PfpAdAction> adActionList = pfpAdActionService.getAdActionByConditions(conditionMap);

			PfpAdAction pfpAdAction = null;
			String pfpCustomerInfoId = null;
			String pfpCustomerInfoName = null;
			String boardContent = null; //公告內容
			String adActionSeq = null;
			String adActionName = null;
			for (int i=0; i<adActionList.size(); i++) {
				pfpAdAction = adActionList.get(i);
				pfpCustomerInfoId = pfpAdAction.getPfpCustomerInfo().getCustomerInfoId();
				pfpCustomerInfoName = pfpAdAction.getPfpCustomerInfo().getCustomerInfoTitle();
				adActionSeq = pfpAdAction.getAdActionSeq();
				adActionName = pfpAdAction.getAdActionName();

				boardContent = EnumCategory.AD_EXPIRE.getBoardContent();
				boardContent = "<a href=\"http://show.pchome.com.tw/adAdEdit.html?adSeq=" + adActionSeq + "\">" + adActionName + "</a>" + boardContent;

				//寫 PFP 公告
				boardProvider.add(pfpCustomerInfoId, boardContent, EnumBoardType.AD, EnumCategory.AD_EXPIRE, adActionSeq);

				//查看是否有經銷商，有的話也要發經銷商公告
				List<PfdUserAdAccountRef> PfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpCustomerInfoId);

				if (PfdUserAdAccountRefList.size()>0) {

					String pfdCustomerInfoId = PfdUserAdAccountRefList.get(0).getPfdCustomerInfo().getCustomerInfoId();
					String pfdUserId = PfdUserAdAccountRefList.get(0).getPfdUser().getUserId();

					//PFD 公告
					String content = "廣告帳戶 " + pfpCustomerInfoName + " 廣告 " + adActionName + " 即將下架！";
					//若要做超連結，還包含切換帳戶的動作，應該有困難
					//content += "<a href=\"http://show.pchome.com.tw/xxx.html?seq=" + ??? + "\">查看</a>";

					PfdBoard board = new PfdBoard();
					board.setBoardType(EnumPfdBoardType.AD.getType());
					board.setBoardContent(content);
					board.setPfdCustomerInfoId(pfdCustomerInfoId);
					board.setPfdUserId(pfdUserId);
					board.setStartDate(startDate);
					board.setEndDate(endDate);
					board.setHasUrl("n");
					board.setIsSysBoard("n");
					board.setUrlAddress(null);
					board.setCreateDate(startDate);

					pfdBoardService.save(board);
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

        log.info("====PfdNoticeJob.adOvertimeNotice() end====");
    }

    public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

    public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfpAdActionService(IPfpAdActionService pfpAdActionService) {
		this.pfpAdActionService = pfpAdActionService;
	}

	public void setAkbPfdServer(String akbPfdServer) {
		this.akbPfdServer = akbPfdServer;
	}

	public void setPfdBonusInvoiceService(
			IPfdBonusInvoiceService pfdBonusInvoiceService) {
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}

	public static void main(String[] args) {
    	ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(new String[]{"local"}));
        //ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        PfdNoticeJob job = context.getBean(PfdNoticeJob.class);
        job.adOvertimeNotice();
    }
}
