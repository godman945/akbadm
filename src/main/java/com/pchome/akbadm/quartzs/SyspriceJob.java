package com.pchome.akbadm.quartzs;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.PfpAdSysprice;
import com.pchome.akbadm.db.pojo.PfpKeywordSysprice;
import com.pchome.akbadm.db.service.ad.PfpAdDetailService;
import com.pchome.akbadm.db.service.ad.PfpAdKeywordService;
import com.pchome.akbadm.db.service.sysprice.PfpAdSyspriceService;
import com.pchome.akbadm.db.service.sysprice.PfpKeywordSyspriceService;
import com.pchome.soft.util.DateValueUtil;

public class SyspriceJob {

	protected Logger log = LogManager.getRootLogger();
	
	private PfpAdDetailService adDetailService;
	private PfpAdKeywordService adKeywordService;
	private PfpAdSyspriceService adSyspriceService;
	private PfpKeywordSyspriceService keywordSyspriceService;
	
	private String sysPriceAdPoolSeq;
	
	public void syspriceProcess() throws Exception{
		log.info("================= syspriceProcess start ==========================");
		this.updateAdSysprice();
		
		this.updateKeywordSysprice();
		log.info("================= syspriceProcess end ==========================");
	}
	
	public void newSyspriceProcess() throws Exception{
		log.info("================= newSyspriceProcess start ==========================");
		this.updateNewAdSysprice();
		log.info("================= newSyspriceProcess end ==========================");
	}
	
	private void updateAdSysprice() throws Exception{	
		
		// 每小時更新 Ad 系統價格 
		List<PfpAdSysprice> adSysprices = adSyspriceService.getAdSyspriceList();
		String today = DateValueUtil.getInstance().dateToString(new Date());
		
		for(PfpAdSysprice adSysprice:adSysprices){
			float sysprice = adDetailService.countAdSysprice(adSysprice.getAdPoolSeq(), adSysprice.getSysprice(), today);
			adSysprice.setSysprice(sysprice);
			adSyspriceService.saveOrUpdate(adSysprice);
		}
	}
	
	private void updateKeywordSysprice() throws Exception{
		
		// 每小時更新 keyword 系統價格 
		List<PfpKeywordSysprice> keywordSysprices = keywordSyspriceService.getKeywordSyspriceList();
		String today = DateValueUtil.getInstance().dateToString(new Date());
		
		for(PfpKeywordSysprice keywordSysprice:keywordSysprices){
			float sysprice = adKeywordService.countKeywordSysprice(keywordSysprice.getKeyword(), keywordSysprice.getSysprice(), today);
			keywordSysprice.setSysprice(sysprice);
			keywordSyspriceService.saveOrUpdate(keywordSysprice);
		}
	}
	
	private void updateNewAdSysprice() throws Exception {
		
		String day = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		
		PfpAdSysprice pfpAdSysprice = adSyspriceService.getAdSysprice(sysPriceAdPoolSeq);
		
		float newAdSysprice = adSyspriceService.getNewAdSysprice(day);
		
		pfpAdSysprice.setSysprice(newAdSysprice);
		pfpAdSysprice.setUpdateDate(new Date());
		adSyspriceService.saveOrUpdate(pfpAdSysprice);
	}
	
	
	
	
	
	
	
	public void setAdDetailService(PfpAdDetailService adDetailService) {
		this.adDetailService = adDetailService;
	}

	public void setAdKeywordService(PfpAdKeywordService adKeywordService) {
		this.adKeywordService = adKeywordService;
	}
	
	public void setAdSyspriceService(PfpAdSyspriceService adSyspriceService) {
		this.adSyspriceService = adSyspriceService;
	}
	
	public void setKeywordSyspriceService(PfpKeywordSyspriceService keywordSyspriceService) {
		this.keywordSyspriceService = keywordSyspriceService;
	}

	public void setSysPriceAdPoolSeq(String sysPriceAdPoolSeq) {
		this.sysPriceAdPoolSeq = sysPriceAdPoolSeq;
	}
	
}
