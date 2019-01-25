package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmTemplateAd;

public interface IAdmTemplateAdDAO extends IBaseDAO<AdmTemplateAd, String> {

	public List<AdmTemplateAd> getTemplateAdByCondition(String templateAdSeq, String templateAdName, String templateAdType, String templateAdWidth, String templateAdHeight) throws Exception;

	public void insertTemplateAd(AdmTemplateAd templatead) throws Exception;

	public void updateTemplateAd(AdmTemplateAd templatead) throws Exception;

	public AdmTemplateAd getTemplateAdById(String templateAdId) throws Exception;

}
