package com.pchome.akbadm.db.service.ad;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfpAdKeywordInvalid;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdKeywordInvalidService extends IBaseService<PfpAdKeywordInvalid, String> {
    public int deleteMalice(Date recordDate, int recordTime);
}
