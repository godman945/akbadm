package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmRelateTproTad;

public interface IAdmRelateTproTadDAO extends IBaseDAO<AdmRelateTproTad, String> {

	public List<AdmRelateTproTad> getRelateTproTadByCondition(String templateProductSeq, String templateAdSeq) throws Exception;

	public AdmRelateTproTad getRelateTproTadById(String relateTproTadId) throws Exception;

	public AdmRelateTproTad getRelateTproTadBySeq(String relateTproTadSeq) throws Exception;

	public void insertRelateTproTad(AdmRelateTproTad relateTproTad) throws Exception;

	public void updateRelateTproTad(AdmRelateTproTad relateTproTad) throws Exception;

	public void deleteRelateTproTad(String relateTproTadSeq) throws Exception;
	
	public void saveRelateTproTad(AdmRelateTproTadVO admRelateTproTadVO) throws Exception;
}
