package com.pchome.akbadm.db.service.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.ad.IPfpAdDAO;
import com.pchome.akbadm.db.dao.report.IAdReport2DAO;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.factory.ad.AdFactory;
import com.pchome.soft.depot.utils.CommonUtils;


public class AdReport2Service extends BaseService<AdReportVO, String> implements IAdReport2Service {

	private IPfpAdDAO pfpAdDAO;
	private AdFactory adFactory;

	public void setPfpAdDAO(IPfpAdDAO pfpAdDAO) {
		this.pfpAdDAO = pfpAdDAO;
	}

	public void setAdFactory(AdFactory adFactory) {
		this.adFactory = adFactory;
	}

	@Override
	public List<AdReportVO> getAdReportList(Map<String, String> conditionMap,int page, int pageSize) throws Exception {
		List<AdReportVO> dataList = ((IAdReport2DAO) dao).getAdReportList(conditionMap, page, pageSize);
		List<String> adSeqList = new ArrayList<String>();
		for(AdReportVO vo:dataList){
			adSeqList.add(vo.getAdSeq());
		}

		Map<String,PfpAd> pfpAdMap = new HashMap<String,PfpAd>();
		pfpAdMap = pfpAdDAO.getPfpAdMap(adSeqList);

		AdReportVO vo = null;
		PfpAd pojo = null;
		String adSeq;
		String[] videoSize;
		for (int i=0; i<dataList.size(); i++) {
			vo = dataList.get(i);
			adSeq = vo.getAdSeq();
			pojo = pfpAdMap.get(adSeq);
			vo.setAdGroup(pojo.getPfpAdGroup().getAdGroupName());
			vo.setAdAction(pojo.getPfpAdGroup().getPfpAdAction().getAdActionName());
			String adStyle = pojo.getAdStyle();
			vo.setAdStyle(adStyle);
			String realUrl = "";

			if("TMG".equals(adStyle)){
				vo.setContent(adFactory.getAdModel(pojo.getTemplateProductSeq(), adSeq));
			}

			String html5Flag = "N";
			if(StringUtils.equals("c_x05_po_tad_0059", pojo.getAdAssignTadSeq())){
				html5Flag = "Y";
			}

			vo.setHtml5Tag(html5Flag);

			for(PfpAdDetail pfpAdDetail : pojo.getPfpAdDetails()){
				if (pfpAdDetail.getAdDetailId().equals("real_url")) {
					realUrl = pfpAdDetail.getAdDetailContent();
					vo.setRealUrl(realUrl);

					String showUrl = realUrl;

	            	showUrl = showUrl.replaceAll("http://", "");
	            	showUrl = showUrl.replaceAll("https://", "");
	            	if(showUrl.indexOf("/") != -1){
	            		showUrl = showUrl.substring(0, showUrl.indexOf("/"));
	            	}
	            	vo.setShowUrl(showUrl);
				} else if("title".equals(pfpAdDetail.getAdDetailId())){
					vo.setTitle(pfpAdDetail.getAdDetailContent());
				}
				if("IMG".equals(adStyle)){
					if("img".equals(pfpAdDetail.getAdDetailId())){
						//取得圖片路徑
						String imgUrl = "";
						if(pfpAdDetail.getAdDetailContent().indexOf("original") == -1){
							String imgFilename = pfpAdDetail.getAdDetailContent().substring(pfpAdDetail.getAdDetailContent().lastIndexOf("/"));
							imgUrl = pfpAdDetail.getAdDetailContent().replace(imgFilename, "/original" + imgFilename);
						} else {
							imgUrl = pfpAdDetail.getAdDetailContent();
						}

						//取得圖片尺寸
						if(StringUtils.equals("N", html5Flag)){
							Map<String,String> imgmap = new HashMap<String,String>();
							imgmap = getImgSize(imgUrl);
							vo.setImgWidth(imgmap.get("imgWidth"));
							vo.setImgHeight(imgmap.get("imgHeight"));
						}
						vo.setOriginalImg(imgUrl);
					}else if("real_url".equals(pfpAdDetail.getAdDetailId())){
						String showUrl = pfpAdDetail.getAdDetailContent();

		            	showUrl = showUrl.replaceAll("http://", "");
		            	showUrl = showUrl.replaceAll("https://", "");
		            	if(showUrl.indexOf("/") != -1){
		            		showUrl = showUrl.substring(0, showUrl.indexOf("/"));
		            	}
		            	vo.setShowUrl(showUrl);
					} else if("zip".equals(pfpAdDetail.getAdDetailId())){
						vo.setZipTitle(pfpAdDetail.getAdDetailContent());
					} else if("size".equals(pfpAdDetail.getAdDetailId())){
						String[] sizeArray = pfpAdDetail.getAdDetailContent().split("x");
						vo.setImgWidth(sizeArray[0].trim());
						vo.setImgHeight(sizeArray[1].trim());
					}
				}
				else if ("VIDEO".equals(adStyle)) {
                    if ("title".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailTitle(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("content".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailContent(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("img".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailImg(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("mp4_path".equals(pfpAdDetail.getAdDetailId())) {
                        if (pfpAdDetail.getAdDetailContent().lastIndexOf("/") + 1 < pfpAdDetail.getAdDetailContent().length()) {
                            vo.setAdDetailMp4Path(pfpAdDetail.getAdDetailContent().substring(pfpAdDetail.getAdDetailContent().lastIndexOf("/") + 1));
                        }
                    }
                    else if ("video_seconds".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailVideoSeconds(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("video_size".equals(pfpAdDetail.getAdDetailId())) {
                        if (StringUtils.isNotBlank(pfpAdDetail.getAdDetailContent())) {
                            videoSize = pfpAdDetail.getAdDetailContent().split("_");
                            if (videoSize.length >= 2) {
                                vo.setAdDetailVideoWidth(videoSize[0]);
                                vo.setAdDetailVideoHeight(videoSize[1]);
                            }
                        }
                    }
                    else if ("video_url".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailVideoUrl(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("real_url".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailRealUrl(pfpAdDetail.getAdDetailContent());
                    }
                }
			}
		}

		return dataList;
	}

	@Override
	public AdReportVO getAdReportListSum(Map<String, String> conditionMap) throws Exception {
		AdReportVO vo = new AdReportVO();
		List<AdReportVO> list = ((IAdReport2DAO) dao).getAdReportListSum(conditionMap);
		if(!list.isEmpty()){
			vo = list.get(0);
		} else {
			vo = null;
		}
		return vo;
	}

	@Override
	public List<AdReportVO> getAdReportDetailList(Map<String, String> conditionMap) throws Exception {
		List<AdReportVO> dataList = ((IAdReport2DAO) dao).getAdReportDetailList(conditionMap);
		List<String> adSeqList = new ArrayList<String>();
		for(AdReportVO vo:dataList){
			adSeqList.add(vo.getAdSeq());
		}

		Map<String,PfpAd> pfpAdMap = new HashMap<String,PfpAd>();
		pfpAdMap = pfpAdDAO.getPfpAdMap(adSeqList);

		AdReportVO vo = null;
		PfpAd pojo = null;
		String adSeq;
        String[] videoSize;
		for (int i=0; i<dataList.size(); i++) {
			vo = dataList.get(i);
			adSeq = vo.getAdSeq();
			pojo = pfpAdMap.get(adSeq);
			vo.setAdGroup(pojo.getPfpAdGroup().getAdGroupName());
			vo.setAdAction(pojo.getPfpAdGroup().getPfpAdAction().getAdActionName());
			String adStyle = pojo.getAdStyle();
			vo.setAdStyle(adStyle);
			String realUrl = "";

			if("TMG".equals(adStyle)){
				vo.setContent(adFactory.getAdModel(pojo.getTemplateProductSeq(), adSeq));
			}

			String html5Flag = "N";
			if(StringUtils.equals("c_x05_po_tad_0059", pojo.getAdAssignTadSeq())){
				html5Flag = "Y";
			}

			vo.setHtml5Tag(html5Flag);

			for(PfpAdDetail pfpAdDetail : pojo.getPfpAdDetails()){
				if (pfpAdDetail.getAdDetailId().equals("real_url")) {
					realUrl = pfpAdDetail.getAdDetailContent();
					vo.setRealUrl(realUrl);

					String showUrl = realUrl;

	            	showUrl = showUrl.replaceAll("http://", "");
	            	showUrl = showUrl.replaceAll("https://", "");
	            	if(showUrl.indexOf("/") != -1){
	            		showUrl = showUrl.substring(0, showUrl.indexOf("/"));
	            	}
	            	vo.setShowUrl(showUrl);
				} else if("title".equals(pfpAdDetail.getAdDetailId())){
					vo.setTitle(pfpAdDetail.getAdDetailContent());
				}

				if("IMG".equals(adStyle)){
					if("img".equals(pfpAdDetail.getAdDetailId())){
						//取得圖片路徑
						String imgUrl = "";
						if(pfpAdDetail.getAdDetailContent().indexOf("original") == -1){
							String imgFilename = pfpAdDetail.getAdDetailContent().substring(pfpAdDetail.getAdDetailContent().lastIndexOf("/"));
							imgUrl = pfpAdDetail.getAdDetailContent().replace(imgFilename, "/original" + imgFilename);
						} else {
							imgUrl = pfpAdDetail.getAdDetailContent();
						}

						//取得圖片尺寸
						if(StringUtils.equals("N", html5Flag)){
							Map<String,String> imgmap = new HashMap<String,String>();
							imgmap = getImgSize(imgUrl);
							vo.setImgWidth(imgmap.get("imgWidth"));
							vo.setImgHeight(imgmap.get("imgHeight"));
						}
						vo.setOriginalImg(imgUrl);
					}else if("real_url".equals(pfpAdDetail.getAdDetailId())){
						String showUrl = pfpAdDetail.getAdDetailContent();

		            	showUrl = showUrl.replaceAll("http://", "");
		            	showUrl = showUrl.replaceAll("https://", "");
		            	if(showUrl.indexOf("/") != -1){
		            		showUrl = showUrl.substring(0, showUrl.indexOf("/"));
		            	}
		            	vo.setShowUrl(showUrl);
					} else if("zip".equals(pfpAdDetail.getAdDetailId())){
						vo.setZipTitle(pfpAdDetail.getAdDetailContent());
					} else if("size".equals(pfpAdDetail.getAdDetailId())){
						String[] sizeArray = pfpAdDetail.getAdDetailContent().split("x");
						vo.setImgWidth(sizeArray[0].trim());
						vo.setImgHeight(sizeArray[1].trim());
					}
				}
                else if ("VIDEO".equals(adStyle)) {
                    if ("title".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailTitle(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("content".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailContent(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("img".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailImg(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("mp4_path".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailMp4Path(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("video_seconds".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailVideoSeconds(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("video_size".equals(pfpAdDetail.getAdDetailId())) {
                        if (StringUtils.isNotBlank(pfpAdDetail.getAdDetailContent())) {
                            videoSize = pfpAdDetail.getAdDetailContent().split("_");
                            if (videoSize.length >= 2) {
                                vo.setAdDetailVideoWidth(videoSize[0]);
                                vo.setAdDetailVideoHeight(videoSize[1]);
                            }
                        }
                    }
                    else if ("video_url".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailVideoUrl(pfpAdDetail.getAdDetailContent());
                    }
                    else if ("real_url".equals(pfpAdDetail.getAdDetailId())) {
                        vo.setAdDetailRealUrl(pfpAdDetail.getAdDetailContent());
                    }
                }
			}
		}

		return dataList;
	}

	//取得圖像廣告圖片的尺寸
	public Map<String,String> getImgSize(String originalImg) throws IOException {
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
}
