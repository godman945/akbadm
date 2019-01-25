package com.pchome.akbadm.db.dao.ad;

import java.util.Date;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordInvalid;

public interface IPfpAdKeywordInvalidDAO extends IBaseDAO<PfpAdKeywordInvalid, String> {
    public int deleteMalice(Date recordDate, int recordTime);
}
