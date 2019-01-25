package com.pchome.akbadm.factory.settlement;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.IBoardProvider;

/**
 * 預付退款計算
 * 1.取當天預付退款完成的全部訂單
 * 2.寫入TransDetail預付退款資料
 * 3.更新 PFPCustomerInfo總加值金額與餘額
 */
public class SettlementAdvanceRefund extends ASettlement {

	private IPfpRefundOrderService pfpRefundOrderService;
	private IBoardProvider boardProvider;
	private IPfdBoardService pfdBoardService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	
	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {
		
		String customerInfoId = vo.getCustomerInfoId();
		List<PfpRefundOrder> list = null;
		
		try {
			//撈當日PM於adm已准核的預付退款
			list = pfpRefundOrderService.findAdvanceRefundOrder(customerInfoId, transDate);
			
			if(!list.isEmpty()){
				float refundMoney = 0;
				float totalSavePrice = vo.getTotalAddMoney();
				float remain = vo.getRemain();
				float tax = 0;
				
				for(PfpRefundOrder pfpRefundOrder:list){
					//加總當日已核准退款的總金額
					refundMoney += pfpRefundOrder.getRefundPrice();
				}
				
				vo.setTransPrice(refundMoney);

				// 更新總加值金額
				totalSavePrice = vo.getTotalAddMoney() - refundMoney;	//用前一筆預付儲值金減今日預付總退款金額
				// 更新帳戶餘額
				remain = vo.getRemain() - refundMoney;					//用前一筆預付餘額減今日預付總退款金額
				
				vo.setTotalAddMoney(totalSavePrice);
				vo.setRemain(remain);
				
				tax=(float) (refundMoney * 0.05);						//算今日預付總退款金額稅額(正式機有小數)
				vo.setTax(tax);
			}
		} catch (Exception e) {
			log.error(customerInfoId, e);
		}	
	}

	
	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {
		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.REFUND.getChName());
			transDetail.setTransType(EnumTransType.REFUND.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.REFUND.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTotalSavePrice(vo.getTotalAddMoney());	//總加值金額
			transDetail.setRemain(vo.getRemain());					//帳戶餘額
			transDetail.setTax(vo.getTax());						//營業稅

			super.transDetailService.saveOrUpdate(transDetail);
		}
	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		//預付退款已在PM於adm准核退款當下就更新PFP帳戶餘額與加值總額了，排程可再更新一次
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);
	}

	@Override
	public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
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
