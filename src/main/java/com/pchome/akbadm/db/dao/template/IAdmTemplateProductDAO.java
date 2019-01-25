package com.pchome.akbadm.db.dao.template;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;

public interface IAdmTemplateProductDAO extends IBaseDAO<AdmTemplateProduct, String> {

    	//依據寬高過濾樣版
	public List<AdmTemplateProduct> getTemplateProductBySize(Map<String,String> condition) throws Exception;
    	
	public List<AdmTemplateProduct> getTemplateProductByCondition(String templateProductSeq, String templateProductName, String templateProductType, String templateProductWidth, String templateProductHeight) throws Exception;

	public void insertTemplateProduct(AdmTemplateProduct templateproduct) throws Exception;

	public void updateTemplateProduct(AdmTemplateProduct templateproduct) throws Exception;

	public AdmTemplateProduct getTemplateProductById(String templateProductId) throws Exception;
	
	public String saveTemplateProduct(AdmTemplateProduct templateproduct) throws Exception;
}
