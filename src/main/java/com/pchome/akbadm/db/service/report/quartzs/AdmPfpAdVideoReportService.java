package com.pchome.akbadm.db.service.report.quartzs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.IAdmPfpAdVideoReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdVideoReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;

public class AdmPfpAdVideoReportService extends BaseService<PfpAdVideoReport, Integer>
		implements IAdmPfpAdVideoReportService {

	public List<PfpAdVideoReportVO> findVideoInfoByDate(final String date) throws Exception {
		List<PfpAdVideoReportVO> pfpAdVideoReportVOList = new ArrayList<>();
		List<Object> list = ((IAdmPfpAdVideoReportDAO) dao).findVideoInfoByDate(date);

		for (Object object : list) {
			Object[] objArray = (Object[]) object;
			PfpAdVideoReportVO pfpAdVideoReportVO = new PfpAdVideoReportVO();
			pfpAdVideoReportVO.setCustomerInfoId((String) objArray[0]);
			pfpAdVideoReportVO.setPfdCustomerInfoId((String) objArray[1]);
			pfpAdVideoReportVO.setTemplateProductSeq((String) objArray[2]);
			pfpAdVideoReportVO.setAdSeq((String) objArray[3]);
			pfpAdVideoReportVO.setAdVideoDate((Date) objArray[4]);
			pfpAdVideoReportVO.setAdVideoTime((int) objArray[5]);
			pfpAdVideoReportVO.setAdPvclkDevice((String) objArray[6]);
			pfpAdVideoReportVO.setAdPriceType((String) objArray[7]);
			pfpAdVideoReportVO.setAdPv(((BigDecimal) objArray[8]).intValue());
			pfpAdVideoReportVO.setAdVpv(((BigDecimal) objArray[9]).intValue());
			pfpAdVideoReportVO.setAdClk(((BigDecimal) objArray[10]).intValue());
			pfpAdVideoReportVO.setAdView(((BigDecimal) objArray[11]).intValue());
			pfpAdVideoReportVO.setAdPrice(Float.valueOf(String.valueOf(objArray[12])));
			pfpAdVideoReportVO.setAdVideoPlay(((BigDecimal) objArray[13]).intValue());
			pfpAdVideoReportVO.setAdVideoMusic(((BigDecimal) objArray[14]).intValue());
			pfpAdVideoReportVO.setAdVideoReplay(((BigDecimal) objArray[15]).intValue());
			pfpAdVideoReportVO.setAdVideoProcess25(((BigDecimal) objArray[16]).intValue());
			pfpAdVideoReportVO.setAdVideoProcess50(((BigDecimal) objArray[17]).intValue());
			pfpAdVideoReportVO.setAdVideoProcess75(((BigDecimal) objArray[18]).intValue());
			pfpAdVideoReportVO.setAdVideoProcess100(((BigDecimal) objArray[19]).intValue());
			pfpAdVideoReportVO.setAdVideoIdc(((BigDecimal) objArray[20]).intValue());
			
			if(pfpAdVideoReportVO.getAdView() < pfpAdVideoReportVO.getAdVideoProcess25()){
				pfpAdVideoReportVO.setAdVideoProcess25(pfpAdVideoReportVO.getAdView());
			}
			if(pfpAdVideoReportVO.getAdVideoProcess25() < pfpAdVideoReportVO.getAdVideoProcess50()){
				pfpAdVideoReportVO.setAdVideoProcess50(pfpAdVideoReportVO.getAdVideoProcess25());
			}
			if(pfpAdVideoReportVO.getAdVideoProcess50() < pfpAdVideoReportVO.getAdVideoProcess75()){
				pfpAdVideoReportVO.setAdVideoProcess75(pfpAdVideoReportVO.getAdVideoProcess50());
			}
			if(pfpAdVideoReportVO.getAdVideoProcess75() < pfpAdVideoReportVO.getAdVideoProcess100()){
				pfpAdVideoReportVO.setAdVideoProcess100(pfpAdVideoReportVO.getAdVideoProcess75());
			}
			
			pfpAdVideoReportVOList.add(pfpAdVideoReportVO);
		}
		return pfpAdVideoReportVOList;
	}

	public int deleteVideoReportDataBytDate(String date) throws Exception {
		return ((IAdmPfpAdVideoReportDAO) dao).deleteVideoReportDataBytDate(date);
	}

	public int addVideoReportDataBytDate(List<PfpAdVideoReportVO> list) throws Exception {
		return ((IAdmPfpAdVideoReportDAO) dao).addVideoReportDataBytDate(list);
	}

}
