package com.pchome.akbadm.factory.controlPrice;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.api.ControlPriceAPI;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.vo.ControlPriceVO;

public abstract class AControlPrice {

	protected Logger log = LogManager.getRootLogger();

	protected IPfpCustomerInfoService customerInfoService;
	protected IPfpAdActionService adActionService;
	protected ControlPriceAPI controlPriceAPI;
	protected float definePrice;

	public abstract List<ControlPriceVO> countControlPrice(PfpCustomerInfo customerInfo);

	//public abstract float countTotalControlPrice();

	@Transactional
	public void contrlPrice(PfpCustomerInfo customerInfo) {

		// 先計算在走期中的調控金額
		List<PfpAdAction> adActions = controlPriceAPI.countProcess(customerInfo);

		float remain = customerInfo.getRemain();

		if(remain > definePrice && !adActions.isEmpty()){

//			log.info(" adActions size = "+adActions.size());

			List<ControlPriceVO> vos = this.countControlPrice(customerInfo);

			for(PfpAdAction adAction:adActions){
				for(ControlPriceVO vo:vos){
					if(vo.getAdActionSeq().equals(adAction.getAdActionSeq())){
//						log.info(" vo ControlPrice() = "+vo.getAdActionControlPrice());
//						log.info(" adAction ControlPrice() = "+adAction.getAdActionControlPrice());
						if(vo.getAdActionControlPrice() < adAction.getAdActionControlPrice() || StringUtils.equals(vo.getChangeMax(), "Y")){
							adAction.setAdActionControlPrice(vo.getAdActionControlPrice());
							adAction.setAdActionUpdateTime(new Date());
						}

						break;
					}
				}

				adAction.setChangeMax(null);
				
//				log.info(" lastControlPrice = "+adAction.getAdActionControlPrice());
			    adActionService.saveOrUpdate(adAction);
			}
//			float totalControlPrice = this.countTotalControlPrice();
//
//			log.info(" totalControlPrice = "+totalControlPrice);
//
//			if(totalControlPrice < remain){
//
//				if(this.isUpdate()){
//					this.updateControlPrice(vos);
//				}
//
//			}
//			else{
//				this.updateControlPrice(vos, totalControlPrice, customerInfo.getRemain());
//			}
		}


	}


	public boolean isUpdate(){
		return true;
	}

//	private void updateControlPrice(List<ControlPriceVO> vos) {
//
//		for(ControlPriceVO vo:vos){
//
//			PfpAdAction adAction = adActionService.getAdAction(vo.getAdActionSeq());
//			adAction.setAdActionControlPrice(vo.getAdActionControlPrice());
//			adAction.setAdActionUpdateTime(new Date());
//			adActionService.saveOrUpdate(adAction);
//		}
//
//	}
//
//	private void updateControlPrice(List<ControlPriceVO> vos, float totalControlPrice, float remain) {
//
//		// 依實際調控金額比率計算調控金額
//		for(ControlPriceVO vo:vos){
//
//			// 計算後調控金額 = (原本調控金額 / 調控金額加總) * 帳戶餘額
//			float controlPrice = (vo.getAdActionControlPrice() / totalControlPrice) * remain;
//
//			if(controlPrice < definePrice){
//				controlPrice = definePrice;
//			}
//
//			PfpAdAction adAction = adActionService.getAdAction(vo.getAdActionSeq());
//			adAction.setAdActionControlPrice(controlPrice);
//			adAction.setAdActionUpdateTime(new Date());
//			adActionService.saveOrUpdate(adAction);
//		}
//	}

}
