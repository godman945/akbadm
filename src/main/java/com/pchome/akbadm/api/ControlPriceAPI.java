package com.pchome.akbadm.api;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;

public class ControlPriceAPI {

	protected Log log = LogFactory.getLog(this.getClass());

	private IPfpAdActionService adActionService;

	private float totalControlPrice;
	private float totalMaxPrice;

	private float definePrice;


	public List<PfpAdAction> countProcess(PfpCustomerInfo customerInfo) {
		// 重新計算帳戶下走期中的活動調控金額
		String customerInfoId = customerInfo.getCustomerInfoId();
		float remain = customerInfo.getRemain();
		List<PfpAdAction> adActions = null;
		// 餘額大於 3塊才計算
		if(remain > definePrice){

			adActions = adActionService.findBroadcastAdAction(customerInfoId);

			this.countControlPrice(adActions);

//			log.info(" remain = "+remain);
//			log.info(" totalMaxPrice = "+totalMaxPrice);
//			log.info(" totalControlPrice = "+totalControlPrice);
//			log.info("========================");

			// 總每日上限金額大於餘額
			if(remain < totalMaxPrice){

				// 依每日上限金額比率計算調控金額
				for(PfpAdAction adAction:adActions){

					// 計算後調控金額 = (原本每日上限金額 / 每日上限金額加總) * 帳戶餘額
					float controlPrice = (adAction.getAdActionMax() / totalMaxPrice) * remain;
//					log.info(" adAction = "+adAction.getAdActionSeq());
//					log.info(" AdActionStatus = "+adAction.getAdActionStatus());
//					log.info(" start = "+adAction.getAdActionStartDate());
//					log.info(" end = "+adAction.getAdActionEndDate());
//					log.info(" controlPrice = "+controlPrice);
					//log.info(" before controlPrice = "+adAction.getAdActionControlPrice());
					//log.info(" after controlPrice = "+controlPrice);
					//float updateControlPrice = this.updateControlPrice(adAction.getAdActionControlPrice(), controlPrice);

					adAction.setAdActionControlPrice(controlPrice);
					adAction.setAdActionUpdateTime(new Date());

					//adActionService.saveOrUpdate(adAction);

				}

			}else if(remain > totalMaxPrice){

				// 設回每日最大上限金額
				for(PfpAdAction adAction:adActions){

//					log.info(" controlPrice = "+adAction.getAdActionMax());

					//float updateControlPrice = this.updateControlPrice(adAction.getAdActionControlPrice(), adAction.getAdActionMax());

					adAction.setAdActionControlPrice(adAction.getAdActionMax());
					adAction.setAdActionUpdateTime(new Date());

					//adActionService.saveOrUpdate(adAction);
				}

			}

//			log.info("========================");
		}

		return adActions;
	}

	private void countControlPrice(List<PfpAdAction> adActions) {

		totalControlPrice = 0;
		totalMaxPrice = 0;

		for(PfpAdAction adAction:adActions){
			//log.info(" adActionMax = "+adAction.getAdActionMax());
			//log.info(" ControlPrice = "+adAction.getAdActionControlPrice());

			totalControlPrice += adAction.getAdActionControlPrice();
			totalMaxPrice += adAction.getAdActionMax();
		}

		//log.info("========================");
	}

//	private float updateControlPrice(float controlPrcie, float countPrice){
//
//		float price = countPrice;
//
//		if(countPrice > controlPrcie){
//			price = controlPrcie;
//		}
//		return price;
//	}

	public void setAdActionService(IPfpAdActionService adActionService) {
		this.adActionService = adActionService;
	}

	public void setDefinePrice(float definePrice) {
		this.definePrice = definePrice;
	}

}
