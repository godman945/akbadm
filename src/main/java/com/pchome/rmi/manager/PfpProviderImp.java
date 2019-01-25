package com.pchome.rmi.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.manager.IAdmManagerDetailService;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerPrivilege;
import com.pchome.soft.util.DateValueUtil;

public class PfpProviderImp implements IPfpProvider{

	private Log log = LogFactory.getLog(getClass().getName());
	 
	private IAdmManagerDetailService admManagerDetailService;
	private IPfpCustomerInfoService pfpCustomerInfoService;
	
	private Date startDate;
	private Date endDate;
	private AdmManagerDetail managerDetail;
	
	private String[] portalIp;
	
	public List<PfpAccountVO> findPfpAccount(String memberId, String ip) {
		
		this.dateRange();
		
		List<PfpAccountVO> vos = null;
		
		if(isManager(memberId,ip)){
			
			vos = new ArrayList<PfpAccountVO>();
			
			for(EnumManagerPrivilege manager:EnumManagerPrivilege.values()){
				
				if(manager.getPrivilege().equals(this.managerDetail.getManagerPrivilege())){
					
					if(manager.isSearchAll()){
						vos = pfpCustomerInfoService.findManagerPfpAccount(null, startDate, endDate);
					}else{
						vos = pfpCustomerInfoService.findManagerPfpAccount(memberId, startDate, endDate);
					}
					
					//log.info(" vos size: "+vos.size());
					
					break;
				}
			}
		}
		
		return vos;
	}

	public boolean isManager(String memberId, String ip) {
		
		boolean isManager = true;
		
		this.managerDetail = admManagerDetailService.findExistAdmManagerDetail(memberId, EnumChannelCategory.PFP.getCategory());
		
		if(this.managerDetail == null){
			isManager = false;
		}
		
		if(!isPChomeIP(ip)){
			isManager = false;
		}
		
		return isManager;
	}
	
	public boolean isPChomeIP(String ip){
		
		boolean isPChomeIP = false;
		
		for(String adm:portalIp){
			if(adm.equals(ip)){	
				log.info("login success ip: "+ip );
				isPChomeIP = true;
			}
		}
		
		return isPChomeIP;
	}
	
	private void dateRange(){
		
		// 取7天前資料
		String today = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
		startDate = DateValueUtil.getInstance().getDateForStartDateAddDay(today, -7);

		Calendar cal = Calendar.getInstance();
		cal.setTime(DateValueUtil.getInstance().stringToDate(today));
		// 昨天 date 23:59:59
		cal.add(Calendar.SECOND, -1);		
		endDate = cal.getTime();
	}
	
	public void setAdmManagerDetailService(
			IAdmManagerDetailService admManagerDetailService) {
		this.admManagerDetailService = admManagerDetailService;
	}

	public void setPfpCustomerInfoService(
			IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public void setPortalIp(String[] portalIp) {
		this.portalIp = portalIp;
	}
	
}
