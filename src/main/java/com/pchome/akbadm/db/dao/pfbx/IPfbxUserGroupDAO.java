package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserGroup;

public interface IPfbxUserGroupDAO extends IBaseDAO<PfbxUserGroup, String> {
    public List<PfbxUserGroup> selectPfbxUserGroupByStatus(String status);
    
    public PfbxUserGroup getPfbxUserGroupById(String id) throws Exception;
}
