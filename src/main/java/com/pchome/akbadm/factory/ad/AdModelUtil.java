package com.pchome.akbadm.factory.ad;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONObject;

import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.service.advideo.IPfpAdVideoSourceService;
import com.pchome.enumerate.ad.EnumProdAdBtnText;
import com.pchome.soft.depot.utils.JredisUtil;

public class AdModelUtil {
	
	protected Logger log = LogManager.getRootLogger();
	
	private static final AdModelUtil adModelUtil = new AdModelUtil();
	private IPfpAdVideoSourceService pfpAdVideoSourceService;
	private String akbpfpCatalogGroupApi;
	private String pfpPhotoPath;
	private String pfpServer;
	
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
						sCurrentLine = sCurrentLine.replaceAll("<#dad_201303070010>", URLDecoder.decode(adPreviewVideoBgImg, "UTF-8").toString());
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
					}else if(System.getProperties().containsKey("akb.pfp.stg")){
						str = str.append("<script language=\"JavaScript\" src=\"http://showstg2.pchome.com.tw/adm/html/js/ad/pcvideoshowpreview.js?t="+System.currentTimeMillis()+"\"></script>");
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

	/**
	 * 商品廣告預覽
	 * */
	public String adProdModelPreview(String pfpProdAdPreviewJson) {
		try{
			JSONObject prodAdPreviewJson = new JSONObject(pfpProdAdPreviewJson);
			String catalogGroupId = prodAdPreviewJson.getString("catalogGroupId");
			String disTxtType = prodAdPreviewJson.getString("disTxtType");
			String disBgColor = prodAdPreviewJson.getString("disBgColor");
			String disFontColor = prodAdPreviewJson.getString("disFontColor");
			String btnBgColor = prodAdPreviewJson.getString("btnBgColor");
			String btnFontColor = prodAdPreviewJson.getString("btnFontColor");
			String btnTxt = prodAdPreviewJson.getString("btnTxt");
			String logoText = prodAdPreviewJson.getString("logoText");
			String logoBgColor = prodAdPreviewJson.getString("logoBgColor");
			String logoFontColor = prodAdPreviewJson.getString("logoFontColor");
			String prodLogoType = prodAdPreviewJson.getString("prodLogoType");
			String imgProportiona = prodAdPreviewJson.getString("imgProportiona");
			String userLogoPath = prodAdPreviewJson.getString("userLogoPath");
			String realUrl = prodAdPreviewJson.getString("realUrl");
			String previewTpro = prodAdPreviewJson.getString("previewTpro");
			String saleImg = prodAdPreviewJson.getString("saleImg");
			String saleEndImg = prodAdPreviewJson.getString("saleEndImg");
			String posterType = prodAdPreviewJson.getString("posterType");
			
			log.info("logoBgColor:"+logoBgColor);
			log.info("logoFontColor:"+logoFontColor);
			log.info("logoText:"+logoText);
			log.info("btnBgColor:"+btnBgColor);
			log.info("btnFontColor:"+btnFontColor);
			log.info("btnTxt:"+btnTxt);
			log.info("disBgColor:"+disBgColor);
			log.info("disFontColor:"+disFontColor);
			log.info("disTxtType:"+disTxtType);
			log.info("prodLogoType:"+prodLogoType);
			log.info("imgProportiona:"+imgProportiona);
			log.info("userLogoPath:"+userLogoPath);
			log.info("realUrl:"+realUrl);
			log.info("previewTpro:"+previewTpro);
			log.info("saleImg:"+saleImg);
			log.info("saleEndImg:"+saleEndImg);
			log.info("posterType:"+posterType);
			
			String prodData = com.pchome.soft.depot.utils.HttpUtil.getInstance().getResult(akbpfpCatalogGroupApi+"?groupId="+catalogGroupId+"&prodNum=10", "UTF-8");
			log.info("PROD API>>>>>>>>"+akbpfpCatalogGroupApi+"?groupId="+catalogGroupId+"&prodNum=10");
			if(StringUtils.isBlank(prodData)){
			log.info(">>>>>> PROD DATA API:NO DATA");
				return "";
			}
			JSONObject json = new JSONObject(prodData);
			JSONArray prodArray = (JSONArray) json.get("prodGroupList");
			if(prodArray.length() == 0){
				log.info(">>>>>> PROD DATA API:NO PROD");
				return "";
			}
			int prodIndex = 0;
			
			//1.先取得tpro
			String tpro = previewTpro+".def";
			File file = new File("/home/webuser/akb/adm/data/tpro/"+tpro);
			if(!file.exists()){
				return "";
			}
			InputStreamReader inputStreamReaderTpro = new InputStreamReader(new FileInputStream(new File("/home/webuser/akb/adm/data/tpro/"+tpro)), "UTF-8");
			BufferedReader bufferedReaderTpro = new BufferedReader(inputStreamReaderTpro);
			String tproStr = "";
			StringBuffer content = new StringBuffer();
			boolean tproFlag = false;
			String tadName = "";
			String sCurrentLine;
			String PositionWidth ="";
			String PositionHeight ="";
			String LogoWeight ="";
			String LogoHeight ="";
			while ((tproStr = bufferedReaderTpro.readLine()) != null) {
				if(tproStr.indexOf("PositionHeight:") >= 0){
					PositionHeight = tproStr.split(":")[1];
					continue;
				}
				if(tproStr.indexOf("PositionWidth:") >= 0){
					PositionWidth = tproStr.split(":")[1];
					continue;
				}
				if(tproStr.indexOf("LogoWeight:") >= 0){
					LogoWeight = tproStr.split(":")[1];
					continue;
				}
				if(tproStr.indexOf("LogoHeight:") >= 0){
					LogoHeight = tproStr.split(":")[1];
					continue;
				}
				if(tproStr.contains("html:")){
					tproFlag = true;
					continue;
				}
				//開始替換tad
				if(tproFlag){
					tadName = tproStr.replace("<#", "").replace(">", "");
					//判斷tad檔案是否存在
					if(StringUtils.isNotBlank(tadName) && (new File("/home/webuser/akb/adm/data/tad/"+tadName+".def")).exists()){
						InputStreamReader inputStreamReaderTad = new InputStreamReader(new FileInputStream(new File("/home/webuser/akb/adm/data/tad/"+tadName+".def")), "UTF-8");
						BufferedReader bufferedReaderTad = new BufferedReader(inputStreamReaderTad);
						String tadStr = "";
						boolean tadFlag = false;
						while ((tadStr = bufferedReaderTad.readLine()) != null) {
							
							if(tadStr.contains("html:")){
								tadFlag = true;
								continue;
							}
							if(tadFlag){
								//取代logo字顏色
								if(tadStr.indexOf("<#dad_logo_font_color>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_font_color>", logoFontColor);
								}
								//取代logo背景顏色
								if(tadStr.indexOf("<#dad_logo_bg_color>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_bg_color>", logoBgColor);
								}
								//取代按鈕字顏色	
								if(tadStr.indexOf("<#dad_buybtn_font_color>") >= 0){
									tadStr = tadStr.replace("<#dad_buybtn_font_color>", btnFontColor );
								}
								//取代按鈕背景顏色
								if(tadStr.indexOf("<#dad_buybtn_bg_color>") >= 0){
									tadStr = tadStr.replace("<#dad_buybtn_bg_color>",btnBgColor);
								}
								
								
								//結尾行銷開關
								if(tadStr.indexOf("<#dad_sale_img_show_type>") >= 0){
									tadStr = tadStr.replace("<#dad_sale_img_show_type>", posterType+" "+imgProportiona);
								}
								if(tadStr.indexOf("<#dad_dis_font_color>") >= 0){
									tadStr = tadStr.replace("<#dad_dis_font_color>", disFontColor);
								}
								if(tadStr.indexOf("<#dad_dis_bg_color>") >= 0){
									tadStr = tadStr.replace("<#dad_dis_bg_color>", disBgColor);
								}
								if(tadStr.indexOf("<#dad_buybtn_font_color>") >= 0){
									tadStr = tadStr.replace("<#dad_buybtn_font_color>", btnFontColor );
								}
								if(tadStr.indexOf("<#dad_buybtn_bg_color>") >= 0){
									tadStr = tadStr.replace("<#dad_buybtn_bg_color>",btnBgColor);
								}
								if(tadStr.indexOf("<#dad_logo_font_color>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_font_color>", logoFontColor);
								}
								if(tadStr.indexOf("<#dad_logo_bg_color>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_bg_color>", logoBgColor);
								}
								if(tadStr.indexOf("<#dad_logo_type>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_type>", "");
								}
								if(tadStr.indexOf("<#dad_logo_txt>") >= 0){
									tadStr = tadStr.replace("<#dad_logo_txt>", logoText);
								}
								if(tadStr.indexOf("<#dad_prod_ad_url>") >= 0){
									if(realUrl.equals("null")){
										tadStr = tadStr.replace("<#dad_prod_ad_url>", "#");
										tadStr = tadStr.replace("target=\"_blank\"", "onclick=\"return false;\" target=\"_blank\"");
									}else{
										tadStr = tadStr.replace("<#dad_prod_ad_url>", realUrl);
									}
								}
								
								if(tadStr.indexOf("<#dad_logo_sale_img_"+LogoWeight+"x"+LogoHeight+">") >= 0){
									file = new File(pfpPhotoPath+saleImg);
									if(file.exists() && StringUtils.isNotBlank(saleImg)){
										String fileExtensionNameArray[] = file.getName().split("\\.");
										String fileExtensionName = fileExtensionNameArray[fileExtensionNameArray.length-1];
										BufferedImage bi = ImageIO.read(file);
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										ImageIO.write(bi, fileExtensionName, baos);
										byte[] bytes = baos.toByteArray();
										String imgBase64 = "data:image/"+fileExtensionName+";base64,"+ new Base64().encodeToString(bytes);
										imgBase64 = imgBase64.replaceAll("\\s", "");
										tadStr = tadStr.replace("<#dad_logo_sale_img_"+LogoWeight+"x"+LogoHeight+">", imgBase64);
									}
								}
								
								if(tadStr.indexOf("<#dad_sale_img_"+PositionWidth+"x"+PositionHeight+">") >= 0){
									file = new File(pfpPhotoPath+saleEndImg);
									if(file.exists() && StringUtils.isNotBlank(saleEndImg)){
										String fileExtensionNameArray[] = file.getName().split("\\.");
										String fileExtensionName = fileExtensionNameArray[fileExtensionNameArray.length-1];
										BufferedImage bi = ImageIO.read(file);
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										ImageIO.write(bi, fileExtensionName, baos);
										byte[] bytes = baos.toByteArray();
										String imgBase64 = "data:image/"+fileExtensionName+";base64,"+ new Base64().encodeToString(bytes);
										imgBase64 = imgBase64.replaceAll("\\s", "");
										tadStr = tadStr.replace("<#dad_sale_img_"+PositionWidth+"x"+PositionHeight+">", imgBase64);
									}
								}
								
								if(tadStr.indexOf("<#dad_logo_img_url>") >= 0){
									file = new File(pfpPhotoPath+userLogoPath);
									if(file.exists() && StringUtils.isNotBlank(userLogoPath)){
										String fileExtensionNameArray[] = userLogoPath.split("\\.");
										String fileExtensionName = fileExtensionNameArray[fileExtensionNameArray.length-1];
										BufferedImage bi = ImageIO.read(file);
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										ImageIO.write(bi, fileExtensionName, baos);
										byte[] bytes = baos.toByteArray();
										String imgBase64 = "data:image/"+fileExtensionName+";base64,"+ new Base64().encodeToString(bytes);
										imgBase64 = imgBase64.replaceAll("\\s", "");
										tadStr = tadStr.replace("<#dad_logo_img_url>", imgBase64);
									}
								}
								//第二層tad
								if(tadStr.contains("pad_tad")){
									tadName = tadStr.replace("<#", "").replace(">", "");
									tadName = tadName.replaceAll(" ", "").trim();
									if(StringUtils.isNotBlank(tadName) && (new File("/home/webuser/akb/adm/data/tad/"+tadName+".def")).exists()){
										InputStreamReader inputStreamReaderTad2 = new InputStreamReader(new FileInputStream(new File("/home/webuser/akb/adm/data/tad/"+tadName+".def")), "UTF-8");
										BufferedReader bufferedReaderTad2 = new BufferedReader(inputStreamReaderTad2);
										String tadStr2 = "";
										JSONObject prodDataInfo = (JSONObject) prodArray.get(prodIndex);
										boolean tadStr2Flag = false;
										while ((tadStr2 = bufferedReaderTad2.readLine()) != null) {
											if(tadStr2.contains("html:")){
												tadStr2Flag = true;
												continue;
											}
											
											if(tadStr2Flag){
												//滑鼠移入滿版或是直立
												if(tadStr2.indexOf("<#dad_prod_img_show_type>") >= 0){
													tadStr2 = tadStr2.replace("<#dad_prod_img_show_type>", prodDataInfo.getString("crop_type"));
												}
												
												if(tadStr2.indexOf("<#dad_dis_show_type>") >= 0){
													String disShowType = "tag-hide";
													if(disTxtType.equals("1")){
														tadStr2 = tadStr2.replace("<#dad_dis_show_type>", disShowType);
													}else if(disTxtType.equals("2")){
														double ecProdPriceDis = prodDataInfo.getDouble("ec_discount_price");
														double ecProdPrice = prodDataInfo.getDouble("ec_price");
														
														if(ecProdPriceDis == ecProdPrice){
															tadStr2 = tadStr2.replace("<#dad_dis_show_type>", disShowType);
														}else{
															double dis = (ecProdPriceDis / ecProdPrice * 10); 
															BigDecimal bigDecimal=new BigDecimal(dis);
															double doubleValue = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
															if(doubleValue <= 0){
																tadStr2 = tadStr2.replace("<#dad_dis_show_type>", disShowType);
															}else{
																tadStr2 = tadStr2.replace("<#dad_dis_show_type>", "tag-show");
															}
														}
													}else if(disTxtType.equals("3")){
														double ecProdPriceDis = prodDataInfo.getDouble("ec_discount_price");
														double ecProdPrice = prodDataInfo.getDouble("ec_price");
														
														if(ecProdPriceDis == ecProdPrice){
															tadStr2 = tadStr2.replace("<#dad_dis_show_type>", disShowType);
														}else{
															double dis = Math.round(((ecProdPriceDis/ecProdPrice) - 1) * 100);
															tadStr2 = tadStr2.replace("<#dad_dis_show_type>", "tag-show");
														}
													}
												}
												
												if(tadStr2.indexOf("<#pad_ec_prod_img>") >= 0){
													String img = prodDataInfo.getString("ec_img");
													if(System.getProperties().containsKey("akb.adm.prd")){
														tadStr2 = tadStr2.replace("<#pad_ec_prod_img>", "/"+img);
													}else{
														tadStr2 = tadStr2.replace("<#pad_ec_prod_img>", img);
													}
												}
												
												if(tadStr2.indexOf("<#dad_prod_img_region>") >= 0){
													tadStr2 = tadStr2.replace("<#dad_prod_img_region>", prodDataInfo.getString("ec_img_region"));
												}
												
												
												if(tadStr2.indexOf("<#pad_ec_prod_url>") >= 0){
													String ecUrl = prodDataInfo.getString("ec_url");
													tadStr2 = tadStr2.replace("<#pad_ec_prod_url>", ecUrl);
												}
												
												if(tadStr2.indexOf("<#pad_ec_prod_price>") >= 0){
													String ecProdPrice = prodDataInfo.getString("ec_price");
													tadStr2 = tadStr2.replace("<#pad_ec_prod_price>", ecProdPrice);
												}
												if(tadStr2.indexOf("<#pad_ec_prod_price_dis>") >= 0){
													String ecProdPriceDis = prodDataInfo.getString("ec_discount_price");
													tadStr2 = tadStr2.replace("<#pad_ec_prod_price_dis>", ecProdPriceDis);
												}
												
												//折扣顯示方是 1:無 2:中文 3:英文
												if(tadStr2.indexOf("<#dad_dis_txt>") >= 0){
													if(disTxtType.equals("1")){
														tadStr2 = tadStr2.replace("<#dad_dis_txt>", "");
													}else if(disTxtType.equals("2")){
														double ecProdPriceDis = prodDataInfo.getDouble("ec_discount_price");
														double ecProdPrice = prodDataInfo.getDouble("ec_price");
														double dis = (ecProdPriceDis / ecProdPrice * 10); 
														BigDecimal bigDecimal=new BigDecimal(dis);
														double doubleValue = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
														String disStr = String.valueOf(doubleValue).replace(".","").replace("0", "");
														tadStr2 = tadStr2.replace("<#dad_dis_txt>", disStr+"折");
													}else if(disTxtType.equals("3")){
														double ecProdPriceDis = prodDataInfo.getDouble("ec_discount_price");
														double ecProdPrice = prodDataInfo.getDouble("ec_price");
														double dis = Math.round(((ecProdPriceDis/ecProdPrice) - 1) * 100);
														String disStr = String.valueOf(dis).replace(".0", "");
														tadStr2 = tadStr2.replace("<#dad_dis_txt>", disStr+"%");
													}
												}
												//按鈕文字
												if(tadStr2.indexOf("#dad_buybtn_txt") >= 0){
													tadStr2 = tadStr2.replace("<#dad_buybtn_txt>", btnTxt);
												}
												
												if(tadStr2.indexOf("<#pad_ec_prod_name>") >= 0){
													String ecProdName = prodDataInfo.getString("ec_name");
													tadStr2 = tadStr2.replace("<#pad_ec_prod_name>", ecProdName);
												}
												
												content.append(tadStr2).append("\n");
											}
											
										}
										inputStreamReaderTad2.close();
										bufferedReaderTad2.close();
									}
									prodIndex = prodIndex + 1;
									if(prodIndex >= prodArray.length()){
										prodIndex = 0;
									}
									continue;
								}
								content.append(tadStr).append("\n");
							}
						}
						inputStreamReaderTad.close();
						bufferedReaderTad.close();
					}
				}
			}
			
			bufferedReaderTpro.close();
			inputStreamReaderTpro.close();

			String content2 = content.toString();
			
			//判斷是否有行銷圖
			if(content2.contains("<#dad_logo_sale_img_"+LogoWeight+"x"+LogoHeight)){
				if(prodLogoType.equals("type3")){
					prodLogoType = "type2"; //長方形
				}else if(prodLogoType.equals("type2")){
					prodLogoType = "type1"; //正方形+文字
				}else if(prodLogoType.equals("type1")){
					prodLogoType = "type1"; //正方形無文字
				}
				content2 = content2.replace("logo-box pos-absolute pos-top pos-left", prodLogoType+" logo-box pos-absolute pos-top pos-left");
				content2 = content2.replace("logo-box pos-absolute pos-top pos-right",prodLogoType+" logo-box pos-absolute pos-top pos-right");
			}else{
				content2 = content2.replace("logo-box pos-absolute pos-top pos-left", "type3 logo-box pos-absolute pos-top pos-left");
				content2 = content2.replace("logo-box pos-absolute pos-top pos-right","type3 logo-box pos-absolute pos-top pos-right");
			}
			
			//判斷是否有結尾行銷圖
			if(content2.contains("<#dad_sale_img_"+PositionWidth+"x"+PositionHeight+">")){
				content2 = content2.replace("<#dad_sale_img_"+PositionWidth+"x"+PositionHeight+">","");
			}
			return content2.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	public IPfpAdVideoSourceService getPfpAdVideoSourceService() {
		return pfpAdVideoSourceService;
	}

	public void setPfpAdVideoSourceService(IPfpAdVideoSourceService pfpAdVideoSourceService) {
		this.pfpAdVideoSourceService = pfpAdVideoSourceService;
	}

	public String getAkbpfpCatalogGroupApi() {
		return akbpfpCatalogGroupApi;
	}

	public void setAkbpfpCatalogGroupApi(String akbpfpCatalogGroupApi) {
		this.akbpfpCatalogGroupApi = akbpfpCatalogGroupApi;
	}

	public String getPfpPhotoPath() {
		return pfpPhotoPath;
	}

	public void setPfpPhotoPath(String pfpPhotoPath) {
		this.pfpPhotoPath = pfpPhotoPath;
	}

	public String getPfpServer() {
		return pfpServer;
	}

	public void setPfpServer(String pfpServer) {
		this.pfpServer = pfpServer;
	}

	
}

