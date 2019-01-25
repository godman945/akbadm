package com.pchome.akbadm.factory.ad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.service.ad.PfpAdDetailService;
import com.pchome.akbadm.db.service.advideo.IPfpAdVideoSourceService;
import com.pchome.soft.depot.utils.JredisUtil;

public class AdFactory{
	
	protected Log log = LogFactory.getLog(this.getClass());
	private ABuildAdProcess adProduct;
	private ABuildAdProcess adTemplate;
	private PfpAdDetailService pfpAdDetailService;
	private String folderPath;
	private String dadTag = EnumAdTag.START_TAG.getAdTag()+EnumAdTag.DAD_TAG.getAdTag();		// 廣告內容Tag
	private AdModelUtil adModelUtil;
	
	public String getAdModel(String tproNo, String adNo) throws Exception{
		
		log.info(" buildAdModel --> tproNo = "+tproNo+", adNo = "+adNo);
		String adModel = null;
		String tproTag = EnumAdTag.START_TAG.getAdTag() + tproNo.substring(0, tproNo.indexOf(EnumAdFolder.tpro.toString())) + EnumAdFolder.tpro;;
		String tadTag = EnumAdTag.START_TAG.getAdTag() + tproNo.substring(0, tproNo.indexOf(EnumAdFolder.tpro.toString())) + EnumAdFolder.tad;;
		if(StringUtils.isNotEmpty(folderPath)){			
			
			// 商品路徑
			String tproPath = folderPath+"/"+EnumAdFolder.tpro;
			File file = new File(tproPath+"/"+tproNo+".def");
			//log.info(" absolutePath  = "+file.getAbsolutePath());
			
			if(file.exists()){
				
				List<String> adContents = adModelUtil.getAdContent(file);
				String width = adContents.get(2);
				String height = adContents.get(3);
				
				// 商品組合
				List<String> productContents = adProduct.buildAdModel(adContents.get(0), adContents.get(1), folderPath, tproTag);
				//log.info(" product css  = "+productContents.get(0));
				//log.info(" product html  = "+productContents.get(1));
				
				// 樣版組合
				List<String> templateContents = adTemplate.buildAdModel(productContents.get(0), productContents.get(1), folderPath, tadTag);
				//log.info(" template css  = "+templateContents.get(0));
				//log.info(" template html  = "+templateContents.get(1));
				
				adModel = this.buildHtml(templateContents, adNo);
				
				adModel = adModel.replace("<#pad_width>", width);
				adModel = adModel.replace("<#pad_height>", height);
				
			}else{			
				log.info(" file = "+file.getAbsolutePath()+" doesn't exist!!");
			}
			
		}else{
			log.info(" folderPath is null");
		}
		
		
		return adModel;
	}
	
	private String buildHtml(List<String> templateContents, String adNo) throws Exception{
		
		StringBuffer htmlAd = new StringBuffer();
		
		if(StringUtils.isNotEmpty(adNo)){
			
			List<String> tagNos = adModelUtil.findHtmlTag(dadTag, templateContents.get(1));
			
			Iterator tagNoIterator = tagNos.listIterator();
			
			String htmlContent = templateContents.get(1);
			
			while(tagNoIterator.hasNext()){
				
				String tagNo = tagNoIterator.next().toString();
				PfpAdDetail pfpAdDetail = pfpAdDetailService.findAdContent(adNo, tagNo);
				
				if(pfpAdDetail != null){
					String tag = EnumAdTag.START_TAG.getAdTag()+tagNo+EnumAdTag.END_TAG.getAdTag();					
					htmlContent = htmlContent.replace(tag, pfpAdDetail.getAdDetailContent());
				}else{
					log.info(" doesn't find PfpAdDetail -->  adNo = "+adNo+" tagNo  = "+tagNo);
				}
				
						
			}
			
			templateContents.remove(1);
			templateContents.add(htmlContent);
			
		}
		
		htmlAd.append("<style type=\"text/css\">");
		htmlAd.append(templateContents.get(0));
		htmlAd.append("</style>");
		htmlAd.append(templateContents.get(1));
		
		return htmlAd.toString();
	}	
	
	public void setAdProduct(AdProduct adProduct) {
		this.adProduct = adProduct;
	}
	
	public void setAdTemplate(ABuildAdProcess adTemplate) {
		this.adTemplate = adTemplate;
	}

	public void setPfpAdDetailService(PfpAdDetailService pfpAdDetailService) {
		this.pfpAdDetailService = pfpAdDetailService;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public AdModelUtil getAdModelUtil() {
		return adModelUtil;
	}

	public void setAdModelUtil(AdModelUtil adModelUtil) {
		this.adModelUtil = adModelUtil;
	}


	
}
