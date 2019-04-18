package com.pchome.rmi.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.AdmManagerDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.manager.IAdmManagerDetailService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdActionReportService;
import com.pchome.akbadm.db.service.report.quartzs.PfdAdActionReportService;
import com.pchome.enumerate.manager.EnumChannelCategory;
import com.pchome.enumerate.manager.EnumManagerPrivilege;
import com.pchome.soft.util.DateValueUtil;

public class PfdProviderImp implements IPfdProvider{

	private Logger log = LogManager.getRootLogger();
	
	private IAdmManagerDetailService admManagerDetailService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private IPfdAdActionReportService pfdAdActionReportService;
	
	
	
	private Date startDate;
	private Date endDate;
	private AdmManagerDetail managerDetail;
	
	private String[] portalIp;
	
	public List<PfdAccountVO> findPfdAccount(String memberId, String ip){
		
		this.dateRange();
		
		List<PfdAccountVO> vos = null;
		
		if(this.isManager(memberId,ip)){
			
			vos = new ArrayList<PfdAccountVO>();
			
			for(EnumManagerPrivilege manager:EnumManagerPrivilege.values()){
				
				if(manager.getPrivilege().equals(this.managerDetail.getManagerPrivilege())){
					
					
					if(manager.isSearchAll()){
						vos = pfdCustomerInfoService.findManagerPfdAccount(null, startDate, endDate);
					}else{
						vos = pfdCustomerInfoService.findManagerPfdAccount(memberId, startDate, endDate);
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
		
		this.managerDetail = admManagerDetailService.findLoginAdmManagerDetail(memberId, EnumChannelCategory.PFD.getCategory());
		
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
	
	public Map<String,String> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate) throws Exception{
		Map<String,String> oneWeekAdCost = new LinkedHashMap<String,String>();
		oneWeekAdCost = pfdAdActionReportService.findPfpAdClickByPfd(pfdCustomerInfoId,startDate, endDate);
		
		return oneWeekAdCost;
	}
	

	public void setAdmManagerDetailService(
			IAdmManagerDetailService admManagerDetailService) {
		this.admManagerDetailService = admManagerDetailService;
	}


	public void setPfdCustomerInfoService(
			IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setPortalIp(String[] portalIp) {
		this.portalIp = portalIp;
	}

	public void setPfdAdActionReportService(IPfdAdActionReportService pfdAdActionReportService) {
		this.pfdAdActionReportService = pfdAdActionReportService;
	}
	
}
