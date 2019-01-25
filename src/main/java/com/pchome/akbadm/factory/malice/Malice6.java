package com.pchome.akbadm.factory.malice;

import java.util.ArrayList;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdClick;

/**
 * click over padding
 * @author weich
 */
public class Malice6 extends AbstractMalice {
    private int padding = 10;

    @Override
    public List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList) {
        List<PfpAdClick> validList = new ArrayList<PfpAdClick>();
        List<PfpAdClick> invalidList = new ArrayList<PfpAdClick>();

        // check malice
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            // invalid
            if ((pfpAdClick.getMouseDownX() <= padding) ||
                    (pfpAdClick.getMouseDownX() >= pfpAdClick.getMouseAreaWidth() - padding) ||
                    (pfpAdClick.getMouseDownY() <= padding) ||
                    (pfpAdClick.getMouseDownY() >= pfpAdClick.getMouseAreaHeight() - padding)) {

                invalidList.add(pfpAdClick);
                continue;
            }

            // valid
            validList.add(pfpAdClick);
        }

        log.info("valid/all = " + validList.size() + "/" + pfpAdClickList.size());

        maliceService.insert(6, invalidList);

        return validList;
    }
}
