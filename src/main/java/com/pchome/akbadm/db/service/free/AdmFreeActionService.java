package com.pchome.akbadm.db.service.free;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.free.IAdmFreeActionDAO;
import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.AdmFreeActionVO;
import com.pchome.enumerate.factory.EnumActivityType;

public class AdmFreeActionService extends BaseService<AdmFreeAction, String> implements IAdmFreeActionService{

	public AdmFreeAction findFreeAction(EnumActivityType enumActivityType) {
		
		List<AdmFreeAction> admFreeActions = ((IAdmFreeActionDAO)dao).findFreeAction(enumActivityType.getActivityId());
		
		if(admFreeActions.size() > 0){
			return admFreeActions.get(0);
		}else{
			return null;
		}
	}

	public AdmFreeAction findFreeAction(String actionId) {
		
		List<AdmFreeAction> admFreeActions = ((IAdmFreeActionDAO)dao).findFreeAction(actionId);
		
		if(admFreeActions.size() > 0){
			return admFreeActions.get(0);
		}else{
			return null;
		}
	}
	
	public List<AdmFreeAction> findFreeAction(Map<String, String> conditionMap) throws Exception {
		return ((IAdmFreeActionDAO)dao).findFreeAction(conditionMap);
	}

	@Override
	public List<AdmFreeActionVO> getAdmFreeActionData(final Map<String, String> conditionMap) {
		return ((IAdmFreeActionDAO)dao).getAdmFreeActionData(conditionMap);
	}
	
	
}
