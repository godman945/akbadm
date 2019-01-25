package com.pchome.akbadm.db.dao.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPermission;

public interface IPfbxPermissionDAO extends IBaseDAO<PfbxPermission, String> {

    public void savePfbxPermissionList(List<PfbxPermission> PfbxPermissionList) throws Exception;

    
    public List<Object> findPfbxPermissionListByUrl(String pfbxApplyUrl) throws Exception;
}
