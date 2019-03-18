package com.pchome.akbadm.db.service.code;

import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpCodeTrackingService extends IBaseService<PfpCodeTracking, String> {
    public Map<String, PfpCodeTracking> selectPfpCodeTrackingMap();
}
