package com.pchome.akbadm.db.service.report.quartzs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.IPfdAdVideoReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdVideoReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdVideoReportService extends BaseService<PfdAdVideoReport, Integer> implements IPfdAdVideoReportService {

	public List<PfdAdVideoReport> findVideoInfoByDate(String date) throws Exception {

		List<Object> list = ((IPfdAdVideoReportDAO) dao).findVideoInfoByDate(date);
		
		List<PfdAdVideoReport> pfdAdVideoReportList = new ArrayList<PfdAdVideoReport>();
		for (Object object : list) {
			Object[] objArray = (Object[]) object;
			PfdAdVideoReport pfdAdVideoReportVO = new PfdAdVideoReport();
			pfdAdVideoReportVO.setAdVideoDate((Date)objArray[0]);
			pfdAdVideoReportVO.setAdVideoTime((int)objArray[1]);
			pfdAdVideoReportVO.setPfdCustomerInfoId((String)objArray[2]);
			pfdAdVideoReportVO.setPfdUserId((String)objArray[3]);
			pfdAdVideoReportVO.setPfpCustomerInfoId((String)objArray[4]);
			pfdAdVideoReportVO.setAdSeq((String)objArray[5]);
			pfdAdVideoReportVO.setTemplateProductSeq((String)objArray[6]);
			pfdAdVideoReportVO.setAdPriceType((String)objArray[7]);
			pfdAdVideoReportVO.setAdPvclkDevice((String)objArray[8]);
			pfdAdVideoReportVO.setAdVpv(((BigDecimal)objArray[9]).intValue());
			pfdAdVideoReportVO.setAdPv(((BigDecimal)objArray[10]).intValue());
			pfdAdVideoReportVO.setAdClk(((BigDecimal)objArray[11]).intValue());
			pfdAdVideoReportVO.setAdView(((BigDecimal)objArray[12]).intValue());
			pfdAdVideoReportVO.setAdPrice(((Double)objArray[13]).floatValue());
			pfdAdVideoReportVO.setAdVideoPlay(((BigDecimal)objArray[14]).intValue());
			pfdAdVideoReportVO.setAdVideoMusic(((BigDecimal)objArray[15]).intValue());
			pfdAdVideoReportVO.setAdVideoReplay(((BigDecimal)objArray[16]).intValue());
			pfdAdVideoReportVO.setAdVideoProcess25(((BigDecimal)objArray[17]).intValue());
			pfdAdVideoReportVO.setAdVideoProcess50(((BigDecimal)objArray[18]).intValue());
			pfdAdVideoReportVO.setAdVideoProcess75(((BigDecimal)objArray[19]).intValue());
			pfdAdVideoReportVO.setAdVideoProcess100(((BigDecimal)objArray[20]).intValue());
			pfdAdVideoReportList.add(pfdAdVideoReportVO);
		}
		
		return pfdAdVideoReportList;
	}

	public int deleteVideoReportDataBytDate(String date) throws Exception {
		return ((IPfdAdVideoReportDAO) dao).deleteVideoReportDataBytDate(date);
	}

	public int addVideoReportDataBytDate(List<PfdAdVideoReport> list) throws Exception {
		return ((IPfdAdVideoReportDAO) dao).addVideoReportDataBytDate(list);
	}

}
