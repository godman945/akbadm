package com.pchome.akbadm.utils;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class HtmlConvertPdfUtil {

	private static final Logger log = LogManager.getRootLogger();
	
	/**
	 * 生成 html 檔案後, 再轉換成 Pdf 格式
	 */
	public ByteArrayInputStream htmlConvertPdf(Map<String,Object> map, String ftlPath, String htmlName, String fontsPath) {
		
		
		ByteArrayInputStream inputStream = null;
		
		return inputStream;
	}
}
