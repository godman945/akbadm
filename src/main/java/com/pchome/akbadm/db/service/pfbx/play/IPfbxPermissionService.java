package com.pchome.akbadm.db.service.pfbx.play;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxPermission;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxPermissionService extends IBaseService<PfbxPermission,String> {
    //新增拒絕理由
    public void savePfbxPermissionList(List<PfbxPermission> PfbxPermissionList) throws Exception;
    //查詢網址
    public List<PfbxPermission> findPfbxPermissionListByUrl(String pfbxApplyUrl) throws Exception;
}
