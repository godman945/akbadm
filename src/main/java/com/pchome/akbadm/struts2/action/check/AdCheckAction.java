package com.pchome.akbadm.struts2.action.check;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdCategory;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdVideoSource;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.advideo.IPfpAdVideoSourceService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.vo.AdCheckDisplayVO;
import com.pchome.akbadm.db.vo.AdQueryConditionVO;
import com.pchome.akbadm.factory.ad.AdFactory;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.enumerate.privilege.EnumPrivilegeModel;
import com.pchome.enumerate.video.EnumDownloadStatus;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.depot.utils.CommonUtils;


public class AdCheckAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private IPfpAdVideoSourceService pfpAdVideoSourceService;
	private IPfpAdService pfpAdService;
	private IPfpAdCategoryService pfpAdCategoryService;
	private IAdmAccesslogService admAccesslogService;
	private AdFactory adFactory;
	protected IBoardProvider boardProvider;
	private EmailUtils emailUtils;
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfdBoardService pfdBoardService;
	private String akbPfpServer;

	//發信參數
	private String mailFrom;
	private String mailDir;
	private String mailUserName;

	//查詢參數
	private String style; //廣告型態
	private String status; //狀態
	private String sendVerifyStartTime; //廣告送審起始時間
	private String sendVerifyEndTime; //廣告送審結束時間
	private String pfdCustomerInfoId;
	//換頁參數
	private int pageNo = 1; //目前頁數
	private int pageSize = 10; //每頁幾筆
	private int pageCount = 0; //共幾頁
	private int totalCount = 0; //共幾筆

	//查詢結果
	private List<AdCheckDisplayVO> adDataList = null;

	//廣告類別
	private List<PfpAdCategoryMapping> pfpAdCategoryMappingList = new ArrayList<PfpAdCategoryMapping>();
	//要審核的廣告序號
	private String[] adSeqs;
	private String[] adCategory;
	private String[] rejectReason;
	private String[] adCategoryCode;
	private String allRejectReason;		//批次退件原因;

	//訊息
	private String message;

	//下拉選單
	private Map<String, String> adCategorySelectOptionsMap;

	//經銷商名稱下拉選單
	private Map<String, String> adPfdAllNameMap = new HashMap<>();

	//經銷商名稱
	private Map<String, String> adPfdNameMap = new HashMap<>();

	public static final String MAIL_API_NO = "P144";

	@Override
    public String execute() throws Exception{
		this.init();
		return SUCCESS;
	}
	//查詢需審核廣告
	public String doQuery() throws Exception{
		log.info(">>> style = " + style);
		log.info(">>> sendVerifyStartTime = " + sendVerifyStartTime);
		log.info(">>> sendVerifyEndTime = " + sendVerifyEndTime);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		request.getSession(false);
		try{
			this.init();

			AdQueryConditionVO queryVO = new AdQueryConditionVO();
			String[] videoSize = null;

			if (StringUtils.isNotEmpty(status)) {
				queryVO.setStatus(new String[]{status});
			} else {
				queryVO.setStatus(new String[]{Integer.toString(EnumAdStatus.Verify_sys_pass.getStatusId()), Integer.toString(EnumAdStatus.Verify_sys_regect.getStatusId())});
			}

			if (StringUtils.isNotEmpty(style)) {
				queryVO.setStyle(style);
			}
			if (StringUtils.isNotEmpty(sendVerifyStartTime)) {
				queryVO.setSendVerifyStartTime(sendVerifyStartTime);
			}
			if (StringUtils.isNotEmpty(sendVerifyEndTime)) {
				queryVO.setSendVerifyEndTime(sendVerifyEndTime);
			}
			if(StringUtils.isNotBlank(pfdCustomerInfoId)){
				queryVO.setPfdCustomerInfoId(pfdCustomerInfoId);
			}

			this.totalCount = pfpAdService.getPfpAdCountByConditions(queryVO);
			this.pageCount = (int) Math.ceil(((float)totalCount / pageSize));

			//數量有變時，更新目前所在頁碼
			if (totalCount <= (pageNo-1)*pageSize) {
				this.pageNo = 1;
			}
			List<PfpAd> pfpAdList = pfpAdService.getPfpAdByConditions(queryVO, pageNo, pageSize);
			log.info(">>> pfpAdList.size() = " + pfpAdList.size());
			if (pfpAdList.size()==0) {

				this.message = "查無資料！";

			} else {
			    this.adDataList = new ArrayList<AdCheckDisplayVO>();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				//整理成頁面資訊
				for (int i=0; i<pfpAdList.size(); i++) {
					//違規項目
					String illegalString = "";
					PfpAd pfpAd = pfpAdList.get(i);
					Set<PfdUserAdAccountRef> set =  pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getPfdUserAdAccountRefs();
					for (PfdUserAdAccountRef pfdUserAdAccountRef : set) {
						adPfdNameMap.put(pfpAd.getAdSeq(), pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
						break;
					}
					String adStyle = pfpAd.getAdStyle();
					AdCheckDisplayVO data = new AdCheckDisplayVO();
					data.setAdSeq(pfpAd.getAdSeq());
					data.setAdStyle(adStyle);
					if (pfpAd.getAdSendVerifyTime() != null) {
						data.setSendVerifyTime(dateFormat.format(pfpAd.getAdSendVerifyTime()));
					}
					data.setCustomerName(pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoTitle());

					String html5Flag = "N";
					if(StringUtils.equals("c_x05_po_tad_0059", pfpAd.getAdAssignTadSeq()) || StringUtils.equals("c_x03_po_tad_0167", pfpAd.getAdAssignTadSeq()) || StringUtils.equals("c_x03_po_tad_0168", pfpAd.getAdAssignTadSeq())){
						html5Flag = "Y";
					}
					data.setHtml5Tag(html5Flag);

					if("TMG".equals(adStyle)){
						data.setAdPreview(adFactory.getAdModel(pfpAd.getTemplateProductSeq(), pfpAd.getAdSeq()));
					} else if("IMG".equals(adStyle)){
						for(PfpAdDetail pfpAdDetail : pfpAd.getPfpAdDetails()){
							//取得第三方監控代碼
							if(pfpAdDetail.getAdDetailId().equals("tracking_code")){
								data.setThirdCode(pfpAdDetail.getAdDetailContent());
							}
							if("img".equals(pfpAdDetail.getAdDetailId())){
								//取得圖片路徑
								String imgUrl = "";
								if(pfpAdDetail.getAdDetailContent().indexOf("original") == -1){
									String imgFilename = pfpAdDetail.getAdDetailContent().substring(pfpAdDetail.getAdDetailContent().lastIndexOf("/"));
									imgUrl = pfpAdDetail.getAdDetailContent().replace(imgFilename, "/original" + imgFilename);
								} else {
									imgUrl = pfpAdDetail.getAdDetailContent();
								}
								if(StringUtils.equals("N",html5Flag)){
									//取得圖片尺寸
									Map<String,String> imgmap = new HashMap<String,String>();
									imgmap = getImgSize(imgUrl);
									data.setImgWidth(imgmap.get("imgWidth"));
									data.setImgHeight(imgmap.get("imgHeight"));
								}
								data.setOriginalImg(imgUrl);
							}else if("real_url".equals(pfpAdDetail.getAdDetailId())){
								data.setRealUrl(pfpAdDetail.getAdDetailContent());
								String showUrl = pfpAdDetail.getAdDetailContent();

				            	showUrl = showUrl.replaceAll("http://", "");
				            	showUrl = showUrl.replaceAll("https://", "");
				            	if(showUrl.lastIndexOf(".com/") != -1){
				            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".com/") + 4);
				            	}
				            	if(showUrl.lastIndexOf(".tw/") != -1){
				            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".tw/") + 3);
				            	}
				            	data.setShowUrl(showUrl);
							} else if("title".equals(pfpAdDetail.getAdDetailId())){
								data.setTitle(pfpAdDetail.getAdDetailContent());
							} else if("zip".equals(pfpAdDetail.getAdDetailId())){
								data.setZipTitle(pfpAdDetail.getAdDetailContent());
							} else if("size".equals(pfpAdDetail.getAdDetailId())){
								String[] sizeArray = pfpAdDetail.getAdDetailContent().split("x");
								data.setImgWidth(sizeArray[0].trim());
								data.setImgHeight(sizeArray[1].trim());
							} else if("zipFile".equals(pfpAdDetail.getAdDetailId())){
								data.setZipFile(pfpAdDetail.getAdDetailContent());
							}
						}
					}
					else if ("VIDEO".equals(adStyle)) {
						String videoUrl = "";
	                    for(PfpAdDetail pfpAdDetail : pfpAd.getPfpAdDetails()){
	                    	//取得第三方監控代碼
							if(pfpAdDetail.getAdDetailId().equals("tracking_code")){
								data.setThirdCode(pfpAdDetail.getAdDetailContent());
							}
	                        if ("title".equals(pfpAdDetail.getAdDetailId())) {
	                            data.setAdDetailTitle(pfpAdDetail.getAdDetailContent());
	                        }
	                        else if ("content".equals(pfpAdDetail.getAdDetailId())) {
	                            data.setAdDetailContent(pfpAdDetail.getAdDetailContent());
	                        }
	                        else if ("img".equals(pfpAdDetail.getAdDetailId())) {
	                        	if(pfpAdDetail.getAdDetailContent().contains("na.gif")){
	                        		data.setAdDetailImg("");
	                        	}else{
	                        		data.setAdDetailImg(pfpAdDetail.getAdDetailContent());
	                        	}
	                        }
	                        else if ("mp4_path".equals(pfpAdDetail.getAdDetailId())) {
	                            data.setAdDetailMp4Path(pfpAdDetail.getAdDetailContent());
	                        }
	                        else if ("video_seconds".equals(pfpAdDetail.getAdDetailId())) {
	                            data.setAdDetailVideoSeconds(pfpAdDetail.getAdDetailContent());
	                        }
	                        else if ("video_size".equals(pfpAdDetail.getAdDetailId())) {
	                        	 if (StringUtils.isNotBlank(pfpAdDetail.getAdDetailContent())) {
	                                 videoSize = pfpAdDetail.getAdDetailContent().split("_");
	                                 if (videoSize.length >= 2) {
	                                     data.setAdDetailVideoWidth(videoSize[0]);
	                                     data.setAdDetailVideoHeight(videoSize[1]);
	                                 }
	                             }
	                        }
	                        else if ("video_url".equals(pfpAdDetail.getAdDetailId())) {
	                        	videoUrl = pfpAdDetail.getAdDetailContent();
	                            data.setAdDetailVideoUrl(pfpAdDetail.getAdDetailContent());
	                        }
	                        else if ("real_url".equals(pfpAdDetail.getAdDetailId())) {
	                            data.setAdDetailRealUrl(pfpAdDetail.getAdDetailContent());
	                        }
	                    }

	                    //影片下載狀態:共用呈現在違規項目內容
	                    PfpAdVideoSource pfpAdVideoSource = pfpAdVideoSourceService.getVideoUrl(videoUrl);
	                    for (EnumDownloadStatus enumDownloadStatus : EnumDownloadStatus.values()) {
	                    	if(pfpAdVideoSource.getAdVideoStatus() == enumDownloadStatus.getStatus() && enumDownloadStatus.getStatus() != EnumDownloadStatus.DOWNLOAD_SUCCESS.getStatus()){
	                    		illegalString = illegalString + "影片下載狀態:"+enumDownloadStatus.getMessage()+"<br>";
	                    		break;
	                    	}
						}
	                }
					// TODO
	                else if ("PROD".equals(adStyle)) {
                        data.setAdbgType("hasposter");
                        data.setPreviewTpro("c_x05_pad_tpro_0145");

                        for(PfpAdDetail pfpAdDetail : pfpAd.getPfpAdDetails()){
                            if ("buybtn_bg_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailBuybtnBgColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("buybtn_font_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailBuybtnFontColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("buybtn_txt".equals(pfpAdDetail.getAdDetailId())) {
                            	data.setAdDetailBuybtnTxt(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("dis_bg_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailDisBgColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("dis_font_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailDisFontColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("dis_txt_type".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailDisTxtType(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("logo_bg_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailLogoBgColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("logo_font_color".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailLogoFontColor(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("logo_img_url".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailLogoImgUrl(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("logo_txt".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailLogoTxt(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("logo_type".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailLogoType(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("prod_ad_url".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailProdAdUrl(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("prod_group".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailProdGroup(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("prod_img_show_type".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailProdImgShowType(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("prod_report_name".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailProdReportName(pfpAdDetail.getAdDetailContent());
                            }
                            else if ("sale_img_show_type".equals(pfpAdDetail.getAdDetailId())) {
                                data.setAdDetailSaleImgShowType(pfpAdDetail.getAdDetailContent());
                            }
                            else if (pfpAdDetail.getAdDetailId().indexOf("logo_sale_img_") == 0) {
                                data.getAdDetailLogoSaleImgList().add(pfpAdDetail.getAdDetailContent());
                            }
                            else if (pfpAdDetail.getAdDetailId().indexOf("sale_img_") == 0) {
                                data.getAdDetailSaleImgList().add(pfpAdDetail.getAdDetailContent());
                            }
                            else if (pfpAdDetail.getAdDetailId().indexOf("sale_end_img_") == 0) {
                                data.getAdDetailSaleEndImgList().add(pfpAdDetail.getAdDetailContent());
                            }
                            else if (pfpAdDetail.getAdDetailId().indexOf("sale_img_300x250") == 0) {
                                data.setAdDetailSaleImg(pfpAdDetail.getAdDetailContent());
                            }
                            else if (pfpAdDetail.getAdDetailId().indexOf("sale_end_img_300x250") == 0) {
                                data.setAdDetailSaleEndImg(pfpAdDetail.getAdDetailContent());
                            }
                        }
	                }

					data.setAdGroupName(pfpAd.getPfpAdGroup().getAdGroupName());
					data.setAdActionName(pfpAd.getPfpAdGroup().getPfpAdAction().getAdActionName());
					data.setStatus(pfpAd.getAdStatus());
					data.setAdVerifyRejectReason(pfpAd.getAdVerifyRejectReason());
					if (StringUtils.isNotEmpty(pfpAd.getAdCategorySeq())) {
						data.setAdCategory(pfpAd.getAdCategorySeq());
					}

					Set<PfpAdDetail> detailSet = pfpAd.getPfpAdDetails();
					Iterator<PfpAdDetail> it = detailSet.iterator();
					while (it.hasNext()) {
						PfpAdDetail adDetail = it.next();
						if (adDetail.getVerifyFlag().equals("y") && adDetail.getVerifyStatus().equals("n")) {
							String title = adDetail.getAdDetailId();
							String keyword = adDetail.getAdDetailContent();
							
							if(adDetail.getAdDetailId().equals("tracking_code")){
								data.setThirdCode(adDetail.getAdDetailContent());
							}
							
							
							// sales_price商品原價(銷售價)、商品促銷價(促銷價)非必填欄位，其他欄位為必填，則其他欄位需判斷是否為空
							if ("sales_price".equals(title) || "promotional_price".equals(title)) {
								if (!StringUtils.isNumeric(keyword)) { // 輸入非空值、數字，記錄違規項目。
									illegalString += (title + ": " + keyword + "<br>");
								}
							} else if (StringUtils.isBlank(keyword)) {
								illegalString += (title + ": " + keyword + "<br>");
							}
						}
					}
					data.setIllegalKeyWord(illegalString);
					adDataList.add(data);
				}

				String adCategoryCode="";
				for (PfpAd pfpAd : pfpAdList) {
				    adCategoryCode =  pfpAdService.getCategoryMappingCodeById(pfpAd.getAdSeq());
				    PfpAdCategoryMapping pfpAdCategoryMapping = new PfpAdCategoryMapping();
				    pfpAdCategoryMapping.setAdSeq(pfpAd.getAdSeq());
				    pfpAdCategoryMapping.setCode(adCategoryCode);
				    pfpAdCategoryMappingList.add(pfpAdCategoryMapping);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 核准
	 */
	public String doApprove() throws Exception{
		adCategoryLength();
		if (adSeqs==null || adSeqs.length==0) {
			this.message = "請選擇要審核的廣告！";
			return INPUT;
		}
		if (adCategory==null || adCategory.length==0) {
			this.message = "請選擇廣告類別！";
			return INPUT;
		}
		if (adSeqs.length != adCategory.length) {
			this.message = "系統錯誤！";
			return INPUT;
		}
		String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date= new Date();
		String now = dformat.format(new Date());
		//通過
		for (int i=0; i<adSeqs.length; i++) {
		    //改變狀態
		    pfpAdService.updateAdCheckStatus(Integer.toString(EnumAdStatus.Open.getStatusId()), adSeqs[i], adCategory[i], userId, null);
		    PfpAdCategoryMapping pfpAdCategoryMapping = new PfpAdCategoryMapping();
		    pfpAdCategoryMapping.setAdSeq(adSeqs[i]);
		    pfpAdCategoryMapping.setCode(adCategoryCode[i]);
		    pfpAdCategoryMapping.setCreateDate(date);
		    pfpAdCategoryMapping.setUpdateDate(date);
		    pfpAdService.insertAdCategoryMapping(pfpAdCategoryMapping);
		    PfpAd pfpAd = pfpAdService.getPfpAdBySeq(adSeqs[i]);
		    Set<PfpAdDetail> detailSet = pfpAd.getPfpAdDetails();
		    String title = "";
		    Iterator<PfpAdDetail> iterator = detailSet.iterator();
		    while(iterator.hasNext()) {
			PfpAdDetail detail = iterator.next();
		    	if (detail.getAdDetailId().equals("title")) {
		    	    title = detail.getAdDetailContent();
		    	}
		    }
			//access log
		    String logMsg = "廣告審核：廣告已核淮(" + title + " | " + now + ")";
		    admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg,
			    super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null,
			    null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
			//刪除全部退件公告
			boardProvider.delete(pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId(), EnumCategory.VERIFY_DENIED, adSeqs[i]);
			pfdBoardService.deletePfdBoardByDeleteId(adSeqs[i]);
		}
		return SUCCESS;
	}

	/**
	 * 核准後暫停，待客戶自行確認
	 */
	public String doApproveAndPause() throws Exception{

		adCategoryLength();

		if (adSeqs==null || adSeqs.length==0) {
			this.message = "請選擇要審核的廣告！";
			return INPUT;
		}

		if (adCategory==null || adCategory.length==0) {
			this.message = "請選擇廣告類別！";
			return INPUT;
		}

		if (adSeqs.length != adCategory.length) {
			this.message = "系統錯誤！";
			return INPUT;
		}

		String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);

		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		String now = dformat.format(new Date());

		//通過
		Date date= new Date();
		for (int i=0; i<adSeqs.length; i++) {
			//改變狀態
			pfpAdService.updateAdCheckStatus(Integer.toString(EnumAdStatus.Pause.getStatusId()), adSeqs[i], adCategory[i], userId, null);
			PfpAdCategoryMapping pfpAdCategoryMapping = new PfpAdCategoryMapping();
			pfpAdCategoryMapping.setAdSeq(adSeqs[i]);
			pfpAdCategoryMapping.setCode(adCategoryCode[i]);
			pfpAdCategoryMapping.setCreateDate(date);
			pfpAdCategoryMapping.setUpdateDate(date);
			pfpAdService.insertAdCategoryMapping(pfpAdCategoryMapping);
			PfpAd pfpAd = pfpAdService.getPfpAdBySeq(adSeqs[i]);
			Set<PfpAdDetail> detailSet = pfpAd.getPfpAdDetails();
			String title = "";
			Iterator<PfpAdDetail> iterator = detailSet.iterator();
		    while(iterator.hasNext()) {
		    	PfpAdDetail detail = iterator.next();
		    	if (detail.getAdDetailId().equals("title")) {
		    		title = detail.getAdDetailContent();
		    	}
		    }

			//access log
			String logMsg = "廣告審核：廣告已核淮/待客戶確認(" + title + " | " + now + ")";
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg,
											super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null,
											null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);


			//刪除全部退件公告
			boardProvider.delete(pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId(), EnumCategory.VERIFY_DENIED, adSeqs[i]);
			pfdBoardService.deletePfdBoardByDeleteId(adSeqs[i]);
		}

		return SUCCESS;
	}

	/**
	 * 拒絕
	 */
	public String doReject() throws Exception {
		adCategoryLength();
		if (adSeqs==null || adSeqs.length==0) {
			this.message = "請選擇要審核的廣告！";
			return INPUT;
		}
		if (rejectReason==null || rejectReason.length==0) {
			if(StringUtils.isEmpty(allRejectReason)){
				this.message = "請填寫退件原因！";
				return INPUT;
			}
		}
		if ((adSeqs.length != rejectReason.length) && StringUtils.isEmpty(allRejectReason)) {
			this.message = "系統錯誤！";
			return INPUT;
		}
		String userId = (String) getSession().get(SessionConstants.SESSION_USER_ID);
		SimpleDateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String now = dformat.format(new Date());

		//發信內容
		String mailContent = "";
		File mailFile = new File(mailDir + EnumCategory.VERIFY_DENIED.getCategory() + ".html");
		log.info(mailFile.getPath());
		if (mailFile.exists()) {
		    try {
			mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
		    } catch (IOException e) {
                log.error(mailFile.getPath(), e);
		    }
		}

        Map<String, String> adTypeMap = ComponentUtils.getAdStatusDescMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date createDate = new Date();
        Date startDate = sdf.parse(sdf.format(createDate));
        Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 6);
		Date endDate = sdf.parse(sdf.format(c.getTime()));

        //退回
        Date date= new Date();
        for (int i=0; i<adSeqs.length; i++) {
            //改變狀態
        	if(StringUtils.isNotEmpty(allRejectReason)){
        		System.out.println("allRejectReason>>>>>>>>>>>>>>>>>>>"+allRejectReason);
        		pfpAdService.updateAdCheckStatus(Integer.toString(EnumAdStatus.Reject.getStatusId()), adSeqs[i], adCategory[i], userId, allRejectReason);
        	} else {
        		System.out.println("rejectReason[A]>>>>>>>>>>>>>>>>>>>"+rejectReason[i]);
        		pfpAdService.updateAdCheckStatus(Integer.toString(EnumAdStatus.Reject.getStatusId()), adSeqs[i], adCategory[i], userId, rejectReason[i]);
        	}
            PfpAdCategoryMapping pfpAdCategoryMapping = new PfpAdCategoryMapping();
            pfpAdCategoryMapping.setAdSeq(adSeqs[i]);
            pfpAdCategoryMapping.setCode(adCategoryCode[i]);
            pfpAdCategoryMapping.setCreateDate(date);
            pfpAdCategoryMapping.setUpdateDate(date);
            pfpAdService.insertAdCategoryMapping(pfpAdCategoryMapping);
            PfpAd pfpAd = pfpAdService.getPfpAdBySeq(adSeqs[i]);
            Set<PfpAdDetail> detailSet = pfpAd.getPfpAdDetails();
            String title = "";
            Iterator<PfpAdDetail> iterator = detailSet.iterator();
            while(iterator.hasNext()) {
	        	PfpAdDetail detail = iterator.next();
	        	if (detail.getAdDetailId().equals("title")) {
	        	    title = detail.getAdDetailContent();
	        	}
            }
		    //access log
            String logMsg = "廣告審核：廣告已拒絕(" + title + " | " + now + ")";
            admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, logMsg,
        	    super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null,
        	    null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
            String adStatusDesc = adTypeMap.get(Integer.toString(pfpAd.getAdStatus()));
            String rejectReason = pfpAd.getAdVerifyRejectReason();
            String adTitle = "";
            String adContent = "";
            String realUrl = "";
            String showUrl = "";
            String adStyle = "";
            String adImgUrl = "";
            String adImgWidth = "0";
            String adImgHeight = "0";
            boolean isHtml5 = false;
            String html5Zip = "";
            String html5Size = "";
            if (pfpAd!=null) {
            	adStyle = pfpAd.getAdStyle();
            	Iterator<PfpAdDetail> it = pfpAd.getPfpAdDetails().iterator();
	        	while (it.hasNext()) {
	        	    PfpAdDetail adDetail = it.next();
	        	    if (adDetail.getAdDetailId().equals("title")) {
	        	    	adTitle = adDetail.getAdDetailContent();
	        	    }
	        	    if (adDetail.getAdDetailId().equals("content")) {
	        	    	adContent = adDetail.getAdDetailContent();
	        	    }
	        	    if (adDetail.getAdDetailId().equals("real_url")) {
	        	    	realUrl = adDetail.getAdDetailContent();
	        	    }
	        	    if (adDetail.getAdDetailId().equals("show_url")) {
	        	    	showUrl = adDetail.getAdDetailContent();
	        	    }
	        	    if (adDetail.getAdDetailId().equals("img")) {
	        	    	adImgUrl = adDetail.getAdDetailContent();
	            	}
	        	    if ("zip".equals(adDetail.getAdDetailId())) { //html5才有zip
	        	    	html5Zip = adDetail.getAdDetailContent();
	        	    	isHtml5 = true;
	        	    }
	        	    if ("size".equals(adDetail.getAdDetailId())) {
	        	    	html5Size = adDetail.getAdDetailContent();
	        	    }
	        	}

	        	if("IMG".equals(adStyle)){
	        		if(adImgUrl.indexOf("original") == -1){
                    	if(adImgUrl.lastIndexOf("/") >= 0){
                    		String imgFilename = adImgUrl.substring(adImgUrl.lastIndexOf("/"));
                    		adImgUrl = adImgUrl.replace(imgFilename, "/original" + imgFilename);
                    	}
                    }
	        		//不是html5，才取圖片大小
	        		if(!isHtml5){
		        		Map<String,String> imgmap = new HashMap<String,String>();
						imgmap = getImgSize(adImgUrl);
						adImgWidth = imgmap.get("imgWidth");
						adImgHeight = imgmap.get("imgHeight");
	        		}
	        	}
            }

        	String pfpCustomerInfoId = pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId();
        	String customerInfoTitle = pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoTitle();
        	//查看是否有經銷商，有的話也要發經銷商公告
        	List<PfdUserAdAccountRef> PfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpCustomerInfoId);
        	if (PfdUserAdAccountRefList.size()>0) {
        		//刪除之前拒絕的公告
        		pfdBoardService.deletePfdBoardByDeleteId(adSeqs[i]);

        		String pfdCustomerInfoId = PfdUserAdAccountRefList.get(0).getPfdCustomerInfo().getCustomerInfoId();
        		String pfdUserId = PfdUserAdAccountRefList.get(0).getPfdUser().getUserId();
        		//PFD 公告
        		String content = "廣告帳戶 <a href=\"./adAccountList.html\">" + customerInfoTitle + "</a>，";
        		String content2 = "廣告帳戶 <span style=\"color:#1d5ed6;\">" + customerInfoTitle + "</span>，";
        		if(adTitle.equals("")){
        			content += "圖像廣告審核遭拒絕。";
        			content2 += "圖像廣告審核遭拒絕。";
        		} else {
        			content +="<span style=\"color:#1d5ed6;\">" + adTitle + "</span> 廣告審核拒絕。";
        			content2 +="<span style=\"color:#1d5ed6;\">" + adTitle + "</span> 廣告審核拒絕。";
        		}
        		//content += "<a href=\"http://show.pchome.com.tw/xxx.html?seq=" + ??? + "\">查看</a>";
        		PfdBoard board = new PfdBoard();
        		board.setBoardType(EnumPfdBoardType.AD.getType());
        		board.setBoardContent(content);
        		board.setPfdCustomerInfoId(pfdCustomerInfoId);
        		//board.setPfdUserId(pfdUserId);
        		board.setStartDate(startDate);
        		board.setEndDate(endDate);
        		board.setIsSysBoard("n");
        		board.setHasUrl("n");
        		board.setUrlAddress(null);
        		board.setDeleteId(adSeqs[i]);

        		//觀看權限(總管理者/帳戶管理/行政管理)
    			String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege();
    			board.setMsgPrivilege(msgPrivilege);

        		board.setCreateDate(createDate);
        		pfdBoardService.save(board);

        		//給行政管理/業務管理看的公告
        		PfdBoard board2 = new PfdBoard();
        		board2.setBoardType(EnumPfdBoardType.AD.getType());
        		board2.setBoardContent(content2);
        		board2.setPfdCustomerInfoId(pfdCustomerInfoId);
        		board2.setPfdUserId(pfdUserId);
        		board2.setStartDate(startDate);
        		board2.setEndDate(endDate);
        		board2.setIsSysBoard("n");
        		board2.setHasUrl("n");
        		board2.setUrlAddress(null);
        		board2.setDeleteId(adSeqs[i]);
        		board2.setMsgPrivilege(EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.SALES_MANAGER.getPrivilege());
        		board2.setCreateDate(createDate);
        		pfdBoardService.save(board2);

        	}
        	//公告內容
        	String boardContent = EnumCategory.VERIFY_DENIED.getBoardContent();
        	if("IMG".equals(adStyle)){
        		if(adTitle.equals("")){
        			boardContent = "<a href=\"./adAdEditImg.html?adSeq=" + pfpAd.getAdSeq() + "\">圖像</a>" + boardContent;
        		} else {
        			boardContent = "<a href=\"./adAdEditImg.html?adSeq=" + pfpAd.getAdSeq() + "\">" + adTitle + "</a>" + boardContent;
        		}
        	} else {
        		boardContent = "<a href=\"./adAdEdit.html?adSeq=" + pfpAd.getAdSeq() + "\">" + adTitle + "</a>" + boardContent;
        	}
        	//刪除全部舊的退件公告
        	boardProvider.delete(pfpCustomerInfoId, EnumCategory.VERIFY_DENIED, adSeqs[i]);
        	//新增退件公告
        	boardProvider.add(pfpCustomerInfoId, boardContent, EnumBoardType.VERIFY, EnumCategory.VERIFY_DENIED, adSeqs[i]);

            Iterator<PfpUser> itUser = pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getPfpUsers().iterator();
            log.info(">>> pfpCustomerInfoId = " + pfpAd.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId());
            List<String> mailList = new ArrayList<String>();
            while (itUser.hasNext()) {
	        	PfpUser pfpUser = itUser.next();
	        	log.info(">>> pfpUserId = " + pfpUser.getUserId());
	        	//只發給主管理者
	        	if (EnumPrivilegeModel.ROOT_USER.getPrivilegeId().equals(pfpUser.getPrivilegeId())) {
	        	    mailList.add(pfpUser.getUserEmail());
	        	}
            }
            String[] mailArray = mailList.toArray(new String[mailList.size()]);
            //整理發信內容
            String tmp = mailContent;
            log.info(">>>>>>>>>>>>>>>>>>>>request.getContextPath()=" + request.getContextPath());
            tmp = tmp.replaceAll("@logoImg", "<img src=\"http://show.pchome.com.tw/html/img/logo_pchome.png\" />");

            if("IMG".equals(adStyle) && isHtml5){
            	tmp = tmp.replaceAll("@adStyle","【圖像廣告】");
            	tmp = tmp.replaceAll("@img", html5Zip); //放圖片位置改放zip名稱
            }else if("IMG".equals(adStyle)){
            	int imgWidth = Integer.parseInt(adImgWidth);
            	int imgHeight = Integer.parseInt(adImgHeight);

            	if(imgWidth > imgHeight){
            		imgHeight = imgHeight*90/imgWidth;
            		imgWidth = 90;
            	} else {
            		imgWidth = imgWidth*90/imgHeight;
            		imgHeight = 90;
            	}

            	tmp = tmp.replaceAll("@adStyle","【圖像廣告】");
            	tmp = tmp.replaceAll("@img","<img src=\"" + akbPfpServer + adImgUrl + "\"  />");
            } else if("TMG".equals(adStyle)){
            	tmp = tmp.replaceAll("@adStyle","【圖文廣告】");
            	tmp = tmp.replaceAll("@img","<img src=\"" + akbPfpServer + adImgUrl + "\" style=\"width:90px;height:90px;\" />");
            } else {
            	tmp = tmp.replaceAll("@adStyle","");
            	tmp = tmp.replaceAll("@img","");
            }

            tmp = tmp.replaceAll("@adName", replaceAllSpecialSymbolHandling(pfpAd.getPfpAdGroup().getPfpAdAction().getAdActionName())); //廣告
            tmp = tmp.replaceAll("@adCategoryName", replaceAllSpecialSymbolHandling(pfpAd.getPfpAdGroup().getAdGroupName())); //分類

            adTitle = replaceAllSpecialSymbolHandling(adTitle);
            adContent = replaceAllSpecialSymbolHandling(adContent);

            if("IMG".equals(adStyle) && isHtml5){
            	String width = "";
            	String height = "";
            	if(html5Size.split("x").length == 2){
            		width = html5Size.split("x")[0].trim();
            		height = html5Size.split("x")[1].trim();
            	}
            	tmp = tmp.replaceAll("@content","檔名：" +adTitle + "<br />尺寸：" + width + " x " + height);
            }else if("IMG".equals(adStyle)){
            	tmp = tmp.replaceAll("@content","檔名：" +adTitle + "<br />尺寸：" + adImgWidth + " x " + adImgHeight);
            } else {
            	tmp = tmp.replaceAll("@content","標題：" +adTitle + "<br />內容：" + adContent);
            }

            tmp = tmp.replaceAll("@readUrl", realUrl); //連結網址
            if("IMG".equals(adStyle)){
            	showUrl = realUrl;

            	showUrl = showUrl.replaceAll("http://", "");
            	showUrl = showUrl.replaceAll("https://", "");
            	if(showUrl.lastIndexOf(".com/") != -1){
            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".com/") + 4);
            	}
            	if(showUrl.lastIndexOf(".tw/") != -1){
            		showUrl = showUrl.substring(0, showUrl.lastIndexOf(".tw/") + 3);
            	}
            	tmp = tmp.replaceAll("@showUrl", showUrl); //顯示網址
            } else {
            	tmp = tmp.replaceAll("@showUrl", showUrl); //顯示網址
            }
            tmp = tmp.replaceAll("@statusDesc", adStatusDesc); //廣告狀態
            tmp = tmp.replaceAll("@question", rejectReason); //廣告問題


            Mail mail = null;
            //發信
            try {
	        	mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
	        	if (mail == null) {
	        	    throw new Exception("Mail Object is null.");
	        	}
            } catch (Exception e) {
	        	log.error(e.getMessage(), e);
	        	throw e;
            }
            mail.setMailFrom(mailFrom);
            for (int k=0; k<mailArray.length; k++) {
            	log.info(">>> mailArray[" + k + "] = " + mailArray[k]);
            }
            //mail.setMailTo(mailArray);
            mail.setMailTo(mailArray);

            String[] test123 = mail.getMailTo();
            for (int k=0; k<test123.length; k++) {
            	log.info(">>> test123[" + k + "] = " + test123[k]);
            }
            mail.setRname(EnumCategory.VERIFY_DENIED.getMailTitle());
            mail.setMsg(tmp);
            emailUtils.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mailUserName, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
        }
        return SUCCESS;
	}

	private void init() throws Exception {

		List<PfpAdCategory> adCategoryList = pfpAdCategoryService.getAllPfpAdCategory();

		this.adCategorySelectOptionsMap = new LinkedHashMap<String, String>();

		for (int i=0; i<adCategoryList.size(); i++) {
			PfpAdCategory pfpAdCategory = adCategoryList.get(i);
			adCategorySelectOptionsMap.put(pfpAdCategory.getSeq().toString(), pfpAdCategory.getName());
		}

		List<PfdUserAdAccountRef> pfdUserAdAccountRefList = pfdUserAdAccountRefService.loadAll();
		for (PfdUserAdAccountRef pfdUserAdAccountRef : pfdUserAdAccountRefList) {
			this.adPfdAllNameMap.put(pfdUserAdAccountRef.getPfdCustomerInfo().getCustomerInfoId(), pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
		}
	}


	private void adCategoryLength(){
		for (int i =this.adCategory.length; i>0; i--) {
			if(adCategory[i-1].length() == 0){
			 adCategory = (String[]) ArrayUtils.remove(adCategory, i-1);
			 adCategoryCode = (String[]) ArrayUtils.remove(adCategoryCode, i-1);
			}
		}
	}

	//取得圖像廣告圖片的尺寸
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

	/**
	 * 全部取代前特殊符號處理
	 * @param name
	 */
	private String replaceAllSpecialSymbolHandling(String name) {
		if (name.indexOf("$") == -1) {
			return name;
		} else {
			// 名稱內有特殊符號則加跳脫字元處理
			return name.replace("$", "\\$");
		}
	}

	public void setPfpAdService(IPfpAdService pfpAdService) {
		this.pfpAdService = pfpAdService;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSendVerifyStartTime() {
		return sendVerifyStartTime;
	}

	public void setSendVerifyStartTime(String sendVerifyStartTime) {
		this.sendVerifyStartTime = sendVerifyStartTime;
	}

	public String getSendVerifyEndTime() {
		return sendVerifyEndTime;
	}

	public void setSendVerifyEndTime(String sendVerifyEndTime) {
		this.sendVerifyEndTime = sendVerifyEndTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<AdCheckDisplayVO> getAdDataList() {
		return adDataList;
	}

	public void setAdDataList(List<AdCheckDisplayVO> adDataList) {
		this.adDataList = adDataList;
	}

	public String[] getAdSeqs() {
		return adSeqs;
	}

	public void setAdSeqs(String[] adSeqs) {
		this.adSeqs = adSeqs;
	}

	public Map<String, String> getAdStyleSelectOptionsMap() {
		return ComponentUtils.getAdStyleSelectOptionsMap();
	}

	public Map<String, String> getAdStatusSelectOptionsMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(Integer.toString(EnumAdStatus.Verify_sys_pass.getStatusId()), EnumAdStatus.Verify_sys_pass.getStatusDesc());
		map.put(Integer.toString(EnumAdStatus.Verify_sys_regect.getStatusId()), EnumAdStatus.Verify_sys_regect.getStatusDesc());
		return map;
	}

	public Map<String, String> getAdCategorySelectOptionsMap() {
		return this.adCategorySelectOptionsMap;
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

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setAdFactory(AdFactory adFactory) {
		this.adFactory = adFactory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPfpAdCategoryService(IPfpAdCategoryService pfpAdCategoryService) {
		this.pfpAdCategoryService = pfpAdCategoryService;
	}

	public String[] getAdCategory() {
		return adCategory;
	}

	public void setAdCategory(String[] adCategory) {
		this.adCategory = adCategory;
	}

	public String[] getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String[] rejectReason) {
		this.rejectReason = rejectReason;
	}

    public void setBoardProvider(IBoardProvider boardProvider) {
        this.boardProvider = boardProvider;
    }

	public void setEmailUtils(EmailUtils emailUtils) {
		this.emailUtils = emailUtils;
	}
	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setMailDir(String mailDir) {
		this.mailDir = mailDir;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public String[] getAdCategoryCode() {
		return adCategoryCode;
	}

	public void setAdCategoryCode(String[] adCategoryCode) {
		this.adCategoryCode = adCategoryCode;
	}

	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}
	public List<PfpAdCategoryMapping> getPfpAdCategoryMappingList() {
	    return pfpAdCategoryMappingList;
	}
	public void setPfpAdCategoryMappingList(
		List<PfpAdCategoryMapping> pfpAdCategoryMappingList) {
	    this.pfpAdCategoryMappingList = pfpAdCategoryMappingList;
	}
	public void setAkbPfpServer(String akbPfpServer) {
		this.akbPfpServer = akbPfpServer;
	}
	public String getAkbPfpServer() {
        return akbPfpServer;
    }
	public void setAllRejectReason(String allRejectReason) {
		this.allRejectReason = allRejectReason;
	}
	public IPfpAdVideoSourceService getPfpAdVideoSourceService() {
		return pfpAdVideoSourceService;
	}
	public void setPfpAdVideoSourceService(IPfpAdVideoSourceService pfpAdVideoSourceService) {
		this.pfpAdVideoSourceService = pfpAdVideoSourceService;
	}
	public Map<String, String> getAdPfdNameMap() {
		return adPfdNameMap;
	}
	public void setAdPfdNameMap(Map<String, String> adPfdNameMap) {
		this.adPfdNameMap = adPfdNameMap;
	}
	public Map<String, String> getAdPfdAllNameMap() {
		return adPfdAllNameMap;
	}
	public void setAdPfdAllNameMap(Map<String, String> adPfdAllNameMap) {
		this.adPfdAllNameMap = adPfdAllNameMap;
	}
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
}
