package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.IPfbxUserSampleDAO;
import com.pchome.akbadm.db.pojo.PfbxUserSample;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxUserSampleService extends BaseService<PfbxUserSample, String> implements IPfbxUserSampleService {
    public List<PfbxUserSample> selectPfbxUserSampleByStatus(String status) {
        return ((IPfbxUserSampleDAO) dao).selectPfbxUserSampleByStatus(status);
    }
}
