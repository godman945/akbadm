package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxAllowUrl;

public interface IPfbxAllowUrlDAO extends IBaseDAO<PfbxAllowUrl, String> {
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus);

    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus, String playType);

    public List<PfbxAllowUrl> findPfbxAllowUrlByCondition(String pfbxCustomerInfoId) throws Exception;

	public List<PfbxAllowUrl> findDefaultUrl(String pfbxCustomerInfoId) throws Exception;

	public List<PfbxAllowUrl> getPfbxAllowUrlById(String id) throws Exception;

	public List<PfbxAllowUrl> findPfbxAllowUrlByRootDomain(String pfbxCustomerInfoId, String rootDomain, String id) throws Exception;
}
