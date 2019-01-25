package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserSample;

public interface IPfbxUserSampleDAO extends IBaseDAO<PfbxUserSample, String> {
    public List<PfbxUserSample> selectPfbxUserSampleByStatus(String status);
}
