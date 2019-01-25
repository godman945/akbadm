package com.pchome.akbadm.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.config.TestConfig;
import com.pchome.soft.util.DateValueUtil;

public class RemoveTempFileAPI {
	
	protected Log log = LogFactory.getLog(getClass());
	
	private String tempPath;
	
	/**
	 * 刪除請款單產生出來的暫存檔案
	 * 1. 每天刪除前1天資料
	 */
	public void RemovePdfTempFile(String date) {

		log.info(" tempPath: "+tempPath);
        
		StringBuffer command = new StringBuffer();
		
		command.append("find ");
		command.append(tempPath);
		command.append(" -name ");
		command.append(date);
		command.append("*");
		
		log.info(" command: "+command);
		
		try {
			
			Process proc = Runtime.getRuntime().exec(command.toString());
			  
			String line = "";
			  
			BufferedReader p_in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			  
			while((line = p_in.readLine()) != null){
			   
				//log.info("rm com===========>" + "rm -f " + line);			   
				Runtime.getRuntime().exec("rm -f " + line);
			  
			}
			  
			p_in.close();
			
		} catch (Exception e) {
			log.info(e);
		}
    	
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	
	public static void main(String[] args) {
		
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		RemoveTempFileAPI api = context.getBean(RemoveTempFileAPI.class);
        
		String date = null;
        
        if(args.length < 2){
        	date = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
        }else{
        	date = args[1].toString();
        }
        
        api.RemovePdfTempFile(date);
	
		
	}

}
