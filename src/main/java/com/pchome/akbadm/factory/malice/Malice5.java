package com.pchome.akbadm.factory.malice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdClick;

/**
 * over 20 click in an hour
 * @author weich
 */
public class Malice5 extends AbstractMalice {
    private int limitOfHour = 20;
    private int limitOfCount = 1;

    @Override
    public List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList) {
        List<PfpAdClick> validList = new ArrayList<PfpAdClick>();
        List<PfpAdClick> invalidList = new ArrayList<PfpAdClick>();

        Map<String, Map<String, List<PfpAdClick>>> map = null;

        // keyword
        map = this.getKeywordMap(pfpAdClickList);
        this.checkMalice(map, validList, invalidList);

        log.info("valid/all keyword = " + validList.size() + "/" + pfpAdClickList.size());

        maliceService.insertPfpAdKeywordInvalid(5, invalidList);
        maliceService.insertPfpAdKeywordPvclk(invalidList);

        // ad
        map = this.getAdMap(pfpAdClickList);
        this.checkMalice(map, validList, invalidList);

        log.info("valid/all ad = " + validList.size() + "/" + pfpAdClickList.size());

        maliceService.insertPfpAdInvalid(5, invalidList);
        maliceService.insertPfpAdPvclk(invalidList);

        return validList;
    }

    private Map<String, Map<String, List<PfpAdClick>>> getAdMap(List<PfpAdClick> pfpAdClickList) {
        Map<String, Map<String, List<PfpAdClick>>> map1 = new HashMap<String, Map<String, List<PfpAdClick>>>();
        Map<String, List<PfpAdClick>> map2 = null;
        List<PfpAdClick> list = null;
        StringBuffer key1 = null;
        StringBuffer key2 = null;

        for (PfpAdClick adClick: pfpAdClickList) {
            key1 = new StringBuffer();
            key1.append(adClick.getRemoteIp());
            key1.append(adClick.getUserAgent());
            key1.append(adClick.getPfpCustomerInfo().getCustomerInfoId());
            key1.append(adClick.getAdId());
            key1.append(adClick.getKeywordId());
            key1.append(adClick.getAdType());
            key1.append(adClick.getAdActionMaxPrice()); // diff

            key2 = new StringBuffer();
            key2.append(adClick.getPfpCustomerInfo().getCustomerInfoId());
            key2.append(adClick.getAdId()); // diff
            key2.append(adClick.getAdType());
            key2.append(adClick.getAdActionMaxPrice()); // diff

            // check map2, if null, create one
            map2 = map1.get(key1.toString());
            if (map2 == null) {
                list = new ArrayList<PfpAdClick>();
                list.add(adClick);

                map2 = new HashMap<String, List<PfpAdClick>>();
                map2.put(key2.toString(), list);

                map1.put(key1.toString(), map2);

                continue;
            }

            // check clickList, if null, create one
            list = map2.get(key2);
            if (list == null) {
                list = new ArrayList<PfpAdClick>();
            }

            list.add(adClick);
            map2.put(key2.toString(), list);
        }

        return map1;
    }

    private Map<String, Map<String, List<PfpAdClick>>> getKeywordMap(List<PfpAdClick> pfpAdClickList) {
        Map<String, Map<String, List<PfpAdClick>>> map1 = new HashMap<String, Map<String, List<PfpAdClick>>>();
        Map<String, List<PfpAdClick>> map2 = null;
        List<PfpAdClick> list = null;
        StringBuffer key1 = null;
        StringBuffer key2 = null;

        for (PfpAdClick adClick: pfpAdClickList) {
            key1 = new StringBuffer();
            key1.append(adClick.getRemoteIp());
            key1.append(adClick.getUserAgent());
            key1.append(adClick.getPfpCustomerInfo().getCustomerInfoId());
            key1.append(adClick.getAdId());
            key1.append(adClick.getKeywordId());
            key1.append(adClick.getAdType());

            key2 = new StringBuffer();
            key2.append(adClick.getPfpCustomerInfo().getCustomerInfoId());
            key2.append(adClick.getKeywordId()); // diff
            key2.append(adClick.getAdType());

            // check map2, if null, create one
            map2 = map1.get(key1.toString());
            if (map2 == null) {
                list = new ArrayList<PfpAdClick>();
                list.add(adClick);

                map2 = new HashMap<String, List<PfpAdClick>>();
                map2.put(key2.toString(), list);

                map1.put(key1.toString(), map2);

                continue;
            }

            // check clickList, if null, create one
            list = map2.get(key2);
            if (list == null) {
                list = new ArrayList<PfpAdClick>();
            }

            list.add(adClick);
            map2.put(key2.toString(), list);
        }

        return map1;
    }

    private void checkMalice(Map<String, Map<String, List<PfpAdClick>>> map1, List<PfpAdClick> validList, List<PfpAdClick> invalidList) {
        validList = new ArrayList<PfpAdClick>();
        invalidList = new ArrayList<PfpAdClick>();

        Map<String, List<PfpAdClick>> map2 = null;
        List<PfpAdClick> list = null;
        PfpAdClick pfpAdClick = null;

        for (String key1: map1.keySet()) {
            map2 = map1.get(key1);
            for (String key2: map2.keySet()) {
                list = map2.get(key2);
                // if over limit of hour, valid count = 1, invalid count = ck - 1
                for (int i = 0; i < list.size(); i++) {
                    pfpAdClick = list.get(i);

                    if (list.size() >= limitOfHour) {
                        if (i >= limitOfCount) {
                            invalidList.add(pfpAdClick);
                            continue;
                        }
                    }

                    validList.add(pfpAdClick);
                }
            }
        }
    }
}
