package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxPosition;

public interface IPfbxPositionDAO extends IBaseDAO<PfbxPosition, String> {
    public List<PfbxPosition> selectPfbxPositionByDeleteFlag(int deleteFlag);
    public List<PfbxPosition> findPositionByPfbCustomerId(String position, String pfbCustomerId);
}
