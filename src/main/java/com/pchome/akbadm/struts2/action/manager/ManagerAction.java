package com.pchome.akbadm.struts2.action.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmChannelAccount;
import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfbCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.manager.IAdmChannelAccountService;
import com.pchome.akbadm.db.service.manager.IAdmManagerDetailService;
import com.pchome.akbadm.db.vo.manager.ManagerPfbVO;
import com.pchome.akbadm.db.vo.manager.ManagerPfdVO;
import com.pchome.akbadm.db.vo.manager.ManagerPfpVO;
import com.pchome.akbadm.db.vo.manager.ManagerVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerPrivilege;
import com.pchome.enumerate.manager.EnumManagerStatus;

public class ManagerAction extends BaseCookieAction{

	private EnumChannelCategory[] enumChannelCategory = EnumChannelCategory.values();
	private EnumManagerPrivilege[] enumManagerPrivilege = EnumManagerPrivilege.values();
	private EnumManagerStatus[] enumManagerStatus = EnumManagerStatus.values();
	
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private IPfbCustomerInfoService pfbCustomerInfoService;
	private IAdmManagerDetailService admManagerDetailService;
	private IAdmChannelAccountService admChannelAccountService;
	
	private int managerId;
	private String system;
	private String name;
	private String memberId;
	private String privilege;
	private String status;
	private String[] pfdAccount;
	private String[] pfpAccount;
	private String[] pfbAccount;
	
	private ManagerVO managerVO;
	private List<ManagerPfdVO> managerPfdVOs;
	private List<ManagerPfpVO> managerPfpVOs;
	private List<ManagerPfbVO> managerPfbVOs;
	
	public String execute() {
		
		return SUCCESS;
	}

	public String addManagerAction(){
		
		return SUCCESS;
	}

	public String saveManagerAction(){
		
		log.info(" system: "+system);
		log.info(" name: "+name);
		log.info(" memberId: "+memberId);
		log.info(" privilege: "+privilege);
		log.info(" status: "+status);
		
		if(StringUtils.isNotBlank(system) && StringUtils.isNotBlank(name) &&
				StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(privilege) &&
				StringUtils.isNotBlank(status)){
			
			Date today = new Date();
			
			AdmManagerDetail manager = new AdmManagerDetail();
			
			manager.setManagerName(name);
			manager.setMemberId(memberId);
			manager.setManagerChannel(system);
			manager.setManagerPrivilege(privilege);
			manager.setManagerStatus(status);
			manager.setUpdateDate(today);
			manager.setCreateDate(today);
			
			admManagerDetailService.saveOrUpdate(manager);
					
		}
		
		return SUCCESS;
	}

	public String modifyManagerAction() {
		
		managerVO = admManagerDetailService.findAdmManagerDetail(managerId);
		
		if(EnumChannelCategory.PFD.getCategory().equals(managerVO.getSystemId())){
			managerPfdVOs = pfdCustomerInfoService.findManagerAccount(managerVO);
		}else if(EnumChannelCategory.PFP.getCategory().equals(managerVO.getSystemId())){
			managerPfpVOs = pfdUserAdAccountRefService.findManagerAccount(managerVO);
		}else if(EnumChannelCategory.PFB.getCategory().equals(managerVO.getSystemId())){
			managerPfbVOs = pfbCustomerInfoService.findManagerAccount(managerVO);
		}
		
		return SUCCESS;
	}
	
	public String updateManagerAction() {
		
		//log.info(" system: "+system);
		//log.info(" name: "+name);
		//log.info(" memberId: "+memberId);
		//log.info(" privilege: "+privilege);
		//log.info(" status: "+status);
		
		AdmManagerDetail manager = null;
		Date today = new Date();

		admChannelAccountService.deleteAdmChannelAccount(memberId, system);
		
		if(status.equals(EnumManagerStatus.DELETE.getStatus())){
			admManagerDetailService.deleteAdmManagerDetail(memberId, system);
		}
		else{
			manager = admManagerDetailService.findExistAdmManagerDetail(memberId, system);
		}
		
		log.info(" manager: "+manager);
		
		
		if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(privilege) &&
				StringUtils.isNotBlank(status) && manager != null){
			manager.setManagerName(name);
			manager.setManagerPrivilege(privilege);
			manager.setManagerStatus(status);
			manager.setUpdateDate(today);
			
			admManagerDetailService.saveOrUpdate(manager);
		}
		
		if(pfdAccount != null && manager != null){
			
			for(int i=0;i<pfdAccount.length;i++){
				
				AdmChannelAccount channelAccount = new AdmChannelAccount();			
				
				channelAccount.setAccountId(pfdAccount[i]);
				channelAccount.setChannelCategory(system);
				channelAccount.setMemberId(manager.getMemberId());
				channelAccount.setUpdateDate(today);
				channelAccount.setCreateDate(today);
				
				admChannelAccountService.saveOrUpdate(channelAccount);
			}
		}
		
		if(pfpAccount != null && manager != null){
			
			for(int i=0;i<pfpAccount.length;i++){
				
				AdmChannelAccount channelAccount = new AdmChannelAccount();

				channelAccount.setAccountId(pfpAccount[i]);
				channelAccount.setChannelCategory(system);
				channelAccount.setMemberId(manager.getMemberId());
				channelAccount.setUpdateDate(today);
				channelAccount.setCreateDate(today);
				
				admChannelAccountService.saveOrUpdate(channelAccount);
			}
		}
		
		if(pfbAccount != null && manager != null){
			
			for(int i=0;i<pfbAccount.length;i++){
				
				AdmChannelAccount channelAccount = new AdmChannelAccount();

				channelAccount.setAccountId(pfbAccount[i]);
				channelAccount.setChannelCategory(system);
				channelAccount.setMemberId(manager.getMemberId());
				channelAccount.setUpdateDate(today);
				channelAccount.setCreateDate(today);
				
				admChannelAccountService.saveOrUpdate(channelAccount);
			}
		}
		
		return SUCCESS;
	}
	
	public EnumChannelCategory[] getEnumChannelCategory() {
		return enumChannelCategory;
	}

	public EnumManagerPrivilege[] getEnumManagerPrivilege() {
		return enumManagerPrivilege;
	}

	public EnumManagerStatus[] getEnumManagerStatus() {
		return enumManagerStatus;
	}

	public void setPfdUserAdAccountRefService(
			IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfdCustomerInfoService(
			IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setPfbCustomerInfoService(
			IPfbCustomerInfoService pfbCustomerInfoService) {
		this.pfbCustomerInfoService = pfbCustomerInfoService;
	}

	public void setAdmChannelAccountService(
			IAdmChannelAccountService admChannelAccountService) {
		this.admChannelAccountService = admChannelAccountService;
	}

	public void setAdmManagerDetailService(
			IAdmManagerDetailService admManagerDetailService) {
		this.admManagerDetailService = admManagerDetailService;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setPfdAccount(String[] pfdAccount) {
		this.pfdAccount = pfdAccount;
	}

	public void setPfpAccount(String[] pfpAccount) {
		this.pfpAccount = pfpAccount;
	}

	public void setPfbAccount(String[] pfbAccount) {
		this.pfbAccount = pfbAccount;
	}

	public ManagerVO getManagerVO() {
		return managerVO;
	}

	public List<ManagerPfdVO> getManagerPfdVOs() {
		return managerPfdVOs;
	}

	public List<ManagerPfpVO> getManagerPfpVOs() {
		return managerPfpVOs;
	}

	public List<ManagerPfbVO> getManagerPfbVOs() {
		return managerPfbVOs;
	}
	
}
