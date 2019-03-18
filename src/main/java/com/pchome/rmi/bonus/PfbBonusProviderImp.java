package com.pchome.rmi.bonus;

import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetService;

public class PfbBonusProviderImp implements IPfbBonusProvider{

	private Logger log = LogManager.getRootLogger();
	
	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	
	private IPfbxBonusSetService pfbxBonusSetService;	

	
	
	
	public String downloadPfbBonusFileName(String payDate, String pfbId) {

		StringBuffer fileName = new StringBuffer();
		fileName.append(payDate.replace("/", "_"))
				.append("_").append(pfbId)
				.append("_").append("結帳單").append(".pdf"); 
		
		return fileName.toString();
	}
	
	public Map<String,Object> downloadPfbBonusMap(String pfbId, String strYear, String strMonth, String endYear, String endMonth, String payDate) {
		
		Map<String,Object> map = null;
		
//		PfbxBonusVo pfbxBonusVo = pfbxBonusService.findPfbxBonusVo(pfbId);	
//		
//		List<PfbxBonusBillVo> pfbxBonusBillVos = pfbxBonusTransService.findPfbxBonusBill(pfbId, strYear, strMonth, endYear, endMonth);
//		
//		if(pfbxBonusVo != null && !pfbxBonusBillVos.isEmpty()){
//			map = new HashMap<String,Object>();
//			
//			map.put("pfbxBonusVo", pfbxBonusVo);
//			map.put("pfbxBonusBillVos", pfbxBonusBillVos);			
//		}
		
		return map;
	}

	public IPfbxCustomerInfoService getPfbxCustomerInfoService() {
	    return pfbxCustomerInfoService;
	}

	public void setPfbxCustomerInfoService(
		IPfbxCustomerInfoService pfbxCustomerInfoService) {
	    this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public IPfbxBonusSetService getPfbxBonusSetService() {
	    return pfbxBonusSetService;
	}

	public void setPfbxBonusSetService(IPfbxBonusSetService pfbxBonusSetService) {
	    this.pfbxBonusSetService = pfbxBonusSetService;
	}

	
	
	
	
}
