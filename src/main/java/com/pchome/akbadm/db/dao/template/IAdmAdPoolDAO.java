package com.pchome.akbadm.db.dao.template;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmAdPool;

public interface IAdmAdPoolDAO extends IBaseDAO<AdmAdPool, String> {

    public List<AdmAdPool> getAdPoolByCondition(String adPoolSeq, String aPoolName, String diffComapny) throws Exception;

	public AdmAdPool getAdPoolBySeq(String adPoolSeq) throws Exception;
}
