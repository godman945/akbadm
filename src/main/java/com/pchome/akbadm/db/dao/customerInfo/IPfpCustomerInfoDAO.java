package com.pchome.akbadm.db.dao.customerInfo;

import java.util.HashMap;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public interface IPfpCustomerInfoDAO extends IBaseDAO<PfpCustomerInfo,String> {

	public List<Object> findPfpCustomerInfo(String keyword, String customerInfoName, int page, int pageSize) throws Exception;

	public PfpCustomerInfo getCustomerInfo(String customerInfoId);

	public List<PfpCustomerInfo> findPfpCustomerInfosByDate(String startDate, String endDate,
			String pfpCustInfoId, String status) throws Exception;

	public List<PfpCustomerInfo> findPfpCustomerInfosByDate(String startDate, String endDate, List<String> pfpCustomerInfoIds, List<String> status) throws Exception;

    public List<PfpCustomerInfo> getAllCustomerInfo() throws Exception;

    public List<Object> getCustomerInfos(String keyword, String userAccount, String userEmail, String customerInfoStatus, int page, int pageSize,String pfdCustomerId) throws Exception;

    public List<PfpCustomerInfo> getExistentCustomerInfo();

    public List<PfpCustomerInfo> findValidCustomerInfo();

    public List<PfpCustomerInfo> findValidCustomerInfo(String customerInfoId);

    public List<PfpCustomerInfo> selectValidCustomerInfo(float remain);

    public List<Object[]> selectValidAdGroup(String pvclkDate, float remain);

    public List<Object[]> selectValidAdKeyword(String groupId, String pvclkDate, float remain);

    public List<PfpCustomerInfo> selectCustomerInfo(EnumAccountStatus enumAccountStatus);

    public float selectCustomerInfoRemain();

    public int selectCustomerInfoIdCountByActivateDate(String activateDate);

    public int selectCustomerInfoIdCountByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, String activateDate);

    public List<PfpCustomerInfo> findValidCustomerInfoByMemberId(String memberId);

    // 2014-04-24
    public HashMap<String, PfpCustomerInfo> getPfpCustomerInfoBySeqList(List<String> customerInfoIdList) throws Exception;

    //2014-05-02
    public HashMap<String, PfpCustomerInfo> getPfpCustomerInfoBySeqList(String startDate, String endDate, List<String> pfpCustomerInfoIds, List<String> status) throws Exception;

    public List<PfpCustomerInfo> findPfpCustomerInfo(String pcId);

    public Integer deletePfpCustomerInfo(String pfpCustomerInfoId);

    public List<Object> findManagerPfpCustomerInfo(String memberId);

    public int updateLaterRemainLessThanAdCost(String pvclkDate);

    public int updateLaterRemainLessThanAdKeywordCost(String pvclkDate);
    
    public void saveOrUpdateWithCommit(PfpCustomerInfo PfpCustomerInfo) throws Exception;
    
    public List<PfpCustomerInfo> findCustomerInfoIds(List<String> customerInfoList) throws Exception;
    
    public List<Object> findTransDetailPfp(String yesterday , String today , String tomorrow) throws Exception;
}
