package com.pchome.akbadm.db.service.ad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdInvalid;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.pojo.PfpAdKeywordInvalid;
import com.pchome.akbadm.db.pojo.PfpAdKeywordPvclk;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;

public class MaliceService extends AMaliceService {
    @Override
    public void delete(Date recordDate, int recordTime) {
        int count = 0;

        count = pfpAdPvclkService.deleteMalice(recordDate, recordTime);
        log.info("delete pfpAdPvclk " + count);

        pfpAdInvalidService.deleteMalice(recordDate, recordTime);
        log.info("delete pfpAdInvalid " + count);

        pfpAdKeywordPvclkService.deleteMalice(recordDate, recordTime);
        log.info("delete pfpAdKeywordPvclk " + count);

        pfpAdKeywordInvalidService.deleteMalice(recordDate, recordTime);
        log.info("delete pfpAdKeywordInvalid " + count);
    }

    @Override
    public void insert(int maliceType, List<PfpAdClick> pfpAdClickList) {
        this.insertPfpAdInvalid(maliceType, pfpAdClickList);
        this.insertPfpAdPvclk(pfpAdClickList);
        this.insertPfpAdKeywordInvalid(maliceType, pfpAdClickList);
        this.insertPfpAdKeywordPvclk(pfpAdClickList);
    }

