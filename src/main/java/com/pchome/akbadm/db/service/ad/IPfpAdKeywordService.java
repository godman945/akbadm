package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdKeywordViewVO;

public interface IPfpAdKeywordService extends IBaseService<PfpAdKeyword, String>{
	
	public PfpAdKeyword findPfpAdKeywordBySeq(String adKeywordSeq) throws Exception;
	
	public void updatePfpAdKeyword(PfpAdKeyword pfpAdKeyword) throws Exception;
	
	public float countKeywordSysprice(String keyword, float sysprice, String today) throws Exception;

	public List<Object> getAllAdKeywordView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adKeywordPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception;

	public List<PfpAdKeywordViewVO> getAdKeywordView(String userAccount, String searchAdStatus, int adType, String keyword, String adGroupSeq, String adKeywordPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception;

	public List<PfpAdKeywordViewVO> getAdKeywordViewReport(Map<String,String> adKeywordViewConditionMap) throws Exception;

	public PfpAdKeywordViewVO getAdKeywordViewReportSize(Map<String,String> adKeywordViewConditionMap) throws Exception;
}
