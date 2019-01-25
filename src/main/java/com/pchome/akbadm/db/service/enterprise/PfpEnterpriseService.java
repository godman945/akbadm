package com.pchome.akbadm.db.service.enterprise;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.enterprise.IPfpEnterpriseDAO;
import com.pchome.akbadm.db.pojo.PfpEnterprise;
import com.pchome.akbadm.db.service.BaseService;

public class PfpEnterpriseService extends BaseService<PfpEnterprise, String> implements IPfpEnterpriseService {

	public List<PfpEnterprise> findPfpEnterprise(Map<String, String> conditionsMap) throws Exception {
		return ((IPfpEnterpriseDAO) dao).findPfpEnterprise(conditionsMap);
	}

	public void deletePfpEnterprise(String taxId) throws Exception {
		((IPfpEnterpriseDAO) dao).deletePfpEnterprise(taxId);
	}
}
