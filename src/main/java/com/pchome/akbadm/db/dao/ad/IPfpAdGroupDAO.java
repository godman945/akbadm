package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.enumerate.ad.EnumAdStatus;

public interface IPfpAdGroupDAO extends IBaseDAO<PfpAdGroup,String>{
	
	public List<PfpAdGroup> getPfpAdGroups(String adGroupSeq, String adActionSeq, String adGroupName, String adGroupSearchPrice, String adGroupChannelPrice, String adGroupStatus) throws Exception;

	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupName, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception;

	public List<Object> findAdGroupView(String adActionSeq, String adType, String adGroupSeq, String adGroupName, String adGroupStatus, String startDate, String endDate, int page, int pageSize, String customerInfoId) throws Exception;

	public boolean chkAdGroupNameByAdActionSeq(String adGroupName, String adGroupSeq, String adActionSeq) throws Exception;
	
	public PfpAdGroup getPfpAdGroupBySeq(String adGroupSeq) throws Exception;
	
	public void saveOrUpdatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception;
	
	public void insertPfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception;
	
	public void updatePfpAdGroup(PfpAdGroup pfpAdGroup) throws Exception;
	
	public void updatePfpAdGroupStatus(String pfpAdGroupStatus, String adGroupSeq) throws Exception;
	
	public void saveOrUpdateWithCommit(PfpAdGroup adGroup) throws Exception;
	
	public List<Object> getAdGroupPvclk(String userAccount, String adStatus, int adType, String keyword, String adActionSeq, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception;

	// 2014-04-24 
	public HashMap<String, PfpAdGroup> getPfpAdGroupsBySeqList(List<String> adGroupSeqList) throws Exception;

	public List<PfpAdGroup> validAdGroup(String adActionSeq) throws Exception;
	
	public List<PfpAdGroup> findAdGroup(int statusId);
	
	public List<Object> getAdGroupReport(Map<String,String> adGroupConditionMap) throws Exception;
	    
	public int getAdGroupReportSize(Map<String,String> adGroupConditionMap) throws Exception;
}
