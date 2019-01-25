package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.dao.template.AdmTemplateAdDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;
import com.pchome.akbadm.db.service.IBaseService;

public interface ITemplateAdService extends IBaseService<AdmTemplateAd, String>{

	public List<AdmTemplateAd> getAllTemplateAd() throws Exception;

	public List<AdmTemplateAd> getTemplateAdByCondition(String templateAdSeq, String templateAdName, String templateAdType, String templateAdWidth, String templateAdHeight) throws Exception;

	public AdmTemplateAd getTemplateAdById(String templateAdSeq) throws Exception;

	public AdmTemplateAd getTemplateAdBySeq(String templateAdSeq) throws Exception;

	public void insertAdmTemplateAd(AdmTemplateAd templatead) throws Exception;

	public void updateAdmTemplateAd(AdmTemplateAd templatead) throws Exception;

	public void deleteAdmTemplateAd(String templateAdSeq) throws Exception;

	public void saveAdmTemplateAd(AdmTemplateAd admTemplateAd) throws Exception;
	
	public void setAdmTemplateAdDAO(AdmTemplateAdDAO admTemplateadDAO) throws Exception;
}
