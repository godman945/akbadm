package com.pchome.akbadm.db.service.ad;

import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmArwValue;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmArwValueService extends IBaseService <AdmArwValue, String> {
    public abstract Map<String, Integer> selectAdmArwMap();
}
