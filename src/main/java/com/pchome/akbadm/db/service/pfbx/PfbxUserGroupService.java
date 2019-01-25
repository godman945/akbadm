package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.IPfbxUserGroupDAO;
import com.pchome.akbadm.db.pojo.PfbxUserGroup;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxUserGroupService extends BaseService<PfbxUserGroup, String> implements IPfbxUserGroupService {
    public List<PfbxUserGroup> selectPfbxUserGroupByStatus(String status) {
        return ((IPfbxUserGroupDAO) dao).selectPfbxUserGroupByStatus(status);
    }
    
    public PfbxUserGroup getPfbxUserGroupById(String id) throws Exception {
			return ((IPfbxUserGroupDAO) dao).getPfbxUserGroupById(id);
    }
}
