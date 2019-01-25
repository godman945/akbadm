package com.pchome.akbadm.db.service.report;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.ad.IPfpAdActionDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdDAO;
import com.pchome.akbadm.db.dao.ad.IPfpAdGroupDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfdCustomerInfoDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.dao.customerInfo.IPfpCustomerInfoDAO;
import com.pchome.akbadm.db.dao.pfd.user.IPfdUserDAO;
import com.pchome.akbadm.db.dao.report.IAdReportDAO;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdReport;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;
import com.pchome.akbadm.factory.ad.AdFactory;
import com.pchome.config.TestConfig;
import com.pchome.soft.depot.utils.CommonUtils;

public class AdReportService extends BaseService <PfpAdReport, Integer> implements IAdReportService {

	private IAdReportDAO adReportDAO;
	private IPfpAdDAO pfpAdDAO;
	private IPfpAdGroupDAO pfpAdGroupDAO;
	private IPfpAdActionDAO pfpAdActionDAO;
	private IPfpCustomerInfoDAO pfpCustomerInfoDAO;
	private IPfdCustomerInfoDAO pfdCustomerInfoDAO;
	private IPfdUserDAO pfdUserDAO;
	private IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO;
	private AdFactory adFactory;

	public void setPfdUserAdAccountRefDAO(IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO) {
		this.pfdUserAdAccountRefDAO = pfdUserAdAccountRefDAO;
	}

	public void setPfpAdDAO(IPfpAdDAO pfpAdDAO) {
		this.pfpAdDAO = pfpAdDAO;
	}

	public void setPfpAdGroupDAO(IPfpAdGroupDAO pfpAdGroupDAO) {
		this.pfpAdGroupDAO = pfpAdGroupDAO;
	}

	public void setAdReportDAO(IAdReportDAO adReportDAO) {
		this.adReportDAO = adReportDAO;
	}

	public IPfpAdActionDAO getPfpAdActionDAO() {
		return pfpAdActionDAO;
	}

	public void setPfpAdActionDAO(IPfpAdActionDAO pfpAdActionDAO) {
		this.pfpAdActionDAO = pfpAdActionDAO;
	}

	public IPfpCustomerInfoDAO getPfpCustomerInfoDAO() {
		return pfpCustomerInfoDAO;
	}

	public void setPfpCustomerInfoDAO(IPfpCustomerInfoDAO pfpCustomerInfoDAO) {
		this.pfpCustomerInfoDAO = pfpCustomerInfoDAO;
	}

	public IPfdCustomerInfoDAO getPfdCustomerInfoDAO() {
		return pfdCustomerInfoDAO;
	}

	public void setPfdCustomerInfoDAO(IPfdCustomerInfoDAO pfdCustomerInfoDAO) {
		this.pfdCustomerInfoDAO = pfdCustomerInfoDAO;
	}

	public IPfdUserDAO getPfdUserDAO() {
		return pfdUserDAO;
	}

	public void setPfdUserDAO(IPfdUserDAO pfdUserDAO) {
		this.pfdUserDAO = pfdUserDAO;
	}

//
//	/**
//	 * (舊)讀取 AdReport (廣告明細資料)，條件包含 pfdCustomerInfoId (經銷商)
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adKeywordType 廣告形式
//	 * @param sortMode 排序方式
//	 * @param displayCount 顯示比數
//	 * @return List<AdReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdReportVO> getAdReportList(String startDate, String endDate, String adKeywordType, String sortMode, int displayCount) throws Exception {
//		return adReportDAO.getAdReportList(startDate, endDate, adKeywordType, sortMode, displayCount);
//	}

	public void setAdFactory(AdFactory adFactory) {
		this.adFactory = adFactory;
	}

