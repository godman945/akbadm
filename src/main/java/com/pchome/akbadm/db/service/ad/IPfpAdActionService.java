package com.pchome.akbadm.db.service.ad;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ad.PfpAdActionViewVO;

public interface IPfpAdActionService extends IBaseService<PfpAdAction, String> {
    public void updatePfpAdAction(PfpAdAction pfpAdAction);

    public PfpAdAction getAdAction(String adActionSeq);

    public List<PfpAdActionViewVO> getAdActionView(String userAccount, String adStatus, int adType, String adActionName, String adPvclkDevice, String dateType, Date startDate, Date endDate, int page, int pageSize) throws Exception;

    //改善廣告報表效能
    public List<PfpAdActionViewVO> getAdActionReport(Map<String,String> adActionConditionMap) throws Exception;
    
    public int getAdActionReportSize(Map<String,String> adActionConditionMap) throws Exception;
    
    public List<Object> getAllAdActionView(String userAccount, String adStatus, int adType, String adActionName, String adPvclkDevice, String dateType, Date startDate, Date endDate) throws Exception;

    public List<PfpAdAction> getValidAdAction(String customerInfoId);

    public List<PfpAd> selectValidAd();

    public List<PfpAdDetail> selectValidAdDetail();

    public List<PfpAdKeyword> selectValidAdKeyword();

    public int selectAdUnderMaxByPvclkDate(String pvclkDate);

    public List<PfpAdAction> selectAdActionByStatus(int status);

    public float selectActionMaxSum();

    public float selectReachRate(String pvclkDate);

    public List<PfpAdAction> getAdActionByConditions(Map<String, String> conditionMap) throws Exception;
    
    public List<PfpAdAction> findBroadcastAdAction(String customerInfoId);
}
