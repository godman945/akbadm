package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.ad.PfpAdDetailDAO;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdDetailService extends BaseService<PfpAdDetail, String> implements IPfpAdDetailService {
	
	private String definePrice;
	
	public PfpAdDetail findAdContent(String adSeq, String defineAdSeq) throws Exception{
		return ((PfpAdDetailDAO)dao).findAdContent(adSeq, defineAdSeq);
	}
	
	public float countAdSysprice(String adPoolSeq, float sysprice, String today) throws Exception{
		
		List<Object> objects = ((PfpAdDetailDAO) dao).getAdPriceList(adPoolSeq, sysprice, today);

		float countPrice = Float.parseFloat(definePrice);		// 廣告預設價錢
		
		if(objects != null && objects.size() < sysprice){
			countPrice += objects.size();
		}
		
		//log.info(" today = "+today);
		//log.info(" ad definePrice = "+definePrice);
		//log.info(" ad size = "+objects.size());
		//log.info(" ad countPrice = "+countPrice);
		
		return countPrice;
	}

	public void setDefinePrice(String definePrice) {
		this.definePrice = definePrice;
	}

	public List<PfpAdDetail> getPfpAdDetails(String adDetailSeq, String adSeq, String adPoolSeq, String defineAdSeq) throws Exception{
		return ((PfpAdDetailDAO)dao).getPfpAdDetails(adDetailSeq, adSeq, adPoolSeq, defineAdSeq);
	}

}