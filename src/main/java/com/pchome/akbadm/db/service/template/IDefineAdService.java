package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmDefineAd;
import com.pchome.akbadm.db.service.IBaseService;

public interface IDefineAdService extends IBaseService<AdmDefineAd, String> {

    public List<AdmDefineAd> getDefineAdByCondition(String defineAdSeq, String defineAdName, String adPoolSeq) throws Exception;

	public AdmDefineAd getDefineAdById(String defineAdSeq) throws Exception;

	public AdmDefineAd getDefineAdBySeq(String defineAdSeq) throws Exception;

	public AdmDefineAd getDefineAdByPoolSeq(String adPoolSeq) throws Exception;

    public int deleteDefineAdByPoolSeq(String adPoolSeq) throws Exception;
}
