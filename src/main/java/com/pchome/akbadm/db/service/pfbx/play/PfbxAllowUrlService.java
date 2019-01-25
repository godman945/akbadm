package com.pchome.akbadm.db.service.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.play.IPfbxAllowUrlDAO;
import com.pchome.akbadm.db.pojo.PfbxAllowUrl;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAllowUrlService extends BaseService<PfbxAllowUrl, String> implements IPfbxAllowUrlService {
    @Override
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus) {
        return ((IPfbxAllowUrlDAO) dao).selectPfbxAllowUrl(deleteFlag, urlStatus);
    }

    @Override
    public List<PfbxAllowUrl> selectPfbxAllowUrl(String deleteFlag, String urlStatus, String playType) {
        return ((IPfbxAllowUrlDAO) dao).selectPfbxAllowUrl(deleteFlag, urlStatus, playType);
    }

    @Override
	public List<PfbxAllowUrl> findPfbxAllowUrlByCondition(String pfbxCustomerInfoId) throws Exception {
		return ((IPfbxAllowUrlDAO) dao).findPfbxAllowUrlByCondition(pfbxCustomerInfoId);
	}

	@Override
	public PfbxAllowUrl findDefaultUrl(String pfbxCustomerInfoId) throws Exception {

		List<PfbxAllowUrl> list = ((IPfbxAllowUrlDAO) dao).findDefaultUrl(pfbxCustomerInfoId);

		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Override
	public PfbxAllowUrl getPfbxAllowUrlById(String id) throws Exception {

		List<PfbxAllowUrl> list = ((IPfbxAllowUrlDAO) dao).getPfbxAllowUrlById(id);

		if(list.isEmpty()){
			return null;
		}else{
			return list.get(0);
		}
	}

	@Override
	public List<PfbxAllowUrl> findPfbxAllowUrlByRootDomain(String pfbxCustomerInfoId, String rootDomain, String id) throws Exception {
		return ((IPfbxAllowUrlDAO) dao).findPfbxAllowUrlByRootDomain(pfbxCustomerInfoId, rootDomain, id);
	}
}
