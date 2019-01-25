package com.pchome.akbadm.factory.settlement;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;

/**
 * 後付退款計算
 * 1.取當天尚未退款的訂單
 * 2.餘額大於等於退款金額則退款，退款單改為退款完成，並發公告
 * 3.餘額小於退款金額則不退款，退款單改為退款失敗，並發公告
 * 4.補(重跑)交易明細時取退款完成的退款單，不再發公告
 */
public class SettlementLaterRefund extends ASettlement {

	private IPfpRefundOrderService pfpRefundOrderService;
	private IBoardProvider boardProvider;
	private IPfdBoardService pfdBoardService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	
	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {
		
		DecimalFormat df1 = new DecimalFormat("###,###,###,###");
		
		String customerInfoId = vo.getCustomerInfoId();
		PfpCustomerInfo pfpCustomerInfo = super.customerInfoService.getCustomerInfo(customerInfoId);
		Set<PfdUserAdAccountRef> set = pfpCustomerInfo.getPfdUserAdAccountRefs();
		List<PfdUserAdAccountRef> pfdUserAdAccountRefList = new ArrayList<PfdUserAdAccountRef>(set);
		
		PfdCustomerInfo pfdCustomerInfo = null;
		PfdUser pfdUser = null;
		if(pfdUserAdAccountRefList.size()>0){
			pfdCustomerInfo = pfdUserAdAccountRefList.get(0).getPfdCustomerInfo();
			pfdUser = pfdUserAdAccountRefList.get(0).getPfdUser();
		}
		
		
		List<PfpRefundOrder> list = null;
		try {
			list = pfpRefundOrderService.findTransRefundOrder(customerInfoId, transDate);
			
			if(!list.isEmpty()){
				
				float refundMoney = 0;
				float addPfdRemainQuota = 0;
				float totalLaterSave = vo.getTotalLaterAddMoney();
				float remain = vo.getRemain();
				boolean openPfp = false;
				
				for(PfpRefundOrder pfpRefundOrder:list){
					if(StringUtils.equals(EnumPfpRefundOrderStatus.NOT_REFUND.getStatus(), pfpRefundOrder.getRefundStatus())){
						openPfp = true;
						boolean refundcheck = true;
						String note = "";
						
						if((remain - refundMoney) < pfpRefundOrder.getRefundPrice()){
							refundcheck = false;
							note += "餘額不足/";
						}
						
						if(!StringUtils.equals(EnumAccountStatus.CLOSE.getStatus(), pfpCustomerInfo.getStatus())){
							refundcheck = false;
							note += "帳戶為未關閉/";
						}
						
						String pfdBoardContent = "";
						String pfpBoardContent = "";
						
						if(refundcheck){
							
							refundMoney += pfpRefundOrder.getRefundPrice();
							addPfdRemainQuota += pfpRefundOrder.getRefundPrice();	//尚未退款的資料才需補回pfd帳戶可用餘額
							
							pfpRefundOrder.setRefundStatus(EnumPfpRefundOrderStatus.SUCCESS.getStatus());
							pfpRefundOrderService.saveOrUpdate(pfpRefundOrder);
							
							pfdBoardContent = pfpCustomerInfo.getCustomerInfoTitle() + "已完成退款 $" + df1.format(pfpRefundOrder.getRefundPrice()) + "，詳細請到報表管理查詢。";
							pfpBoardContent = "帳戶已完成退款 $" + df1.format(pfpRefundOrder.getRefundPrice()) + "，詳細請到帳單管理查詢。";
							
							this.writePfdBoard(pfdCustomerInfo, pfdUser, pfdBoardContent);
							this.writePfpBoard(pfpCustomerInfo, pfpBoardContent);
						} else {
							pfpRefundOrder.setRefundStatus(EnumPfpRefundOrderStatus.FAIL.getStatus());
							pfpRefundOrder.setRefundNote(note);
							pfpRefundOrderService.saveOrUpdate(pfpRefundOrder);
							
							pfdBoardContent = pfpCustomerInfo.getCustomerInfoTitle() + "退款 $" + df1.format(pfpRefundOrder.getRefundPrice()) + " 失敗，請重新申請退款。";
							pfpBoardContent = "帳戶退款 $" + df1.format(pfpRefundOrder.getRefundPrice()) + " 失敗，請重新申請退款。";
							
							this.writePfdBoard(pfdCustomerInfo, pfdUser, pfdBoardContent);
							this.writePfpBoard(pfpCustomerInfo, pfpBoardContent);
						}
					} else if(StringUtils.equals(EnumPfpRefundOrderStatus.SUCCESS.getStatus(), pfpRefundOrder.getRefundStatus())){	//補資料用
						refundMoney += pfpRefundOrder.getRefundPrice();
					}
				}
				
				vo.setTransPrice(refundMoney);

				// 更新總加值金額
				totalLaterSave = vo.getTotalLaterAddMoney() - refundMoney;
				// 更新帳戶餘額
				remain = vo.getRemain() - refundMoney;

				vo.setTotalLaterAddMoney(totalLaterSave);
				vo.setRemain(remain);
				
				//更新pfd後付餘額
				if(addPfdRemainQuota > 0){
					int pfdRemainQuota = pfdCustomerInfo.getRemainQuota();		//剩餘可用金額
					int totalPfpQuota = pfdCustomerInfo.getTotalPfpQuota();		//總分配給PFP的金額
					pfdRemainQuota += (int) addPfdRemainQuota;
					totalPfpQuota -= (int) addPfdRemainQuota;
					pfdCustomerInfo.setRemainQuota(pfdRemainQuota);
					pfdCustomerInfo.setTotalPfpQuota(totalPfpQuota);
					pfdCustomerInfoService.saveOrUpdate(pfdCustomerInfo);
				}
				
				//先將帳戶設為開啟
				if(StringUtils.equals(EnumAccountStatus.CLOSE.getStatus(), pfpCustomerInfo.getStatus()) && openPfp){
					pfpCustomerInfo.setStatus(EnumAccountStatus.START.getStatus());
					super.customerInfoService.saveOrUpdate(pfpCustomerInfo);
				}
			}
		} catch (Exception e) {
			log.error(customerInfoId, e);
		}	
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
			
			pfdBoard.setBoardType(EnumPfdBoardType.REMIND.getType());
			pfdBoard.setBoardContent(boardContent);
			pfdBoard.setPfdCustomerInfoId(pfdCustomerInfo.getCustomerInfoId());
			pfdBoard.setPfdUserId(pfdUser.getUserId());
			pfdBoard.setStartDate(startDate);
			pfdBoard.setEndDate(endDate);
			pfdBoard.setIsSysBoard("n");
			pfdBoard.setHasUrl("n");
			pfdBoard.setUrlAddress(null);
			pfdBoard.setDeleteId(null);
    		
    		//觀看權限(總管理者/帳戶管理/行政管理/業務管理)
			String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege() + "||"
					 + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.SALES_MANAGER.getPrivilege();
			pfdBoard.setMsgPrivilege(msgPrivilege);
    		
			pfdBoard.setCreateDate(createDate);
    		pfdBoardService.save(pfdBoard);
		}
	}
	
	private void writePfpBoard(PfpCustomerInfo pfpCustomerInfo, String boardContent) throws Exception{   
        boardProvider.add(pfpCustomerInfo.getCustomerInfoId(), boardContent, "", EnumBoardType.FINANCE, EnumCategory.OTHER, null);
	}
	
	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {
		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.LATER_REFUND.getChName());
			transDetail.setTransType(EnumTransType.LATER_REFUND.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.LATER_REFUND.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTotalLaterSave(vo.getTotalLaterAddMoney());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

	@Override
	public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalLaterAddMoney(transDetail.getTotalLaterSave());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
	}

	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setPfdCustomerInfoService(IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}
	
	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}
}
