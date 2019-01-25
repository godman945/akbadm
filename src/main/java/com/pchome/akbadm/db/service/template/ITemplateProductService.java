package com.pchome.akbadm.db.service.template;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.template.AdmTemplateProductDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.service.IBaseService;

public interface ITemplateProductService extends IBaseService<AdmTemplateProduct, String>{
    	
    	//依據寬高過濾樣版
  	public List<AdmTemplateProduct> getTemplateProductBySize(Map<String,String> condition) throws Exception;
    	
	public List<AdmTemplateProduct> getAllTemplateProduct() throws Exception;

	public List<AdmTemplateProduct> getTemplateProductByCondition(String templateProductSeq, String templateProductName, String templateProductType, String templateProductWidth, String templateProductHeight) throws Exception;

	public AdmTemplateProduct getTemplateProductById(String templateProductSeq) throws Exception;

	public AdmTemplateProduct getTemplateProductBySeq(String templateProductSeq) throws Exception;

	public void insertAdmTemplateProduct(AdmTemplateProduct templateproduct) throws Exception;

	public void updateAdmTemplateProduct(AdmTemplateProduct templateproduct) throws Exception;

	public void deleteAdmTemplateProduct(String templateProductSeq) throws Exception;
	
	public void saveAdmTemplateProduct(AdmTemplateProduct admTemplateProduct) throws Exception;
	
	public void setAdmTemplateProductDAO(AdmTemplateProductDAO admTemplateproductDAO) throws Exception;
	
	public List<AdmDefineAd> getAllDefineAdBySeq(IRelateTproTadService relateTproTadService, AdmTemplateProduct admTemplateProduct) throws Exception;
}
