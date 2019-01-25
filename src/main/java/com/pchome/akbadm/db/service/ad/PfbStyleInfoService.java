package com.pchome.akbadm.db.service.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.ad.IPfbStyleInfoDAO;
import com.pchome.akbadm.db.pojo.PfbStyleInfo;
import com.pchome.akbadm.db.service.BaseService;

public class PfbStyleInfoService extends BaseService<PfbStyleInfo, String> implements IPfbStyleInfoService {
    public List<PfbStyleInfo> selectValidPfbStyleInfo() {
        return ((IPfbStyleInfoDAO) dao).selectValidPfbStyleInfo();
    }
}