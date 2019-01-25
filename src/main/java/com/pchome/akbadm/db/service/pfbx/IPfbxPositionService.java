package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxPositionService extends IBaseService<PfbxPosition,String> {
    public List<PfbxPosition> selectPfbxPositionByDeleteFlag(int deleteFlag);
    public List<PfbxPosition> findPositionByPfbCustomerId(String position, String pfbCustomerId);
}