	/**
	 * (新)讀取 AdReport (廣告明細資料)，條件包含 pfdCustomerInfoId (經銷商)
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adKeywordType 廣告形式
	 * @param sortMode 排序方式
	 * @param displayCount 顯示比數
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdReportVO> 廣告成效列表
	 */
	@Override
	public List<AdReportVO> getAdReportList(String startDate, String endDate, String adKeywordType,
			String sortMode, int displayCount, String pfdCustomerInfoId, String templateProductSeq) throws Exception {

		List<AdReportVO> dataList = adReportDAO.getAdReportList(startDate, endDate, adKeywordType,
				sortMode, displayCount, pfdCustomerInfoId, templateProductSeq);

		AdReportVO vo = null;
		PfpAd pojo = null;
		String adSeq;
		for (int i=0; i<dataList.size(); i++) {
			vo = dataList.get(i);
			adSeq = vo.getAdSeq();
			pojo = pfpAdDAO.getPfpAdBySeq(adSeq);
			vo.setAdGroup(pojo.getPfpAdGroup().getAdGroupName());
			vo.setAdAction(pojo.getPfpAdGroup().getPfpAdAction().getAdActionName());
			/*Set<PfpAdDetail> set = pojo.getPfpAdDetails();
			Iterator<PfpAdDetail> it = set.iterator();*/
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
			/*while (it.hasNext()) {
				PfpAdDetail adDetail = it.next();
				if (adDetail.getAdDetailId().equals("real_url")) {
					realUrl = adDetail.getAdDetailContent();
					break;
				}
			}
			vo.setRealUrl(realUrl);*/
		}

		return dataList;
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

//
//	/**
//	 * (舊)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告刑式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportList(String startDate, String endDate, String adStyle, String customerInfoId, String adType) throws Exception {
//		return adReportDAO.getAdActionReportList(startDate, endDate, adStyle, customerInfoId, adType);
//	}

	/**
	 * (新)讀取 AdActionReport(總廣告成效) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告型式 (找東西廣告 or PChome頻道廣告)
	 * @param pfdCustomerInfoId 經銷商編號
	 * @param payType 付款方式
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdActionReportVO> getAdActionReportList(String startDate, String endDate,
			String pfpCustomerInfoId, String adType, String pfdCustomerInfoId,
			String payType) throws Exception {

		return adReportDAO.getAdActionReportList(startDate, endDate, pfpCustomerInfoId, adType,
				pfdCustomerInfoId, payType);
	}

//	/**
//	 * (舊)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, String adStyle, String customerInfoId, String adType) throws Exception {
//		return adReportDAO.getAdActionReportDetail(startDate, endDate, adStyle, customerInfoId, adType);
//	}

//	/**
//	 * (新)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param adStyle 廣告型式
//	 * @param pfpCustomerInfoId 帳戶序號
//	 * @param adType 廣告樣式
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, String adStyle, String customerInfoId, String adType, String pfdCustomerInfoId) throws Exception {
//		return adReportDAO.getAdActionReportDetail(startDate, endDate, adStyle, customerInfoId, adType, pfdCustomerInfoId);
//	}

	/**
	 * 強哥改的，完全看不懂在改什麼，動機為何
	 * (新)讀取 AdActionReportDetail(總廣告成效明細表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param adStyle 廣告型式
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告樣式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	/*public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate, String adStyle, String customerInfoId, String adType, String pfdCustomerInfoId) throws Exception {
		List<String> adSeqs = pfpAdDAO.getPfpAdSeqByAdStyle(adStyle);

		List<AdActionReportVO> adActionReportVOs = new ArrayList<AdActionReportVO>();
		if(adSeqs != null && adSeqs.size() > 0) {
			adActionReportVOs = adReportDAO.getAdActionReportDetail(startDate, endDate, adSeqs, customerInfoId, adType, pfdCustomerInfoId);
			if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
				List<String> adActionSeqs = new ArrayList<String>();
				List<String> pfpCustomerInfoIds = new ArrayList<String>();
				List<String> pfdCustomerInfoIds = new ArrayList<String>();
				List<String> pfdUsers = new ArrayList<String>();
				for(AdActionReportVO adActionReportVO:adActionReportVOs) {
					if(!adActionSeqs.contains(adActionReportVO.getAdActionSeq())) {
						adActionSeqs.add(adActionReportVO.getAdActionSeq());
					}
					if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
						pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
					}
					if(!pfdCustomerInfoIds.contains(adActionReportVO.getPfdCustomerInfoId())) {
						pfdCustomerInfoIds.add(adActionReportVO.getPfdCustomerInfoId());
					}
					if(!pfdUsers.contains(adActionReportVO.getPfdUserId())) {
						pfdUsers.add(adActionReportVO.getPfdUserId());
					}
				}

				// 廣告活動
				HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqs);

				// 帳戶名稱
				HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

				// 經銷商
				HashMap<String, PfdCustomerInfo> pfdAdCustomerInfoIdMap = pfdCustomerInfoDAO.getPfdCustomerInfoBySeqList(pfdCustomerInfoIds);

				// 業務
				HashMap<String, PfdUser> pfdUserMap = pfdUserDAO.getPfdUserBySeqList(pfdUsers);

				for(AdActionReportVO adActionReportVO:adActionReportVOs) {
					adActionReportVO.setAdActionName(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()).getAdActionName());
					adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
					if(StringUtils.isNotBlank(adActionReportVO.getPfdCustomerInfoId())) {
						adActionReportVO.setDealer(pfdAdCustomerInfoIdMap.get(adActionReportVO.getPfdCustomerInfoId()).getCompanyName());
					}
					if(StringUtils.isNotBlank(adActionReportVO.getPfdUserId())) {
						adActionReportVO.setSales(pfdUserMap.get(adActionReportVO.getPfdUserId()).getUserName());
					}
				}
			}
		}
		return adActionReportVOs;
	}*/

	/**
	 * 讀取 AdActionReport(廣告成效表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfpCustomerInfoId 帳戶序號
	 * @param adType 廣告型式
	 * @param pfdCustomerInfoId 經銷商編號
	 * @param payType 付款方式
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	public List<AdActionReportVO> getAdActionReportDetail(String startDate, String endDate,
			String pfpCustomerInfoId, String adType,
			String pfdCustomerInfoId, String payType) throws Exception {

		List<AdActionReportVO> voList = adReportDAO.getAdActionReportDetail(startDate, endDate,
				pfpCustomerInfoId, adType, pfdCustomerInfoId, payType);

		//補上 經銷商、業務、帳戶名稱、廣告活動 4個欄位資料
		List<String> adActionSeqList = new ArrayList<String>();

		for (int i=0; i<voList.size(); i++) {
			AdActionReportVO vo = voList.get(i);
			adActionSeqList.add(vo.getAdActionSeq());
		}

		// 廣告活動
		HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqList);

		for (int i=0; i<voList.size(); i++) {

			AdActionReportVO reportVO = voList.get(i);

			String _adActionSeq = reportVO.getAdActionSeq();

			PfpAdAction pfpAdAction = pfpAdActionMap.get(_adActionSeq);
			String _adActionName = pfpAdAction.getAdActionName();

			PfpCustomerInfo pfpCustomerInfo = pfpAdAction.getPfpCustomerInfo();
			String _pfpCustomerInfoTitle = pfpCustomerInfo.getCustomerInfoTitle();

			reportVO.setAdActionName(_adActionName);
			reportVO.setCustomerInfoName(_pfpCustomerInfoTitle);

			if (pfpCustomerInfo.getPfdUserAdAccountRefs()!=null &&
					pfpCustomerInfo.getPfdUserAdAccountRefs().size()>0) {

				PfdUserAdAccountRef ref = (PfdUserAdAccountRef) pfpCustomerInfo.getPfdUserAdAccountRefs().toArray()[0];

				reportVO.setPfdCustomerInfoId(ref.getPfdCustomerInfo().getCustomerInfoId());
				reportVO.setPfdCustomerInfoTitle(ref.getPfdCustomerInfo().getCompanyName());
				reportVO.setPfdUserId(ref.getPfdUser().getUserId());
				reportVO.setPfdUserName(ref.getPfdUser().getUserName());
			}
		}

		return voList;
	}

//	/**
//	 * (舊)讀取 AdTemplateReport(廣告樣版成效表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdTemplateReportVO> getAdTemplateReport(String startDate, String endDate) throws Exception {
//		return adReportDAO.getAdTemplateReport(startDate, endDate);
//	}

	/**
	 * (新)讀取 AdTemplateReport(廣告樣版成效表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdTemplateReportVO> getAdTemplateReport(String startDate, String endDate, String pfdCustomerInfoId) throws Exception {
		return adReportDAO.getAdTemplateReport(startDate, endDate, pfdCustomerInfoId);
	}

//	/**
//	 * (舊)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getOfflineAdActionReportList(String startDate, String endDate) throws Exception {
//		return adReportDAO.getOfflineAdActionReportList(startDate, endDate);
//	}
//
//	/**
//	 * (新)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 開始日期
//	 * @param endDate 結束日期
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 廣告成效列表
//	 */
//	@Override
//	public List<AdActionReportVO> getOfflineAdActionReportList(String startDate, String endDate, String pfdCustomerInfoId) throws Exception {
//		return adReportDAO.getOfflineAdActionReportList(startDate, endDate, pfdCustomerInfoId);
//	}

	/**
	 * (2014-04-25)讀取 OfflineAdActionReport(廣告十日內即將下檔報表) 資料，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 開始日期
	 * @param endDate 結束日期
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 廣告成效列表
	 */
	@Override
	public List<AdActionReportVO> getOfflineAdActionReportList(String startDate, String endDate, String pfdCustomerInfoId) throws Exception {
		HashMap<String, PfpAdAction> adActionMap = pfpAdActionDAO.getPfpAdActionsByActionDate(startDate, endDate);

		List<AdActionReportVO> adActionReportVOs = new ArrayList<AdActionReportVO>();
		if(adActionMap != null && adActionMap.size() > 0) {
			adActionReportVOs = adReportDAO.getOfflineAdActionReportList(adActionMap, pfdCustomerInfoId);

			if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
				List<String> adActionSeqs = new ArrayList<String>();
				List<String> pfpCustomerInfoIds = new ArrayList<String>();
				List<String> pfdCustomerInfoIds = new ArrayList<String>();
				for(AdActionReportVO adActionReportVO:adActionReportVOs) {
					System.out.println("adActionReportVO.getAdActionSeq() = " + adActionReportVO.getAdActionSeq());
					if(!adActionSeqs.contains(adActionReportVO.getAdActionSeq())) {
						adActionSeqs.add(adActionReportVO.getAdActionSeq());
					}
					if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
						pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
					}
					if(!pfdCustomerInfoIds.contains(adActionReportVO.getPfdCustomerInfoId())) {
						pfdCustomerInfoIds.add(adActionReportVO.getPfdCustomerInfoId());
					}
				}

				// 廣告活動
				HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqs);

				// 帳戶名稱
				HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

				// 經銷商
				HashMap<String, PfdCustomerInfo> pfdAdCustomerInfoIdMap = pfdCustomerInfoDAO.getPfdCustomerInfoBySeqList(pfdCustomerInfoIds);

				for(AdActionReportVO adActionReportVO:adActionReportVOs) {
					adActionReportVO.setAdActionName(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()).getAdActionName());
					adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
					if(StringUtils.isNotBlank(adActionReportVO.getPfdCustomerInfoId())) {
						adActionReportVO.setDealer(pfdAdCustomerInfoIdMap.get(adActionReportVO.getPfdCustomerInfoId()).getCompanyName());
					}

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					PfpAdAction pfpAdAction = pfpAdActionMap.get(adActionReportVO.getAdActionSeq());

					Date dStartDate = pfpAdAction.getAdActionStartDate();
					Date dEndDate = pfpAdAction.getAdActionEndDate();
					adActionReportVO.setStartDate(sdf.format(dStartDate));
					adActionReportVO.setEndDate(sdf.format(dEndDate));
					adActionReportVO.setTaxId(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getRegistration());

					Double adActionMaxPrice = Double.parseDouble(adActionReportVO.getMaxPrice());
					int count = adActionReportVO.getCount();
					Long pvSum = Long.parseLong(adActionReportVO.getPvSum());
					Long clkSum = Long.parseLong(adActionReportVO.getClkSum());
					Double price = Double.parseDouble(adActionReportVO.getPriceSum());

					//計算平均每日花費上限
					double adActionMaxPriceAvg = 0;
					if (adActionMaxPrice>0 && count>0) {
						adActionMaxPriceAvg = adActionMaxPrice/count;
					}

					DecimalFormat df = new DecimalFormat("###,###,###,###.##");
					DecimalFormat df2 = new DecimalFormat("###,###,###,###");

					java.util.Date today = new java.util.Date();
					int oneDay = 1000*3600*24;

					//計算達成率
					double arrivalRate = 0;
					if (price>0 && adActionMaxPriceAvg>0) {
						double dateRange = (today.getTime() - dStartDate.getTime()) / oneDay;
						arrivalRate = price / (dateRange * adActionMaxPriceAvg);
					}
					adActionReportVO.setStartDate(sdf.format(dStartDate));
					adActionReportVO.setEndDate(sdf.format(dEndDate));
					adActionReportVO.setMaxPrice(df2.format(Math.rint(adActionMaxPriceAvg)));
					adActionReportVO.setArrivalRate(df.format(arrivalRate) + "%");
					if (clkSum==0 || pvSum==0) {
						adActionReportVO.setClkRate(df.format(0) + "%");
					} else {
						adActionReportVO.setClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
					}
					if (price==0 || clkSum==0) {
						adActionReportVO.setClkPriceAvg(df.format(0));
					} else {
						adActionReportVO.setClkPriceAvg(df.format(price.doubleValue() / clkSum.doubleValue()));
					}
				}
			}
		}

		return adActionReportVOs;
	}

