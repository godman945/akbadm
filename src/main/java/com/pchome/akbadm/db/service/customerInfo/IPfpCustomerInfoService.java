package com.pchome.akbadm.db.service.customerInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.bean.ValidKeywordBean;
import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdDetail;
import com.pchome.akbadm.db.pojo.PfpAdExcludeKeyword;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.account.EnumAccountStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.manager.PfpAccountVO;

public interface IPfpCustomerInfoService extends IBaseService<PfpCustomerInfo,String>{

	public List<Object> findPfpCustomerInfo(String keyword, String customerInfoName, int page, int pageSize) throws Exception;

	public PfpCustomerInfo getCustomerInfo(String customerInfoId);

//	public List<CustomerInfoReportVO> findPfpCustomerInfosByDate(String startDate, String endDate,
//			String pfdCustomerInfoId, String orderType, String payType) throws Exception;

    public List<PfpCustomerInfo> getAllCustomerInfo() throws Exception;

    public List<Object> getCustomerInfos(String keyword, String userAccount, String userEmail, String customerInfoStatus, int page, int pageSize,String pfdCustomerId) throws Exception;

    public List<PfpCustomerInfo> getExistentCustomerInfo() throws Exception;

    public List<PfpCustomerInfo> getActivityCustomerInfo() throws Exception;

    public List<PfpCustomerInfo> findValidCustomerInfo();

    public List<PfpCustomerInfo> findValidCustomerInfo(String customerInfoId);

    public List<PfpCustomerInfo> selectValidCustomerInfo(float remain);

    public List<String> selectValidAdGroup(String type);

    public List<PfpAd> selectValidAd();

    public List<PfpAdDetail> selectValidAdDetail();

    public List<PfpAdDetail> selectValidAdDetail(Date date);

    public List<PfpAdKeyword> selectValidAdKeyword();

    public List<ValidKeywordBean> selectValidAdKeyword(List<PfpAdExcludeKeyword> excludeKeywordList, Map<String, Float> syspriceMap, String groupId, String pvclkDate);

    public List<PfpCustomerInfo> selectCustomerInfo(EnumAccountStatus enumAccountStatus);

    public float selectCustomerInfoRemain();

    public int selectCustomerInfoIdCountByActivateDate(String activateDate);

    public int selectCustomerInfoIdCountByActivateDate(EnumPfdAccountPayType enumPfdAccountPayType, String activateDate);

    public List<PfpCustomerInfo> findValidCustomerInfoByMemberId(String memberId);

    public List<PfpCustomerInfo> findPfpCustomerInfo(String pcId);

    public Integer deletePfpCustomerInfo(String pfpCustomerInfoId);

    public List<PfpAccountVO> findManagerPfpAccount(String memberId, Date startDate, Date endDate);

    public Map<String,String> findPfpCustomerInfoNameMap() throws Exception;

    public void saveOrUpdateWithCommit(PfpCustomerInfo pfpCustomerInfo) throws Exception;

    public List<PfpCustomerInfo> findCustomerInfoIds(List<String> customerInfoList) throws Exception;

    public List<String> findTransDetailPfp(String yesterday , String today , String tomorrow) throws Exception;
}
