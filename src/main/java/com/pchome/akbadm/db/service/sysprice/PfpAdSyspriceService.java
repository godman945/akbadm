package com.pchome.akbadm.db.service.sysprice;

import java.util.List;

import com.pchome.akbadm.db.dao.sysprice.IPfpAdSyspriceDAO;
import com.pchome.akbadm.db.dao.sysprice.PfpAdSyspriceDAO;
import com.pchome.akbadm.db.pojo.PfpAdSysprice;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdSyspriceService extends BaseService<PfpAdSysprice, String> implements IPfpAdSyspriceService {
	
	private String definePrice;
	
    public PfpAdSysprice getAdSysprice(String adPoolSeq) throws Exception{

        PfpAdSysprice adSysprice = ((PfpAdSyspriceDAO) dao).getAdSysprice(adPoolSeq);

        if(adSysprice == null){

            adSysprice = new PfpAdSysprice();
            adSysprice.setAdPoolSeq(adPoolSeq);
            adSysprice.setSysprice(Float.parseFloat(definePrice));		// 廣告預設價錢
            adSysprice.setAmount(0);

            super.saveOrUpdate(adSysprice);
        }

        return adSysprice;

    }

    public List<PfpAdSysprice> getAdSyspriceList() throws Exception{
        return ((PfpAdSyspriceDAO) dao).getAdSyspriceList();
    }

    public PfpAdSysprice selectAdSyspriceByPoolSeq(String poolSeq) {
        List<PfpAdSysprice> list = ((IPfpAdSyspriceDAO) dao).selectAdSyspriceByPoolSeq(poolSeq);
        for (PfpAdSysprice pfpAdSysprice: list) {
            return pfpAdSysprice;
        }
        return null;
    }
    
    public float getNewAdSysprice(String day) throws Exception {
    	float adSysprice = ((IPfpAdSyspriceDAO) dao).getNewAdSysprice(day);
    	adSysprice += Float.parseFloat(definePrice);
    	return adSysprice;
    }
    
	public void setDefinePrice(String definePrice) {
		this.definePrice = definePrice;
	}
}