package com.pchome.akbadm.db.service.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.code.IPfpCodeTrackingDAO;
import com.pchome.akbadm.db.pojo.PfpCodeTracking;
import com.pchome.akbadm.db.service.BaseService;

public class PfpCodeTrackingService extends BaseService<PfpCodeTracking, String> implements IPfpCodeTrackingService {
    @Override
    public Map<String, PfpCodeTracking> selectPfpCodeTrackingMap() {
        Map<String, PfpCodeTracking> map = new HashMap<>();

        List<PfpCodeTracking> list = ((IPfpCodeTrackingDAO)dao).loadAll();
        for (PfpCodeTracking bean: list) {
            map.put(bean.getTrackingSeq(), bean);
        }

        return map;
    }
}
