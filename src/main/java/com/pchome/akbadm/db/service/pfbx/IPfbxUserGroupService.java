package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxUserGroup;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxUserGroupService extends IBaseService<PfbxUserGroup, String> {
    public List<PfbxUserGroup> selectPfbxUserGroupByStatus(String status);
    
    public PfbxUserGroup getPfbxUserGroupById(String id) throws Exception ;
}
