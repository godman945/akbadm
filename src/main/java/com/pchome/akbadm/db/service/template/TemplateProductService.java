package com.pchome.akbadm.db.service.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.dao.template.AdmTemplateAdDAO;
import com.pchome.akbadm.db.dao.template.AdmTemplateProductDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.config.TestConfig;

public class TemplateProductService extends BaseService<AdmTemplateProduct, String> implements ITemplateProductService {

	private AdmTemplateProductDAO admTemplateproductDAO;
	
	//依據寬高過濾樣版
	public List<AdmTemplateProduct> getTemplateProductBySize(Map<String,String> condition) throws Exception {
		return admTemplateproductDAO.getTemplateProductBySize(condition);
	}
	
	

	public List<AdmTemplateProduct> getAllTemplateProduct() throws Exception {
		return admTemplateproductDAO.loadAll();
	}

	public List<AdmTemplateProduct> getTemplateProductByCondition(String templateProductSeq, String templateProductName, String templateProductType, String templateProductWidth, String templateProductHeight) throws Exception {
		return admTemplateproductDAO.getTemplateProductByCondition(templateProductSeq, templateProductName,templateProductType,templateProductWidth,templateProductHeight);
	}
	
	public AdmTemplateProduct getTemplateProductById(String templateProductId) throws Exception {
		return admTemplateproductDAO.getTemplateProductById(templateProductId);
	}
	
	public AdmTemplateProduct getTemplateProductBySeq(String templateProductSeq) throws Exception {
		return admTemplateproductDAO.getTemplateProductBySeq(templateProductSeq);
	}

	public void insertAdmTemplateProduct(AdmTemplateProduct admTemplateproduct) throws Exception {
		admTemplateproductDAO.insertTemplateProduct(admTemplateproduct);
	}
	@Transactional
	public void updateAdmTemplateProduct(AdmTemplateProduct admTemplateproduct) throws Exception {
		admTemplateproductDAO.updateTemplateProduct(admTemplateproduct);
	}

	public void deleteAdmTemplateProduct(String admTemplateProductSeq) throws Exception {
		String templateproductFilePath = "D://akbadm_file//" + admTemplateProductSeq + ".html";
		FileUtils.deleteQuietly(new File(templateproductFilePath));
		admTemplateproductDAO.deleteTemplateProduct(admTemplateProductSeq);
	}

	public void saveAdmTemplateProduct(AdmTemplateProduct admTemplateProduct) throws Exception {
		admTemplateproductDAO.saveTemplateProduct(admTemplateProduct);
	}

	public void setAdmTemplateProductDAO(AdmTemplateProductDAO admTemplateproductDAO) throws Exception {
		this.admTemplateproductDAO = admTemplateproductDAO;
	}
	
	public List<AdmDefineAd> getAllDefineAdBySeq(IRelateTproTadService relateTproTadService, AdmTemplateProduct admTemplateProduct) throws Exception{
		List<AdmDefineAd> DefineAds = new ArrayList<AdmDefineAd>();
		//IRelateTproTadService relateTproTadService = new RelateTproTadService();
		List<AdmRelateTproTad> rTproTads = relateTproTadService.getRelateTproTadByCondition(admTemplateProduct.getTemplateProductSeq(), null);
		int Dadcount = 0;
		System.out.println("DefineAd counts = " + rTproTads.size());
		for(AdmRelateTproTad rTproTdad : rTproTads) {
			System.out.println("rTproTdad.getAdmTemplateAd() = " + rTproTdad.getAdmTemplateAd());
			if(rTproTdad.getAdmTemplateAd() != null) {
				Set<AdmRelateTadDad> rTadDads = rTproTdad.getAdmTemplateAd().getAdmRelateTadDads();
				System.out.println("DefineAd counts = " + rTadDads.size());
				for(AdmRelateTadDad rTadDad : rTadDads) {
					DefineAds.add(rTadDad.getAdmDefineAd());
				}
			}
		}
		return DefineAds;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
