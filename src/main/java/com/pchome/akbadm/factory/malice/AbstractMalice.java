package com.pchome.akbadm.factory.malice;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.ad.AMaliceService;

public abstract class AbstractMalice {
    protected Logger log = LogManager.getRootLogger();

    protected AMaliceService maliceService;

    public abstract List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList);

    public void setMaliceService(AMaliceService maliceService) {
        this.maliceService = maliceService;
    }
}