    @Override
    public void insertPfpAdInvalid(int maliceType, List<PfpAdClick> pfpAdClickList) {
        Date date = new Date();

        // PfpAdInvalid
        List<PfpAdInvalid> pfpAdInvalidList = new ArrayList<PfpAdInvalid>();
        PfpAdInvalid pfpAdInvalid = null;
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            pfpAdInvalid = new PfpAdInvalid();
            pfpAdInvalid.setAdInvalidDate(pfpAdClick.getRecordDate());
            pfpAdInvalid.setAdInvalidTime(pfpAdClick.getRecordTime());
            pfpAdInvalid.setCustomerInfoId(pfpAdClick.getPfpCustomerInfo().getCustomerInfoId());
            pfpAdInvalid.setPfpAd(pfpAdService.get(pfpAdClick.getAdId()));
            pfpAdInvalid.setAdType(Integer.parseInt(pfpAdClick.getAdType()));
            pfpAdInvalid.setMaliceType(maliceType);
            pfpAdInvalid.setAdInvalidClk(pfpAdClick.getAdClk());
            pfpAdInvalid.setAdInvalidClkPrice(pfpAdClick.getAdPrice());
            pfpAdInvalid.setAdInvalidUpdateTime(date);
            pfpAdInvalid.setAdInvalidCreateTime(date);
            pfpAdInvalidList.add(pfpAdInvalid);
        }
        pfpAdInvalidService.saveOrUpdateAll(pfpAdInvalidList);
    }

    @Override
    public void insertPfpAdPvclk(List<PfpAdClick> pfpAdClickList) {
        Date date = new Date();

        // PfpAdPvclk
        List<PfpAdPvclk> pfpAdPvclkList = new ArrayList<PfpAdPvclk>();
        PfpAdPvclk pfpAdPvclk = null;
        PfpAd pfpAd = null;
        PfpAdGroup pfpAdGroup = null;
        PfpAdAction pfpAdAction = null;
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            pfpAd = pfpAdService.get(pfpAdClick.getAdId());
            pfpAdGroup = pfpAd.getPfpAdGroup();
            pfpAdAction = pfpAdGroup.getPfpAdAction();

            pfpAdPvclk = new PfpAdPvclk();
            pfpAdPvclk.setAdPvclkDate(pfpAdClick.getRecordDate());
            pfpAdPvclk.setAdPvclkTime(pfpAdClick.getRecordTime());
            pfpAdPvclk.setCustomerInfoId(pfpAdClick.getPfpCustomerInfo().getCustomerInfoId());
            pfpAdPvclk.setStyleNo("");
            pfpAdPvclk.setTemplateProductSeq(pfpAdClick.getTproId());
            pfpAdPvclk.setTemplateAdSeq(pfpAdClick.getTadId());
            pfpAdPvclk.setPfpAd(pfpAd);
            pfpAdPvclk.setAdType(Integer.parseInt(pfpAdClick.getAdType()));
            pfpAdPvclk.setAdPvclkPropClassify("");
            pfpAdPvclk.setAdUrl("");
            pfpAdPvclk.setAdPvclkDevice("");
            pfpAdPvclk.setAdPvclkOs("");
            pfpAdPvclk.setAdPvclkBrand("");
            pfpAdPvclk.setAdPvclkArea("");
            pfpAdPvclk.setAdActionControlPrice(pfpAdClick.getAdActionControlPrice());
            pfpAdPvclk.setAdActionMaxPrice(pfpAdClick.getAdActionMaxPrice());
            pfpAdPvclk.setAdActionSeq(pfpAdAction.getAdActionSeq());
            pfpAdPvclk.setAdGroupSeq(pfpAdGroup.getAdGroupSeq());
            pfpAdPvclk.setPfdCustomerInfoId(pfpAdClick.getPfdCustomerInfoId());
            pfpAdPvclk.setPfdUserId(pfpAdClick.getPfdUserId());
            pfpAdPvclk.setPfbxCustomerInfoId(pfpAdClick.getPfbxCustomerInfoId());
            pfpAdPvclk.setPfbxPositionId(pfpAdClick.getPfbxPositionId());
//            pfpAdPvclk.setPayType();  // no data
            pfpAdPvclk.setAdPv(0);
            pfpAdPvclk.setAdClk(0);
            pfpAdPvclk.setAdInvalidClk(pfpAdClick.getAdClk());
            pfpAdPvclk.setAdPvPrice(0f);
            pfpAdPvclk.setAdClkPrice(0f);
            pfpAdPvclk.setAdInvalidClkPrice(pfpAdClick.getAdPrice());
            pfpAdPvclk.setAdPvclkUpdateTime(date);
            pfpAdPvclk.setAdPvclkCreateTime(date);
            pfpAdPvclkList.add(pfpAdPvclk);
        }
        pfpAdPvclkService.saveOrUpdateAll(pfpAdPvclkList);
    }

    @Override
    public void insertPfpAdKeywordInvalid(int maliceType, List<PfpAdClick> pfpAdClickList) {
        Date date = new Date();

        // PfpAdKeywordInvalid
        List<PfpAdKeywordInvalid> pfpAdKeywordInvalidList = new ArrayList<PfpAdKeywordInvalid>();
        PfpAdKeywordInvalid pfpAdKeywordInvalid = null;
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            pfpAdKeywordInvalid = new PfpAdKeywordInvalid();
            pfpAdKeywordInvalid.setAdKeywordInvalidDate(pfpAdClick.getRecordDate());
            pfpAdKeywordInvalid.setAdKeywordInvalidTime(pfpAdClick.getRecordTime());
            pfpAdKeywordInvalid.setCustomerInfoId(pfpAdClick.getPfpCustomerInfo().getCustomerInfoId());
            pfpAdKeywordInvalid.setPfpAdKeyword(pfpAdKeywordService.get(pfpAdClick.getKeywordId()));
            pfpAdKeywordInvalid.setAdKeywordType(Integer.parseInt(pfpAdClick.getAdType()));
            pfpAdKeywordInvalid.setMaliceType(maliceType);
            pfpAdKeywordInvalid.setAdKeywordInvalidClk(pfpAdClick.getAdClk());
            pfpAdKeywordInvalid.setAdKeywordInvalidClkPrice(pfpAdClick.getAdPrice());
            pfpAdKeywordInvalid.setAdKeywordInvalidUpdateTime(date);
            pfpAdKeywordInvalid.setAdKeywordInvalidCreateTime(date);
            pfpAdKeywordInvalidList.add(pfpAdKeywordInvalid);

        }
        pfpAdKeywordInvalidService.saveOrUpdateAll(pfpAdKeywordInvalidList);
    }

    @Override
    public void insertPfpAdKeywordPvclk(List<PfpAdClick> pfpAdClickList) {
        Date date = new Date();

        // PfpAdKeywordPvclk
        List<PfpAdKeywordPvclk> pfpAdKeywordPvclkList = new ArrayList<PfpAdKeywordPvclk>();
        PfpAdKeywordPvclk pfpAdKeywordPvclk = null;
        PfpAdKeyword pfpAdKeyword = null;
        PfpAdGroup pfpAdGroup = null;
        PfpAdAction pfpAdAction = null;
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            pfpAdKeyword = pfpAdKeywordService.get(pfpAdClick.getKeywordId());
            pfpAdGroup = pfpAdKeyword.getPfpAdGroup();
            pfpAdAction = pfpAdGroup.getPfpAdAction();

            pfpAdKeywordPvclk = new PfpAdKeywordPvclk();
            pfpAdKeywordPvclk.setAdKeywordPvclkDate(pfpAdClick.getRecordDate());
            pfpAdKeywordPvclk.setAdKeywordPvclkTime(pfpAdClick.getRecordTime());
            pfpAdKeywordPvclk.setCustomerInfoId(pfpAdClick.getPfpCustomerInfo().getCustomerInfoId());
            pfpAdKeywordPvclk.setPfpAdKeyword(pfpAdKeywordService.get(pfpAdClick.getKeywordId()));
            pfpAdKeywordPvclk.setAdKeywordType(Integer.parseInt(pfpAdClick.getAdType()));
            pfpAdKeywordPvclk.setAdKeywordPvclkPropClassify("");
            pfpAdKeywordPvclk.setAdKeywordPvclkDevice("");
            pfpAdKeywordPvclk.setAdKeywordPvclkOs("");
            pfpAdKeywordPvclk.setAdKeywordPvclkBrand("");
            pfpAdKeywordPvclk.setAdKeywordPvclkArea("");
            pfpAdKeywordPvclk.setAdActionSeq(pfpAdAction.getAdActionSeq());
            pfpAdKeywordPvclk.setAdGroupSeq(pfpAdGroup.getAdGroupSeq());
            pfpAdKeywordPvclk.setPfdCustomerInfoId(pfpAdClick.getPfdCustomerInfoId());
            pfpAdKeywordPvclk.setPfdUserId(pfpAdClick.getPfdUserId());
//            pfpAdKeywordPvclk.setPayType(); // no data
//            pfpAdKeywordPvclk.setAdKeywordSearchStyle(); // no data
            pfpAdKeywordPvclk.setAdKeywordPv(0);
            pfpAdKeywordPvclk.setAdKeywordClk(0);
            pfpAdKeywordPvclk.setAdKeywordInvalidClk(pfpAdClick.getAdClk());
            pfpAdKeywordPvclk.setAdKeywordPvPrice(0f);
            pfpAdKeywordPvclk.setAdKeywordClkPrice(0f);
            pfpAdKeywordPvclk.setAdKeywordInvalidClkPrice(pfpAdClick.getAdPrice());
            pfpAdKeywordPvclk.setAdKeywordPvclkUpdateTime(date);
            pfpAdKeywordPvclk.setAdKeywordPvclkCreateTime(date);
            pfpAdKeywordPvclkList.add(pfpAdKeywordPvclk);
        }
        pfpAdKeywordPvclkService.saveOrUpdateAll(pfpAdKeywordPvclkList);
    }
}
