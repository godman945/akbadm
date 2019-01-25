package com.pchome.akbadm.struts2.action.pfbx.account;

import com.pchome.akbadm.db.pojo.*;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.customerInfoRefXtype.IPfbxCustomerInfoRefXTypeService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionMenuService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.board.IPfbxBoardService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBankService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxPersonalService;
import com.pchome.akbadm.db.service.pfbx.play.IPfbxAllowUrlService;
import com.pchome.akbadm.db.service.pfbx.IPfbxWebsiteCategoryService;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxAllowUrlVO;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxBankVo;
import com.pchome.akbadm.db.vo.pfbx.account.PfbxPersonalVo;
import com.pchome.akbadm.factory.pfbx.bonus.CheckStatusFactory;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.akbadm.utils.DateTimeUtils;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.akbadm.utils.PfbxBoardUtils;
import com.pchome.enumerate.pfbx.account.EnumPfbAccountStatus;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountPlayType;
import com.pchome.enumerate.pfbx.account.EnumPfbxAllowUrlStatus;
import com.pchome.enumerate.pfbx.account.EnumPfbxCheckStatus;
import com.pchome.enumerate.pfbx.board.EnumBoardContent;
import com.pchome.enumerate.pfbx.user.EnumUserPrivilege;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.board.EnumPfbBoardType;
import com.pchome.soft.util.DateValueUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class PfbAccountAction extends BaseCookieAction {

    private static final long serialVersionUID = 1L;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //Service 注入
    private IPfbxCustomerInfoService pfbxCustomerInfoService;
    private IPfbxBoardService pfbxBoardService;
    private IPfbxBankService pfbxBankService;
    private IPfbxPersonalService pfbxPersonalService;
    private IPfbxPositionMenuService pfbxPositionMenuService;
    private IPfbxCustomerInfoRefXTypeService pfbxCustomerInfoRefXTypeService;
    private IAdmAccesslogService accesslogService;
    private CheckStatusFactory checkStatusFactory;
    private IPfbxAllowUrlService pfbxAllowUrlService;
    private IPfbxWebsiteCategoryService pfbxWebsiteCategoryService;

    // 查詢參數
    private String queryStartDate; // 申請日期起
    private String queryEndDate; // 申請日期迄
    private String queryType; // 查詢條件(帳戶編號、會員帳號、公司名稱、統一編號、聯絡人)
    private String queryText; // 查詢字串
    private String queryStatus; // 狀態
    private String queryCategory; // 帳戶類別

    // 查詢結果
    private List<PfbxCustomerInfo> accountList;
    private String message; //查詢的結果
    private String targetId; // 指定要修改的 id

    // 修改頁面資料
    private String customerInfoId;
    private String category;
    private String categoryName;
    private String memberId;
    private String userEmail;
    private String companyName;
    private String taxId;
    private String contactName;
    private String contactPhone;
    private String contactCell;
    private String applyDate;
    private String activeDate;
    private String closeDate;
    private String status;
    private String statusNote;
    private String websiteChineseName;
    private String websiteEnglishName;
    private String websiteDisplayUrl;
    private String adType;
    private String positionMenuIdArray;
    private String rootDomain;
    private String playType;
    private String categoryCode;

    //20170313修改播放網址資料
    private String updAllowUrlId;
    private String updCategoryCode;
    private String updRootDomain;
    private String updUrlStatus;
    private String updRefuseNote;
    
    // 20150714新增銀行、領款人資料
    // 銀行list
    private List<PfbxBankVo> pfbxBankVos;
    // 領款人list
    private List<PfbxPersonalVo> pfbxPersonalVos;
    // 廣告播放網站list
    private List<PfbxAllowUrlVO> pfbxAllowUrlVOs;
    // 審核狀態
    private EnumPfbxCheckStatus[] enumCheckStatus = null;
    //樣板屬性list
    List<PfbxCustomerInfoRefXType> pfbxCustomerInfoRefXTypeList = new ArrayList<PfbxCustomerInfoRefXType>();

    private String strResult = ""; // ajax return string
    private InputStream streamResult; // ajax return InputStream
    private Integer checkid; // pfbx_Bank id
    private String checkNote; // checkNote
    private String checkStatus; // checkStatus

    //Email 相關參數
    private String mailDir;
    private String mailServer;
    private String mailFrom;
    private String mailFromAlias;
    private String auditSuccessSubject;
    private String auditRefuseSubject;

    public String execute() throws Exception {

        log.info(">>> queryStartDate = " + queryStartDate);
        log.info(">>> queryEndDate = " + queryEndDate);
        log.info(">>> queryType = " + queryType);
        log.info(">>> queryText = " + queryText);
        log.info(">>> queryStatus = " + queryStatus);
        log.info(">>> queryCategory = " + queryCategory);

        Map<String, String> conditionMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(queryStartDate)) {
            conditionMap.put("createStartDate", queryStartDate);
        }
        if (StringUtils.isNotBlank(queryEndDate)) {
            conditionMap.put("createEndDate", queryEndDate);
        }
        if (StringUtils.isNotBlank(queryStatus)) {
            conditionMap.put("status", "'" + queryStatus + "'");
        }
        if (StringUtils.isNotBlank(queryType) && StringUtils.isNotBlank(queryText)) {
            conditionMap.put(queryType, queryText);
        }
        if (StringUtils.isNotBlank(queryCategory)) {
            conditionMap.put("category", queryCategory);
        }

        this.accountList = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);

