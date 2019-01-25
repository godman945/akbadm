package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbStyleInfo;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbStyleInfoService extends IBaseService<PfbStyleInfo, String> {
    public List<PfbStyleInfo> selectValidPfbStyleInfo();
}