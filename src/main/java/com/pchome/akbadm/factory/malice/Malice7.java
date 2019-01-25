package com.pchome.akbadm.factory.malice;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdClick;

/**
 * over 10 click on 10x10 px rectangle
 * @author weich
 */
public class Malice7 extends AbstractMalice {
    private int width = 10;
    private int height = 10;

    @Override
    public List<PfpAdClick> execute(List<PfpAdClick> pfpAdClickList) {
        List<PfpAdClick> validList = new ArrayList<PfpAdClick>();
        List<PfpAdClick> invalidList = new ArrayList<PfpAdClick>();

        Map<String, Map<String, List<PfpAdClick>>> positionMap = new HashMap<String, Map<String, List<PfpAdClick>>>();
        Map<String, List<PfpAdClick>> rectangleMap = null;
        List<PfpAdClick> rectangleList = null;
        boolean coverFlag = false;
        StringBuffer position = null;
        String[] postions = null;
        Rectangle rectangle = null;
        int validCount = 0;

        // arrange
        for (PfpAdClick pfpAdClick: pfpAdClickList) {
            coverFlag = false;

            position = new StringBuffer(pfpAdClick.getMouseDownX() + "," + pfpAdClick.getMouseDownY());

            // get rectangleMap, if null, create one
            rectangleMap = positionMap.get(pfpAdClick.getPfbxPositionId());
            if (rectangleMap == null) {
                rectangleList = new ArrayList<PfpAdClick>();
                rectangleList.add(pfpAdClick);

                rectangleMap = new HashMap<String, List<PfpAdClick>>();
                rectangleMap.put(position.toString(), rectangleList);

                positionMap.put(pfpAdClick.getPfbxPositionId(), rectangleMap);

                continue;
            }

            // check position, if graph cover, add to list
            for (String pos: rectangleMap.keySet()) {
                postions = pos.split(",");

                rectangle = new Rectangle(pfpAdClick.getMouseDownX(), pfpAdClick.getMouseDownY(), width, height);
                if (rectangle.contains(Integer.parseInt(postions[0]), Integer.parseInt(postions[1]), width, height)) {
                    // check rectangleList, if null, create one
                    rectangleList = rectangleMap.get(pos);
                    if (rectangleList == null) {
                        rectangleList = new ArrayList<PfpAdClick>();
                    }

                    rectangleList.add(pfpAdClick);
                    rectangleMap.put(pos, rectangleList);

                    coverFlag = true;
                    break;
                }
            }
            if (coverFlag) {
                continue;
            }

            // graphs don't cover, create one
            rectangleList = new ArrayList<PfpAdClick>();
            rectangleList.add(pfpAdClick);

            rectangleMap.put(position.toString(), rectangleList);
        }

        // check malice
        for (String positionId: positionMap.keySet()) {
            rectangleMap = positionMap.get(positionId);

            for (String pos: rectangleMap.keySet()) {
                rectangleList = rectangleMap.get(pos);

                // every 10 count 1, and count less than 10
                validCount = (rectangleList.size() / 10) + (rectangleList.size() % 10);

                for (int i = 0; i < rectangleList.size(); i++) {
                    // invalid
                    if (i >= validCount) {
                        invalidList.add(rectangleList.get(i));
                        continue;
                    }

                    // valid
                    validList.add(rectangleList.get(i));
                }
            }
        }

        log.info("valid/all = " + validList.size() + "/" + pfpAdClickList.size());

        maliceService.insert(7, invalidList);

        return validList;
    }
}
