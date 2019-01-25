package com.pchome.akbadm.db.service.enterprise;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpEnterprise;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpEnterpriseService extends IBaseService<PfpEnterprise, String> {

	public List<PfpEnterprise> findPfpEnterprise(Map<String, String> conditionsMap) throws Exception;

	public void deletePfpEnterprise(String taxId) throws Exception;
}
