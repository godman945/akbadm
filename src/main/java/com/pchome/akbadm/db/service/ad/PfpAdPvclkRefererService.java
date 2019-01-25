package com.pchome.akbadm.db.service.ad;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pchome.akbadm.db.dao.ad.IPfpAdPvclkRefererDAO;
import com.pchome.akbadm.db.pojo.PfpAdPvclkReferer;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdPvclkRefererService extends BaseService<PfpAdPvclkReferer, String> implements IPfpAdPvclkRefererService {
    @Override
    public boolean insertSelect(Date date) {
        int selectCount = 0;
        int replaceCount = 0;
        int deleteCount = 0;

        // select by date
        PfpAdPvclkReferer pfpAdPvclkReferer = null;
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            date = calendar.getTime();

            pfpAdPvclkReferer = ((IPfpAdPvclkRefererDAO)dao).selectOneBeforeDate(date);
        }
        else {
            pfpAdPvclkReferer = ((IPfpAdPvclkRefererDAO)dao).selectOneByDate(date);
        }

        if (pfpAdPvclkReferer == null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            log.info("date " + df.format(date));
            log.info("pfpAdPvclkReferer = null");
            return true;
        }

        Date pvclkDate = pfpAdPvclkReferer.getAdPvclkDate();
        log.info("pvclkDate " + pvclkDate);

        // select backup by date
        pfpAdPvclkReferer = ((IPfpAdPvclkRefererDAO)dao).selectBackupByDate(pvclkDate);
        if (pfpAdPvclkReferer != null) {
            log.info("pfpAdPvclkReferer backup != null");
            return false;
        }

        // select count
        selectCount = ((IPfpAdPvclkRefererDAO)dao).selectCountByDate(pvclkDate);
        log.info("select " + selectCount);

        // replace
        replaceCount = ((IPfpAdPvclkRefererDAO)dao).replaceSelectByDate(pvclkDate);
        log.info("replace " + replaceCount);

        if (selectCount != replaceCount) {
            return false;
        }

        // delete
        deleteCount = ((IPfpAdPvclkRefererDAO)dao).deleteByDate(pvclkDate);
        log.info("delete " + deleteCount);

        if (replaceCount != deleteCount) {
            return false;
        }

        return true;
    }
}
