package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.dao.template.AdmRelateTproTadDAO;
import com.pchome.akbadm.db.dao.template.AdmRelateTproTadVO;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;
import com.pchome.akbadm.db.service.IBaseService;

public interface IRelateTproTadService extends IBaseService<AdmRelateTproTad, String>{

	public List<AdmRelateTproTad> getAllRelateTproTad() throws Exception;

	public List<AdmRelateTproTad> getRelateTproTadByCondition(String templateProductSeq, String templateAdSeq) throws Exception;

	public AdmRelateTproTad getRelateTproTadById(String relateTproTadId) throws Exception;

	public AdmRelateTproTad getRelateTproTadBySeq(String relateTproTadSeq) throws Exception;

	public void insertAdmRelateTproTad(AdmRelateTproTad admRelateTproTad) throws Exception;
	
	public void saveAdmRelateTproTad(AdmRelateTproTadVO admRelateTproTadVO) throws Exception;

	public void updateAdmRelateTproTad(AdmRelateTproTad admRelateTproTad) throws Exception;

	public void deleteAdmRelateTproTad(String relateTproTadSeq) throws Exception;
	
	public void setAdmRelateTproTadDAO(AdmRelateTproTadDAO admRelateTproTadDAO) throws Exception;

}
