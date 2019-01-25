package com.pchome.akbadm.struts2.action.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionService;
import com.pchome.akbadm.db.service.pfbx.PfbSizeService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class AdKdclDemoAction extends BaseCookieAction {

    private IPfbxCustomerInfoService pfbxCustomerInfoService;
    private IPfbxPositionService pfbxPositionService;
    private String pfbxCustomerId;
    private String pid;
    private String pName;
    private String padWidth;
    private String padHeight;
    private PfbxPosition pfbxPosition;
    private PfbSizeService pfbSizeService;
    private List<PfbxSize> pfbSizeList;
    private String jsSource;
    
    private static final long serialVersionUID = 1L;

    private String result;
    private List<PfbxCustomerInfo> pfbxCustomerInfoList = null;
    private List<PfbxPosition> pfbxPositionList = new ArrayList<PfbxPosition>();

    public String execute() {
	pfbxCustomerInfoList = pfbxCustomerInfoService.getDemoPfbxCustomerInfo();
	return SUCCESS;
    }
    
    public String getPositionInfo() {
	if(StringUtils.isBlank(pfbxCustomerId)){
	    result = "error";
	    return SUCCESS;
	}

	String startStr ="{\"data\":[";
	String content = "";
	List<PfbxPosition> pfbxPositionList  = pfbxPositionService.selectPfbxPositionByDeleteFlag(0);
	List<PfbxPosition> dataList = new ArrayList<PfbxPosition>();
	for (int i = 0; i <= pfbxPositionList.size()-1; i++) {
	    if(pfbxPositionList.get(i).getPfbxCustomerInfo().getCustomerInfoId().equals(pfbxCustomerId) ){
		dataList.add(pfbxPositionList.get(i));
	    }
	}
	for (int i = 0; i <= dataList.size()-1; i++) {
	    if (i == dataList.size() - 1) {
		content = content + "{\"pId\":"
			+ "\""+dataList.get(i).getPId()+"\"," +"\"pName\":"
			+ "\""+dataList.get(i).getPName() + "\"}]}";
	    } else {
		content = content + "{\"pId\":"
			+ "\""+dataList.get(i).getPId()+"\"," + "\"pName\":"
			+ "\""+dataList.get(i).getPName() + "\"},";
	    }
	}
	content = startStr + content ;
	result = content;
	return SUCCESS;
    }
    
    public String getKdclAd(){
	pfbxCustomerInfoList = pfbxCustomerInfoService.getDemoPfbxCustomerInfo();
	List<PfbxPosition> pfbxPositionList  = pfbxPositionService.selectPfbxPositionByDeleteFlag(0);
	for (int i = 0; i <= pfbxPositionList.size()-1; i++) {
	    if(pfbxPositionList.get(i).getPfbxCustomerInfo().getCustomerInfoId().equals(pfbxCustomerId) ){
		this.pfbxPositionList.add(pfbxPositionList.get(i));
	    }
	}
	this.pfbxPosition = pfbxPositionService.get(pid);
	String height="";
	String weight="";
	pfbSizeList = pfbSizeService.loadAll();
	for (PfbxSize pfbxSize : pfbSizeList) {
	    if(String.valueOf(pfbxSize.getId()).equals(String.valueOf(this.pfbxPosition.getSId()))){
		height = String.valueOf(pfbxSize.getHeight());
		weight = String.valueOf(pfbxSize.getWidth());
	    }
	}
	
	if(StringUtils.isBlank(padHeight) || StringUtils.isBlank(padWidth)){
	    padHeight = height;
	    padWidth = weight;
	}
	return SUCCESS;
    }


    public PfbxPosition getPfbxPosition() {
        return pfbxPosition;
    }

    public void setPfbxPosition(PfbxPosition pfbxPosition) {
        this.pfbxPosition = pfbxPosition;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public IPfbxCustomerInfoService getPfbxCustomerInfoService() {
        return pfbxCustomerInfoService;
    }

    public void setPfbxCustomerInfoService(
    	IPfbxCustomerInfoService pfbxCustomerInfoService) {
        this.pfbxCustomerInfoService = pfbxCustomerInfoService;
    }

    public IPfbxPositionService getPfbxPositionService() {
        return pfbxPositionService;
    }

    public void setPfbxPositionService(IPfbxPositionService pfbxPositionService) {
        this.pfbxPositionService = pfbxPositionService;
    }

    public String getPfbxCustomerId() {
        return pfbxCustomerId;
    }

    public void setPfbxCustomerId(String pfbxCustomerId) {
        this.pfbxCustomerId = pfbxCustomerId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<PfbxCustomerInfo> getPfbxCustomerInfoList() {
        return pfbxCustomerInfoList;
    }

    public void setPfbxCustomerInfoList(List<PfbxCustomerInfo> pfbxCustomerInfoList) {
        this.pfbxCustomerInfoList = pfbxCustomerInfoList;
    }

    public List<PfbxPosition> getPfbxPositionList() {
        return pfbxPositionList;
    }

    public void setPfbxPositionList(List<PfbxPosition> pfbxPositionList) {
        this.pfbxPositionList = pfbxPositionList;
    }

    public String getPadWidth() {
        return padWidth;
    }

    public void setPadWidth(String padWidth) {
        this.padWidth = padWidth;
    }

    public String getPadHeight() {
        return padHeight;
    }

    public void setPadHeight(String padHeight) {
        this.padHeight = padHeight;
    }

    public PfbSizeService getPfbSizeService() {
        return pfbSizeService;
    }

    public void setPfbSizeService(PfbSizeService pfbSizeService) {
        this.pfbSizeService = pfbSizeService;
    }

    public List<PfbxSize> getPfbSizeList() {
        return pfbSizeList;
    }

    public void setPfbSizeList(List<PfbxSize> pfbSizeList) {
        this.pfbSizeList = pfbSizeList;
    }

    public String getJsSource() {
        return jsSource;
    }

    public void setJsSource(String jsSource) {
        this.jsSource = jsSource;
    }

}
