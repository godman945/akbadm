package com.pchome.akbadm.struts2.ajax.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.manager.IAdmManagerDetailService;
import com.pchome.akbadm.db.vo.manager.ManagerListVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class ManagerAjax extends BaseCookieAction{

	private IAdmManagerDetailService admManagerDetailService;
	
	private String sys;
	private String memberId;
	
	private List<ManagerListVO> vos; 
	private String exist;
	
	public String searchManagerAjax(){
		
		vos = admManagerDetailService.findAdmManagerDetails(sys);
		
		return SUCCESS;
	}

	public String checkManagerAjax() {
		
		//log.info(" memberId: "+memberId);
		//log.info(" sys: "+sys);
		
		if(StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(sys)){
			
			AdmManagerDetail manager = admManagerDetailService.findExistAdmManagerDetail(memberId, sys);
			
			if(manager != null){
				exist = "Y";
			}
			
		}
		
		return SUCCESS;
	}
	
	public void setSys(String sys) {
		this.sys = sys;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setAdmManagerDetailService(
			IAdmManagerDetailService admManagerDetailService) {
		this.admManagerDetailService = admManagerDetailService;
	}

	public List<ManagerListVO> getVos() {
		return vos;
	}

	public String getExist() {
		return exist;
	}

}
