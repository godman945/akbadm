package com.pchome.akbadm.db.service.privilege;

import java.util.List;

import com.pchome.akbadm.db.dao.privilege.PrivilegeModelVO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPrivilegeModelService extends IBaseService<AdmPrivilegeModel, String>{

	public List<AdmPrivilegeModel> getAllPrivilegeModel() throws Exception;

	public PrivilegeModelVO getPrivilegeModelById(String id) throws Exception;

	public void insertPrivilegeModel(PrivilegeModelVO privilegeModelVO) throws Exception;

	public List<AdmUser> deletePrivilegeModel(String modelId) throws Exception;

	public void updatePrivilegeModel(PrivilegeModelVO privilegeModelVO) throws Exception;

}
