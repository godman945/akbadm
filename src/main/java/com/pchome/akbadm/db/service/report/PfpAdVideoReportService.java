package com.pchome.akbadm.db.service.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdmUniqDataDAO;
import com.pchome.akbadm.db.dao.report.IPfpAdVideoReportDAO;
import com.pchome.akbadm.db.pojo.AdmUniqData;
import com.pchome.akbadm.db.pojo.PfpAdVideoReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public class PfpAdVideoReportService extends BaseService<PfpAdVideoReport, String> implements IPfpAdVideoReportService {
    private IAdmUniqDataDAO admUniqDataDAO;

    @Override
    public int selectCountByCondition(Map<String, Object> conditionMap) {
        return ((IPfpAdVideoReportDAO) dao).selectCountByCondition(conditionMap);
    }

    @Override
    public List<PfpAdVideoReportVO> selectByCondition(Map<String, Object> conditionMap, int firstResult, int maxResults) {
        List<PfpAdVideoReportVO> voList = new ArrayList<>();

        List<Object[]> objectsList = ((IPfpAdVideoReportDAO) dao).selectByCondition(conditionMap, firstResult, maxResults);
        PfpAdVideoReportVO vo = null;
        List<AdmUniqData> admUniqDateList = null;
        for (Object[] objects: objectsList) {
            vo = new PfpAdVideoReportVO();
            vo.setAdVideoDate((Date) objects[0]);
            vo.setPfdCustomerInfoId((String) objects[1]);
            vo.setPfdCustomerInfoName((String) objects[2]);
            vo.setPfpCustomerInfoId((String) objects[3]);
            vo.setPfpCustomerInfoName((String) objects[4]);
            vo.setAdId((String) objects[5]);
            vo.setTproId((String) objects[6]);
            vo.setTproWidth((String) objects[7]);
            vo.setTproHeight((String) objects[8]);
            vo.setAdPriceType((String) objects[9]);
            vo.setAdDevice((String) objects[10]);
            if (conditionMap.get("adDevice") == null) {
                vo.setAdDevice("全部");
            }
            vo.setAdPv(((BigDecimal) objects[11]).intValue());
            vo.setAdClk(((BigDecimal) objects[12]).intValue());
            vo.setAdVpv(((BigDecimal) objects[13]).intValue());
            vo.setAdView(((BigDecimal) objects[14]).intValue());
            BigDecimal costBigDecimal = new BigDecimal(objects[15].toString()).setScale(2,BigDecimal.ROUND_FLOOR);
            vo.setAdPrice(costBigDecimal.floatValue());
            if (vo.getAdPv() > 0) {
                vo.setAdPvPriceAvg(vo.getAdPrice()*1000 / vo.getAdPv());
            }
            if (vo.getAdPv() > 0) {
                vo.setAdClkRate(vo.getAdClk()*100f / vo.getAdPv());
            }
            if (vo.getAdPv() > 0) {
                vo.setAdVpvRate(vo.getAdVpv()*100f / vo.getAdPv());
            }
            if (vo.getAdVpv() > 0) {
                vo.setAdVpvPrice(vo.getAdPrice() / vo.getAdVpv());
            }
            if (vo.getAdPv() > 0) {
                vo.setAdViewRate(vo.getAdView()*100f / vo.getAdPv());
            }
            if (vo.getAdView() > 0) {
                vo.setAdViewPrice(vo.getAdPrice() / vo.getAdView());
            }
            vo.setAdVideoPlay(((BigDecimal) objects[16]).intValue());
            vo.setAdVideoMusic(((BigDecimal) objects[17]).intValue());
            vo.setAdVideoReplay(((BigDecimal) objects[18]).intValue());
            vo.setAdVideoProcess25(((BigDecimal) objects[19]).intValue());
            vo.setAdVideoProcess50(((BigDecimal) objects[20]).intValue());
            vo.setAdVideoProcess75(((BigDecimal) objects[21]).intValue());
            vo.setAdVideoProcess100(((BigDecimal) objects[22]).intValue());
//            vo.setAdVideoUniq(((BigDecimal) objects[23]).intValue());
            vo.setAdVideoIdc(((BigDecimal) objects[24]).intValue());
            if (vo.getAdPv() > 0) {
                vo.setAdVideoProcessRate(vo.getAdVideoProcess100()*100f / vo.getAdPv());
            }

            admUniqDateList = admUniqDataDAO.select(vo.getAdVideoDate(), vo.getAdId());
            if (admUniqDateList != null) {
                for (AdmUniqData admUniqData: admUniqDateList) {
                    vo.setAdVideoUniq(admUniqData.getUniqCount());
                    break;
                }
            }

            voList.add(vo);
        }

        return voList;
    }

    public void setAdmUniqDataDAO(IAdmUniqDataDAO admUniqDataDAO) {
        this.admUniqDataDAO = admUniqDataDAO;
    }
}
