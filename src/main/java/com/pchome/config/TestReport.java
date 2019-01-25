package com.pchome.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.quartzs.PfpAdActionReportJob;

public class TestReport {

	private static String localWebContent = "WebContent/WEB-INF/src/";

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext();

		if (args.length>0) {
	           String envParam = args[0];
	           if (envParam.equals("stg")) {
	        	   // 正式機、測試機才用此列
	        	   context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	           } else if (envParam.equals("prd")) {
	        	   // 正式機、測試機才用此列
	        	   context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
				} else if (envParam.equals("local")) {
					// 本機用此列
					context = new FileSystemXmlApplicationContext(pathLocal);
	           } else {
	        	   System.out.println(">>> args[0] must be prd/stg");
	           }
		}
		
		System.out.println(">>> start");

		PfpAdActionReportJob job = context.getBean(PfpAdActionReportJob.class);
		System.out.println("job = " + job);

		//job.processPerHour();
		//job.processPerDay();
		job.processOldData();
		//job.processAllPfpReport("2013-10-01");
		//job.processAdGroupReport("2013-11-20");
		//job.processAdReport("2013-11-20");
		//job.processAdKeywordReport("2013-11-20");
		//job.processAdOsReport("2013-11-20");

//		if (args.length > 0) {
//			if (args[0].equals("day")) { //補指定日期
//				if (args.length > 1) {
//					job.processAllPfpReport(args[1]);
//				} else {
//					System.out.println("Plz input args[1]: date(yyyy-MM-dd)");     
//				}
//			} else if (args[0].equals("all")) { //補全部日期
//				job.processOldData();
//			} else {
//				System.out.println("args[0]: must be day or all");
//			}
//		} else {
//			System.out.println("Plz input parameters, args[0]: function(day or all), args[1]: date(yyyy-MM-dd)");
//		}

		System.out.println(">>> end");
	}


	private static String[] pathLocal = {
		localWebContent + "config/spring/main-stg-spring-log4j.xml",
		localWebContent + "config/spring/spring-action.xml",
		localWebContent + "config/spring/spring-api.xml",
		localWebContent + "config/spring/spring-dao.xml",
		localWebContent + "config/spring/spring-datasource.xml",
		localWebContent + "config/spring/spring-factory.xml",
		localWebContent + "config/spring/spring-hibernate.xml",
		localWebContent + "config/spring/spring-mail-config.xml",
		localWebContent + "config/spring/stg-spring-prop.xml",
		localWebContent + "config/spring/stg-spring-quartz-class.xml",
		localWebContent + "config/spring/spring-report.xml",
		localWebContent + "config/spring/spring-rmi-class.xml",
		localWebContent + "config/spring/spring-service.xml",
		localWebContent + "config/spring/spring-utils.xml"
    };
}
