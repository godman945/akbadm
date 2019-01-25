package com.pchome.akbadm.db.dao.ad;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.vo.ad.PfpAdActionViewVO;

public interface IPfpAdActionDAO extends IBaseDAO<PfpAdAction, String> {
    public PfpAdAction getAdAction(String adActionSeq);

	public List<Object> getAdActionPvclk(final String userAccount, final String adStatus, final int adType, final String adActionName, final String adPvclkDevice, final String dateType, final Date startDate, final Date endDate, final int page, final int pageSize) throws Exception;

    public List<PfpAdAction> getValidAdAction(String customerInfoId);

    //改善廣告報表效能
    public List<Object> getAdActionReport(Map<String,String> adActionConditionMap);
    
    public int getAdActionReportSize(Map<String,String> adActionConditionMap) throws Exception;
    
    public List<PfpAdAction> selectValidAdAction(Date date);

    public List<PfpAdAction> selectAdActionByStatus(int status);

    public float selectActionMaxSum();

    public List<PfpAdAction> getAdActionByConditions(Map<String, String> conditionMap) throws Exception;
    
    public List<PfpAdAction> findBroadcastAdAction(String customerInfoId);

	// 2014-04-24 
	public HashMap<String, PfpAdAction> getPfpAdActionsBySeqList(List<String> adActionSeqList) throws Exception;
	
	// 2014-04-24
	public HashMap<String, PfpAdAction> getPfpAdActionsByActionDate(String startDate, String endDate) throws Exception;
}