package com.pchome.akbadm.factory.ad;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public abstract class ABuildAdProcess {
	
	protected Logger log = LogManager.getRootLogger();
	
	public List<String> buildAdModel(String css, String html, String path, String tag) throws Exception{
		
		this.setCssContent(css);
		this.setHtmlContent(html);
		this.setPath(path);
		
		List<String> tags = new ArrayList<String>();
		List<String> adContent = new ArrayList<String>();
		//String tag = this.getTag();
		
		do{
			
			tags = AdModelUtil.getInstance().findHtmlTag(tag, this.getHtmlContent());
			
			this.replaceTag(tags);
						
		}while((this.getHtmlContent().indexOf(tag) != -1));
		
		adContent.add(this.getCssContent());
		adContent.add(this.getHtmlContent());
		
		return adContent;
	}
	
	public abstract String getTag() throws Exception;
	
	public abstract String getPath() throws Exception;
	
	public abstract void setPath(String path) throws Exception;
	
	public abstract String getCssContent() throws Exception;
	
	public abstract void setCssContent(String css) throws Exception;
	
	public abstract String getHtmlContent() throws Exception;
	
	public abstract void setHtmlContent(String html) throws Exception;
	
	public void replaceTag(List<String> tagNos) throws Exception {
		// TODO Auto-generated method stub
		
		Iterator tagNoIterator = tagNos.listIterator();
		
		StringBuffer css = new StringBuffer();
		css.append(this.getCssContent());
		
		String htmlContent = this.getHtmlContent();
		
		while(tagNoIterator.hasNext()){
			
			String tagNo = tagNoIterator.next().toString();
			List<String> fileContent = this.readFile(tagNo);
			
			if(fileContent != null && fileContent.size() > 0){
				
				css.append(fileContent.get(0));
				
				String tag = EnumAdTag.START_TAG.getAdTag()+tagNo+EnumAdTag.END_TAG.getAdTag();
				htmlContent = htmlContent.replace(tag, fileContent.get(1));
			}
						
		}
		
		this.setCssContent(css.toString());
		this.setHtmlContent(htmlContent);

	}
	
	public List<String> readFile(String no) throws Exception{
		
		List<String> fileContent = null;
			
		for(EnumAdFolder name:EnumAdFolder.values()){
			
			if((no.indexOf(name.toString()) != -1)){
				
				String filePath = this.getPath()+name.toString()+"/"+no+".def";
				File file = new File(filePath);
				//log.info(" absolutePath  = "+file.getAbsolutePath());
				
				if(file.exists()){
					
					fileContent = AdModelUtil.getInstance().getAdContent(file);		
					
				}else{
					log.info(" file = "+file.getAbsolutePath()+" doesn't exist!!");
				}
			}
			
		}
		
		return fileContent;
	}

}
