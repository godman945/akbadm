package com.pchome.akbadm.factory.settlement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.order.IPfpOrderService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.db.vo.SettlementVO;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;

/**
 * 帳戶加值計算
 * 1. 取加值成功能訂單
 * 2. 更新 VO
 * 3. 更新交易明細
 * 4. 更新帳戶餘額
 */
public class SettlementSaveMoney extends ASettlement{

	private IPfpOrderService orderService;
	private IBoardProvider boardProvider;
	private IPfdBoardService pfdBoardService;

	@Override
	public void updateSettlementVO(SettlementVO vo, Date transDate) {

		String customerInfoId = vo.getCustomerInfoId();

		List<PfpOrder> orders = orderService.successOrder(customerInfoId, transDate);

//		log.info(">>> orders: "+orders);

		if(!orders.isEmpty()){

			float saveMoney = 0;
			float tax = 0;
			float totalSavePrice = 0;
			float remain = 0;

			for(PfpOrder order:orders){
				// 當日加值成功金額
				saveMoney += order.getOrderPrice();
				tax += order.getTax();
			}

			vo.setTransPrice(saveMoney);

			// 更新總加值金額
			totalSavePrice = vo.getTotalAddMoney() + saveMoney;
			// 更新帳戶餘額
			remain = vo.getRemain() + saveMoney;

			vo.setTotalAddMoney(totalSavePrice);
			vo.setTax(tax);
			vo.setRemain(remain);
		}
	}

	@Override
	public void updateTransDetail(SettlementVO vo, PfpTransDetail transDetail) {

		if(vo.getTransPrice() > 0){

			transDetail.setTransContent(EnumTransType.ADD_MONEY.getChName());
			transDetail.setTransType(EnumTransType.ADD_MONEY.getTypeId());
			transDetail.setIncomeExpense(EnumTransType.ADD_MONEY.getOperator());
			transDetail.setTransPrice(vo.getTransPrice());
			transDetail.setTax(vo.getTax());
			transDetail.setTotalSavePrice(vo.getTotalAddMoney());
			transDetail.setRemain(vo.getRemain());

			super.transDetailService.saveOrUpdate(transDetail);
		}

	}

	@Override
	public void updateCustomerInfo(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
		customerInfo.setRemain(transDetail.getRemain());
		customerInfo.setLaterRemain(transDetail.getRemain());
		customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
		customerInfo.setUpdateDate(new Date());

		super.customerInfoService.saveOrUpdate(customerInfo);

		if(customerInfo.getRemain() > 0){
			// 刪除餘額不足公告
			boardProvider.delete(customerInfo.getCustomerInfoId(), EnumCategory.REMAIN_NOT_ENOUGH);
			
			Set<PfdUserAdAccountRef> set = customerInfo.getPfdUserAdAccountRefs();
			List<PfdUserAdAccountRef> list = new ArrayList<PfdUserAdAccountRef>(set);
			
			//查看是否有經銷商，有的話也要刪除經銷商公告
			try {
				if(list.size()>0){
					pfdBoardService.deletePfdBoardByDeleteId(customerInfo.getCustomerInfoId());
				}
			} catch (Exception e) {
				log.error(customerInfo.getCustomerInfoId(), e);
			}
		}
	}

    @Override
    public PfpCustomerInfo updateCustomerInfo2(PfpCustomerInfo customerInfo, PfpTransDetail transDetail) {
        customerInfo.setRemain(transDetail.getRemain());
        customerInfo.setLaterRemain(transDetail.getRemain());
        customerInfo.setTotalAddMoney(transDetail.getTotalSavePrice());
        customerInfo.setUpdateDate(new Date());

        return customerInfo;
    }

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		super.transDetailService = transDetailService;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		super.customerInfoService = customerInfoService;
	}

	public void setOrderService(IPfpOrderService orderService) {
		this.orderService = orderService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

}
