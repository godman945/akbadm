package com.pchome.akbadm.db.service.report;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdKeywordReport;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.KeywordReportVO;

public interface IAdKeywordReportService extends IBaseService<PfdKeywordReport, String>{

	public List<KeywordReportVO> getAdKeywordReportList(String startDate, String endDate,
			String adKeywordType, String sortMode, int displayCount,
			String pfdCustomerInfoId) throws Exception;

	public List<KeywordReportVO> getAdKeywordReportDetail(String startDate, String endDate,
			String adKeywordType, String sortMode, int displayCount, String keyword,
			String pfdCustomerInfoId) throws Exception;

	public List<KeywordReportVO> getAdKeywordOfferPriceReportList(String startDate, String endDate,
			String adKeywordType, String searchText, int displayCount,
			String pfdCustomerInfoId) throws Exception;
}
