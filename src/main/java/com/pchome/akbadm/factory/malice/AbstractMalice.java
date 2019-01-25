package com.pchome.akbadm.factory.malice;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.ad.AMaliceService;

public abstract class AbstractMalice {
    protected Log log = LogFactory.getLog(this.getClass());

    protected AMaliceService maliceService;

    public abstract List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList);

    public void setMaliceService(AMaliceService maliceService) {
        this.maliceService = maliceService;
    }
}
