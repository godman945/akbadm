package com.pchome.akbadm.db.dao.enterprise;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpEnterprise;

public interface IPfpEnterpriseDAO extends IBaseDAO<PfpEnterprise, String> {

	public List<PfpEnterprise> findPfpEnterprise(Map<String, String> conditionsMap) throws Exception;

	public void deletePfpEnterprise(String taxId) throws Exception;
}
