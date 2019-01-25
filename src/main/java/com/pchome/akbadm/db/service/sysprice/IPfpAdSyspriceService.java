package com.pchome.akbadm.db.service.sysprice;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdSysprice;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdSyspriceService extends IBaseService<PfpAdSysprice, String> {
    public PfpAdSysprice getAdSysprice(String adPoolSeq) throws Exception;

    public List<PfpAdSysprice> getAdSyspriceList() throws Exception;

    public PfpAdSysprice selectAdSyspriceByPoolSeq(String poolSeq);
    
    public float getNewAdSysprice(String day) throws Exception;
}