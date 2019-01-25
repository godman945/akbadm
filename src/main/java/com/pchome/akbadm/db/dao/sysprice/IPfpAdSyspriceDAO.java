package com.pchome.akbadm.db.dao.sysprice;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdSysprice;

public interface IPfpAdSyspriceDAO extends IBaseDAO<PfpAdSysprice, String> {
    public PfpAdSysprice getAdSysprice(String adPoolSeq) throws Exception;

    public List<PfpAdSysprice> getAdSyspriceList() throws Exception;

    public List<PfpAdSysprice> selectAdSyspriceByPoolSeq(String adPoolSeq);
    
    public float getNewAdSysprice(String day) throws Exception;
}