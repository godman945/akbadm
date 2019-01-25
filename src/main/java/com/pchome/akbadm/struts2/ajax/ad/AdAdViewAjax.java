package com.pchome.akbadm.struts2.ajax.ad;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryMappingService;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryNewService;
import com.pchome.akbadm.db.service.ad.IPfpAdDetailService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.vo.ad.PfpAdAdViewVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.depot.utils.CommonUtils;

public class AdAdViewAjax extends BaseCookieAction{
	
	private static final long serialVersionUID = 1L;
	
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

	private IPfpAdService pfpAdService;
	private IPfpAdDetailService pfpAdDetailService;
	private IPfpAdCategoryMappingService pfpAdCategoryMappingService;
	private IPfpAdCategoryNewService pfpAdCategoryNewService;
	private String startDate;
	private String endDate;
	private String dateType;
	private String userAccount;
	private String searchAdStatus;
	private String searchType;
	private String keyword;
	private String adGroupSeq;
	private String adSeq;
	private String adPvclkDevice;
	private List<PfpAdAdViewVO> adAdViewVO;
	private String adclkDevice;
	private String adCategoryCode;
	
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 20;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private int totalCount = 0;   				// 初始化共幾筆
	
	private int totalSize = 0;						
	private int totalPv = 0;						
	private int totalClk = 0;						
	private float totalClkRate = 0;
	private float totalAvgCost = 0;
	private int totalCost = 0;
	private int totalInvalidClk = 0;
	private int totalInvalidClkPrice = 0;
	private boolean changeSelect;
	
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	public String adAdViewTableAjax() throws Exception{
	    if(!changeSelect && !StringUtils.isEmpty(adclkDevice)){
		adPvclkDevice = adclkDevice;
	    }
	    if(StringUtils.isBlank(adPvclkDevice)){
		adPvclkDevice ="ALL Device";
	    }else{
		adPvclkDevice = adPvclkDevice.equals("裝置") ? "ALL Device" :adPvclkDevice;
	    }
	    Map<String,String> adAdViewConditionMap = new HashMap<String,String>();
	    adAdViewConditionMap.put("startDate", startDate);
	    adAdViewConditionMap.put("endDate", endDate);
	    adAdViewConditionMap.put("dateType",dateType);
	    adAdViewConditionMap.put("userAccount",userAccount);
	    adAdViewConditionMap.put("adType",searchType);
	    adAdViewConditionMap.put("adPvclkDevice",adPvclkDevice);
	    adAdViewConditionMap.put("pageNo",String.valueOf(pageNo));
	    adAdViewConditionMap.put("pageSize",String.valueOf(pageSize));
	    adAdViewConditionMap.put("adAdViewName",keyword);
	    adAdViewConditionMap.put("adSeq",adSeq);
	    adAdViewConditionMap.put("adGroupSeq",adGroupSeq);
	    adAdViewConditionMap.put("changeSelect",String.valueOf(changeSelect));
	    adAdViewConditionMap.put("searchAdStatus",searchAdStatus);
	    
	    Map<String,String> pfpAdCategoryMappingMap = new HashMap<String,String>();
	    if(!StringUtils.isEmpty(adCategoryCode)){
	    	String adSeqListString = "''";
	    	String code2 = adCategoryCode.substring(4, 8);
	    	String code3 = adCategoryCode.substring(8, 12);
	    	String code4 = adCategoryCode.substring(12);
	    	if("0000".equals(code2) && "0000".equals(code3) && "0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 4);
	    	} else if("0000".equals(code3) && "0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 8);
	    	} else if("0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 12);
	    	}
	    	
	    	List<PfpAdCategoryMapping> pfpAdCategoryMappingList = pfpAdCategoryMappingService.selectPfpAdCategoryMappingByAdCode(adCategoryCode);
	    	if(!pfpAdCategoryMappingList.isEmpty()){
	    		for(PfpAdCategoryMapping pfpAdCategoryMapping:pfpAdCategoryMappingList){
	    			adSeqListString += ",'" +  pfpAdCategoryMapping.getAdSeq() + "'";
	    			pfpAdCategoryMappingMap.put(pfpAdCategoryMapping.getAdSeq(), pfpAdCategoryMapping.getCode());
	    		}
	    	}
	    	adAdViewConditionMap.put("adSeqListString",adSeqListString);
	    } else {
	    	List<PfpAdCategoryMapping> pfpAdCategoryMappingList = pfpAdCategoryMappingService.selectPfpAdCategoryMappingByAdCode("");
	    	if(!pfpAdCategoryMappingList.isEmpty()){
	    		for(PfpAdCategoryMapping pfpAdCategoryMapping:pfpAdCategoryMappingList){
	    			pfpAdCategoryMappingMap.put(pfpAdCategoryMapping.getAdSeq(), pfpAdCategoryMapping.getCode());
	    		}
	    	}
	    	adAdViewConditionMap.put("adSeqListString","");
	    }
	    
	    Map<String,String> pfpAdCategoryNewMap =  pfpAdCategoryNewService.getPfpAdCategoryNewNameByCodeMap();
	    
	    adAdViewVO = pfpAdService.getAdViewReport(adAdViewConditionMap);
	    int adViewReportSize = pfpAdService.getAdViewReportSize(adAdViewConditionMap);
	    totalSize = adViewReportSize;
	    totalCount = adViewReportSize;
	    pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    if(adAdViewVO != null){
	    	for(PfpAdAdViewVO vo:adAdViewVO){
	    		//取得廣告類別
	    		if(pfpAdCategoryMappingMap.get(vo.getAdSeq()) != null){
	    			vo.setAdCategoryCode(pfpAdCategoryMappingMap.get(vo.getAdSeq()));
	    		} else {
	    			vo.setAdCategoryCode("0000000000000000");
	    		}
	    		//取得廣告類別名稱
	    		if(pfpAdCategoryMappingMap.get(vo.getAdSeq()) != null){
	    			String code = pfpAdCategoryMappingMap.get(vo.getAdSeq());
	    			String[] codeArray = code.split(";");
	    			String code1 = "";
	    			String code2 = "";
	    			String code3 = "";
	    			if(codeArray.length >= 1){
	    				code1 = codeArray[0];
	    			}
	    			if(codeArray.length >= 2){
	    				code2 = codeArray[1];
	    			}
	    			if(codeArray.length >= 3){
	    				code3 = codeArray[2];
	    			}
	    			
	    			if(StringUtils.isNotEmpty(code1)){
	    				vo.setAdCategoryName(getPfpAdCategoryName(vo.getAdSeq(),code1,pfpAdCategoryNewMap));
	    			}
	    			if(StringUtils.isNotEmpty(code2)){
	    				vo.setAdCategoryName2(getPfpAdCategoryName(vo.getAdSeq(),code2,pfpAdCategoryNewMap));
	    			}
	    			if(StringUtils.isNotEmpty(code3)){
	    				vo.setAdCategoryName3(getPfpAdCategoryName(vo.getAdSeq(),code3,pfpAdCategoryNewMap));
	    			}
	    		}
	    		
	    		String html5Flag = vo.getHtml5Tag();
	    		
	    		//取得圖像廣告尺寸
				if("IMG".equals(vo.getAdStyle())){
					Map<String,String> imgmap = new HashMap<String,String>();
					List<PfpAdDetail> pfpAdDetailList = pfpAdDetailService.getPfpAdDetails(null,vo.getAdSeq(),null,null);
					if(pfpAdDetailList != null){
						for(PfpAdDetail pfpAdDetail:pfpAdDetailList){
							if("img".equals(pfpAdDetail.getAdDetailId())){
								vo.setImg(pfpAdDetail.getAdDetailContent());
		                        if(vo.getImg().indexOf("original") == -1){
		                        	if(vo.getImg().lastIndexOf("/") >= 0){
		                        		String imgFilename = vo.getImg().substring(vo.getImg().lastIndexOf("/"));
		                        		vo.setOriginalImg(vo.getImg().replace(imgFilename, "/original" + imgFilename));	
		                        	}
		                        	vo.setOriginalImg(vo.getImg());
		                        } else {
		                        	vo.setOriginalImg(vo.getImg());
		                        }
		                        
		                        if(StringUtils.equals("N", html5Flag)){
		                        	imgmap = getImgSize(vo.getOriginalImg());
		                        	vo.setImgWidth(imgmap.get("imgWidth"));
		                        	vo.setImgHeight(imgmap.get("imgHeight"));
		                        }
							} else if("real_url".equals(pfpAdDetail.getAdDetailId())){
								vo.setRealUrl(pfpAdDetail.getAdDetailContent());
								String showUrl = pfpAdDetail.getAdDetailContent();
								showUrl = showUrl.replaceAll("http://", "");
								showUrl = showUrl.replaceAll("https://", "");
				            	if(showUrl.lastIndexOf(".com/") != -1){
				            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".com/") + 4);
				            	}
				            	if(showUrl.lastIndexOf(".tw/") != -1){
				            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".tw/") + 3);
				            	}
				            	
				            	vo.setShowUrl(showUrl);
							} else if("title".equals(pfpAdDetail.getAdDetailId())){
								vo.setTitle(pfpAdDetail.getAdDetailContent());
							} else if("zip".equals(pfpAdDetail.getAdDetailId())){
								vo.setZipTitle(pfpAdDetail.getAdDetailContent());
							} else if("size".equals(pfpAdDetail.getAdDetailId())){
								String[] sizeArray = pfpAdDetail.getAdDetailContent().split("x");
								vo.setImgWidth(sizeArray[0].trim());
								vo.setImgHeight(sizeArray[1].trim());
							}
						}	
					}
				}
				
	    		totalPv += vo.getAdPv();
	    		totalClk += vo.getAdClk();		
	    		totalCost += vo.getAdClkPrice();
	    		totalInvalidClk += vo.getAdInvalidClk();
	    		totalInvalidClkPrice += vo.getAdInvalidClkPrice();
	    		adSeq += vo.getAdSeq();
	    	}
	    	if(totalClk > 0 || totalPv > 0){
	    		totalClkRate = (float)totalClk / (float)totalPv*100;
	    	}
	    	if(totalCost > 0 || totalClk > 0){
	    		totalAvgCost = (float)totalCost / (float)totalClk;	
	    	}
		}
		return SUCCESS;
	}
	
	public String adDownloadAjax()  throws Exception{
		
		if(!changeSelect && !StringUtils.isEmpty(adclkDevice)){
		adPvclkDevice = adclkDevice;
	    }
	    if(StringUtils.isBlank(adPvclkDevice)){
		adPvclkDevice ="ALL Device";
	    }else{
		adPvclkDevice = adPvclkDevice.equals("裝置") ? "ALL Device" :adPvclkDevice;
	    }
	    Map<String,String> adAdViewConditionMap = new HashMap<String,String>();
	    adAdViewConditionMap.put("startDate", startDate);
	    adAdViewConditionMap.put("endDate", endDate);
	    adAdViewConditionMap.put("dateType",dateType);
	    adAdViewConditionMap.put("userAccount",userAccount);
	    adAdViewConditionMap.put("adType",searchType);
	    adAdViewConditionMap.put("adPvclkDevice",adPvclkDevice);
	    adAdViewConditionMap.put("pageNo",String.valueOf(-1));
	    adAdViewConditionMap.put("pageSize",String.valueOf(-1));
	    adAdViewConditionMap.put("adAdViewName",keyword);
	    adAdViewConditionMap.put("adSeq",adSeq);
	    adAdViewConditionMap.put("adGroupSeq",adGroupSeq);
	    adAdViewConditionMap.put("changeSelect",String.valueOf(changeSelect));
	    adAdViewConditionMap.put("searchAdStatus",searchAdStatus);
	    
	    Map<String,String> pfpAdCategoryMappingMap = new HashMap<String,String>();
	    if(!StringUtils.isEmpty(adCategoryCode)){
	    	String adSeqListString = "''";
	    	String code2 = adCategoryCode.substring(4, 8);
	    	String code3 = adCategoryCode.substring(8, 12);
	    	String code4 = adCategoryCode.substring(12);
	    	if("0000".equals(code2) && "0000".equals(code3) && "0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 4);
	    	} else if("0000".equals(code3) && "0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 8);
	    	} else if("0000".equals(code4)){
	    		adCategoryCode = adCategoryCode.substring(0, 12);
	    	}
	    	
	    	List<PfpAdCategoryMapping> pfpAdCategoryMappingList = pfpAdCategoryMappingService.selectPfpAdCategoryMappingByAdCode(adCategoryCode);
	    	if(!pfpAdCategoryMappingList.isEmpty()){
	    		for(PfpAdCategoryMapping pfpAdCategoryMapping:pfpAdCategoryMappingList){
	    			adSeqListString += ",'" +  pfpAdCategoryMapping.getAdSeq() + "'";
	    			pfpAdCategoryMappingMap.put(pfpAdCategoryMapping.getAdSeq(), pfpAdCategoryMapping.getCode());
	    		}
	    	}
	    	adAdViewConditionMap.put("adSeqListString",adSeqListString);
	    } else {
	    	List<PfpAdCategoryMapping> pfpAdCategoryMappingList = pfpAdCategoryMappingService.selectPfpAdCategoryMappingByAdCode("");
	    	if(!pfpAdCategoryMappingList.isEmpty()){
	    		for(PfpAdCategoryMapping pfpAdCategoryMapping:pfpAdCategoryMappingList){
	    			pfpAdCategoryMappingMap.put(pfpAdCategoryMapping.getAdSeq(), pfpAdCategoryMapping.getCode());
	    		}
	    	}
	    	adAdViewConditionMap.put("adSeqListString","");
	    }
	    
	    Map<String,String> pfpAdCategoryNewMap =  pfpAdCategoryNewService.getPfpAdCategoryNewNameByCodeMap();
	    
	    adAdViewVO = pfpAdService.getAdViewReport(adAdViewConditionMap);
	    
	    totalSize = adAdViewVO.size();
	    totalCount = adAdViewVO.size();
	
	    SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告明細報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"新增日期","會員帳號","帳戶名稱","廣告名稱","廣告內容","顯示連結","實際連結","狀態","廣告類別1-1","廣告類別1-2","廣告類別1-3","廣告類別1-4",
    					"廣告類別2-1","廣告類別2-2","廣告類別2-3","廣告類別2-4","廣告類別3-1","廣告類別3-2","廣告類別3-3","廣告類別3-4","裝置","曝光數","點擊數","點閱率(%)",
    					"平均點選費用","費用","無效點擊數","無效點擊費用","廣告","分類","分類狀態"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,廣告明細報表");
		content.append("\n\n");
	    
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
	    if(adAdViewVO != null){
	    	for(PfpAdAdViewVO vo:adAdViewVO){
	    		totalPv += vo.getAdPv();
	    		totalClk += vo.getAdClk();		
	    		totalCost += vo.getAdClkPrice();
	    		totalInvalidClk += vo.getAdInvalidClk();
	    		totalInvalidClkPrice += vo.getAdInvalidClkPrice();
	    		
	    		//取得廣告類別名稱
	    		if(pfpAdCategoryMappingMap.get(vo.getAdSeq()) != null){
	    			String code = pfpAdCategoryMappingMap.get(vo.getAdSeq());
	    			String[] codeArray = code.split(";");
	    			String code1 = "";
	    			String code2 = "";
	    			String code3 = "";
	    			if(codeArray.length >= 1){
	    				code1 = codeArray[0];
	    			}
	    			if(codeArray.length >= 2){
	    				code2 = codeArray[1];
	    			}
	    			if(codeArray.length >= 3){
	    				code3 = codeArray[2];
	    			}
	    			
	    			if(StringUtils.isNotEmpty(code1)){
	    				vo.setAdCategoryName(getPfpAdCategoryName(vo.getAdSeq(),code1,pfpAdCategoryNewMap));
	    			}
	    			if(StringUtils.isNotEmpty(code2)){
	    				vo.setAdCategoryName2(getPfpAdCategoryName(vo.getAdSeq(),code2,pfpAdCategoryNewMap));
	    			}
	    			if(StringUtils.isNotEmpty(code3)){
	    				vo.setAdCategoryName3(getPfpAdCategoryName(vo.getAdSeq(),code3,pfpAdCategoryNewMap));
	    			}
	    		}
	    		
	    		//取得廣告明細
	    		Map<String,String> imgmap = new HashMap<String,String>();
				List<PfpAdDetail> pfpAdDetailList = pfpAdDetailService.getPfpAdDetails(null,vo.getAdSeq(),null,null);
				if(pfpAdDetailList != null){
					for(PfpAdDetail pfpAdDetail:pfpAdDetailList){
						String html5Flag = vo.getHtml5Tag();
						
						if("IMG".equals(vo.getAdStyle())){
							if("img".equals(pfpAdDetail.getAdDetailId())){
								vo.setImg(pfpAdDetail.getAdDetailContent());
		                        if(vo.getImg().indexOf("original") == -1){
		                        	if(vo.getImg().lastIndexOf("/") >= 0){
		                        		String imgFilename = vo.getImg().substring(vo.getImg().lastIndexOf("/"));
		                        		vo.setOriginalImg(vo.getImg().replace(imgFilename, "/original" + imgFilename));	
		                        	}
		                        	vo.setOriginalImg(vo.getImg());
		                        } else {
		                        	vo.setOriginalImg(vo.getImg());
		                        }
		                        if(StringUtils.equals("N", html5Flag)){
		                        	imgmap = getImgSize(vo.getOriginalImg());
		                        	vo.setContent("尺寸：" + imgmap.get("imgWidth") + " x " + imgmap.get("imgHeight"));
		                        }
							}
						}
						
						if("real_url".equals(pfpAdDetail.getAdDetailId())){
							vo.setRealUrl(pfpAdDetail.getAdDetailContent());
						} else if("show_url".equals(pfpAdDetail.getAdDetailId())){
							vo.setShowUrl(pfpAdDetail.getAdDetailContent());
						} else if("title".equals(pfpAdDetail.getAdDetailId())){
							vo.setTitle(pfpAdDetail.getAdDetailContent());
						} else if("content".equals(pfpAdDetail.getAdDetailId())){
							if(!"IMG".equals(vo.getAdStyle())){
								vo.setContent(pfpAdDetail.getAdDetailContent());
							}
						} else if("zip".equals(pfpAdDetail.getAdDetailId())){
							vo.setZipTitle(pfpAdDetail.getAdDetailContent());
						} else if("size".equals(pfpAdDetail.getAdDetailId())){
							String[] sizeArray = pfpAdDetail.getAdDetailContent().split("x");
							vo.setContent("尺寸：" + sizeArray[0].trim() + " x " + sizeArray[1].trim());
						}
						
					}
				}
	    		
	    		content.append("\"" + vo.getAdCreateTime() + "\",");
	    		content.append("\"" + vo.getMemberId() + "\",");
	    		content.append("\"" + vo.getCustomerInfoTitle() + "\",");
	    		content.append("\"" + vo.getTitle() + "\",");
	    		content.append("\"" + vo.getContent() + "\",");
	    		content.append("\"" + vo.getShowUrl() + "\",");
	    		content.append("\"" + vo.getRealUrl() + "\",");
	    		content.append("\"" + vo.getAdStatusDesc() + "\",");
	    		
	    		if(StringUtils.isNotEmpty(vo.getAdCategoryName())){
	    			
	    			String[] nameArray = vo.getAdCategoryName().split("<br>");
	    			
	    			int i = 0;
	    			for(String name:nameArray){
	    				content.append("\"" + name + "\",");
	    				i++;
	    			}
	    			
	    			while(i < 4){
	    				content.append("\"\",");
	    				i++;
	    			}
	    		} else {
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    		}
	    		
	    		if(StringUtils.isNotEmpty(vo.getAdCategoryName2())){
	    			
	    			String[] nameArray = vo.getAdCategoryName2().split("<br>");
	    			
	    			int i = 0;
	    			for(String name:nameArray){
	    				content.append("\"" + name + "\",");
	    				i++;
	    			}
	    			
	    			while(i < 4){
	    				content.append("\"\",");
	    				i++;
	    			}
	    		} else {
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    		}
	    		
	    		if(StringUtils.isNotEmpty(vo.getAdCategoryName3())){
	    			
	    			String[] nameArray = vo.getAdCategoryName3().split("<br>");
	    			
	    			int i = 0;
	    			for(String name:nameArray){
	    				content.append("\"" + name + "\",");
	    				i++;
	    			}
	    			
	    			while(i < 4){
	    				content.append("\"\",");
	    				i++;
	    			}
	    		} else {
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    			content.append("\"\",");
	    		}
	    		
	    		content.append("\"" + vo.getAdPvclkDevice() + "\",");
	    		content.append("\"" + df.format(vo.getAdPv()) + "\",");
	    		content.append("\"" + df.format(vo.getAdClk()) + "\",");
	    		content.append("\"" + df2.format(vo.getAdClkRate()) + "%\",");
	    		content.append("\"$ " + df2.format(vo.getAdClkPriceAvg()) + "\",");
	    		content.append("\"$ " + df.format(vo.getAdClkPrice()) + "\",");
	    		content.append("\"" + df.format(vo.getAdInvalidClk()) + "\",");
	    		content.append("\"$ " + df.format(vo.getAdInvalidClkPrice()) + "\",");
	    		content.append("\"" + vo.getAdActionName() + "\",");
	    		content.append("\"" + vo.getAdGroupName() + "\",");
	    		content.append("\"" + vo.getAdGroupStatusDesc() + "\"");
	    		content.append("\n");
	    	}
	    }
	    
	    content.append("\n");
		content.append("\n");
		
		if(totalClk > 0 || totalPv > 0){
    		totalClkRate = (float)totalClk / (float)totalPv*100;
    	}
    	if(totalCost > 0 || totalClk > 0){
    		totalAvgCost = (float)totalCost / (float)totalClk;	
    	}
		
		content.append("\"總計：" + totalCount + "筆\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"" + df.format(totalPv) + "\",");
		content.append("\"" + df.format(totalClk) + "\",");
		content.append("\"" + df2.format(totalClkRate) + "%\",");
		content.append("\"$ " + df2.format(totalAvgCost) + "\",");
		content.append("\"$ " + df.format(totalCost) + "\",");
		content.append("\"" + df.format(totalInvalidClk) + "\",");
		content.append("\"$ " + df.format(totalInvalidClkPrice) + "\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\"");
		content.append("\n");
		
		
    	if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		
		return SUCCESS;
	}
	
	public Map<String,String> getImgSize(String originalImg) throws Exception {
		Map<String,String> imgmap = new HashMap<String,String>();
		File picture = null;
		picture = new File("/home/webuser/akb/pfp/" +  originalImg.replace("\\", "/"));
		if(picture != null){
			Map<String,String> imgInfo = CommonUtils.getInstance().getImgInfo(picture);
	        imgmap.put("imgWidth", imgInfo.get("imgWidth"));
	 		imgmap.put("imgHeight", imgInfo.get("imgHeight"));
		}
		return imgmap;
	}
	
	//廣告類別名稱
	public String getPfpAdCategoryName(String adSeq,String pfpAdCategoryCode,Map<String,String> pfpAdCategoryNewMap){
		String pfpAdCategoryName = "";

		String code2 = pfpAdCategoryCode.substring(4, 8);		//第2層code
		String code3 = pfpAdCategoryCode.substring(8, 12);		//第3層code
		String code4 = pfpAdCategoryCode.substring(12);			//第4層code
		
		if(pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 4) + "000000000000") != null){
			pfpAdCategoryName += pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 4) + "000000000000");
		}
		
		if(!"0000".equals(code2)){
			if(pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 8) + "00000000") != null){
				pfpAdCategoryName += "<br>" + pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 8) + "00000000");
			}
		}
		
		if(!"0000".equals(code3)){
			if(pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 12) + "0000") != null){
				pfpAdCategoryName += "<br>" + pfpAdCategoryNewMap.get(pfpAdCategoryCode.substring(0, 12) + "0000");
			}
		}
		
		if(!"0000".equals(code4)){
			if(pfpAdCategoryNewMap.get(pfpAdCategoryCode) != null){
				pfpAdCategoryName += "<br>" + pfpAdCategoryNewMap.get(pfpAdCategoryCode);
			}
		}

		
		return pfpAdCategoryName;
	}
	
	public void setPfpAdService(IPfpAdService pfpAdService) {
		this.pfpAdService = pfpAdService;
	}

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setSearchAdStatus(String searchAdStatus) {
		this.searchAdStatus = searchAdStatus;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public void setAdPvclkDevice(String adPvclkDevice) {
		this.adPvclkDevice = adPvclkDevice;
	}

	public List<PfpAdAdViewVO> getAdAdViewVO() {
		return adAdViewVO;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public int getTotalPv() {
		return totalPv;
	}

	public int getTotalClk() {
		return totalClk;
	}

	public float getTotalClkRate() {
		return totalClkRate;
	}

	public float getTotalAvgCost() {
		return totalAvgCost;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public int getTotalInvalidClk() {
		return totalInvalidClk;
	}

	public int getTotalInvalidClkPrice() {
		return totalInvalidClkPrice;
	}

	public String getAdclkDevice() {
	    return adclkDevice;
	}

	public void setAdclkDevice(String adclkDevice) {
	    this.adclkDevice = adclkDevice;
	}

	public boolean isChangeSelect() {
	    return changeSelect;
	}

	public void setChangeSelect(boolean changeSelect) {
	    this.changeSelect = changeSelect;
	}

	public String getAdSeq() {
	    return adSeq;
	}

	public void setAdSeq(String adSeq) {
	    this.adSeq = adSeq;
	}

	public IPfpAdDetailService getPfpAdDetailService() {
		return pfpAdDetailService;
	}

	public void setPfpAdDetailService(IPfpAdDetailService pfpAdDetailService) {
		this.pfpAdDetailService = pfpAdDetailService;
	}

	public void setPfpAdCategoryMappingService(
			IPfpAdCategoryMappingService pfpAdCategoryMappingService) {
		this.pfpAdCategoryMappingService = pfpAdCategoryMappingService;
	}

	public void setPfpAdCategoryNewService(IPfpAdCategoryNewService pfpAdCategoryNewService) {
		this.pfpAdCategoryNewService = pfpAdCategoryNewService;
	}

	public String getAdCategoryCode() {
		return adCategoryCode;
	}

	public void setAdCategoryCode(String adCategoryCode) {
		this.adCategoryCode = adCategoryCode;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}
	
}
