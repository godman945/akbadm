package com.pchome.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.quartzs.VerifyAdIllegalKeyWordJob;

public class TestVerifyAdIllegalKeywordJob {

	private static String localWebContent = "WebContent/WEB-INF/src/";

	/**
	 * 此為手動審核排程
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext();

		if (args.length>0) {
			// 第一個值為主機名稱
			String envParam = args[0];
			if (envParam.equals("stg")) {
	        	// 測試機用此列
				context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
			} else if (envParam.equals("prd")) {
				// 正式機用此列
				context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
			} else if (envParam.equals("local")) {
				// 本機用此列
				context = new FileSystemXmlApplicationContext(pathLocal);
			} else {
				System.out.println(">>> args[0] must be prd/stg");
			}
		}

		VerifyAdIllegalKeyWordJob job = context.getBean(VerifyAdIllegalKeyWordJob.class);

		job.process();

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
