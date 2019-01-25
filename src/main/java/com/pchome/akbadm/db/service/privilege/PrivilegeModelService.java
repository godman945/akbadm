package com.pchome.akbadm.db.service.privilege;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.dao.privilege.PrivilegeModelDAO;
import com.pchome.akbadm.db.dao.privilege.PrivilegeModelMenuRefDAO;
import com.pchome.akbadm.db.dao.privilege.PrivilegeModelVO;
import com.pchome.akbadm.db.dao.user.AdmUserDAO;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModel;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModelMenuRef;
import com.pchome.akbadm.db.pojo.AdmPrivilegeModelMenuRefId;
import com.pchome.akbadm.db.pojo.AdmUser;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.menu.MenuService;
import com.pchome.akbadm.db.service.privilege.IPrivilegeModelService;
import com.pchome.config.TestConfig;

public class PrivilegeModelService extends BaseService<AdmPrivilegeModel, String> implements IPrivilegeModelService {

	private PrivilegeModelDAO privilegeModelDAO;
	private PrivilegeModelMenuRefDAO privilegeModelMenuRefDAO;
	private AdmUserDAO admuserDAO;

	public List<AdmPrivilegeModel> getAllPrivilegeModel() throws Exception {
		return privilegeModelDAO.loadAll();
	}

	public PrivilegeModelVO getPrivilegeModelById(String id) throws Exception {
		PrivilegeModelVO privilegeModelVO = null;

		//撈出權限模組
		AdmPrivilegeModel privilegeModel = privilegeModelDAO.findPrivilegeModelById(id);

		if (privilegeModel!=null) {
			privilegeModelVO = new PrivilegeModelVO();
			privilegeModelVO.setModelId(privilegeModel.getModelId().toString());
			privilegeModelVO.setModelName(privilegeModel.getModelName());
			privilegeModelVO.setNote(privilegeModel.getNote());

			//撈出權限模組功能關聯
			List<String> menuIdList = privilegeModelMenuRefDAO.getMenuIdByPrivilegeModelId(privilegeModelVO.getModelId());
			privilegeModelVO.setDbMenuIdList(menuIdList);
		}

		return privilegeModelVO;
	}

	public void insertPrivilegeModel(PrivilegeModelVO privilegeModelVO) throws Exception {

		//新增權限模組
		AdmPrivilegeModel privilegeModel = new AdmPrivilegeModel();
		privilegeModel.setModelName(privilegeModelVO.getModelName());
		privilegeModel.setNote(privilegeModelVO.getNote());
		Date now = new Date();
		privilegeModel.setCreateDate(now);
		privilegeModel.setUpdateDate(now);
		Integer modelId = privilegeModelDAO.savePrivilegeModel(privilegeModel);

		//新增權限模組功能關聯
		if (modelId!=null) {
			String[] menuIdArray = privilegeModelVO.getNewMenuIds();
			for (int i=0; i<menuIdArray.length; i++) {
				AdmPrivilegeModelMenuRef objRef = new AdmPrivilegeModelMenuRef();
				AdmPrivilegeModelMenuRefId objRefId = new AdmPrivilegeModelMenuRefId();
				objRefId.setModelId(modelId);
				objRefId.setMenuId(Integer.parseInt(menuIdArray[i]));
				objRef.setId(objRefId);

				privilegeModelMenuRefDAO.save(objRef);
			}
		}
	}

	public List<AdmUser> deletePrivilegeModel(String modelId) throws Exception {

		//檢查是否有使用者正在使用此權限模組 -> 有人使用時不准刪
		List<AdmUser> userList = admuserDAO.getUserByPrivilegeModelId(modelId.toString());
		if (userList!=null && userList.size()>0) {
			return userList;
		}

		//刪除權限模組
		privilegeModelDAO.deletePrivilegeModelById(modelId);

		//刪除權限模組功能關聯
		privilegeModelMenuRefDAO.deletePrivilegeModelMenuRefById(modelId);

		return null;
	}

	public void updatePrivilegeModel(PrivilegeModelVO privilegeModelVO) throws Exception {

		//更新權限模組
		AdmPrivilegeModel privilegeModel = privilegeModelDAO.findPrivilegeModelById(privilegeModelVO.getModelId().toString());
		String modelId = privilegeModelVO.getModelId();
		privilegeModel.setModelName(privilegeModelVO.getModelName());
		privilegeModel.setNote(privilegeModelVO.getNote());
		Date now = new Date();
		privilegeModel.setUpdateDate(now);
		privilegeModelDAO.savePrivilegeModel(privilegeModel);

		//刪除舊權限模組功能關聯
		privilegeModelMenuRefDAO.deletePrivilegeModelMenuRefById(modelId);

		//新增權限模組功能關聯
		String[] menuIdArray = privilegeModelVO.getNewMenuIds();
		for (int i=0; i<menuIdArray.length; i++) {
			AdmPrivilegeModelMenuRef objRef = new AdmPrivilegeModelMenuRef();
			AdmPrivilegeModelMenuRefId objRefId = new AdmPrivilegeModelMenuRefId();
			objRefId.setModelId(Integer.parseInt(modelId));
			objRefId.setMenuId(Integer.parseInt(menuIdArray[i]));
			objRef.setId(objRefId);
			privilegeModelMenuRefDAO.save(objRef);
		}
	}

	public void setPrivilegeModelDAO(PrivilegeModelDAO privilegeModelDAO) {
		this.privilegeModelDAO = privilegeModelDAO;
	}

	public void setPrivilegeModelMenuRefDAO(PrivilegeModelMenuRefDAO privilegeModelMenuRefDAO) {
		this.privilegeModelMenuRefDAO = privilegeModelMenuRefDAO;
	}

	public void setAdmuserDAO(AdmUserDAO admuserDAO) {
		this.admuserDAO = admuserDAO;
	}

	public static void main(String[] args) throws Exception {

		System.out.println("===== start test =====");

	    ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
	    MenuService service = (MenuService) context.getBean("PrivilegeModelService");

	    System.out.println("===== end test =====");
	}
}
