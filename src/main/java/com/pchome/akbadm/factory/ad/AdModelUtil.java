package com.pchome.akbadm.factory.ad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.service.advideo.IPfpAdVideoSourceService;
import com.pchome.soft.depot.utils.JredisUtil;

public class AdModelUtil {
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	private static final AdModelUtil adModelUtil = new AdModelUtil();
	private IPfpAdVideoSourceService pfpAdVideoSourceService;
	public static AdModelUtil getInstance(){
		return adModelUtil;
	}
	
	public List<String> getAdContent(File file) throws Exception{
		List<String> adContents = new ArrayList<String>();
		
		FileInputStream fileInputStream = new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String str;
		
		boolean isCss = false;
		boolean isHtml = false;
		StringBuffer css = new StringBuffer();
		StringBuffer html = new StringBuffer();
		StringBuffer width = new StringBuffer();
		StringBuffer height = new StringBuffer();
		
		while ((str = br.readLine()) != null) {

		    if(str.equals("css:")){
		    	isCss = true;
		    }
		    
		    if(str.equals("html:")){
		    	isCss = false;
		    	isHtml = true;
		    }
		    
		    if(isCss && !str.equals("css:")){
		    	css.append(str);
		    }
		    
		    if(isHtml && !str.equals("html:")){
		    	html.append(str);	
		    }		    
		    
		    if(str.indexOf("PositionWidth:") > -1){
		    	width.append(str.substring(str.indexOf(":") +1));
		    }
		    
		    if(str.indexOf("PositionHeight:") > -1){
		    	height.append(str.substring(str.indexOf(":") +1));
		    }
		    
		}
		adContents.add(css.toString());
		adContents.add(html.toString());
		adContents.add(width.toString());
		adContents.add(height.toString());
		
		return adContents;
	}
	
	public List<String> findHtmlTag(String tag, String html) throws Exception{
		
		List<String> tagNos = new ArrayList<String>();
		String tempHtml = html;
		
		while((tempHtml.indexOf(tag) != -1)){
			
			int startTag = tempHtml.indexOf(tag);
			//log.info(" startTag  = "+startTag);
			String subStr = tempHtml.substring(startTag+EnumAdTag.START_TAG.getAdTag().length(), tempHtml.length());
			//log.info(" tempStr  = "+subStr);
			int endTag = subStr.indexOf(EnumAdTag.END_TAG.getAdTag());
			//log.info(" endTag  = "+endTag);
			String tagNo = subStr.substring(0, endTag);
			//log.info(" tproNo  = "+tagNo);
			tagNos.add(tagNo);
			tempHtml = subStr.substring(tagNo.length()+EnumAdTag.END_TAG.getAdTag().length(), subStr.length());	
		}
		
		return tagNos;
	}
	
	
	/**
	 * 影音廣告預覽
	 * */
	public String getAdVideoModel(String adPreviewVideoURL,String adPreviewVideoBgImg,String realUrl) throws Exception{
		log.info(">>>>>adPreviewVideoURL:"+adPreviewVideoURL);
		log.info(">>>>>adPreviewVideoBgImg:"+adPreviewVideoBgImg);
		log.info(">>>>>realUrl:"+realUrl);
		
		String mp4Url ="";
		String webmUrl ="";
		String mp4Path ="";
		String webmPath ="";
		//判斷是否直立影片
		boolean verticalAd = false;
		
		//1.傳入youtube網址轉為預覽網址
		PfpAdVideoSource pfpAdVideoSource = pfpAdVideoSourceService.getVideoUrl(adPreviewVideoURL);
		
		if(adPreviewVideoURL.indexOf("googlevideo.com") >= 0){
			mp4Url = adPreviewVideoURL;
			webmUrl = adPreviewVideoURL;
		}else if(pfpAdVideoSource != null && pfpAdVideoSource.getAdVideoStatus() == 1 ){
			mp4Path = pfpAdVideoSource.getAdVideoMp4Path();
			webmPath = pfpAdVideoSource.getAdVideoWebmPath();
			if(pfpAdVideoSource.getVideoVertical().equals("Y")){
				verticalAd = true;
			}
		}else{
			JredisUtil jredisUtil = null;
			
			String mp4Key = "";
			String webmKey = "";
			if(System.getProperties().containsKey("akb.adm.prd")){
				jredisUtil = JredisUtil.getInstance("prd");
				mp4Key = "prd:akb:"+adPreviewVideoURL+"_"+"mp4";
				webmKey = "prd:akb:"+adPreviewVideoURL+"_"+"webm";
			}else{
				jredisUtil = JredisUtil.getInstance("stg");
				mp4Key = "stg:akb:"+adPreviewVideoURL+"_"+"mp4";
				webmKey = "prd:akb:"+adPreviewVideoURL+"_"+"webm";
			}

			try{
				Process process = null;
				//先從redis取出youtube -dl播放網址，若不存在則重新取得
				if(StringUtils.isBlank(jredisUtil.getKey(mp4Key))){
					process = Runtime.getRuntime().exec(new String[] { "bash", "-c", "youtube-dl -f 18 -g --get-format " + adPreviewVideoURL });
					mp4Url = IOUtils.toString(process.getInputStream(),"UTF-8").trim();
					String videoSize = mp4Url.substring(mp4Url.indexOf("18 - "),mp4Url.length());
					videoSize = videoSize.replace("18 - ", "");
					String [] videoSizeArray = videoSize.toString().split("x");
					String [] videoSizeArray2=videoSizeArray[1].split(" ");
					if(Integer.parseInt(videoSizeArray2[0]) > Integer.parseInt(videoSizeArray[0])){
						verticalAd = true;
					}
					mp4Url = mp4Url.substring(0, mp4Url.indexOf("18 -"));
					jredisUtil.setKeyAndExpire(mp4Key, mp4Url.trim(), 3600);
					jredisUtil.setKeyAndExpire(adPreviewVideoURL+"_"+"verticalAd", String.valueOf(verticalAd), 3600);
				}else{
					mp4Url = jredisUtil.getKey(mp4Key);
					verticalAd = Boolean.valueOf(jredisUtil.getKey(adPreviewVideoURL+"_"+"verticalAd"));
				}
					
				if(StringUtils.isBlank(jredisUtil.getKey(webmKey))){
					process = Runtime.getRuntime().exec(new String[] { "bash", "-c", "youtube-dl -f 43 -g --get-format " + adPreviewVideoURL });
					webmUrl = IOUtils.toString(process.getInputStream(),"UTF-8").trim();
					webmUrl = webmUrl.substring(0, webmUrl.indexOf("43 -"));
					jredisUtil.setKeyAndExpire(webmKey, webmUrl.trim(), 3600);
					process.destroy();
				}else{
					webmUrl = jredisUtil.getKey(webmKey);
				}
			}catch(Exception e){
				e.printStackTrace();
				log.error(">>>>>>"+e.getMessage());
			}
		}
			
			//開始組版
			FileReader fr = null;
			if(verticalAd){
				fr = new FileReader(new File("/home/webuser/akb/adm/data/tad/c_x05_mo_tad_0110.def"));	
			}else{
				fr = new FileReader(new File("/home/webuser/akb/adm/data/tad/c_x05_mo_tad_0080.def"));	
				
			}
			BufferedReader br =  new BufferedReader(fr);
			StringBuffer str = new StringBuffer();
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.indexOf("PoolSeq") >= 0){
					continue;
				}
				if(sCurrentLine.indexOf("DiffCompany") >= 0){
					continue;
				}
				if(StringUtils.isBlank(sCurrentLine)){
					continue;
				}
				if(sCurrentLine.indexOf("<!DOCTYPE html>") >= 0){
					continue;
				}
				if(sCurrentLine.indexOf("pcvideo_action_test.js") >= 0){
					continue;
				}
				if(sCurrentLine.indexOf("html:") >= 0 || sCurrentLine.indexOf("html>") >= 0 || sCurrentLine.indexOf("head>") >= 0 || sCurrentLine.indexOf("body>") >= 0 || sCurrentLine.indexOf("<meta charset=\"utf-8\">") >= 0){
					continue;
				}
				
