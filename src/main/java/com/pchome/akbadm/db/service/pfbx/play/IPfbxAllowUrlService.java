package com.pchome.akbadm.db.service.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAllowUrl;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxAllowUrlService extends IBaseService<PfbxAllowUrl, String> {
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus);

    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus, String playType);

    public List<PfbxAllowUrl> findPfbxAllowUrlByCondition(String pfbxCustomerInfoId) throws Exception;

	public PfbxAllowUrl findDefaultUrl(String pfbxCustomerInfoId) throws Exception;

	public PfbxAllowUrl getPfbxAllowUrlById(String id) throws Exception;

	public List<PfbxAllowUrl> findPfbxAllowUrlByRootDomain(String pfbxCustomerInfoId, String rootDomain, String id) throws Exception;
}
