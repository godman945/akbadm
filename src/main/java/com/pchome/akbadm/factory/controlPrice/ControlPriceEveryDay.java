package com.pchome.akbadm.factory.controlPrice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.api.ControlPriceAPI;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.ad.IPfpAdInvalidService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.vo.ControlPriceVO;
import com.pchome.soft.util.DateValueUtil;

public class ControlPriceEveryDay extends AControlPrice{

	protected Logger log = LogManager.getRootLogger();

	private IPfpAdPvclkService adPvclkService;
	private IPfpAdInvalidService adInvalidService;
	//private float totalControlPrice;

    /**
     * 1. 實際花費費用  = 昨天廣告費用 - 惡意點擊費用
     * 2. 調控金額 = (實際花費費用 - 每日最大花費)*2 (實際花費費用 > 每日最大花費 )
     * 3. 調控金額 = 每日最大花費 (實際花費費用 < 每日最大花費 )
     * 4. 調控金額小於預設金額 3塊，調控金額為3塊
     * 5. 調控金額加總不得超過帳戶餘額，如果超過依比例調整調控金額
     */
	@Override
	public List<ControlPriceVO> countControlPrice(PfpCustomerInfo customerInfo) {
		//totalControlPrice = 0;
		String date = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		Date yesterday = DateValueUtil.getInstance().stringToDate(date);
		List<ControlPriceVO> vos = new ArrayList<ControlPriceVO>();

		List<Object> objects = adPvclkService.adActionPvclkSum(customerInfo.getCustomerInfoId(), yesterday);

		for(Object object:objects){
			Object[] ob = (Object[])object;

			float controlPrice = 0;
			ControlPriceVO vo = new ControlPriceVO();

			if(ob[0] != null){

				vo.setAdActionSeq(ob[0].toString());
				vo.setAdActionMax(Float.parseFloat(ob[1].toString()));
				vo.setAdActionCost(Float.parseFloat(ob[2].toString()));

				this.getInvalidPvclkCost(vo.getAdActionSeq(), yesterday, vo);

				if(ob[4] != null){
					vo.setChangeMax(ob[4].toString());
				}
				
				//log.info("== count before ==");
				//log.info(" adActionSeq = "+vo.getAdActionSeq());
				//log.info(" adActionMax = "+vo.getAdActionMax());
				//log.info(" adActionCost = "+vo.getAdActionCost());
				//log.info(" adActionInvalidCost = "+vo.getAdActionInvalidCost());
				//log.info(" adActionRealCost = "+vo.getAdActionRealCost());
				//log.info(" adActionControlPrice = "+vo.getAdActionControlPrice());

				//如果前一日有更動過每日預算，則調控金額為更動過後的每日預算
				if(StringUtils.equals(vo.getChangeMax(), "Y")){
					controlPrice = vo.getAdActionMax();
				} else {
					if(vo.getAdActionRealCost() > vo.getAdActionMax()){
						
						// 調控金額 = 每日花費上限 - ((當日實際花費 - 每日花費上限) * 2)
						controlPrice = vo.getAdActionMax() - ((vo.getAdActionRealCost() - vo.getAdActionMax())*2);
						
						if(controlPrice < definePrice){
							controlPrice = definePrice;
						}
						
					}
					else{
						// 調控金額 = 每日花費上限
						controlPrice = vo.getAdActionMax();
					}
				}
				

				vo.setAdActionControlPrice(controlPrice);

				//log.info("== count after ==");
				//log.info(" adActionMax = "+vo.getAdActionMax());
				//log.info(" adActionCost = "+vo.getAdActionCost());
				//log.info(" adActionInvalidCost = "+vo.getAdActionInvalidCost());
				//log.info(" adActionRealCost = "+vo.getAdActionRealCost());
				//log.info(" adActionControlPrice = "+vo.getAdActionControlPrice());

				//totalControlPrice += controlPrice;

				vos.add(vo);
			}

		}

		return vos;
	}

//	@Override
//	public float countTotalControlPrice() {
//		// TODO Auto-generated method stub
//		return totalControlPrice;
//	}

	private void getInvalidPvclkCost(String adActionSeq, Date date, ControlPriceVO vo) {

		List<Object> objects = adInvalidService.adActionInvalidSum(adActionSeq, date);

		if(objects.get(0) != null){
			vo.setAdActionInvalidCost(Float.parseFloat(objects.get(0).toString()));
		}

		float adActionRealCost = vo.getAdActionCost() - vo.getAdActionInvalidCost();
		vo.setAdActionRealCost(adActionRealCost);

	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}

	public void setAdActionService(IPfpAdActionService adActionService) {
		this.adActionService = adActionService;
	}

	public void setControlPriceAPI(ControlPriceAPI controlPriceAPI) {
		this.controlPriceAPI = controlPriceAPI;
	}

	public void setAdPvclkService(IPfpAdPvclkService adPvclkService) {
		this.adPvclkService = adPvclkService;
	}

	public void setAdInvalidService(IPfpAdInvalidService adInvalidService) {
		this.adInvalidService = adInvalidService;
	}

	public void setDefinePrice(float definePrice) {
		this.definePrice = definePrice;
	}

}
