package com.pchome.akbadm.report;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.report.pdf.APDFReport;
import com.pchome.akbadm.report.pdf.order.OrderQueryReport;
import com.pchome.config.TestConfig;

public class ReportTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//arg[0] = prd / stg / local
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		
		String startDate = "2013-03-10";
		String endDate = "2013-05-02";

		/* customerInfo report
		APDFReport report = (CustomerInfoQueryReport) context.getBean("CustomerInfoQueryReport");
		
		List<String> list = new ArrayList<String>();
		list.add(startDate);
		list.add(endDate);
		
		report.getPdfStream(list);
		*/
		
		// customerInfo report
		APDFReport report = (OrderQueryReport) context.getBean("OrderQueryReport");
		
		List<String> list = new ArrayList<String>();
		list.add(startDate);
		list.add(endDate);
		
		report.getPdfStream(list);
		
	}

}
