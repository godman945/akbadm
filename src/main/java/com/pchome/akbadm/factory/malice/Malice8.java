package com.pchome.akbadm.factory.malice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdClick;

/**
 * over today control price
 * @author weich
 */
public class Malice8 extends AbstractMalice {
    @Override
    public List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList) {
        List<PfpAdClick> validList = new ArrayList<PfpAdClick>();
        List<PfpAdClick> invalidList = new ArrayList<PfpAdClick>();
        List<PfpAdClick> recordList = new ArrayList<PfpAdClick>();
        Map<String, Float> costPriceMap = new HashMap<String, Float>();

        PfpAd pfpAd = null;
        PfpAdAction pfpAdAction = null;
        Float costPrice = null;

        // get cost price sum
        if (pfpAdClickList.size() > 0) {
            PfpAdClick pfpAdClick = pfpAdClickList.get(0);
            costPriceMap = this.maliceService.getPfpAdClickService().findCostSum(pfpAdClick.getRecordDate(), pfpAdClick.getRecordTime());
        }

        // check malice
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            pfpAd = this.maliceService.getPfpAdService().get(pfpAdClick.getAdId());
            if (pfpAd == null) {
                continue;
            }

            pfpAdAction = pfpAd.getPfpAdGroup().getPfpAdAction();
            costPrice = costPriceMap.get(pfpAdAction.getAdActionSeq());
            if (costPrice == null) {
                costPrice = pfpAdClick.getAdPrice();
            }

            // invalid
            if (costPrice >= pfpAdClick.getAdActionMaxPrice()) {
                invalidList.add(pfpAdClick);
                continue;
            }

            costPriceMap.put(pfpAdAction.getAdActionSeq(), costPrice + pfpAdClick.getAdPrice());

            // valid
            validList.add(pfpAdClick);
        }

        log.info("valid/all = " + validList.size() + "/" + pfpAdClickList.size());

        maliceService.insert(8, invalidList);

        return validList;
    }
}
