package com.pchome.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestConfig {

	private static String webContent = "src/main/resources/";

	public static String[] getPath(String[] args){

       String[] springPath = null;

       System.out.println(">>> args.length = " + args.length);

       if (args.length>0) {

           for (int i=0; i<args.length; i++) {
        	   System.out.println(">>> args[" + i + "] = " + args[i]);
           }

           String envParam = args[0];

           if (envParam.equals("stg")) {

				springPath = pathStg;

           } else if (envParam.equals("prd")) {

				springPath=pathPrd;

           }else if(args[0].equals("local")){

				springPath=pathLocal;

           } else {

        	   System.out.println(">>> args[0] must be prd/stg");

           }

       } else {

    	   System.out.println(">>> args[0] must be prd/stg");

       }

       return springPath;
	}

	private static String[] pathPrd = {
			"config/spring/spring-action.xml",
			"config/spring/spring-api.xml",
			"config/spring/spring-dao.xml",
			"config/spring/spring-pfb-dao.xml",
			"config/spring/spring-datasource.xml",
			"config/spring/spring-factory.xml",
			"config/spring/spring-hibernate.xml",
			"config/spring/spring-log4j.xml",
			"config/spring/spring-mail-config.xml",
			"config/spring/spring-report.xml",
			"config/spring/spring-rmi-class.xml",
			"config/spring/spring-service.xml",
			"config/spring/spring-utils.xml",
			"config/spring/spring-pfb-service.xml",
			"config/spring/prd/prd-spring-prop.xml",
			"config/spring/prd/prd-spring-quartz-class.xml"
    };

	private static String[] pathStg = {
//			webContent	+"config/spring/spring-action.xml",
//			webContent	+"config/spring/spring-api.xml",
//			webContent	+"config/spring/spring-dao.xml",
//			webContent	+"config/spring/spring-pfb-dao.xml",
//			webContent	+"config/spring/spring-datasource.xml",
//			webContent	+"config/spring/spring-factory.xml",
//			webContent	+"config/spring/spring-hibernate.xml",
//			webContent	+"config/spring/spring-log4j.xml",
//			webContent	+"config/spring/spring-mail-config.xml",
//			webContent	+"config/spring/spring-report.xml",
//			webContent	+"config/spring/spring-rmi-class.xml",
//			webContent	+"config/spring/spring-service.xml",
//			webContent	+"config/spring/spring-utils.xml",
//			webContent	+"config/spring/spring-pfb-service.xml",
//			webContent	+"config/spring/stg/stg-spring-prop.xml",
//			webContent	+"config/spring/stg/stg-spring-quartz-class.xml"
			

			
			"config/spring/spring-action.xml",
			"config/spring/spring-api.xml",
			"config/spring/spring-dao.xml",
			"config/spring/spring-pfb-dao.xml",
			"config/spring/spring-datasource.xml",
			"config/spring/spring-factory.xml",
			"config/spring/spring-hibernate.xml",
			"config/spring/spring-log4j.xml",
			"config/spring/spring-mail-config.xml",
			"config/spring/spring-report.xml",
			"config/spring/spring-rmi-class.xml",
			"config/spring/spring-service.xml",
			"config/spring/spring-utils.xml",
			"config/spring/spring-pfb-service.xml",
			"config/spring/stg/stg-spring-prop.xml",
			"config/spring/stg/stg-spring-quartz-class.xml"
			
			
			
			
			
			
			
			
			
			
//			localWebContent	+"config/spring/spring-action.xml",
//			localWebContent	+"config/spring/spring-api.xml",
//			localWebContent	+"config/spring/spring-dao.xml",
//			localWebContent	+"config/spring/spring-pfb-dao.xml",
//			localWebContent	+"config/spring/spring-datasource.xml",
//			localWebContent	+"config/spring/spring-factory.xml",
//			localWebContent	+"config/spring/spring-hibernate.xml",
//			localWebContent	+"config/spring/spring-log4j.xml",
//			localWebContent	+"config/spring/spring-mail-config.xml",
//			localWebContent	+"config/spring/spring-report.xml",
//			localWebContent	+"config/spring/spring-rmi-class.xml",
//			localWebContent	+"config/spring/spring-service.xml",
//			localWebContent	+"config/spring/spring-utils.xml",
//			localWebContent	+"config/spring/spring-pfb-service.xml",
//			localWebContent	+"config/spring/stg/stg-spring-prop.xml",
//			localWebContent	+"config/spring/stg/stg-spring-quartz-class.xml"
			
    };

	private static String[] pathLocal = {
			webContent + "config/spring/spring-action.xml",
			webContent + "config/spring/spring-api.xml",
			webContent + "config/spring/spring-dao.xml",
			webContent + "config/spring/spring-pfb-dao.xml",
			webContent + "config/spring/spring-datasource.xml",
			webContent + "config/spring/spring-factory.xml",
			webContent + "config/spring/spring-hibernate.xml",
			webContent + "config/spring/spring-log4j.xml",
			webContent + "config/spring/spring-mail-config.xml",
			webContent + "config/spring/spring-report.xml",
			webContent + "config/spring/spring-rmi-class.xml",
			webContent + "config/spring/spring-service.xml",
			webContent + "config/spring/spring-utils.xml",
			webContent + "config/spring/spring-pfb-service.xml",
			webContent + "config/spring/local/local-spring-prop.xml",
			webContent + "config/spring/local/local-spring-quartz-class.xml",
//			測試自行定義quartz
//			localWebContent + "config/spring/local/local-spring-quartz-cron-open.xml",
//		 	執行排程用
//			localWebContent + "config/spring/quartz/spring-quartz-cron-open.xml",
//			localWebContent + "config/spring/spring-rmi-server.xml",
//			localWebContent + "config/spring/spring-rmi-client.xml",
//			localWebContent + "config/spring/local/local-spring-test-class.xml",

    };

	public static void main(String[] args) throws Exception{

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.pathLocal);

	}
}
