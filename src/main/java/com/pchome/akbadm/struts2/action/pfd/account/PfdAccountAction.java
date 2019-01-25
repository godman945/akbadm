package com.pchome.akbadm.struts2.action.pfd.account;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.pfd.user.IPfdUserService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.enumerate.pfd.EnumPfdUserStatus;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.soft.depot.utils.RSAUtils;
import com.pchome.soft.util.DateValueUtil;
import com.pchome.soft.util.SpringEmailUtil;

public class PfdAccountAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfdAccountService pfdAccountService;
	private IPfdUserService pfdUserService;
	private ISequenceService sequenceService;
	private IAdmAccesslogService admAccesslogService;
	private String akbPfdServer;
	private String mailDir;
	private String mailServer;
	private String mailFrom;
	private String mailSubject;

	private String targetId;

	//輸入欄位
	private String customerInfoId;
	private String customerInfoStatus;
	private String companyName;
	private String companyTaxId;
	private String companyOwner;
	private String companyTel;
	private String companyFax;
	private String companyZip;
	private String companyAddr;
	private String companyWebsite;
	private String note;
	private String contactPerson;
	private String contactTel;
	private String contactMobile;
	private String contactEmail;
	private String financialOfficer;
	private String financialTel;
	private String financialMobile;
	private String financialEmail;
	private String totalQuota; //總額度
	private String companyCategory;
	private String pfpAdtypeSelect;

	private String message = "";
	
	private String emailTitle = "PChome廣告刊登-經銷商服務";

	private List<PfdCustomerInfo> accountList = new ArrayList<PfdCustomerInfo>();
	private List<PfdUser> userList = new ArrayList<PfdUser>();

	private Map<String, String> accountStatusMap = null;
	private Map<String, String> userStatusMap = null;

	public String execute() throws Exception {

		this.init();

		this.accountList = pfdAccountService.loadAll();

		return SUCCESS;
	}

	public String goAddPage() throws Exception {
		return SUCCESS;
	}

	public String doAdd() throws Exception {

		log.info(">>> companyName = " + companyName);
		log.info(">>> companyTaxId = " + companyTaxId);
		log.info(">>> companyOwner = " + companyOwner);
		log.info(">>> companyTel = " + companyTel);
		log.info(">>> companyFax = " + companyFax);
		log.info(">>> companyZip = " + companyZip);
		log.info(">>> companyAddr = " + companyAddr);
		log.info(">>> companyWebsite = " + companyWebsite);
		log.info(">>> note = " + note);
		log.info(">>> contactPerson = " + contactPerson);
		log.info(">>> contactTel = " + contactTel);
		log.info(">>> contactMobile = " + contactMobile);
		log.info(">>> contactEmail = " + contactEmail);
		log.info(">>> financialOfficer = " + financialOfficer);
		log.info(">>> financialTel = " + financialTel);
		log.info(">>> financialMobile = " + financialMobile);
		log.info(">>> financialEmail = " + financialEmail);
		log.info(">>> totalQuota = " + totalQuota);
		log.info(">>> companyCategory = " + companyCategory);
		log.info(">>> pfpAdtypeSelect = " + pfpAdtypeSelect);
		
		String emailCheck = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
		String urlCheck = "^(http://)?[a-zA-Z0-9-]+(\\.[a-zA-z0-9-]+)+/?$";

		if (companyName==null || companyName.trim().equals("")) {
			message = "請輸入公司名稱！";
			return INPUT;
		} else {
			companyName = companyName.trim();
		}

		if (companyTaxId==null || companyTaxId.trim().equals("")) {
			message = "請輸入統一編號！";
			return INPUT;
		} else {
			companyTaxId = companyTaxId.trim();
		}

		//檢查公司統編是否被使用過
		Map<String, String> checkTaxIdMap = new HashMap<String, String>();
		checkTaxIdMap.put("companyTaxId", companyTaxId);
		List<PfdCustomerInfo> checkList = pfdAccountService.getPfdCustomerInfoByCondition(checkTaxIdMap);
		
		if(!checkList.isEmpty()){
			message = "統一編號已被註冊，請更換！";
			return INPUT;
		}
		
		if (companyOwner==null || companyOwner.trim().equals("")) {
			message = "請輸入公司負責人！";
			return INPUT;
		} else {
			companyOwner = companyOwner.trim();
		}

		if (companyTel==null || companyTel.trim().equals("")) {
			message = "請輸入公司電話！";
			return INPUT;
		} else {
			companyTel = companyTel.trim();
		}

		if (companyFax==null || companyFax.trim().equals("")) {
			message = "請輸入公司傳真電話！";
			return INPUT;
		} else {
			companyFax = companyFax.trim();
		}
		
		if (companyZip==null || companyZip.trim().equals("")) {
			message = "請輸入公司郵遞區號！";
			return INPUT;
		} else {
			companyZip = companyZip.trim();
		}

		if (companyAddr==null || companyAddr.trim().equals("")) {
			message = "請輸入公司地址！";
			return INPUT;
		} else {
			companyAddr = companyAddr.trim();
		}

		if (companyWebsite==null || companyWebsite.trim().equals("")) {
			message = "請輸入公司網址！";
			return INPUT;
		} else {
			companyWebsite = companyWebsite.trim();
		}
		
		String checkUrl = companyWebsite;
		checkUrl = checkUrl.replaceAll("http://", "");
		checkUrl = checkUrl.replaceAll("https://", "");
		checkUrl = "http://" + checkUrl;
		if(!checkUrl.matches(urlCheck)){
			message = "公司網址格式錯誤！";
			return INPUT;
		}
		
		if (contactPerson==null || contactPerson.trim().equals("")) {
			message = "請輸入經銷商聯絡人！";
			return INPUT;
		} else {
			contactPerson = contactPerson.trim();
		}

		if (contactTel==null || contactTel.trim().equals("")) {
			message = "請輸入經銷商聯絡人電話！";
			return INPUT;
		} else {
			contactTel = contactTel.trim();
		}

		if (contactMobile==null || contactMobile.trim().equals("")) {
			message = "請輸入經銷商聯絡人行動電話！";
			return INPUT;
		} else {
			contactMobile = contactMobile.trim();
		}
		
		if (contactEmail==null || contactEmail.trim().equals("")) {
			message = "請輸入經銷商聯絡人Email！";
			return INPUT;
		} else {
			contactEmail = contactEmail.trim();
		}
		
		//檢查經銷商聯絡人 email 格式是否正確
		if(!contactEmail.matches(emailCheck)){
			message = "經銷商聯絡人Email格式錯誤！";
			return INPUT;
		}
		
		//檢查經銷商聯絡人 email 是否可用
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("userEmail", contactEmail);
		conditionMap.put("status", "'" + EnumPfdUserStatus.START.getStatus() + "','" +
				EnumPfdUserStatus.CLOSE.getStatus() + "','" +
				EnumPfdUserStatus.STOP.getStatus() + "'");

		if (pfdUserService.getPfdUserByCondition(conditionMap).size() > 0) {
			message = "經銷商聯絡人Email已被註冊，請更換！";
			return INPUT;
		}
		
		if (financialOfficer==null || financialOfficer.trim().equals("")) {
			message = "請輸入財務聯絡人！";
			return INPUT;
		} else {
			financialOfficer = financialOfficer.trim();
		}

		if (financialTel==null || financialTel.trim().equals("")) {
			message = "請輸入財務聯絡人電話！";
			return INPUT;
		} else {
			financialTel = financialTel.trim();
		}

		if (financialMobile==null || financialMobile.trim().equals("")) {
			message = "請輸入財務聯絡人行動電話！";
			return INPUT;
		} else {
			financialMobile = financialMobile.trim();
		}
		
		if (financialEmail==null || financialEmail.trim().equals("")) {
			message = "請輸入財務聯絡人Email！";
			return INPUT;
		} else {
			financialEmail = financialEmail.trim();
		}
		
		//檢查經銷商聯絡人 email 格式是否正確
		if(!financialEmail.matches(emailCheck)){
			message = "財務聯絡人Email格式錯誤！";
			return INPUT;
		}
		
		if (totalQuota==null || totalQuota.trim().equals("")) {
			message = "請輸入可使用總額度！";
			return INPUT;
		} else {
			totalQuota = totalQuota.trim();
		}

		Date now = new Date();

		//step1. insert PfdCustomerInfo
		String customerInfoId = sequenceService.getSerialNumber(EnumSequenceTableName.PFD_ACCOUNT);
		log.info(">>> customerInfoId = " + customerInfoId);

		PfdCustomerInfo pfdCustomerInfo = new PfdCustomerInfo();
		pfdCustomerInfo.setCustomerInfoId(customerInfoId);
		pfdCustomerInfo.setStatus(EnumPfdAccountStatus.APPLY.getStatus());
		pfdCustomerInfo.setCompanyName(companyName);
		pfdCustomerInfo.setCompanyTaxId(companyTaxId);
		pfdCustomerInfo.setCompanyOwner(companyOwner);
		pfdCustomerInfo.setCompanyTel(companyTel);
		if (companyFax!=null && !companyFax.trim().equals("")) {
			pfdCustomerInfo.setCompanyFax(companyFax);
		}
		pfdCustomerInfo.setCompanyZip(companyZip);
		pfdCustomerInfo.setCompanyAddr(companyAddr);
		if (companyWebsite!=null && !companyWebsite.trim().equals("")) {
			pfdCustomerInfo.setCompanyWebsite(companyWebsite);
		}
		pfdCustomerInfo.setContactPerson(contactPerson);
		pfdCustomerInfo.setContactTel(contactTel);
		if (contactMobile!=null && !contactMobile.trim().equals("")) {
			pfdCustomerInfo.setContactMobile(contactMobile);
		}
		pfdCustomerInfo.setContactEmail(contactEmail);
		if (financialOfficer!=null && !financialOfficer.trim().equals("")) {
			pfdCustomerInfo.setFinancialOfficer(financialOfficer);
		}
		if (financialTel!=null && !financialTel.trim().equals("")) {
			pfdCustomerInfo.setFinancialTel(financialTel);
		}
		if (financialMobile!=null && !financialMobile.trim().equals("")) {
			pfdCustomerInfo.setFinancialMobile(financialMobile);
		}
		if (financialEmail!=null && !financialEmail.trim().equals("")) {
			pfdCustomerInfo.setFinancialEmail(financialEmail);
		}
		if (note!=null && !note.trim().equals("")) {
			pfdCustomerInfo.setNote(note);
		}
		pfdCustomerInfo.setPfpAdtypeSelect(pfpAdtypeSelect);
		pfdCustomerInfo.setTotalQuota(Integer.parseInt(totalQuota));
		pfdCustomerInfo.setRemainQuota(Integer.parseInt(totalQuota));
		pfdCustomerInfo.setCompanyCategory(companyCategory);
		pfdCustomerInfo.setCreateDate(now);
		pfdCustomerInfo.setUpdateDate(now);

		pfdAccountService.save(pfdCustomerInfo);

		//step2. insert PfdUser
		String userId = sequenceService.getSerialNumber(EnumSequenceTableName.PFD_USER);
		log.info(">>> userId = " + userId);

		PfdUser pfdUser = new PfdUser();
		pfdUser.setPfdCustomerInfo(pfdCustomerInfo);
		pfdUser.setUserId(userId);
		pfdUser.setUserName(contactPerson);
		pfdUser.setUserEmail(contactEmail);
		pfdUser.setPrivilegeId(EnumPfdPrivilege.ROOT_USER.getPrivilege());
		pfdUser.setStatus(EnumPfdUserStatus.APPLY.getStatus());
		pfdUser.setInviteDate(now);
		pfdUser.setCreateDate(now);
		pfdUser.setUpdateDate(now);

		pfdUserService.save(pfdUser);

		//step3. 發綁定通知信
		String inviteUrl = akbPfdServer + "verifyUser.html?key=" + URLEncoder.encode(RSAUtils.encode(userId + "," + contactEmail), "UTF-8");
		log.info(">>> inviteUrl = " + inviteUrl);

		String mailContent = "";

		File mailFile = new File(mailDir + "pfd_invite.html");

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
			
			mailContent = mailContent.replaceAll("--companyName--", companyName);
			mailContent = mailContent.replaceAll("--inviteUrl--", inviteUrl);
			mailContent = mailContent.replaceAll("--endDate--", endDate);

			EmailUtils.getInstance().setHost(mailServer);

	    	try {

	    		log.info(">>> start to send invite mail ");
	    		EmailUtils.getInstance().sendHtmlEmail(mailSubject, mailFrom, emailTitle, new String[]{contactEmail}, null, mailContent);
	    		log.info(">>> end of send invite mail ");

	    	} catch (Exception e) {
	            log.error(e.getMessage(), e);
	        }
    	} else {
    		log.error(">>> mailContent is empty");
    	}

		message = "新增成功！";

		return SUCCESS;
	}

	public String goUpdatePage() throws Exception {
		log.info(">>> targetId = " + targetId);

		this.init();

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("customerInfoId", targetId);

		PfdCustomerInfo pfdCustomerInfo = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap).get(0);

		this.customerInfoId = pfdCustomerInfo.getCustomerInfoId();
		this.customerInfoStatus = pfdCustomerInfo.getStatus();
		this.companyName = pfdCustomerInfo.getCompanyName();
		this.companyTaxId = pfdCustomerInfo.getCompanyTaxId();
		this.companyOwner = pfdCustomerInfo.getCompanyOwner();
		this.companyTel = pfdCustomerInfo.getCompanyTel();
		this.companyFax = pfdCustomerInfo.getCompanyFax();
		this.companyZip = pfdCustomerInfo.getCompanyZip();
		this.companyAddr = pfdCustomerInfo.getCompanyAddr();
		this.companyWebsite = pfdCustomerInfo.getCompanyWebsite();
		this.note = pfdCustomerInfo.getNote();
		this.contactPerson = pfdCustomerInfo.getContactPerson();
		this.contactTel = pfdCustomerInfo.getContactTel();
		this.contactMobile = pfdCustomerInfo.getContactMobile();
		this.contactEmail = pfdCustomerInfo.getContactEmail();
		this.financialOfficer = pfdCustomerInfo.getFinancialOfficer();
		this.financialTel = pfdCustomerInfo.getFinancialTel();
		this.financialMobile = pfdCustomerInfo.getFinancialMobile();
		this.financialEmail = pfdCustomerInfo.getFinancialEmail();
		this.totalQuota = "" + pfdCustomerInfo.getTotalQuota();
		this.companyCategory = pfdCustomerInfo.getCompanyCategory();
		this.pfpAdtypeSelect = pfdCustomerInfo.getPfpAdtypeSelect();

		this.userList = new ArrayList<PfdUser>(pfdCustomerInfo.getPfdUsers());

		return SUCCESS;
	}

	public String doUpdate() throws Exception {

		log.info(">>> customerInfoId = " + customerInfoId);
		log.info(">>> companyName = " + companyName);
		log.info(">>> companyTaxId = " + companyTaxId);
		log.info(">>> companyOwner = " + companyOwner);
		log.info(">>> companyTel = " + companyTel);
		log.info(">>> companyFax = " + companyFax);
		log.info(">>> companyZip = " + companyZip);
		log.info(">>> companyAddr = " + companyAddr);
		log.info(">>> companyWebsite = " + companyWebsite);
		log.info(">>> note = " + note);
		log.info(">>> contactPerson = " + contactPerson);
		log.info(">>> contactTel = " + contactTel);
		log.info(">>> contactMobile = " + contactMobile);
		log.info(">>> financialOfficer = " + financialOfficer);
		log.info(">>> financialTel = " + financialTel);
		log.info(">>> financialMobile = " + financialMobile);
		log.info(">>> financialEmail = " + financialEmail);
		log.info(">>> companyCategory = " + companyCategory);
		log.info(">>> pfpAdtypeSelect = " + pfpAdtypeSelect);

		String emailCheck = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
		String urlCheck = "^(http://)?[a-zA-Z0-9-]+(\\.[a-zA-z0-9-]+)+/?$";

		if (companyName==null || companyName.trim().equals("")) {
			message = "請輸入公司名稱！";
			return INPUT;
		} else {
			companyName = companyName.trim();
		}

		if (companyTaxId==null || companyTaxId.trim().equals("")) {
			message = "請輸入統一編號！";
			return INPUT;
		} else {
			companyTaxId = companyTaxId.trim();
		}

		//檢查公司統編是否被使用過
		Map<String, String> checkTaxIdMap = new HashMap<String, String>();
		checkTaxIdMap.put("companyTaxId", companyTaxId);
		List<PfdCustomerInfo> checkList = pfdAccountService.getPfdCustomerInfoByCondition(checkTaxIdMap);
		
		if(!checkList.isEmpty()){
			PfdCustomerInfo pfdCustomerInfo = checkList.get(0);
			if(!StringUtils.equals(pfdCustomerInfo.getCustomerInfoId(), customerInfoId)){
				message = "統一編號已被使用，請更換！";
				return INPUT;
			}
		}
		
		if (companyOwner==null || companyOwner.trim().equals("")) {
			message = "請輸入公司負責人！";
			return INPUT;
		} else {
			companyOwner = companyOwner.trim();
		}

		if (companyTel==null || companyTel.trim().equals("")) {
			message = "請輸入公司電話！";
			return INPUT;
		} else {
			companyTel = companyTel.trim();
		}

		if (companyFax==null || companyFax.trim().equals("")) {
			message = "請輸入公司傳真電話！";
			return INPUT;
		} else {
			companyFax = companyFax.trim();
		}
		
		if (companyZip==null || companyZip.trim().equals("")) {
			message = "請輸入公司郵遞區號！";
			return INPUT;
		} else {
			companyZip = companyZip.trim();
		}

		if (companyAddr==null || companyAddr.trim().equals("")) {
			message = "請輸入公司地址！";
			return INPUT;
		} else {
			companyAddr = companyAddr.trim();
		}

		if (companyWebsite==null || companyWebsite.trim().equals("")) {
			message = "請輸入公司網址！";
			return INPUT;
		} else {
			companyWebsite = companyWebsite.trim();
		}
		
		String checkUrl = companyWebsite;
		checkUrl = checkUrl.replaceAll("http://", "");
		checkUrl = checkUrl.replaceAll("https://", "");
		checkUrl = "http://" + checkUrl;
		if(!checkUrl.matches(urlCheck)){
			message = "公司網址格式錯誤！";
			return INPUT;
		}
		
		if (contactPerson==null || contactPerson.trim().equals("")) {
			message = "請輸入經銷商聯絡人！";
			return INPUT;
		} else {
			contactPerson = contactPerson.trim();
		}

		if (contactTel==null || contactTel.trim().equals("")) {
			message = "請輸入經銷商聯絡人電話！";
			return INPUT;
		} else {
			contactTel = contactTel.trim();
		}

		if (contactMobile==null || contactMobile.trim().equals("")) {
			message = "請輸入經銷商聯絡人行動電話！";
			return INPUT;
		} else {
			contactMobile = contactMobile.trim();
		}
		
		if (contactEmail==null || contactEmail.trim().equals("")) {
			message = "請輸入經銷商聯絡人Email！";
			return INPUT;
		} else {
			contactEmail = contactEmail.trim();
		}
		
		//檢查經銷商聯絡人 email 格式是否正確
		if(!contactEmail.matches(emailCheck)){
			message = "經銷商聯絡人Email格式錯誤！";
			return INPUT;
		}
		
		if (financialOfficer==null || financialOfficer.trim().equals("")) {
			message = "請輸入財務聯絡人！";
			return INPUT;
		} else {
			financialOfficer = financialOfficer.trim();
		}

		if (financialTel==null || financialTel.trim().equals("")) {
			message = "請輸入財務聯絡人電話！";
			return INPUT;
		} else {
			financialTel = financialTel.trim();
		}

		if (financialMobile==null || financialMobile.trim().equals("")) {
			message = "請輸入財務聯絡人行動電話！";
			return INPUT;
		} else {
			financialMobile = financialMobile.trim();
		}
		
		if (financialEmail==null || financialEmail.trim().equals("")) {
			message = "請輸入財務聯絡人Email！";
			return INPUT;
		} else {
			financialEmail = financialEmail.trim();
		}
		
		//檢查經銷商聯絡人 email 格式是否正確
		if(!financialEmail.matches(emailCheck)){
			message = "財務聯絡人Email格式錯誤！";
			return INPUT;
		}

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("customerInfoId", customerInfoId);

		PfdCustomerInfo pfdCustomerInfo = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap).get(0);

		String oldStatus = pfdCustomerInfo.getStatus();
		
		Date now = new Date();

		pfdCustomerInfo.setStatus(customerInfoStatus);
		pfdCustomerInfo.setCompanyName(companyName);
		pfdCustomerInfo.setCompanyTaxId(companyTaxId);
		pfdCustomerInfo.setCompanyOwner(companyOwner);
		pfdCustomerInfo.setCompanyTel(companyTel);
		if (companyFax!=null && !companyFax.trim().equals("")) {
			pfdCustomerInfo.setCompanyFax(companyFax);
		}
		pfdCustomerInfo.setCompanyZip(companyZip);
		pfdCustomerInfo.setCompanyAddr(companyAddr);
		if (companyWebsite!=null && !companyWebsite.trim().equals("")) {
			pfdCustomerInfo.setCompanyWebsite(companyWebsite);
		}
		pfdCustomerInfo.setCompanyCategory(companyCategory);
		pfdCustomerInfo.setContactPerson(contactPerson);
		pfdCustomerInfo.setContactTel(contactTel);
		if (contactMobile!=null && !contactMobile.trim().equals("")) {
			pfdCustomerInfo.setContactMobile(contactMobile);
		}
		if (contactEmail!=null && !contactEmail.trim().equals("")) {
			pfdCustomerInfo.setContactEmail(contactEmail);
		}
		if (financialOfficer!=null && !financialOfficer.trim().equals("")) {
			pfdCustomerInfo.setFinancialOfficer(financialOfficer);
		}
		if (financialTel!=null && !financialTel.trim().equals("")) {
			pfdCustomerInfo.setFinancialTel(financialTel);
		}
		if (financialMobile!=null && !financialMobile.trim().equals("")) {
			pfdCustomerInfo.setFinancialMobile(financialMobile);
		}
		if (financialEmail!=null && !financialEmail.trim().equals("")) {
			pfdCustomerInfo.setFinancialEmail(financialEmail);
		}
		if (note!=null && !note.trim().equals("")) {
			pfdCustomerInfo.setNote(note);
		}
		pfdCustomerInfo.setPfpAdtypeSelect(pfpAdtypeSelect);
		pfdCustomerInfo.setUpdateDate(now);

		pfdAccountService.saveOrUpdate(pfdCustomerInfo);

		message = "修改成功！";

		//access log
		if(!StringUtils.equals(oldStatus, customerInfoStatus)){
			
			String oldStatusName = "";
			String statusName = "";
			for (EnumPfdAccountStatus e: EnumPfdAccountStatus.values()) {
				if(StringUtils.equals(oldStatus, e.getStatus())){
					oldStatusName = e.getChName();
				}
				
				if(StringUtils.equals(customerInfoStatus, e.getStatus())){
					statusName = e.getChName();
				}
			}
			
			String logMsg = "帳戶狀態--> " + oldStatusName + " 變更成 " + statusName;
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.PFD, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
					super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, pfdCustomerInfo.getCustomerInfoId(), 
					null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		}
		
		return SUCCESS;
	}

	/**
	 * 補寄認證信 
	 */
	public String reSend() throws Exception {
		log.info(">>> targetId = " + targetId);

		message = "發送失敗！";

		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("customerInfoId", targetId);

		PfdCustomerInfo pfdCustomerInfo = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap).get(0);

		Iterator<PfdUser> it = pfdCustomerInfo.getPfdUsers().iterator();
		String contactUserId = null;
		while (it.hasNext()) {
			PfdUser pfdUser = it.next();
			if (pfdUser.getPrivilegeId()==EnumPfdPrivilege.ROOT_USER.getPrivilege()) {
				contactUserId = pfdUser.getUserId();
				break;
			}
		}

		String inviteUrl = akbPfdServer + "verifyUser.html?key=" + URLEncoder.encode(RSAUtils.encode(contactUserId + "," + pfdCustomerInfo.getContactEmail()), "UTF-8");
		log.info(">>> inviteUrl = " + inviteUrl);

		String mailContent = "";

		File mailFile = new File(mailDir + "pfd_invite.html");

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

	    	try {

	    		String endDate = DateValueUtil.getInstance().getDateValue(7, DateValueUtil.DBPATH);
	    		log.info(">>> endDate = " + endDate);
			
	    		mailContent = mailContent.replaceAll("--companyName--", pfdCustomerInfo.getCompanyName());
	    		mailContent = mailContent.replaceAll("--inviteUrl--", inviteUrl);
	    		mailContent = mailContent.replaceAll("--endDate--", endDate);

	    		log.info(">>> mailServer = " + mailServer);
	    		EmailUtils.getInstance().setHost(mailServer);
	    		//SpringEmailUtil.getInstance().setHost(mailServer);

	    		log.info(">>> start to send invite mail ");
	    		EmailUtils.getInstance().sendHtmlEmail(mailSubject, mailFrom, emailTitle, new String[]{pfdCustomerInfo.getContactEmail()}, null, mailContent);
	    		//SpringEmailUtil.getInstance().sendHtmlEmail(mailSubject, mailFrom, new String[]{pfdCustomerInfo.getContactEmail()}, null, mailContent);
	    		log.info(">>> end of send invite mail ");

	    		message = "已重新發送！";

	    	} catch (Exception e) {
	            log.error(e.getMessage(), e);
	        }
    	} else {
    		log.error(">>> mailContent is empty");
    	}

    	return SUCCESS;
	}

	private void init() {

		accountStatusMap = new HashMap<String, String>();
		for (EnumPfdAccountStatus e: EnumPfdAccountStatus.values()) {
			accountStatusMap.put(e.getStatus(), e.getChName());
		}

		userStatusMap = new HashMap<String, String>();
		for (EnumPfdUserStatus e: EnumPfdUserStatus.values()) {
			userStatusMap.put(e.getStatus(), e.getChName());
		}
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyTaxId() {
		return companyTaxId;
	}

	public void setCompanyTaxId(String companyTaxId) {
		this.companyTaxId = companyTaxId;
	}

	public String getCompanyOwner() {
		return companyOwner;
	}

	public void setCompanyOwner(String companyOwner) {
		this.companyOwner = companyOwner;
	}

	public String getCompanyTel() {
		return companyTel;
	}

	public void setCompanyTel(String companyTel) {
		this.companyTel = companyTel;
	}

	public String getCompanyFax() {
		return companyFax;
	}

	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}

	public String getCompanyZip() {
		return companyZip;
	}

	public void setCompanyZip(String companyZip) {
		this.companyZip = companyZip;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getCompanyWebsite() {
		return companyWebsite;
	}

	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getFinancialOfficer() {
		return financialOfficer;
	}

	public void setFinancialOfficer(String financialOfficer) {
		this.financialOfficer = financialOfficer;
	}

	public String getFinancialTel() {
		return financialTel;
	}

	public void setFinancialTel(String financialTel) {
		this.financialTel = financialTel;
	}

	public String getFinancialMobile() {
		return financialMobile;
	}

	public void setFinancialMobile(String financialMobile) {
		this.financialMobile = financialMobile;
	}

	public String getFinancialEmail() {
		return financialEmail;
	}

	public void setFinancialEmail(String financialEmail) {
		this.financialEmail = financialEmail;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setPfdUserService(IPfdUserService pfdUserService) {
		this.pfdUserService = pfdUserService;
	}

	public void setMailDir(String mailDir) {
		this.mailDir = mailDir;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public void setAkbPfdServer(String akbPfdServer) {
		this.akbPfdServer = akbPfdServer;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public Map<String, String> getAccountStatusMap() {
		return accountStatusMap;
	}

	public List<PfdCustomerInfo> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<PfdCustomerInfo> accountList) {
		this.accountList = accountList;
	}

	public Map<String, String> getUserStatusMap() {
		return userStatusMap;
	}

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getCustomerInfoStatus() {
		return customerInfoStatus;
	}

	public void setCustomerInfoStatus(String customerInfoStatus) {
		this.customerInfoStatus = customerInfoStatus;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getTotalQuota() {
		return totalQuota;
	}

	public void setTotalQuota(String totalQuota) {
		this.totalQuota = totalQuota;
	}

	public List<PfdUser> getUserList() {
		return userList;
	}

	public void setUserList(List<PfdUser> userList) {
		this.userList = userList;
	}

	public String getCompanyCategory() {
		return companyCategory;
	}

	public void setCompanyCategory(String companyCategory) {
		this.companyCategory = companyCategory;
	}

	public String getPfpAdtypeSelect() {
		return pfpAdtypeSelect;
	}

	public void setPfpAdtypeSelect(String pfpAdtypeSelect) {
		this.pfpAdtypeSelect = pfpAdtypeSelect;
	}
	
}