//	/**
//	 * (舊)花費成效排名查詢DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 花費成效排名列表
//	 */
//	@Override
//	public List<AdActionReportVO> getAdSpendReport(String startDate, String endDate, int displayCount) throws Exception {
//		return adReportDAO.getAdSpendReport(startDate, endDate, displayCount);
//	}

	/**
	 * (新)花費成效排名查詢DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param displayCount 顯示筆數
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 花費成效排名列表
	 */
	@Override
	public List<AdActionReportVO> getAdSpendReport(String startDate, String endDate, String pfdCustomerInfoId) throws Exception {

		DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

		List<AdActionReportVO> adActionReportVOs = adReportDAO.getAdSpendReport(startDate, endDate, pfdCustomerInfoId);
		if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
			List<String> adActionSeqs = new ArrayList<String>();
			List<String> pfpCustomerInfoIds = new ArrayList<String>();
			List<String> pfdCustomerInfoIds = new ArrayList<String>();
			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(!adActionSeqs.contains(adActionReportVO.getAdActionSeq())) {
					adActionSeqs.add(adActionReportVO.getAdActionSeq());
				}
				if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
					pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
				}
				if(!pfdCustomerInfoIds.contains(adActionReportVO.getPfdCustomerInfoId())) {
					pfdCustomerInfoIds.add(adActionReportVO.getPfdCustomerInfoId());
				}
			}

			// 廣告活動
			HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqs);

			// 帳戶名稱
			HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

			// 經銷商
			HashMap<String, PfdCustomerInfo> pfdAdCustomerInfoIdMap = pfdCustomerInfoDAO.getPfdCustomerInfoBySeqList(pfdCustomerInfoIds);

			// 業務
			HashMap<String, PfdUserAdAccountRef> pfdUserAdAccountRefMap = pfdUserAdAccountRefDAO.getPfdUserAdAccountRefBySeqList(pfpCustomerInfoIds);

			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()) != null){
					adActionReportVO.setAdActionName(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()).getAdActionName());
				}
				if(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()) != null){
					adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
				}
			    if (pfdUserAdAccountRefMap.containsKey(adActionReportVO.getCustomerInfoId())) {
				    adActionReportVO.setPfdUserName(pfdUserAdAccountRefMap.get(adActionReportVO.getCustomerInfoId()).getPfdUser().getUserName());
			    }
			    if(StringUtils.isNotBlank(adActionReportVO.getPfdCustomerInfoId())) {
			        adActionReportVO.setDealer(pfdAdCustomerInfoIdMap.get(adActionReportVO.getPfdCustomerInfoId()).getCompanyName());
			    }
			}
		}

		return adActionReportVOs;
	}

