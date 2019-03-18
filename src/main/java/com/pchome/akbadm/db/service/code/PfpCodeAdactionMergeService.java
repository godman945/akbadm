package com.pchome.akbadm.db.service.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.code.IPfpCodeAdactionMergeDAO;
import com.pchome.akbadm.db.pojo.PfpCodeAdactionMerge;
import com.pchome.akbadm.db.service.BaseService;

public class PfpCodeAdactionMergeService extends BaseService<PfpCodeAdactionMerge, String> implements IPfpCodeAdactionMergeService {
    @Override
    public Map<String, PfpCodeAdactionMerge> selectPfpCodeAdactionMergeMap() {
        Map<String, PfpCodeAdactionMerge> map = new HashMap<>();

        List<PfpCodeAdactionMerge> list = ((IPfpCodeAdactionMergeDAO)dao).loadAll();
        for (PfpCodeAdactionMerge bean: list) {
            map.put(bean.getAdActionSeq(), bean);
        }

        return map;
    }
}
