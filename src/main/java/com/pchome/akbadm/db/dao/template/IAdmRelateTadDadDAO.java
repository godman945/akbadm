package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmRelateTadDad;

public interface IAdmRelateTadDadDAO extends IBaseDAO<AdmRelateTadDad, String> {

	public List<AdmRelateTadDad> getRelateTadDadByCondition(String templateAdSeq, String defineAdSeq) throws Exception;

	public void insertRelateTadDad(AdmRelateTadDad relateTadDad) throws Exception;

	public void updateRelateTadDad(AdmRelateTadDad relateTadDad) throws Exception;

	public void deleteRelateTadDad(String relateTadDadSeq) throws Exception;

	public AdmRelateTadDad getRelateTadDadById(String relateTadDadId) throws Exception;

	public AdmRelateTadDad getRelateTadDadBySeq(String relateTadDadSeq) throws Exception;
}
