package com.pchome.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.quartzs.AdjustAccountJob;

public class TestAdjustAccountJob {

	private static String localWebContent = "WebContent/WEB-INF/src/";

	/**
	 * 此為手動執行計算金額
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String argsDate = dateFormat.format(new Date());		// shell 傳入參數用

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

			// 第二個值為排程開始時間
			if(StringUtils.isNotBlank(args[1])) {
				argsDate = args[1];
			}
		} else {
			// 本機直接設定開始日期
			argsDate = "2014-04-07";
		}

		AdjustAccountJob job = context.getBean(AdjustAccountJob.class);

		Date startDate = dateFormat.parse(argsDate);
		Date now = new Date();
		Date reportDate = startDate;
		System.out.println(">>> start date = " + dateFormat.format(reportDate));
		while (reportDate.getTime() <= now.getTime()) {
			System.out.println("reportDate = " + dateFormat.format(reportDate));

			job.process(dateFormat.format(reportDate));

			Calendar c = Calendar.getInstance();
			c.setTime(reportDate);
			c.add(Calendar.DATE, 1);

			reportDate = c.getTime();
		}


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
