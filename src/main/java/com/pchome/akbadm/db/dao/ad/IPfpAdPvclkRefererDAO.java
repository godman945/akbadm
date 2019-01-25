package com.pchome.akbadm.db.dao.ad;

import java.util.Date;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkReferer;

public interface IPfpAdPvclkRefererDAO extends IBaseDAO<PfpAdPvclkReferer, String> {
    public PfpAdPvclkReferer selectOneBeforeDate(Date pvclkDate);

    public PfpAdPvclkReferer selectOneByDate(Date pvclkDate);

    public PfpAdPvclkReferer selectBackupByDate(Date pvclkDate);

    public int selectCountByDate(Date pvclkDate);

    public int replaceSelectByDate(Date pvclkDate);

    public int deleteByDate(Date pvclkDate);
}
