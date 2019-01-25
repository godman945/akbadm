package com.pchome.akbadm.db.dao.privilege;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;

public interface IPrivilegeModelDAO extends IBaseDAO<AdmPrivilegeModel, String> {

	public AdmPrivilegeModel findPrivilegeModelById(String id) throws Exception;

	public Integer savePrivilegeModel(AdmPrivilegeModel privilegeModel) throws Exception;

	public void deletePrivilegeModelById(String id) throws Exception;

}
