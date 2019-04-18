package com.pchome.akbadm.db.service.code;

import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpCodeAdactionMerge;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpCodeAdactionMergeService extends IBaseService<PfpCodeAdactionMerge, String> {
    public Map<String, PfpCodeAdactionMerge> selectPfpCodeAdactionMergeMap();
}
