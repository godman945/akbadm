package com.pchome.akbadm.struts2.action.ad;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.ad.IPfpAdGroupService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.enumerate.ad.EnumAdStatus;
import com.pchome.enumerate.ad.EnumAdType;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.enumerate.privilege.EnumPrivilegeModel;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.depot.utils.CommonUtils;
import com.pchome.soft.util.DateValueUtil;


public class AdAdViewAction extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private LinkedHashMap<String,String> dateSelectMap = DateValueUtil.getInstance().getDateRangeMap(); 			// 查詢日期頁面顯示
	private String startDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);					// 開始日期
	private String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);	// 結束日期

	private IAdmAccesslogService admAccesslogService;
	private IPfpAdGroupService pfpAdGroupService;
	private IPfpAdService pfpAdService;
	private IBoardProvider boardProvider;
	private EmailUtils emailUtils;
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfdBoardService pfdBoardService;
	private String akbPfpServer;

	//發信參數
	private String mailFrom;
	private String mailDir;
	private String mailUserName;

	private String adStartDate;			// 傳入開始日期
	private String adEndDate;			// 傳入結束日期
	
	private String adGroupSeq;
	private String dateType;
	private String userAccount;
	private String searchAdStatus;
	private String searchType;
	private String adclkDevice;
	private PfpAdGroup adGroup;
	private EnumAdType[] searchAdType;
	private String adAdSeq;
	private String illegalize;	// 下架原因

	// return data
	private InputStream msg;
	private String result;

	public String execute() throws Exception{
	    if(StringUtils.isBlank(dateType))			dateType = "adActionDate";
		if(StringUtils.isBlank(searchAdStatus))		searchAdStatus = "";
		//if(StringUtils.isBlank(searchType))			searchType = "0";
		if(StringUtils.isNotBlank(adStartDate)) {
			startDate = adStartDate;
		}
		if(StringUtils.isNotBlank(adEndDate)) {
			endDate = adEndDate;
		}
		
		searchAdType = EnumAdType.values();
		adGroup = pfpAdGroupService.getPfpAdGroupBySeq(adGroupSeq);
		
		return SUCCESS;
	}

	
	
	//違規下架
	public String illegalizeAdAction() throws Exception{
		result = "fail";
		JSONArray adSeqArray = new JSONArray(adAdSeq);
		String userId = super.getSession().get(SessionConstants.SESSION_USER_ID).toString();
        Map<String, String> adTypeMap = ComponentUtils.getAdStatusDescMap();

		//發信內容
		String mailContent = "";
		File mailFile = new File(mailDir + EnumCategory.INVALID_DOWN.getCategory() + ".html");
        log.info(mailFile.getPath());
        if (mailFile.exists()) {
            try {
                mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
            } catch (IOException e) {
                log.error(mailFile.getPath(), e);
            }
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

		Date now = new Date();
		Date startDate = sdf2.parse(sdf2.format(now));
		 Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, 6);
			Date endDate = sdf2.parse(sdf2.format(c.getTime()));
		
		for(int i = 0; i <= adSeqArray.length()-1;i++){
			PfpAd ad = pfpAdService.getPfpAdBySeq(adSeqArray.get(i).toString());
			//log.info(" adGroup = "+adGroup);
			if(ad != null){
				ad.setAdStatus(EnumAdStatus.Illegalize.getStatusId());
				ad.setAdVerifyRejectReason(illegalize);
				ad.setAdUserVerifyTime(new Date());
				ad.setAdVerifyUser(userId);
				pfpAdService.saveOrUpdateWithCommit(ad);
				
				adGroupSeq = ad.getPfpAdGroup().getAdGroupSeq();
				
				String adTitle = "";
				String adContent = "";
				String realUrl = "";
				String showUrl = "";
				String adStatusDesc = adTypeMap.get(Integer.toString(ad.getAdStatus()));
				String rejectReason = ad.getAdVerifyRejectReason();
				String adStyle = ad.getAdStyle();
				String adImgUrl = "";
	            String adImgWidth = "0";
	            String adImgHeight = "0";
				
				Iterator<PfpAdDetail> iAdDetail = ad.getPfpAdDetails().iterator();
				while(iAdDetail.hasNext()) {
					PfpAdDetail adDetail = iAdDetail.next();
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
				}
				
				if("IMG".equals(adStyle)){
	        		if(adImgUrl.indexOf("original") == -1){
                    	if(adImgUrl.lastIndexOf("/") >= 0){
                    		String imgFilename = adImgUrl.substring(adImgUrl.lastIndexOf("/"));
                    		adImgUrl = adImgUrl.replace(imgFilename, "/original" + imgFilename);	
                    	}
                    } 
	        		Map<String,String> imgmap = new HashMap<String,String>();
					imgmap = getImgSize(adImgUrl);
					adImgWidth = imgmap.get("imgWidth");
					adImgHeight = imgmap.get("imgHeight");
	        		
	        	}

            	String pfpCustomerInfoId = ad.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId();
            	String customerInfoTitle = ad.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getCustomerInfoTitle();
            	String memberId = ad.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getMemberId();
            	
            	//查看是否有經銷商，有的話也要發經銷商公告
            	List<PfdUserAdAccountRef> PfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(pfpCustomerInfoId);
            	
            	if (PfdUserAdAccountRefList.size()>0) {
            		
            		String pfdCustomerInfoId = PfdUserAdAccountRefList.get(0).getPfdCustomerInfo().getCustomerInfoId();
            		String pfdUserId = PfdUserAdAccountRefList.get(0).getPfdUser().getUserId();
            		
            		//PFD 公告
            		String content = "廣告帳戶 <a href=\"./adAccountList.html\">" + customerInfoTitle + "</a>，";
            		String content2 = "廣告帳戶 <span style=\"color:#1d5ed6;\">" + customerInfoTitle + "</span>，";
            		if(adTitle.equals("")){
            			content += "圖像廣告違反刊登規範，已下架拒刊。";
            			content2 += "圖像廣告違反刊登規範，已下架拒刊。";
            		} else {
            			content += "<span style=\"color:#1d5ed6;\">" + adTitle + "</span> 廣告違反刊登規範，已下架拒刊。";
            			content2 += "<span style=\"color:#1d5ed6;\">" + adTitle + "</span> 廣告違反刊登規範，已下架拒刊。";
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
            		
            		//觀看權限(總管理者/帳戶管理/行政管理)
        			String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege();
        			board.setMsgPrivilege(msgPrivilege);
            		
            		board.setCreateDate(now);
            		
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
            		board2.setMsgPrivilege(EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.SALES_MANAGER.getPrivilege());
            		board2.setCreateDate(now);
            		pfdBoardService.save(board2);
            	}
            	
            	// PFP 公告
            	String boardContent = EnumCategory.INVALID_DOWN.getBoardContent();
            	if("IMG".equals(adStyle)){
            		if(adTitle.equals("")){
            			boardContent = boardContent.replace("ad_Title", "<a href=\"./adAdEditImg.html?adSeq="+adSeqArray.get(i).toString()+"\">圖像</a>");
            		} else {
            			boardContent = boardContent.replace("ad_Title", "<a href=\"./adAdEditImg.html?adSeq="+adSeqArray.get(i).toString()+"\">"+adTitle+"</a>");
            		}
            	} else {
            		boardContent = boardContent.replace("ad_Title", "<a href=\"./adAdEdit.html?adSeq="+adSeqArray.get(i).toString()+"\">"+adTitle+"</a>");
            	}
            	//boardContent = "<a href=\"./adAdEdit.html?adSeq="+adAdSeqs[i]+"\" target=\"_blank\">"+adTitle+"</a> 廣告已被違規下架" ;
            	
            	// 新增違規下架公告
            	boardProvider.add(pfpCustomerInfoId, boardContent, EnumBoardType.VERIFY, EnumCategory.INVALID_DOWN, adSeqArray.get(i).toString());
            	// Access Log 查詢新增記錄項目 - 違規下架
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	String message = adTitle + " -> " + sdf.format(new Date()) + "：廣告已違規下架";
            	admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.AD_STATUS_MODIFY, message, 
            			memberId, null, pfpCustomerInfoId, null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);	

				Iterator<PfpUser> itUser = ad.getPfpAdGroup().getPfpAdAction().getPfpCustomerInfo().getPfpUsers().iterator();

				List<String> mailList = new ArrayList<String>();
				//// 測試用
				//mailList.add("johnwei@staff.pchome.com.tw");
				//正式用
				while (itUser.hasNext()) {
					PfpUser pfpUser = itUser.next(); 
					//只發給主管理者
					if (EnumPrivilegeModel.ROOT_USER.getPrivilegeId().equals(pfpUser.getPrivilegeId())) {
						mailList.add(pfpUser.getUserEmail());
					}
				}

				String[] mailArray = mailList.toArray(new String[mailList.size()]);

				//整理發信內容
				String tmp = mailContent;

				tmp = tmp.replaceAll("@logoImg","<img src=\"http://show.pchome.com.tw/html/img/logo_pchome.gif\" />"
	                       + "<img src=\"http://show.pchome.com.tw/html/img/logo_index.gif\" />"
	                       + "<img src=\"http://show.pchome.com.tw/html/img/logo_pro.gif\" />");
				
				if("IMG".equals(adStyle)){
	            	int imgWidth = Integer.parseInt(adImgWidth);
	            	int imgHeight = Integer.parseInt(adImgHeight);
	            	
	            	if(imgWidth > imgHeight){
	            		imgHeight = imgHeight*90/imgWidth;
	            		imgWidth = 90;
	            	} else {
	            		imgWidth = imgWidth*90/imgHeight;
	            		imgHeight = 90;
	            	}
	            	
	            	tmp = tmp.replaceAll("@adStyle","【關鍵字-圖像廣告】");
	            	tmp = tmp.replaceAll("@img","<img src=\"" + akbPfpServer + adImgUrl +  "\"  />");
	            } else if("TMG".equals(adStyle)){
	            	tmp = tmp.replaceAll("@adStyle","【關鍵字-圖文廣告】");
	            	tmp = tmp.replaceAll("@img","<img src=\"" + akbPfpServer + adImgUrl + "\" style=\"width:90px;height:90px;\" />");
	            } else {
	            	tmp = tmp.replaceAll("@adStyle","");
	            	tmp = tmp.replaceAll("@img","");
	            }
				
				tmp = tmp.replaceAll("@adName", ad.getPfpAdGroup().getPfpAdAction().getAdActionName()); //廣告
				tmp = tmp.replaceAll("@adCategoryName", ad.getPfpAdGroup().getAdGroupName()); //分類
				
				if("IMG".equals(adStyle)){
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

				//發信
				Mail mail = new Mail();
	            mail.setMailFrom(mailFrom);
	            mail.setMailTo(mailArray);
	            mail.setRname(EnumCategory.INVALID_DOWN.getMailTitle());
	            mail.setMsg(tmp);

	            emailUtils.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mailUserName, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

			}
		}	
		
//		// adGroup 下層關鍵字或播放明細都被關閉, 此分類改為未完成
//		if(StringUtils.isNotBlank(adGroupSeq)){
//			//List<PfpAdKeyword> adKeyword = pfpAdKeywordService.validAdKeyword(adGroupSeq);
//			List<PfpAd> adAd = pfpAdService.validAdAd(adGroupSeq);
//			
//			if(adAd.size() <= 0 ){
//				PfpAdGroup adGroup = pfpAdGroupService.getPfpAdGroupBySeq(adGroupSeq);
//				adGroup.setAdGroupStatus(EnumAdStatus.UnDone.getStatusId());
//				adGroup.setAdGroupUpdateTime(new Date());
//				pfpAdGroupService.saveOrUpdate(adGroup);
//			}
//		}

		// 是否為 "儲存後再新增廣告"
		result = "saveOK";
		msg = new ByteArrayInputStream(result.getBytes());
		return SUCCESS;
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
	
	public void setPfdUserAdAccountRefService(IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public void setPfpAdGroupService(IPfpAdGroupService pfpAdGroupService) {
		this.pfpAdGroupService = pfpAdGroupService;
	}

//	public void setPfpAdKeywordService(IPfpAdKeywordService pfpAdKeywordService) {
//		this.pfpAdKeywordService = pfpAdKeywordService;
//	}
	
	public void setPfpAdService(IPfpAdService pfpAdService) {
		this.pfpAdService = pfpAdService;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
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

	public LinkedHashMap<String, String> getDateSelectMap() {
		return dateSelectMap;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getMailDir() {
		return mailDir;
	}

	public void setMailDir(String mailDir) {
		this.mailDir = mailDir;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}

	public String getAdStartDate() {
		return adStartDate;
	}

	public void setAdStartDate(String adStartDate) {
		this.adStartDate = adStartDate;
	}

	public String getAdEndDate() {
		return adEndDate;
	}

	public void setAdEndDate(String adEndDate) {
		this.adEndDate = adEndDate;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getSearchAdStatus() {
		return searchAdStatus;
	}

	public void setSearchAdStatus(String searchAdStatus) {
		this.searchAdStatus = searchAdStatus;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public EnumAdType[] getSearchAdType() {
		return searchAdType;
	}
	
	public String getAdGroupSeq() {
		return adGroupSeq;
	}

	public PfpAdGroup getAdGroup() {
		return adGroup;
	}

	



	public String getAdAdSeq() {
	    return adAdSeq;
	}



	public void setAdAdSeq(String adAdSeq) {
	    this.adAdSeq = adAdSeq;
	}



	public void setIllegalize(String illegalize) {
		this.illegalize = illegalize;
	}

	public InputStream getMsg() {
		return msg;
	}

	public void setMsg(InputStream msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Map<String, String> getAdPvclkDeviceMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "裝置");
		map.put("PC", "PC");
		map.put("mobile", "mobile");
		return map;
	}

	public String getAdclkDevice() {
	    return adclkDevice;
	}

	public void setAdclkDevice(String adclkDevice) {
	    this.adclkDevice = adclkDevice;
	}
	
	public void setAkbPfpServer(String akbPfpServer) {
		this.akbPfpServer = akbPfpServer;
	}



	public static void main(String args[]) throws Exception{
	   String d = "[\"ad_201312010001\"]";
//	   ["ad_201312010001","ad_201312040001"]
	   System.out.println(d);
	   
//	   JSONObject asGroupConditionJsonObject = new JSONObject(d);
	   JSONArray adSpaceResult= new JSONArray(d);
//	   System.out.println(adSpaceResult);
//	   System.out.println(adSpaceResult.length());
	   for(int i = 0;i<=adSpaceResult.length()-1;i++){
	       System.out.println(adSpaceResult.get(i));
	   }
	   
	   
	}
}
