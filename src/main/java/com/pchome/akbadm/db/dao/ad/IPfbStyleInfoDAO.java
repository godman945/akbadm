package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbStyleInfo;

public interface IPfbStyleInfoDAO extends IBaseDAO<PfbStyleInfo, String> {
    public List<PfbStyleInfo> selectValidPfbStyleInfo();
}