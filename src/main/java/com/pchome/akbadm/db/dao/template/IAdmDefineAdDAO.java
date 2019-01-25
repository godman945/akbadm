package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmDefineAd;

public interface IAdmDefineAdDAO extends IBaseDAO<AdmDefineAd, String> {

    public List<AdmDefineAd> getDefineAdByCondition(String defineAdSeq, String defineAdName, String adPoolSeq) throws Exception;

	public AdmDefineAd getDefineAdById(String defineAdId) throws Exception;

	public AdmDefineAd getDefineAdBySeq(String defineAdSeq) throws Exception;

	public AdmDefineAd getDefineAdByPoolSeq(String adPoolSeq) throws Exception;

    public int deleteDefineAdByPoolSeq(String adPoolSeq) throws Exception;
}
