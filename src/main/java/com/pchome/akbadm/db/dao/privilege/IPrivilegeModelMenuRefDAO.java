package com.pchome.akbadm.db.dao.privilege;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModelMenuRef;

public interface IPrivilegeModelMenuRefDAO extends IBaseDAO<AdmPrivilegeModelMenuRef, String> {

	public List<String> getMenuIdByPrivilegeModelId(String modelId) throws Exception;

	public void deletePrivilegeModelMenuRefById(String modelId) throws Exception;

	public void insertPrivilegeModelMenuRef(List<AdmPrivilegeModel> privilegeModelList) throws Exception;

}
