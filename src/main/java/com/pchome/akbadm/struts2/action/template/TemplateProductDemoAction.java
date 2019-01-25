package com.pchome.akbadm.struts2.action.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.service.pfbx.PfbSizeService;
import com.pchome.akbadm.db.service.template.RelateTproTadService;
import com.pchome.akbadm.db.service.template.TemplateAdService;
import com.pchome.akbadm.db.service.template.TemplateProductService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdType;
public class TemplateProductDemoAction extends BaseCookieAction{

    private static final long serialVersionUID = 1L;
    private TemplateAdService templateAdService;
    private TemplateProductService templateProductService;
    private RelateTproTadService relateTproTadService;
    private PfbSizeService pfbSizeService;
    private List<PfbxSize> pfbxSizeList;
    private List<AdmTemplateProduct> templateProductList;
    private String size;
    private String templateAdType;
    private String result;
    private String adType;
    private String keyWordText;
    private List<AdmTemplateProduct> templateProductQueryList = new ArrayList<AdmTemplateProduct>();
    public String execute(){
	pfbxSizeList = pfbSizeService.loadAll();
	return SUCCESS;
    }
    public String templateProductFilter() throws Exception{
	log.info("size:"+size+" >>> adType:"+adType);
	if(StringUtils.isBlank(size) || StringUtils.isBlank(adType)){
	    pfbxSizeList = pfbSizeService.loadAll();
	    return SUCCESS;
	}
	this.templateProductList = templateProductService.getAllTemplateProduct();
	pfbxSizeList = pfbSizeService.loadAll();
	String height ="";
	String width ="";
	width = size.substring(0, size.indexOf("x")).trim();
	height = size.substring(size.indexOf("x")+1,size.length()).trim();
	
	if(Integer.parseInt(adType) == EnumAdType.AD_CHANNEL.getType()){
	    for (AdmTemplateProduct admTemplateProduct : templateProductList) {
		if(admTemplateProduct.getAdmTemplateAdType() == Integer.parseInt(templateAdType) && admTemplateProduct.getAdType() == EnumAdType.AD_CHANNEL.getType()){
		    if(admTemplateProduct.getTemplateProductWidth().equals(width.trim()) && admTemplateProduct.getTemplateProductHeight().equals(height.trim())){
			templateProductQueryList.add(admTemplateProduct);
		   }
		}
	    }
	}
	if(Integer.parseInt(adType) == EnumAdType.AD_SEARCH.getType()){
	    for (AdmTemplateProduct admTemplateProduct : templateProductList) {
		if(admTemplateProduct.getTemplateProductWidth().equals(width.trim()) && admTemplateProduct.getTemplateProductHeight().equals(height.trim())){
		    if(admTemplateProduct.getAdType() == EnumAdType.AD_SEARCH.getType()){
			templateProductQueryList.add(admTemplateProduct);
		    }
		}
	    }
	}
	return SUCCESS;
    }
    public TemplateProductService getTemplateProductService() {
        return templateProductService;
    }

    public void setTemplateProductService(
    	TemplateProductService templateProductService) {
        this.templateProductService = templateProductService;
    }

    public PfbSizeService getPfbSizeService() {
        return pfbSizeService;
    }

    public void setPfbSizeService(PfbSizeService pfbSizeService) {
        this.pfbSizeService = pfbSizeService;
    }

    public List<PfbxSize> getPfbxSizeList() {
        return pfbxSizeList;
    }

    public void setPfbxSizeList(List<PfbxSize> pfbxSizeList) {
        this.pfbxSizeList = pfbxSizeList;
    }
    public List<AdmTemplateProduct> getTemplateProductList() {
        return templateProductList;
    }
    public void setTemplateProductList(List<AdmTemplateProduct> templateProductList) {
        this.templateProductList = templateProductList;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
    public String getResult() {
        return result;
    }
    
    public String getTemplateAdType() {
        return templateAdType;
    }
    public void setTemplateAdType(String templateAdType) {
        this.templateAdType = templateAdType;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public List<AdmTemplateProduct> getTemplateProductQueryList() {
        return templateProductQueryList;
    }
    public void setTemplateProductQueryList(
    	List<AdmTemplateProduct> templateProductQueryList) {
        this.templateProductQueryList = templateProductQueryList;
    }
    public TemplateAdService getTemplateAdService() {
        return templateAdService;
    }
    public void setTemplateAdService(TemplateAdService templateAdService) {
        this.templateAdService = templateAdService;
    }
    public RelateTproTadService getRelateTproTadService() {
        return relateTproTadService;
    }
    public void setRelateTproTadService(RelateTproTadService relateTproTadService) {
        this.relateTproTadService = relateTproTadService;
    }
    public String getAdType() {
        return adType;
    }
    public void setAdType(String adType) {
        this.adType = adType;
    }
    public String getKeyWordText() {
        return keyWordText;
    }
    public void setKeyWordText(String keyWordText) {
        this.keyWordText = keyWordText;
    }
    
}
