package com.pchome.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.quartzs.AdjustAccountJob;

public class MainTestClass {

	public static void main(String[] args) {
		
		//String[] test = {"local"};
		//ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));
		
		/**  test class function
		
		PfdAdActionReportService job = context.getBean(PfdAdActionReportService.class);
		
		List<MonthBonusDetailVo> vos = job.findPfdAdActionReportToVo("PFDC20141111001", "2014-10-26", "2014-11-25", "2");
		
		System.out.println(" vos : "+vos);
		*/
		
		/** test pfd bonusJob 
		PfdBonusJob job = context.getBean(PfdBonusJob.class);
		String date = "2014-09-26";
		job.manual(date);
		*/
		
		 
		//公式：含稅廣告點擊費 = 含稅餘額 - ((未含稅餘額 - 未含稅廣告點擊費) * 1.05) 
		float adClkPrice = 30;
		float orderRemain = 2652;
		float taxRemain = 133;
		
		float taxAdClkPrice = 0;
		
		if(taxRemain <= 0){
			taxAdClkPrice = adClkPrice;
		}else{
			taxAdClkPrice = (float)((orderRemain+taxRemain) - Math.round((orderRemain - adClkPrice)*1.05));
		}
		float tax = taxAdClkPrice-adClkPrice;	
		
		System.out.print(" taxAdClkPrice: "+taxAdClkPrice);
		
		System.out.print(" tax: "+tax);
		
		
		taxRemain = taxRemain - tax;
		
		System.out.print(" taxRemain: "+taxRemain);
		/**
		AdjustAccountJob job = context.getBean(AdjustAccountJob.class);
		String date = "2015-05-12";
		try {
			job.reProcess(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
