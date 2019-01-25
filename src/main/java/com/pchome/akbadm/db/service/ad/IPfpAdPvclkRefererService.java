package com.pchome.akbadm.db.service.ad;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfpAdPvclkReferer;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdPvclkRefererService extends IBaseService<PfpAdPvclkReferer, String> {
    public boolean insertSelect(Date date);
}
