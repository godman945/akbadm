package com.pchome.akbadm.db.service.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.IPfbxUserPriceDAO;
import com.pchome.akbadm.db.pojo.PfbxUserPrice;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxUserPriceService extends BaseService<PfbxUserPrice, String> implements IPfbxUserPriceService {
    public List<PfbxUserPrice> selectPfbxUserPriceByStatus(String status) {
        return ((IPfbxUserPriceDAO) dao).selectPfbxUserPriceByStatus(status);
    }
}
