package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxUserSample;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxUserSampleService extends IBaseService<PfbxUserSample, String> {
    public List<PfbxUserSample> selectPfbxUserSampleByStatus(String status);
}
