package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdGroupViewVO;
import com.pchome.enumerate.ad.EnumAdStatus;


public interface IPfpAdGroupService extends IBaseService<PfpAdGroup,String>{
	
	public List<PfpAdGroup> getAllPfpAdGroups() throws Exception;

	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupName, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception;

	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception;
	
	public String getCount(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception;

	public List<PfpAdGroup> getPfpAdGroups(String adGroupSeq, String adActionSeq, String adGroupName, String adGroupSearchPrice, String adGroupChannelPrice, String adGroupStatus) throws Exception;
	
	public boolean chkAdGroupNameByAdActionSeq(String adGroupName, String adGroupSeq, String adActionSeq) throws Exception;
	
	public PfpAdGroup getPfpAdGroupBySeq(String adGroupSeq) throws Exception;
	
	public void insertPfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception;
	
	public void updatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception;
	
	public void updatePfpAdGroupStatus(String pfpAdGroupStatus, String adGroupSeq) throws Exception;
	
	public void savePfpAdGroup(PfpAdGroup adGroup) throws Exception;
	
	public void saveOrUpdateWithCommit(PfpAdGroup adGroup) throws Exception;
	
	public List<PfpAdGroupViewVO> getAdGroupView(String userAccount, String adStatus, int adType, String keyword, String adActionSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception;

	public List<Object> getAllAdGroupView(String userAccount, String adStatus, int adType, String keyword, String adActionSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception;

	public List<PfpAdGroup> validAdGroup(String adActionSeq) throws Exception; 
	
	public List<PfpAdGroup> findAdGroup(EnumAdStatus enumAdStatus);
	
	public List<PfpAdGroupViewVO> getAdGroupReport(Map<String,String> adGroupConditionMap) throws Exception;
	    
	public int getAdGroupReportSize(Map<String,String> adGroupConditionMap) throws Exception;
}
