package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfpAdClick;

public abstract class AMaliceService {
    protected Log log = LogFactory.getLog(this.getClass());

    protected IPfpAdClickService pfpAdClickService;
    protected IPfpAdService pfpAdService;
    protected IPfpAdInvalidService pfpAdInvalidService;
    protected IPfpAdKeywordService pfpAdKeywordService;
    protected IPfpAdKeywordInvalidService pfpAdKeywordInvalidService;
    protected IPfpAdKeywordPvclkService pfpAdKeywordPvclkService;
    protected IPfpAdPvclkService pfpAdPvclkService;

    public abstract void delete(Date recordDate, int recordTime);

    public abstract void insert(int maliceType, List<PfpAdClick> pfpAdClickList);

    public abstract void insertPfpAdInvalid(int maliceType, List<PfpAdClick> pfpAdClickList);

    public abstract void insertPfpAdPvclk(List<PfpAdClick> pfpAdClickList);

    public abstract void insertPfpAdKeywordInvalid(int maliceType, List<PfpAdClick> pfpAdClickList);

    public abstract void insertPfpAdKeywordPvclk(List<PfpAdClick> pfpAdClickList);

    public IPfpAdClickService getPfpAdClickService() {
        return pfpAdClickService;
    }

    public void setPfpAdClickService(IPfpAdClickService pfpAdClickService) {
        this.pfpAdClickService = pfpAdClickService;
    }

    public IPfpAdService getPfpAdService() {
        return pfpAdService;
    }

    public void setPfpAdService(IPfpAdService pfpAdService) {
        this.pfpAdService = pfpAdService;
    }

    public IPfpAdInvalidService getPfpAdInvalidService() {
        return pfpAdInvalidService;
    }

    public void setPfpAdInvalidService(IPfpAdInvalidService pfpAdInvalidService) {
        this.pfpAdInvalidService = pfpAdInvalidService;
    }

    public IPfpAdKeywordService getPfpAdKeywordService() {
        return pfpAdKeywordService;
    }

    public void setPfpAdKeywordService(IPfpAdKeywordService pfpAdKeywordService) {
        this.pfpAdKeywordService = pfpAdKeywordService;
    }

    public IPfpAdKeywordInvalidService getPfpAdKeywordInvalidService() {
        return pfpAdKeywordInvalidService;
    }

    public void setPfpAdKeywordInvalidService(
            IPfpAdKeywordInvalidService pfpAdKeywordInvalidService) {
        this.pfpAdKeywordInvalidService = pfpAdKeywordInvalidService;
    }

    public IPfpAdKeywordPvclkService getPfpAdKeywordPvclkService() {
        return pfpAdKeywordPvclkService;
    }

    public void setPfpAdKeywordPvclkService(
            IPfpAdKeywordPvclkService pfpAdKeywordPvclkService) {
        this.pfpAdKeywordPvclkService = pfpAdKeywordPvclkService;
    }

    public IPfpAdPvclkService getPfpAdPvclkService() {
        return pfpAdPvclkService;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }
}
