package com.pchome.akbadm.db.service.template;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmAdPool;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdPoolService extends IBaseService<AdmAdPool, String> {

    public List<AdmAdPool> getAdPoolByCondition(String adPoolSeq, String aPoolName, String diffComapny) throws Exception;

	public AdmAdPool getAdPoolBySeq(String adPoolSeq) throws Exception;
}
