package com.pchome.akbadm.db.service.ad;

import java.util.Date;

import com.pchome.akbadm.db.dao.ad.IPfpAdKeywordInvalidDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordInvalid;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdKeywordInvalidService extends BaseService<PfpAdKeywordInvalid, String> implements IPfpAdKeywordInvalidService {
    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        return ((IPfpAdKeywordInvalidDAO)dao).deleteMalice(recordDate, recordTime);
    }
}
