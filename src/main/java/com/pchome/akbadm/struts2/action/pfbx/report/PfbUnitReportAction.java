package com.pchome.akbadm.struts2.action.pfbx.report;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.report.IPfbxUnitReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PfbUnitReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private IPfbxUnitReportService pfbxUnitReportService;

    //輸入參數
	private String startDate;
	private String endDate;
	private String searchOption;
	private String searchText;
	private String unit;
	
	private PfbxCustomerReportVo pfbxCustomerReportVo;
	
	private List<PfbxCustomerInfo> list = new ArrayList<PfbxCustomerInfo>();
	private List<PfbxReportVO> voList = new ArrayList<PfbxReportVO>();
	//訊息
	private String message = "";

    public String execute() throws Exception {
    	
    	log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchText = " + searchText);
    	
    	Map<String, String> conditionMap = new HashMap<String, String>();
    	Map<String, String> conditionMap1 = new HashMap<String, String>();
    	
    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			list = new ArrayList<PfbxCustomerInfo>();
			conditionMap = new HashMap<String, String>();
			conditionMap1 = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);
	    	if(StringUtils.isNotBlank(searchOption)){
	    		if (StringUtils.isNotBlank(searchText)){
	    			log.info("searchOption: "+searchOption);
	    			log.info("searchText: "+searchText);
	    			if(searchOption.equals("unit")){
	    				conditionMap1.put(searchOption, searchText);
	    			}else{
	    				
	    				conditionMap.put(searchOption, searchText);
	    			}
	    		}
	    		
	    		list = pfbxCustomerInfoService.getPfbxCustomerInfoByCondition(conditionMap);
	    		log.info("list: "+list.size());
	    		for(PfbxCustomerInfo custInfo: list){
	    			log.info("PfbxCustomerInfoId: "+custInfo.getCustomerInfoId());
	    			log.info("websiteChineseName: "+custInfo.getWebsiteChineseName());
//	    			log.info("websiteDisplayUrl: "+custInfo.getWebsiteDisplayUrl());
	    			
	    			conditionMap1.put("PfbxCustomerInfoId", custInfo.getCustomerInfoId());
	    			conditionMap1.put("websiteChineseName", custInfo.getWebsiteChineseName());
//	    			conditionMap1.put("websiteDisplayUrl", custInfo.getWebsiteDisplayUrl());
	    		}
	    	}
	    	voList = new ArrayList<PfbxReportVO>();
	    	voList = pfbxUnitReportService.getPfbxUnitReportByCondition(conditionMap, conditionMap1);
	    	log.info("voList: "+voList.size());
	    	
	    	if(voList.size()==0){
	    		
	    		this.message = "查無資料！";
	    	}else{
	    		
		    	for (int i=0; i<voList.size(); i++) {
		    		PfbxReportVO vo = new PfbxReportVO();
	    			vo = voList.get(i);
	    			log.info("getAdPvSum: "+vo.getAdPvSum());
	    			
	    			PfbxCustomerInfo info = pfbxCustomerInfoService.get(vo.getCustomerInfoId());
	    			vo.setWebsiteChineseName(info.getWebsiteChineseName());
//	    			vo.setWebsiteDisplayUrl(info.getWebsiteDisplayUrl());
//	    			log.info("CustomerInfoId: "+vo.getCustomerInfoId());
//	    			log.info("chineseName: "+vo.getWebsiteChineseName());
//	    			log.info("url: "+vo.getWebsiteDisplayUrl());
	    			
				}
	    	}
    	
		}
		return SUCCESS;
	}

	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}


	public PfbxCustomerReportVo getPfbxCustomerReportVo() {
		return pfbxCustomerReportVo;
	}




	public void setPfbxCustomerReportVo(PfbxCustomerReportVo pfbxCustomerReportVo) {
		this.pfbxCustomerReportVo = pfbxCustomerReportVo;
	}




	public List<PfbxCustomerInfo> getList() {
		return list;
	}




	public void setList(List<PfbxCustomerInfo> list) {
		this.list = list;
	}


	public IPfbxCustomerInfoService getPfbxCustomerInfoService() {
		return pfbxCustomerInfoService;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}


	public IPfbxUnitReportService getPfbxUnitReportService() {
		return pfbxUnitReportService;
	}

	public void setPfbxUnitReportService(
			IPfbxUnitReportService pfbxUnitReportService) {
		this.pfbxUnitReportService = pfbxUnitReportService;
	}

	public List<PfbxReportVO> getVoList() {
		return voList;
	}

	public void setVoList(List<PfbxReportVO> voList) {
		this.voList = voList;
	}

}
