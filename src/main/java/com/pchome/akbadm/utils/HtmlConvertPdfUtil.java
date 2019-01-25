package com.pchome.akbadm.utils;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HtmlConvertPdfUtil {

	private static final Log log = LogFactory.getLog(PdfUtil.class);
	
	/**
	 * 生成 html 檔案後, 再轉換成 Pdf 格式
	 */
	public ByteArrayInputStream htmlConvertPdf(Map<String,Object> map, String ftlPath, String htmlName, String fontsPath) {
		
		
		ByteArrayInputStream inputStream = null;
		
		return inputStream;
	}
}
