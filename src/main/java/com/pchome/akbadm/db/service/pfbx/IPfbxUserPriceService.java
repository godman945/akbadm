package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxUserPrice;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxUserPriceService extends IBaseService<PfbxUserPrice, String> {
    public List<PfbxUserPrice> selectPfbxUserPriceByStatus(String status);
}
