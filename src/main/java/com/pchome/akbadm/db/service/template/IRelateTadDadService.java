package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.dao.template.AdmRelateTadDadDAO;
import com.pchome.akbadm.db.dao.template.AdmRelateTadDadVO;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;
import com.pchome.akbadm.db.service.IBaseService;

public interface IRelateTadDadService extends IBaseService<AdmRelateTadDad, String>{

	public List<AdmRelateTadDad> getAllRelateTadDad() throws Exception;

	public List<AdmRelateTadDad> getRelateTadDadByCondition(String templateAdSeq, String defineAdSeq) throws Exception;

	public AdmRelateTadDad getRelateTadDadById(String relateTadDadId) throws Exception;

	public AdmRelateTadDad getRelateTadDadBySeq(String relateTadDadSeq) throws Exception;

	public void insertAdmRelateTadDad(AdmRelateTadDad admRelateTadDad) throws Exception;

	public void saveAdmRelateTadDad(AdmRelateTadDadVO admRelateTadDadVO) throws Exception;

	public void updateAdmRelateTadDad(AdmRelateTadDad admRelateTadDad) throws Exception;

	public void deleteAdmRelateTadDad(String relateTadDadSeq) throws Exception;

	public void setAdmRelateTadDadDAO(AdmRelateTadDadDAO admRelateTadDadDAO) throws Exception;
}