				if(sCurrentLine.indexOf("<#dad_201303070010>") >= 0){
					if(System.getProperties().containsKey("akb.pfp.local")){
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070010>", "http://showstg.pchome.com.tw/pfp/"+URLDecoder.decode(adPreviewVideoBgImg, "UTF-8"));
					}else if(System.getProperties().containsKey("akb.pfp.stg")){
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070010>", URLDecoder.decode(adPreviewVideoBgImg, "UTF-8"));
					}else{
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070010>", URLDecoder.decode(adPreviewVideoBgImg, "UTF-8"));
					}
				}

				if(sCurrentLine.indexOf("<#dad_201303070014>") >= 0){
					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070014>", realUrl);
				}
				
				if(sCurrentLine.indexOf("<#dad_201303070015>") >= 0){
					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070015>", mp4Url);
				}
				if(sCurrentLine.indexOf("<#dad_201303070016>") >= 0){
					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070016>", webmUrl);
				}
				//備用mp4影片
				if(sCurrentLine.indexOf("<#dad_201303070017>") >= 0){
					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070017>", mp4Path);
//					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070017>", "http://showstg.pchome.com.tw/pfp/img/video/2017_11_04/adv_201711040001.mp4");
//					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070017>", "http://showstg.pchome.com.tw/pfp/img/video/2018_01_30/adv_201801300001.mp4");
					
				}
				//備用webm影片
				if(sCurrentLine.indexOf("<#dad_201303070018>") >= 0){
					sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070018>", webmPath);
				}
				
				
				//是否直立影片
				if(sCurrentLine.indexOf("<#dad_201303070013>") >= 0){
					if(verticalAd){
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070013>", "Y");	
					}else{
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070013>", "N");
					}
					
				}
				
				//取代js
				if(sCurrentLine.indexOf("pcadscript") >=0){
					if(System.getProperties().containsKey("akb.pfp.local")){
						str = str.append("<script language=\"JavaScript\" src=\"http://alex.pchome.com.tw:8080/akbadm/html/js/ad/pcvideoshowpreview.js?t="+System.currentTimeMillis()+"\"></script>");
					}else if(System.getProperties().containsKey("akb.pfp.stg")){
						str = str.append("<script language=\"JavaScript\" src=\"http://showstg.pchome.com.tw/adm/html/js/ad/pcvideoshowpreview.js?t="+System.currentTimeMillis()+"\"></script>");
					}else{
						str = str.append("<script language=\"JavaScript\" src=\"http://kdadm.pchome.com.tw/html/js/ad/pcvideoshowpreview.js?t="+System.currentTimeMillis()+"\"></script>");
					}
					continue;
				}
				str = str.append(sCurrentLine+"\r\n");
			}
			
			
			String previewHtml = str.toString();
			br.close();	
			fr.close();
			return previewHtml;
		
	}



	public IPfpAdVideoSourceService getPfpAdVideoSourceService() {
		return pfpAdVideoSourceService;
	}

	public void setPfpAdVideoSourceService(IPfpAdVideoSourceService pfpAdVideoSourceService) {
		this.pfpAdVideoSourceService = pfpAdVideoSourceService;
	}

	
}
