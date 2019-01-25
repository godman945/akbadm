package com.pchome.soft.depot.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.pchome.soft.util.DateValueUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * Html 轉換成 pdf 格式
 * 1. 需要安裝 wkhtmltopdf 此套件
 * 2. wkhtmltopdf-qt 安裝後可以不透過 X11 Service
 */
public class PdfUtil {

	private Log log = LogFactory.getLog(getClass().getName());
	
	public ByteArrayInputStream htmlConvertPdf(Map<String,Object> map, String ftlPath, String ftlName, String commandPath, String tempPath){
		
		ByteArrayInputStream inputStream = null;
		
		if(!map.isEmpty()){			
			
//			// 網頁基本路徑
//			HttpServletRequest request = ServletActionContext.getRequest();
//			String basePath = request.getSession().getServletContext().getRealPath("/");
			
			log.info(" tempPath: "+tempPath);
			
			// html 檔案路徑
	        StringBuffer tempHtml = new StringBuffer(); 
	        tempHtml.append(tempPath);
	        
	        
	        // pdf 檔案路徑
	        StringBuffer tempPdf = new StringBuffer(); 
	        tempPdf.append(tempPath);
	               
	        //生成随機文件名
	        String tmpUID = UUID.randomUUID().toString(); 
	        
	        String today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
	        Configuration cfg = null;
			Template template = null;
			try {
				// 指定模板存放的路径  (ftl)
				cfg = new Configuration();  
				cfg.setDirectoryForTemplateLoading(new File(ftlPath));
				cfg.setDefaultEncoding("UTF-8");  
				
				// 從上面指定的模板目錄中加載對應的模板文件		        
				template = cfg.getTemplate(ftlName);
	        	tempHtml.append(today).append("-").append(tmpUID).append("-temp.html");	        	
	        	tempPdf.append(today).append("-").append(tmpUID).append("-temp.pdf");
	        	
			} catch (Exception e) {
				log.info(e);
			}
			
	        File htmlFile = new File(tempHtml.toString());
	        
	        File pdfFile = new File(tempPdf.toString());
	         
			try {
				
				if (!htmlFile.exists()){
					htmlFile.createNewFile();
				}			
				
			} catch (Exception e) {
				log.info(e);
			}
			   			
			try {
				// 將數據寫入 html 文件
				String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
				IOUtils.write(htmlText, new FileOutputStream(htmlFile)); 
			} catch (Exception e) {
				log.info(e);
			}
			// 下指令由套件轉換成 PDF
			String pdfCommand = commandPath + " " + tempHtml.toString() + " " + tempPdf.toString(); 
			log.info(" tempHtmlPath: "+tempHtml.toString());
			log.info(" tempPdfPath: "+tempPdf.toString());
			
			try { 
		    	Runtime.getRuntime().exec(pdfCommand);
		    	TimeUnit.SECONDS.sleep(3);
		    	
		    	inputStream = new ByteArrayInputStream(FileUtils.readFileToByteArray(pdfFile));
		    	
		    } catch (Exception e) {
		    	log.info(e);
		    }
			
		}
		
		return inputStream;
	}
	
	
}
