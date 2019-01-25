package com.pchome.akbadm.utils;

import java.math.BigDecimal;

public class ConvertUtil {
    public static Integer convertInteger(Object srcObj) {
        Integer destObj = 0;

        if (srcObj instanceof Integer) {
            destObj = ((Integer) srcObj).intValue();
        }
        else if (srcObj instanceof Long) {
            destObj = ((Long) srcObj).intValue();
        }
        else if (srcObj instanceof Float) {
            destObj = ((Float) srcObj).intValue();
        }
        else if (srcObj instanceof Double) {
            destObj = ((Double) srcObj).intValue();
        }
        else if (srcObj instanceof BigDecimal) {
            destObj = ((BigDecimal) srcObj).intValue();
        }

        return destObj;
    }

    public static Float convertFloat(Object srcObj) {
        Float destObj = 0f;

        if (srcObj instanceof Integer) {
            destObj = ((Integer) srcObj).floatValue();
        }
        else if (srcObj instanceof Long) {
            destObj = ((Long) srcObj).floatValue();
        }
        else if (srcObj instanceof Float) {
            destObj = ((Float) srcObj).floatValue();
        }
        else if (srcObj instanceof Double) {
            destObj = ((Double) srcObj).floatValue();
        }
        else if (srcObj instanceof BigDecimal) {
            destObj = ((BigDecimal) srcObj).floatValue();
        }

        return destObj;
    }

    public static Float[] convertFloat(Object[] srcObjs) {
        Float[] destObjs = new Float[srcObjs.length];

        for (int i = 0; i < srcObjs.length; i++) {
            destObjs[i] = convertFloat(srcObjs[i]);
        }

        return destObjs;
    }
}