//	/**
//	 * (舊)未達每日花費上限報表DAO
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @return List<AdActionReportVO> 未達每日花費上限列表
//	 */
//	@Override
//	public List<AdActionReportVO> getUnReachBudgetReport(String startDate, String endDate, int displayCount) throws Exception {
//		List<AdActionReportVO> list = adReportDAO.getUnReachBudgetReport(startDate, endDate, displayCount);
//
//		//sort
//		Collections.sort(list, new Comparator<AdActionReportVO>() {
//			DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
//			public int compare(AdActionReportVO vo1, AdActionReportVO vo2) {
//				try {
//					int price1 = numFormat.parse(vo1.getUnReachPrice()).intValue();
//		        	int price2 = numFormat.parse(vo2.getUnReachPrice()).intValue();
//		            return (price2 < price1)?-1:price2>price1?1:0;
//				} catch (Exception e) {
//					return 0;
//				}
//	        }
//	    });
//
//		return list;
//	}

//	/**
//	 * (新)未達每日花費上限報表DAO，條件包含 pfdCustomerInfoId 經銷商
//	 * @param startDate 查詢開始日期
//	 * @param endDate 查詢結束時間
//	 * @param displayCount 顯示筆數
//	 * @param pfdCustomerInfoId 經銷商編號
//	 * @return List<AdActionReportVO> 未達每日花費上限列表
//	 */
//	@Override
//	public List<AdActionReportVO> getUnReachBudgetReport(String startDate, String endDate, int displayCount, String pfdCustomerInfoId) throws Exception {
//		List<AdActionReportVO> list = adReportDAO.getUnReachBudgetReport(startDate, endDate, displayCount, pfdCustomerInfoId);
//
//		//sort
//		Collections.sort(list, new Comparator<AdActionReportVO>() {
//			DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
//			public int compare(AdActionReportVO vo1, AdActionReportVO vo2) {
//				try {
//					int price1 = numFormat.parse(vo1.getUnReachPrice()).intValue();
//		        	int price2 = numFormat.parse(vo2.getUnReachPrice()).intValue();
//		            return (price2 < price1)?-1:price2>price1?1:0;
//				} catch (Exception e) {
//					return 0;
//				}
//	        }
//	    });
//
//		return list;
//	}

	/**
	 * (2014-04-25)未達每日花費上限報表DAO，條件包含 pfdCustomerInfoId 經銷商
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param pfdCustomerInfoId 經銷商編號
	 * @return List<AdActionReportVO> 未達每日花費上限列表
	 */
	@Override
	public List<AdActionReportVO> getUnReachBudgetReport(String startDate, String endDate, String pfdCustomerInfoId) throws Exception {

		DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

		List<AdActionReportVO> adActionReportVOs = adReportDAO.getUnReachBudgetReport(startDate, endDate, pfdCustomerInfoId);
		if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
			List<String> adActionSeqs = new ArrayList<String>();
			List<String> pfpCustomerInfoIds = new ArrayList<String>();
			List<String> pfdCustomerInfoIds = new ArrayList<String>();
			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(!adActionSeqs.contains(adActionReportVO.getAdActionSeq())) {
					adActionSeqs.add(adActionReportVO.getAdActionSeq());
				}
				if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
					pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
				}
				if(!pfdCustomerInfoIds.contains(adActionReportVO.getPfdCustomerInfoId())) {
					pfdCustomerInfoIds.add(adActionReportVO.getPfdCustomerInfoId());
				}
			}

			// 廣告活動
			HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqs);

			// 帳戶名稱
			HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

			// 經銷商
			HashMap<String, PfdCustomerInfo> pfdAdCustomerInfoIdMap = pfdCustomerInfoDAO.getPfdCustomerInfoBySeqList(pfdCustomerInfoIds);

			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()) != null){
					adActionReportVO.setAdActionName(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()).getAdActionName());
				}
				if(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()) != null){
					adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
				}
				if(StringUtils.isNotBlank(adActionReportVO.getPfdCustomerInfoId())) {
					adActionReportVO.setDealer(pfdAdCustomerInfoIdMap.get(adActionReportVO.getPfdCustomerInfoId()).getCompanyName());
				}
			}
		}

		//sort
		Collections.sort(adActionReportVOs, new Comparator<AdActionReportVO>() {
			DecimalFormat numFormat = new DecimalFormat("###,###,###,###");
			@Override
            public int compare(AdActionReportVO vo1, AdActionReportVO vo2) {
				try {
					int price1 = numFormat.parse(vo1.getUnReachPrice()).intValue();
		        	int price2 = numFormat.parse(vo2.getUnReachPrice()).intValue();
		            return (price2 < price1)?-1:price2>price1?1:0;
				} catch (Exception e) {
					return 0;
				}
	        }
	    });

		return adActionReportVOs;
	}

	/**
	 * (2014-04-28)行動裝置成效DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	@Override
    public List<AdActionReportVO> getAdMobileOSReport(String startDate, String endDate, String adMobileOS, String customerInfoId) throws Exception {
		List<AdActionReportVO> adActionReportVOs = adReportDAO.getAdMobileOSReport(startDate, endDate, adMobileOS, customerInfoId);
		if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
			List<String> pfpCustomerInfoIds = new ArrayList<String>();
			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
					pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
				}
			}

			// 帳戶名稱
			HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
			}
		}
		System.out.println("adActionReportVOs.size() = " + adActionReportVOs.size());
		return adActionReportVOs;
	}

	/**
	 * (2014-04-28)行動裝置成效明細DAO
	 * @param startDate 查詢開始日期
	 * @param endDate 查詢結束時間
	 * @param adOs 作業系統
	 * @param customerInfoId 帳戶序號
	 * @return List<AdActionReportVO> 行動裝置成效列表
	 */
	@Override
    public List<AdActionReportVO> getAdMobileOSReportDetail(String startDate, String endDate, String adMobileOS, String customerInfoId) throws Exception{
		List<AdActionReportVO> adActionReportVOs = adReportDAO.getAdMobileOSReportDetail(startDate, endDate, adMobileOS, customerInfoId);
		if(adActionReportVOs != null && adActionReportVOs.size() > 0) {
			List<String> pfpCustomerInfoIds = new ArrayList<String>();
			List<String> adActionSeqs = new ArrayList<String>();
			List<String> adGroupSeqs = new ArrayList<String>();
			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				if(!pfpCustomerInfoIds.contains(adActionReportVO.getCustomerInfoId())) {
					pfpCustomerInfoIds.add(adActionReportVO.getCustomerInfoId());
				}
				if(!adActionSeqs.contains(adActionReportVO.getAdActionSeq())) {
					adActionSeqs.add(adActionReportVO.getAdActionSeq());
				}
				if(!adGroupSeqs.contains(adActionReportVO.getAdGroupSeq())) {
					adGroupSeqs.add(adActionReportVO.getAdGroupSeq());
				}
			}

			// 帳戶名稱
			HashMap<String, PfpCustomerInfo> pfpAdCustomerInfoIdMap = pfpCustomerInfoDAO.getPfpCustomerInfoBySeqList(pfpCustomerInfoIds);

			// 廣告活動
			HashMap<String, PfpAdAction> pfpAdActionMap = pfpAdActionDAO.getPfpAdActionsBySeqList(adActionSeqs);

			// 廣告群組
			HashMap<String, PfpAdGroup> pfpAdGroupMap = pfpAdGroupDAO.getPfpAdGroupsBySeqList(adGroupSeqs);

			for(AdActionReportVO adActionReportVO:adActionReportVOs) {
				adActionReportVO.setAdActionName(pfpAdActionMap.get(adActionReportVO.getAdActionSeq()).getAdActionName());
				adActionReportVO.setAdGroupName(pfpAdGroupMap.get(adActionReportVO.getAdGroupSeq()).getAdGroupName());
				adActionReportVO.setCustomerInfoName(pfpAdCustomerInfoIdMap.get(adActionReportVO.getCustomerInfoId()).getCustomerInfoTitle());
			}
		}
		System.out.println("adActionReportVOs.size() = " + adActionReportVOs.size());
		return adActionReportVOs;
	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		IAdReportService service = (IAdReportService) context.getBean("AdReportService");

        System.out.println(">>> start");
        List<AdActionReportVO> list = service.getUnReachBudgetReport("2013-08-01", "2013-08-15", "PFDC20131124001");
        System.out.println(">>> end");
    }
}
