package com.pchome.akbadm.db.service.ad;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IAdmArwValueDAO;
import com.pchome.akbadm.db.pojo.AdmArwValue;
import com.pchome.akbadm.db.service.BaseService;

public class AdmArwValueService extends BaseService<AdmArwValue, String> implements IAdmArwValueService {
    @Override
    public Map<String, Integer> selectAdmArwMap() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Map<String, Integer> map = new HashMap<>();

        List<AdmArwValue> list = ((IAdmArwValueDAO) dao).loadAll();
        for (AdmArwValue admArwValue: list) {
            if (admArwValue.getDateFlag() == 0) {
                map.put(admArwValue.getCustomerInfoId(), admArwValue.getArwValue());
            }
            else if (admArwValue.getDateFlag() == 1) {
                if (calendar.getTimeInMillis() < admArwValue.getStartDate().getTime()) {
                    continue;
                }
                if (calendar.getTimeInMillis() > admArwValue.getEndDate().getTime()) {
                    continue;
                }
                map.put(admArwValue.getCustomerInfoId(), admArwValue.getArwValue());
            }
        }

        return map;
    }
}
