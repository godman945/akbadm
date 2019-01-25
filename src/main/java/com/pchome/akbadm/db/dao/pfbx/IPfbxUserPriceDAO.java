package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserPrice;

public interface IPfbxUserPriceDAO extends IBaseDAO<PfbxUserPrice, String> {
    public List<PfbxUserPrice> selectPfbxUserPriceByStatus(String status);
}