//		for (PfbxCustomerInfoRefXType pfbxCustomerInfoRefXType : pfbxCustomerInfoRefXTypeService.loadAll()) {
//		    System.out.println(pfbxCustomerInfoRefXType
//			    .getPfbxPositionMenu().getPfbxMenuName());
//		}

        return SUCCESS;
    }

    /**
     * 取得PFB User的頁面資料
     *
     * @return
     * @throws Exception
     */
    public String goUpdatePage() throws Exception {
        log.info(">>> targetId = " + targetId);

        Map<String, String> conditionMap = new HashMap<String, String>();
        conditionMap.put("customerInfoId", targetId);

        PfbxCustomerInfo customerInfo = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap).get(0);

        //檢查預設網址是否存在，不存在則新增預設網址
  		PfbxAllowUrl defaultUrl = pfbxAllowUrlService.findDefaultUrl(customerInfo.getCustomerInfoId());
  		
  		if(defaultUrl == null){
  			defaultUrl = this.addDefaultUrl(customerInfo);
  		}
        
        this.customerInfoId = customerInfo.getCustomerInfoId();
        this.category = customerInfo.getCategory();
        this.categoryName = getQueryCategoryOptionsMap().get(customerInfo.getCategory());
        this.memberId = customerInfo.getMemberId();
        // private String userEmail;
        Iterator<PfbxUser> it = customerInfo.getPfbxUsers().iterator();
        while (it.hasNext()) {
            PfbxUser user = it.next();
            if (user.getPrivilegeId() == EnumUserPrivilege.ROOT_USER.getPrivilegeId().intValue()) {
                this.userEmail = user.getUserEmail();
            }
        }
        this.companyName = customerInfo.getCompanyName();
        this.taxId = customerInfo.getTaxId();
        this.contactName = customerInfo.getContactName();
        this.contactPhone = customerInfo.getContactPhone();
        this.contactCell = customerInfo.getContactCell();
        if (customerInfo.getCreateDate() != null) {
            this.applyDate = DateTimeUtils.getDateStr(customerInfo.getCreateDate() , DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
        }
        //若狀態為申請中，則無開通時間
        if (customerInfo.getActivateDate() != null
                && !StringUtils.equals(customerInfo.getStatus() , EnumPfbAccountStatus.APPLY.getStatus())) {
            this.activeDate = DateTimeUtils.getDateStr(customerInfo.getActivateDate(), DateTimeUtils.yyyy_MM_dd_HH_mm_ss);
        }


        if (customerInfo.getCloseDate() != null) {
            this.closeDate = DateTimeUtils.getDateStr(customerInfo.getCloseDate() , DateTimeUtils.yyyy_MM_dd);
        }
        this.status = customerInfo.getStatus();
        this.statusNote = customerInfo.getStatusNote();
        this.websiteChineseName = customerInfo.getWebsiteChineseName();
//        this.websiteEnglishName = customerInfo.getWebsiteEnglishName();
        this.websiteDisplayUrl = customerInfo.getWebsiteDisplayUrl();
        this.adType = String.valueOf(customerInfo.getAdType());

        for (PfbxCustomerInfoRefXType pfbxCustomerInfoRefXType : customerInfo.getPfbxCustomerInfoRefXTypes()) {
            this.pfbxCustomerInfoRefXTypeList.add(pfbxCustomerInfoRefXType);
        }

        playType = "0";
        if(StringUtils.isNotEmpty(customerInfo.getPlayType())){
        	playType = customerInfo.getPlayType();
        }
        
        rootDomain = defaultUrl.getRootDomain();
        
        categoryCode = "";
        if(StringUtils.isNotEmpty(defaultUrl.getCategoryCode())){
        	categoryCode = defaultUrl.getCategoryCode();
        }
        
        
        enumCheckStatus = EnumPfbxCheckStatus.values();

        // 根據帳戶ID取得銀行資料
        pfbxBankVos = pfbxBankService.getPfbxBankVosByCustomerId(customerInfoId);
        pfbxPersonalVos = pfbxPersonalService.getPfbxPersonalVosByCustomerId(customerInfoId);

        // 根據帳戶ID取得播放網站資料
        pfbxAllowUrlVOs = new ArrayList<PfbxAllowUrlVO>();
        List<PfbxAllowUrl> pfbxAllowUrlList = pfbxAllowUrlService.findPfbxAllowUrlByCondition(customerInfoId);
        
        if(!pfbxAllowUrlList.isEmpty()){
        	for(PfbxAllowUrl pfbxAllowUrl:pfbxAllowUrlList){
        		PfbxAllowUrlVO pfbxAllowUrlVO = new PfbxAllowUrlVO();
        		
        		pfbxAllowUrlVO.setId(pfbxAllowUrl.getId().toString());
        		pfbxAllowUrlVO.setCustomerInfoId(pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId());
        		pfbxAllowUrlVO.setCategoryCode(pfbxAllowUrl.getCategoryCode());
        		pfbxAllowUrlVO.setUrl(pfbxAllowUrl.getUrl());
        		pfbxAllowUrlVO.setRootDomain(pfbxAllowUrl.getRootDomain());
        		pfbxAllowUrlVO.setUrlName(pfbxAllowUrl.getUrlName());
        		pfbxAllowUrlVO.setUrlStatus(pfbxAllowUrl.getUrlStatus());
        		pfbxAllowUrlVO.setRefuseNote(pfbxAllowUrl.getRefuseNote());
        		pfbxAllowUrlVO.setDefaultType(pfbxAllowUrl.getDefaultType());
        		pfbxAllowUrlVO.setDeleteFlag(pfbxAllowUrl.getDeleteFlag());
        		pfbxAllowUrlVO.setUpdateDate(format.format(pfbxAllowUrl.getUpdateDate()));
        		pfbxAllowUrlVO.setCreateDate(format.format(pfbxAllowUrl.getCreateDate()));
        		
        		pfbxAllowUrlVOs.add(pfbxAllowUrlVO);
        	}
        }
        
        return SUCCESS;
    }

    /**
     * 更新銀行帳戶狀態
     *
     * @return
     * @throws Exception
     */
    public String doUpdateBankStatus() throws Exception {
        if (StringUtils.isNotBlank(checkid.toString())) {
            PfbxBank bank = pfbxBankService.get(checkid);
            bank.setCheckStatus(checkStatus);
            bank.setCheckNote(checkNote);
            pfbxBankService.update(bank);

            PfbxCustomerInfo pfbxCustomerInfo = bank.getPfbxCustomerInfo();
            if(StringUtils.equals(checkStatus,"3")){ //審核失敗要寫公告
                String linkUrl="viewBank.html"; //帳戶管理頁面
                PfbxBoardUtils.writePfbxBoard(pfbxCustomerInfo.getCustomerInfoId(),EnumPfbBoardType.FINANCE,EnumBoardContent.BOARD_CONTENT_9,linkUrl);
            }
            strResult = "success";
        } else {
            strResult = "noid";
        }
        return SUCCESS;
    }

    /**
     * 更新收款人資訊
     *
     * @return
     * @throws Exception
     */
    public String doUpdatePersonalStatus() throws Exception {
        if (StringUtils.isNotBlank(checkid.toString())) {
            PfbxPersonal personal = pfbxPersonalService.get(checkid);
            personal.setCheckStatus(checkStatus);
            personal.setCheckNote(checkNote);
            pfbxPersonalService.update(personal);

            PfbxCustomerInfo pfbxCustomerInfo = personal.getPfbxCustomerInfo();
            if(StringUtils.equals(checkStatus,"3")){ //審核失敗要寫公告
                String linkUrl="viewPersonalInfo.html"; //收款人資訊
                PfbxBoardUtils.writePfbxBoard(pfbxCustomerInfo.getCustomerInfoId(),EnumPfbBoardType.FINANCE,EnumBoardContent.BOARD_CONTENT_10,linkUrl);
            }
            strResult = "success";
        } else {
            strResult = "noid";
        }

        return SUCCESS;
    }

    /**
     * 更新帳戶資料
     * @return
     * @throws Exception
     */
    public String doUpdate() throws Exception {
        log.info(">>> customerInfoId = " + customerInfoId);
        log.info(">>> status = " + status);
        Map<String, String> conditionMap = new HashMap<String, String>();
        Map<String,String> msgMap = new HashMap<String, String>();

        conditionMap.put("customerInfoId", customerInfoId);
        PfbxCustomerInfo customerInfo = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap).get(0);
        String orgStatus = customerInfo.getStatus();
        customerInfo.setAdType(Integer.valueOf(adType));

        msgMap.put("preStatus", checkStatusFactory.getEnumPfbAccountStatusName(customerInfo.getStatus()));
        msgMap.put("newStatus",checkStatusFactory.getEnumPfbAccountStatusName(status));
        customerInfo.setStatus(status);
        customerInfo.setStatusNote(statusNote);
        customerInfo.setUpdateDate(new Date());
        //紀錄結束時間
        if(StringUtils.isNotBlank(closeDate)) {
            customerInfo.setCloseDate(DateTimeUtils.parseDate(closeDate, DateTimeUtils.yyyy_MM_dd));
        }else{
            customerInfo.setCloseDate(null);
        }

        //若審核狀態變更為開通的話，要寫入開通日期，清空結束日期
        if(!StringUtils.equals( orgStatus , EnumPfbAccountStatus.START.getStatus())
                && StringUtils.equals(status,EnumPfbAccountStatus.START.getStatus())){
            customerInfo.setActivateDate(new Date());
            customerInfo.setCloseDate(null);
        }

        //若狀態為申請中，則清空開通時間
        if(StringUtils.equals( status , EnumPfbAccountStatus.APPLY.getStatus())){
            customerInfo.setActivateDate(null);
        }

        //若審核狀態是關閉的話，寫入結束日期
        //若未填寫結束時間，則帶入現在時間
        if(!StringUtils.equals( orgStatus , EnumPfbAccountStatus.CLOSE.getStatus())
                && StringUtils.equals(status,EnumPfbAccountStatus.CLOSE.getStatus())
                && StringUtils.isBlank(closeDate)){
            customerInfo.setCloseDate(new Date());
        }

        String oldPlayType = customerInfo.getPlayType();
        customerInfo.setPlayType(playType);
        
        customerInfo.setWebsiteChineseName(websiteChineseName);
        customerInfo.setWebsiteDisplayUrl(websiteDisplayUrl);
        
        pfbxCustomerInfoService.saveOrUpdate(customerInfo);

        //紀錄AccessLog
        String preStatus = convertMsgMap(msgMap,"preStatus","");
        String newStatus = convertMsgMap(msgMap,"newStatus","");
        if(!StringUtils.equals(preStatus,newStatus)) { //狀態有異動要寫Log
            String accessLogMsg = "帳戶狀態-->" + preStatus + "變更成" + newStatus;
            accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
                    EnumAccesslogAction.ACCOUNT_MODIFY,
                    accessLogMsg,
                    String.valueOf(session.get("session_user_id")),
                    null,
                    customerInfoId,
                    null,
                    request.getRemoteAddr(),
                    EnumAccesslogEmailStatus.NO);
        }

        if(!StringUtils.equals(oldPlayType,playType)){
        	Map<String, String> playTypeMap = new LinkedHashMap<String, String>();
        	playTypeMap = getQueryPlayTypeMap();
        	String accessLogMsg = "廣告撥放網域限制-->" + playTypeMap.get(oldPlayType) + "變更成" + playTypeMap.get(playType);
        	accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
                    EnumAccesslogAction.ACCOUNT_MODIFY,
                    accessLogMsg,
                    String.valueOf(session.get("session_user_id")),
                    null,
                    customerInfoId,
                    null,
                    request.getRemoteAddr(),
                    EnumAccesslogEmailStatus.NO);
        }
        
        Date now = new Date();
        String linkUrl ="";
        // 帳戶從開通被關閉時要寫公告
        if (StringUtils.equals(orgStatus , EnumPfbAccountStatus.START.getStatus())
                && StringUtils.equals(status , EnumPfbAccountStatus.CLOSE.getStatus())) {
        	linkUrl = "http://faq.pchome.com.tw/service/user_reply.html?ch=PFB";
        	PfbxBoardUtils.writePfbxBoard(customerInfoId,EnumPfbBoardType.REMIND,EnumBoardContent.BOARD_CONTENT_2,linkUrl);
        }

        // 帳戶被還原時要移除關閉的公告，並寫重新開通的公告
        if (StringUtils.equals( orgStatus , EnumPfbAccountStatus.CLOSE.getStatus())
                && StringUtils.equals( status , EnumPfbAccountStatus.START.getStatus())) {
            //移除關閉的公告
//            pfbxBoardService.deleteBoard(customerInfoId, EnumBoardContent.BOARD_CONTENT_2.getId());
            //寫入重新開通的公告
            linkUrl="play.html"; //播放管理
            PfbxBoardUtils.writePfbxBoard(customerInfoId,EnumPfbBoardType.REMIND,EnumBoardContent.BOARD_CONTENT_8,linkUrl);
        }

        //審核通過時需發送Email
        //審核失敗過時需發送Email
        if((!StringUtils.equals( orgStatus , EnumPfbAccountStatus.START.getStatus())
                && StringUtils.equals( status , EnumPfbAccountStatus.START.getStatus()))
            || (!StringUtils.equals( orgStatus , EnumPfbAccountStatus.FAIL.getStatus())
                && StringUtils.equals( status , EnumPfbAccountStatus.FAIL.getStatus()))){

            String userEmail ="";
            // private String userEmail;
            Iterator<PfbxUser> it = customerInfo.getPfbxUsers().iterator();
            while (it.hasNext()) {
                PfbxUser user = it.next();
                if (user.getPrivilegeId() == EnumUserPrivilege.ROOT_USER.getPrivilegeId().intValue()) {
                    userEmail = user.getUserEmail();
                }
            }
            
            String category = customerInfo.getCategory();
            String customerInfoName = customerInfo.getCompanyName();
            
            if(StringUtils.equals("1", category)){
            	customerInfoName = customerInfo.getContactName();
            }
            	
            
            sendMail(userEmail,status,customerInfoName);
        }

        //處理版位資訊
        List<String> positionMenuIdList = new ArrayList<String>();
        if (!positionMenuIdArray.equals("[]")) {
            String data = "";
            for (char a : positionMenuIdArray.toCharArray()) {
                if (String.valueOf(a).equals("[") || String.valueOf(a).equals("\"")) {
                    continue;
                }
                if (String.valueOf(a).equals("]")) {
                    positionMenuIdList.add(data);
                } else {
                    if (String.valueOf(a).equals(",")) {
                        positionMenuIdList.add(data);
                        data = "";
                    } else {
                        data = data + String.valueOf(a);
                    }
                }
            }

            for (PfbxCustomerInfoRefXType pfbxCustomerInfoRefXType : customerInfo.getPfbxCustomerInfoRefXTypes()) {
                pfbxCustomerInfoRefXType.setStatus(0);
                pfbxCustomerInfoRefXTypeService.saveOrUpdate(pfbxCustomerInfoRefXType);
            }

            for (PfbxCustomerInfoRefXType pfbxCustomerInfoRefXType : customerInfo.getPfbxCustomerInfoRefXTypes()) {
                for (String positionMenuId : positionMenuIdList) {
                    if (Integer.parseInt(positionMenuId) == pfbxCustomerInfoRefXType.getPfbxPositionMenu().getId()) {
                        pfbxCustomerInfoRefXType.setStatus(1);
                        pfbxCustomerInfoRefXTypeService.saveOrUpdate(pfbxCustomerInfoRefXType);
                    }
                }
            }
        } else {
            for (PfbxCustomerInfoRefXType pfbxCustomerInfoRefXType : customerInfo.getPfbxCustomerInfoRefXTypes()) {
                pfbxCustomerInfoRefXType.setStatus(0);
                pfbxCustomerInfoRefXTypeService.saveOrUpdate(pfbxCustomerInfoRefXType);
            }
        }
        
        //更新預設網站資訊
  		PfbxAllowUrl defaultUrl = pfbxAllowUrlService.findDefaultUrl(customerInfo.getCustomerInfoId());
  		
  		if(defaultUrl != null){
  			String oldRootDomain = defaultUrl.getRootDomain();
  			
  			defaultUrl.setPfbxCustomerInfo(customerInfo);
  			if(StringUtils.isNotEmpty(categoryCode)){
  				defaultUrl.setCategoryCode(categoryCode);
  			} else {
  				defaultUrl.setCategoryCode("");
  			}
  			defaultUrl.setUrlName(websiteChineseName);
  			defaultUrl.setUrl(websiteDisplayUrl);
  			defaultUrl.setRootDomain(rootDomain);
  			defaultUrl.setUpdateDate(now);
  			
  			pfbxAllowUrlService.saveOrUpdate(defaultUrl);
  			
  			//紀錄AccessLog
  			if(!StringUtils.equals(oldRootDomain,rootDomain)){
  	        	String accessLogMsg = "網站名稱：" + defaultUrl.getUrlName() + "，主網域-->" + oldRootDomain + "變更成" + rootDomain;
  	        	accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
  	                    EnumAccesslogAction.ACCOUNT_MODIFY,
  	                    accessLogMsg,
  	                    String.valueOf(session.get("session_user_id")),
  	                    null,
  	                    customerInfoId,
  	                    null,
  	                    request.getRemoteAddr(),
  	                    EnumAccesslogEmailStatus.NO);
  	        }
  		}
        
        strResult = "success";
        return SUCCESS;
    }

    /**
     * 更新允許網站狀態資訊
     *
     * @return
     * @throws Exception
     */
    public String doUpdAllowUrlStatus() throws Exception {
    	
    	Date now = new Date();
    	strResult = "noid";
    	String oldUrlStatus = EnumPfbxAllowUrlStatus.APPLY.getCode();
    	
    	if(StringUtils.isNotEmpty(updAllowUrlId)){
    		PfbxAllowUrl pfbxAllowUrl = pfbxAllowUrlService.getPfbxAllowUrlById(updAllowUrlId);
    		
    		if(pfbxAllowUrl != null){
    			if(StringUtils.equals(EnumPfbxAllowUrlStatus.APPLY.getCode(), updUrlStatus) || StringUtils.equals(EnumPfbxAllowUrlStatus.START.getCode(), updUrlStatus)){
	    			//檢查主網域是否有重複
	        		List<PfbxAllowUrl> list = pfbxAllowUrlService.findPfbxAllowUrlByRootDomain(pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId(), pfbxAllowUrl.getRootDomain(), updAllowUrlId);
	        		if(!list.isEmpty()){
	        			strResult = "repeat";
	        			return SUCCESS;
	        		}
    			}
    			
    			//檢查網站類型是否為空
    			if(StringUtils.equals(EnumPfbxAllowUrlStatus.START.getCode(), updUrlStatus) && StringUtils.isBlank(pfbxAllowUrl.getCategoryCode())){
    				strResult = "nocode";
        			return SUCCESS;
    			}
    			
    			oldUrlStatus = pfbxAllowUrl.getUrlStatus();
    			pfbxAllowUrl.setUrlStatus(updUrlStatus);
    			if(StringUtils.equals(EnumPfbxAllowUrlStatus.FAIL.getCode(), updUrlStatus) || StringUtils.equals(EnumPfbxAllowUrlStatus.STOP.getCode(), updUrlStatus)){
    				pfbxAllowUrl.setRefuseNote(updRefuseNote);
    			} else {
    				pfbxAllowUrl.setRefuseNote(null);
    			}
    			
    			pfbxAllowUrl.setUpdateDate(now);
    			pfbxAllowUrlService.saveOrUpdate(pfbxAllowUrl);
    			
    			//新增公告
    			String boardContent = "";
    	        String linkUrl ="";
    	        String boardContentId = EnumBoardContent.BOARD_CONTENT_15.getId();
    			
    	        if(StringUtils.equals(EnumPfbxAllowUrlStatus.START.getCode(), updUrlStatus) && StringUtils.equals(EnumPfbxAllowUrlStatus.STOP.getCode(), oldUrlStatus)){
    	        	boardContentId = EnumBoardContent.BOARD_CONTENT_18.getId();
    	        	boardContent = EnumBoardContent.BOARD_CONTENT_18.getContent();
    	        	linkUrl = "play.html";
    	        } else if(StringUtils.equals(EnumPfbxAllowUrlStatus.START.getCode(), updUrlStatus) && !StringUtils.equals(EnumPfbxAllowUrlStatus.STOP.getCode(), oldUrlStatus)){
    	        	boardContentId = EnumBoardContent.BOARD_CONTENT_16.getId();
    	        	boardContent = EnumBoardContent.BOARD_CONTENT_16.getContent();
    	        	linkUrl = "play.html";
    	        } else if(StringUtils.equals(EnumPfbxAllowUrlStatus.FAIL.getCode(), updUrlStatus)){
    	        	boardContentId = EnumBoardContent.BOARD_CONTENT_15.getId();
    	        	boardContent = EnumBoardContent.BOARD_CONTENT_15.getContent();
    	        	linkUrl = "listAllowUrl.html";
    	        } else if(StringUtils.equals(EnumPfbxAllowUrlStatus.STOP.getCode(), updUrlStatus)){
    	        	boardContentId = EnumBoardContent.BOARD_CONTENT_17.getId();
    	        	boardContent = EnumBoardContent.BOARD_CONTENT_17.getContent();
    	        	linkUrl = "listAllowUrl.html";
    	        }
    			
    	        boardContent = boardContent.replaceAll("〈網站〉", "<span style='color:#0088cc;' >" + pfbxAllowUrl.getUrlName() + "</span>");
    	        PfbxBoardUtils.writePfbxBoard_UserContent2(pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId(),EnumPfbBoardType.VERIFY,boardContentId,boardContent,linkUrl);
    	        
    	        //新增accessLog記錄
    	        if(!StringUtils.equals(oldUrlStatus, updUrlStatus)){
    	        	
    	        	String oldUrlStatusName = "";
    	        	String newUrlStatusName = "";
    	        	
    	        	for(EnumPfbxAllowUrlStatus enumPfbxAllowUrlStatus:EnumPfbxAllowUrlStatus.values()){
    	        		if(StringUtils.equals(enumPfbxAllowUrlStatus.getCode(), oldUrlStatus)){
    	        			oldUrlStatusName = enumPfbxAllowUrlStatus.getChName();
    	        		}
    	        		if(StringUtils.equals(enumPfbxAllowUrlStatus.getCode(), updUrlStatus)){
    	        			newUrlStatusName = enumPfbxAllowUrlStatus.getChName();
    	        		}
    	        	}
    	        	
    	        	String accessLogMsg = "廣告播放網址:" + pfbxAllowUrl.getUrlName() + "(" + pfbxAllowUrl.getUrl() + ")" + "網站狀態-->" + oldUrlStatusName + "變更成" + newUrlStatusName;
    	        	accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
    	        			EnumAccesslogAction.ACCOUNT_MODIFY,
    	        			accessLogMsg,
    	        			String.valueOf(session.get("session_user_id")),
    	        			null,
    	        			pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId(),
    	        			null,
    	        			request.getRemoteAddr(),
    	        			EnumAccesslogEmailStatus.NO);
    	        	
    	        	strResult = "success";
    	        }
    		}
    	}
    	
    	return SUCCESS;
    }
    
    /**
     * 更新允許網站主網域
     *
     * @return
     * @throws Exception
     */
    public String doUpdAllowUrlRootDomain() throws Exception {
    	
    	Date now = new Date();
    	strResult = "noid";
    	
    	if(StringUtils.isNotEmpty(updAllowUrlId)){
    		PfbxAllowUrl pfbxAllowUrl = pfbxAllowUrlService.getPfbxAllowUrlById(updAllowUrlId);
    		
    		//檢查主網域是否有重複
    		List<PfbxAllowUrl> list = pfbxAllowUrlService.findPfbxAllowUrlByRootDomain(pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId(), updRootDomain, updAllowUrlId);
    		if(!list.isEmpty()){
    			strResult = "repeat";
    			return SUCCESS;
    		}
    		
    		String oldRootDomain = pfbxAllowUrl.getRootDomain();
    		
    		if(StringUtils.equals(updRootDomain.substring(updRootDomain.length() -1), "/")){
    			updRootDomain = updRootDomain.substring(0,updRootDomain.length() -1);
  			}
    		
    		pfbxAllowUrl.setRootDomain(updRootDomain);
    		pfbxAllowUrl.setUpdateDate(now);
    		pfbxAllowUrlService.saveOrUpdate(pfbxAllowUrl);
    		
    		//紀錄AccessLog
  			if(!StringUtils.equals(oldRootDomain,updRootDomain)){
  	        	String accessLogMsg = "網站名稱：" + pfbxAllowUrl.getUrlName() + "，主網域-->" + oldRootDomain + "變更成" + updRootDomain;
  	        	accesslogService.addAdmAccesslog(EnumAccesslogChannel.PFB,
  	                    EnumAccesslogAction.ACCOUNT_MODIFY,
  	                    accessLogMsg,
  	                    String.valueOf(session.get("session_user_id")),
  	                    null,
  	                    pfbxAllowUrl.getPfbxCustomerInfo().getCustomerInfoId(),
  	                    null,
  	                    request.getRemoteAddr(),
  	                    EnumAccesslogEmailStatus.NO);
  	        }
    		
    		strResult = "success";
    	}
    	
    	return SUCCESS;
    }
    
    /**
     * 更新網站類別
     *
     * @return
     * @throws Exception
     */
    public String doUpdAllowUrlCategoryCode() throws Exception {
    	
    	Date now = new Date();
    	strResult = "noid";
    	
    	if(StringUtils.isNotEmpty(updAllowUrlId)){
    		PfbxAllowUrl pfbxAllowUrl = pfbxAllowUrlService.getPfbxAllowUrlById(updAllowUrlId);
    		
    		if(StringUtils.isEmpty(updCategoryCode)){
    			strResult = "nocode";
    			return SUCCESS;
    		}
    		
    		pfbxAllowUrl.setCategoryCode(updCategoryCode);
    		pfbxAllowUrl.setUpdateDate(now);
    		pfbxAllowUrlService.saveOrUpdate(pfbxAllowUrl);
    		
    		strResult = "success";
    	}
    	
    	return SUCCESS;
    }
    
    /**
     * 依審核狀況發送相關Email
     * @param contactEmail
     * @param status
     */
    private void sendMail(String contactEmail , String status,String customerInfoName) {
        //發送通知信
        String mailContent = "";
        String mailSubject = auditSuccessSubject;

        File mailFile = new File(mailDir + "pfbApplySuccess.html");
        //若審核失敗，則改用審核失敗的通知信
        if(StringUtils.equals(status,EnumPfbAccountStatus.FAIL.getStatus())){
            mailFile = new File(mailDir + "pfbApplyRefuse.html");
            mailSubject = auditRefuseSubject;
        }

        if (mailFile.exists()) {
            try {
                mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.error(">>> error file path: " + mailDir + "pfd_invite.html");
        }

        if (!mailContent.equals("")) {

            String endDate = DateValueUtil.getInstance().getDateValue(7, DateValueUtil.DBPATH);
            log.info(">>> endDate = " + endDate);

            mailContent = mailContent.replace("{:accountName:}", customerInfoName);

            EmailUtils.getInstance().setHost(mailServer);

            try {
                log.info(">>> start to send invite mail ");
                EmailUtils.getInstance().sendHtmlEmail(mailSubject, mailFrom,mailFromAlias, new String[]{contactEmail}, null, mailContent);
                log.info(">>> end of send invite mail ");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            log.error(">>> mailContent is empty");
        }
    }

    /**
     * 新增預設網址名單
     * @param customerInfoId
     * @return PfbxAllowUrl
     */
  	private PfbxAllowUrl addDefaultUrl(PfbxCustomerInfo customerInfo){

  		PfbxAllowUrl data = new PfbxAllowUrl();
  	
  		if(customerInfo != null){
  			
  			String url = customerInfo.getWebsiteDisplayUrl();
  			url = url.replace("http://", "");
  			url = url.replace("https://", "");
  			
  			data.setPfbxCustomerInfo(customerInfo);
  			data.setUrl(url);
  			
  			if(StringUtils.equals(url.substring(url.length() -1), "/")){
  				url = url.substring(0,url.length() -1);
  			}
  			
  			data.setRootDomain(url);
  			data.setUrlName(customerInfo.getWebsiteChineseName());
  			data.setUrlStatus(EnumPfbxAllowUrlStatus.START.getCode());
  			data.setDefaultType("Y");
  			data.setDeleteFlag("0");
  			data.setCategoryCode("");
  			data.setUpdateDate(new Date());
  			data.setCreateDate(new Date());
  			
  			pfbxAllowUrlService.saveOrUpdate(data);
  		}
  		
  		return data;
  	}
    
    /**
     * 取得msgMap 的內容字串
     * @param msgMap
     * @param key
     * @return
     */
    private String convertMsgMap(Map msgMap, String key ,String spStr) {
        String resultStr ="";
        if(msgMap.get(key) != null){
            resultStr = String.valueOf(msgMap.get(key)) + spStr;
        }
        return resultStr;
    }

    public String getPositionMenuIdArray() {
        return positionMenuIdArray;
    }

    public void setPositionMenuIdArray(String positionMenuIdArray) {
        this.positionMenuIdArray = positionMenuIdArray;
    }

    /**
     * 類別清單
     * @return
     */
    public Map<String, String> getQueryTypeOptionsMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "全部");
        map.put("customerInfoId", "帳戶編號");
        map.put("memberId", "會員帳號");
        map.put("companyName", "公司名稱");
        map.put("taxId", "統一編號");
        map.put("contactName", "聯絡人");
        return map;
    }

    /**
     * 狀態清單
     * @return
     */
    public Map<String, String> getQueryStatusOptionsMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "全部");

        for (EnumPfbAccountStatus s : EnumPfbAccountStatus.values()) {
            //不顯示刪除的選項
            if(!StringUtils.equals(s.getStatus() , EnumPfbAccountStatus.DELETE.getStatus())) {
                map.put(s.getStatus(), s.getDescription());
            }
        }

        return map;
    }

    /**
     * 播放網域限制清單
     * @return
     */
    public Map<String, String> getQueryPlayTypeMap(){
    	 Map<String, String> map = new LinkedHashMap<String, String>();
    	 
    	 for (EnumPfbxAccountPlayType type:EnumPfbxAccountPlayType.values()){
    		 map.put(type.getType(), type.getChName());
    	 }
    	 
    	 return map;
    }
    
    /**
     * 網站類型清單
     * @return
     */
    public Map<String, String> getQueryCategoryCodeMap(){
    	 Map<String, String> map = new LinkedHashMap<String, String>();
    	 map.put("", "尚未分類");
    	 
    	 List<PfbxWebsiteCategory> categoryCodeList = new ArrayList<PfbxWebsiteCategory>();
    	 categoryCodeList = pfbxWebsiteCategoryService.getFirstLevelPfpPfbxWebsiteCategory();
    	 
    	 if(!categoryCodeList.isEmpty()){
    		 for (PfbxWebsiteCategory pfbxWebsiteCategory:categoryCodeList){
    			 map.put(pfbxWebsiteCategory.getCode(), pfbxWebsiteCategory.getName());
    		 } 
    	 }
    	 
    	 return map;
    }
    
    /**
     * 播放網站狀態清單
     * @return
     */
    public Map<String, String> getQueryUrlStatusMap(){
    	 Map<String, String> map = new LinkedHashMap<String, String>();
    	 
    	 for (EnumPfbxAllowUrlStatus type:EnumPfbxAllowUrlStatus.values()){
    		 map.put(type.getCode(), type.getChName());
    	 }
    	 
    	 return map;
    }
    
    /**
     * 身分別清單
     * @return
     */
    public Map<String, String> getQueryCategoryOptionsMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("", "全部");

        for (EnumPfbxAccountCategory c : EnumPfbxAccountCategory.values()) {
            map.put(c.getCategory(), c.getChName());
        }

        return map;
    }

    public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
        this.pfbxCustomerInfoService = pfbxCustomerInfoService;
    }

    public void setPfbxBoardService(IPfbxBoardService pfbxBoardService) {
        this.pfbxBoardService = pfbxBoardService;
    }

    public void setPfbxBankService(IPfbxBankService pfbxBankService) {
        this.pfbxBankService = pfbxBankService;
    }

    public void setPfbxPersonalService(IPfbxPersonalService pfbxPersonalService) {
        this.pfbxPersonalService = pfbxPersonalService;
    }

    public void setPfbxPositionMenuService(
            IPfbxPositionMenuService pfbxPositionMenuService) {
        this.pfbxPositionMenuService = pfbxPositionMenuService;
    }

    public void setPfbxCustomerInfoRefXTypeService(
            IPfbxCustomerInfoRefXTypeService pfbxCustomerInfoRefXTypeService) {
        this.pfbxCustomerInfoRefXTypeService = pfbxCustomerInfoRefXTypeService;
    }

    public void setMailDir(String mailDir) {
        this.mailDir = mailDir;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailFromAlias(String mailFromAlias) {
        this.mailFromAlias = mailFromAlias;
    }

    public void setAuditSuccessSubject(String auditSuccessSubject) {
        this.auditSuccessSubject = auditSuccessSubject;
    }

    public void setAuditRefuseSubject(String auditRefuseSubject) {
        this.auditRefuseSubject = auditRefuseSubject;
    }

    public void setCheckStatusFactory(CheckStatusFactory checkStatusFactory) {
        this.checkStatusFactory = checkStatusFactory;
    }

    public void setAccesslogService(IAdmAccesslogService accesslogService) {
        this.accesslogService = accesslogService;
    }


    public String getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(String queryStartDate) {
        this.queryStartDate = queryStartDate;
    }

    public String getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(String queryEndDate) {
        this.queryEndDate = queryEndDate;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getQueryCategory() {
        return queryCategory;
    }

    public void setQueryCategory(String queryCategory) {
        this.queryCategory = queryCategory;
    }

    public List<PfbxCustomerInfo> getAccountList() {
        return accountList;
    }

    public String getMessage() {
        return message;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCustomerInfoId() {
        return customerInfoId;
    }

    public void setCustomerInfoId(String customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void setStatusNote(String statusNote) {
        this.statusNote = statusNote;
    }

    public String getCategory() {
        return category;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactCell() {
        return contactCell;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public String getWebsiteChineseName() {
        return websiteChineseName;
    }

    public void setWebsiteChineseName(String websiteChineseName) {
    	this.websiteChineseName = websiteChineseName;
    }
    
    public String getWebsiteEnglishName() {
        return websiteEnglishName;
    }

    public String getWebsiteDisplayUrl() {
        return websiteDisplayUrl;
    }

	public void setWebsiteDisplayUrl(String websiteDisplayUrl) {
		this.websiteDisplayUrl = websiteDisplayUrl;
	}

	public String getActiveDate() {
        return activeDate;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public List<PfbxPersonalVo> getPfbxPersonalVos() {
        return pfbxPersonalVos;
    }

    public List<PfbxBankVo> getPfbxBankVos() {
        return pfbxBankVos;
    }

    public List<PfbxAllowUrlVO> getPfbxAllowUrlVOs() {
		return pfbxAllowUrlVOs;
	}

	public EnumPfbxCheckStatus[] getEnumCheckStatus() {
        return enumCheckStatus;
    }

    public void setCheckid(Integer checkid) {
        this.checkid = checkid;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getStrResult() {
        return strResult;
    }

    public InputStream getStreamResult() {
        return streamResult;
    }

    public List<PfbxCustomerInfoRefXType> getPfbxCustomerInfoRefXTypeList() {
        return pfbxCustomerInfoRefXTypeList;
    }

    public void setPfbxCustomerInfoRefXTypeList(
            List<PfbxCustomerInfoRefXType> pfbxCustomerInfoRefXTypeList) {
        this.pfbxCustomerInfoRefXTypeList = pfbxCustomerInfoRefXTypeList;
    }

    public void setPfbxAllowUrlService(IPfbxAllowUrlService pfbxAllowUrlService) {
		this.pfbxAllowUrlService = pfbxAllowUrlService;
	}

	public void setPfbxWebsiteCategoryService(IPfbxWebsiteCategoryService pfbxWebsiteCategoryService) {
		this.pfbxWebsiteCategoryService = pfbxWebsiteCategoryService;
	}

	public String getCategoryName() {
        return categoryName;
    }

    public String getMailDir() {
        return mailDir;
    }

    public String getMailServer() {
        return mailServer;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailFromAlias() {
        return mailFromAlias;
    }

    public String getAuditSuccessSubject() {
        return auditSuccessSubject;
    }

    public String getAuditRefuseSubject() {
        return auditRefuseSubject;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

	public String getRootDomain() {
		return rootDomain;
	}

	public void setRootDomain(String rootDomain) {
		this.rootDomain = rootDomain;
	}

	public String getPlayType() {
		return playType;
	}

	public void setPlayType(String playType) {
		this.playType = playType;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setUpdCategoryCode(String updCategoryCode) {
		this.updCategoryCode = updCategoryCode;
	}

	public void setUpdRootDomain(String updRootDomain) {
		this.updRootDomain = updRootDomain;
	}

	public void setUpdUrlStatus(String updUrlStatus) {
		this.updUrlStatus = updUrlStatus;
	}

	public void setUpdRefuseNote(String updRefuseNote) {
		this.updRefuseNote = updRefuseNote;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public void setUpdAllowUrlId(String updAllowUrlId) {
		this.updAllowUrlId = updAllowUrlId;
	}
    
}
