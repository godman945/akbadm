package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.IPfbxPositionDAO;
import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxPositionService extends BaseService<PfbxPosition,String> implements IPfbxPositionService {
    @Override
    public List<PfbxPosition> selectPfbxPositionByDeleteFlag(int deleteFlag) {
        return ((IPfbxPositionDAO) dao).selectPfbxPositionByDeleteFlag(deleteFlag);
    }
    
    @Override
    public List<PfbxPosition> findPositionByPfbCustomerId(String position, String pfbCustomerId){
    	 return ((IPfbxPositionDAO) dao).findPositionByPfbCustomerId(position, pfbCustomerId);
	}
}
