package com.pchome.akbadm.factory.notice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.mailbox.EnumCategory;

/**
 * 餘額低於 3 元時，發出餘額不足通知
 */
public class NoticeRemainNotEnough extends ANotice {

	private int LIMIT_OF_REMAIN = 3; //餘額條件設定

	private EnumCategory enumCategory = EnumCategory.REMAIN_NOT_ENOUGH;
    private EnumBoardType enumBoardType = EnumBoardType.FINANCE;

    private IPfdBoardService pfdBoardService;
    private IPfdUserAdAccountRefService pfdUserAdAccountRefService;

    @Override
    public void notice() {

    	if (!enumCategory.isBoard() && !enumCategory.isMailbox()) {
            return;
        }

        List<PfpCustomerInfo> targetList = new ArrayList<PfpCustomerInfo>();

        List<PfpCustomerInfo> list = pfpCustomerInfoService.selectCustomerInfo(EnumAccountStatus.START);

        for (PfpCustomerInfo pfpCustomerInfo: list) {
        	if (pfpCustomerInfo.getRemain() < LIMIT_OF_REMAIN) {
        		targetList.add(pfpCustomerInfo);
        	}
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        Date createDate = new Date();
        Date startDate = null;
        Date endDate = null;

        try {
    		startDate = sdf.parse(sdf.format(createDate));
    		Calendar c = Calendar.getInstance();
    		c.add(Calendar.MONTH, 6);
    		endDate = sdf.parse(sdf.format(c.getTime()));
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }

        for (PfpCustomerInfo pfpCustomerInfo: targetList) {

            try {

                //pfp board
                addBoard(pfpCustomerInfo, enumBoardType, enumCategory);

                //pfp mailbox
                addMailbox(pfpCustomerInfo, enumCategory);

    			//查看是否有經銷商，有的話也要發經銷商公告
    			List<PfdUserAdAccountRef> PfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpCustomerInfo.getCustomerInfoId());

    			if (PfdUserAdAccountRefList.size()>0) {

    				String pfdCustomerInfoId = PfdUserAdAccountRefList.get(0).getPfdCustomerInfo().getCustomerInfoId();
    				String pfdUserId = PfdUserAdAccountRefList.get(0).getPfdUser().getUserId();

    				//檢查是否有發過公告
    				Map<String, String> conditionsMap = new HashMap<String, String>();
    				conditionsMap.put("content","%帳戶餘額不足%");
    				conditionsMap.put("deleteId",pfpCustomerInfo.getCustomerInfoId());
    				
    				List<PfdBoard> pfdBoardList = pfdBoardService.findPfdBoard(conditionsMap);
    				
    				//如果公告沒發過就要發
    				if(pfdBoardList.isEmpty()){
    					List<PfdBoard> addList = new ArrayList<PfdBoard>();
    					//PFD 公告
    					String content = "廣告帳戶 <span style=\"color:#1d5ed6;\">" + pfpCustomerInfo.getCustomerInfoTitle() + "</span> ，帳戶餘額不足，";
    					//content += "<a href=\"http://show.pchome.com.tw/xxx.html?seq=" + ??? + "\">查看</a>";
    					
    					if(StringUtils.equals("1", pfpCustomerInfo.getPayType())){
    						content += "<a href=\"addAdvanceMoney.html?adAccountId=" + pfpCustomerInfo.getCustomerInfoId() + "\" target=\"_blank\" >請立即進行加值</a>";
    					} else {
    						content += "<a href=\"./adAccountPay.html?openLaterPay=y&payCustomerInfoId=" + pfpCustomerInfo.getCustomerInfoId() + "\">請立即進行加值</a>";
    					}
    					content += "以便繼續播出廣告!";
    					
    					PfdBoard board = new PfdBoard();
    					board.setBoardType(EnumPfdBoardType.REMIND.getType());
    					board.setBoardContent(content);
    					board.setPfdCustomerInfoId(pfdCustomerInfoId);
    					//board.setPfdUserId(pfdUserId);
    					board.setStartDate(startDate);
    					board.setEndDate(endDate);
    					board.setIsSysBoard("n");
    					board.setHasUrl("n");
    					board.setUrlAddress(null);
    					board.setDeleteId(pfpCustomerInfo.getCustomerInfoId());
    					
    					//觀看權限(總管理者/帳戶管理/行政管理)
    					String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege()
    							 + "||" + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.BILL_MANAGER.getPrivilege();
    					board.setMsgPrivilege(msgPrivilege);
    					
    					board.setCreateDate(createDate);
    					
    					addList.add(board);
    					
    					//給業務看的
    					PfdBoard board2 = new PfdBoard();
    					board2.setBoardType(EnumPfdBoardType.REMIND.getType());
    					board2.setPfdCustomerInfoId(pfdCustomerInfoId);
    					board2.setPfdUserId(pfdUserId);
    					board2.setBoardContent("廣告帳戶 <span style=\"color:#1d5ed6;\">" + pfpCustomerInfo.getCustomerInfoTitle() + "</span> ，帳戶餘額不足，請立即進行加值以便繼續播出廣告!");
    					board2.setStartDate(startDate);
    					board2.setEndDate(endDate);
    					board2.setIsSysBoard("n");
    					board2.setHasUrl("n");
    					board2.setUrlAddress(null);
    					board2.setDeleteId(pfpCustomerInfo.getCustomerInfoId());
    					board2.setMsgPrivilege(EnumPfdPrivilege.SALES_MANAGER.getPrivilege().toString());
    					board2.setCreateDate(createDate);
    					
    					addList.add(board2);
    					
    					pfdBoardService.saveOrUpdateAll(addList);
    				}
    			}
            } catch (Exception e) {
                log.error(pfpCustomerInfo.getCustomerInfoId(), e);
            }
        }
    }

    @Override
    public String getMailContent() {
        return getMailContent(enumCategory);
    }

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}
}
